package com.sergivb01.hcf.faction.argument.staff;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.faction.FactionMember;
import com.sergivb01.hcf.faction.struct.Role;
import com.sergivb01.hcf.faction.type.PlayerFaction;
import com.sergivb01.util.command.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class FactionForceKickArgument
		extends CommandArgument{
	private final HCF plugin;

	public FactionForceKickArgument(HCF plugin){
		super("forcekick", "Forcefully kick a player from their faction.");
		this.plugin = plugin;
		this.permission = "hcf.commands.faction.argument." + this.getName();
	}

	public String getUsage(String label){
		return "" + '/' + label + ' ' + this.getName() + " <playerName>";
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(args.length < 2){
			sender.sendMessage(ChatColor.RED + "Incorrect usage!" + ChatColor.YELLOW + " Use like this: " + ChatColor.AQUA + this.getUsage(label));
			return true;
		}
		PlayerFaction playerFaction = this.plugin.getFactionManager().getContainingPlayerFaction(args[1]);
		if(playerFaction == null){
			sender.sendMessage(ChatColor.RED + "Faction containing member with IGN or UUID " + args[1] + " not found.");
			return true;
		}
		FactionMember factionMember = playerFaction.getMember(args[1]);
		if(factionMember == null){
			sender.sendMessage(ChatColor.RED + "Faction containing member with IGN or UUID " + args[1] + " not found.");
			return true;
		}
		if(factionMember.getRole() == Role.LEADER){
			sender.sendMessage(ChatColor.RED + "You cannot forcefully kick faction leaders.");
			return true;
		}
		if(playerFaction.setMember(factionMember.getUniqueId(), null, true)){
			playerFaction.broadcast(ChatColor.GOLD.toString() + ChatColor.BOLD + factionMember.getName() + " has been forcefully kicked by " + sender.getName() + '.');
		}
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		return args.length == 2 ? null : Collections.emptyList();
	}
}

