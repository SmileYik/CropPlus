package com.github.schooluniform.cropplus.data.manager;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import com.github.schooluniform.cropplus.CropPlus;
import com.outlook.schooluniformsama.api.ReaLSurvivalAPI;

public class Manager {
	private static CropPlus plugin;
	private static String split,label;
	public static int noWater;
	public static ReaLSurvivalAPI rsAPI;
	public static void init(CropPlus plugin){
		Manager.plugin = plugin;
		split = plugin.getConfig().getString("lore.split","Seed");
		label = plugin.getConfig().getString("lore.label",">");
		noWater = plugin.getConfig().getInt("no-water",2);
		setupRealSurvivalAPI();
		CropManager.init(new File(plugin.getDataFolder()+"/crops"));
	}
	
	private static void setupRealSurvivalAPI(){
		if(Bukkit.getPluginManager().isPluginEnabled("RealSurvival")){
			rsAPI = (ReaLSurvivalAPI) Bukkit.getPluginManager().getPlugin("RealSurvival");
		}else{
			Bukkit.getLogger().warning("You need load RealSurvival first!");
			Bukkit.getPluginManager().disablePlugin(plugin);
		}
	}
	
	public static String getSeed(ItemStack item){
		if(item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore())return null;
		List<String> lore = item.getItemMeta().getLore();
		for(String line : lore){
			line = removeColor(line);
			if(line.contains(label)&&line.contains(split))return line.split(split)[1].replace(" ", "");
		}
		return null;
	}
	
	public static String getSplit(){
		return split;
	}
	
	public static String getLabel(){
		return label;
	}
	
	public static File getDataFolder(){
		return plugin.getDataFolder();
	}
	
	public static double random(double min,double max){
		if(min>max)
			return Math.random()*(min-max)+max;
		else
			return Math.random()*(max-min)+min;
	}
	
	public static String removeColor(String input){
		return input.replaceAll("&r", "").replaceAll("&o", "").replaceAll("&n", "").replaceAll("&m", "").replaceAll("&l", "").replaceAll("&k", "").replaceAll("&f", "")
				.replaceAll("&e", "").replaceAll("&d", "").replaceAll("&c", "").replaceAll("&b", "").replaceAll("&a", "").replaceAll("&9", "").replaceAll("&8", "").replaceAll("&7", "")
		        .replaceAll("&6", "").replaceAll("&5", "").replaceAll("&4", "").replaceAll("&3", "").replaceAll("&2", "").replaceAll("&1", "").replaceAll("&0", "").replaceAll("\247r", "")
		        .replaceAll("\247o", "").replaceAll("\247n", "").replaceAll("\247m", "").replaceAll("\247l", "").replaceAll("\247k", "").replaceAll("\247f", "").replaceAll("\247e", "")
		        .replaceAll("\247d", "").replaceAll("\247c", "").replaceAll("\247b", "").replaceAll("\247a", "").replaceAll("\2479", "").replaceAll("\2478", "").replaceAll("\2477", "")
		        .replaceAll("\2476", "").replaceAll("\2475", "").replaceAll("\2474", "").replaceAll("\2473", "").replaceAll("\2472", "").replaceAll("\2471", "").replaceAll("\2470", "");
	}
}
