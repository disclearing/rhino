package com.sergivb01.base.command;

public interface CommandManager{
	boolean containsCommand(BaseCommand var1);

	void registerAll(BaseCommandModule var1);

	void registerCommand(BaseCommand var1);

	void registerCommands(BaseCommand[] var1);

	void unregisterCommand(BaseCommand var1);

	BaseCommand getCommand(String var1);
}

