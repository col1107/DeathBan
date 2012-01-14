package info.kanlaki101.deathban.listeners;

import info.kanlaki101.deathban.DeathBan;
import info.kanlaki101.deathban.utilities.DBConfigHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;

public class DBPlayerListener extends PlayerListener{

	public static DeathBan plugin;
	public DBPlayerListener(DeathBan instance) {
		plugin = instance;
	}
	
	public void onPlayerLogin(PlayerLoginEvent event) {
		DBConfigHandler.loadConfig();
		String player = event.getPlayer().getName().toLowerCase();
		if (plugin.bannedPlayers.contains(player)) { //Check if the player is banned.
			if (banIsExpired(player) == true) {
				plugin.bannedPlayers.remove(player); //remove from the banlist
				DBConfigHandler.banlist.set("banned-players." + player, null);
				DBConfigHandler.saveBanlist();
				return; //If the ban is expired, remove their ban, and allow them to join
			}
			
			String time = displayBannedUntilDate(player);
			String message = DBConfigHandler.getBannedMessage();
			if (message.contains("$t")) message = message.replace("$t", time);
			
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, message); //If still banned, kick them from the server
		}
	}
	
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		String pname = p.getName();
		String player = p.getName().toLowerCase();
		DBConfigHandler.loadLives();
		if (DBConfigHandler.isUsingLivesSystem() == true) {
			int lives = DBConfigHandler.getLivesLeft(player);
			if (DBConfigHandler.lives.contains("player-lives." + player)) {
				if (lives > 1) {
					p.sendMessage(ChatColor.YELLOW + "Welcome back, " + pname + "! You have " + lives + " lives left.");
				}
				else {
					p.sendMessage(ChatColor.YELLOW + "Welcome back, " + pname + "! You are on your last life. Be careful!.");
				}
			}
			else {
				DBConfigHandler.givePlayerLives(player);
				DBConfigHandler.saveLives();
				p.sendMessage(ChatColor.YELLOW + "Welcome " + pname + "! You have been given " + DBConfigHandler.getLivesAmount() + " lives. Be careful.");
			}
		}
	}

	private boolean banIsExpired(String player) { // Check to see if the ban is expired
		Long today = plugin.getTimeInMillis();
		long bandate = DBConfigHandler.banlist.getLong("banned-players." + player);
		int time = today.compareTo(bandate);
		if (time >= 0) return true;
		return false;
	}
	
    public String displayBannedUntilDate(String player) { //Takes the ban time in milliseconds from the banlist, and converts it to a readable format.
    	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    	long time = DBConfigHandler.banlist.getLong("banned-players." + player);
    	
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(time);
    	
    	String bantime = dateFormat.format(time);
    	return bantime;
    }
}