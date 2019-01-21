package com.sergivb01.hcf.database.redis;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.database.redis.pubsub.Publisher;
import com.sergivb01.hcf.database.redis.pubsub.Subscriber;
import lombok.Getter;


public class RedisManager{
	@Getter
	public static Publisher publisher;
	@Getter
	public static Subscriber subscriber;
	private HCF plugin;

	public RedisManager(HCF plugin){
		this.plugin = plugin;
		init();
	}

	private void init(){
		publisher = new Publisher();
		subscriber = new Subscriber(plugin);
		plugin.getLogger().info("Registered sub and pub");
	}


}