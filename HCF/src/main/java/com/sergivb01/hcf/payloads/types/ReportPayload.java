package com.sergivb01.hcf.payloads.types;

import com.sergivb01.hcf.utils.config.ConfigurationService;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.ChatColor;

import java.util.UUID;

@Getter
public class ReportPayload extends Payload{
	private UUID uuid;

	private String reporterName;
	private UUID reporterUUID;

	private String reportedName;
	private UUID reportedUUID;

	private String reason;

	public ReportPayload(){
		super("report");
	}

	public ReportPayload(String reporterName, UUID reporterUUID, String reportedName, UUID reportedUUID, String reason){
		super("report");
		this.uuid = UUID.randomUUID();
		this.reporterName = reporterName;
		this.reporterUUID = reporterUUID;
		this.reportedName = reportedName;
		this.reportedUUID = reportedUUID;
		this.reason = reason;
	}

	public void fromDocument(Document document){
		this.uuid = (UUID) document.get("uuid");
		this.reporterName = document.getString("reporterName");
		this.reporterUUID = (UUID) document.get("reporterUUID");
		this.reportedName = document.getString("reportedName");
		this.reportedUUID = (UUID) document.get("reportedUUID");
		this.reason = document.getString("reason");
		this.server = document.getString("server");
	}

	public Document toDocument(){
		return new Document("reporterName", reportedName)
				.append("reporterUUID", reportedUUID)
				.append("reportedName", reportedName)
				.append("reportedUUID", reportedUUID)
				.append("reason", reason)
				.append("uuid", uuid)
				.append("server", server);
	}

	public void broadcast(){
		getStaffMembers().forEach(p -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigurationService.REPORT
				.replace("%REPORTER%", reporterName)
				.replace("%REPORTED%", reportedName)
				.replace("%REASON%", reason)
				.replace("%SERVER%", server)
		)));
	}


}
