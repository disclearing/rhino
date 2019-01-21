package com.sergivb01.hcf.commands;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.timer.type.SotwTimer;
import com.sergivb01.hcf.utils.config.ConfigurationService;
import com.sergivb01.util.BukkitUtils;
import com.sergivb01.util.JavaUtils;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.util.com.google.common.collect.ImmutableList;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SotwCommand implements CommandExecutor, TabCompleter{
	private static final List<String> COMPLETIONS;

	static{
		COMPLETIONS = ImmutableList.of("start", "end");
	}

	private final HCF plugin;

	public SotwCommand(final HCF plugin){
		super();
		this.plugin = plugin;
	}

	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (!sender.hasPermission("sotw.admin")) {
			if (args.length != 1 || !args[0].equalsIgnoreCase("enable")) {
				sender.sendMessage(ChatColor.RED + "To enable your sotw timer, utilise the /sotw enable command.");
				return true;
			}
			if (plugin.getSotwTimer().getSotwRunnable() == null) {
				sender.sendMessage(ChatColor.RED + "Sotw isn't active.");
				return true;
			}
			if (plugin.getSotwTimer().getSotwRunnable().isSotwEnabled((Player) sender)) {
				sender.sendMessage(ChatColor.RED + "You've already enabled your SOTW timer.");
				return true;
			}
			plugin.getSotwTimer().enabledSotw.add(((Player) sender).getUniqueId());
			sender.sendMessage(ChatColor.GREEN + "Your SOTW timer has been enabled, you can now take, and deal damage to players who don't have their SOTW timer.");
			return true;
		}

		if(args.length > 0){
			if(args[0].equalsIgnoreCase("start")){
				if(args.length < 2){
					sender.sendMessage(ChatColor.RED + "Usage: /" + label + " " + "start h:m:s ");
					sender.sendMessage(ChatColor.RED + "Example: /sotw start 3h3m");
					return true;
				}
				final long duration = JavaUtils.parse(args[1]);
				if(duration == -1L){
					sender.sendMessage(ChatColor.RED + "'" + args[0] + "' is an invalid duration.");
					return true;
				}
				if(duration < 1000L){
					sender.sendMessage(ChatColor.RED + "SOTW protection time must last for at least 20 ticks.");
					return true;
				}
				final SotwTimer.SotwRunnable sotwRunnable = this.plugin.getSotwTimer().getSotwRunnable();
				if(sotwRunnable != null){
					sender.sendMessage(ChatColor.RED + "SOTW protection is already enabled, use /" + label + " cancel to end it.");
					return true;
				}
				this.plugin.getSotwTimer().start(duration);
				sender.sendMessage(ConfigurationService.SOTW_STARTED.replace("%time%", DurationFormatUtils.formatDurationWords(duration, true, true)));
				//sender.sendMessage(ChatColor.RED + "Started SOTW protection for " + DurationFormatUtils.formatDurationWords(duration, true, true) + ".");
				return true;
			}else if(args[0].equalsIgnoreCase("end") || args[0].equalsIgnoreCase("cancel")){
				if(this.plugin.getSotwTimer().cancel()){
					sender.sendMessage(ConfigurationService.SOTW_CANCELLED);
					plugin.getSotwTimer().enabledSotw.clear();
					return true;
				}
				sender.sendMessage(ConfigurationService.SOTW_NOT_ACTIVE);
				return true;
			} else if(args[0].equalsIgnoreCase("enable")) {
				if (plugin.getSotwTimer().getSotwRunnable() == null) {
					sender.sendMessage(ChatColor.RED + "Sotw isn't active.");
					return true;
				}
				if (plugin.getSotwTimer().getSotwRunnable().isSotwEnabled((Player) sender)) {
					sender.sendMessage(ChatColor.RED + "You've already enabled your SOTW timer.");
					return true;
				}
				plugin.getSotwTimer().enabledSotw.add(((Player) sender).getUniqueId());
				sender.sendMessage(ChatColor.GREEN + "Your SOTW timer has been enabled, you can now take, and deal damage to players who don't have their SOTW timer.");
				return true;
			}
		}
		sender.sendMessage(ChatColor.RED + "To enable your sotw timer, utilise the /sotw enable command.");
		sender.sendMessage(ChatColor.RED + "Usage: /" + label + " " + " start h:m:s ");
		sender.sendMessage(ChatColor.RED + "Example: /sotw start 3h3m");
		return true;
	}

	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args){
		return (args.length == 1) ? BukkitUtils.getCompletions(args, SotwCommand.COMPLETIONS) : Collections.emptyList();
	}
}