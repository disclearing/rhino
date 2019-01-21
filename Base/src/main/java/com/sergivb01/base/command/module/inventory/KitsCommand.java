package com.sergivb01.base.command.module.inventory;

import com.sergivb01.base.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class KitsCommand
		extends BaseCommand{
	public KitsCommand(){
		super("kits", "Alias");
		this.setUsage("/(command)");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		Bukkit.dispatchCommand(sender, "gkit");
		return true;
	}
}