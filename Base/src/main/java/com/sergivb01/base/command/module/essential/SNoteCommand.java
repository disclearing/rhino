package com.sergivb01.base.command.module.essential;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.base.user.BaseUser;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SNoteCommand
		extends BaseCommand{
	public SNoteCommand(){
		super("snote", "add, removes, and checks notes for a user");
		this.setUsage("/(command) <add|remove|check> <playerName> [note]");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args){
		if(args.length >= 2){
			if(Bukkit.getPlayer(args[1]) == null && Bukkit.getOfflinePlayer(args[1]) == null){
				sender.sendMessage(ChatColor.RED + "Player not found.");
				return true;
			}
			OfflinePlayer tg = Bukkit.getOfflinePlayer(args[1]);
			BaseUser target = BasePlugin.getPlugin().getUserManager().getUser(tg.getUniqueId());
			String note = StringUtils.join(args, ' ', 2, args.length);
			if(args[0].equalsIgnoreCase("add")){
				if(args[2] != null){
					String date = DateFormatUtils.format(System.currentTimeMillis(), "dd/MM");
					String time = DateFormatUtils.format(System.currentTimeMillis(), "hh:mm");
					target.setNote(ChatColor.translateAlternateColorCodes('&', "&7[" + date + "/" + time + "] &e[" + sender.getName() + "] &c" + note));
					sender.sendMessage(ChatColor.YELLOW + "You have added a note to " + target.getName());
					return true;
				}

			}
			sender.sendMessage(ChatColor.RED + "Usage: /note <check/add/remove> <player> [note]");
			return true;
		}
		sender.sendMessage(ChatColor.RED + "Usage: /note <check/add/remove> <player> [note]");
		return true;
	}

}
