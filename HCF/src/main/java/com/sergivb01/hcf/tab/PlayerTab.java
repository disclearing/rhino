package com.sergivb01.hcf.tab;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.faction.type.Faction;
import com.sergivb01.hcf.faction.type.PlayerFaction;
import com.sergivb01.hcf.tab.com.bizarrealex.azazel.Azazel;
import com.sergivb01.hcf.tab.com.bizarrealex.azazel.tab.TabAdapter;
import com.sergivb01.hcf.tab.com.bizarrealex.azazel.tab.TabTemplate;

import com.sergivb01.hcf.user.TabStyles;
import com.sergivb01.hcf.user.UserManager;
import com.sergivb01.hcf.utils.config.ConfigurationService;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class PlayerTab implements TabAdapter, Listener{
	public static List<Player> clean = new ArrayList<>();
	private HCF plugin;
	private Azazel azazel;

	private UserManager userManager;

	public PlayerTab(HCF plugin){
		this.plugin = plugin;
		this.azazel = new Azazel(plugin, this);

		this.userManager = HCF.getInstance().getUserManager();
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public TabTemplate getTemplate(Player player){
		if(clean.remove(player)){
			return getClearTemplate();
		}

		Faction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
		TabTemplate line = new TabTemplate();

		int intxlocation = (int) player.getLocation().getX();
		int intzlocation = (int) player.getLocation().getZ();

		if (plugin.getFactionManager().getPlayerFaction(player) == null) {
			line.left(1, "&7You do not");
			line.left(2, "&7have a faction");
			line.left(3, "&7/f create <name>");
		} else {
			line.left(1, "&7Online: " + plugin.getFactionManager().getPlayerFaction(player).getOnlineMembers().size());
			line.left(2, "&7DTR: " + (double)plugin.getFactionManager().getPlayerFaction(player).getDeathsUntilRaidable());
			line.left(3, "&7Balance: $" + plugin.getFactionManager().getPlayerFaction(player).getBalance());
			if(plugin.getFactionManager().getPlayerFaction(player).getHome() == null) {
				line.left(4, "&7Home: None");
			} else {
				int locx = (int) plugin.getFactionManager().getPlayerFaction(player).getHome().getX();
				int locz = (int) plugin.getFactionManager().getPlayerFaction(player).getHome().getZ();
				line.left(4, "&7Home: " + locx + ", " + locz);
			}
			line.middle(2, "&6&l" + plugin.getFactionManager().getPlayerFaction(player).getName());
			line.middle(3, "&aLeader: &2" + plugin.getFactionManager().getPlayerFaction(player).getLeader().getName());

		}
		line.left(0, "&6Faction Info");
		line.left(6, "");
		line.left(7, "&6Player Info");
		line.left(8, "&7Kills: " + player.getStatistic(Statistic.PLAYER_KILLS));
		line.left(9, "&7Deaths: " + player.getStatistic(Statistic.DEATHS));
		line.left(10, "");
		line.left(11, "&6Location");
		line.left(12, plugin.getFactionManager().getFactionAt(player.getLocation()).getDisplayName(player));
		line.left(13, "&7" + intxlocation + ", " + intzlocation);

		line.middle(0, "&6&l" + ConfigurationService.SERVER_NAME);

		line.right(0, "&6End Portals");
		line.right(1, "&71000 1000");
		line.right(2, "&7in each quadrant");
		line.right(4, "&6Map Kit");
		line.right(5, "&7Protection " + ConfigurationService.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL));
		line.right(6, "&7Sharpness " + ConfigurationService.ENCHANTMENT_LIMITS.get(Enchantment.DAMAGE_ALL));
		line.right(8, "&6Map Border");
		line.right(9, "&7" + ConfigurationService.BORDER_SIZES.get(World.Environment.NORMAL) + " x " + ConfigurationService.BORDER_SIZES.get(World.Environment.NORMAL));
		line.right(11, "&6Players Online");
		line.right(12, "&7" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers());
		return line;
	}




		private TabTemplate getClearTemplate(){
		TabTemplate tabTemplate = new TabTemplate();
		for(int i = 0; i < 20; i++){
			tabTemplate.left(0, "");
			tabTemplate.middle(0, "");
			tabTemplate.right(0, "");
			tabTemplate.farRight(0, "");
		}
		return tabTemplate;
	}


}
