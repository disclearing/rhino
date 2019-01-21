package com.sergivb01.base;

import com.google.common.collect.ImmutableMap;
import org.bukkit.entity.Player;

public enum StaffPriority{
	OWNER(6),
	HEADADMIN(5),
	STAFFMANAGER(4),
	ADMIN(3),
	MODERATOR(2),
	TRIAL(1),
	NONE(0);

	private static final ImmutableMap<Integer, StaffPriority> BY_ID;

	static{
		ImmutableMap.Builder builder = new ImmutableMap.Builder();
		for(StaffPriority staffPriority : StaffPriority.values()){
			builder.put(staffPriority.priorityLevel, staffPriority);
		}
		BY_ID = builder.build();
	}

	private final int priorityLevel;

	StaffPriority(int priorityLevel){
		this.priorityLevel = priorityLevel;
	}

	public static StaffPriority of(int level){
		return BY_ID.get(level);
	}

	public static StaffPriority of(Player player){
		for(StaffPriority staffPriority : StaffPriority.values()){
			if(!player.hasPermission("staffpriority." + staffPriority.priorityLevel)) continue;
			return staffPriority;
		}
		return NONE;
	}

	public int getPriorityLevel(){
		return this.priorityLevel;
	}

	public boolean isMoreThan(StaffPriority other){
		return this.priorityLevel > other.priorityLevel;
	}
}

