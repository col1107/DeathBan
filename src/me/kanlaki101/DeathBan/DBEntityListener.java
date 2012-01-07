package me.kanlaki101.DeathBan;

import org.bukkit.Location;
import org.bukkit.World;
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
        	plugin.loadConfig();
            
            Player p = (Player) event.getEntity();
            String player = p.getName();
            if (!plugin.isAuthorized(p, "deathban.exception")) {
	            plugin.bannedPlayers.add(player);
	            
	            plugin.banlist.set("banned-players." + player, plugin.banTimeInMillis());
	            plugin.saveBanlist();
	            
				String banmessage = plugin.config.getString("death-message");
				String lengthmessage = plugin.bannedUntil();
				String message = banmessage.replace("$t", lengthmessage);
				
				if (plugin.config.getBoolean("tp-to-spawn-on-death") == true) {
					World world = p.getWorld();
					Location spawn = world.getSpawnLocation();
					p.teleport(spawn);
				}
				p.getInventory().clear();
	            p.kickPlayer(message);
	        	
	            PlayerDeathEvent e = (PlayerDeathEvent) event;
	            String dmessage = plugin.config.getString("death-message-broadcast");
	            String deathmessage = dmessage.replace("$p", player);
	            
	            e.setDeathMessage(deathmessage);
	            plugin.log.info(deathmessage);
            }
            
        }
    }
}