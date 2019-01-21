package com.sergivb01.hcf.events;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.events.palace.PalaceTracker;
import com.sergivb01.hcf.events.tracker.ConquestTracker;
import com.sergivb01.hcf.events.tracker.EventTracker;
import com.sergivb01.hcf.events.tracker.KothTracker;


public enum EventType{
	CONQUEST("Conquest", new ConquestTracker(HCF.getPlugin())), PALACE("Palace", new PalaceTracker((HCF.getPlugin()))), KOTH("KingOfTheHill", new KothTracker(HCF.getPlugin()));

	private static final ImmutableMap<String, EventType> byDisplayName;

	static{
		ImmutableBiMap.Builder builder = new ImmutableBiMap.Builder();
		for(EventType eventType : EventType.values()){
			builder.put(eventType.displayName.toLowerCase(), eventType);
		}
		byDisplayName = builder.build();
	}

	private final EventTracker eventTracker;
	private final String displayName;

	EventType(String displayName, EventTracker eventTracker){
		this.displayName = displayName;
		this.eventTracker = eventTracker;
	}

	@Deprecated
	public static EventType getByDisplayName(String name){
		return (EventType) byDisplayName.get(name.toLowerCase());
	}

	public EventTracker getEventTracker(){
		return this.eventTracker;
	}

	public String getDisplayName(){
		return this.displayName;
	}
}

