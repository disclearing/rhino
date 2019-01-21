package com.sergivb01.base.listener;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.command.module.essential.StaffUtilitiesCommand;
import com.sergivb01.base.user.BaseUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class StaffListener implements Listener{

	public Inventory page = Bukkit.createInventory(null, 54, "Staff Online");

	@EventHandler
	public void staff(PlayerInteractEvent event){
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			final ItemStack hand = event.getPlayer().getItemInHand();
			Player p = event.getPlayer();
			BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(p.getUniqueId());
			if(hand.equals(StaffUtilitiesCommand.getRandomTeleport())){
				if(!p.hasPermission("command.random")){
					p.sendMessage(ChatColor.RED + "No permission!");
					return;
				}

				final ArrayList<Player> players = new ArrayList<>();
				for(final Player p2 : Bukkit.getOnlinePlayers()){
					if((p2 != p) && (!p2.hasPermission("command.random"))){
						players.add(p2);
					}
				}

				if(players.size() == 0){
					p.sendMessage(ChatColor.RED + "There are no players to teleport to.");
					return;
				}


				final Player target = players.get(new Random().nextInt(players.size()));

				p.teleport(target);
				p.sendMessage(ChatColor.YELLOW + "You have been teleported to " + ChatColor.GREEN + target.getName());
				event.setCancelled(true);
			}
			if(hand.equals(StaffUtilitiesCommand.getVanishTool(true))){
				if(!p.hasPermission("command.vanish")){
					p.sendMessage(ChatColor.RED + "No permission!");
					return;
				}

				p.getInventory().setItemInHand(StaffUtilitiesCommand.getVanishTool(false));
				baseUser.setVanished(false);
				p.sendMessage(ChatColor.YELLOW + "Vanish mode of " + p.getName() + " set to " + "false" + '.');
			}
			if(hand.equals(StaffUtilitiesCommand.getVanishTool(false))){
				if(!p.hasPermission("command.vanish")){
					p.sendMessage(ChatColor.RED + "No permission!");
					return;
				}

				p.getInventory().setItemInHand(StaffUtilitiesCommand.getVanishTool(true));
				// final BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(p.getUniqueId());
				baseUser.setVanished(true);
				p.sendMessage(ChatColor.YELLOW + "Vanish mode of " + p.getName() + " set to " + "true" + '.');
			}
		}
		if(event.getAction().equals(Action.LEFT_CLICK_AIR) || (event.getAction().equals(Action.LEFT_CLICK_BLOCK))){
			final ItemStack hand = event.getPlayer().getItemInHand();
			Player p = event.getPlayer();
			BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(p.getUniqueId());
			if(hand.getType() == Material.DIAMOND_PICKAXE){
				if(baseUser.isStaffUtil()){
					p.getInventory().setItem(7, StaffUtilitiesCommand.getRandomTeleport());
					return;
				}
			}
			if(hand.equals(StaffUtilitiesCommand.getRandomTeleport())){
				if(baseUser.isStaffUtil()){
					p.getInventory().setItem(7, StaffUtilitiesCommand.getMinerTeleport());
					return;
				}
			}

		}
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))){
			final ItemStack hand = event.getPlayer().getItemInHand();
			Player p = event.getPlayer();
			BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(p.getUniqueId());
			if(hand.getType() == Material.DIAMOND_PICKAXE){
				if(baseUser.isStaffUtil()){

					final ArrayList<Player> players = new ArrayList<>();
					for(final Player p2 : Bukkit.getOnlinePlayers()){
						if((p2 != p) && (!p2.hasPermission("command.random") && (p2.getLocation().getY() < 20))){
							players.add(p2);
						}
					}

					if(players.size() == 0){
						p.sendMessage(ChatColor.GRAY + "(Miner) " + ChatColor.RED + "There are no players to teleport to.");
						return;
					}


					final Player target = players.get(new Random().nextInt(players.size()));

					p.teleport(target);
					p.sendMessage(ChatColor.GRAY + "(Miner) " + ChatColor.YELLOW + "You have been teleported to " + ChatColor.GREEN + target.getName());
					event.setCancelled(true);
				}
			}
		}

		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			final ItemStack hand = event.getPlayer().getItemInHand();
			if(hand.equals(StaffUtilitiesCommand.getRandomTeleport())){
				if(!event.getPlayer().hasPermission("command.staffmode")){
					event.getPlayer().sendMessage(ChatColor.RED + "No permission!");
					return;
				}

				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event){
		Player player = (Player) event.getWhoClicked();
		final BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(event.getWhoClicked().getUniqueId());
		Inventory inventory = event.getInventory();
		ItemStack clicked = event.getCurrentItem();
		if(inventory.getName().equals("Xrayer Gui")){
			if(clicked == null){
				return;
			}
			if(clicked.getType() == Material.SKULL_ITEM){
				Bukkit.dispatchCommand(player, "tp " + ChatColor.stripColor(clicked.getItemMeta().getDisplayName()));
			}
		}
		if(inventory.getName().contains("Inventory: ")){
			Player target = Bukkit.getPlayer(event.getInventory().getTitle().substring("Inventory: ".length()));
			if(event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null || event.getCurrentItem().getItemMeta().getDisplayName() == null){
				return;
			}
			switch(event.getCurrentItem().getItemMeta().getDisplayName().toLowerCase()){
				case "§cclear inventory":
					Bukkit.dispatchCommand(player, "ci " + target.getName());
					event.setCancelled(true);
					player.closeInventory();
					break;
				case "§cplayer history":
					Bukkit.dispatchCommand(player, "litebans:history " + target.getName());
					event.setCancelled(true);
					player.closeInventory();
					break;
				case "§cplayer alts":
					Bukkit.dispatchCommand(player, "alts " + target.getName());
					event.setCancelled(true);
					player.closeInventory();
					break;
				case "§bfreeze player":
					Bukkit.dispatchCommand(player, "freeze " + target.getName());
					event.setCancelled(true);
					player.closeInventory();
					break;

			}
			event.setCancelled(true);
		}

		if(baseUser.isStaffUtil()){
			event.setCancelled(true);
		}
	}


	@EventHandler
	public void touchytouchy(PlayerInteractEntityEvent event){
		if(event.getRightClicked() instanceof Player){
			Player rightclick = (Player) event.getRightClicked();
			final ItemStack hand = event.getPlayer().getItemInHand();
			Player p = event.getPlayer();
			if(hand.equals(StaffUtilitiesCommand.getBookTool())){
				if(!p.hasPermission("command.staffmode")){
					p.sendMessage(ChatColor.RED + "No permission!");
					return;
				}

				Inventory i = null;
				if(i != null){
					i.clear();
				}
				i = Bukkit.createInventory(rightclick.getPlayer(), 54, "Inventory: " + rightclick.getPlayer().getName());
				if(rightclick.getPlayer().getInventory().getHelmet() != null){
					i.setItem(0, rightclick.getPlayer().getInventory().getHelmet());
				}
				if(rightclick.getPlayer().getInventory().getChestplate() != null){
					i.setItem(1, rightclick.getPlayer().getInventory().getChestplate());
				}
				if(rightclick.getPlayer().getInventory().getLeggings() != null){
					i.setItem(2, rightclick.getPlayer().getInventory().getLeggings());
				}
				if(rightclick.getPlayer().getInventory().getBoots() != null){
					i.setItem(3, rightclick.getPlayer().getInventory().getBoots());
				}
				if(rightclick.getPlayer().getItemInHand() != null){
					i.setItem(4, rightclick.getPlayer().getItemInHand());
				}
				int a = 0;
				for(int ix = 9; ix < rightclick.getPlayer().getInventory().getSize() + 9; ix++){
					i.setItem(ix, rightclick.getPlayer().getInventory().getItem(ix - 9));
					a = ix;
				}

				ItemStack clearItem = new ItemStack(Material.BLAZE_POWDER);
				ItemMeta clearMeta = clearItem.getItemMeta();
				clearMeta.setDisplayName("§cClear inventory");
				clearItem.setItemMeta(clearMeta);
				i.setItem(a + 1, clearItem);

				ItemStack freezeItem = new ItemStack(Material.PACKED_ICE);
				ItemMeta freezeMeta = freezeItem.getItemMeta();
				freezeMeta.setDisplayName("§bFreeze Player");
				freezeItem.setItemMeta(freezeMeta);
				i.setItem(a + 4, freezeItem);

				ItemStack altsItem = new ItemStack(Material.PAPER);
				ItemMeta altsMeta = altsItem.getItemMeta();
				altsMeta.setDisplayName("§cPlayer Alts");
				altsItem.setItemMeta(altsMeta);
				i.setItem(a + 3, altsItem);

				ItemStack histItem = new ItemStack(Material.getMaterial(101));
				ItemMeta histMeta = histItem.getItemMeta();
				histMeta.setDisplayName("§cPlayer History");
				histItem.setItemMeta(histMeta);
				i.setItem(a + 2, histItem);

				DecimalFormat df = new DecimalFormat("#.##");

				ItemStack health = new ItemStack(Material.GOLDEN_APPLE);
				ItemMeta healthMeta = health.getItemMeta();
				healthMeta.setDisplayName("§aPlayer Health");
				healthMeta.setLore(Arrays.asList(ChatColor.YELLOW + rightclick.getName() + " Health is: " + df.format(rightclick.getPlayer().getHealth())));
				health.setItemMeta(healthMeta);
				i.setItem(a + 8, health);

				ItemStack food = new ItemStack(Material.COOKED_BEEF);
				ItemMeta foodMeta = health.getItemMeta();
				foodMeta.setDisplayName("§aPlayer Hunger");
				foodMeta.setLore(Arrays.asList(ChatColor.YELLOW + rightclick.getName() + " Hunger is: " + df.format(rightclick.getPlayer().getFoodLevel())));
				food.setItemMeta(foodMeta);
				i.setItem(a + 9, food);


				p.openInventory(i);
				p.sendMessage("§eOpening the inventory of " + rightclick.getName());
				return;


			}else if(hand.equals(StaffUtilitiesCommand.getFreezeTool())){
				if(!p.hasPermission("command.staffmode")){
					p.sendMessage(ChatColor.RED + "No permission!");
					return;
				}

				Bukkit.dispatchCommand(p, "freeze " + rightclick.getName());
			}
		}
	}

	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event){
		final BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(event.getPlayer().getUniqueId());
		if(baseUser.isStaffUtil()){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event){
		final BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(event.getPlayer().getUniqueId());
		if(baseUser.isStaffUtil()){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event){
		final BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(event.getPlayer().getUniqueId());
		if(baseUser.isStaffUtil()){
			Player p = event.getPlayer();
			p.sendMessage(ChatColor.RED + "You can not do this while in staff mode.");
			event.setCancelled(true);
		}
	}


	@EventHandler
	public void onBreak(BlockBreakEvent event){
		final BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(event.getPlayer().getUniqueId());
		if(baseUser.isStaffUtil()){
			Player p = event.getPlayer();
			p.sendMessage(ChatColor.RED + "You can not do this while in staff mode.");
			event.setCancelled(true);
		}
	}


	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		if(event.getPlayer().hasPermission("command.staff")){
            /*for(Player on : Bukkit.getOnlinePlayers()) {
                if (on.hasPermission("command.staff")) {
                    on.sendMessage(ChatColor.BLUE + "(Staff) " + ChatColor.AQUA + event.getPlayer().getName() + " has joined the server.");
                }
            }*/
			final BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(event.getPlayer().getUniqueId());
			baseUser.setVanished(true);
			event.getPlayer().sendMessage(ChatColor.GREEN + "Your vanish has been enabled.");

		}
	}
}