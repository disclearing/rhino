package com.sergivb01.util.bossbar;

import com.google.common.base.Preconditions;
import com.sergivb01.base.BasePlugin;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossBarManager{
	private static final Map<UUID, BossBarEntry> bossBars = new HashMap<UUID, BossBarEntry>();
	private static JavaPlugin plugin;

	public static void hook(){
		Preconditions.checkArgument(plugin == null, "BossBarManager is already hooked");
		plugin = BasePlugin.getPlugin();
	}

	public static void unhook(){
		Preconditions.checkArgument(plugin != null, "BossBarManager is already unhooked");
		for(UUID uuid : bossBars.keySet()){
			Player player = Bukkit.getPlayer(uuid);
			if(player == null) continue;
			BossBarManager.hideBossBar(player);
		}
		plugin = null;
	}

	public static Map<UUID, BossBarEntry> getBossBars(){
		return bossBars;
	}

	public static boolean isShowingBossBar(Player player){
		return BossBarManager.getShownBossBar(player) != null;
	}

	public static BossBar getShownBossBar(Player player){
		UUID uuid = player.getUniqueId();
		BossBarEntry bossBarEntry = bossBars.get(uuid);
		return bossBarEntry != null ? bossBarEntry.getBossBar() : null;
	}

	public static BossBar hideBossBar(Player player){
		BossBarEntry entry = bossBars.get(player.getUniqueId());
		if(entry == null){
			return null;
		}
		BossBar bossBar = entry.getBossBar();
		BukkitTask bukkitTask = entry.getCancelTask();
		if(bukkitTask != null){
			bukkitTask.cancel();
		}
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(bossBar.destroyPacket);
		bossBars.remove(player.getUniqueId());
		return bossBar;
	}

	public static void showBossBar(BossBar bossBar, Player player){
		BossBarManager.showBossBar(bossBar, player, 0);
	}

	public static void showBossBar(BossBar bossBar, final Player player, long ticks){
		BukkitTask bukkitTask;
		BossBar current = BossBarManager.getShownBossBar(player);
		if(current != null){
			if(current.equals(bossBar)){
				return;
			}
			BossBarManager.hideBossBar(player);
		}
		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
		connection.sendPacket(bossBar.spawnPacket);
		if(ticks <= 0){
			bukkitTask = null;
		}else{
			Preconditions.checkArgument(plugin != null, "Cannot start destroy runnable as plugin wasn't hooked correctly.");
			bukkitTask = new BukkitRunnable(){

				public void run(){
					BossBarManager.hideBossBar(player);
				}
			}.runTaskLater(plugin, ticks);
		}
		bossBars.put(player.getUniqueId(), new BossBarEntry(bossBar, bukkitTask));
		connection.sendPacket(bossBar.spawnPacket);
	}

}

