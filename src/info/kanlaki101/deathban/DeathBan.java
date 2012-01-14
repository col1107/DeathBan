package info.kanlaki101.deathban;

import info.kanlaki101.deathban.commands.DBLives;
import info.kanlaki101.deathban.commands.DBReload;
import info.kanlaki101.deathban.commands.DBUnban;
import info.kanlaki101.deathban.listeners.DBEntityListener;
import info.kanlaki101.deathban.listeners.DBPlayerListener;
import info.kanlaki101.deathban.utilities.DBConfigHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathBan extends JavaPlugin {

	private final DBPlayerListener playerListener = new DBPlayerListener(this);
	private final DBEntityListener entityListener = new DBEntityListener(this);
	public final DBConfigHandler configHandler = new DBConfigHandler(this);
	public final Logger log = Logger.getLogger("Minecraft");
    public Permission permission = null;

    public ArrayList<String> bannedPlayers = new ArrayList<String>();
	
	public void onEnable() {
		configHandler.setupConfig(); 
		configHandler.setupBanlist();
		configHandler.setupLives();

	    setupPermissions(); //Find permissions systems via Vault
	    
	    addBannedPlayersToArray(); //Add everyone on the banlist.yml to the bannedPlayers array
	    
	    //Register events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_LOGIN, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
		
		//Register commands
		getCommand("dbreload").setExecutor(new DBReload(this));
		getCommand("dbunban").setExecutor(new DBUnban(this));
		getCommand("dblives").setExecutor(new DBLives(this));
		
		log.info("[DeathBan] Enabling...");
	}

	public void onDisable() {
		log.info("[DeathBan] Disabling...");
	}
	
    public Boolean setupPermissions() { //Check for permissions plugins (VAULT)
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
    
	public final boolean isAuthorized(CommandSender sender, String node) { //Check if player is an OP or has the correct permissions
		if (permission.has(sender, node) || sender.isOp()) return true;
		return false;
	}
    
	public final boolean isAuthorized(Player player, String node) { //Check if player is an OP or has the correct permissions
		if (permission.has(player, node) || player.isOp()) return true;
		return false;
	}
	
	private void addBannedPlayersToArray() {
		ConfigurationSection groupSection = DBConfigHandler.banlist.getConfigurationSection("banned-players"); //Saves the section we are in for re-use
		 
		if (groupSection != null) {
			for (String players : groupSection.getKeys(false)) { //Iterate over all keys
			    bannedPlayers.add(players);
			}
		}
	}
	
    public long getTimeInMillis() { //Gets the current time in milliseconds. Used to compare times
		Calendar calendar = Calendar.getInstance();
		long time = calendar.getTimeInMillis();

    	return time;
    }
    
    public String bannedUntil() { //Takes the current time, adds the length of the ban, and formats it
    	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    	int bantime = DBConfigHandler.getDeathBanTime();
    	
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.MINUTE, bantime);
    	
		String time = dateFormat.format(calendar.getTime());
    	
    	return time;
    }
    
    public long banTimeInMillis() { //Takes the current time, adds the length of the ban, all in milliseconds
    	int bantime = DBConfigHandler.getDeathBanTime();
    	
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.MINUTE, bantime);
    	
    	long date = calendar.getTimeInMillis();
    	
    	return date;
    	
    }

}
