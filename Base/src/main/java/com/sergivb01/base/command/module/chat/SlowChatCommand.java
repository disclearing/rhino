package com.sergivb01.base.command.module.chat;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.util.JavaUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.concurrent.TimeUnit;

public class SlowChatCommand
		extends BaseCommand{
	private static final long DEFAULT_DELAY = TimeUnit.MINUTES.toMillis(5);
	private final BasePlugin plugin;

	public SlowChatCommand(BasePlugin plugin){
		super("slowchat", "Slows the chat down for non-staff.");
		this.setAliases(new String[]{"slow"});
		this.setUsage("/(command)");
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		Long newTicks;
		long oldTicks = this.plugin.getServerHandler().getRemainingChatSlowedMillis();
		if(oldTicks > 0){
			newTicks = (long) 0;
		}else if(args.length < 1){
			newTicks = DEFAULT_DELAY;
		}else{
			newTicks = JavaUtils.parse(args[0]);
			if(newTicks == -1){
				sender.sendMessage(ChatColor.RED + "Invalid duration, use the correct format: 10m1s");
				return true;
			}
		}
		this.plugin.getServerHandler().setChatSlowedMillis(newTicks);
		Command.broadcastCommandMessage(sender, ChatColor.translateAlternateColorCodes('&', "&eYou have " + (newTicks > 0 ? " slowed down chat." : String.valueOf(ChatColor.YELLOW) + " de-restricted chat.")));
		Bukkit.broadcastMessage(ChatColor.YELLOW + "Global chat is" + (newTicks > 0 ? " now slowed down for " + DurationFormatUtils.formatDurationWords(newTicks, true, true) : String.valueOf(ChatColor.YELLOW) + " no longer slowed") + ChatColor.YELLOW + '.');
		return true;
	}
}

