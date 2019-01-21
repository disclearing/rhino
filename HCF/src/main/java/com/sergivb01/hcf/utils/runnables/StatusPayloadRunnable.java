package com.sergivb01.hcf.utils.runnables;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.hcf.payloads.types.Payload;
import com.sergivb01.hcf.payloads.types.StatusPayload;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class StatusPayloadRunnable implements Runnable{

	public void run(){
		Map<String, UUID> map = new HashMap<>(Bukkit.getOnlinePlayers().stream()
				.filter(p -> p.hasPermission("hcf.utils.staff"))
				.collect(Collectors.toMap(HumanEntity::getName, Entity::getUniqueId)));

		Payload payload = new StatusPayload(Bukkit.getOnlinePlayers().size(),
				Bukkit.getMaxPlayers(),
				Bukkit.hasWhitelist(),
				BasePlugin.getPlugin().getServerHandler().isDonorOnly(),
				false,
				map
		);
		payload.send();
	}


}
