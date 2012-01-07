package me.kanlaki101.DeathBan;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
		if (plugin.bannedPlayers.contains(player)) {
			if (banIsExpired(player)) {
				plugin.bannedPlayers.remove(player);
				return;
			}
			
			String time = displayBannedUntilDate(player);
			String banmessage = plugin.config.getString("you-are-banned");
			String message = banmessage.replace("$t", time);
			
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, message);
		}
	}

	private boolean banIsExpired(String player) {
		long today = plugin.getTimeInMillis();
		long bandate = plugin.banTimeInMillis();
		if (today >= bandate) return true;
		return false;
	}
	
	
    public String displayBannedUntilDate(String player) {
    	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    	long time = plugin.banlist.getLong("banned-players." + player);
    	
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(time);
    	
    	String bantime = dateFormat.format(time);
    	
    	return bantime;
    }
}