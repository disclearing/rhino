package com.sergivb01.hcf.combatlog;

import net.minecraft.server.v1_7_R4.EntityTypes;

import java.lang.reflect.Field;
import java.util.Map;

public class CustomEntityRegistration{
	public static void registerCustomEntities(){
		try{
			CustomEntityRegistration.registerCustomEntity(LoggerEntity.class, "CraftVillager", 120);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void registerCustomEntity(Class entityClass, String name, int id) {
		setFieldPrivateStaticMap("d", entityClass, name);
		setFieldPrivateStaticMap("f", entityClass, Integer.valueOf(id));
	}

	public static void unregisterCustomEntities() {
	}

	public static void setFieldPrivateStaticMap(String fieldName, Object key, Object value) {
		try {
			Field ex = EntityTypes.class.getDeclaredField(fieldName);
			ex.setAccessible(true);
			Map map = (Map) ex.get(null);
			map.put(key, value);
			ex.set(null, map);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var5) {
			var5.printStackTrace();
		}
	}

	public static void setField(String fieldName, Object key, Object value) {
		try {
			Field ex = key.getClass().getDeclaredField(fieldName);
			ex.setAccessible(true);
			ex.set(key, value);
			ex.setAccessible(false);
		} catch (Exception var4) {
			var4.printStackTrace();
		}
	}
}
