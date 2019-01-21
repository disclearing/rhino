package com.sergivb01.base.command.module.essential;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.command.BaseCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class RulesCommand
		extends BaseCommand{
	private final BasePlugin plugin;
	//final HashMap<String, String> rule = new HashMap();

	public RulesCommand(BasePlugin plugin){
		super("rules", "Shows the server rules.");
		this.setUsage("/(command)");
        /*this.rule.put("Staff-Disrespect", "Intentionally trying to insult and criticise a staff member.");
        this.rule.put("DDoS-Threats", "Threats relating to DoS attacks [DDoS].");
        this.rule.put("DDoS-Comedy", "Joking around about DDoS.");
        this.rule.put("Spamming", "Constantly posting the same message over.");
        this.rule.put("Chat-Flood", "Using multiple lines to express one's thought.");
        this.rule.put("Racist-Content", "Any messages related to profanity towards one's race.");
        this.rule.put("Death-Threats", "Messages that encourage harm/suicidal inflictions towards one's personal life.");
        this.rule.put("Unfair-Modification", "Any mods that aren't on the allowed mod list.");
        this.rule.put("Insiding", "Removing items and or Griefing your current faction to then leave and betray.");*/
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        /*Integer ruleAmount = 1;
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
            for (String string : this.rule.keySet()) {
                new Text(ChatColor.GRAY + "[" + ruleAmount + "] " + ChatColor.DARK_AQUA.toString() + string + ". " + ChatColor.AQUA + this.rule.get(string)).setColor(ChatColor.AQUA).setHoverText(ChatColor.GOLD + "Click for more information").setClick(ClickAction.RUN_COMMAND, "/rules " + string).send(sender);
                Integer n = ruleAmount;
                Integer n2 = ruleAmount = Integer.valueOf(ruleAmount + 1);
            }
            sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        }
        if (args.length == 1) {
            for (String string : this.rule.keySet()) {
                if (!args[0].equalsIgnoreCase(string)) continue;
                if (string.equalsIgnoreCase("Staff-Disrespect")) {
                    sender.sendMessage(ChatColor.YELLOW + "In-Depth Description: " + ChatColor.RED + "\n Any attempts to disrupt or discourage a staff member from performing their duty will \nnot be tolerated. Issues with staff members should be brought up on the forums with valid evidence. \nDisrespecting staff or undermining decisions will result in consequence.");
                    sender.sendMessage("");
                    sender.sendMessage(ChatColor.GOLD + "Punishments: ");
                    sender.sendMessage(ChatColor.YELLOW + "First Offence: " + ChatColor.RED + "30 Minute Mute");
                    sender.sendMessage(ChatColor.YELLOW + "Second Offence: " + ChatColor.RED + "1 Hour Mute");
                    sender.sendMessage(ChatColor.YELLOW + "Third Offence: " + ChatColor.RED + "1 Day Mute");
                }
                if (string.equalsIgnoreCase("DDoS-Threats")) {
                    sender.sendMessage(ChatColor.YELLOW + "In-Depth Description: " + ChatColor.RED + "\n Chat messages related to inappropriate DDoS usage are subject to consequence, whether \nthe opposing party is joking or not.\n*These are considered federal crimes in most countries and will NOT be tolerated on our servers.* ");
                    sender.sendMessage("");
                    sender.sendMessage(ChatColor.GOLD + "Punishments: ");
                    sender.sendMessage(ChatColor.YELLOW + "First Offence: " + ChatColor.RED + "Permanent Ban");
                }
                if (string.equalsIgnoreCase("DDoS-Comedy")) {
                    sender.sendMessage(ChatColor.YELLOW + "In-Depth Description: " + ChatColor.RED + "\n Joking about DDoS. Not to be confused with DDoS threats.");
                    sender.sendMessage("");
                    sender.sendMessage(ChatColor.GOLD + "Punishments: ");
                    sender.sendMessage(ChatColor.YELLOW + "First Offence: " + ChatColor.RED + "Mute or a Temporary Ban depending on the severity.");
                }
                if (string.equalsIgnoreCase("Spamming")) {
                    sender.sendMessage(ChatColor.YELLOW + "In-Depth Description: " + ChatColor.RED + "\nPosting the same message more than 3 times.");
                    sender.sendMessage("");
                    sender.sendMessage(ChatColor.GOLD + "Punishments: ");
                    sender.sendMessage(ChatColor.YELLOW + "First Offence: " + ChatColor.RED + "5 Minute Mute");
                    sender.sendMessage(ChatColor.YELLOW + "Second Offence: " + ChatColor.RED + "15 Minute Mute");
                    sender.sendMessage(ChatColor.YELLOW + "Third Offence: " + ChatColor.RED + "30 Minute Mute");
                    sender.sendMessage(ChatColor.YELLOW + "Fourth Offence: " + ChatColor.RED + "Tempban 3 Days");
                }
                if (string.equalsIgnoreCase("Chat-Flood")) {
                    sender.sendMessage(ChatColor.YELLOW + "In-Depth Description: " + ChatColor.RED + "\nUsing multiple lines to express one's thoughts.");
                    sender.sendMessage("");
                    sender.sendMessage(ChatColor.GOLD + "Punishments: ");
                    sender.sendMessage(ChatColor.YELLOW + "First Offence: " + ChatColor.RED + "Warn");
                    sender.sendMessage(ChatColor.YELLOW + "Second Offence: " + ChatColor.RED + "5 Minute Mute");
                    sender.sendMessage(ChatColor.YELLOW + "Third Offence: " + ChatColor.RED + "15 Minute Mute");
                    sender.sendMessage(ChatColor.YELLOW + "Fourth Offence: " + ChatColor.RED + "30 Minute Mute");
                }
                if (string.equalsIgnoreCase("Racism")) {
                    sender.sendMessage(ChatColor.YELLOW + "In-Depth Description: " + ChatColor.RED + "\nWe do not want racial content on the Network, people do not want to be called \u00e2\u20ac\u0153ni*gers\u00e2\u20ac\ufffd etc. \nRacist \u00e2\u20ac\u02dc\u00e2\u20ac\u2122Slang\u00e2\u20ac\u2122\u00e2\u20ac\u2122 such as nigga is discouraged but is not punishable.");
                    sender.sendMessage("");
                    sender.sendMessage(ChatColor.GOLD + "Punishments: ");
                    sender.sendMessage(ChatColor.YELLOW + "First Offence: " + ChatColor.RED + "1 Hour Mute");
                    sender.sendMessage(ChatColor.YELLOW + "Second Offence: " + ChatColor.RED + "3 Hour Mute");
                    sender.sendMessage(ChatColor.YELLOW + "Third Offence: " + ChatColor.RED + "1 Day Mute");
                    sender.sendMessage(ChatColor.GOLD + "Note: Punishment may vary due to severity");
                }
                if (string.equalsIgnoreCase("Death-Threats")) {
                    sender.sendMessage(ChatColor.YELLOW + "In-Depth Description: " + ChatColor.RED + "\nAny sort of Death Threats will not be tolerated on the Network.\nWe want to have a very clean server, and that does not involve people telling them to kill themselves.");
                    sender.sendMessage("");
                    sender.sendMessage(ChatColor.GOLD + "Punishments: ");
                    sender.sendMessage(ChatColor.YELLOW + "First Offence: " + ChatColor.RED + "30 Minute Mute");
                    sender.sendMessage(ChatColor.YELLOW + "Second Offence: " + ChatColor.RED + "1 Hour Mute");
                    sender.sendMessage(ChatColor.YELLOW + "Third Offence: " + ChatColor.RED + "1 Day Mute");
                    sender.sendMessage(ChatColor.GOLD + "Note: Punishment may vary due to severity");
                }
                if (string.equalsIgnoreCase("Unfair-Modification")) {
                    sender.sendMessage(ChatColor.YELLOW + "In-Depth Description: " + ChatColor.RED + "\nSuch mods include hacked clients, see-through texture packs, etc.\nInquire with a staff member about mods you have doubts about.");
                    sender.sendMessage("");
                    sender.sendMessage(ChatColor.GOLD + "Punishments: ");
                    sender.sendMessage(ChatColor.YELLOW + "First Offence: " + ChatColor.RED + "Permanent Ban");
                    sender.sendMessage(ChatColor.GOLD + "Note: Punishment may vary due to severity");
                }
                if (!string.equalsIgnoreCase("Insiding")) continue;
                sender.sendMessage(ChatColor.YELLOW + "In-Depth Description: " + ChatColor.RED + "\nAny time you remove an item or block in intent to harm or betray your faction.\nThis may include griefing then leaving, or taking items for another Faction in itent to betray.\nAnything that you willingly and knowingly do to cause harm to the faction for antoehr faction will fall under this rule.");
                sender.sendMessage("");
                sender.sendMessage(ChatColor.GOLD + "Punishments: ");
                sender.sendMessage(ChatColor.YELLOW + "First Offence: " + ChatColor.RED + "3 Day Tempban & Prefix");
                sender.sendMessage(ChatColor.YELLOW + "Second Offence: " + ChatColor.RED + "5 Day TempBan & Longer Deathban");
                sender.sendMessage(ChatColor.YELLOW + "Third Offence: " + ChatColor.RED + "Blacklist from joining any faction.");
                sender.sendMessage(ChatColor.GOLD + "Note: Punishment may vary due to severity");
            }
        }*/
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eRules can be found at &aveilhcf.us/rules"));
		return true;
	}
}

