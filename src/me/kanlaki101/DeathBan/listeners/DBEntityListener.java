package me.kanlaki101.DeathBan.listeners;

import me.kanlaki101.DeathBan.DeathBan;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
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
			PlayerDeathEvent e = (PlayerDeathEvent) event;
			plugin.loadBanlist();
			plugin.loadConfig();
			
			Player victim = (Player) e.getEntity();
			Entity killer = victim.getKiller();
			String name = victim.getName();
			
			if (plugin.isAuthorized(victim, "deathban.exception")) return; //If the player is excluded from being banned

			if (plugin.config.getBoolean("ban-for-pvp-ban-only") ==  true) { //If the server only wants to ban for PVP
				if (!(killer instanceof Player)) return;
			}
			
			if (plugin.config.getBoolean("tp-to-spawn-on-death") == true) { //If the server wants dead players to be teleported to spawn
				Location spawn = victim.getWorld().getSpawnLocation();
				victim.teleport(spawn); //Teleport them to the spawn location
			}
			
			victim.getInventory().clear(); //Clear their inventory
			
			String deathmessage = plugin.config.getString("death-message-broadcast").replace("$p", name);
			e.setDeathMessage(deathmessage); //Broadcast their death to all players
			plugin.log.info(deathmessage);
			
			String message = plugin.config.getString("death-message").replace("$t", plugin.bannedUntil());
			victim.kickPlayer(message); //Kick the player from the server
			
			plugin.bannedPlayers.add(name);
			plugin.banlist.set("banned-players." + name, plugin.banTimeInMillis());
			plugin.saveBanlist(); //Add the player to the banlist.

		}
	}
}