package com.github.schooluniform.cropplus.data;

import com.github.schooluniform.cropplus.data.manager.Manager;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import miskyle.realsurvival.api.Season;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class CropData {

  private String id;

  private int light;
  private int multiple;
  private double needTime;
  private double growthRate;
  private double boneMealMin;
  private double boneMealMax;
  private double reback;
  private double breakDropSeedChance;

  // season
  private LinkedList<Season> season;
  private double seasonDeathChance;
  private double seasonGrowthRate;

  private Crop type;
  private ItemStack seed;
  private HashMap<ItemStack, DropChance> drops;

  private CropData(String id, int light, double needTime, double growthRate, double boneMealMin, 
      double boneMealMax, double reback, int multiple, Crop type, ItemStack seed, 
      HashMap<ItemStack, DropChance> drops, LinkedList<Season> season,
      double seasonDeathChance, double seasonGrowthRate, double breakDropSeedChance) {
    super();
    this.light = light;
    this.needTime = needTime;
    this.growthRate = growthRate;
    this.boneMealMin = boneMealMin;
    this.boneMealMax = boneMealMax;
    this.reback = reback;
    this.multiple = multiple;
    this.type = type;
    this.seed = seed;
    this.drops = drops;
    this.season = season;
    this.seasonDeathChance = seasonDeathChance;
    this.seasonGrowthRate = seasonGrowthRate;
    this.breakDropSeedChance = breakDropSeedChance;
  }

  /**
   * 从文件中获取作物信息.
   * @param name 文件名
   * @return
   */
  public static CropData load(String name) {
    File cropFile = new File(Manager.getDataFolder() + "/crops/" + name + ".yml");
    if (!cropFile.exists()) {
      return null;      
    }
    YamlConfiguration crop = YamlConfiguration.loadConfiguration(cropFile);
    ItemStack seed = crop.getItemStack("seed");
    HashMap<ItemStack, DropChance> drops = new HashMap<>();
    if (crop.getDouble("drops.drop-seed.chance", 0) > 0) {
      String temp = crop.getString("drops.drop-seed.amount");
      int min;
      int max;
      if (temp.contains("-")) {
        min = Integer.parseInt(temp.split("-")[0]);
        max = Integer.parseInt(temp.split("-")[1]);
      } else {
        min = max = Integer.parseInt(temp);        
      }
      drops.put(seed, new DropChance(crop.getDouble("drops.drop-seed.chance"), min, max));
    }
    int index = 1;
    while (crop.getDouble("drops.drop" + index + ".chance", 0) > 0) {
      double chance = crop.getDouble("drops.drop" + index + ".chance", 0);
      String temp = crop.getString("drops.drop" + index + ".amount");
      int min;
      int max;
      if (temp.contains("-")) {
        min = Integer.parseInt(temp.split("-")[0]);
        max = Integer.parseInt(temp.split("-")[1]);
      } else {
        min = max = Integer.parseInt(temp);        
      }
      ItemStack dropItem = crop.getItemStack("drops.drop" + index + ".item");
      drops.put(dropItem, new DropChance(chance, min, max));
      index++;
    }

    String temp = crop.getString("bone-meal-promote");
    double min;
    double max;
    if (temp.contains("-")) {
      min = Double.parseDouble(temp.split("-")[0]);
      max = Double.parseDouble(temp.split("-")[1]);
    } else {
      min = max = Double.parseDouble(temp);      
    }

    // season
    LinkedList<Season> seasons = new LinkedList<>();
    for (String season : crop.getStringList("season.season")) {
      try {
        seasons.add(Season.valueOf(season));
      } catch (Exception e) {
        Bukkit.getLogger().warning("[CropPlus]: Wrong Season in \"" + cropFile.getName() + "\"");
      }
    }

    return new CropData(name, crop.getInt("light"), 
        crop.getDouble("time"), 
        crop.getDouble("growth-rate"), 
        min, max, 
        crop.getDouble("reback"), 
        crop.getInt("multiple"), 
        Crop.valueOf(crop.getString("crop-type", "Wheat")), 
        seed, drops, seasons, 
        crop.getDouble("season.death-chance", 100), 
        crop.getDouble("season.growth-rate", 1),  
        crop.getDouble("break-drop-seed", 100));
  }

  /**
   * 获取掉落物.
   * @return
   */
  public LinkedList<ItemStack> getDrops() {
    LinkedList<ItemStack> drops = new LinkedList<>();

    for (Map.Entry<ItemStack, DropChance> entry : this.drops.entrySet()) {
      int amount = entry.getValue().getDrop();
      if (amount > 0) {
        ItemStack itemClone = entry.getKey().clone();
        itemClone.setAmount(amount);
        drops.add(itemClone);
      }
    }

    return drops;
  }

  public String getId() {
    return id;
  }

  public int getLight() {
    return light;
  }

  public double getNeedTime() {
    return needTime;
  }

  /**
   * 获取生长速度.
   * @param season 季节
   * @return
   */
  public double getGrowthRate(Season season) {
    if (season == null) {
      return growthRate;      
    } else if (this.season.contains(season)) {
      return growthRate;      
    } else {
      return seasonGrowthRate;
    }
  }

  public double getBoneMeal() {
    return Manager.random(boneMealMin, boneMealMax);
  }

  public double getReback() {
    return reback;
  }

  public int getMultiple() {
    return multiple;
  }

  public Crop getType() {
    return type;
  }

  public ItemStack getSeed() {
    return seed;
  }

  public double getSeasonDeathChance() {
    return seasonDeathChance;
  }

  public boolean inSeason(Season season) {
    return season == null ? null : this.season.contains(season);
  }

  public double getBreakDropSeedChance() {
    return breakDropSeedChance;
  }

}
