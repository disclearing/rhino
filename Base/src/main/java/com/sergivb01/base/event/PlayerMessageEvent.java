package com.sergivb01.base.event;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.user.BaseUser;
import com.sergivb01.util.chat.StaffMessage;
import me.joeleoli.nucleus.player.NucleusPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Set;

public class PlayerMessageEvent extends Event implements Cancellable{
	private static final HandlerList handlers = new HandlerList();
	private final Player sender;
	private final Player recipient;
	private final String message;
	private final boolean isReply;
	private boolean cancelled = false;

	public PlayerMessageEvent(Player sender, Set<Player> recipients, String message, boolean isReply){
		this.sender = sender;
		this.recipient = (Player) Iterables.getFirst(recipients, (Object) null);
		this.message = message;
		this.isReply = isReply;
	}

	public static HandlerList getHandlerList(){
		return handlers;
	}

	public Player getSender(){
		return this.sender;
	}

	public Player getRecipient(){
		return this.recipient;
	}

	public String getMessage(){
		return this.message;
	}

	public boolean isReply(){
		return this.isReply;
	}

	public void send(){
		Preconditions.checkNotNull((Object) this.sender, "The sender cannot be null");
		Preconditions.checkNotNull((Object) this.recipient, "The recipient cannot be null");
		BasePlugin plugin = BasePlugin.getPlugin();
		BaseUser sendingUser = plugin.getUserManager().getUser(this.sender.getUniqueId());
		BaseUser recipientUser = plugin.getUserManager().getUser(this.recipient.getUniqueId());
		sendingUser.setLastRepliedTo(recipientUser.getUniqueId());
		recipientUser.setLastRepliedTo(sendingUser.getUniqueId());
		long millis = System.currentTimeMillis();
		recipientUser.setLastReceivedMessageMillis(millis);

		String[] blockedWords = new String[]
				{
						"kys"
						, "nigger"
						, "hack"
						, "phase"
						, "aura"
						, "killaura"
						, "forceop"
						, "exploit"
						, "theboys"
						, "antikb"
						, "grief"
						, "etb"
						, "client"
						, "hack"
						, "worldedit"
				};

		for(String strings : blockedWords){
			if(this.message.contains(strings)){
				StaffMessage.broadcastFilter(this.sender, ChatColor.translateAlternateColorCodes('&', "&7&o-> " + this.recipient.getName() + "&7&o: &7&o" + this.message.replace(strings, ChatColor.RED + "" + ChatColor.ITALIC + strings + ChatColor.GRAY + "" + ChatColor.ITALIC)), false);
				break;
			}
		}

		String rank;
		String displayName;
		String rank1;
		String displayName1;

		if(plugin.getConfig().getBoolean("nucleus")) {
			rank = ChatColor.translateAlternateColorCodes('&', "&f" + NucleusPlayer.getByUuid(sender.getUniqueId()).getActiveRank().getPrefix()).replace("_", " ");
			displayName = rank + this.sender.getDisplayName();
			rank1 = ChatColor.translateAlternateColorCodes('&', "&f" + NucleusPlayer.getByUuid(recipient.getUniqueId()).getActiveRank().getPrefix()).replace("_", " ");
			displayName1 = rank1 + this.recipient.getDisplayName();
		} else {
			rank = ChatColor.translateAlternateColorCodes('&', "&f" + BasePlugin.getChat().getPlayerPrefix(this.sender)).replace("_", " ");
			displayName = rank + this.sender.getDisplayName();
			rank1 = ChatColor.translateAlternateColorCodes('&', "&f" + BasePlugin.getChat().getPlayerPrefix(this.recipient)).replace("_", " ");
			displayName1 = rank1 + this.recipient.getDisplayName();
		}

		this.sender.sendMessage(ChatColor.GRAY + "(" + ChatColor.GRAY + "To " + displayName1 + ChatColor.GRAY + ") " + ChatColor.GRAY + this.message);
		this.recipient.sendMessage(ChatColor.GRAY + "(" + ChatColor.GRAY + "From " + displayName + ChatColor.GRAY + ") " + ChatColor.GRAY + this.message);
	}

	public boolean isCancelled(){
		return this.cancelled;
	}

	public void setCancelled(boolean cancelled){
		this.cancelled = cancelled;
	}

	public HandlerList getHandlers(){
		return handlers;
	}
}

