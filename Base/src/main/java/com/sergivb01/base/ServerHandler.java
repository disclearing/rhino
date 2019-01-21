package com.sergivb01.base;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ServerHandler{
	@Getter
	private final List<String> announcements = new ArrayList<String>();
	private final List<String> serverRules = new ArrayList<String>();
	private final BasePlugin plugin;
	@Getter
	@Setter
	public boolean useProtocolLib;
	@Getter
	@Setter
	private boolean kitmap;
	@Getter
	@Setter
	private int clearlagdelay;
	@Getter
	@Setter
	private int announcementDelay;
	@Getter
	@Setter
	private long chatSlowedMillis;
	@Setter
	private long chatDisabledMillis;
	@Setter
	private int chatSlowedDelay;
	@Getter
	private String broadcastFormat;
	@Getter
	private FileConfiguration config;
	@Getter
	@Setter
	private boolean decreasedLagMode;
	@Getter
	@Setter
	private boolean end;
	@Getter
	private Location endExit;
	@Getter
	@Setter
	private boolean donorOnly;
	@Getter
	private int worldBorder;
	@Getter
	private int netherBorder;
	@Getter
	private int endBorder;
	@Getter
	private boolean Lockdown;
	@Getter
	public String teamspeak;

	public ServerHandler(BasePlugin plugin){
		this.plugin = plugin;
		this.config = plugin.getConfig();
		this.reloadServerData();
	}

	public void setServerBorder(World.Environment environment, Integer integer){
		if(environment.equals(World.Environment.NORMAL)){
			this.worldBorder = integer;
		}else if(environment.equals(World.Environment.NETHER)){
			this.netherBorder = integer;
		}else if(environment.equals(World.Environment.THE_END)){
			this.endBorder = integer;
		}
	}

	private void reloadServerData(){
		this.plugin.reloadConfig();
		this.config = this.plugin.getConfig();
		String exitWorld = this.config.getString("end.exitLocation.world", "world");
		double x = this.config.getDouble("end.exitLocation.x", 0.0);
		double y = this.config.getDouble("end.exitLocation.y", 66.0);
		double z = this.config.getDouble("end.exitLocation.z", -200.0);
		this.endExit = new Location(Bukkit.getWorld(exitWorld), x, y, z);
		this.donorOnly = this.config.getBoolean("donor-only-enter");
		this.Lockdown = this.config.getBoolean("lockdown-enabled", false);
		this.end = this.config.getBoolean("end-open");
		this.worldBorder = this.config.getInt("border.worldBorder", 3000);
		this.netherBorder = this.config.getInt("border.netherBorder", 1000);
		this.endBorder = this.config.getInt("border.endBorder", 1500);
		this.serverRules.clear();
		this.clearlagdelay = this.config.getInt("clearlag.delay", 100000);
		this.announcementDelay = this.config.getInt("announcements.delay", 15);
		this.announcements.clear();
		for(String each : this.config.getStringList("announcements.list")){
			this.announcements.add(ChatColor.translateAlternateColorCodes('&', each));
		}
		this.chatDisabledMillis = this.config.getLong("chat.disabled.millis", 0);
		this.chatSlowedMillis = this.config.getLong("chat.slowed.millis", 0);
		this.chatSlowedDelay = this.config.getInt("chat.slowed.delay", 15);
		this.useProtocolLib = this.config.getBoolean("use-protocol-lib", true);
		this.decreasedLagMode = this.config.getBoolean("decreased-lag-mode");
		this.broadcastFormat = ChatColor.translateAlternateColorCodes('&', this.config.getString("broadcast.format", ChatColor.AQUA + " &7%1$s"));
		this.teamspeak = config.getString("teamspeak");
		this.kitmap = this.config.getBoolean("kit-map");
	}

	public void saveServerData(){
		this.config.set("clearlag.delay", this.clearlagdelay);
		this.config.set("server-rules", this.serverRules);
		this.config.set("use-protocol-lib", this.useProtocolLib);
		this.config.set("chat.disabled.millis", this.chatDisabledMillis);
		this.config.set("chat.slowed.millis", this.chatSlowedMillis);
		this.config.set("chat.slowed-delay", this.chatSlowedDelay);
		this.config.set("announcements.delay", this.announcementDelay);
		this.config.set("announcements.list", this.announcements);
		this.config.set("kit-map", this.kitmap);
		this.config.set("decreased-lag-mode", this.decreasedLagMode);
		this.config.set("end.exitLocation.world", this.endExit.getWorld().getName());
		this.config.set("end.exitLocation.x", this.endExit.getX());
		this.config.set("end.exitLocation.y", this.endExit.getY());
		this.config.set("end.exitLocation.z", this.endExit.getX());
		this.config.set("donor-only-enter", this.donorOnly);
		this.config.set("lockdown-enabled", this.Lockdown);
		this.config.set("end-open", this.end);
		this.config.set("border.worldBorder", this.worldBorder);
		this.config.set("border.netherBorder", this.netherBorder);
		this.config.set("border.endBorder", this.endBorder);
		this.plugin.saveConfig();
	}


	public void setChatSlowedMillis(long ticks){
		this.chatSlowedMillis = System.currentTimeMillis() + ticks;
	}

	public long getRemainingChatSlowedMillis(){
		return this.chatSlowedMillis - System.currentTimeMillis();
	}

	public boolean isChatDisabled(){
		return this.getRemainingChatDisabledMillis() > 0;
	}

	public long getChatDisabledMillis(){
		return this.chatDisabledMillis;
	}

	public void setChatDisabledMillis(long ticks){
		long millis = System.currentTimeMillis();
		this.chatDisabledMillis = millis + ticks;
	}

	public long getRemainingChatDisabledMillis(){
		return this.chatDisabledMillis - System.currentTimeMillis();
	}

	public int getChatSlowedDelay(){
		return this.chatSlowedDelay;
	}

}

