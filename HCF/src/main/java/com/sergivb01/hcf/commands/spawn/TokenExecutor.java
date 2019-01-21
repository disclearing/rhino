package com.sergivb01.hcf.commands.spawn;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.commands.spawn.argument.TokenCheckArgument;
import com.sergivb01.hcf.commands.spawn.argument.TokenGiveArgument;
import com.sergivb01.hcf.commands.spawn.argument.TokenSetArgument;
import com.sergivb01.util.command.ArgumentExecutor;

public class TokenExecutor extends ArgumentExecutor{
	public TokenExecutor(HCF plugin){
		super("token");
		this.addArgument(new TokenCheckArgument(plugin));
		this.addArgument(new TokenSetArgument(plugin));
		this.addArgument(new TokenGiveArgument(plugin));
	}
}
