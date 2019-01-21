package com.sergivb01.hcf.faction.argument;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.faction.struct.Role;
import com.sergivb01.hcf.faction.type.PlayerFaction;
import com.sergivb01.util.command.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionDisbandArgument
		extends CommandArgument{
	private final HCF plugin;

	public FactionDisbandArgument(HCF plugin){
		super("disband", "Disband your faction.");
		this.plugin = plugin;
	}

	public String getUsage(String label){
		return "" + '/' + label + ' ' + this.getName();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "This commands is only executable by players.");
			return true;
		}
		Player player = (Player) sender;
		PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
		if(playerFaction == null){
			sender.sendMessage(ChatColor.RED + "You are not in a faction.");
			return true;
		}
		if(playerFaction.isRaidable() && !this.plugin.getEotwHandler().isEndOfTheWorld()){
			sender.sendMessage(ChatColor.RED + "You cannot disband your faction while it is raidable.");
			return true;
		}
		if(playerFaction.getMember(player.getUniqueId()).getRole() != Role.LEADER){
			sender.sendMessage(ChatColor.RED + "You must be a leader to disband the faction.");
			return true;
		}
		this.plugin.getFactionManager().removeFaction(playerFaction, sender);
		return true;
	}
}

