package com.github.schooluniform.cropplus.api.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CPPlayerPlantingCropEvent extends CropPlusPlayerEvent {
  private Location clickLocation;
  private ItemStack item;

  public CPPlayerPlantingCropEvent(Player player, String cropId, Location location, Location clickLocation,
      ItemStack item) {
    super(player, cropId, location);
    this.clickLocation = clickLocation;
    this.item = item;
  }

  /**
   * Get the location player click.
   * <p>
   * \u83b7\u53d6\u73a9\u5bb6\u70b9\u51fb\u7684\u4f4d\u7f6e
   * 
   * @return A Location player click.
   *         <p>
   *         \u73a9\u5bb6\u70b9\u51fb\u7684\u4f4d\u7f6e
   */
  public Location getClickLocation() {
    return clickLocation;
  }

  /**
   * Get the item player want plant crop.
   * <p>
   * \u83b7\u53d6\u73a9\u5bb6\u60f3\u8981\u79cd\u690d\u7684\u4f5c\u7269
   * 
   * @return the Item player using
   */
  public ItemStack getItem() {
    return item;
  }
}
