package com.sergivb01.hcf.faction.argument;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.faction.FactionMember;
import com.sergivb01.hcf.faction.struct.Relation;
import com.sergivb01.hcf.faction.struct.Role;
import com.sergivb01.hcf.faction.type.PlayerFaction;
import com.sergivb01.util.command.CommandArgument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class FactionDemoteArgument
		extends CommandArgument{
	private final HCF plugin;

	public FactionDemoteArgument(HCF plugin){
		super("demote", "Demotes a player to a member.", new String[]{"uncaptain", "delcaptain", "delofficer"});
		this.plugin = plugin;
	}

	public String getUsage(String label){
		return "" + '/' + label + ' ' + this.getName() + " <playerName>";
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "This commands is only executable by players.");
			return true;
		}
		if(args.length < 2){
			sender.sendMessage(ChatColor.RED + "Incorrect usage!" + ChatColor.YELLOW + " Use like this: " + ChatColor.AQUA + this.getUsage(label));
			return true;
		}
		Player player = (Player) sender;
		PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
		if(playerFaction == null){
			sender.sendMessage(ChatColor.RED + "You are not in a faction.");
			return true;
		}
		if(playerFaction.getMember(player.getUniqueId()).getRole() != Role.LEADER){
			sender.sendMessage(ChatColor.RED + "You must be a officer to edit the roster.");
			return true;
		}
		FactionMember targetMember = playerFaction.getMember(args[1]);
		if(targetMember == null){
			sender.sendMessage(ChatColor.RED + "That player is not in your faction.");
			return true;
		}

		if(targetMember.getRole() != Role.CAPTAIN){
			sender.sendMessage(ChatColor.RED + "You can only demote faction captains.");
			return true;
		}
		playerFaction.broadcast(Relation.MEMBER.toChatColour() + targetMember.getName() + ChatColor.YELLOW + " has been demoted from a faction " + targetMember.getRole().toString().toLowerCase() + ".");
		targetMember.setRole(Role.MEMBER);

		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		if(args.length != 2 || !(sender instanceof Player)){
			return Collections.emptyList();
		}
		Player player = (Player) sender;
		PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
		if(playerFaction == null || playerFaction.getMember(player.getUniqueId()).getRole() != Role.LEADER){
			return Collections.emptyList();
		}
		ArrayList<String> results = new ArrayList<String>();
		Set<UUID> keySet = playerFaction.getMembers().keySet();
		for(UUID entry : keySet){
			OfflinePlayer target = Bukkit.getOfflinePlayer(entry);
			String targetName = target.getName();
			if(targetName == null || playerFaction.getMember(target.getUniqueId()).getRole() != Role.CAPTAIN) continue;
			results.add(targetName);
		}
		return results;
	}
}

