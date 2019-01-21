package com.sergivb01.base.command.module.essential;

import com.sergivb01.base.command.BaseCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class LockdownCommand extends BaseCommand{
	private final String TO_BE_ON_LIST_PERMISSION;

	public LockdownCommand(){
		super("lockdown", "Puts the server on high alert");
		this.setUsage("/(command)");
		this.TO_BE_ON_LIST_PERMISSION = "command.lockdown";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		return true;
	}

}
