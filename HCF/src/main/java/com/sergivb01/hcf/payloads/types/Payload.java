package com.sergivb01.hcf.payloads.types;

import com.sergivb01.hcf.database.mongo.MongoManager;
import com.sergivb01.hcf.database.redis.RedisManager;
import com.sergivb01.hcf.utils.config.ConfigurationService;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.stream.Collectors;

public abstract class Payload{
	String server;
	private String type;

	public Payload(String type){
		this.type = type;
		this.server = ConfigurationService.SERVER_NAME;
	}

	@Override
	public boolean equals(Object obj){
		return super.equals(obj);
	}

	public void send(){
		Document document = this.toDocument()
				.append("type", type)
				.append("server", server)
				.append("timestamp", System.currentTimeMillis());

		if(!type.equals("status") && ConfigurationService.MONGO_ENABLED) MongoManager.addPayload(document);

		RedisManager.publisher.write("payload;" +
				document.toJson()
		);
	}

	public Collection<Player> getStaffMembers(){
		return Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("hcf.utils.staff")).collect(Collectors.toList());
	}

	public abstract void fromDocument(Document document);

	public abstract Document toDocument();

	public abstract void broadcast();


}
