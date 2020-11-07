package com.github.schooluniform.cropplus.api.event.player;

import java.util.LinkedList;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Triggered when harvesting crops.
 * 
 * <p>\u6536\u83b7\u4f5c\u7269\u65f6\u89e6\u53d1
 * 
 * @author School_Uniform
 *
 */
public class CPPlayerHarvestingCropEvent extends CropPlusPlayerEvent {
  private LinkedList<ItemStack> drops;

  public CPPlayerHarvestingCropEvent(
      Player player, String cropId, LinkedList<ItemStack> drops, Location location) {
    super(player, cropId, location);
    this.drops = drops;
  }

  /**
   * Get drop items.
   * 
   * <p>\u83b7\u53d6\u6389\u843d\u7269
   * 
   * @return A LinkedList of drop items. \u5305\u542b\u6389\u843d\u7269\u7684LinkedList
   */
  public LinkedList<ItemStack> getDrops() {
    return drops;
  }

  /**
   * Invalid for this.
   * 
   * <p>\u5bf9\u6b64\u65e0\u6548
   */
  @Override
  public void setCropId(String cropId) {
    super.setCropId(cropId);
  }
}
