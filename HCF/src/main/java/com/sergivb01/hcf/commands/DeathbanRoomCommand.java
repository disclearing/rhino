package com.sergivb01.hcf.commands;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.utils.config.ConfigurationService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class DeathbanRoomCommand implements CommandExecutor {

    private HCF plugin;

    public DeathbanRoomCommand(HCF plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(!ConfigurationService.DEATHBAN_ROOM) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEnable the &c&ldeathban room&c option in the config in order to enable this command"));
            return true;
        }

        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "only players");
            return true;
        }

        Player p = (Player) commandSender;

        if(strings.length != 1) {
            commandSender.sendMessage(ChatColor.RED + "Usage: /" + s + " setspawn");
            return true;
        }

        if(!strings[0].equalsIgnoreCase("setspawn")) {
            commandSender.sendMessage(ChatColor.RED + "Usage: /" + s + " setspawn");
            return true;
        }
            plugin.getConfig().set("deathban_room.world_name", (Object)p.getLocation().getWorld().getName());
            plugin.getConfig().set("deathban_room.x", (Object)p.getLocation().getX());
            plugin.getConfig().set("deathban_room.y", (Object)p.getLocation().getY());
            plugin.getConfig().set("deathban_room.z", (Object)p.getLocation().getZ());
            plugin.getConfig().set("deathban_room.yaw", (Object)p.getLocation().getYaw());
            plugin.getConfig().set("deathban_room.pitch", (Object)p.getLocation().getPitch());
            try {
                plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            commandSender.sendMessage(ChatColor.GREEN + "Successfully set the deathbanroom spawn point.");
            return true;
        }
}
