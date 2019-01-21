package com.sergivb01.hcf.events.tracker;

import com.sergivb01.hcf.events.CaptureZone;
import com.sergivb01.hcf.events.EventTimer;
import com.sergivb01.hcf.events.EventType;
import com.sergivb01.hcf.events.faction.EventFaction;
import org.bukkit.entity.Player;

@Deprecated
public interface EventTracker{
	EventType getEventType();

	void tick(EventTimer var1, EventFaction var2);

	void onContest(EventFaction var1, EventTimer var2);

	boolean onControlTake(Player var1, CaptureZone var2);

	boolean onControlLoss(Player var1, CaptureZone var2, EventFaction var3);

	void stopTiming();
}

