package com.sergivb01.base.command.module.inventory;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.command.BaseCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemCommand
		extends BaseCommand{
	public ItemCommand(){
		super("item", "Spawns an item.");
		this.setAliases(new String[]{"i", "get"});
		this.setUsage("/(command) <itemName> [quantity]");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "This command is only executable for players.");
			return true;
		}
		String amount = "";
		Player p = (Player) sender;
		if(args.length == 0){
			p.sendMessage(ChatColor.RED + this.getUsage());
			return true;
		}
		if(BasePlugin.getPlugin().getItemDb().getItem(args[0]) == null){
			sender.sendMessage(ChatColor.RED + "Item or ID not found.");
			return true;
		}
		if(args.length == 1){
			if(!p.getInventory().addItem(new ItemStack[]{BasePlugin.getPlugin().getItemDb().getItem(args[0], BasePlugin.getPlugin().getItemDb().getItem(args[0]).getMaxStackSize())}).isEmpty()){
				p.sendMessage(ChatColor.RED + "Your inventory is full.");
				return true;
			}
			amount = "" + BasePlugin.getPlugin().getItemDb().getItem(args[0]).getMaxStackSize() + "";
		}
		if(args.length == 2){
			if(!p.getInventory().addItem(new ItemStack[]{BasePlugin.getPlugin().getItemDb().getItem(args[0], Integer.parseInt(args[1]))}).isEmpty()){
				p.sendMessage(ChatColor.RED + "Your inventory is full.");
				return true;
			}
			amount = args[1];
		}
		Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "You have given yourself " + amount + " " + BasePlugin.getPlugin().getItemDb().getName(BasePlugin.getPlugin().getItemDb().getItem(args[0])), true);
		return true;
	}
}

