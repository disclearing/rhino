package com.sergivb01.hcf.commands.death;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.commands.death.argument.DeathInfoArgument;
import com.sergivb01.hcf.commands.death.argument.DeathRefundArgument;
import com.sergivb01.hcf.commands.death.argument.DeathReviveArgument;
import com.sergivb01.util.command.ArgumentExecutor;

public class DeathExecutor extends ArgumentExecutor{
	public DeathExecutor(HCF plugin){
		super("death");
		this.addArgument(new DeathInfoArgument(plugin));
		this.addArgument(new DeathRefundArgument(plugin));
		this.addArgument(new DeathReviveArgument(plugin));
	}
}
