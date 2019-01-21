package com.sergivb01.hcf.commands;

import com.sergivb01.hcf.payloads.Cache;
import com.sergivb01.hcf.payloads.types.Payload;
import com.sergivb01.hcf.payloads.types.ReportPayload;
import com.sergivb01.hcf.utils.StringUtils;
import com.sergivb01.hcf.utils.config.ConfigurationService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

import static org.bukkit.ChatColor.RED;

public class ReportCommand implements CommandExecutor{

	public boolean onCommand(CommandSender sender, Command command, String s, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(RED + "This command may not be executed by Console.");
			return true;
		}

		Player player = (Player) sender;
		if(args.length < 2){
			player.sendMessage(RED + "Invalid usage: `/report <playername> <reason...>`");
			return true;
		}

		Player target = Bukkit.getPlayer(args[0]);
		String reason = StringUtils.join(args, 1);

		if(target == null){
			player.sendMessage(RED + "Player `" + args[0] + "` is not online or has never joined before!");
			return true;
		}

		if(player.equals(target)){
			player.sendMessage(RED + "You may not report yourself!");
			return true;
		}

		if(!Cache.canExecute(player)){
			player.sendMessage(RED + "You hav already used the report or request command in the past 5 minutes! Please wait and try again.");
			return true;
		}

		Cache.addPlayerDelay(player);

		if(ConfigurationService.REDIS_ENABLED){
			Payload payload = new ReportPayload(player.getName(), player.getUniqueId(), target.getName(), target.getUniqueId(), reason);
			payload.send();
			Cache.addPayload(payload);
			return true;
		}

		Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("hcf.utils.staff")).collect(Collectors.toList())
				.forEach(p -> p.sendMessage(String.format(ChatColor.translateAlternateColorCodes(
						'&', "&4[Report] &c%s&7 has been reported by &7%s&7 for &c%s"
				), target.getName(), player.getName(), reason)));

		Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("hcf.utils.staff")).collect(Collectors.toList())
				.forEach(p -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigurationService.REPORT
						.replace("%REPORTER%", player.getName())
						.replace("%REPORTED%", target.getName())
						.replace("%REASON%", StringUtils.join(args))
				)));

		return true;
	}


}
