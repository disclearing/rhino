package com.sergivb01.base.command.module.essential;

import com.google.common.collect.Maps;
import com.sergivb01.base.BaseConstants;
import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.util.ItemBuilder;
import com.sergivb01.util.chat.ClickAction;
import com.sergivb01.util.chat.Text;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ReportCommand
		extends BaseCommand
		implements Listener{
	private static final long REPORT_DELAY_MILLIS = TimeUnit.MINUTES.toMillis(3);
	private final Map<UUID, Long> reportMap;
	private final HashSet<String> whoReported = new HashSet();

	public ReportCommand(){
		super("report", "Reports a player");
		this.setUsage("/(command) <playerName>");
		this.reportMap = Maps.newHashMap();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "Only players may report others.");
			return true;
		}
		if(args.length != 1){
			sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <player> ");
			return true;
		}
		Player player = (Player) sender;
		Player target = Bukkit.getPlayer(args[0]);
		if(target == null || !player.canSee(target)){
			sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
			return true;
		}
		if(sender.equals(target)){
			sender.sendMessage(ChatColor.RED + "You may not report yourself.");
			return true;
		}
		UUID uuid = player.getUniqueId();
		long millis = System.currentTimeMillis();
		Long lastReport = this.reportMap.get(uuid);
		if(lastReport != null && lastReport - millis > 0){
			sender.sendMessage(ChatColor.RED + "You have already reported someone in the last " + DurationFormatUtils.formatDurationWords(REPORT_DELAY_MILLIS, true, true) + '.');
			return true;
		}
		this.reportMap.put(uuid, millis + REPORT_DELAY_MILLIS);
		this.whoReported.add(sender.getName());
		this.openInv((Player) sender, target);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		return args.length == 1 ? null : Collections.emptyList();
	}

	private void openInv(Player p, Player target){
		Inventory inv = Bukkit.createInventory(null, 18, ChatColor.DARK_AQUA + "Report " + target.getName());
		ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(ChatColor.YELLOW + "ForceField");
		item.setItemMeta(itemMeta);
		inv.addItem(item);
		ItemStack item3 = new ItemStack(Material.FEATHER);
		ItemMeta itemMeta3 = item3.getItemMeta();
		itemMeta3.setDisplayName(ChatColor.YELLOW + "Speed");
		item3.setItemMeta(itemMeta3);
		inv.addItem(item3);
		ItemStack item5 = new ItemStack(Material.DIAMOND_PICKAXE);
		ItemMeta itemMeta5 = item5.getItemMeta();
		itemMeta5.setDisplayName(ChatColor.YELLOW + "X-Ray");
		item5.setItemMeta(itemMeta5);
		inv.addItem(item5);
		ItemStack item6 = new ItemStack(Material.ENDER_PEARL);
		ItemMeta itemMeta6 = item6.getItemMeta();
		itemMeta6.setDisplayName(ChatColor.YELLOW + "Fly");
		item6.setItemMeta(itemMeta6);
		inv.addItem(item6);
		ItemStack item8 = new ItemStack(Material.SAND);
		ItemMeta itemMeta8 = item8.getItemMeta();
		itemMeta8.setDisplayName(ChatColor.YELLOW + "Phasing");
		item8.setItemMeta(itemMeta8);
		inv.addItem(item8);
		ItemStack item9 = new ItemStack(Material.BRICK);
		ItemMeta itemMeta9 = item9.getItemMeta();
		itemMeta9.setDisplayName(ChatColor.YELLOW + "Anti-KB");
		item9.setItemMeta(itemMeta9);
		inv.addItem(item9);
		ItemStack builder = new ItemBuilder(Material.DIAMOND_CHESTPLATE).displayName(ChatColor.YELLOW + "Glitching").build();
		inv.addItem(builder);
		p.openInventory(inv);
	}

	@EventHandler
	public void onClick(InventoryClickEvent e){
		if(e.getInventory().getName().contains("Report")){
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			p.closeInventory();
			for(Player on : Bukkit.getOnlinePlayers()){
				if(!on.hasPermission("hcf.report.view")) continue;
				if(Bukkit.getPlayer(e.getInventory().getName().replace(ChatColor.DARK_AQUA + "Report ", "")) != null){
					new Text(ChatColor.YELLOW + "[" + ChatColor.RED + "R" + ChatColor.YELLOW + "] " + ChatColor.RED + e.getInventory().getName().replace(new StringBuilder().append(ChatColor.DARK_AQUA).append("Report ").toString(), "") + ChatColor.GRAY + " has been reported for: " + ChatColor.YELLOW + e.getCurrentItem().getItemMeta().getDisplayName().replace(ChatColor.AQUA.toString(), "") + ChatColor.GRAY + ChatColor.ITALIC + " (Click here)").setHoverText(ChatColor.YELLOW + "Reported by: " + ChatColor.GRAY + p.getName() + ChatColor.YELLOW + ". Click to teleport").setClick(ClickAction.RUN_COMMAND, "/tp " + Bukkit.getPlayer(e.getInventory().getName().replace(new StringBuilder().append(ChatColor.DARK_AQUA).append("Report ").toString(), "")).getName()).send(on);
					continue;
				}
				new Text(ChatColor.GRAY + "[" + ChatColor.AQUA + "R" + ChatColor.GRAY + "] " + ChatColor.AQUA + e.getInventory().getName().replace(new StringBuilder().append(ChatColor.DARK_AQUA).append("Report ").toString(), "") + ChatColor.GRAY + " has been reported for: " + ChatColor.AQUA + e.getCurrentItem().getItemMeta().getDisplayName().replace(ChatColor.AQUA.toString(), "") + ChatColor.GRAY + ChatColor.ITALIC + " (Click here)").setHoverText(ChatColor.YELLOW + "The reporter: " + ChatColor.GRAY + p.getName()).send(on);
			}
		}
	}

	@EventHandler
	public void ReportClose(InventoryCloseEvent e){
		if(e.getInventory().getName().contains("Report")){
			Player p = (Player) e.getPlayer();
			p.sendMessage(ChatColor.GOLD + "Report has been sent, any illegitimate reports may result in punishment.");
		}
	}
}

