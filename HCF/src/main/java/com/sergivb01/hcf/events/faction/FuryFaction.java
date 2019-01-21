package com.sergivb01.hcf.events.faction;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.sergivb01.hcf.events.CaptureZone;
import com.sergivb01.hcf.events.EventType;
import com.sergivb01.hcf.faction.claim.Claim;
import com.sergivb01.hcf.faction.type.ClaimableFaction;
import com.sergivb01.util.BukkitUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

public class FuryFaction extends CapturableFaction implements ConfigurationSerializable{

	private final EnumMap<FuryZone, CaptureZone> captureZones = new EnumMap<>(FuryZone.class);

	public FuryFaction(String name){
		super(name);
	}

	public FuryFaction(Map<String, Object> map){
		super(map);

		Object object;
		if((object = map.get("overworld")) instanceof CaptureZone){
			captureZones.put(FuryZone.GREEN, (CaptureZone) object);
		}

		if((object = map.get("nether")) instanceof CaptureZone){
			captureZones.put(FuryZone.RED, (CaptureZone) object);
		}

	}

	@Override
	public Map<String, Object> serialize(){
		Map<String, Object> map = super.serialize();
		for(Map.Entry<FuryZone, CaptureZone> entry : captureZones.entrySet()){
			map.put(entry.getKey().name().toLowerCase(), entry.getValue());
		}

		return map;
	}

	@Override
	public EventType getEventType(){
		return EventType.CONQUEST;
	}

	public FuryZone getZone(final CaptureZone captureZone){
		for(final Map.Entry<FuryZone, CaptureZone> captureZoneEntry : this.captureZones.entrySet()){
			if(captureZoneEntry.getValue() == captureZone){
				return captureZoneEntry.getKey();
			}
		}
		return null;
	}

	@Override
	public void printDetails(CommandSender sender){
		sender.sendMessage(ChatColor.GOLD + BukkitUtils.STRAIGHT_LINE_DEFAULT);
		sender.sendMessage(getDisplayName(sender));

		for(Claim claim : claims){
			Location location = claim.getCenter();
			sender.sendMessage(ChatColor.YELLOW + "  Location: " + ChatColor.RED + '(' + ClaimableFaction.ENVIRONMENT_MAPPINGS.get(location.getWorld().getEnvironment()) + ", " + location.getBlockX() + " | " + location.getBlockZ() + ')');
		}

		sender.sendMessage(ChatColor.GOLD + BukkitUtils.STRAIGHT_LINE_DEFAULT);
	}

	public void setZone(FuryZone conquestZone, CaptureZone captureZone){
		switch(conquestZone){
			case RED:
				captureZones.put(FuryZone.RED, captureZone);
				break;
			case GREEN:
				captureZones.put(FuryZone.GREEN, captureZone);
				break;
			default:
				throw new AssertionError("Unsupported operation");
		}
	}

	public CaptureZone getNether(){
		return captureZones.get(FuryZone.RED);
	}

	public CaptureZone getOverworld(){
		return captureZones.get(FuryZone.GREEN);
	}

	public Collection<FuryZone> getFuryZones(){
		return ImmutableSet.copyOf(captureZones.keySet());
	}

	@Override
	public List<CaptureZone> getCaptureZones(){
		return ImmutableList.copyOf(captureZones.values());
	}

	public enum FuryZone{
		RED(ChatColor.RED, "Nether"), GREEN(ChatColor.GREEN, "Overworld");

		private static final Map<String, FuryZone> BY_NAME;

		static{
			ImmutableMap.Builder<String, FuryZone> builder = ImmutableMap.builder();
			for(FuryZone zone : values()){
				builder.put(zone.name().toUpperCase(), zone);
			}

			BY_NAME = builder.build();
		}

		private final String name;
		private final ChatColor color;

		FuryZone(ChatColor color, String name){
			this.color = color;
			this.name = name;
		}

		public static FuryZone getByName(String name){
			return BY_NAME.get(name.toUpperCase());
		}

		public static Collection<String> getNames(){
			return new ArrayList<>(BY_NAME.keySet());
		}

		public ChatColor getColor(){
			return color;
		}

		public String getName(){
			return name;
		}

		public String getDisplayName(){
			return color.toString() + name;
		}
	}
}
