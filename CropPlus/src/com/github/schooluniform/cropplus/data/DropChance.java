package com.github.schooluniform.cropplus.data;

import com.github.schooluniform.cropplus.data.manager.Manager;

public class DropChance {
  DropChance(double chance, int min, int max) {
    this.chance = chance;
    minAmount = min;
    maxAmount = max;
  }

  private double chance;
  private int minAmount;
  private int maxAmount;

  /**
   * 获取掉落数量.
   * @return
   */
  public int getDrop() {
    if (Manager.random(0, 100) < chance) {
      if (minAmount == maxAmount) {
        return minAmount;        
      } else {
        return (int) Manager.random(minAmount, maxAmount + 1);        
      }
    } else {
      return 0;      
    }
  }
}
