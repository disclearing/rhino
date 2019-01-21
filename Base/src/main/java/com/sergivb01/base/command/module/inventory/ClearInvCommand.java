package com.sergivb01.base.command.module.inventory;

import com.sergivb01.base.BaseConstants;
import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.util.BukkitUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Collections;
import java.util.List;

public class ClearInvCommand
		extends BaseCommand{
	public ClearInvCommand(){
		super("ci", "Clears a players inventory.");
		this.setAliases(new String[]{"clear", "clearinventory"});
		this.setUsage("/(command) <playerName>");
	}

	@Override
	public boolean isPlayerOnlyCommand(){
		return true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		Player target;
		if(args.length > 0 && sender.hasPermission(command.getPermission() + ".others")){
			target = BukkitUtils.playerWithNameOrUUID(args[0]);
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
		PlayerInventory targetInventory = target.getInventory();
		targetInventory.clear();
		targetInventory.setArmorContents(new ItemStack[]{new ItemStack(Material.AIR, 1), new ItemStack(Material.AIR, 1), new ItemStack(Material.AIR, 1), new ItemStack(Material.AIR, 1)});
		Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Cleared inventory of player " + target.getName() + '.');
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		return args.length == 1 ? null : Collections.emptyList();
	}
}

