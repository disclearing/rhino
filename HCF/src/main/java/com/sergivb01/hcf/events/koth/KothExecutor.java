package com.sergivb01.hcf.events.koth;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.events.koth.argument.KothNextArgument;
import com.sergivb01.hcf.events.koth.argument.KothScheduleArgument;
import com.sergivb01.hcf.events.koth.argument.KothSetCapDelayArgument;
import com.sergivb01.hcf.events.koth.argument.KothShowArgument;
import com.sergivb01.util.command.ArgumentExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class KothExecutor
		extends ArgumentExecutor{
	private final KothScheduleArgument kothScheduleArgument;

	public KothExecutor(HCF plugin){
		super("koth");
		this.addArgument(new KothNextArgument(plugin));
		this.addArgument(new KothShowArgument());
		this.kothScheduleArgument = new KothScheduleArgument(plugin);
		this.addArgument(this.kothScheduleArgument);
		this.addArgument(new KothSetCapDelayArgument(plugin));
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(args.length < 1){
			this.kothScheduleArgument.onCommand(sender, command, label, args);
			return true;
		}
		return super.onCommand(sender, command, label, args);
	}
}

