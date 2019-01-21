// 

// 

package com.sergivb01.base.command.module.essential;

import com.google.common.collect.ImmutableSet;
import com.sergivb01.base.BaseConstants;
import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.util.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class HealCommand extends BaseCommand{
	private static final Set<PotionEffectType> HEALING_REMOVEABLE_POTION_EFFECTS;

	static{
		HEALING_REMOVEABLE_POTION_EFFECTS = ImmutableSet.of(PotionEffectType.SLOW, PotionEffectType.SLOW_DIGGING, PotionEffectType.POISON, PotionEffectType.WEAKNESS);
	}

	public HealCommand(){
		super("heal", "Heals a player.");
		this.setUsage("/(command) <playerName>");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args){
		Player onlyTarget = null;
		Collection<Player> targets;
		if(args.length > 0 && sender.hasPermission(command.getPermission() + ".others")){
			if(args[0].equalsIgnoreCase("all") && sender.hasPermission(command.getPermission() + ".all")){
				targets = ImmutableSet.copyOf(Bukkit.getOnlinePlayers());
			}else{
				if((onlyTarget = BukkitUtils.playerWithNameOrUUID(args[0])) == null || !BaseCommand.canSee(sender, onlyTarget)){
					sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
					return true;
				}
				targets = ImmutableSet.of(onlyTarget);
			}
		}else{
			if(!(sender instanceof Player)){
				sender.sendMessage(this.getUsage(label));
				return true;
			}
			targets = ImmutableSet.of((onlyTarget = (Player) sender));
		}
		final double maxHealth;
		if(onlyTarget != null && (maxHealth = onlyTarget.getHealth()) == onlyTarget.getMaxHealth()){
			sender.sendMessage(ChatColor.RED + onlyTarget.getName() + " already has full health (" + maxHealth + ").");
			return true;
		}
		for(final Player target : targets){
			target.setHealth(target.getMaxHealth());
			for(final PotionEffectType type : HealCommand.HEALING_REMOVEABLE_POTION_EFFECTS){
				target.removePotionEffect(type);
			}
			target.setFireTicks(0);
		}
		Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Healed " + ((onlyTarget == null) ? "all online players" : onlyTarget.getName()) + '.');
		return true;
	}

	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args){
		return (args.length == 1) ? null : Collections.emptyList();
	}
}
