package com.sergivb01.hcf.faction.argument;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.faction.claim.Claim;
import com.sergivb01.hcf.faction.struct.Role;
import com.sergivb01.hcf.faction.type.PlayerFaction;
import com.sergivb01.util.command.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionClaimChunkArgument
		extends CommandArgument{
	private static final int CHUNK_RADIUS = 7;
	private final HCF plugin;

	public FactionClaimChunkArgument(HCF plugin){
		super("claimchunk", "Claim a chunk of land in the Wilderness.", new String[]{"chunkclaim"});
		this.plugin = plugin;
	}

	public String getUsage(String label){
		return "" + '/' + label + ' ' + this.getName();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "This commands is only executable by players.");
			return true;
		}
		Player player = (Player) sender;
		PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
		if(playerFaction == null){
			sender.sendMessage(ChatColor.RED + "You are not in a faction.");
			return true;
		}
		if(playerFaction.isRaidable()){
			sender.sendMessage(ChatColor.RED + "You cannot claim land for your faction while raidable.");
			return true;
		}
		if(playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER){
			sender.sendMessage(ChatColor.RED + "You must be an officer to claim land.");
			return true;
		}
		Location location = player.getLocation();
		this.plugin.getClaimHandler().tryPurchasing(player, new Claim(playerFaction, location.clone().add(7.0, 0.0, 7.0), location.clone().add(-7.0, 256.0, -7.0)));
		return true;
	}
}

