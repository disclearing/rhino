package com.sergivb01.hcf.database.redis.pubsub;

import com.sergivb01.hcf.utils.config.ConfigurationService;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Publisher{
	@Getter
	private JedisPool pool;
	private String channel;

	public Publisher(){
		if(ConfigurationService.REDIS_AUTH){ //Handle auth
			pool = new JedisPool(new JedisPoolConfig(), ConfigurationService.REDIS_HOST, ConfigurationService.REDIS_PORT, 2000, ConfigurationService.REDIS_PASSWORD);
		}else{
			pool = new JedisPool(new JedisPoolConfig(), ConfigurationService.REDIS_HOST, ConfigurationService.REDIS_PORT, 2000);
		}
		this.channel = ConfigurationService.REDIS_CHANNEL;
	}

	public void write(final String message){
		Jedis jedis = null;
		try{
			jedis = pool.getResource();
			if(ConfigurationService.REDIS_AUTH){ //Need to auth every single time we write a message
				jedis.auth(ConfigurationService.REDIS_PASSWORD);
			}
			jedis.publish(channel, message);
			pool.returnResource(jedis);
		}finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}


}