package com.sergivb01.hcf.payloads.types;

import com.sergivb01.hcf.utils.config.ConfigurationService;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.ChatColor;

import java.util.UUID;

@Getter
public class StaffChatPayload extends Payload{
	private UUID uuid;

	private UUID playerUUID;
	private String playerName;
	private String message;

	public StaffChatPayload(){
		super("staffchat");
	}

	public StaffChatPayload(UUID playerUUID, String playerName, String message){
		super("staffchat");
		this.uuid = UUID.randomUUID();
		this.playerUUID = playerUUID;
		this.playerName = playerName;
		this.message = message;
	}

	public void fromDocument(Document document){
		this.uuid = (UUID) document.get("uuid");
		this.playerUUID = (UUID) document.get("playerUUID");
		this.playerName = document.getString("playerName");
		this.message = document.getString("message");
		this.server = document.getString("server");
	}

	public Document toDocument(){
		return new Document("uuid", uuid)
				.append("playerUUID", playerUUID)
				.append("playerName", playerName)
				.append("message", message)
				.append("server", server);
	}


	public void broadcast(){
		getStaffMembers().forEach(p -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigurationService.STAFFCHAT
				.replace("%PLAYER%", playerName)
				.replace("%MESSAGE%", message)
				.replace("%SERVER%", server)
		)));
	}
}
