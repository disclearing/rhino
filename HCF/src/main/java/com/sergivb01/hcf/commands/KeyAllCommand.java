package com.sergivb01.hcf.commands;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.timer.type.KeyAllTimer;
import com.sergivb01.util.BukkitUtils;
import com.sergivb01.util.JavaUtils;
import net.minecraft.util.com.google.common.collect.ImmutableList;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class KeyAllCommand implements CommandExecutor {

    private static final List<String> COMPLETIONS;

    static {
        COMPLETIONS = ImmutableList.of("start", "end");
    }

    private final HCF plugin;

    public KeyAllCommand(final HCF plugin) {
        super();
        this.plugin = plugin;
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("start")) {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /" + label + " " + "start h:m:s ");
                    sender.sendMessage(ChatColor.RED + "Example: /keyall start 3h3m");
                    return true;
                }
                final long duration = JavaUtils.parse(args[1]);
                if (duration == -1L) {
                    sender.sendMessage(ChatColor.RED + "'" + args[0] + "' is an invalid duration.");
                    return true;
                }
                if (duration < 1000L) {
                    sender.sendMessage(ChatColor.RED + "Key All time must last for at least 20 ticks.");
                    return true;
                }
                final KeyAllTimer.KeyAllRunnable keyAllRunnable = this.plugin.getKeyAllTimer().getkeyAllRunnable();
                if (keyAllRunnable != null) {
                    sender.sendMessage(ChatColor.RED + "Key All is already enabled, use /" + label + " cancel to end it.");
                    return true;
                }
                this.plugin.getKeyAllTimer().start(duration);
                //sender.sendMessage(ConfigurationService.SOTW_STARTED.replace("%time%", DurationFormatUtils.formatDurationWords(duration, true, true)));
                sender.sendMessage(ChatColor.GREEN + "Started the key all for " + DurationFormatUtils.formatDurationWords(duration, true, true) + ".");
                return true;
            } else if (args[0].equalsIgnoreCase("end") || args[0].equalsIgnoreCase("cancel")) {
                if (this.plugin.getKeyAllTimer().cancel()) {
                    //sender.sendMessage(ConfigurationService.SOTW_CANCELLED);
                    sender.sendMessage(ChatColor.RED + "The key all has ended!");
                    return true;
                }
                sender.sendMessage(ChatColor.RED + "The key all is already active!");
                //sender.sendMessage(ConfigurationService.SOTW_NOT_ACTIVE);
                return true;
            }
        }
        sender.sendMessage(ChatColor.RED + "Usage: /" + label + " " + "start h:m:s ");
        sender.sendMessage(ChatColor.RED + "Example: /keyall start 3h3m");
        return true;
    }

    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1) ? BukkitUtils.getCompletions(args, KeyAllCommand.COMPLETIONS) : Collections.emptyList();
    }
}