package com.github.schooluniform.cropplus.data;

import org.bukkit.Material;

public enum Crop {
	Wheat(Material.CROPS),
	Carrot(Material.CARROT),
	Potato(Material.POTATO),
	Beetroot(Material.BEETROOT_BLOCK)
	;
	private Material type;
	
	Crop(Material type){
		this.type = type;
	}
	
	public Material getType(){
		return type;
	}
}
