package com.sergivb01.base.command.module.chat;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.base.user.BaseUser;
import com.sergivb01.base.user.ServerParticipator;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;

public class StaffChatCommand {

	//	extends BaseCommand{
	/*
	private final BasePlugin plugin;

	public StaffChatCommand(BasePlugin plugin){
		super("staffchat", "Enters staff chat mode.");
		this.setAliases(new String[]{"sc", "ac"});
		this.setUsage("/(command) [playerName]");
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		ServerParticipator target;
		ServerParticipator participator = this.plugin.getUserManager().getParticipator(sender);
		if(participator == null){
			sender.sendMessage(ChatColor.RED + "You are not allowed to do this.");
			return true;
		}
		if(args.length <= 0){
			if(!(sender instanceof Player)){
				sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <message|playerName>");
				return true;
			}
			target = participator;
		}else{
			Player targetPlayer = Bukkit.getPlayerExact(args[0]);
			if(targetPlayer == null || !BaseCommand.canSee(sender, targetPlayer) || !sender.hasPermission(command.getPermission() + ".others")){
				String message = StringUtils.join(args, ' ');
				String format = ChatColor.AQUA + String.format(Locale.ENGLISH, new StringBuilder().append(ChatColor.BLUE).append("(Staff) ").append(ChatColor.AQUA).append("%1$s").append(ChatColor.GRAY).append(": ").append(ChatColor.AQUA).append("%2$s").toString(), sender.getName(), message);
				Bukkit.getConsoleSender().sendMessage(format);
				for(Player other : Bukkit.getServer().getOnlinePlayers()){
					BaseUser otherUser = this.plugin.getUserManager().getUser(other.getUniqueId());
					if(!otherUser.isStaffChatVisible() || !other.hasPermission("base.command.staffchat")) continue;
					other.sendMessage(format);
				}
				return true;
			}
			target = this.plugin.getUserManager().getUser(targetPlayer.getUniqueId());
		}
		boolean newStaffChat = !target.isInStaffChat() || args.length >= 2 && Boolean.parseBoolean(args[1]);
		target.setInStaffChat(newStaffChat);
		sender.sendMessage(ChatColor.YELLOW + "Staff chat mode of " + target.getName() + " set to " + newStaffChat + '.');
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		return null;
	}
	*/
}

