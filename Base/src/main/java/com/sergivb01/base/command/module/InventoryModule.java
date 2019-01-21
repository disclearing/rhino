package com.sergivb01.base.command.module;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.command.BaseCommandModule;
import com.sergivb01.base.command.module.inventory.*;

public class InventoryModule
		extends BaseCommandModule{
	public InventoryModule(BasePlugin plugin){
		this.commands.add(new ClearInvCommand());
		this.commands.add(new GiveCommand());
		this.commands.add(new IdCommand());
		this.commands.add(new InvSeeCommand(plugin));
		this.commands.add(new ItemCommand());
		this.commands.add(new KitsCommand());
		this.commands.add(new MoreCommand());
		this.commands.add(new SkullCommand());
		this.commands.add(new CopyInvCommand());
	}
}

