package com.sergivb01.base.command.module.chat;

import com.sergivb01.util.BukkitUtils;
import com.sergivb01.util.chat.ClickAction;
import com.sergivb01.util.chat.Text;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatCommands implements Listener{

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent event){
		String command = event.getMessage();
		Player player = event.getPlayer();
		String spacebar = ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT;
		switch(command){
			case "?help":
				event.setCancelled(true);
				player.sendMessage(spacebar);
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9&lChatCommands list &7(Click)"));
				new Text(ChatColor.translateAlternateColorCodes('&', " &7* &b?claim")).setHoverText(ChatColor.BLUE + "Click to ?claim").setClick(ClickAction.RUN_COMMAND, "?claim").send(player);
				new Text(ChatColor.translateAlternateColorCodes('&', " &7* &b?elevator")).setHoverText(ChatColor.BLUE + "Click to ?elevator").setClick(ClickAction.RUN_COMMAND, "?elevator").send(player);
				new Text(ChatColor.translateAlternateColorCodes('&', " &7* &b?potions")).setHoverText(ChatColor.BLUE + "Click to ?potions").setClick(ClickAction.RUN_COMMAND, "?potions").send(player);
				new Text(ChatColor.translateAlternateColorCodes('&', " &7* &b?links")).setHoverText(ChatColor.BLUE + "Click to ?links").setClick(ClickAction.RUN_COMMAND, "?links").send(player);
				new Text(ChatColor.translateAlternateColorCodes('&', " &7* &b?teamspeak")).setHoverText(ChatColor.BLUE + "Click to ?teamspeak").setClick(ClickAction.RUN_COMMAND, "?teamspeak").send(player);
				player.sendMessage(spacebar);
				break;
			case "?claim":
				event.setCancelled(true);
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						spacebar +
								"&9&lClaiming tutorial\n" +
								"&rTo start claiming you must type &b/f claim &rand you will get a claiming wand. &7&o(Hover over the item for more information)&r.\n" +
								" \n" +
								" &7* &3&lUsing the wand:\n" +
								" &r&lLeft &rclick on the first corner of the land you want to claim.\n" +
								" &r&lRight &rclick on the second corner of the land you want to claim.\n" +
								" &r&lRight &rclick &r&lair &rto &ccancel &ryour current claim selection.\n" +
								" &r&lShift &r+ &r&lLeft &rclick to &apurchase &ryour current claim selection.\n" +
								" \n" +
								" &7* &3&lUnclaiming land:\n" +
								" &rYou must type &b/f unclaim &rand you will have to confirm it by adding &a&lyes &ror &c&lno &rto the command.\n" +
								spacebar
				));
				break;
			case "?elevator":
			case "?elevators":
				event.setCancelled(true);
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						spacebar +
								"&9&lElevators tutorial (2 Signs)\n" +
								" \n" +
								" &7* &rPlace the first sign on a block and type &3&l[Elevator] &7&o(First line) &rand &bDown &7&o(Second line)&r.\n" +
								" \n" +
								" &7* &rThen &cin the same pillar of blocks&r, go down as many blocks as you want, place the second sign and type &3&l[Elevator] &7&o(First line) &rand &bUp &7&o(Second line)&r.\n" +
								spacebar
				));
				break;
			case "?potions":
			case "?brewing":
				String square = "\u2588";
				String circle = "\u2B24";
				String triangle = "&l\u29CA";
				String arrow = " &r\u279D ";
				event.setCancelled(true);
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						spacebar +
								"&9&lBrewing tutorial\n" +
								spacebar +
								"&3&lIcons:\n" +
								" &7* &rGlowstone &e" + triangle + " &7(Increases Potency)\n" +
								" &7* &rRedstone &4" + triangle + " &7(Increases Duration)\n" +
								" &7* &rGunpowder &8" + triangle + " &7(Make potions splashable)\n" +
								" &7* &rSugar &r" + triangle + "\n" +
								" &7* &rNether Wart &c" + square + "\n" +
								" &7* &rGlistering Melon &6" + square + "\n" +
								" &7* &rGolden Carrot &e" + square + "\n" +
								" &7* &rMagma Cream &a" + circle + "\n" +
								" &7* &rSpider Eye &c" + circle + "\n" +
								" &7* &rFermented Spider Eye &4" + circle + "\n" +
								spacebar +
								"&3&lCraftings:\n" +
								" &7- &a" + circle + " &r= Slimeball + Blaze Powder\n" +
								" &7- &6" + square + " &r= Melon + 8xGold Nuggets\n" +
								" &7- &e" + square + " &r= Carrot + 8xGold Nuggets\n" +
								" &7- &4" + circle + " &r= " + "&c" + circle + " &r+ " + triangle + " &r+ Brown Mushroom\n" +
								spacebar +
								"&3&lPotions:\n" +
								" &7» &c&ki&rHealing II Splash&c&ki&r: &c" + square + arrow + "&6" + square + arrow + "&e" + triangle + arrow + "&8" + triangle + "\n" +
								" &7» &b&ki&rSpeed II&b&ki&r: &c" + square + arrow + "&r" + triangle + arrow + "&e" + triangle + "\n" +
								" &7» &6&ki&rFire Resistance +&6&ki&r: &c" + square + arrow + "&a" + circle + arrow + "&4" + triangle + "\n" +
								" &7» &7&ki&rInvisibility +&7&ki&r: &c" + square + arrow + "&a" + circle + arrow + "&4" + circle + arrow + "&4" + triangle + "\n" +
								" &7» &2&ki&rPoison II Splash&2&ki&r: &c" + square + arrow + "&c" + circle + arrow + "&e" + triangle + arrow + "&8" + triangle + "\n" +
								" &7» &5&ki&rSlowness II Splash&5&ki&r: &c" + square + arrow + "&r" + triangle + arrow + "&4" + circle + arrow + "&4" + triangle + arrow + "&8" + triangle + "\n" +
								spacebar
				));
				break;
			case "?links":
			case "?forums":
			case "?forum":
			case "?discord":
			case "?website":
			case "?store":
			case "?twitter":
			case "?staff":
			case "?mods":
			case "?rules":
			case "?clients":
				event.setCancelled(true);
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						spacebar +
								"&9&lServer links\n" +
								" &7* &3Website: &rveilhcf.us\n" +
								" &7* &3Forums: &rveilhcf.us/community\n" +
								" &7* &3Store: &rstore.veilhcf.us\n" +
								" &7* &3Twitter: &rtwitter.com/veilhcf\n" +
								" &7* &3Discord: &rdiscord.gg/RdbbBx7\n" +
								" &7* &3Allowed Mods: &rhttps://veilhcf.us/community/d/12-allowed-modifications\n" +
								" &7* &3Server Rules: &rhttps://veilhcf.us/community/d/118-our-rules\n" +
								spacebar
				));
				break;
			case "?teamspeak":
			case "?ts":
			case "?ts3":
				event.setCancelled(true);
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						spacebar +
								"&9&lTeamSpeak: &7ts.veilhcf.us\n" +
								spacebar
				));
				break;
		}
		if(command.startsWith("?") && command.trim().split(" ").length == 1 && !event.isCancelled() && command.toCharArray().length > 1){
			event.setCancelled(true);
			player.chat("?help");
		}
	}
}
