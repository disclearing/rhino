package com.sergivb01.base.command.module.inventory;

import com.sergivb01.base.BaseConstants;
import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand
		extends BaseCommand{
	public GiveCommand(){
		super("give", "Gives an item to a player.");
		this.setUsage("/(command) <playerName> <itemName> [quantity]");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "This command is only executable for players.");
			return true;
		}
		Player p = (Player) sender;
		if(args.length < 2){
			p.sendMessage(ChatColor.RED + this.getUsage());
			return true;
		}
		if(Bukkit.getPlayer(args[0]) == null){
			sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
			return true;
		}
		Integer ammount = 0;
		Player t = Bukkit.getPlayer(args[0]);
		if(BasePlugin.getPlugin().getItemDb().getItem(args[1]) == null){
			sender.sendMessage(ChatColor.RED + "Item or ID not found.");
			return true;
		}
		if(args.length == 2){
			if(!t.getInventory().addItem(new ItemStack[]{BasePlugin.getPlugin().getItemDb().getItem(args[1], BasePlugin.getPlugin().getItemDb().getItem(args[1]).getMaxStackSize())}).isEmpty()){
				p.sendMessage(ChatColor.RED + "The inventory of the player is full.");
				return true;
			}
			ammount = 64;
		}
		if(args.length == 3){
			if(!t.getInventory().addItem(new ItemStack[]{BasePlugin.getPlugin().getItemDb().getItem(args[1], Integer.parseInt(args[2]))}).isEmpty()){
				p.sendMessage(ChatColor.RED + "The inventory of the player is full.");
				return true;
			}
			ammount = Integer.parseInt(args[2]);
		}
		Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "You have given " + t.getName() + " " + ammount.toString() + " " + BasePlugin.getPlugin().getItemDb().getItem(args[1]));
		return true;
	}
}

