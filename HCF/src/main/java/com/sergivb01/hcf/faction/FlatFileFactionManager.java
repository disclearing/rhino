package com.sergivb01.hcf.faction;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.database.mongo.MongoManager;
import com.sergivb01.hcf.faction.claim.Claim;
import com.sergivb01.hcf.faction.event.*;
import com.sergivb01.hcf.faction.event.cause.ClaimChangeCause;
import com.sergivb01.hcf.faction.struct.ChatChannel;
import com.sergivb01.hcf.faction.struct.Relation;
import com.sergivb01.hcf.faction.struct.Role;
import com.sergivb01.hcf.faction.type.*;
import com.sergivb01.hcf.utils.config.ConfigurationService;
import com.sergivb01.util.Config;
import com.sergivb01.util.JavaUtils;
import com.sergivb01.util.cuboid.CoordinatePair;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.craftbukkit.v1_7_R4.util.LongHash;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FlatFileFactionManager implements Listener, FactionManager{
	private final WarzoneFaction warzone;
	private final WildernessFaction wilderness;
	private Table<String, Long, Claim> claimPositionMap = HashBasedTable.create();
	private final Map<UUID, UUID> factionPlayerUuidMap;
	private final Map<UUID, Faction> factionUUIDMap;
	private final Map<String, UUID> factionNameMap;
	private final HCF plugin;
	private Config config;

	public FlatFileFactionManager(final HCF plugin){
		super();
		this.factionPlayerUuidMap = new ConcurrentHashMap<>();
		this.factionUUIDMap = new HashMap<>();
		this.factionNameMap = new CaseInsensitiveMap<>();
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.warzone = new WarzoneFaction();
		this.wilderness = new WildernessFaction();
		this.reloadFactionData();
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerJoinedFaction(final PlayerJoinedFactionEvent event){
		this.factionPlayerUuidMap.put(event.getUniqueID(), event.getFaction().getUniqueID());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerLeftFaction(final PlayerLeftFactionEvent event){
		this.factionPlayerUuidMap.remove(event.getUniqueID());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onFactionRename(final FactionRenameEvent event){
		this.factionNameMap.remove(event.getOriginalName());
		this.factionNameMap.put(event.getNewName(), event.getFaction().getUniqueID());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onFactionClaim(final FactionClaimChangedEvent event){
		for(final Claim claim : event.getAffectedClaims()){
			this.cacheClaim(claim, event.getCause());
		}
	}

	@Deprecated
	public Map<String, UUID> getFactionNameMap(){
		return this.factionNameMap;
	}

	public List<Faction> getFactions(){
		final List<Faction> asd = new ArrayList<Faction>();
		for(final Faction fac : this.factionUUIDMap.values()){
			asd.add(fac);
		}
		return asd;
	}

	public Claim getClaimAt(final World world, final int x, final int z){
		return this.claimPositionMap.get(world.getName(), LongHash.toLong(x, z));
	}

	public Claim getClaimAt(final Location location){
		return this.getClaimAt(location.getWorld(), location.getBlockX(), location.getBlockZ());
	}

	public Faction getFactionAt(final World world, final int x, final int z){
		final World.Environment environment = world.getEnvironment();
		final Claim claim = this.getClaimAt(world, x, z);
		if(claim != null){
			final Faction faction = claim.getFaction();
			if(faction != null){
				return faction;
			}
		}
		if(environment == World.Environment.THE_END){
			return this.warzone;
		}
		int warzoneRadius = ConfigurationService.WARZONE_RADIUS * (ConfigurationService.BORDER_SIZES.get(environment) / ConfigurationService.BORDER_SIZES.get(World.Environment.NORMAL));
		return (Math.abs(x) > warzoneRadius || Math.abs(z) > warzoneRadius) ? this.wilderness : this.warzone;
	}

	public Faction getFactionAt(final Location location){
		return this.getFactionAt(location.getWorld(), location.getBlockX(), location.getBlockZ());
	}

	public Faction getFactionAt(final Block block){
		return this.getFactionAt(block.getLocation());
	}

	public Faction getFaction(final String factionName){
		final UUID uuid = this.factionNameMap.get(factionName);
		return (uuid == null) ? null : this.factionUUIDMap.get(uuid);
	}

	public Faction getFaction(final UUID factionUUID){
		return this.factionUUIDMap.get(factionUUID);
	}

	public PlayerFaction getPlayerFaction(final UUID playerUUID){
		final UUID uuid = this.factionPlayerUuidMap.get(playerUUID);
		final Faction faction = (uuid == null) ? null : this.factionUUIDMap.get(uuid);
		return (faction instanceof PlayerFaction) ? ((PlayerFaction) faction) : null;
	}

	public PlayerFaction getPlayerFaction(final Player player){
		return this.getPlayerFaction(player.getUniqueId());
	}

	public PlayerFaction getContainingPlayerFaction(final String search){
		final OfflinePlayer target = JavaUtils.isUUID(search) ? Bukkit.getOfflinePlayer(UUID.fromString(search)) : Bukkit.getOfflinePlayer(search);
		return (target.hasPlayedBefore() || target.isOnline()) ? this.getPlayerFaction(target.getUniqueId()) : null;
	}

	public Faction getContainingFaction(final String search){
		final Faction faction = this.getFaction(search);
		if(faction != null){
			return faction;
		}
		final UUID playerUUID = Bukkit.getOfflinePlayer(search).getUniqueId();
		if(playerUUID != null){
			return this.getPlayerFaction(playerUUID);
		}
		return null;
	}

	public boolean containsFaction(final Faction faction){
		return this.factionNameMap.containsKey(faction.getName());
	}

	public boolean createFaction(final Faction faction){
		return this.createFaction(faction, Bukkit.getConsoleSender());
	}

	public boolean createFaction(final Faction faction, final CommandSender sender){
		if(this.factionUUIDMap.putIfAbsent(faction.getUniqueID(), faction) != null){
			return false;
		}
		this.factionNameMap.put(faction.getName(), faction.getUniqueID());
		if(faction instanceof PlayerFaction && sender instanceof Player){
			final Player player = (Player) sender;
			final PlayerFaction playerFaction = (PlayerFaction) faction;
			if(!playerFaction.setMember(player, new FactionMember(player, ChatChannel.PUBLIC, Role.LEADER))){
				return false;
			}
		}
		final FactionCreateEvent createEvent = new FactionCreateEvent(faction, sender);
		Bukkit.getPluginManager().callEvent(createEvent);
		return !createEvent.isCancelled();
	}

	public boolean removeFaction(final Faction faction, final CommandSender sender){
		if(this.factionUUIDMap.remove(faction.getUniqueID()) == null){
			return false;
		}
		this.factionNameMap.remove(faction.getName());
		final FactionRemoveEvent removeEvent = new FactionRemoveEvent(faction, sender);
		Bukkit.getPluginManager().callEvent(removeEvent);
		if(removeEvent.isCancelled()){
			return false;
		}
		if(faction instanceof ClaimableFaction){
			Bukkit.getPluginManager().callEvent(new FactionClaimChangedEvent(sender, ClaimChangeCause.UNCLAIM, ((ClaimableFaction) faction).getClaims()));
		}
		if(faction instanceof PlayerFaction){
			final PlayerFaction playerFaction = (PlayerFaction) faction;
			for(final PlayerFaction ally : playerFaction.getAlliedFactions()){
				Bukkit.getPluginManager().callEvent(new FactionRelationRemoveEvent(playerFaction, ally, Relation.ENEMY));
				ally.getRelations().remove(faction.getUniqueID());
			}
		}
		if(faction instanceof PlayerFaction){
			final PlayerFaction playerFaction = (PlayerFaction) faction;
			for(final PlayerFaction ally : playerFaction.getAlliedFactions()){
				ally.getRelations().remove(faction.getUniqueID());
			}
			for(final UUID uuid : playerFaction.getMembers().keySet()){
				playerFaction.setMember(uuid, null, true);
			}
		}
		return true;
	}


	private void cacheClaim(Claim claim, ClaimChangeCause cause) {
		Preconditions.checkNotNull(claim, "Claim cannot be null");
		Preconditions.checkNotNull(cause, "Cause cannot be null");
		Preconditions.checkArgument(cause != ClaimChangeCause.RESIZE, "Cannot cache claims of resize type");

		World world = claim.getWorld();
		if (world == null) {
			return; // safe-guard if Nether or End is disabled for example
		}

		int minX = Math.min(claim.getX1(), claim.getX2());
		int maxX = Math.max(claim.getX1(), claim.getX2());
		int minZ = Math.min(claim.getZ1(), claim.getZ2());
		int maxZ = Math.max(claim.getZ1(), claim.getZ2());
		for (int x = minX; x <= maxX; x++) {
			for (int z = minZ; z <= maxZ; z++) {
				if (cause == ClaimChangeCause.CLAIM) {
					this.claimPositionMap.put(world.getName(), LongHash.toLong(x, z), claim);
				} else if (cause == ClaimChangeCause.UNCLAIM) {
					this.claimPositionMap.remove(world.getName(), LongHash.toLong(x, z));
				}
			}
		}
	}

	private void cacheFaction(final Faction faction){
		this.factionNameMap.put(faction.getName(), faction.getUniqueID());
		this.factionUUIDMap.put(faction.getUniqueID(), faction);
		if(faction instanceof ClaimableFaction){
			final ClaimableFaction claimableFaction = (ClaimableFaction) faction;
			for(final Claim claim : claimableFaction.getClaims()){
				this.cacheClaim(claim, ClaimChangeCause.CLAIM);
			}
		}
		if(faction instanceof PlayerFaction){
			for(final FactionMember factionMember : ((PlayerFaction) faction).getMembers().values()){
				this.factionPlayerUuidMap.put(factionMember.getUniqueId(), faction.getUniqueID());
			}
		}
	}

	public void reloadFactionData(){
		this.factionNameMap.clear();
		this.config = new Config(this.plugin, "factions");
		final Object object = this.config.get("factions");
		if(object instanceof MemorySection){
			final MemorySection section = (MemorySection) object;
			for(final String factionName : section.getKeys(false)){
				final Object next = this.config.get(section.getCurrentPath() + '.' + factionName);
				if(next instanceof Faction){
					this.cacheFaction((Faction) next);
				}
			}
		}else if(object instanceof List){
			final List list = (List) object;
			for(final Object next2 : list){
				if(next2 instanceof Faction){
					this.cacheFaction((Faction) next2);
				}
			}
		}
		final Set<Faction> adding = new HashSet<Faction>();
		if(!this.factionNameMap.containsKey("Warzone")){
			adding.add(new WarzoneFaction());
		}
		if(!this.factionNameMap.containsKey("Glowstone")){
			adding.add(new GlowstoneFaction());
		}
		if (!factionNameMap.containsKey("Spawn")) {
			adding.add(new SpawnFaction());
		}
		if(!this.factionNameMap.containsKey("NorthRoad")){
			adding.add(new RoadFaction.NorthRoadFaction());
		}
		if(!this.factionNameMap.containsKey("EastRoad")){
			adding.add(new RoadFaction.EastRoadFaction());
		}
		if(!this.factionNameMap.containsKey("WestRoad")){
			adding.add(new RoadFaction.WestRoadFaction());
		}
		if(!this.factionNameMap.containsKey("SouthRoad")){
			adding.add(new RoadFaction.SouthRoadFaction());
		}
		if(!this.factionNameMap.containsKey("EndPortal")){
			adding.add(new EndPortalFaction());
		}
		if(!this.factionNameMap.containsKey("Wilderness")){
			adding.add(new WildernessFaction());
		}
		for(Faction added : adding){
			this.cacheFaction(added);
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Faction " + added.getName() + " not found, created.");
		}
	}

	public void saveFactionData(){
		this.config.set("factions", new ArrayList<>(this.factionUUIDMap.values()));

		if(ConfigurationService.MONGO_ENABLED){
			factionUUIDMap.values().stream()
					.filter(faction -> faction instanceof PlayerFaction)
					.map(faction -> (PlayerFaction) faction)
					.forEach(MongoManager::saveFaction);
		}
		this.config.save();
	}
}

