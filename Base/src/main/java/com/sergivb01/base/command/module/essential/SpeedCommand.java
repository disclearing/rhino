package com.sergivb01.base.command.module.essential;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Floats;
import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.util.BukkitUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SpeedCommand
		extends BaseCommand{
	private static final float DEFAULT_FLIGHT_SPEED = 2.0f;
	private static final float DEFAULT_WALK_SPEED = 1.0f;
	private static final ImmutableList<String> COMPLETIONS_FIRST = ImmutableList.of("fly", "walk");
	private static final ImmutableList<String> COMPLETIONS_SECOND = ImmutableList.of("reset");

	public SpeedCommand(){
		super("speed", "Sets the fly/walk speed of a player.");
		this.setUsage("/(command) <fly|walk> <speedMultiplier|reset> [playerName]");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		block19:
		{
			Boolean flight;
			Player target;
			Float multiplier;
			if(args.length < 2){
				sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
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
			if(args[0].equalsIgnoreCase("fly")){
				flight = true;
			}else{
				if(!args[0].equalsIgnoreCase("walk")){
					sender.sendMessage(this.getUsage(label));
					return true;
				}
				flight = false;
			}
			if(args[1].equalsIgnoreCase("reset")){
				multiplier = Float.valueOf(flight != false ? 2.0f : 1.0f);
			}else{
				multiplier = Floats.tryParse(args[1]);
				if(multiplier == null){
					sender.sendMessage(ChatColor.RED + "Invalid speed multiplier: '" + args[1] + "'.");
					return true;
				}
			}
			if(flight.booleanValue()){
				float flySpeed = 0.1f * multiplier.floatValue();
				try{
					target.setFlySpeed(flySpeed);
					Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Flight speed of " + target.getName() + " has been set to " + multiplier + '.');
					return true;
				}catch(IllegalArgumentException ex){
					if(flySpeed < 0.1f){
						sender.sendMessage(ChatColor.RED + "Speed multiplier too low: " + multiplier);
					}else if(flySpeed > 0.1f){
						sender.sendMessage(ChatColor.RED + "Speed multiplier too high: " + multiplier);
					}
					break block19;
				}
			}
			float walkSpeed = 0.2f * multiplier.floatValue();
			try{
				target.setWalkSpeed(walkSpeed);
				Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Walking speed of " + target.getName() + " has been set to " + multiplier + '.');
				return true;
			}catch(IllegalArgumentException ex){
				if(walkSpeed < 0.2f){
					sender.sendMessage(ChatColor.RED + "Speed multiplier too low: " + multiplier);
				}
				if(walkSpeed <= 0.2f) break block19;
				sender.sendMessage(ChatColor.RED + "Speed multiplier too high: " + multiplier);
			}
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		switch(args.length){
			case 1:{
				return BukkitUtils.getCompletions(args, COMPLETIONS_FIRST);
			}
			case 2:{
				return BukkitUtils.getCompletions(args, COMPLETIONS_SECOND);
			}
			case 3:{
				return null;
			}
		}
		return Collections.emptyList();
	}
}

