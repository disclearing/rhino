package com.sergivb01.hcf.events.fury;


import com.sergivb01.hcf.HCF;
import com.sergivb01.util.command.ArgumentExecutor;

public class FuryExecutor
		extends ArgumentExecutor{
	public FuryExecutor(HCF plugin){
		super("fury");
		//	this.addArgument(new ConquestSetpointsArgument(plugin));
	}
}