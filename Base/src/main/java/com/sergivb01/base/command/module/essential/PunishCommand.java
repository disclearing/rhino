package com.sergivb01.base.command.module.essential;

// Created by iDaniel84

import com.sergivb01.base.command.BaseCommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PunishCommand extends BaseCommand implements Listener{

	private static String target;
	private static String silent;
	private Inventory inv;

	public PunishCommand(){
		super("punish", "Punish a player.");
		this.setAliases(new String[]{"who"});
		this.setUsage("/(command) <player> [-s]");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(args.length == 0 || args.length > 2 || (args.length == 2 && !args[1].equalsIgnoreCase("-s"))){
			sender.sendMessage(this.getUsage());
			return true;
		}
		Player player = (Player) sender;
		if(!(sender instanceof Player)){
			sender.sendMessage("Only players can use this command.");
			return true;
		}else{
			target = args[0];
			if(args.length == 1){
				if(silent == null) silent = "";
				else silent = "";
				inv = Bukkit.createInventory(player, 54, "Punish Manager");
			}else if(args.length == 2 && args[1].equalsIgnoreCase("-s")){
				silent = " -s";
				inv = Bukkit.createInventory(player, 54, "Punish Manager" + ChatColor.ITALIC + " (Silent)");
			}
			PunishmentGUI(player);
		}
		return true;
	}

	private void PunishmentGUI(Player player){
		inv.clear();

		// ####################################################################################################################################################################### //

		ItemStack title_GlobalRules = new ItemStack(Material.INK_SACK, 1, (short) 1);
		ItemStack title_ChatRules = new ItemStack(Material.INK_SACK, 1, (short) 11);
		ItemStack title_HCFRules = new ItemStack(Material.INK_SACK, 1, (short) 14);

		ItemStack global_HackedClient = new ItemStack(Material.WOOL, 1, (short) 14);
		ItemStack global_HackedClientAdmit = new ItemStack(Material.CARPET, 1, (short) 14);
		ItemStack global_Xray = new ItemStack(Material.WOOL, 1, (short) 14);
		ItemStack global_XrayAdmit = new ItemStack(Material.CARPET, 1, (short) 14);
		ItemStack global_Autoclick = new ItemStack(Material.WOOL, 1, (short) 14);
		ItemStack global_AutoclickAdmit = new ItemStack(Material.CARPET, 1, (short) 14);
		ItemStack global_AbusingGlitchs = new ItemStack(Material.WOOL, 1, (short) 14);
		ItemStack global_UnallowedMods = new ItemStack(Material.WOOL, 1, (short) 14);
		ItemStack global_Advertising = new ItemStack(Material.WOOL, 1, (short) 14);
		ItemStack global_Ddos = new ItemStack(Material.WOOL, 1, (short) 14);


		ItemStack hcf_AnvilSpam = new ItemStack(Material.WOOL, 1, (short) 1);
		ItemStack hcf_Betray = new ItemStack(Material.WOOL, 1, (short) 1);
		ItemStack hcf_LeaveAndKill = new ItemStack(Material.WOOL, 1, (short) 1);
		ItemStack hcf_SuffocationTraps = new ItemStack(Material.WOOL, 1, (short) 1);
		ItemStack hcf_PvpProtAbuse = new ItemStack(Material.WOOL, 1, (short) 1);
		ItemStack hcf_KickAndKill = new ItemStack(Material.WOOL, 1, (short) 1);
		ItemStack hcf_DtrEvading = new ItemStack(Material.WOOL, 1, (short) 1);
		ItemStack hcf_Alting = new ItemStack(Material.WOOL, 1, (short) 1);
		ItemStack hcf_GriefingOutsideClaims = new ItemStack(Material.WOOL, 1, (short) 1);
		ItemStack hcf_Inside = new ItemStack(Material.WOOL, 1, (short) 1);
		ItemStack hcf_Blockglitch = new ItemStack(Material.WOOL, 1, (short) 1);
		ItemStack hcf_Allying = new ItemStack(Material.WOOL, 1, (short) 1);

		ItemStack chat_Toxicity = new ItemStack(Material.WOOL, 1, (short) 4);
		ItemStack chat_UsersDisrIndirectly = new ItemStack(Material.WOOL, 1, (short) 4);
		ItemStack chat_UsersDisrDirectly = new ItemStack(Material.WOOL, 1, (short) 4);
		ItemStack chat_StaffDisr = new ItemStack(Material.WOOL, 1, (short) 4);
		ItemStack chat_ChatSpam = new ItemStack(Material.WOOL, 1, (short) 4);
		ItemStack chat_ChatFlood = new ItemStack(Material.WOOL, 1, (short) 4);
		ItemStack chat_ExternalLinks = new ItemStack(Material.WOOL, 1, (short) 4);
		ItemStack chat_FactionMesaggesSpam = new ItemStack(Material.WOOL, 1, (short) 4);
		ItemStack chat_HelpopMissue = new ItemStack(Material.WOOL, 1, (short) 4);
		ItemStack chat_LieToStaff = new ItemStack(Material.WOOL, 1, (short) 4);

		ItemStack misc_InsiderRank = new ItemStack(Material.WOOL, 1, (short) 3);
		ItemStack misc_Unban = new ItemStack(Material.WOOL, 1, (short) 5);
		ItemStack misc_RulesDoc = new ItemStack(Material.INK_SACK, 1, (short) 10);
		ItemStack misc_LinksDoc = new ItemStack(Material.INK_SACK, 1, (short) 12);
		ItemStack misc_Skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());

		// ####################################################################################################################################################################### //

		ItemMeta meta_title_GlobalRules = title_GlobalRules.getItemMeta();
		ItemMeta meta_title_ChatRules = title_ChatRules.getItemMeta();
		ItemMeta meta_title_HCFRules = title_HCFRules.getItemMeta();
		ItemMeta meta_global_HackedClient = global_HackedClient.getItemMeta();
		ItemMeta meta_global_HackedClientAdmit = global_HackedClientAdmit.getItemMeta();
		ItemMeta meta_global_Xray = global_Xray.getItemMeta();
		ItemMeta meta_global_XrayAdmit = global_XrayAdmit.getItemMeta();
		ItemMeta meta_global_Autoclick = global_Autoclick.getItemMeta();
		ItemMeta meta_global_AutoclickAdmit = global_AutoclickAdmit.getItemMeta();
		ItemMeta meta_global_AbusingGlitchs = global_AbusingGlitchs.getItemMeta();
		ItemMeta meta_global_UnallowedMods = global_UnallowedMods.getItemMeta();
		ItemMeta meta_global_Advertising = global_Advertising.getItemMeta();
		ItemMeta meta_global_Ddos = global_Ddos.getItemMeta();
		ItemMeta meta_hcf_SuffocationTraps = hcf_SuffocationTraps.getItemMeta();
		ItemMeta meta_hcf_PvpProtAbuse = hcf_PvpProtAbuse.getItemMeta();
		ItemMeta meta_hcf_KickAndKill = hcf_KickAndKill.getItemMeta();
		ItemMeta meta_hcf_LeaveAndKill = hcf_LeaveAndKill.getItemMeta();
		ItemMeta meta_hcf_DtrEvading = hcf_DtrEvading.getItemMeta();
		ItemMeta meta_hcf_Alting = hcf_Alting.getItemMeta();
		ItemMeta meta_hcf_GriefingOutsideClaims = hcf_GriefingOutsideClaims.getItemMeta();
		ItemMeta meta_hcf_Inside = hcf_Inside.getItemMeta();
		ItemMeta meta_hcf_Betray = hcf_Betray.getItemMeta();
		ItemMeta meta_hcf_Blockglitch = hcf_Blockglitch.getItemMeta();
		ItemMeta meta_hcf_Allying = hcf_Allying.getItemMeta();
		ItemMeta meta_hcf_AnvilSpam = hcf_AnvilSpam.getItemMeta();
		ItemMeta meta_chat_LieToStaff = chat_LieToStaff.getItemMeta();
		ItemMeta meta_chat_Toxicity = chat_Toxicity.getItemMeta();
		ItemMeta meta_chat_UsersDisrIndirectly = chat_UsersDisrIndirectly.getItemMeta();
		ItemMeta meta_chat_UsersDisrDirectly = chat_UsersDisrDirectly.getItemMeta();
		ItemMeta meta_chat_StaffDisr = chat_StaffDisr.getItemMeta();
		ItemMeta meta_chat_ChatSpam = chat_ChatSpam.getItemMeta();
		ItemMeta meta_chat_ChatFlood = chat_ChatFlood.getItemMeta();
		ItemMeta meta_chat_ExternalLinks = chat_ExternalLinks.getItemMeta();
		ItemMeta meta_chat_FactionMesaggesSpam = chat_FactionMesaggesSpam.getItemMeta();
		ItemMeta meta_chat_HelpopMissue = chat_HelpopMissue.getItemMeta();
		ItemMeta meta_misc_InsiderRank = misc_InsiderRank.getItemMeta();
		ItemMeta meta_misc_Unban = misc_Unban.getItemMeta();
		ItemMeta meta_misc_RulesDoc = misc_RulesDoc.getItemMeta();
		ItemMeta meta_misc_LinksDoc = misc_LinksDoc.getItemMeta();
		SkullMeta skullmeta_misc_Skull = (SkullMeta) misc_Skull.getItemMeta();

		// ####################################################################################################################################################################### //

		meta_title_GlobalRules.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + ChatColor.UNDERLINE + "Global Rules");
		meta_title_ChatRules.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + ChatColor.UNDERLINE + "Chat Rules");
		meta_title_HCFRules.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + ChatColor.UNDERLINE + "HCF Rules");

		meta_global_HackedClient.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Hacked client");
		meta_global_HackedClientAdmit.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Hacked client Admit");
		meta_global_Xray.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Xray");
		meta_global_XrayAdmit.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Xray Admit");
		meta_global_Autoclick.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Autoclick");
		meta_global_AutoclickAdmit.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Autoclick Admit");
		meta_global_AbusingGlitchs.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Abusing Glitches");
		meta_global_UnallowedMods.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Unapproved Mods");
		meta_global_Advertising.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Advertising");
		meta_global_Ddos.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Ddos");

		meta_hcf_SuffocationTraps.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Suffocation Traps");
		meta_hcf_PvpProtAbuse.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "PvP Protection Abuse");
		meta_hcf_KickAndKill.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Kick & Kill");
		meta_hcf_LeaveAndKill.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Leave & Kill");
		meta_hcf_DtrEvading.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "DTR Evading");
		meta_hcf_Alting.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Alting");
		meta_hcf_GriefingOutsideClaims.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Griefing outside faction claims");
		meta_hcf_Inside.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Inside");
		meta_hcf_Betray.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Betray");
		meta_hcf_Blockglitch.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "BlockGlitch");
		meta_hcf_Allying.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Allying");
		meta_hcf_AnvilSpam.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Anvil Spam");
		meta_chat_LieToStaff.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Liying to Staff");

		meta_chat_Toxicity.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Toxicity");
		meta_chat_UsersDisrIndirectly.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Users disrespect indirectly");
		meta_chat_UsersDisrDirectly.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Users disrespect directly");
		meta_chat_StaffDisr.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Staff disrespect");
		meta_chat_ChatSpam.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Chat spam");
		meta_chat_ChatFlood.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Chat flood");
		meta_chat_ExternalLinks.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "External links");
		meta_chat_FactionMesaggesSpam.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Spamming faction commands");
		meta_chat_HelpopMissue.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Missue of Helpop");

		meta_misc_InsiderRank.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Insider rank");
		meta_misc_Unban.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Unban");
		meta_misc_RulesDoc.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Rules document");
		meta_misc_LinksDoc.setDisplayName(ChatColor.BLUE.toString() + ChatColor.BOLD + "Links document");
		skullmeta_misc_Skull.setOwner(target);
		skullmeta_misc_Skull.setDisplayName(ChatColor.GRAY.toString() + ChatColor.BOLD + target);

		// ####################################################################################################################################################################### //

		meta_global_HackedClient.setLore(Arrays.asList(ChatColor.GOLD + "Permanent BAN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Killaura, bhop, phase, etc."));
		meta_global_HackedClientAdmit.setLore(Arrays.asList(ChatColor.GOLD + "30 days BAN"));
		meta_global_Xray.setLore(Arrays.asList(ChatColor.GOLD + "15 days BAN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Includes X-Ray resourcepacks."));
		meta_global_XrayAdmit.setLore(Arrays.asList(ChatColor.GOLD + "7 days BAN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Includes X-Ray resourcepacks."));
		meta_global_Autoclick.setLore(Arrays.asList(ChatColor.GOLD + "30 days BAN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "More than 21cps constantly."));
		meta_global_AutoclickAdmit.setLore(Arrays.asList(ChatColor.GOLD + "15 days BAN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "More than 21cps constantly."));
		meta_global_AbusingGlitchs.setLore(Arrays.asList(ChatColor.GOLD + "3 days BAN"));
		meta_global_UnallowedMods.setLore(Arrays.asList(ChatColor.GOLD + "15 days BAN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Click links to see the", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "allowed mods list."));
		meta_global_Advertising.setLore(Arrays.asList(ChatColor.GOLD + "Permanent BAN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Sending ips of other", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "servers (Not ts3 ips)."));
		meta_global_Ddos.setLore(Arrays.asList(ChatColor.GOLD + "Blacklist", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Ddosing // Doxing other users."));

		meta_hcf_SuffocationTraps.setLore(Arrays.asList(ChatColor.GOLD + "WARN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Allowed on KITMAP."));
		meta_hcf_PvpProtAbuse.setLore(Arrays.asList(ChatColor.GOLD + "1 day BAN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Abusing of pvp timer."));
		meta_hcf_KickAndKill.setLore(Arrays.asList(ChatColor.GOLD + "WARN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Allowed on KITMAP."));
		meta_hcf_LeaveAndKill.setLore(Arrays.asList(ChatColor.GOLD + "WARN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Allowed on KITMAP."));
		meta_hcf_DtrEvading.setLore(Arrays.asList(ChatColor.GOLD + "WARN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Remember to reduce DTR of the faction."));
		meta_hcf_Alting.setLore(Arrays.asList(ChatColor.GOLD + "1 day BAN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Alting for DTR or", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "while deathbanned."));
		meta_hcf_GriefingOutsideClaims.setLore(Arrays.asList(ChatColor.GOLD + "WARN"));
		meta_hcf_Inside.setLore(Arrays.asList(ChatColor.GOLD + "WARN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Ask a manager for Insider rank."));
		meta_hcf_Betray.setLore(Arrays.asList(ChatColor.GOLD + "3 days BAN"));
		meta_hcf_Blockglitch.setLore(Arrays.asList(ChatColor.GOLD + "WARN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Includes pearl blockglitch."));
		meta_hcf_Allying.setLore(Arrays.asList(ChatColor.GOLD + "WARN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Teaming with non allies."));
		meta_hcf_AnvilSpam.setLore(Arrays.asList(ChatColor.GOLD + "WARN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Excessive anvil spam."));
		meta_chat_LieToStaff.setLore(Arrays.asList(ChatColor.GOLD + "1 day BAN"));

		meta_chat_Toxicity.setLore(Arrays.asList(ChatColor.GOLD + "1 hour MUTE", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Racism, suicidal encouragement", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "or excessive toxicity."));
		meta_chat_UsersDisrIndirectly.setLore(Arrays.asList(ChatColor.GOLD + "15 min MUTE", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "~You are a ***~"));
		meta_chat_UsersDisrDirectly.setLore(Arrays.asList(ChatColor.GOLD + "30 min MUTE", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "~Pepito you are a ***~"));
		meta_chat_StaffDisr.setLore(Arrays.asList(ChatColor.GOLD + "1 hour MUTE", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "~Sh** staff~"));
		meta_chat_ChatSpam.setLore(Arrays.asList(ChatColor.GOLD + "5 min MUTE", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Sending same message", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "a lot of times."));
		meta_chat_ChatFlood.setLore(Arrays.asList(ChatColor.GOLD + "WARN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Sending 1 word per line."));
		meta_chat_ExternalLinks.setLore(Arrays.asList(ChatColor.GOLD + "30 min MUTE", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Any link non-related"));
		meta_chat_FactionMesaggesSpam.setLore(Arrays.asList(ChatColor.GOLD + "KICK", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Spamming /f create, disband, etc."));
		meta_chat_HelpopMissue.setLore(Arrays.asList(ChatColor.GOLD + "WARN"));

		meta_misc_InsiderRank.setLore(Arrays.asList(ChatColor.GOLD + "Click to set Insider rank", ChatColor.GOLD + "to " + ChatColor.YELLOW.toString() + ChatColor.ITALIC + target));
		meta_misc_Unban.setLore(Arrays.asList(ChatColor.GOLD + "Click to unban " + ChatColor.YELLOW.toString() + ChatColor.ITALIC + target));
		meta_misc_RulesDoc.setLore(Arrays.asList(ChatColor.GOLD + "Click to get the rules", ChatColor.GOLD + "document in a chat message."));
		meta_misc_LinksDoc.setLore(Arrays.asList(ChatColor.GOLD + "Click to get the links", ChatColor.GOLD + "document in a chat message."));
		skullmeta_misc_Skull.setLore(Arrays.asList(ChatColor.GOLD + "Left click to see " + ChatColor.YELLOW.toString() + ChatColor.ITALIC + target + ChatColor.GOLD + " alts.", ChatColor.GOLD + "Right click to see " + ChatColor.YELLOW.toString() + ChatColor.ITALIC + target + ChatColor.GOLD + " history."));

		// ####################################################################################################################################################################### //

		title_GlobalRules.setItemMeta(meta_title_GlobalRules);
		title_ChatRules.setItemMeta(meta_title_ChatRules);
		title_HCFRules.setItemMeta(meta_title_HCFRules);
		global_HackedClient.setItemMeta(meta_global_HackedClient);
		global_HackedClientAdmit.setItemMeta(meta_global_HackedClientAdmit);
		global_Xray.setItemMeta(meta_global_Xray);
		global_XrayAdmit.setItemMeta(meta_global_XrayAdmit);
		global_Autoclick.setItemMeta(meta_global_Autoclick);
		global_AutoclickAdmit.setItemMeta(meta_global_AutoclickAdmit);
		global_AbusingGlitchs.setItemMeta(meta_global_AbusingGlitchs);
		global_UnallowedMods.setItemMeta(meta_global_UnallowedMods);
		global_Advertising.setItemMeta(meta_global_Advertising);
		global_Ddos.setItemMeta(meta_global_Ddos);
		hcf_SuffocationTraps.setItemMeta(meta_hcf_SuffocationTraps);
		hcf_PvpProtAbuse.setItemMeta(meta_hcf_PvpProtAbuse);
		hcf_KickAndKill.setItemMeta(meta_hcf_KickAndKill);
		hcf_LeaveAndKill.setItemMeta(meta_hcf_LeaveAndKill);
		hcf_DtrEvading.setItemMeta(meta_hcf_DtrEvading);
		hcf_Alting.setItemMeta(meta_hcf_Alting);
		hcf_GriefingOutsideClaims.setItemMeta(meta_hcf_GriefingOutsideClaims);
		hcf_Inside.setItemMeta(meta_hcf_Inside);
		hcf_Betray.setItemMeta(meta_hcf_Betray);
		hcf_Blockglitch.setItemMeta(meta_hcf_Blockglitch);
		hcf_Allying.setItemMeta(meta_hcf_Allying);
		hcf_AnvilSpam.setItemMeta(meta_hcf_AnvilSpam);
		chat_LieToStaff.setItemMeta(meta_chat_LieToStaff);
		chat_Toxicity.setItemMeta(meta_chat_Toxicity);
		chat_UsersDisrIndirectly.setItemMeta(meta_chat_UsersDisrIndirectly);
		chat_UsersDisrDirectly.setItemMeta(meta_chat_UsersDisrDirectly);
		chat_StaffDisr.setItemMeta(meta_chat_StaffDisr);
		chat_ChatSpam.setItemMeta(meta_chat_ChatSpam);
		chat_ChatFlood.setItemMeta(meta_chat_ChatFlood);
		chat_ExternalLinks.setItemMeta(meta_chat_ExternalLinks);
		chat_FactionMesaggesSpam.setItemMeta(meta_chat_FactionMesaggesSpam);
		chat_HelpopMissue.setItemMeta(meta_chat_HelpopMissue);
		misc_InsiderRank.setItemMeta(meta_misc_InsiderRank);
		misc_Unban.setItemMeta(meta_misc_Unban);
		misc_Skull.setItemMeta(skullmeta_misc_Skull);
		misc_RulesDoc.setItemMeta(meta_misc_RulesDoc);
		misc_LinksDoc.setItemMeta(meta_misc_LinksDoc);

		// ####################################################################################################################################################################### //

		inv.setItem(0, title_GlobalRules);
		inv.setItem(8, title_ChatRules);
		inv.setItem(13, title_HCFRules);
		inv.setItem(9, global_HackedClient);
		inv.setItem(10, global_HackedClientAdmit);
		inv.setItem(18, global_Xray);
		inv.setItem(19, global_XrayAdmit);
		inv.setItem(27, global_Autoclick);
		inv.setItem(28, global_AutoclickAdmit);
		inv.setItem(36, global_AbusingGlitchs);
		inv.setItem(37, global_UnallowedMods);
		inv.setItem(45, global_Advertising);
		inv.setItem(46, global_Ddos);
		inv.setItem(30, hcf_SuffocationTraps);
		inv.setItem(31, hcf_PvpProtAbuse);
		inv.setItem(32, hcf_KickAndKill);
		inv.setItem(39, hcf_DtrEvading);
		inv.setItem(40, hcf_Alting);
		inv.setItem(41, hcf_GriefingOutsideClaims);
		inv.setItem(48, hcf_Inside);
		inv.setItem(49, hcf_Blockglitch);
		inv.setItem(50, hcf_Allying);
		inv.setItem(16, chat_Toxicity);
		inv.setItem(17, chat_UsersDisrIndirectly);
		inv.setItem(25, chat_UsersDisrDirectly);
		inv.setItem(26, chat_StaffDisr);
		inv.setItem(34, chat_ChatSpam);
		inv.setItem(35, chat_ChatFlood);
		inv.setItem(43, chat_ExternalLinks);
		inv.setItem(44, chat_FactionMesaggesSpam);
		inv.setItem(2, misc_InsiderRank);
		inv.setItem(6, misc_Unban);
		inv.setItem(4, misc_Skull);
		inv.setItem(5, misc_RulesDoc);
		inv.setItem(3, misc_LinksDoc);
		inv.setItem(23, hcf_LeaveAndKill);
		inv.setItem(22, hcf_Betray);
		inv.setItem(52, chat_HelpopMissue);
		inv.setItem(21, hcf_AnvilSpam);
		inv.setItem(53, chat_LieToStaff);

		// ####################################################################################################################################################################### //

		player.openInventory(inv);
	}

	@EventHandler
	public void onClick(InventoryClickEvent event){

		if(!event.getInventory().getName().contains("Punish Manager")){
			return;
		}
		Player player = (Player) event.getWhoClicked();
		event.setCancelled(true);
		switch(event.getRawSlot()){
			// ####################################################################################################################################################################### //
			case 9:
				if(player.hasPermission("base.command.punishment.ipban")){
					Bukkit.dispatchCommand(player, "ipban " + target + " Hacked client" + silent);
					player.closeInventory();
				}else{
					Bukkit.dispatchCommand(player, "tempban " + target + " 365d" + " Hacked client" + silent);
					player.closeInventory();
				}
				break;

			case 10:
				Bukkit.dispatchCommand(player, "tempban " + target + " 30d" + " Hacked client [Admit]" + silent);
				player.closeInventory();
				break;

			case 18:
				Bukkit.dispatchCommand(player, "tempban " + target + " 15d" + " Xray" + silent);
				player.closeInventory();
				break;

			case 19:
				Bukkit.dispatchCommand(player, "tempban " + target + " 7d" + " Xray [Admit]" + silent);
				player.closeInventory();
				break;

			case 27:
				Bukkit.dispatchCommand(player, "tempban " + target + " 30d" + " Autoclick" + silent);
				player.closeInventory();
				break;

			case 28:
				Bukkit.dispatchCommand(player, "tempban " + target + " 15d" + " Autoclick [Admit]" + silent);
				player.closeInventory();
				break;

			case 36:
				Bukkit.dispatchCommand(player, "tempban " + target + " 3d" + " Abusing of glitches" + silent);
				player.closeInventory();
				break;

			case 37:
				Bukkit.dispatchCommand(player, "tempban " + target + " 15d" + " Unapproved Mods" + silent);
				player.closeInventory();
				break;

			case 45:
				if(player.hasPermission("base.command.punishment.ipban")){
					Bukkit.dispatchCommand(player, "ipban " + target + " Advertising" + silent);
					player.closeInventory();
				}else{
					Bukkit.dispatchCommand(player, "tempban " + target + " 365d" + " Advertising" + silent);
					player.closeInventory();
				}
				break;

			case 46:
				if(player.hasPermission("hcf.command.punishment.ipban")){
					Bukkit.dispatchCommand(player, "ipban " + target + " DDos" + silent);
					player.closeInventory();
				}else{
					Bukkit.dispatchCommand(player, "tempban " + target + " 365d" + " DDos" + silent);
					player.closeInventory();
				}
				break;

			// ####################################################################################################################################################################### //

			case 21:
				Bukkit.dispatchCommand(player, "warn " + target + " Anvil Spam" + silent);
				player.closeInventory();
				break;

			case 22:
				Bukkit.dispatchCommand(player, "tempban " + target + " 3d" + " Betray" + silent);
				player.closeInventory();
				break;

			case 23:
				Bukkit.dispatchCommand(player, "warn " + target + " Leave & Kill" + silent);
				player.closeInventory();
				break;

			case 30:
				Bukkit.dispatchCommand(player, "warn " + target + " Suffocation traps" + silent);
				player.closeInventory();
				break;

			case 31:
				Bukkit.dispatchCommand(player, "tempban " + target + " 1d" + " Abusing of pvp protection" + silent);
				player.closeInventory();
				break;

			case 32:
				Bukkit.dispatchCommand(player, "warn " + target + " Kick & Kill" + silent);
				player.closeInventory();
				break;

			case 39:
				Bukkit.dispatchCommand(player, "warn " + target + " DTR Evading" + silent);
				player.closeInventory();
				break;

			case 40:
				Bukkit.dispatchCommand(player, "tempban " + target + " 1d" + " Alting" + silent);
				player.closeInventory();
				break;

			case 41:
				Bukkit.dispatchCommand(player, "warn " + target + " Griefing outside faction claims" + silent);
				player.closeInventory();
				break;

			case 48:
				Bukkit.dispatchCommand(player, "warn " + target + " Inside" + silent);
				player.closeInventory();
				break;

			case 49:
				Bukkit.dispatchCommand(player, "warn " + target + " Block glitching" + silent);
				player.closeInventory();
				break;

			case 50:
				Bukkit.dispatchCommand(player, "warn " + target + " Allying" + silent);
				player.closeInventory();
				break;

			// ####################################################################################################################################################################### //

			case 16:
				Bukkit.dispatchCommand(player, "tempmute " + target + " 1h" + " Toxicity" + silent);
				player.closeInventory();
				break;

			case 17:
				Bukkit.dispatchCommand(player, "tempmute " + target + " 15m" + " Users disrespect" + silent);
				player.closeInventory();
				break;

			case 25:
				Bukkit.dispatchCommand(player, "tempmute " + target + " 30m" + " Users disrespect" + silent);
				player.closeInventory();
				break;

			case 26:
				Bukkit.dispatchCommand(player, "tempmute " + target + " 1h" + " Staff disrespect" + silent);
				player.closeInventory();
				break;

			case 34:
				Bukkit.dispatchCommand(player, "tempmute " + target + " 5m" + " Chat spam" + silent);
				player.closeInventory();
				break;

			case 35:
				Bukkit.dispatchCommand(player, "warn " + target + " Chat flood" + silent);
				player.closeInventory();
				break;

			case 43:
				Bukkit.dispatchCommand(player, "tempmute " + target + " 30m" + " External links" + silent);
				player.closeInventory();
				break;

			case 44:
				Bukkit.dispatchCommand(player, "kick " + target + " Spamming faction commands" + silent);
				player.closeInventory();
				break;

			case 52:
				Bukkit.dispatchCommand(player, "warn " + target + " Missue of Helpop" + silent);
				player.closeInventory();
				break;

			case 53:
				Bukkit.dispatchCommand(player, "tempban " + target + " 1d" + " Lying to Staff" + silent);
				player.closeInventory();
				break;

			// ####################################################################################################################################################################### //

			case 2:
				if(player.hasPermission("base.command.punishment.insider")){
					Bukkit.dispatchCommand(player, "pex user " + target + " group set Insider");
					player.closeInventory();
				}else{
					player.sendMessage(ChatColor.BOLD + " * " + ChatColor.GOLD + ChatColor.BOLD + "Punishment " + ChatColor.RED + "You don't have pex access, ask a manager.");
					player.closeInventory();
				}
				break;

			case 6:
				if(player.hasPermission("base.command.punishment.ipban")){
					Bukkit.dispatchCommand(player, "unban " + target + silent);
					player.closeInventory();
				}else{
					player.closeInventory();
					player.sendMessage(ChatColor.BOLD + " * " + ChatColor.GOLD + ChatColor.BOLD + "Punishment " + ChatColor.RED + "You can't unban players, ask a moderator.");
				}
				break;

			case 5:
				player.sendMessage(ChatColor.BOLD + " * " + ChatColor.GOLD + ChatColor.BOLD + "ALERT " + ChatColor.YELLOW + "Rules document " + ChatColor.RED + ChatColor.UNDERLINE + "https://goo.gl/ous7pE");
				player.closeInventory();
				break;

			case 3:
				player.sendMessage(ChatColor.BOLD + " * " + ChatColor.GOLD + ChatColor.BOLD + "ALERT " + ChatColor.YELLOW + "Links document " + ChatColor.RED + ChatColor.UNDERLINE + "https://goo.gl/3xc79b");
				player.closeInventory();
				break;
			case 4:
				if(event.getClick() == ClickType.LEFT) Bukkit.dispatchCommand(player, "alts " + target);
				player.closeInventory();
				if(event.getClick() == ClickType.RIGHT) Bukkit.dispatchCommand(player, "history " + target);
				player.closeInventory();
				break;
		}
		silent = "";
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		return args.length == 1 ? null : Collections.emptyList();
	}

}