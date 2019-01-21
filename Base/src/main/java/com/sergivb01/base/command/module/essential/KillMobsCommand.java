package com.sergivb01.base.command.module.essential;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class KillMobsCommand extends BaseCommand{
	private BasePlugin plugin;

	public KillMobsCommand(BasePlugin plugin){
		super("killmobs", "Kills mobs in all worlds.");
		this.plugin = plugin;
		this.setUsage("/(command)");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args){
		new Thread(() -> {
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
				int removed = 0;
				for(World w : Bukkit.getServer().getWorlds()){
					for(Entity e : w.getEntities()){
						EntityType entityType = e.getType();
						if(entityType.equals(EntityType.DROPPED_ITEM) || entityType.equals(EntityType.PRIMED_TNT) || entityType.equals(EntityType.SKELETON) || entityType.equals(EntityType.ZOMBIE) || entityType.equals(EntityType.COW) || entityType.equals(EntityType.SHEEP) || entityType.equals(EntityType.ENDERMAN) || entityType.equals(EntityType.PIG_ZOMBIE) || entityType.equals(EntityType.PIG) || entityType.equals(EntityType.SKELETON) || entityType.equals(EntityType.CREEPER) || entityType.equals(EntityType.BAT) || entityType.equals(EntityType.CHICKEN) || entityType.equals(EntityType.ENDER_DRAGON)){
							e.remove();
							removed++;
						}
					}
				}
				sender.sendMessage(ChatColor.YELLOW + "You have removed a total of " + ChatColor.GOLD + removed + ChatColor.YELLOW + " entities");
			});
		}).start();

		return true;
	}

}
