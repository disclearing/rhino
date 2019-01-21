package com.sergivb01.base.user;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.net.InetAddresses;
import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.StaffPriority;
import com.sergivb01.base.event.PlayerVanishEvent;
import com.sergivb01.base.kit.Kit;
import com.sergivb01.util.GenericUtils;
import com.sergivb01.util.PersistableLocation;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import net.minecraft.util.gnu.trove.map.TObjectIntMap;
import net.minecraft.util.gnu.trove.map.TObjectLongMap;
import net.minecraft.util.gnu.trove.map.hash.TObjectIntHashMap;
import net.minecraft.util.gnu.trove.map.hash.TObjectLongHashMap;
import net.minecraft.util.gnu.trove.procedure.TObjectIntProcedure;
import net.minecraft.util.gnu.trove.procedure.TObjectLongProcedure;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftItem;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class BaseUser extends ServerParticipator{
	private final List<String> addressHistories;
	private final List<NameHistory> nameHistories;
	private final TObjectIntMap<UUID> kitUseMap;
	private final TObjectLongMap<UUID> kitCooldownMap;
	private List<String> notes;
	private Location backLocation;
	private boolean messagingSounds;
	private boolean hasStarter;
	private boolean staffutil;
	private boolean vanished;
	private boolean glintEnabled;
	private long lastGlintUse;

	public BaseUser(final UUID uniqueID){
		super(uniqueID);
		this.notes = new ArrayList<String>();
		this.hasStarter = false;
		this.staffutil = false;
		this.addressHistories = new ArrayList<String>();
		this.nameHistories = new ArrayList<NameHistory>();
		this.glintEnabled = true;
		this.kitUseMap = (TObjectIntMap<UUID>) new TObjectIntHashMap();
		this.kitCooldownMap = (TObjectLongMap<UUID>) new TObjectLongHashMap();
	}

	public BaseUser(final Map<String, Object> map){
		super(map);
		this.notes = new ArrayList<String>();
		this.addressHistories = new ArrayList<String>();
		this.nameHistories = new ArrayList<NameHistory>();
		this.glintEnabled = true;
		this.kitUseMap = (TObjectIntMap<UUID>) new TObjectIntHashMap();
		this.kitCooldownMap = (TObjectLongMap<UUID>) new TObjectLongHashMap();
		this.notes.addAll(GenericUtils.createList(map.get("notes"), String.class));
		this.addressHistories.addAll(GenericUtils.createList(map.get("addressHistories"), String.class));
		Object object = map.get("nameHistories");
		if(object != null){
			this.nameHistories.addAll(GenericUtils.createList(object, NameHistory.class));
		}
		if((object = map.get("backLocation")) instanceof PersistableLocation){
			final PersistableLocation persistableLocation = (PersistableLocation) object;
			if(persistableLocation.getWorld() != null){
				this.backLocation = ((PersistableLocation) object).getLocation();
			}
		}
		if((object = map.get("staffmode")) instanceof Boolean){
			this.staffutil = (boolean) object;
		}
		if((object = map.get("starter")) instanceof Boolean){
			this.hasStarter = (boolean) object;
		}
		if((object = map.get("messagingSounds")) instanceof Boolean){
			this.messagingSounds = (boolean) object;
		}
		if((object = map.get("vanished")) instanceof Boolean){
			this.vanished = (boolean) object;
		}
		if((object = map.get("glintEnabled")) instanceof Boolean){
			this.glintEnabled = (boolean) object;
		}
		if((object = map.get("lastGlintUse")) instanceof String){
			this.lastGlintUse = Long.parseLong((String) object);
		}
		for(final Map.Entry<String, Integer> entry : GenericUtils.castMap(map.get("kit-use-map"), String.class, Integer.class).entrySet()){
			this.kitUseMap.put(UUID.fromString(entry.getKey()), (int) entry.getValue());
		}
		for(final Map.Entry<String, String> entry2 : GenericUtils.castMap(map.get("kit-cooldown-map"), String.class, String.class).entrySet()){
			this.kitCooldownMap.put(UUID.fromString(entry2.getKey()), Long.parseLong(entry2.getValue()));
		}
	}

	@Override
	public Map<String, Object> serialize(){
		final Map<String, Object> map = super.serialize();
		map.put("addressHistories", this.addressHistories);
		map.put("notes", this.notes);
		map.put("staffmode", this.staffutil);
		map.put("starter", this.hasStarter);
		map.put("nameHistories", this.nameHistories);
		if(this.backLocation != null && this.backLocation.getWorld() != null){
			map.put("backLocation", new PersistableLocation(this.backLocation));
		}
		map.put("messagingSounds", this.messagingSounds);
		map.put("vanished", this.vanished);
		map.put("glintEnabled", this.glintEnabled);
		map.put("lastGlintUse", Long.toString(this.lastGlintUse));
		final Map<String, Integer> kitUseSaveMap = new HashMap<String, Integer>(this.kitUseMap.size());
		this.kitUseMap.forEachEntry((uuid, value) -> {
			kitUseSaveMap.put(uuid.toString(), value);
			return true;
		});
		new TObjectIntProcedure<UUID>(){
			public boolean execute(final UUID uuid, final int value){
				kitUseSaveMap.put(uuid.toString(), value);
				return true;
			}
		};
		final Map<String, String> kitCooldownSaveMap = new HashMap<String, String>(this.kitCooldownMap.size());
		this.kitCooldownMap.forEachEntry((uuid, value) -> {
			kitCooldownSaveMap.put(uuid.toString(), Long.toString(value));
			return true;
		});
		new TObjectLongProcedure<UUID>(){
			public boolean execute(final UUID uuid, final long value){
				kitCooldownSaveMap.put(uuid.toString(), Long.toString(value));
				return true;
			}
		};
		map.put("kit-use-map", kitUseSaveMap);
		map.put("kit-cooldown-map", kitCooldownSaveMap);
		return map;
	}

	public long getRemainingKitCooldown(final Kit kit){
		final long remaining = this.kitCooldownMap.get((Object) kit.getUniqueID());
		if(remaining == this.kitCooldownMap.getNoEntryValue()){
			return 0L;
		}
		return remaining - System.currentTimeMillis();
	}

	public void updateKitCooldown(final Kit kit){
		this.kitCooldownMap.put(kit.getUniqueID(), System.currentTimeMillis() + kit.getDelayMillis());
	}

	public int getKitUses(final Kit kit){
		final int result = this.kitUseMap.get((Object) kit.getUniqueID());
		return (result == this.kitUseMap.getNoEntryValue()) ? 0 : result;
	}

	public int incrementKitUses(final Kit kit){
		return this.kitUseMap.adjustOrPutValue(kit.getUniqueID(), 1, 1);
	}

	@Override
	public String getName(){
		return this.getLastKnownName();
	}

	public List<NameHistory> getNameHistories(){
		return this.nameHistories;
	}

	public void tryLoggingName(final Player player){
		Preconditions.checkNotNull((Object) player, (Object) "Cannot log null player");
		final String playerName = player.getName();
		for(final NameHistory nameHistory : this.nameHistories){
			if(nameHistory.getName().contains(playerName)){
				return;
			}
		}
		this.nameHistories.add(new NameHistory(playerName, System.currentTimeMillis()));
	}

	public List<String> getNotes(){
		return this.notes;
	}

	public void setNote(final String note){
		this.notes.add(note);
	}

	public boolean tryRemoveNote(){
		this.notes.clear();
		return true;
	}

	public List<String> getAddressHistories(){
		return this.addressHistories;
	}

	public boolean isStaffUtil(){
		return this.staffutil;
	}

	public void setStaffUtil(final boolean value){
		this.staffutil = value;
	}

	public void setStarterKit(final boolean val){
		this.hasStarter = val;
	}

	public boolean hasStartKit(){
		return this.hasStarter;
	}

	public void tryLoggingAddress(final String address){
		Preconditions.checkNotNull((Object) address, (Object) "Cannot log null address");
		if(!this.addressHistories.contains(address)){
			Preconditions.checkArgument(InetAddresses.isInetAddress(address), (Object) "Not an Inet address");
			this.addressHistories.add(address);
		}
	}

	public Location getBackLocation(){
		return (this.backLocation == null) ? null : this.backLocation.clone();
	}

	public void setBackLocation(final Location backLocation){
		this.backLocation = backLocation;
	}

	public boolean isMessagingSounds(){
		return this.messagingSounds;
	}

	public void setMessagingSounds(final boolean messagingSounds){
		this.messagingSounds = messagingSounds;
	}

	public boolean isVanished(){
		return this.vanished;
	}

	public void setVanished(final boolean vanished){
		this.setVanished(vanished, true);
	}

	public void setVanished(){
		this.setVanished(!this.isVanished(), true);
	}

	public void setVanished(final boolean vanished, final boolean update){
		this.setVanished(Bukkit.getPlayer(this.getUniqueId()), vanished, update);
	}

	public boolean setVanished(final Player player, final boolean vanished, final boolean notifyPlayerList){
		if(this.vanished != vanished){
			if(player != null){
				final PlayerVanishEvent event = new PlayerVanishEvent(player, notifyPlayerList ?
						new HashSet<>(Bukkit.getOnlinePlayers()) : Collections.emptySet(), vanished);
				Bukkit.getPluginManager().callEvent(event);
				if(event.isCancelled()){
					return false;
				}
				if(notifyPlayerList){
					this.updateVanishedState(player, event.getViewers(), vanished);
				}
			}
			this.vanished = vanished;
			return true;
		}
		return false;
	}

	public void updateVanishedState(final Player player, final boolean vanished){
		this.updateVanishedState(player, new HashSet<Player>(Bukkit.getOnlinePlayers()), vanished);
	}

	public void updateVanishedState(final Player player, final Collection<Player> viewers, final boolean vanished){
		player.spigot().setCollidesWithEntities(!vanished);
		//player.showInvisibles(vanished);
		final StaffPriority playerPriority = StaffPriority.of(player);
		for(final Player target : viewers){
			if(player.equals(target)){
				continue;
			}
			if(vanished && playerPriority.isMoreThan(StaffPriority.of(target))){
				target.hidePlayer(player);
			}else{
				target.showPlayer(player);
			}
		}
	}

	public boolean isGlintEnabled(){
		return this.glintEnabled;
	}

	public void setGlintEnabled(final boolean glintEnabled){
		this.setGlintEnabled(glintEnabled, true);
	}

	public void setGlintEnabled(final boolean glintEnabled, final boolean sendUpdatePackets){
		final Player player = this.toPlayer();
		if(player == null || !player.isOnline()){
			return;
		}
		this.glintEnabled = glintEnabled;
		if(BasePlugin.getPlugin().getServerHandler().useProtocolLib){
			final int viewDistance = Bukkit.getViewDistance();
			final PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
			for(final org.bukkit.entity.Entity entity : player.getNearbyEntities((double) viewDistance, (double) viewDistance, (double) viewDistance)){
				if(entity instanceof Item){
					final Item item = (Item) entity;
					if(!(item instanceof CraftItem)){
						continue;
					}
					connection.sendPacket((Packet) new PacketPlayOutEntityMetadata(entity.getEntityId(), ((CraftItem) item).getHandle().getDataWatcher(), true));
				}else{
					if(!(entity instanceof Player)){
						continue;
					}
					if(entity.equals(player)){
						continue;
					}
					final Player target = (Player) entity;
					final PlayerInventory inventory = target.getInventory();
					final int entityID = entity.getEntityId();
					final org.bukkit.inventory.ItemStack[] armour = inventory.getArmorContents();
					for(int i = 0; i < armour.length; ++i){
						final org.bukkit.inventory.ItemStack stack = armour[i];
						if(stack != null && stack.getType() != Material.AIR){
							connection.sendPacket((Packet) new PacketPlayOutEntityEquipment(entityID, i + 1, CraftItemStack.asNMSCopy(stack)));
						}
					}
					final org.bukkit.inventory.ItemStack stack2 = inventory.getItemInHand();
					if(stack2 == null){
						continue;
					}
					if(stack2.getType() == Material.AIR){
						continue;
					}
					connection.sendPacket((Packet) new PacketPlayOutEntityEquipment(entityID, 0, CraftItemStack.asNMSCopy(stack2)));
				}
			}
		}
	}

	public long getLastGlintUse(){
		return this.lastGlintUse;
	}

	public void setLastGlintUse(final long lastGlintUse){
		this.lastGlintUse = lastGlintUse;
	}

	public String getLastKnownName(){
		return ((NameHistory) Iterables.getLast((Iterable) this.nameHistories)).getName();
	}

	public Player toPlayer(){
		return Bukkit.getPlayer(this.getUniqueId());
	}
}