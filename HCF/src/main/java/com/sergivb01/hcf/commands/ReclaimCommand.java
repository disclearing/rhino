package com.sergivb01.hcf.commands;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.user.FactionUser;
import com.sergivb01.hcf.utils.config.ConfigurationService;
import me.joeleoli.nucleus.player.NucleusPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ReclaimCommand implements CommandExecutor{
	private final HCF plugin;

	public ReclaimCommand(HCF plugin){
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		Player p = (Player) sender;
		FileConfiguration config = HCF.getPlugin().getConfig();
		if(ConfigurationService.NUCLEUS) {
			if(NucleusPlayer.getByUuid(p.getUniqueId()).getActiveRank() == null) {
				sender.sendMessage(ChatColor.RED + "You don't have a rank.");
				return true;
			}
			String groupName = NucleusPlayer.getByUuid(p.getUniqueId()).getActiveRank().getName();
			String groups = "reclaim.groups.";

			FactionUser user = HCF.getPlugin().getUserManager().getUser(p.getUniqueId());

			if(config.getString(groups + groupName.toLowerCase()) == null){
				p.sendMessage(ChatColor.RED + "You don't have anything to reclaim!");
			}else{
				if(user.isReclaimed()){
					p.sendMessage(ChatColor.RED + "You have already reclaimed your rewards.");
				}else{
					user.setReclaimed(true);

					for(String reclaimCommand : config.getStringList(groups + groupName.toLowerCase())){
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), reclaimCommand
								.replace("{PLAYER}", p.getName())
								.replace("{RANK}", groupName)
						);
					}

					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', config.getString("reclaim.message")
							.replace("{PLAYER}", p.getName())
							.replace("{RANK}", groupName))
					);
				}
			}
			return true;
		}
		if(HCF.permission.getPrimaryGroup(p) == null) {
			sender.sendMessage(ChatColor.RED + "You don't have a rank.");
			return true;
		}
		String groupName = HCF.permission.getPrimaryGroup(p);
		String groups = "reclaim.groups.";

		FactionUser user = HCF.getPlugin().getUserManager().getUser(p.getUniqueId());

		if(config.getString(groups + groupName.toLowerCase()) == null){
			p.sendMessage(ChatColor.RED + "You don't have anything to reclaim!");
		}else{
			if(user.isReclaimed()){
				p.sendMessage(ChatColor.RED + "You have already reclaimed your rewards.");
			}else{
				user.setReclaimed(true);

				for(String reclaimCommand : config.getStringList(groups + groupName.toLowerCase())){
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), reclaimCommand
							.replace("{PLAYER}", p.getName())
							.replace("{RANK}", groupName)
					);
				}

				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', config.getString("reclaim.message")
						.replace("{PLAYER}", p.getName())
						.replace("{RANK}", groupName))
				);
			}
		}
		return false;
	}

}