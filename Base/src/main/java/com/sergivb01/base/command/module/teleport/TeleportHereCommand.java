package com.sergivb01.base.command.module.teleport;

import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.util.BukkitUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class TeleportHereCommand
		extends BaseCommand{
	public TeleportHereCommand(){
		super("teleporthere", "Teleport to a player to your position.");
		this.setAliases(new String[]{"tphere"});
		this.setUsage("/(command) <playerName>");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
			return true;
		}
		if(args.length < 1){
			sender.sendMessage(this.getUsage(label));
			return true;
		}
		if(TeleportHereCommand.checkNull(sender, args[0])){
			return true;
		}
		Player player = (Player) sender;
		BukkitUtils.playerWithNameOrUUID(args[0]).teleport(player);
		Command.broadcastCommandMessage(player, ChatColor.translateAlternateColorCodes('&', "&eYou have teleported &a" + args[0] + " &eto you."));
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		return args.length == 1 ? null : Collections.emptyList();
	}
}

