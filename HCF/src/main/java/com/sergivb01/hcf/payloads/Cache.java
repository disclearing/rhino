package com.sergivb01.hcf.payloads;

import com.sergivb01.hcf.payloads.types.Payload;
import com.sergivb01.hcf.payloads.types.StatusPayload;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Cache{
	public static Map<UUID, Long> commandDelay = new HashMap<>();
	public static Map<String, StatusPayload> serverStatuses = new HashMap<>();
	public static List<Payload> payloads = new ArrayList<>();

	public static boolean canExecute(Player player){
		return !commandDelay.containsKey(player.getUniqueId()) || System.currentTimeMillis() - commandDelay.get(player.getUniqueId()) > TimeUnit.MINUTES.toMillis(5);
	}

	public static void addPlayerDelay(Player player){
		commandDelay.put(player.getUniqueId(), System.currentTimeMillis());
	}

	public static boolean addPayload(Payload payload){
		return payloads.add(payload);
	}

	public static void setStatus(StatusPayload payload){
		String server = payload.getServer();
		if(serverStatuses.containsKey(server)){
			if(serverStatuses.get(server).isUp() != payload.isUp()){
				Bukkit.broadcastMessage("Server " + server + " is now " + (payload.isUp() ? "online" : "offline") + "!");
			}
		}
		serverStatuses.put(server, payload);
	}

	public StatusPayload getServerStatus(String server){
		return serverStatuses.getOrDefault(server, null);
	}


}
