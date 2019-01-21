package com.sergivb01.util;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;
import java.util.Arrays;

public class ExperienceManager{
	private static int hardMaxLevel = 100000;
	private static int[] xpTotalToReachLevel;

	static{
		ExperienceManager.initLookupTables(25);
	}

	private final WeakReference<Player> player;
	private final String playerName;

	public ExperienceManager(Player player){
		Preconditions.checkNotNull((Object) player, "Player cannot be null");
		this.player = new WeakReference<Player>(player);
		this.playerName = player.getName();
	}

	public static int getHardMaxLevel(){
		return hardMaxLevel;
	}

	public static void setHardMaxLevel(int hardMaxLevel){
		ExperienceManager.hardMaxLevel = hardMaxLevel;
	}

	private static void initLookupTables(int maxLevel){
		xpTotalToReachLevel = new int[maxLevel];
		for(int i = 0; i < xpTotalToReachLevel.length; ++i){
			ExperienceManager.xpTotalToReachLevel[i] = i >= 30 ? (int) (3.5 * (double) i * (double) i - 151.5 * (double) i + 2220.0) : (i >= 16 ? (int) (1.5 * (double) i * (double) i - 29.5 * (double) i + 360.0) : 17 * i);
		}
	}

	private static int calculateLevelForExp(int exp){
		int level = 0;
		int curExp = 7;
		int incr = 10;
		while(curExp <= exp){
			curExp += incr;
			incr += ++level % 2 == 0 ? 3 : 4;
		}
		return level;
	}

	public Player getPlayer(){
		Player p = this.player.get();
		if(p == null){
			throw new IllegalStateException("Player " + this.playerName + " is not online");
		}
		return p;
	}

	public void changeExp(int amt){
		this.changeExp((double) amt);
	}

	public void changeExp(double amt){
		this.setExp(this.getCurrentFractionalXP(), amt);
	}

	public void setExp(int amt){
		this.setExp(0.0, amt);
	}

	public void setExp(double amt){
		this.setExp(0.0, amt);
	}

	private void setExp(double base, double amt){
		int newLvl;
		int xp = (int) Math.max(base + amt, 0.0);
		Player player = this.getPlayer();
		int curLvl = player.getLevel();
		if(curLvl != (newLvl = this.getLevelForExp(xp))){
			player.setLevel(newLvl);
		}
		if((double) xp > base){
			player.setTotalExperience(player.getTotalExperience() + xp - (int) base);
		}
		double pct = (base - (double) this.getXpForLevel(newLvl) + amt) / (double) this.getXpNeededToLevelUp(newLvl);
		player.setExp((float) pct);
	}

	public int getCurrentExp(){
		Player player = this.getPlayer();
		int lvl = player.getLevel();
		return this.getXpForLevel(lvl) + Math.round((float) this.getXpNeededToLevelUp(lvl) * player.getExp());
	}

	private double getCurrentFractionalXP(){
		Player player = this.getPlayer();
		int lvl = player.getLevel();
		return (float) this.getXpForLevel(lvl) + (float) this.getXpNeededToLevelUp(lvl) * player.getExp();
	}

	public boolean hasExp(int amt){
		return this.getCurrentExp() >= amt;
	}

	public boolean hasExp(double amt){
		return this.getCurrentFractionalXP() >= amt;
	}

	public int getLevelForExp(int exp){
		int pos;
		if(exp <= 0){
			return 0;
		}
		if(exp > xpTotalToReachLevel[xpTotalToReachLevel.length - 1]){
			int newMax = ExperienceManager.calculateLevelForExp(exp) * 2;
			Preconditions.checkArgument(newMax <= hardMaxLevel, "Level for exp " + exp + " > hard max level " + hardMaxLevel);
			ExperienceManager.initLookupTables(newMax);
		}
		return (pos = Arrays.binarySearch(xpTotalToReachLevel, exp)) < 0 ? -pos - 2 : pos;
	}

	public int getXpNeededToLevelUp(int level){
		Preconditions.checkArgument(level >= 0, "Level may not be negative.");
		return level > 30 ? 62 + (level - 30) * 7 : (level >= 16 ? 17 + (level - 15) * 3 : 17);
	}

	public int getXpForLevel(int level){
		Preconditions.checkArgument(level >= 0 && level <= hardMaxLevel, "Invalid level " + level + "(must be in range 0.." + hardMaxLevel + ')');
		if(level >= xpTotalToReachLevel.length){
			ExperienceManager.initLookupTables(level * 2);
		}
		return xpTotalToReachLevel[level];
	}
}

