package com.sergivb01.util;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.PostLoad;
import org.mongodb.morphia.annotations.PreSave;
import org.mongodb.morphia.annotations.Transient;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Embedded
public class PersistableLocation implements ConfigurationSerializable, Cloneable{
	@Transient
	private Location location;
	@Transient
	private World world;
	private String worldName;
	@Transient
	private UUID worldUID;
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;

	public PersistableLocation(Location location){
		Preconditions.checkNotNull((Object) location, "Location cannot be null");
		Preconditions.checkNotNull((Object) location.getWorld(), "Locations' world cannot be null");
		this.world = location.getWorld();
		this.worldName = this.world.getName();
		this.worldUID = this.world.getUID();
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.yaw = location.getYaw();
		this.pitch = location.getPitch();
	}

	public PersistableLocation(World world, double x, double y, double z){
		this.worldName = world.getName();
		this.x = x;
		this.y = y;
		this.z = z;
		float n = 0.0f;
		this.yaw = 0.0f;
		this.pitch = 0.0f;
	}

	public PersistableLocation(String worldName, double x, double y, double z){
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
		float n = 0.0f;
		this.yaw = 0.0f;
		this.pitch = 0.0f;
	}

	public PersistableLocation(Map<String, Object> map){
		this.worldName = (String) map.get("worldName");
		this.worldUID = UUID.fromString((String) map.get("worldUID"));
		Object o = map.get("x");
		this.x = o instanceof String ? Double.parseDouble((String) o) : (Double) o;
		o = map.get("y");
		this.y = o instanceof String ? Double.parseDouble((String) o) : (Double) o;
		o = map.get("z");
		this.z = o instanceof String ? Double.parseDouble((String) o) : (Double) o;
		this.yaw = Float.parseFloat((String) map.get("yaw"));
		this.pitch = Float.parseFloat((String) map.get("pitch"));
	}

	@PreSave
	public void presaveMethod(){
		if(this.worldName == null && this.world != null){
			this.worldName = this.world.getName();
		}
	}

	@PostLoad
	public void postloadMethod(){
		if(this.worldName != null){
			this.world = Bukkit.getWorld(this.worldName);
			if(this.world != null){
				this.worldUID = this.world.getUID();
			}
		}
	}

	public Map<String, Object> serialize(){
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("worldName", this.worldName);
		map.put("worldUID", this.worldUID.toString());
		map.put("x", this.x);
		map.put("y", this.y);
		map.put("z", this.z);
		map.put("yaw", Float.toString(this.yaw));
		map.put("pitch", Float.toString(this.pitch));
		return map;
	}

	public String getWorldName(){
		return this.worldName;
	}

	private void setWorldName(String worldName){
		this.worldName = worldName;
	}

	public UUID getWorldUID(){
		return this.worldUID;
	}

	private void setWorldUID(UUID worldUID){
		this.worldUID = worldUID;
	}

	public World getWorld(){
		Preconditions.checkNotNull((Object) this.worldUID, "World UUID cannot be null");
		Preconditions.checkNotNull((Object) this.worldName, "World name cannot be null");
		if(this.world == null){
			this.world = Bukkit.getWorld(this.worldUID);
		}
		return this.world;
	}

	public void setWorld(World world){
		this.worldName = world.getName();
		this.worldUID = world.getUID();
		this.world = world;
	}

	public double getX(){
		return this.x;
	}

	public void setX(double x){
		this.x = x;
	}

	public double getY(){
		return this.y;
	}

	public void setY(double y){
		this.y = y;
	}

	public double getZ(){
		return this.z;
	}

	public void setZ(double z){
		this.z = z;
	}

	public float getYaw(){
		return this.yaw;
	}

	public void setYaw(float yaw){
		this.yaw = yaw;
	}

	public float getPitch(){
		return this.pitch;
	}

	public void setPitch(float pitch){
		this.pitch = pitch;
	}

	public Location getLocation(){
		if(this.location == null){
			this.location = new Location(this.getWorld(), this.x, this.y, this.z, this.yaw, this.pitch);
		}
		return this.location;
	}

	public PersistableLocation clone(){
		try{
			return (PersistableLocation) super.clone();
		}catch(CloneNotSupportedException ex){
			ex.printStackTrace();
			throw new RuntimeException();
		}
	}

	public String toString(){
		return "PersistableLocation [worldName=" + this.worldName + ", worldUID=" + this.worldUID + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", yaw=" + this.yaw + ", pitch=" + this.pitch + ']';
	}

	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		if(!(o instanceof PersistableLocation)){
			return false;
		}
		PersistableLocation that = (PersistableLocation) o;
		if(Double.compare(that.x, this.x) != 0){
			return false;
		}
		if(Double.compare(that.y, this.y) != 0){
			return false;
		}
		if(Double.compare(that.z, this.z) != 0){
			return false;
		}
		if(Float.compare(that.yaw, this.yaw) != 0){
			return false;
		}
		if(Float.compare(that.pitch, this.pitch) != 0){
			return false;
		}
		if(this.world != null ? !this.world.equals(that.world) : that.world != null){
			return false;
		}
		if(this.worldName != null ? !this.worldName.equals(that.worldName) : that.worldName != null){
			return false;
		}
		if(this.worldUID != null){
			return this.worldUID.equals(that.worldUID);
		}
		return that.worldUID == null;
	}

	public int hashCode(){
		int result = this.world != null ? this.world.hashCode() : 0;
		result = 31 * result + (this.worldName != null ? this.worldName.hashCode() : 0);
		result = 31 * result + (this.worldUID != null ? this.worldUID.hashCode() : 0);
		long temp = Double.doubleToLongBits(this.x);
		result = 31 * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(this.y);
		result = 31 * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(this.z);
		result = 31 * result + (int) (temp ^ temp >>> 32);
		result = 31 * result + (this.yaw != 0.0f ? Float.floatToIntBits(this.yaw) : 0);
		result = 31 * result + (this.pitch != 0.0f ? Float.floatToIntBits(this.pitch) : 0);
		return result;
	}
}

