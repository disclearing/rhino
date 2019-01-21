package com.sergivb01.base.warp;

import java.util.Collection;

public interface WarpManager{
	Collection<String> getWarpNames();

	Collection<Warp> getWarps();

	Warp getWarp(String var1);

	boolean containsWarp(Warp var1);

	void createWarp(Warp var1);

	void removeWarp(Warp var1);

	String getWarpDelayWords();

	long getWarpDelayMillis();

	long getWarpDelayTicks();

	void reloadWarpData();

	void saveWarpData();
}

