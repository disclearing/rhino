package com.sergivb01.base.command.module.inventory;

import com.sergivb01.base.command.BaseCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IdCommand
		extends BaseCommand{
	public IdCommand(){
		super("id", "Checks the ID/name of an item.");
		this.setUsage("/(command) [itemName]");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(p.getInventory().getItemInHand() != null && p.getItemInHand().getType() != Material.AIR){
				p.sendMessage(ChatColor.YELLOW + "The ID of: " + p.getItemInHand().getType().toString().replace("_", "").toLowerCase() + " is " + p.getItemInHand().getTypeId());
				return true;
			}
			p.sendMessage(ChatColor.RED + "Put something in your hand.");
			return true;
		}
		sender.sendMessage(ChatColor.RED + "You must be a player to execute this.");
		return true;
	}
}

