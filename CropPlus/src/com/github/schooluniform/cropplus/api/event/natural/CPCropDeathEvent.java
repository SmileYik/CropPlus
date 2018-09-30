package com.github.schooluniform.cropplus.api.event.natural;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.github.schooluniform.cropplus.api.event.CropPlusEvent;

public class CPCropDeathEvent extends CropPlusEvent{
	private CropDeathEnum death;
	private Entity entity;
	public CPCropDeathEvent(String cropId, Location location,CropDeathEnum death) {
		super(cropId, location);
		this.death = death;
	}
	public CPCropDeathEvent(String cropId, Location location,CropDeathEnum death,Entity entity) {
		super(cropId, location);
		this.death = death;
		this.entity = entity;
	}
	
	public CropDeathEnum getDeathWay() {
		return death;
	}
	
	public Entity getEntity() {
		return entity;
	}
}
