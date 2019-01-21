package com.sergivb01.base.command;

import com.google.common.collect.Sets;

import java.util.Set;

public abstract class BaseCommandModule{
	protected final Set<BaseCommand> commands = Sets.newHashSet();
	protected boolean enabled = true;

	Set<BaseCommand> getCommands(){
		return this.commands;
	}

	void unregisterCommand(BaseCommand command){
		this.commands.remove(command);
	}

	void unregisterCommands(){
		this.commands.clear();
	}

	boolean isEnabled(){
		return this.enabled;
	}

	void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
}

