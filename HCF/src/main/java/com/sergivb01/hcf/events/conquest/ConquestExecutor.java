package com.sergivb01.hcf.events.conquest;

import com.sergivb01.hcf.HCF;
import com.sergivb01.util.command.ArgumentExecutor;

public class ConquestExecutor
		extends ArgumentExecutor{
	public ConquestExecutor(HCF plugin){
		super("conquest");
		this.addArgument(new ConquestSetpointsArgument(plugin));
	}
}

