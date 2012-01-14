package info.kanlaki101.deathban.utilities;

import info.kanlaki101.deathban.DeathBan;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DBConfigHandler {
	
    private static DeathBan plugin;

    public DBConfigHandler(DeathBan plugin) {
        DBConfigHandler.plugin = plugin;
    }
    
    public static File configFile;
    public static File banlistFile;
    public static File livesFile;
    public static FileConfiguration config;
    public static FileConfiguration banlist;
    public static FileConfiguration lives;
    

	public void setupConfig() {
		
		configFile = new File(plugin.getDataFolder(), "config.yml");
	    config = new YamlConfiguration();
	    
	    if(!configFile.exists()) {
	    	configFile.getParentFile().mkdirs();
	        copy(plugin.getResource("config.yml"), configFile);
	    }
	}
	
	public void setupBanlist() {
		
		banlistFile = new File(plugin.getDataFolder(), "banlist.yml");
	    banlist = new YamlConfiguration();
		
		if(!banlistFile.exists()) {
			banlistFile.getParentFile().mkdirs();
			copy(plugin.getResource("banlist.yml"), banlistFile);
		}
	}
	
	public void setupLives() {
		
		livesFile = new File(plugin.getDataFolder(), "lives.yml");
		lives = new YamlConfiguration();
		
		if(!livesFile.exists()) {
			livesFile.getParentFile().mkdirs();
			copy(plugin.getResource("lives.yml"), livesFile);
		}
	}
	
	private void copy(InputStream in, File file) {
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
	
	public static void saveConfig() { //Save configuration file
	    try {
	        config.save(configFile);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void saveBanlist() { //Save friends list
	    try {
	    	banlist.save(banlistFile);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void saveLives() { //Save lives file
	    try {
	    	lives.save(livesFile);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void loadConfig() { //Load configuration file
	    try {
	        config.load(configFile);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static void loadBanlist() { //Load friends list
	    try {
	    	banlist.load(banlistFile);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static void loadLives() { //Load lives file
	    try {
	        lives.load(livesFile);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static int getDeathBanTime() {
		return config.getInt("deathban-time");
	}
	
	public static String getDeathBanMessage() {
		return config.getString("death-message");
	}
	
	public static String getBannedMessage() {
		return config.getString("you-are-banned");
	}
	
	public static String getBroadcastMessage() {
		return config.getString("death-message-broadcast");
	}

	public static boolean banForPVP() {
		return config.getBoolean("ban-for-pvp-only");
	}
	
	public static boolean tpSpawnOnDeath() {
		return config.getBoolean("tp-to-spawn-on-death");
	}
	
	public static int getLivesAmount() {
		return config.getInt("lives-allowed");
	}

	public static int getLivesLeft(String player) {
		return lives.getInt("player-lives." + player);
	}

	public static void givePlayerLives(String player) {
		lives.set("player-lives." + player, getLivesAmount());
		return;
	}

	public static boolean isUsingLivesSystem() {
		return config.getBoolean("use-lives-system");
	}
		
}
