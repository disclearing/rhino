package com.sergivb01.base.command.module.essential;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.command.BaseCommand;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class SetMotdCommand
		extends BaseCommand{
	private final BasePlugin plugin;

	public SetMotdCommand(BasePlugin plugin){
		super("setmotd", "Set the servers motd.");
		this.setUsage("/(command) <message>");
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(args.length < 1){
			sender.sendMessage(ChatColor.RED + "You must specify a message.");
			return false;
		}

		String message = StringUtils.join(args, ' ', 0, args.length);
		MinecraftServer.getServer().setMotd(ChatColor.translateAlternateColorCodes('&', message));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou have set this servers MOTD to: &a" + message));
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		return args.length == 1 ? null : Collections.emptyList();
	}
}

