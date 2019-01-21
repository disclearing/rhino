package com.sergivb01.base.task;

import com.sergivb01.base.BasePlugin;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.util.com.google.common.primitives.Ints;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

public class AutoRestartHandler{
	private static final int[] ALERT_SECONDS = new int[]{14400, 7200, 1800, 600, 300, 270, 240, 210, 180, 150, 120, 90, 60, 30, 15, 10, 5, 4, 3, 2, 1};
	private static final long TICKS_DAY = TimeUnit.DAYS.toMillis(1);
	private final BasePlugin plugin;
	private long current = Long.MIN_VALUE;
	private String reason;
	private BukkitTask task;

	public AutoRestartHandler(BasePlugin plugin){
		this.plugin = plugin;
		this.scheduleRestart(TICKS_DAY);
	}

	public String getReason(){
		return this.reason;
	}

	public void setReason(String reason){
		this.reason = reason;
	}

	public boolean isPendingRestart(){
		return this.task != null && this.current != Long.MIN_VALUE;
	}

	public void cancelRestart(){
		if(this.isPendingRestart()){
			this.task.cancel();
			this.task = null;
			this.current = Long.MIN_VALUE;
		}
	}

	public long getRemainingMilliseconds(){
		return this.getRemainingTicks() * 50;
	}

	public long getRemainingTicks(){
		return this.current - (long) MinecraftServer.currentTick;
	}

	public void scheduleRestart(long milliseconds){
		this.scheduleRestart(milliseconds, null);
	}

	public void scheduleRestart(long millis, final String reason){
		this.cancelRestart();
		this.reason = reason;
		this.current = (long) (MinecraftServer.currentTick + 20) + millis / 50;
		this.task = new BukkitRunnable(){

			public void run(){
				long remainingTicks;
				if(AutoRestartHandler.this.current == 0){
					this.cancel();
				}
				if((remainingTicks = AutoRestartHandler.this.getRemainingTicks()) <= 0){
					this.cancel();
					Bukkit.getServer().savePlayers();
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "safestop");
					return;
				}
				long remainingMillis = remainingTicks * 50;
				if(Ints.contains(ALERT_SECONDS, (int) (remainingMillis / 1000))){
					Bukkit.broadcastMessage(" ");
					Bukkit.broadcastMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + " Server Restart");
					Bukkit.broadcastMessage(" ");
					Bukkit.broadcastMessage(ChatColor.YELLOW + "   Time: " + ChatColor.WHITE + DurationFormatUtils.formatDurationWords(remainingMillis, true, true));
					Bukkit.broadcastMessage(ChatColor.YELLOW + "   Reason: " + ChatColor.WHITE + (reason.isEmpty() ? "Scheduled Restart" : reason.replace("[", "").replace("]", "")));
					Bukkit.broadcastMessage(" ");

				}
			}
		}.runTaskTimer(this.plugin, 20L, 20L);
	}

}

