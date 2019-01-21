package com.sergivb01.base.command.module.essential;

import com.sergivb01.base.BaseConstants;
import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.util.BukkitUtils;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class PingCommand
		extends BaseCommand{
	public PingCommand(){
		super("ping", "Checks the ping of a player.");
		this.setUsage("/(command) <playerName>");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		Player target;
		if(args.length > 0 && sender.hasPermission(command.getPermission() + ".others")){
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
		sender.sendMessage((target.equals(sender) ? new StringBuilder().append(ChatColor.YELLOW).append("Your ping is").toString() : new StringBuilder().append(ChatColor.YELLOW).append("Ping of ").append(target.getName()).toString()) + ChatColor.GRAY + ": " + ChatColor.WHITE + getPing(target));
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		return args.length == 1 && sender.hasPermission(command.getPermission() + ".others") ? null : Collections.emptyList();
	}

	private int getPing(Player p){
		CraftPlayer cp = (CraftPlayer) p;
		EntityPlayer ep = cp.getHandle();
		return ep.ping;
	}

}

