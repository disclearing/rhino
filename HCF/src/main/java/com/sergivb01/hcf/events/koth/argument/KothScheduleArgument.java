package com.sergivb01.hcf.events.koth.argument;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.utils.DateTimeFormats;
import com.sergivb01.util.BukkitUtils;
import com.sergivb01.util.command.CommandArgument;
import net.minecraft.util.org.apache.commons.lang3.text.WordUtils;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class KothScheduleArgument
		extends CommandArgument{
	public static final DateTimeFormatter HHMMA;
	private static final String TIME_UNTIL_PATTERN = "d'd' H'h' mm'm'";
	private static List<String> shownEvents;

	static{
		shownEvents = new ArrayList<>();
		HHMMA = DateTimeFormatter.ofPattern("h:mma");
	}

	private final HCF plugin;

	public KothScheduleArgument(HCF plugin){
		super("schedule", "View the schedule for KOTH arenas");
		this.plugin = plugin;
		this.aliases = new String[]{"info", "i", "time"};
		this.permission = "hcf.commands.koth.argument." + this.getName();
	}

	public String getUsage(String label){
		return "" + '/' + label + ' ' + this.getName();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		LocalDateTime now = LocalDateTime.now(DateTimeFormats.SERVER_ZONE_ID);
		int currentDay = now.getDayOfYear();
		Map<LocalDateTime, String> scheduleMap = this.plugin.getEventScheduler().getScheduleMap();
		ArrayList<String> shownEvents = new ArrayList<String>();
		for(Map.Entry<LocalDateTime, String> entry : scheduleMap.entrySet()){
			LocalDateTime scheduleDateTime = entry.getKey();
			if(!scheduleDateTime.isAfter(now)) continue;
			int dayDifference = scheduleDateTime.getDayOfYear() - currentDay;
			String eventName = entry.getValue();
			String monthName = scheduleDateTime.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
			String weekName = scheduleDateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
			ChatColor colour = dayDifference == 0 ? ChatColor.GREEN : ChatColor.RED;
			shownEvents.add("  " + colour + WordUtils.capitalizeFully(eventName) + ": " + ChatColor.YELLOW + weekName + ' ' + scheduleDateTime.getDayOfMonth() + ' ' + monthName + ChatColor.RED + " (" + HHMMA.format(scheduleDateTime) + ')' + ChatColor.GRAY + " - " + ChatColor.GOLD + DurationFormatUtils.formatDuration(now.until(scheduleDateTime, ChronoUnit.MILLIS), "d'd' H'h' mm'm'"));
		}
		if(shownEvents.isEmpty()){
			sender.sendMessage(ChatColor.RED + "There are no event schedules defined.");
			return true;
		}
		String monthName2 = WordUtils.capitalizeFully(now.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
		String weekName2 = WordUtils.capitalizeFully(now.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
		sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
		sender.sendMessage(ChatColor.YELLOW + "Current Server time " + ChatColor.GREEN + weekName2 + ' ' + now.getDayOfMonth() + ' ' + monthName2 + ' ' + HHMMA.format(now) + ChatColor.YELLOW + '.');
		sender.sendMessage(shownEvents.toArray(new String[shownEvents.size()]));
		return true;
	}
}

