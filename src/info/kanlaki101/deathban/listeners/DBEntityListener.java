package info.kanlaki101.deathban.listeners;

import info.kanlaki101.deathban.DeathBan;
import info.kanlaki101.deathban.utilities.DBConfigHandler;

import org.bukkit.ChatColor;
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
			DBConfigHandler.loadBanlist();
			DBConfigHandler.loadConfig();
			
			Player victim = (Player) e.getEntity();
			Entity killer = victim.getKiller();
			String name = victim.getName().toLowerCase();
			
			if (plugin.isAuthorized(victim, "deathban.exception")) return; //If the player is excluded from being banned

			if (DBConfigHandler.banForPVP() ==  true) { //If the server only wants to ban for PVP
				if (!(killer instanceof Player)) return;
			}
			
			if (DBConfigHandler.tpSpawnOnDeath() == true) { //If the server wants dead players to be teleported to spawn
				Location spawn = victim.getWorld().getSpawnLocation();
				victim.teleport(spawn); //Teleport them to the spawn location
			}
			
			if (DBConfigHandler.isUsingLivesSystem() == true) {
				DBConfigHandler.loadLives();
				int lives = DBConfigHandler.getLivesLeft(name);
				
				if (lives > 2) {
					int newLives = lives - 1;
					DBConfigHandler.lives.set("player-lives." + name, newLives);
					DBConfigHandler.saveLives();
					victim.sendMessage(ChatColor.YELLOW + "You have died! You have " + newLives + " lives left.");
					return;
				}
				if (lives == 2){
					DBConfigHandler.lives.set("player-lives." + name, 1);
					DBConfigHandler.saveLives();
					victim.sendMessage(ChatColor.YELLOW + "You have died! This is your last life.");
					return;
				}
			}
			
			victim.getInventory().clear(); //Clear their inventory
			
			DBConfigHandler.lives.set("player-lives." + name, null);
			DBConfigHandler.saveLives();
			
			String deathmessage = DBConfigHandler.getBroadcastMessage();
			if (deathmessage.contains("$p")) deathmessage = deathmessage.replace("$p", victim.getName());
			e.setDeathMessage(deathmessage); //Broadcast their death to all players
			plugin.log.info(deathmessage);
			
			String message = DBConfigHandler.getDeathBanMessage();
			if (message.contains("$t")) message = message.replace("$t", plugin.bannedUntil());
			victim.kickPlayer(message); //Kick the player from the server
			
			plugin.bannedPlayers.add(name);
			DBConfigHandler.banlist.set("banned-players." + name, plugin.banTimeInMillis());
			DBConfigHandler.saveBanlist(); //Add the player to the banlist.

		}
	}
}