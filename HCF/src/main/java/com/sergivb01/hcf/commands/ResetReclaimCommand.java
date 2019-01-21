package com.sergivb01.hcf.commands;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.user.FactionUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetReclaimCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length != 1) {
            commandSender.sendMessage(ChatColor.RED + "Usage: /resetreclaim <player:all>");
            return true;
        }
            OfflinePlayer p = Bukkit.getPlayer(strings[0]);

            if(p == null) {
                commandSender.sendMessage(ChatColor.RED + "That player has never joined the server before!");
                return true;
            }

            FactionUser user = HCF.getPlugin().getUserManager().getUser(p.getUniqueId());
            user.setReclaimed(false);
            commandSender.sendMessage(ChatColor.GREEN + "Successfully reset " + strings[0] + "'s reclaim.");
            return true;
    }
}
