package com.sergivb01.base.command.module.essential;

import com.google.common.primitives.Ints;
import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.command.BaseCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ClearLagg
		extends BaseCommand{
	public ClearLagg(){
		super("clearlagg", "Clears the lag on the server");
		this.setAliases(new String[]{"cl", "laggclear", "clearlag", "clag"});
		this.setUsage("/(command) [Delay]");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(args.length == 0){
			sender.sendMessage(this.getUsage(label));
			return true;
		}
		if(args.length == 1){
			if(Ints.tryParse(args[0]) == null){
				sender.sendMessage(ChatColor.RED + "Must be a number");
				return true;
			}
			long duration = Integer.parseInt(args[0]);
			Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Changed the Clear Lag From " + BasePlugin.getPlugin().getServerHandler().getClearlagdelay() + " To " + Ints.tryParse(args[0]), true);
			//BasePlugin.getPlugin().getServerHandler().setClearlagdelay((int)duration);

			BasePlugin.getPlugin().getClearEntityHandler().cancel();
			BasePlugin.getPlugin().getClearEntityHandler().runTaskTimer(BasePlugin.getPlugin(), 20L, duration);
			return true;
		}
		return true;
	}
}

