package com.kokicraft.SqliteWhitelist;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class WorldEvent implements Listener {

	public Main plugin;

	public WorldEvent(Main instance) {
		plugin = instance;
	}
  
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void PlayerLoginEvent(PlayerLoginEvent event) {
    if (event.getPlayer().isOp()) {
        return;
    }

    UUID uuid = event.getPlayer().getUniqueId();
    String name = event.getPlayer().getName();

    try {
      DBConnect.Query q = Main.db.queryDB("SELECT uuid FROM intro2mc_student WHERE IGN = ?;", name);
      if (!q.next() || q.getString("uuid") != uuid.toString()) {
        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, plugin.getConfig().getString("Whitelist.KickMessage"));
      }
      q.close();
    } catch (SQLException e) {
      plugin.getLogger().severe("[SqliteWhitelist] Error while checking whitelist: " + e.getMessage());
      event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "[SqliteWhitelist] Error while checking whitelist: " + e.getMessage());
    }
    return;
	}
}
