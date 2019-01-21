package com.sergivb01.base.command.module.chat;

import com.google.common.collect.ImmutableList;
import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.base.task.AnnouncementHandler;
import com.sergivb01.util.BukkitUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class AnnouncementCommand
		extends BaseCommand{
	private final BasePlugin plugin;
	private List<String> COMPLETIONS;
	private int MAX_ANNOUNCEMENT_PER_PAGE = 10;

	public AnnouncementCommand(BasePlugin plugin){
		super("announcement", "Broadcasts a message to the server.");
		this.setAliases(new String[]{"announce", "ann"});
		this.COMPLETIONS = ImmutableList.of("add", "remove", "list", "delay");
		this.setUsage("/(command) <add|remove|list|delay> <text..|delay in s>");
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(args.length == 0){
			sender.sendMessage(this.getUsage(label));
			return true;
		}
		if(args.length >= 1){
			if(args[0].equalsIgnoreCase("list")){
				Integer number = 0;
				sender.sendMessage(ChatColor.AQUA + "Announcement Lists: ");
				if(number <= this.MAX_ANNOUNCEMENT_PER_PAGE){
					for(String announce : this.plugin.getServerHandler().getAnnouncements()){
						sender.sendMessage(ChatColor.GRAY.toString() + " [" + ChatColor.YELLOW + number + ChatColor.GRAY + ']' + ChatColor.YELLOW + announce);
						Integer n = number;
						Integer n2 = number = Integer.valueOf(number + 1);
					}
				}else{
					sender.sendMessage(ChatColor.YELLOW + "Too many to display!");
					return true;
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("add")){
				String messagge = StringUtils.join(args, ' ', 1, args.length);
				this.plugin.getServerHandler().getAnnouncements().add(ChatColor.translateAlternateColorCodes('&', messagge));
				Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Added " + messagge + " to the announcements.", true);
				return true;
			}
			if(args[0].equalsIgnoreCase("remove")){
				int integer;
				try{
					integer = Integer.parseInt(args[1]);
				}catch(NumberFormatException ex){
					sender.sendMessage(ChatColor.RED + "Use /" + label + " list to get the number.");
					return true;
				}
				Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Announcement number " + integer + " has been removed." + '\n' + ChatColor.GRAY + "(" + this.plugin.getServerHandler().getAnnouncements().get(integer) + ")");
				this.plugin.getServerHandler().getAnnouncements().remove(integer);
				return true;
			}
			if(args[0].equalsIgnoreCase("delay")){
				int integer;
				try{
					integer = Integer.parseInt(args[1]);
				}catch(NumberFormatException ex){
					sender.sendMessage(ChatColor.RED + "Must be a number to set the delay.");
					return true;
				}
				if(this.plugin.getServerHandler().getAnnouncementDelay() == integer){
					return true;
				}

				this.plugin.getServerHandler().setAnnouncementDelay(integer * 20);
				BasePlugin.getPlugin().announcementTask.cancel();
				final AnnouncementHandler announcementTask = new AnnouncementHandler(this.plugin);
				(BasePlugin.getPlugin().announcementTask = announcementTask).runTaskTimerAsynchronously(BasePlugin.getPlugin(), (long) this.plugin.getServerHandler().getAnnouncementDelay(), (long) this.plugin.getServerHandler().getAnnouncementDelay());
				Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Announcement Delay has been modified to " + integer);
				sender.sendMessage(args[0]);
				sender.sendMessage(args[1]);
				return true;
			}
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		return args.length == 1 ? BukkitUtils.getCompletions(args, this.COMPLETIONS) : Collections.emptyList();
	}
}

