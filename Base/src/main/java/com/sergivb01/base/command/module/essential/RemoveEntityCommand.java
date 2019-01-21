package com.sergivb01.base.command.module.essential;

import com.google.common.base.Enums;
import com.google.common.base.Optional;
import com.google.common.primitives.Ints;
import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.util.BukkitUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RemoveEntityCommand
		extends BaseCommand{
	public RemoveEntityCommand(){
		super("removeentity", "Removes all of a specific entity.");
		this.setUsage("/(command) <worldName> <entityType> [removeCustomNamed] [radius]");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		boolean removeCustomNamed;
		Integer radius;
		if(args.length < 2){
			sender.sendMessage(this.getUsage(label));
			return true;
		}
		World world = Bukkit.getWorld(args[0]);
		Optional optionalType = Enums.getIfPresent((Class) EntityType.class, args[1].toUpperCase());
		if(!optionalType.isPresent()){
			sender.sendMessage(ChatColor.RED + "Not an entity named '" + args[1] + "'.");
			return true;
		}
		EntityType entityType = (EntityType) optionalType.get();
		if(entityType == EntityType.PLAYER){
			sender.sendMessage(ChatColor.RED + "You cannot remove " + entityType.name() + " entities!");
			return true;
		}
		boolean bl = removeCustomNamed = args.length > 2 && Boolean.parseBoolean(args[2]);
		if(args.length > 3){
			radius = Ints.tryParse(args[3]);
			if(radius == null){
				sender.sendMessage(ChatColor.RED + "'" + args[3] + "' is not a number.");
				return true;
			}
			if(radius <= 0){
				sender.sendMessage(ChatColor.RED + "Radius must be positive.");
				return true;
			}
		}else{
			radius = 0;
		}
		Location location = sender instanceof Player ? ((Player) sender).getLocation() : null;
		int removed = 0;
		for(Chunk chunk : world.getLoadedChunks()){
			for(Entity entity : chunk.getEntities()){
				LivingEntity livingEntity;
				if(entity.getType() != entityType || radius != 0 && (location == null || location.distanceSquared(entity.getLocation()) > (double) radius.intValue()) || !removeCustomNamed && (entity instanceof Tameable && ((Tameable) entity).isTamed() || entity instanceof LivingEntity && (livingEntity = (LivingEntity) entity).getCustomName() != null))
					continue;
				entity.remove();
				++removed;
			}
		}
		sender.sendMessage(ChatColor.YELLOW + "Removed " + removed + " of " + entityType.getName() + '.');
		return true;
	}

	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args){
		List<String> results = null;
		switch(args.length){
			case 1:{
				final Collection<World> worlds = Bukkit.getWorlds();
				results = new ArrayList<String>(worlds.size());
				for(final World world : worlds){
					results.add(world.getName());
				}
				break;
			}
			case 2:{
				EntityType[] entityTypes = EntityType.values();
				results = new ArrayList(entityTypes.length);
				for(EntityType entityType : entityTypes){
					if(entityType == EntityType.UNKNOWN || entityType == EntityType.PLAYER) continue;
					results.add(entityType.name());
				}
				break;
			}
			default:{
				return Collections.emptyList();
			}
		}
		return BukkitUtils.getCompletions(args, results);
	}
}

