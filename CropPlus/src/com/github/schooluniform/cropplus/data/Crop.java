package com.github.schooluniform.cropplus.data;

import com.github.schooluniform.cropplus.data.manager.Manager;
import org.bukkit.Material;

public enum Crop {
  Wheat("CROPS", "WHEAT"), 
  Carrot("CARROT", "CARROTS"), 
  Potato("POTATO", "POTATOES"),
  Beetroot("BEETROOT_BLOCK", "BEETROOTS");

  private String oldType;
  private String type;

  Crop(String oldType, String type) {
    this.oldType = oldType;
    this.type = type;
  }

  /**
   * 获取作物类型.
   * @return 返回适宜当前版本的作物类型.
   */
  public Material getType() {
    if (Manager.bukkitVersion >= 13) {
      return Material.valueOf(type);
    } else {
      return Material.valueOf(oldType);
    }
  }
}
