package com.sergivb01.util.bossbar;

import org.bukkit.scheduler.BukkitTask;

public class BossBarEntry{
	private final BossBar bossBar;
	private final BukkitTask cancelTask;

	public BossBarEntry(BossBar bossBar, BukkitTask cancelTask){
		this.bossBar = bossBar;
		this.cancelTask = cancelTask;
	}

	public BossBar getBossBar(){
		return this.bossBar;
	}

	public BukkitTask getCancelTask(){
		return this.cancelTask;
	}
}

