package me.kanlaki101.DeathBan;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DBEntityListener extends EntityListener {
	
	public static DeathBan plugin;
	public DBEntityListener(DeathBan instance) {
		plugin = instance;
	}
	
    public void onEntityDeath(EntityDeathEvent event) {
        if (event instanceof PlayerDeathEvent) {
        	plugin.loadBanlist();
            
            Player p = (Player) event.getEntity();
            String player = p.getName();
            if (!plugin.isAuthorized(p, "deathban.exception")) {
	            plugin.bannedPlayers.add(player);
	            
	            plugin.banlist.set("banned-players." + player, plugin.banTimeInMillis());
	            plugin.saveBanlist();
	            
				String banmessage = plugin.config.getString("death-message");
				String lengthmessage = " Ban will expire: " + plugin.bannedUntil();
				String message = banmessage + lengthmessage;
	            p.kickPlayer(message);
	        	
	            PlayerDeathEvent e = (PlayerDeathEvent) event;
	            String deathmessage = player + " " + plugin.config.getString("death-message-broadcast");
	            
	            e.setDeathMessage(deathmessage);
	            plugin.log.info(deathmessage);
            }
            
        }
    }
}