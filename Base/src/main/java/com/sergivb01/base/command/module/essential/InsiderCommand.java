package com.sergivb01.base.command.module.essential;

import com.sergivb01.base.BaseConstants;
import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.base.user.BaseUser;
import com.sergivb01.util.BukkitUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class InsiderCommand extends BaseCommand{
	private BasePlugin plugin;

	public InsiderCommand(BasePlugin plugin){
		super("insider", "Set insider rank of a player");
		this.setUsage("/(command) <add/remove> <playerName>");
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(!(args.length == 2)){
			sender.sendMessage(this.getUsage());
			return true;
		}

        /* THIS THING CALL 2 METHODS. IF IT IS ADD IT WONT BE REMOVE, SAME IF IT IS REMOVE IT WONT BE ADD
        if (!args[0].equalsIgnoreCase("add") || !args[0].equalsIgnoreCase("remove")) {
            sender.sendMessage("ARGS0: " + args[0]);
            sender.sendMessage("ARGS1: " + args[1]);
            sender.sendMessage(ChatColor.RED + "Usage: /insider " + ChatColor.AQUA + "<add/remove> " + ChatColor.RED + "<playerName>");
           return true;
        }*/

		Player target = BukkitUtils.playerWithNameOrUUID(args[1]);
		if(((args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) && target == null) || target == null){
			sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[1]));
			return true;
		}

		BaseUser baseUser = this.plugin.getUserManager().getUser(target.getUniqueId());
		String upTime = DateFormatUtils.format(System.currentTimeMillis(), "dd/MM");
		String time = DateFormatUtils.format(System.currentTimeMillis(), "hh:mm");
		if(args[0].equalsIgnoreCase("add")){
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + target.getName() + " group add Insider");
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou have added &a" + target.getName() + " &eas Insider."));
			baseUser.setNote(ChatColor.YELLOW + "Staff: " + ChatColor.GREEN + sender.getName() + ChatColor.GRAY + " [" + upTime + "-" + time + "]" + " - " + ChatColor.YELLOW + "Set player as insider.");
			return true;
		}else if(args[0].equalsIgnoreCase("remove")){
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + target.getName() + " group remove Insider");
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou have been removed &a" + target.getName() + "&e's Insider Rank."));
			baseUser.setNote(ChatColor.YELLOW + "Staff: " + ChatColor.GREEN + sender.getName() + ChatColor.GRAY + " [" + upTime + "-" + time + "]" + " - " + ChatColor.YELLOW + "Set player as " + ChatColor.RED + "" + ChatColor.BOLD + "NOT" + ChatColor.YELLOW + " insider.");
			return true;
		}else{
			sender.sendMessage(this.getUsage());
			return true;
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		return args.length == 2 ? null : Collections.emptyList();
	}
}