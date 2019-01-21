package com.sergivb01.util.player;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class PlayerList
		implements Iterable<Player>{
	private final List<UUID> playerUniqueIds;
	private final List<Player> playerList = new ArrayList<Player>();

	public PlayerList(){
		this.playerUniqueIds = new ArrayList<UUID>();
	}

	public PlayerList(Iterable<UUID> iterable){
		this.playerUniqueIds = Lists.newArrayList(iterable);
	}

	@Override
	public Iterator<Player> iterator(){
		return new Iterator<Player>(){
			private int index;

			@Override
			public boolean hasNext(){
				return !PlayerList.this.playerUniqueIds.isEmpty() && this.index < PlayerList.this.playerUniqueIds.size();
			}

			@Override
			public Player next(){
				++this.index;
				return PlayerList.this.getPlayers().get(this.index - 1);
			}

			@Override
			public void remove(){
			}
		};
	}

	public int size(){
		return this.playerUniqueIds.size();
	}

	public List<Player> getPlayers(){
		this.playerList.clear();
		for(UUID uuid : this.playerUniqueIds){
			this.playerList.add(Bukkit.getPlayer(uuid));
		}
		return this.playerList;
	}

	public boolean contains(Player player){
		return player != null && this.playerUniqueIds.contains(player.getUniqueId());
	}

	public boolean add(Player player){
		return !this.playerUniqueIds.contains(player.getUniqueId()) && this.playerUniqueIds.add(player.getUniqueId());
	}

	public boolean remove(Player player){
		return this.playerUniqueIds.remove(player.getUniqueId());
	}

	public void remove(UUID playerUUID){
		this.playerUniqueIds.remove(playerUUID);
	}

	public void clear(){
		this.playerUniqueIds.clear();
	}

}

