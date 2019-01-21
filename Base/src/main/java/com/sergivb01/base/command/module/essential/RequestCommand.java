package com.sergivb01.base.command.module.essential;

import com.google.common.collect.Maps;
import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.util.chat.ClickAction;
import com.sergivb01.util.chat.Text;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RequestCommand
		extends BaseCommand{
	private static final long REPORT_DELAY_MILLIS = TimeUnit.MINUTES.toMillis(3);
	private final String RECIEVE_MESSAGE;
	private final Map<UUID, Long> reportMap;
	private final HashSet<String> whoReported = new HashSet();

	public RequestCommand(){
		super("request", "Gets the staffs attention");
		this.setUsage("/(command) <Message>");
		this.setAliases(new String[]{"helpop"});
		this.RECIEVE_MESSAGE = "command.request.recieve";
		this.reportMap = Maps.newHashMap();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args){
		if(args.length == 0){
			sender.sendMessage(this.getUsage(s));
			return true;
		}
		Player player = (Player) sender;
		UUID uuid = player.getUniqueId();
		long millis = System.currentTimeMillis();
		Long lastReport = this.reportMap.get(uuid);
		if(lastReport != null && lastReport - millis > 0){
			sender.sendMessage(org.bukkit.ChatColor.RED + "You have already requested help in the last " + DurationFormatUtils.formatDurationWords(REPORT_DELAY_MILLIS, true, true) + '.');
			return true;
		}
		this.reportMap.put(uuid, millis + REPORT_DELAY_MILLIS);

		String message = StringUtils.join(args, ' ');
		for(Player on : Bukkit.getOnlinePlayers()){
			if(!on.hasPermission(this.RECIEVE_MESSAGE)) continue;
			new Text(ChatColor.translateAlternateColorCodes('&', "&e[&aRequest&e] &f" + sender.getName() + " &7requested: &e" + message)).setHoverText(ChatColor.translateAlternateColorCodes('&', "&eClick to teleport to &6" + sender.getName())).setClick(ClickAction.RUN_COMMAND, "/tp " + sender.getName()).send(on);
		}
		sender.sendMessage(ChatColor.GREEN + "Your request has been sent to staff.");
		return true;
	}
}

