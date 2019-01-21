package com.sergivb01.hcf.timer;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.timer.argument.TimerCheckArgument;
import com.sergivb01.hcf.timer.argument.TimerSetArgument;
import com.sergivb01.util.command.ArgumentExecutor;

public class TimerExecutor extends ArgumentExecutor{

	public TimerExecutor(HCF plugin){
		super("timer");
		this.addArgument(new TimerCheckArgument(plugin));
		this.addArgument(new TimerSetArgument(plugin));
	}


}

