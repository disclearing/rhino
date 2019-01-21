package com.sergivb01.base.command.module.essential;

import com.sergivb01.base.command.BaseCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class NearCommand extends BaseCommand{

	public NearCommand(){
		super("near", "Count entities near a player.");
		this.setUsage("/(command) <playerName>");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "No.");
			return false;
		}
		Player p = (Player) sender;
		List<String> nearby = getNearbyEnemies(p);
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &eNearby players: &a(" + nearby.size() + ")"));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&l * &7" + (nearby.isEmpty() ? "None" : nearby.toString().replace("[", "").replace("]", ""))));
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		return args.length == 1 ? null : Collections.emptyList();
	}

	private List<String> getNearbyEnemies(final Player player){
		final List<String> players = new ArrayList<>();
		final Collection<Entity> nearby = player.getNearbyEntities(50.0, 50.0, 50.0);
		for(final Entity entity : nearby){
			if(entity instanceof Player){
				final Player target = (Player) entity;
				if(!target.canSee(player)){
					continue;
				}
				if(!player.canSee(target)){
					continue;
				}
				if(target.hasPotionEffect(PotionEffectType.INVISIBILITY)){
					continue;
				}
				players.add(target.getName());
			}
		}
		return players;
	}
}

