package com.sergivb01.base.listener;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.sergivb01.util.cuboid.CoordinatePair;
import net.minecraft.util.gnu.trove.iterator.TObjectIntIterator;
import net.minecraft.util.gnu.trove.map.hash.TObjectIntHashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

public class MobstackListener extends BukkitRunnable implements Listener{
	private static final String STACKED_PREFIX = ChatColor.GREEN.toString() + "x";
	private final Table<CoordinatePair, EntityType, Integer> naturalSpawnStacks;
	private final TObjectIntHashMap<Location> spawnerStacks;

	public MobstackListener(){
		this.naturalSpawnStacks = HashBasedTable.create();
		this.spawnerStacks = new TObjectIntHashMap<>();
	}

	private CoordinatePair fromLocation(Location location){
		return new CoordinatePair(location.getWorld(), 81 * Math.round(location.getBlockX() / 81), 81 * Math.round(location.getBlockZ() / 81));
	}

	public void run(){
		for(World world : Bukkit.getServer().getWorlds()){
			if(world.getEnvironment() != World.Environment.THE_END){
				for(LivingEntity entity : world.getLivingEntities()){
					if(entity.getType().equals(EntityType.ENDERMAN) || entity.getType().equals(EntityType.SLIME))
						return;
					if((entity.isValid()) && (!(entity instanceof Player))){
						for(Entity nearby : entity.getNearbyEntities(8.0D, 8.0D, 8.0D)){
							if(((nearby instanceof LivingEntity)) && (nearby.isValid()) && (!(nearby instanceof Player))){
								stack(entity, (LivingEntity) nearby);
							}
						}
					}
				}
			}
		}
	}


	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onSpawnerSpawn(SpawnerSpawnEvent event){
		if(event.getEntityType().equals(EntityType.ENDERMAN) || event.getEntityType().equals(EntityType.SLIME)){
			return;
		}

		CreatureSpawner spawner = event.getSpawner();
		World world = spawner.getWorld();
		if((world != null) || (world != null && world.getEnvironment().equals(World.Environment.THE_END))){
			return;
		}
		Location location = spawner.getLocation();
		Optional<Integer> entityIdOptional = Optional.of(this.spawnerStacks.get(location));
		int entityId = entityIdOptional.get();
		net.minecraft.server.v1_7_R4.Entity nmsTarget = ((CraftWorld) location.getWorld()).getHandle().getEntity(entityId);
		Entity target = nmsTarget != null ? nmsTarget.getBukkitEntity() : null;
		if(((target instanceof LivingEntity))){
			LivingEntity targetLiving = (LivingEntity) target;
			int stackedQuantity = getStackedQuantity(targetLiving);
			if(stackedQuantity == -1){
				stackedQuantity = 1;
			}
			if(stackedQuantity <= 200){
				setStackedQuantity(targetLiving, ++stackedQuantity);
				event.setCancelled(true);
				return;
			}
		}
		this.spawnerStacks.put(location, event.getEntity().getEntityId());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		EntityType entityType = event.getEntityType();

		if(entityType.equals(EntityType.ENDERMAN) || entityType.equals(EntityType.SLIME)){
			return;
		}

		switch(event.getSpawnReason()){
			case CHUNK_GEN:
			case NATURAL:
			case DEFAULT:
				Location location = event.getLocation();
				CoordinatePair coordinatePair = fromLocation(location);
				Optional<Integer> entityIdOptional = Optional.ofNullable(this.naturalSpawnStacks.get(coordinatePair, entityType));
				if(entityIdOptional.isPresent()){
					int entityId = entityIdOptional.get();
					net.minecraft.server.v1_7_R4.Entity nmsTarget = ((CraftWorld) location.getWorld()).getHandle().getEntity(entityId);
					Entity target = nmsTarget == null ? null : nmsTarget.getBukkitEntity();
					if(((target instanceof LivingEntity))){
						LivingEntity targetLiving = (LivingEntity) target;
						boolean canSpawn;
						if((targetLiving instanceof Ageable)){
							canSpawn = ((Ageable) targetLiving).isAdult();
						}else{
							canSpawn = (!(targetLiving instanceof Zombie)) || (!((Zombie) targetLiving).isBaby());
						}
						if(canSpawn){
							int stackedQuantity = getStackedQuantity(targetLiving);
							if(stackedQuantity == -1){
								stackedQuantity = 1;
							}
							if(stackedQuantity < 200){
								setStackedQuantity(targetLiving, ++stackedQuantity);
								event.setCancelled(true);
								return;
							}
						}
					}
				}
				this.naturalSpawnStacks.put(coordinatePair, entityType, event.getEntity().getEntityId());
				break;
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onEntityDeath(EntityDeathEvent event){
		LivingEntity livingEntity = event.getEntity();
		int stackedQuantity = getStackedQuantity(livingEntity);
		if(stackedQuantity > 1){
			LivingEntity respawned = (LivingEntity) livingEntity.getWorld().spawnEntity(livingEntity.getLocation(), event.getEntityType());
			setStackedQuantity(respawned, Math.min(200, --stackedQuantity));
			if((respawned instanceof Ageable)){
				((Ageable) respawned).setAdult();
			}
			if((respawned instanceof Zombie)){
				((Zombie) respawned).setBaby(false);
			}
			if(this.spawnerStacks.containsValue(livingEntity.getEntityId())){
				TObjectIntIterator<Location> iterator = this.spawnerStacks.iterator();
				while(iterator.hasNext()){
					iterator.advance();
					if(iterator.value() == livingEntity.getEntityId()){
						iterator.setValue(respawned.getEntityId());
					}
				}
			}
		}
	}

	private int getStackedQuantity(LivingEntity livingEntity){
		String customName = livingEntity.getCustomName();
		if((customName != null) && (customName.contains(STACKED_PREFIX))){
			customName = customName.replace(STACKED_PREFIX, "");
			customName = ChatColor.stripColor(customName);
			return Integer.parseInt(customName);
		}
		return -1;
	}

	private boolean stack(LivingEntity tostack, LivingEntity toremove){
		Integer newStack = 1;
		Integer removeStack = 1;
		if(hasStack(tostack)){
			newStack = getStackedQuantity(tostack);
		}
		if(hasStack(toremove)){
			removeStack = getStackedQuantity(toremove);
		}else if((getStackedQuantity(toremove) == -1) &&
				(toremove.getCustomName() != null) && (toremove.getCustomName().contains(ChatColor.WHITE.toString()))){
			return false;
		}
		if(toremove.getType() != tostack.getType()){
			return false;
		}
		if((toremove.getType() == EntityType.SLIME) || (toremove.getType() == EntityType.MAGMA_CUBE) || (tostack.getType() == EntityType.SLIME) || (tostack.getType() == EntityType.MAGMA_CUBE) || tostack.getType() == EntityType.VILLAGER){
			return false;
		}
		toremove.remove();
		setStackedQuantity(tostack, Math.min(newStack + removeStack, 200));
		return true;
	}

	private boolean hasStack(LivingEntity livingEntity){
		return getStackedQuantity(livingEntity) != -1;
	}

	private void setStackedQuantity(LivingEntity livingEntity, int quantity){
		if(quantity > 200){
			return;
		}
		if(quantity < 1){
			return;
		}
		if(quantity <= 1){
			livingEntity.setCustomName(null);
		}else{
			livingEntity.setCustomName(STACKED_PREFIX + quantity);
			livingEntity.setCustomNameVisible(false);
		}
	}

}


