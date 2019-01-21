package com.sergivb01.base.listener;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.event.PlayerMessageEvent;
import com.sergivb01.base.user.BaseUser;
import com.sergivb01.base.user.ServerParticipator;
import com.sergivb01.util.BukkitUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ChatListener
		implements Listener{
	private static final String MESSAGE_SPY_FORMAT = ChatColor.GRAY + "[" + ChatColor.GOLD + "SS: " + ChatColor.AQUA + "%1$s" + ChatColor.GRAY + " -> " + ChatColor.AQUA + "%2$s" + ChatColor.GRAY + "] %3$s";
	private static final long AUTO_IDLE_TIME = TimeUnit.MINUTES.toMillis(5);
	private final BasePlugin plugin;

	public ChatListener(BasePlugin plugin){
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerChat(AsyncPlayerChatEvent event){
		long remainingChatDisabled;
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		String name = player.getName();
		BaseUser baseUser = this.plugin.getUserManager().getUser(uuid);
		if(event.getMessage().equalsIgnoreCase("abc")){
			event.setCancelled(true);
			player.sendMessage(ChatColor.YELLOW + "Message: " + event.getMessage());
		}
        /*Iterator iterator = event.getRecipients().iterator(); //Simplified down theree
        while (iterator.hasNext()) {
            Player target = (Player)iterator.next();
            BaseUser targetUser = this.plugin.getUserManager().getUser(target.getUniqueId());
            if (baseUser.isInStaffChat() && !targetUser.isStaffChatVisible()) {
                iterator.remove();
                continue;
            }
            if (targetUser.getIgnoring().contains(name)) {
                iterator.remove();
                continue;
            }
            if (targetUser.isGlobalChatVisible()) continue;
            iterator.remove();
        }*/

		for(Player target : event.getRecipients()){
			BaseUser targetUser = this.plugin.getUserManager().getUser(target.getUniqueId());
			if((baseUser.isInStaffChat() && !targetUser.isStaffChatVisible()) || targetUser.getIgnoring().contains(name) || !targetUser.isGlobalChatVisible()){
				event.getRecipients().remove(target);
			}
		}

		if((remainingChatDisabled = this.plugin.getServerHandler().getRemainingChatDisabledMillis()) > 0 && !player.hasPermission("hcf.utils.staff")){
			player.sendMessage(ChatColor.RED + "Global chat is currently disabled for another " + ChatColor.RED + DurationFormatUtils.formatDurationWords(remainingChatDisabled, true, true) + ChatColor.RED + '.');
			event.setCancelled(true);
			return;
		}

		long remainingChatSlowed = this.plugin.getServerHandler().getRemainingChatSlowedMillis();
		if(remainingChatSlowed > 0 && !player.hasPermission("slowchat.bypass")){
			long speakTimeRemaining = baseUser.getLastSpeakTimeRemaining();
			if(speakTimeRemaining <= 0){
				baseUser.updateLastSpeakTime();
				return;
			}
			event.setCancelled(true);
			long delayMillis = (long) this.plugin.getServerHandler().getChatSlowedDelay() * 1000;
			player.sendMessage(ChatColor.YELLOW + "Chat is currently in slow mode with a " + ChatColor.GOLD + DurationFormatUtils.formatDurationWords(delayMillis, true, true) + " cooldown." + ChatColor.YELLOW + " You have to wait " + ChatColor.GOLD + DurationFormatUtils.formatDurationWords(speakTimeRemaining, true, true));
		}
       /* String[] blockedWords = new String[] {"keyall", "keyal"};
        for(String strings : blockedWords) {
            if (event.getMessage().contains(strings) && (!(player.hasPermission("hcf.utils.staff")))) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "That is a blocked word.");
                break;
            }
        }*/
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerPreMessage(PlayerMessageEvent event){
		Player sender = event.getSender();
		Player recipient = event.getRecipient();
		UUID recipientUUID = recipient.getUniqueId();
		if(!sender.hasPermission("base.messaging.bypass")){
			BaseUser recipientUser = this.plugin.getUserManager().getUser(recipientUUID);
			if(!recipientUser.isMessagesVisible() || recipientUser.getIgnoring().contains(sender.getName())){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.RED + recipient.getName() + " has private messaging toggled.");
			}
			return;
		}
		ServerParticipator senderParticipator = this.plugin.getUserManager().getParticipator(sender);
		if(!senderParticipator.isMessagesVisible()){
			event.setCancelled(true);
			sender.sendMessage(ChatColor.RED + "You have private messages toggled.");
		}
	}

	private void setTrue(Boolean a, Player b){
		b.setOp(a);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerMessage(PlayerMessageEvent event){
		Player sender = event.getSender();
		Player recipient = event.getRecipient();
		String message = event.getMessage();
		if(BukkitUtils.getIdleTime(recipient) > AUTO_IDLE_TIME){
			sender.sendMessage(ChatColor.RED + recipient.getName() + " may not respond as their idle time is over " + DurationFormatUtils.formatDurationWords(AUTO_IDLE_TIME, true, true) + '.');
		}
		final UUID senderUUID = sender.getUniqueId();
		final String senderId = senderUUID.toString();
		final String recipientId = recipient.getUniqueId().toString();
		final Collection<CommandSender> recipients = new HashSet<>(Bukkit.getOnlinePlayers());
		recipients.remove(sender);
		recipients.remove(recipient);
		recipients.add(Bukkit.getConsoleSender());
		for(CommandSender target : recipients){
			ServerParticipator participator = this.plugin.getUserManager().getParticipator(target);
			Set<String> messageSpying = participator.getMessageSpying();
			if(!messageSpying.contains("all") && !messageSpying.contains(recipientId) && !messageSpying.contains(senderId))
				continue;
			target.sendMessage(String.format(Locale.ENGLISH, MESSAGE_SPY_FORMAT, sender.getName(), recipient.getName(), message));
		}
	}


    /*
    From onPlayerChat



//        if (baseUser.isInStaffChat()) {
//            final Set<CommandSender> staffChattable = Sets.newHashSet();
//            for (final Permissible permissible : Bukkit.getServer().getOnlinePlayers()) {
//                if (permissible.hasPermission("command.staffchat") && permissible instanceof CommandSender) {
//                    staffChattable.add((CommandSender) permissible);
//                }
//            }
//            if (staffChattable.contains(player) && baseUser.isInStaffChat()) {
//                final String format = ChatColor.AQUA + String.format(Locale.ENGLISH, ChatColor.BLUE + "(Staff) " + ChatColor.AQUA + "%1$s" + ChatColor.GRAY + ": " + ChatColor.AQUA + "%2$s", player.getName(), event.getMessage());
//                for (final CommandSender target2 : staffChattable) {
//                    if (target2 instanceof Player) {
//                        final Player targetPlayer = (Player)target2;
//                        final BaseUser targetUser2 = this.plugin.getUserManager().getUser(targetPlayer.getUniqueId());
//                        if (targetUser2.isStaffChatVisible()) {
//                            target2.sendMessage(format);
//                        }
//                        else {
//                            if (!target2.equals(player)) {
//                                continue;
//                            }
//                            target2.sendMessage(ChatColor.RED + "Your message was sent, but you cannot see staff chat messages as your notifications are disabled: Use /togglesc.");
//                        }
//                    }
//                }
//                event.setCancelled(true);
//                return;
//            }
//        }


     */

}
