package com.sergivb01.base.listener;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.StaffPriority;
import com.sergivb01.base.event.PlayerVanishEvent;
import com.sergivb01.base.user.BaseUser;
import com.sergivb01.util.BukkitUtils;
import net.minecraft.server.v1_7_R4.Blocks;
import net.minecraft.server.v1_7_R4.PacketPlayOutBlockAction;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class VanishListener implements Listener{
	private final Map<UUID, Location> fakeChestLocationMap = new HashMap<UUID, Location>();
	private final Set<Player> onlineVanishedPlayers = new HashSet<Player>();
	private final BasePlugin plugin;

	public VanishListener(BasePlugin plugin){
		this.plugin = plugin;
	}

	public static void handleFakeChest(Player player, Chest chest, boolean open){
		Inventory chestInventory = chest.getInventory();
		if(chestInventory instanceof DoubleChestInventory){
			chest = (Chest) ((DoubleChestInventory) chestInventory).getHolder().getLeftSide();
		}
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutBlockAction(chest.getX(), chest.getY(), chest.getZ(), Blocks.CHEST, 1, open ? 1 : 0));
		player.playSound(chest.getLocation(), open ? Sound.CHEST_OPEN : Sound.CHEST_CLOSE, 1.0f, 1.0f);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event){
		BaseUser baseUser;
		StaffPriority selfPriority;
		Player player = event.getPlayer();
		if(!this.onlineVanishedPlayers.isEmpty() && (selfPriority = StaffPriority.of(player)) != StaffPriority.ADMIN){
			for(Player target : this.onlineVanishedPlayers){
				if(!this.plugin.getUserManager().getUser(target.getUniqueId()).isVanished() || !StaffPriority.of(target).isMoreThan(selfPriority))
					continue;
				player.hidePlayer(target);
			}
		}
		if((baseUser = this.plugin.getUserManager().getUser(player.getUniqueId())).isVanished()){
			this.onlineVanishedPlayers.add(player);
			player.sendMessage(ChatColor.YELLOW + "You have joined vanished.");
			ArrayList<String> vanished = new ArrayList<String>();
			for(Player on : Bukkit.getServer().getOnlinePlayers()){
				if(!on.hasPermission("vanish.chestinteract") || player.equals(on) || !this.plugin.getUserManager().getUser(on.getUniqueId()).isVanished() || !player.canSee(on))
					continue;
				vanished.add(on.getName());
			}
			baseUser.updateVanishedState(player, true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event){
		if(this.plugin.getUserManager().getUser(event.getPlayer().getUniqueId()).isVanished()){
			this.onlineVanishedPlayers.remove(event.getPlayer());
			event.setQuitMessage(null);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerVanish(PlayerVanishEvent event){
		if(event.isVanished()){
			this.onlineVanishedPlayers.add(event.getPlayer());
		}else{
			this.onlineVanishedPlayers.remove(event.getPlayer());
		}
	}

   /* @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player;
        Entity entity = event.getRightClicked();
        if (entity instanceof Player && !(player = event.getPlayer()).isSneaking() && player.hasPermission("vanish.inventorysee") && this.plugin.getUserManager().getUser(player.getUniqueId()).isVanished()) {
            player.openInventory(((Player)entity).getInventory());
            event.setCancelled(true);
        }
    } */

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onEntityTarget(EntityTargetEvent event){
		if(event.getReason() == EntityTargetEvent.TargetReason.CUSTOM){
			return;
		}
		Entity target = event.getTarget();
		Entity entity = event.getEntity();
		if((entity instanceof ExperienceOrb || entity instanceof LivingEntity) && target instanceof Player && this.plugin.getUserManager().getUser(target.getUniqueId()).isVanished()){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDropItem(PlayerDropItemEvent e){
		if(this.plugin.getUserManager().getUser(e.getPlayer().getUniqueId()).isVanished()){
			e.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerPickupItem(PlayerPickupItemEvent event){
		if(this.plugin.getUserManager().getUser(event.getPlayer().getUniqueId()).isVanished()){
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerDeath(PlayerDeathEvent event){
		if(this.plugin.getUserManager().getUser(event.getEntity().getUniqueId()).isVanished()){
			event.setDeathMessage(null);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event){
		EntityDamageEvent.DamageCause cause = event.getCause();
		if(cause == EntityDamageEvent.DamageCause.VOID || cause == EntityDamageEvent.DamageCause.SUICIDE){
			return;
		}
		Entity entity = event.getEntity();
		if(entity instanceof Player){
			Player attacked = (Player) entity;
			BaseUser attackedUser = this.plugin.getUserManager().getUser(attacked.getUniqueId());
			Player attacker = BukkitUtils.getFinalAttacker(event, true);
			if(attackedUser.isVanished()){
				if(attacker != null && StaffPriority.of(attacked) != StaffPriority.NONE){
					attacker.sendMessage(ChatColor.RED + "That player is vanished.");
				}
				event.setCancelled(true);
				return;
			}
			if(attacker != null && this.plugin.getUserManager().getUser(attacker.getUniqueId()).isVanished()){
				attacker.sendMessage(ChatColor.RED + "You cannot attack players whilst vanished.");
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		BaseUser baseUser = this.plugin.getUserManager().getUser(player.getUniqueId());
		if(baseUser.isVanished() && !player.hasPermission("vanish.build")){
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You cannot build whilst vanished.");
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onBlockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		BaseUser baseUser = this.plugin.getUserManager().getUser(player.getUniqueId());
		if(baseUser.isVanished() && !player.hasPermission("vanish.build")){
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You cannot build whilst vanished.");
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onBucketEmpty(PlayerBucketEmptyEvent event){
		Player player = event.getPlayer();
		BaseUser baseUser = this.plugin.getUserManager().getUser(player.getUniqueId());
		if(baseUser.isVanished() && !player.hasPermission("vanish.build")){
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You cannot build whilst vanished.");
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		switch(event.getAction()){
			case PHYSICAL:{
				if(!this.plugin.getUserManager().getUser(uuid).isVanished()) break;
				event.setCancelled(true);
				break;
			}
			case RIGHT_CLICK_BLOCK:{
				Block block = event.getClickedBlock();
				BlockState state = block.getState();
				if(!(state instanceof Chest) || !this.plugin.getUserManager().getUser(uuid).isVanished()) break;
				Chest chest = (Chest) state;
				Location chestLocation = chest.getLocation();
				InventoryType type = chest.getInventory().getType();
				if(type != InventoryType.CHEST || this.fakeChestLocationMap.putIfAbsent(uuid, chestLocation) != null)
					break;
				ItemStack[] contents = chest.getInventory().getContents();
				Inventory fakeInventory = Bukkit.createInventory(null, contents.length, "[F] " + type.getDefaultTitle());
				player.sendMessage(ChatColor.RED + "Opening chest silently...");
				fakeInventory.setContents(contents);
				event.setCancelled(true);
				player.openInventory(fakeInventory);
				VanishListener.handleFakeChest(player, chest, true);
				this.fakeChestLocationMap.put(uuid, chestLocation);
				break;
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onInventoryClose(InventoryCloseEvent event){
		BlockState blockState;
		Player player = (Player) event.getPlayer();
		Location chestLocation = this.fakeChestLocationMap.remove(player.getUniqueId());
		if(chestLocation != null && (blockState = chestLocation.getBlock().getState()) instanceof Chest){
			VanishListener.handleFakeChest(player, (Chest) blockState, false);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onInventoryClick(InventoryClickEvent event){
		Player player;
		ItemStack stack;
		HumanEntity humanEntity = event.getWhoClicked();
		if(humanEntity instanceof Player && this.fakeChestLocationMap.containsKey((player = (Player) humanEntity).getUniqueId()) && (stack = event.getCurrentItem()) != null && stack.getType() != Material.AIR && !player.hasPermission("vanish.chestinteract")){
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You cannot interact with fake chest inventories.");
		}
	}

}

