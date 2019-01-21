package com.sergivb01.hcf.commands.crate;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.commands.crate.argument.*;
import com.sergivb01.util.command.ArgumentExecutor;

public class LootExecutor
		extends ArgumentExecutor{
	public LootExecutor(HCF plugin){
		super("loot");
		this.addArgument(new LootBankArgument(plugin));
		this.addArgument(new LootBroadcastsArgument());
		this.addArgument(new LootDepositArgument(plugin));
		this.addArgument(new LootGiveArgument(plugin));
		this.addArgument(new LootListArgument(plugin));
		this.addArgument(new LootWithdrawArgument(plugin));
	}
}

