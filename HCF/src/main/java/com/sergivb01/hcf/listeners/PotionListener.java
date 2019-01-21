package com.sergivb01.hcf.listeners;

import com.sergivb01.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.Vector;

public class PotionListener implements Listener{

	public PotionListener(HCF plugin){
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	void onProjectileLaunch(ProjectileLaunchEvent event){
		if(event.getEntityType() == EntityType.SPLASH_POTION){
			Projectile projectile = event.getEntity();
			if(((projectile.getShooter() instanceof Player)) && (((Player) projectile.getShooter()).isSprinting())){
				Vector velocity = projectile.getVelocity();

				velocity.setY(velocity.getY() - 1.25);
				projectile.setVelocity(velocity);
			}
		}
	}

	@EventHandler
	void onPotionSplash(PotionSplashEvent event){
		if((event.getEntity().getShooter() instanceof Player)){
			Player shooter = (Player) event.getEntity().getShooter();
			if((shooter.isSprinting()) && (event.getIntensity(shooter) > 0.5D)){
				event.setIntensity(shooter, 1.0D);
			}
		}
	}
}
