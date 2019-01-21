package com.sergivb01.base.command.module.chat;

import com.sergivb01.base.command.BaseCommand;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChatCommand
		extends BaseCommand{
	private static final String BYPASS_PERMISSION = "command.clearchat.bypass";
	private static final String[] CLEAR_MESSAGE = new String[101];

	public ClearChatCommand(){
		super("clearchat", "Clears the server chat for players.");
		this.setAliases(new String[]{"cc"});
		this.setUsage("/(command) <reason>");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(args.length == 0){
			sender.sendMessage(this.getUsage());
			return true;
		}
		String reason = StringUtils.join(args, ' ');
		for(Player player : Bukkit.getOnlinePlayers()){
			if(player.hasPermission(BYPASS_PERMISSION)) continue;
			player.sendMessage(CLEAR_MESSAGE);
		}
		Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "You have cleared chat for: " + reason, true);

		return true;
	}
}

