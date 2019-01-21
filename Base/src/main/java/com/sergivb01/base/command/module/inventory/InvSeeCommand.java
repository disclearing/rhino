package com.sergivb01.base.command.module.inventory;

import com.sergivb01.base.BaseConstants;
import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.StaffPriority;
import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.util.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class InvSeeCommand
		extends BaseCommand
		implements Listener{
	private final InventoryType[] types = new InventoryType[]{InventoryType.BREWING, InventoryType.CHEST, InventoryType.DISPENSER, InventoryType.ENCHANTING, InventoryType.FURNACE, InventoryType.HOPPER, InventoryType.PLAYER, InventoryType.WORKBENCH};
	private final Map<InventoryType, Inventory> inventories = new EnumMap<InventoryType, Inventory>(InventoryType.class);

	public InvSeeCommand(BasePlugin plugin){
		super("invsee", "View the inventory of a player.");
		this.setAliases(new String[]{"inventorysee", "inventory", "inv"});
		this.setUsage("/(command) <playerName>");
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public boolean isPlayerOnlyCommand(){
		return true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(!(sender instanceof Player)){
			if(args.length < 1){
				sender.sendMessage(this.getUsage(label));
				return true;
			}
			Player target = BukkitUtils.playerWithNameOrUUID(args[0]);
			if(target == null){
				sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
				return true;
			}
			sender.sendMessage(ChatColor.YELLOW + "This players inventory contains: ");
			for(ItemStack items : target.getInventory().getContents()){
				if(items == null) continue;
				sender.sendMessage(ChatColor.AQUA + items.getType().toString().replace("_", "").toLowerCase() + ": " + items.getAmount());
			}
			return true;
		}
		if(args.length < 1){
			sender.sendMessage(this.getUsage(label));
			return true;
		}
		Player player = (Player) sender;
		Inventory inventory = null;
		for(InventoryType type : this.types){
			if(!type.name().equalsIgnoreCase(args[0])) continue;
			Inventory inventoryRevert = Bukkit.createInventory(player, type);
			inventory = this.inventories.putIfAbsent(type, inventoryRevert);
			if(inventory != null) break;
			inventory = inventoryRevert;
			break;
		}
		if(inventory == null){
			Player target = BukkitUtils.playerWithNameOrUUID(args[0]);
			if(sender.equals(target)){
				sender.sendMessage(ChatColor.RED + "You cannot check the inventory of yourself.");
				return true;
			}
			if(target == null || !BaseCommand.canSee(sender, target)){
				sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
				return true;
			}
			StaffPriority selfPriority = StaffPriority.of(player);
			if(StaffPriority.of(target).isMoreThan(selfPriority)){
				sender.sendMessage(ChatColor.RED + "You do not have access to check the inventory of that player.");
				return true;
			}
			inventory = target.getInventory();
		}
		player.openInventory(inventory);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		if(args.length != 1){
			return Collections.emptyList();
		}
		InventoryType[] values = InventoryType.values();
		ArrayList<String> results = new ArrayList<String>(values.length);
		Player senderPlayer = sender instanceof Player ? (Player) sender : null;
		for(Player target : Bukkit.getOnlinePlayers()){
			if(senderPlayer != null && !senderPlayer.canSee(target)) continue;
			results.add(target.getName());
		}
		return BukkitUtils.getCompletions(args, results);
	}
}

