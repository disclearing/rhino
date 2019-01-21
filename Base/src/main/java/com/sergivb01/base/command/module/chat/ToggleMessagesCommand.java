package com.sergivb01.base.command.module.chat;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.base.user.BaseUser;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleMessagesCommand
		extends BaseCommand{
	private final BasePlugin plugin;

	public ToggleMessagesCommand(BasePlugin plugin){
		super("togglemessages", "Toggles private messages.");
		this.setAliases(new String[]{"togglepm", "toggleprivatemessages"});
		this.setUsage("/(command)");
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "This command is only executable for players.");
			return true;
		}
		Player player = (Player) sender;
		BaseUser baseUser = this.plugin.getUserManager().getUser(player.getUniqueId());
		boolean newToggled = !baseUser.isMessagesVisible();
		baseUser.setMessagesVisible(newToggled);
		if(newToggled){
			sender.sendMessage(ChatColor.GREEN + "You have enabled private messages.");
			return true;
		}else{
			sender.sendMessage(ChatColor.RED + "You have disabled private messages.");
		}
		return true;
	}
}

