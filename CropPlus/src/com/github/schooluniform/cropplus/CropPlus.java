package com.github.schooluniform.cropplus;

import com.github.schooluniform.cropplus.data.manager.CropManager;
import com.github.schooluniform.cropplus.data.manager.Manager;
import com.github.schooluniform.cropplus.event.CropEvents;
import java.io.File;
import java.io.IOException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CropPlus extends JavaPlugin {
  private int id;
  public static CropPlus plugin;

  @Override
  public void onEnable() {
    plugin = this;
    firstLoad();
    Manager.init(this);
    getServer().getPluginManager().registerEvents(new CropEvents(), this);
    id = getServer().getScheduler().runTaskTimerAsynchronously(
        this, new CropManager(), 0, getConfig().getInt("period", 1) * 20L).getTaskId();
  }

  @Override
  public void onDisable() {
    CropManager.save();
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (label.equalsIgnoreCase("crop") && sender.isOp()) {
      if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
        CropManager.save();
        firstLoad();
        Manager.init(this);
        getServer().getScheduler().cancelTask(id);
        id = getServer().getScheduler().runTaskTimer(
            this, new CropManager(), 0, getConfig().getInt("period") * 20L).getTaskId();
        sender.sendMessage("Successed!");
      }
    }
    return true;
  }

  private void firstLoad() {
    if (!getDataFolder().exists()) {
      getDataFolder().mkdir();      
    }
    if (!new File(getDataFolder() + File.separator + "crops").exists()) {
      new File(getDataFolder() + File.separator + "crops").mkdir();      
    }
    if (!new File(getDataFolder() + File.separator + "config.yml").exists()) {
      saveDefaultConfig();
      try {
        reloadConfig();
      } catch (Exception e) {
        e.printStackTrace();
      }      
    }

    if (!new File(getDataFolder() + File.separator + "timer.yml").exists()) {
      try {
        new File(getDataFolder() + File.separator + "timer.yml").createNewFile();
      } catch (IOException e1) {
        e1.printStackTrace();
      }      
    }
  }
}
