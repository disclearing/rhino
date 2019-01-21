package com.sergivb01.base.command.module.teleport;

import com.google.common.collect.Maps;
import com.sergivb01.base.command.BaseCommand;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class LobbyCommand extends BaseCommand{
	private static final long LOBBY_DELAY = TimeUnit.SECONDS.toMillis(30);
	private final Map<UUID, Long> lobbyMap;
	private Plugin main;

	public LobbyCommand(Plugin plugin){
		super("lobby", "Goes to lobby.");
		this.setAliases(new String[]{"hub", "leaveserver"});
		this.setUsage("/(command) <world>");
		this.main = plugin;
		this.lobbyMap = Maps.newHashMap();
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "This command is only executable for players.");
			return true;
		}
		Player player = (Player) sender;
		UUID uuid = player.getUniqueId();
		long millis = System.currentTimeMillis();
		Long lastReport = this.lobbyMap.get(uuid);
		if(lastReport != null && lastReport - millis > 0){
			sender.sendMessage(ChatColor.RED + "You have already used this command in the last " + DurationFormatUtils.formatDurationWords(LOBBY_DELAY, true, true) + '.');
			return true;
		}
		this.lobbyMap.put(uuid, millis + LOBBY_DELAY);

		Player p = (Player) sender;

		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		int lobbyNumber = (int) (Math.random() * 2 + 1);
		try{
			p.sendMessage(ChatColor.GREEN + "You have been sent to the lobby " + lobbyNumber + ".");
			out.writeUTF("Connect");
			out.writeUTF("lobby" + lobbyNumber);
		}catch(IOException e){
			p.sendMessage(ChatColor.RED + "Error while trying to connect to the lobby" + lobbyNumber + ".");
		}
		p.sendPluginMessage(main, "BungeeCord", b.toByteArray());


		return true;
	}

}

