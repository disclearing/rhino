package com.sergivb01.base.command.module.essential;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.base.user.BaseUser;
import com.sergivb01.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

import static org.bukkit.ChatColor.*;

public class SettingsCommand extends BaseCommand implements Listener, InventoryHolder{
	private final BasePlugin plugin;

	public SettingsCommand(BasePlugin plugin){
		super("settings", "Configure the looking on the server.");
		this.setUsage("/(command) <message>");
		this.setAliases(new String[]{"options", "config"});
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(RED + "You may not execute this command.");
			return true;
		}

		Player player = (Player) sender;
		player.sendMessage(YELLOW + "Opening your menu...");

		ItemStack[] contents = getContents(player);
		Inventory inv = Bukkit.createInventory(this, contents.length, ChatColor.translateAlternateColorCodes('&', "&6&lSettings"));
		inv.setContents(contents);
		player.openInventory(inv);

		return true;
	}

	private ItemStack[] getContents(Player player){
		BaseUser baseUser = this.plugin.getUserManager().getUser(player.getUniqueId());
		boolean staff = player.hasPermission("hcf.utils.staff");

		ItemStack[] itemStacks = new ItemStack[9];
		itemStacks[1] = new ItemBuilder(Material.INK_SACK, 1, (byte) (baseUser.isMessagesVisible() ? 10 : 8))
				.displayName((baseUser.isMessagesVisible() ? GREEN : RED) + "Private messages")
				.lore(c("&7Toggle whether you receive messages", "", "&7Click to toggle"))
				.build();

		itemStacks[4] = new ItemBuilder(Material.INK_SACK, 1, (byte) (baseUser.isMessagingSounds() ? 10 : 8))
				.displayName((baseUser.isMessagingSounds() ? GREEN : RED) + "Sounds")
				.lore(c("&7Toggle whether you want notifications sounds.", "", "&7Click to toggle"))
				.build();


		//if(staff){
			//TODO: Handle staff tab style
		//	itemStacks[7] = new ItemBuilder(Material.INK_SACK, 1, (byte) (baseUser.isMessagesVisible() ? 10 : 8))
		//			.displayName((true /*TODO: Change this to baseUser.getTabStyle()*/ ? GREEN : RED) + "Tab style")
		/*			.lore(c("&7Change your tab style.", "", "&7Click to change"))
					.build();
		*/
		//}else{
			//itemStacks[7] = new ItemBuilder(Material.INK_SACK, 1, (byte) (baseUser.isMessagesVisible() ? 10 : 8))
			//		.displayName((true /*TODO: Change this to baseUser.getTabStyle()*/ ? GREEN : RED) + "Tab style")
		/*			.lore(c("&7Change your tab style.", "", "&7Click to change"))
					.build();
		}
		*/
		return itemStacks;
	}


	@EventHandler
	public void onInventoryClick(InventoryClickEvent event){
		if((event.getView() != null) && ((event.getWhoClicked() instanceof Player)) && (event.getClickedInventory() != null) && ((event.getClickedInventory().getHolder() instanceof SettingsCommand))){
			Player player = (Player) event.getWhoClicked();
			BaseUser baseUser = this.plugin.getUserManager().getUser(player.getUniqueId());
			//FactionUser factionUser = HCF.getInstance().getUserManager().getUser(player.getUniqueId());

			event.setCancelled(true);

			if(baseUser != null){
				int slot = event.getSlot();
				boolean staff = player.hasPermission("hcf.utils.staff");
				switch(slot){
					case 1:{ //private messages
						player.performCommand("togglepm");
						break;
					}
					case 4:{ //sounds
						player.performCommand("togglesounds");
						break;
					}
				/*	case 7:{ //tab style
						int next = (factionUser.getTabStyleInt() >= (staff ? 2 : 1) ? 0 : factionUser.getTabStyleInt() + 1);
						factionUser.setTabStyle(next);
						player.sendMessage(YELLOW + "Tab style set to " + next + " => " + factionUser.getTabStyle().toString());
						PlayerTab.clean.add(player);
						break;
					}
					*/
					default:
						return;
				}
				Inventory inventory = event.getClickedInventory();
				inventory.setContents(getContents(player));
			}
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		return args.length == 1 ? null : Collections.emptyList();
	}

	private String[] c(String... strs){
		for(int i = 0; i < strs.length; i++){
			strs[i] = ChatColor.translateAlternateColorCodes('&', strs[i]);
		}
		return strs;
	}

	@Override
	public Inventory getInventory(){
		return null;
	}
}
