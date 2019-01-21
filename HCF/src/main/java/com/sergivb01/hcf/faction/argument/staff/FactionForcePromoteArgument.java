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

public class FactionForcePromoteArgument
		extends CommandArgument{
	private final HCF plugin;

	public FactionForcePromoteArgument(HCF plugin){
		super("forcepromote", "Forces the promotion status of a player.");
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
		if(factionMember.getRole() != Role.MEMBER){
			sender.sendMessage(ChatColor.RED + factionMember.getName() + " is already a " + factionMember.getRole().getName() + '.');
			return true;
		}
		factionMember.setRole(Role.CAPTAIN);
		playerFaction.broadcast(ChatColor.GOLD.toString() + ChatColor.BOLD + sender.getName() + " has been forcefully assigned as a captain.");
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		return args.length == 2 ? null : Collections.emptyList();
	}
}

