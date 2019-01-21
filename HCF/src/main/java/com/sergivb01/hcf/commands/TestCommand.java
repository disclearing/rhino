package com.sergivb01.hcf.commands;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.tab.PlayerTab;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor{

	public boolean onCommand(CommandSender sender, Command command, String s, String[] args){
		int style = Integer.parseInt(args[0]);
		HCF.getInstance().getUserManager().getUser(((Player) sender).getUniqueId()).setTabStyle(style);
		sender.sendMessage("Style set to " + style);
		PlayerTab.clean.add((Player) sender);
		return true;
	}


}
