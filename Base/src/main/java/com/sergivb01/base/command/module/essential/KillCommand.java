package com.sergivb01.base.command.module.essential;

import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.util.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Collections;
import java.util.List;

public class KillCommand
		extends BaseCommand{
	public KillCommand(){
		super("kill", "Kills a player.");
		this.setAliases(new String[]{"slay"});
		this.setUsage("/(command) <playerName>");
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
		if(target.isDead()){
			sender.sendMessage(ChatColor.RED + target.getName() + " is already dead.");
			return true;
		}
		EntityDamageEvent event = new EntityDamageEvent(target, EntityDamageEvent.DamageCause.SUICIDE, 10000);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()){
			sender.sendMessage(ChatColor.RED + "You cannot kill " + target.getName() + '.');
			return true;
		}
		target.setLastDamageCause(event);
		target.setHealth(0.0);
		if(sender.equals(target)){
			sender.sendMessage(ChatColor.GOLD + "You have been killed.");
			return true;
		}
		Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Slain player " + target.getName() + '.');
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		return args.length == 1 && sender.hasPermission(command.getPermission() + ".others") ? null : Collections.emptyList();
	}
}

