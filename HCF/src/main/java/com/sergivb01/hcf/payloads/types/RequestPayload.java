package com.sergivb01.hcf.payloads.types;

import com.sergivb01.hcf.utils.config.ConfigurationService;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.ChatColor;

import java.util.UUID;

@Getter
public class RequestPayload extends Payload{
	private UUID uuid;

	private String playerName;
	private UUID playerUUID;
	private String reason;

	public RequestPayload(){
		super("request");
	}

	public RequestPayload(String playerName, UUID playerUUID, String reason){
		super("request");
		this.uuid = UUID.randomUUID();
		this.playerName = playerName;
		this.playerUUID = playerUUID;
		this.reason = reason;
	}

	public void fromDocument(Document document){
		this.uuid = (UUID) document.get("uuid");
		this.playerName = document.getString("playerName");
		this.playerUUID = (UUID) document.get("playerUUID");
		this.reason = document.getString("reason");
		this.server = document.getString("server");
	}

	public Document toDocument(){
		return new Document("playerName", playerName)
				.append("playerUUID", playerUUID)
				.append("reason", reason)
				.append("uuid", uuid)
				.append("server", server);
	}

	public void broadcast(){
		getStaffMembers().forEach(p -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigurationService.REQUEST
				.replace("%PLAYER%",playerName)
				.replace("%REASON%", reason)
				.replace("%SERVER%", server)
		)));
	}

}

