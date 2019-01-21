package com.sergivb01.hcf.faction.argument;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.events.faction.EventFaction;
import com.sergivb01.hcf.faction.type.Faction;
import com.sergivb01.hcf.timer.type.StuckTimer;
import com.sergivb01.util.command.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionStuckArgument
		extends CommandArgument{
	private final HCF plugin;

	public FactionStuckArgument(HCF plugin){
		super("stuck", "Teleport to a safe position.", new String[]{"trap", "trapped"});
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
		if(player.getWorld().getEnvironment() != World.Environment.NORMAL){
			sender.sendMessage(ChatColor.RED + "You can only use this commands from the overworld.");
			return true;
		}
		Faction factionAt = this.plugin.getFactionManager().getFactionAt(player.getLocation());
		if((factionAt instanceof EventFaction)){
			sender.sendMessage(ChatColor.RED + "You cannot warp whilst in event zones.");
			return true;
		}
		StuckTimer stuckTimer = this.plugin.getTimerManager().stuckTimer;
		if(!stuckTimer.setCooldown(player, player.getUniqueId())){
			sender.sendMessage(ChatColor.RED + "Your " + stuckTimer.getDisplayName() + ChatColor.RED + " timer is already active.");
			return true;
		}
		sender.sendMessage(ChatColor.YELLOW + stuckTimer.getDisplayName() + ChatColor.YELLOW + " timer has started. " + "Teleportation will commence in " + ChatColor.LIGHT_PURPLE + HCF.getRemaining(stuckTimer.getRemaining(player), true, false) + ChatColor.YELLOW + ". " + "This will cancel if you move more than " + 5 + " blocks.");
		return true;
	}
}

