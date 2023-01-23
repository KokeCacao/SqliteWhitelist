package com.kokicraft.SqliteWhitelist;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

  public static Main plugin;
  public static DBConnect db;
  public Logger log = Logger.getLogger("Minecraft");

  public void onEnable() {
    plugin = this;
    connectDB();
    registerEvent();
    this.getConfig();
    this.saveDefaultConfig();
    registerCommand();
    log.info("[SqliteWhitelist] Version " + this.getDescription().getVersion() + " has been enabled.");
  }

  public void connectDB() {
    try {
      db = new DBConnect(getConfig().getString("Sqlite.path"));
    } catch (Exception e) {
      this.getLogger().severe("[SqliteWhitelist] Failed to connect to database.");
    }
  }

  public void registerEvent() {
    getServer().getPluginManager().registerEvents(new WorldEvent(this), this);
  }

  public void registerCommand() {
  }

  public void onDisable() {
    log.info("[SqliteWhitelist] SqliteWhitelist has been disabled.");
  }
}
