package com.sergivb01.hcf.utils.runnables;

import com.sergivb01.hcf.HCF;
import com.sergivb01.util.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class DonorBroadcastRunnable{

	public DonorBroadcastRunnable() {
		new BukkitRunnable() {
			@Override
			public void run() {
				List<String> donators = Bukkit.getOnlinePlayers().stream()
						.filter(p -> p.hasPermission("hcf.utils.donor") && !p.isOp() && !p.hasPermission("*"))
						.map(Player::getName)
						.collect(Collectors.toList());

				HCF.getInstance().getConfig().getStringList("online-donors")
						.forEach(s ->
								Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', s
										.replace("%LINE%", BukkitUtils.STRAIGHT_LINE_DEFAULT + "")
										.replace("%COUNT%", String.valueOf(donators.size()))
										.replace("%MEDICS%", donators.isEmpty() ? "&cNone" :
												HCF.getInstance().getConfig().getString("donor_prefix") + donators.toString()
														.replace("[", "")
														.replace("]", "")))));

			}
		}.runTaskTimer(HCF.getInstance(), 20L, 1000L);
	}

}
