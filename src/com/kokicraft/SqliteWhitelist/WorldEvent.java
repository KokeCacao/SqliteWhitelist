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
    if (Main.db == null) {
      plugin.getLogger().severe("[SqliteWhitelist] Database not connected. Please check your config.yml.");
      return;
    }

    UUID uuid = event.getPlayer().getUniqueId();
    String name = event.getPlayer().getName();

    try {
      boolean authorized = true;

      if (plugin.getConfig().getBoolean("Whitelist.CheckUUID")) {
        boolean uuidAuthorized = false;
        DBConnect.Query q = Main.db.queryDB("SELECT * FROM intro2mc_student WHERE uuid = ?;", uuid.toString());
        uuidAuthorized ||= q.next();
        q.close();

        DBConnect.Query qinvited = Main.db.queryDB("SELECT * FROM intro2mc_invitedstudent WHERE uuid = ?;", uuid.toString());
        uuidAuthorized ||= qinvited.next();
        qinvited.close();
        
        authorized &&= uuidAuthorized;
      }

      if (plugin.getConfig().getBoolean("Whitelist.CheckName")) {
        boolean nameAuthorized = false;
        DBConnect.Query q = Main.db.queryDB("SELECT * FROM intro2mc_student WHERE IGN = ?;", name);
        nameAuthorized ||= q.next();
        q.close();

        DBConnect.Query qinvited = Main.db.queryDB("SELECT * FROM intro2mc_invitedstudent WHERE IGN = ?;", name);
        nameAuthorized ||= qinvited.next();
        qinvited.close();
        
        authorized &&= nameAuthorized;
      }

      if (!authorized) {
        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, plugin.getConfig().getString("Whitelist.KickMessageName"));
        return;
      }

    } catch (SQLException e) {
      plugin.getLogger().severe("Error while checking whitelist (plugin will disable): " + e.getMessage());
      return;
    }
    
  }
}
