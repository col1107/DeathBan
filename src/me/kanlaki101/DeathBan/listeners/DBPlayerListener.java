package me.kanlaki101.DeathBan.listeners;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.kanlaki101.DeathBan.DeathBan;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;

public class DBPlayerListener extends PlayerListener{

	public static DeathBan plugin;
	public DBPlayerListener(DeathBan instance) {
		plugin = instance;
	}
	
	public void onPlayerLogin(PlayerLoginEvent event) {
		plugin.loadConfig();
		String player = event.getPlayer().getName();
		if (plugin.bannedPlayers.contains(player)) { //Check if the player is banned.
			if (banIsExpired(player) == true) {
				plugin.bannedPlayers.remove(player); //remove from the banlist
				plugin.banlist.set("banned-players." + player, null);
				plugin.saveBanlist();
				return; //If the ban is expired, remove their ban, and allow them to join
			}
			
			String time = displayBannedUntilDate(player);
			String message = plugin.config.getString("you-are-banned").replace("$t", time);
			
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, message); //If still banned, kick them from the server
		}
	}

	private boolean banIsExpired(String player) { // Check to see if the ban is expired
		Long today = plugin.getTimeInMillis();
		long bandate = plugin.banlist.getLong("banned-players." + player);
		int time = today.compareTo(bandate);
		if (time >= 0) return true;
		return false;
	}
	
    public String displayBannedUntilDate(String player) { //Takes the ban time in milliseconds from the banlist, and converts it to a readable format.
    	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    	long time = plugin.banlist.getLong("banned-players." + player);
    	
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(time);
    	
    	String bantime = dateFormat.format(time);
    	return bantime;
    }
}