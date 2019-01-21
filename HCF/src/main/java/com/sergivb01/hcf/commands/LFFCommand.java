package com.sergivb01.hcf.commands;

import com.sergivb01.hcf.HCF;
import net.minecraft.util.gnu.trove.map.TObjectLongMap;
import net.minecraft.util.gnu.trove.map.hash.TObjectLongHashMap;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class LFFCommand implements CommandExecutor {

    private HCF plugin;
    public static TObjectLongMap<UUID> cooldown;

    public LFFCommand(HCF plugin) {
        this.plugin = plugin;

        cooldown = new TObjectLongHashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Console can't run this.");
            return true;
        }

        final Player player = (Player) sender;

        final long timestamp = LFFCommand.cooldown.get(player.getUniqueId());
        final long millis = System.currentTimeMillis();
        final long remaining = (timestamp == this.cooldown.getNoEntryValue()) ? -1L : (timestamp - millis);

        if(remaining > 0L) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cYou are currently on a cooldown for &l" + DurationFormatUtils.formatDurationWords(remaining, true, true) + "."));
            return true;
        }
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',"&7&m---*-------------------------------*---"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',"&3&l" + player.getName() + " &ais looking for a &lfaction&a!"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',"&7&m---*-------------------------------*---"));
        cooldown.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30L));
        return true;
    }
}

