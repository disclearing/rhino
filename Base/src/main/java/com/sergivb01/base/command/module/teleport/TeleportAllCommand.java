package com.sergivb01.base.command.module.teleport;

import com.sergivb01.base.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Collections;
import java.util.List;

public class TeleportAllCommand
		extends BaseCommand{
	public TeleportAllCommand(){
		super("teleportall", "Teleport all players to yourself.");
		this.setAliases(new String[]{"tpall"});
		this.setUsage("/(command)");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
			return true;
		}
		Player player = (Player) sender;
		for(Player target : Bukkit.getOnlinePlayers()){
			if(target.equals(player) || !player.canSee(target)) continue;
			target.teleport(player, PlayerTeleportEvent.TeleportCause.COMMAND);
			target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&oAll players have been teleported to " + player.getName()));
		}
		Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "All players have been teleported to your location.");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		return args.length == 1 ? null : Collections.emptyList();
	}
}

