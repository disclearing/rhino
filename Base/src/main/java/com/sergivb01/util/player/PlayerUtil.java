// 

// 

package com.sergivb01.util.player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.sergivb01.base.BasePlugin;
import net.minecraft.util.gnu.trove.map.TObjectLongMap;
import net.minecraft.util.gnu.trove.map.hash.TObjectLongHashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.Map;

public class PlayerUtil{
	private static final Map<Player, Location> frozen;
	private static final Map<Player, PlayerCache> playerCaches;
	private static final TObjectLongMap<Player> lastSent;

	static{
		frozen = new HashMap<Player, Location>();
		playerCaches = new HashMap<Player, PlayerCache>();
		lastSent = new TObjectLongHashMap<Player>();
		Bukkit.getPluginManager().registerEvents(new Listener(){
			@EventHandler
			public void onMove(final PlayerMoveEvent event){
				final Location from = event.getFrom();
				final Location to = event.getTo();
				if(from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ()){
					final Player player = event.getPlayer();
					final Location location = PlayerUtil.frozen.get(player);
					if(location != null && (to.getBlockX() != location.getBlockX() || to.getBlockZ() != location.getBlockZ() || Math.abs(to.getBlockY() - location.getBlockY()) >= 2)){
						location.setYaw(to.getYaw());
						location.setPitch(to.getPitch());
						event.setTo(location);
						final long millis = System.currentTimeMillis();
						final long lastSentMillis = PlayerUtil.lastSent.get(player);
						if(lastSentMillis != PlayerUtil.lastSent.getNoEntryValue() && millis - lastSentMillis <= 3000L){
							return;
						}
						PlayerUtil.lastSent.put(player, millis);
						player.sendMessage(ChatColor.YELLOW + "You are currently " + ChatColor.AQUA + "frozen" + ChatColor.YELLOW + "!");
					}
				}
			}

			@EventHandler
			public void onQuit(final PlayerQuitEvent event){
				final Player player = event.getPlayer();
				PlayerUtil.frozen.remove(player);
				PlayerUtil.lastSent.remove(player);
				final PlayerCache playerCache = PlayerUtil.playerCaches.remove(player);
				if(playerCache != null){
					playerCache.apply(player);
				}
			}
		}, BasePlugin.getPlugin());
	}

	public static void respawn(final Player player){
		final PacketContainer packet = new PacketContainer(PacketType.Play.Client.CLIENT_COMMAND);
		packet.getClientCommands().writeSafely(0, EnumWrappers.ClientCommand.PERFORM_RESPAWN);
		try{
			ProtocolLibrary.getProtocolManager().recieveClientPacket(player, packet);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public static void wipe(final Player player){
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.setExp(0.0f);
		player.setLevel(0);
		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
		player.setRemainingAir(player.getMaximumAir());
		player.setFireTicks(0);
		player.setGameMode(GameMode.SURVIVAL);
		player.setAllowFlight(false);
		player.setFlying(false);
		for(final PotionEffect pe : player.getActivePotionEffects()){
			player.removePotionEffect(pe.getType());
		}
	}

	public static void freeze(final Player player){
		PlayerUtil.frozen.put(player, player.getLocation());
	}

	public static boolean thaw(final Player player){
		return PlayerUtil.frozen.remove(player) != null;
	}

	public static boolean isFrozen(final Player player){
		return PlayerUtil.frozen.containsKey(player);
	}

	public static void cache(final Player player){
		PlayerUtil.playerCaches.put(player, new PlayerCache(player));
	}

	public static void restore(final Player player){
		final PlayerCache playerCache = PlayerUtil.playerCaches.get(player);
		if(playerCache != null){
			playerCache.apply(player);
		}
	}

	public static PlayerCache getCache(final Player player){
		return PlayerUtil.playerCaches.get(player);
	}
}
