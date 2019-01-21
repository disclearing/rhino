package com.sergivb01.hcf.deathban.hospital;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.deathban.Deathban;
import com.sergivb01.hcf.user.FactionUser;
import com.sergivb01.hcf.utils.config.ConfigurationService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class HospitalListeners implements Listener {

    private HCF plugin;

    public HospitalListeners(HCF plugin) {
        this.plugin = plugin;
        if(ConfigurationService.DEATHBAN_ROOM) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        FactionUser user = this.plugin.getUserManager().getUser(player.getUniqueId());
        Deathban deathban = user.getDeathban();
        if(deathban == null) {
            return;
        }

        Material mat = Material.valueOf(plugin.getConfig().getString("deathban_room.information_item"));
        Material mat1 = Material.valueOf(plugin.getConfig().getString("deathban_room.use_lives_item"));
        Material mat2 = Material.valueOf(plugin.getConfig().getString("deathban_room.teleport_spawn_item"));

        if(player.getItemInHand().getType().equals(mat)) {
            plugin.getConfig().getStringList("deathban_room.information_message").forEach(string -> {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', string)
                        .replace("%DEATHBAN_REMAINING%" , HCF.getRemaining(deathban.getRemaining(), true, false))
                        .replace("%LIVES%", this.plugin.getDeathbanManager().getLives(player.getUniqueId()) + "")
                );
            });
        } else if(player.getItemInHand().getType().equals(mat1)) {
            if(this.plugin.getDeathbanManager().getLives(player.getUniqueId()) == 0) {
                player.sendMessage(ChatColor.RED + "You don't have any remaining lives.");
                return;
            }
            user.removeDeathban();
            player.teleport(Bukkit.getWorld(plugin.getConfig().getString("deathban_room.world_name")).getSpawnLocation());
            player.getInventory().clear();
            plugin.getDeathbanManager().takeLives(player.getUniqueId(), 1);
            plugin.getConfig().getStringList("deathban_room.use_lives_message").forEach(string -> {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', string)
                        .replace("%LIVES_REMAINING%", this.plugin.getDeathbanManager().getLives(player.getUniqueId()) + "")
                );
            });
        } else if(player.getItemInHand().getType().equals(mat2)) {
            if(deathban == null || !deathban.isActive()) {
                player.teleport(Bukkit.getWorld(plugin.getConfig().getString("deathban_room.world_name")).getSpawnLocation());
                player.getInventory().clear();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("deathban_room.teleport_spawn_message_success")));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("deathban_room.teleport_spawn_message_failed")));
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        FactionUser user = this.plugin.getUserManager().getUser(player.getUniqueId());
        Deathban deathban = user.getDeathban();

        if(deathban == null) {
            return;
        }

        plugin.getConfig().getStringList("deathban_room.allowed_commands").forEach(commands -> {
            if(event.getMessage().contains(commands)) {
                return;
            } else {
                player.sendMessage(ChatColor.RED + "You cannot use this command whilst deathbanned.");
                event.setCancelled(true);
            }
        });
    }
}
