package com.github.schooluniform.cropplus.api.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.schooluniform.cropplus.api.event.CropPlusEvent;

/**
 * CropPlusPlayerEvent
 * @author School_Uniform
 *
 */
public class CropPlusPlayerEvent extends CropPlusEvent{
    private Player player;

     protected CropPlusPlayerEvent(Player player,String cropId,Location location) {
    	 super(cropId,location);
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}
}
