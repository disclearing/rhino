package com.sergivb01.base.kit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.UUID;

public interface KitManager{
	int UNLIMITED_USES = Integer.MAX_VALUE;

	List<Kit> getKits();

	Kit getKit(String var1);

	Kit getKit(UUID var1);

	boolean containsKit(Kit var1);

	void createKit(Kit var1);

	void removeKit(Kit var1);

	Inventory getGui(Player var1);

	void reloadKitData();

	void saveKitData();
}

