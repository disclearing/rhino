package com.sergivb01.hcf.events.argument;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.events.faction.EventFaction;
import com.sergivb01.hcf.events.faction.KothFaction;
import com.sergivb01.hcf.events.palace.PalaceFaction;
import com.sergivb01.hcf.faction.type.Faction;
import com.sergivb01.util.command.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class EventListArgument extends CommandArgument{
	private final HCF plugin;

	public EventListArgument(HCF plugin){
		super("list", "Check the uptime of an event");
		this.plugin = plugin;
		this.permission = "hcf.commands.event.argument." + this.getName();
	}

	public String getUsage(String label){
		return "" + '/' + label + ' ' + this.getName();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		List<Faction> events = this.plugin.getFactionManager().getFactions().stream().filter(faction -> faction instanceof EventFaction).collect(Collectors.toList());

		sender.sendMessage(ChatColor.GREEN + "Current events:");
		for(Faction factionEvent : events){
			sender.sendMessage(ChatColor.GREEN + factionEvent.getName() + ChatColor.DARK_GRAY +
					" (" + ChatColor.YELLOW + getFactionEventType(factionEvent) + ChatColor.DARK_GRAY + ")");
		}

		return true;
	}

	private String getFactionEventType(Faction factionEvent){
		if(factionEvent instanceof KothFaction){
			return "Koth";
		}else if(factionEvent instanceof PalaceFaction){
			return "Palace";
		}else{
			return "Conquest";
		}

	}


}

