package com.sergivb01.hcf.payloads.types;

import com.sergivb01.hcf.utils.config.ConfigurationService;
import lombok.Getter;
import org.bson.Document;

import java.util.Map;
import java.util.UUID;

@Getter
public class StatusPayload extends Payload{
	private UUID uuid;

	private int online;
	private int max;
	private boolean whitelist,
			donor,
			up;
	private Map<String, UUID> staff;

	public StatusPayload(){
		super("status");
	}

	public StatusPayload(int online, int max, boolean whitelist, boolean donor, boolean up, Map<String, UUID> staff){
		super("status");
		this.uuid = UUID.randomUUID();
		this.online = online;
		this.max = max;
		this.whitelist = whitelist;
		this.donor = donor;
		this.up = up;
		this.staff = staff;
		this.server = ConfigurationService.SERVER_NAME;
	}

	public void fromDocument(Document document){
		this.uuid = (UUID) document.get("uuid");
		this.online = document.getInteger("online");
		this.max = document.getInteger("max");
		this.whitelist = document.getBoolean("whitelist");
		this.donor = document.getBoolean("donor");
		this.up = document.getBoolean("up");
		this.staff = (Map<String, UUID>) document.get("staff");
		this.server = document.getString("server");
	}

	public Document toDocument(){
		return new Document("uuid", uuid)
				.append("online", online)
				.append("max", max)
				.append("whitelist", whitelist)
				.append("donor", donor)
				.append("up", up)
				.append("staff", staff)
				.append("server", server);
	}

	public void broadcast(){
		//Bukkit.broadcastMessage(this.toDocument().toJson());
	}

	public String getServer(){
		return this.server;
	}

	public void setServer(String server){
		this.server = server;
	}

}
