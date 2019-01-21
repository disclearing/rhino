package com.sergivb01.base.command.module.essential;

import com.google.common.base.Strings;
import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.util.JavaUtils;
import com.sergivb01.util.command.CommandArgument;
import com.sergivb01.util.command.CommandWrapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class AutoRestartCommand
		extends BaseCommand{
	private final CommandWrapper handler;

	public AutoRestartCommand(BasePlugin plugin){
		super("autorestart", "Allows management of server restarts.");
		this.setUsage("/(command) <cancel|time|schedule>");
		ArrayList<CommandArgument> arguments = new ArrayList<CommandArgument>(3);
		arguments.add(new AutoRestartCancelArgument(plugin));
		arguments.add(new AutoRestartScheduleArgument(plugin));
		arguments.add(new AutoRestartTimeArgument(plugin));
		arguments.sort(new CommandWrapper.ArgumentComparator());
		this.handler = new CommandWrapper(arguments);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		return this.handler.onCommand(sender, command, label, args);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		return this.handler.onTabComplete(sender, command, label, args);
	}

	private static class AutoRestartTimeArgument
			extends CommandArgument{
		private final BasePlugin plugin;

		public AutoRestartTimeArgument(BasePlugin plugin){
			super("time", "Gets the remaining time until next restart.");
			this.plugin = plugin;
			this.aliases = new String[]{"remaining", "time"};
		}

		@Override
		public String getUsage(String label){
			return "" + '/' + label + ' ' + this.getName();
		}

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
			String reason;
			if(!this.plugin.getAutoRestartHandler().isPendingRestart()){
				sender.sendMessage(ChatColor.RED + "There is not a restart task pending.");
				return true;
			}
			sender.sendMessage(ChatColor.AQUA + "Automatic restart task occurring in " + DurationFormatUtils.formatDurationWords(this.plugin.getAutoRestartHandler().getRemainingMilliseconds(), true, true) + (Strings.nullToEmpty(reason = this.plugin.getAutoRestartHandler().getReason()).isEmpty() ? "" : new StringBuilder().append(" for ").append(reason).toString()) + '.');
			return true;
		}
	}

	private static class AutoRestartScheduleArgument
			extends CommandArgument{
		private final BasePlugin plugin;

		public AutoRestartScheduleArgument(BasePlugin plugin){
			super("schedule", "Schedule an automatic restart.");
			this.plugin = plugin;
			this.aliases = new String[]{"reschedule"};
		}

		@Override
		public String getUsage(String label){
			return "" + '/' + label + ' ' + this.getName() + " <time> [reason]";
		}

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
			if(args.length < 2){
				sender.sendMessage(ChatColor.RED + "Usage: /" + label + ' ' + args[0].toLowerCase() + " <time> [reason]");
				return true;
			}
			long millis = JavaUtils.parse(args[1]);
			if(millis == -1){
				sender.sendMessage(ChatColor.RED + "Invalid duration, use the correct format: 10m1s");
				return true;
			}
			String reason = StringUtils.join(args, ' ', 2, args.length);
			this.plugin.getAutoRestartHandler().scheduleRestart(millis, reason);
			Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Scheduled a restart to occur in " + DurationFormatUtils.formatDurationWords(millis, true, true) + (reason.isEmpty() ? "" : new StringBuilder().append(" for ").append(reason).toString()) + '.');
			return true;
		}
	}

	private static class AutoRestartCancelArgument
			extends CommandArgument{
		private final BasePlugin plugin;

		public AutoRestartCancelArgument(BasePlugin plugin){
			super("cancel", "Cancels the current automatic restart.");
			this.plugin = plugin;
		}

		@Override
		public String getUsage(String label){
			return "" + '/' + label + ' ' + this.getName();
		}

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
			if(!this.plugin.getAutoRestartHandler().isPendingRestart()){
				sender.sendMessage(ChatColor.RED + "There is not a restart task pending.");
				return true;
			}
			this.plugin.getAutoRestartHandler().cancelRestart();
			sender.sendMessage(ChatColor.YELLOW + "Automatic restart task cancelled.");
			return true;
		}
	}

}

