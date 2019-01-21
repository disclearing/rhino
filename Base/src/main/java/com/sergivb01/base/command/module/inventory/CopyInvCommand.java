package com.sergivb01.base.command.module.inventory;

import com.sergivb01.base.BaseConstants;
import com.sergivb01.base.StaffPriority;
import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.util.BukkitUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CopyInvCommand
		extends BaseCommand{
	public CopyInvCommand(){
		super("copyinv", "Copies a players inv");
		this.setAliases(new String[]{"copyinventory"});
		this.setUsage("/(command) <playerName>");
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args){
		if(!(cs instanceof Player)){
			cs.sendMessage(ChatColor.RED + "You cannot copy your inventory.");
			return true;
		}
		if(args.length == 0){
			cs.sendMessage(ChatColor.RED + this.getUsage());
			return true;
		}
		Player player = (Player) cs;
		if(args.length == 1){
			Player target = BukkitUtils.playerWithNameOrUUID(args[0]);
			if(cs.equals(target)){
				cs.sendMessage(ChatColor.RED + "You cannot copygit  the inventory of yourself.");
				return true;
			}
			if(target == null || !CopyInvCommand.canSee(player, target)){
				player.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
				return true;
			}
			StaffPriority selfPriority = StaffPriority.of(player);
			if(StaffPriority.of(target).isMoreThan(selfPriority)){
				cs.sendMessage(ChatColor.RED + "You do not have access to check the inventory of that player.");
				return true;
			}
			player.getInventory().setContents(target.getInventory().getContents());
			player.getInventory().setArmorContents(target.getInventory().getArmorContents());
			player.sendMessage(ChatColor.YELLOW + "You have copied the inventory of " + target.getName());
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		return null;
	}
}

