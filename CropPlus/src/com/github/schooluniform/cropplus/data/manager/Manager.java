package com.github.schooluniform.cropplus.data.manager;

import com.github.miskyle.mcpt.bstat.Metrics;
import com.github.schooluniform.cropplus.CropPlus;
import java.io.File;
import java.util.List;
import miskyle.realsurvival.api.RealSurvivalApi;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class Manager {
  private static CropPlus plugin;
  private static String split;
  private static String label;
  public static int noWater;
  public static RealSurvivalApi rsAPI = null;
  public static int bukkitVersion;
  public static boolean enableRs;

  /**
   * 初始化.
   * @param plugin 本插件.
   */
  public static void init(CropPlus plugin) {
    bukkitVersion = Integer.parseInt(
        plugin.getServer().getBukkitVersion().split("-")[0].replace(".", ",").split(",")[1]);
    Manager.plugin = plugin;
    enableRs = plugin.getConfig().getBoolean("enable-realsurvival");
    split = plugin.getConfig().getString("lore.split", "Seed");
    label = plugin.getConfig().getString("lore.label", ">");
    noWater = plugin.getConfig().getInt("no-water", 2);
    if (enableRs) {
      setupRealSurvivalAPI();
    }
    setupRealSurvivalAPI();
    CropManager.init(new File(plugin.getDataFolder() + "/crops"));
    if (plugin.getConfig().getBoolean("enable-bStats", true)) {
      Metrics.setupMetrics(plugin, 3802);
    }
  }

  private static void setupRealSurvivalAPI() {
    if (Bukkit.getPluginManager().isPluginEnabled("RealSurvival")) {
      rsAPI = (RealSurvivalApi) Bukkit.getPluginManager().getPlugin("RealSurvival");
      plugin.getLogger().info("检测到RealSurvival载入, 季节功能启动!");
    } else {
      rsAPI = null;
      plugin.getLogger().info("未检测到RealSurvival载入, 季节功能停用!");
    }
  }

  /**
   * 获取种子信息.
   * @param item 物品
   * @return 若有效返回种子名反之则返回null
   */
  public static String getSeed(ItemStack item) {
    if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore()) {
      return null;      
    }
    List<String> lore = item.getItemMeta().getLore();
    for (String line : lore) {
      line = removeColor(line);
      if (line.contains(label) && line.contains(split)) {
        return line.split(split)[1].replace(" ", "");        
      }
    }
    return null;
  }

  public static String getSplit() {
    return split;
  }

  public static String getLabel() {
    return label;
  }

  public static File getDataFolder() {
    return plugin.getDataFolder();
  }

  public static double random(double min, double max) {
    return Math.random() * Math.abs(min - max) + Math.min(min, max);
  }

  /**
   * 除去字体颜色.
   * @param input 文本
   * @return 去除颜色的文本
   */
  public static String removeColor(String input) {
    return input.replaceAll("&r", "").replaceAll("&o", "").replaceAll("&n", "").replaceAll("&m", "")
        .replaceAll("&l", "").replaceAll("&k", "").replaceAll("&f", "")
        .replaceAll("&e", "").replaceAll("&d", "")
        .replaceAll("&c", "").replaceAll("&b", "").replaceAll("&a", "")
        .replaceAll("&9", "").replaceAll("&8", "")
        .replaceAll("&7", "").replaceAll("&6", "").replaceAll("&5", "")
        .replaceAll("&4", "").replaceAll("&3", "")
        .replaceAll("&2", "").replaceAll("&1", "").replaceAll("&0", "")
        .replaceAll("\247r", "").replaceAll("\247o", "")
        .replaceAll("\247n", "").replaceAll("\247m", "")
        .replaceAll("\247l", "").replaceAll("\247k", "")
        .replaceAll("\247f", "").replaceAll("\247e", "")
        .replaceAll("\247d", "").replaceAll("\247c", "")
        .replaceAll("\247b", "").replaceAll("\247a", "")
        .replaceAll("\2479", "").replaceAll("\2478", "")
        .replaceAll("\2477", "").replaceAll("\2476", "")
        .replaceAll("\2475", "").replaceAll("\2474", "")
        .replaceAll("\2473", "").replaceAll("\2472", "")
        .replaceAll("\2471", "").replaceAll("\2470", "");
  }
}
