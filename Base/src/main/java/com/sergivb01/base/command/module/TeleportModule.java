package com.sergivb01.base.command.module;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.command.BaseCommandModule;
import com.sergivb01.base.command.module.teleport.*;
import com.sergivb01.base.command.module.warp.WarpExecutor;

public class TeleportModule
		extends BaseCommandModule{
	public TeleportModule(BasePlugin plugin){
		this.commands.add(new LobbyCommand(plugin));
		this.commands.add(new BackCommand(plugin));
		this.commands.add(new TeleportCommand());
		this.commands.add(new TeleportAllCommand());
		this.commands.add(new TeleportHereCommand());
		this.commands.add(new TopCommand());
		this.commands.add(new WorldCommand());
		this.commands.add(new WarpExecutor(plugin));
	}
}

