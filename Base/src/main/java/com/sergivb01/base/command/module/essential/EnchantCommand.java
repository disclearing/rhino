package com.sergivb01.base.command.module.essential;

import com.google.common.primitives.Ints;
import com.sergivb01.base.BaseConstants;
import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.util.BukkitUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnchantCommand
		extends BaseCommand{
	public EnchantCommand(){
		super("enchant", "Adds enchantment to items.");
		this.setUsage("/(command) <enchantment> <level> [playerName]");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		String itemName;
		Player target;
		if(args.length < 2){
			sender.sendMessage(this.getUsage());
			return true;
		}
		if(args.length > 2 && sender.hasPermission(command.getPermission() + ".others")){
			target = BukkitUtils.playerWithNameOrUUID(args[2]);
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
		Enchantment enchantment = Enchantment.getByName(args[0]);
		if(enchantment == null){
			sender.sendMessage(ChatColor.RED + "No enchantment named '" + args[0] + "' found.");
			return true;
		}
		ItemStack stack = target.getItemInHand();
		if(stack == null || stack.getType() == Material.AIR){
			sender.sendMessage(ChatColor.RED + target.getName() + " is not holding an item.");
			return true;
		}
		Integer level = Ints.tryParse(args[1]);
		if(level == null){
			sender.sendMessage(ChatColor.RED + "'" + args[1] + "' is not a number.");
			return true;
		}
		int maxLevel = enchantment.getMaxLevel();
		if(level > maxLevel && !sender.hasPermission(command.getPermission() + ".abovemaxlevel")){
			sender.sendMessage(ChatColor.RED + "The maximum enchantment level for " + enchantment.getName() + " is " + maxLevel + '.');
			return true;
		}
		if(!enchantment.canEnchantItem(stack) && !sender.hasPermission(command.getPermission() + ".anyitem")){
			sender.sendMessage(ChatColor.RED + "Enchantment " + enchantment.getName() + " cannot be applied to that item.");
			return true;
		}
		stack.addUnsafeEnchantment(enchantment, level.intValue());
		try{
			itemName = CraftItemStack.asNMSCopy(stack).getName();
		}catch(Error ex){
			itemName = stack.getType().name();
		}
		Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Enchanted " + enchantment.getName() + " at level " + level + " onto " + itemName + " of " + target.getName() + '.');
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		switch(args.length){
			case 1:{
				Enchantment[] enchantments = Enchantment.values();
				ArrayList<String> results = new ArrayList<String>(enchantments.length);
				for(Enchantment enchantment : enchantments){
					results.add(enchantment.getName());
				}
				return BukkitUtils.getCompletions(args, results);
			}
			case 3:{
				return null;
			}
		}
		return Collections.emptyList();
	}
}

