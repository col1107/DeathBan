package me.kanlaki101.DeathBan;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathBan extends JavaPlugin {

	private final DBPlayerListener playerListener = new DBPlayerListener(this);
	private final DBEntityListener entityListener = new DBEntityListener(this);
	public final Logger log = Logger.getLogger("Minecraft");
    public Permission permission = null;
	public File configFile;
	public File banlistFile;
    public FileConfiguration config;
    public FileConfiguration banlist;
    ArrayList<String> bannedPlayers = new ArrayList<String>();
    Map<String,Long> Bans = new HashMap<String,Long>();
	
	public void onEnable() {
		configFile = new File(getDataFolder(), "config.yml");
		banlistFile = new File(getDataFolder(), "banlist.yml");
		setupConfig(); 
		setupBanlist(); //Create ban list
		config = new YamlConfiguration();
		banlist = new YamlConfiguration();
	    loadConfig();
	    loadBanlist(); //Load ban list
	    
	    setupPermissions();
	    
	    addBannedPlayersToArray();
	    
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_LOGIN, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener, Event.Priority.Normal, this);
		
		getCommand("dbreload").setExecutor(new DBReload(this));
		getCommand("dbunban").setExecutor(new DBUnban(this));
		
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
	
	public void setupConfig() {
	    
	    if(!configFile.exists()) { //If the file doesn't exist, create it
	    	configFile.getParentFile().mkdirs();
	        copy(getResource("config.yml"), configFile);
	    }
	}
	
	public void setupBanlist() {
	    
	    if(!banlistFile.exists()) { //If the file doesn't exist, create it
	    	banlistFile.getParentFile().mkdirs();
	        copy(getResource("banlist.yml"), banlistFile);
	    }
	}
	
	private void copy(InputStream in, File file) { //Copy information from existing files
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void saveConfig() { //Save ban list
	    try {
	    	config.save(configFile);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void loadConfig() { //Load ban list
	    try {
	    	config.load(configFile);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void saveBanlist() { //Save ban list
	    try {
	    	banlist.save(banlistFile);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void loadBanlist() { //Load ban list
	    try {
	    	banlist.load(banlistFile);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}	
	
	private void addBannedPlayersToArray() {
		ConfigurationSection groupSection = banlist.getConfigurationSection("banned-players"); //saves the section we are in for re-use
		 
		if (groupSection != null) {
			for (String players : groupSection.getKeys(false)) { //iterate over all keys
			    bannedPlayers.add(players);
			}
		}
	}
	
    public long getTimeInMillis() { //Used to see if a ban is expired
		Calendar calendar = Calendar.getInstance();
		long time = calendar.getTimeInMillis();

    	return time;
    }
    
    public String bannedUntil() {
    	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    	int bantime = config.getInt("deathban-time");
    	
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.MINUTE, bantime);
    	
		String time = dateFormat.format(calendar.getTime());
    	
    	return time;
    }
    
    public long banTimeInMillis() {
    	int bantime = config.getInt("deathban-time");
    	
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.MINUTE, bantime);
    	
    	long date = calendar.getTimeInMillis();
    	
    	return date;
    	
    }

}
