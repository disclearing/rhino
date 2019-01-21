package com.sergivb01.hcf.commands;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.base.user.ServerParticipator;
import com.sergivb01.hcf.payloads.Cache;
import com.sergivb01.hcf.payloads.types.Payload;
import com.sergivb01.hcf.payloads.types.StaffChatPayload;
import com.sergivb01.hcf.utils.StringUtils;
import com.sergivb01.hcf.utils.config.ConfigurationService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

public class StaffChatCommand implements CommandExecutor{

	public boolean onCommand(CommandSender sender, Command command, String s, String[] args){
		if(!(sender instanceof Player)) {
			sender.sendMessage("§cOnly players");
			return true;
		}

		if(args.length == 0) {
			Player player = (Player) sender;
			boolean newStaffChat = !BasePlugin.getPlugin().getUserManager().getUser(player.getUniqueId()).isInStaffChat() || args.length >= 2 && Boolean.parseBoolean(args[1]);
			BasePlugin.getPlugin().getUserManager().getUser(player.getUniqueId()).setInStaffChat(newStaffChat);
			sender.sendMessage(GREEN + "Staff chat mode of " + sender.getName() + " set to " + newStaffChat + '.');
			return true;
		}
		Player targetPlayer = Bukkit.getPlayer(args[0]);
		ServerParticipator target;
		ServerParticipator participator = BasePlugin.getPlugin().getUserManager().getParticipator(sender);
		if(participator == null){
			sender.sendMessage(RED + "You are not allowed to do this.");
			return true;
		}

		if(args.length <= 0){
			if(!(sender instanceof Player)){
				sender.sendMessage(RED + "Usage: /sc <player|message...>");
				return true;
			}
			target = participator;
		}else{
			if(targetPlayer == null) {
				if (!BaseCommand.canSee(sender, targetPlayer) || !sender.hasPermission("command.staffchat.others")) {
					if (ConfigurationService.REDIS_ENABLED) {
						Payload payload = new StaffChatPayload(participator.getUniqueId(), participator.getName(), StringUtils.join(args));
						payload.send();
						Cache.addPayload(payload);
						return true;
					}

			/*	Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("hcf.command.staffchat")).collect(Collectors.toList())
						.forEach(p -> p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"&5[StaffChat] &d" + participator.getName() + "&7: " + StringUtils.join(args)
						)));
				*/

					Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("hcf.utils.staff")).collect(Collectors.toList())
							.forEach(p -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigurationService.STAFFCHAT
									.replace("%PLAYER%", participator.getName())
									.replace("%MESSAGE%", StringUtils.join(args))
							)));
				}
			} else {
				boolean newStaffChat = !BasePlugin.getPlugin().getUserManager().getUser(targetPlayer.getUniqueId()).isInStaffChat() || args.length >= 2 && Boolean.parseBoolean(args[1]);
				BasePlugin.getPlugin().getUserManager().getUser(targetPlayer.getUniqueId()).setInStaffChat(newStaffChat);
				sender.sendMessage(GREEN + "Staff chat mode of " + targetPlayer.getName() + " set to " + newStaffChat + '.');
				return true;
			}
		}
		return true;
	}

}
