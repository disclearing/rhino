package com.sergivb01.base.command.module.essential;

import com.sergivb01.base.BaseConstants;
import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.util.BukkitUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class AmivisCommand
		extends BaseCommand{
	private final BasePlugin plugin;

	public AmivisCommand(BasePlugin plugin){
		super("amivis", "Check if a player is visible.");
		this.setUsage("/(command) <playerName> [targetName]");
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		boolean vanished;
		Player target;
		if(args.length > 0){
			target = BukkitUtils.playerWithNameOrUUID(args[0]);
		}else{
			if(!(sender instanceof Player)){
				sender.sendMessage(this.getUsage(label));
				return true;
			}
			target = (Player) sender;
		}
		if(target == null || !BaseCommand.canSee(sender, target)){
			sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
			return true;
		}
		sender.sendMessage(ChatColor.YELLOW + target.getName() + " is " + (this.plugin.getUserManager().getUser(target.getUniqueId()).isVanished() ? "in vanish" : "not in vanish") + '.');
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		return args.length == 1 ? null : Collections.emptyList();
	}
}

