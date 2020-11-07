package com.github.schooluniform.cropplus.api.event;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CropPlusEvent extends Event {
  private static final HandlerList handlers = new HandlerList();
  private String cropId;
  private Location location;
  private boolean cancelled;

  protected CropPlusEvent(String cropId, Location location) {
    this.cropId = cropId;
    this.location = location;
  }

  public boolean isCancelled() {
    return cancelled;
  }

  /**
   * Cancel the event.
   * 
   * @param cancel 取消
   */
  public void setCancelled(boolean cancel) {
    cancelled = cancel;
  }

  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  /**
   * Get the Crop's ID.
   * 
   * <p>\u83b7\u53d6\u8fd9\u4e2a\u4f5c\u7269\u7684ID
   * 
   * @return The Crop's ID \u8fd9\u4e2a\u4f5c\u7269\u7684ID
   */
  public String getCropId() {
    return cropId;
  }

  /**
   * Set CropID.
   * 
   * @param cropId 作物id
   */
  public void setCropId(String cropId) {
    this.cropId = cropId;
  }

  /**
   * Get the crop's location.
   * 
   *  <p>\u83b7\u53d6\u4f5c\u7269\u7684\u4f4d\u7f6e
   * 
   * @return The Location of Crop. \u4f5c\u7269\u7684\u4f4d\u7f6e
   */
  public Location getCropLocation() {
    return location;
  }
}
