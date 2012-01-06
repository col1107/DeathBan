package me.kanlaki101.DeathBan;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class DBUnban implements CommandExecutor {
	
	public static DeathBan plugin;
	public DBUnban(DeathBan instance) {
		plugin = instance;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (commandLabel.equalsIgnoreCase("dbunban")) {
			plugin.loadConfig();
			if (args.length == 0) {
				sender.sendMessage(ChatColor.YELLOW + "You must specify a player to unban!");
				return true;
			}
			if (args.length > 1) return true;
			
			if (args.length == 1) {
				if (plugin.banlist.contains("banned-players." + args[0])) { 
					if (sender instanceof ConsoleCommandSender) {
						plugin.log.info("Player: "+ args[0] +" has been unbanned.");
					}
					else {
						if (!plugin.isAuthorized(sender, "deathban.unban")) {
							sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
							return true;
						}
						sender.sendMessage(ChatColor.YELLOW + "Player: " + ChatColor.WHITE + args[0] + ChatColor.YELLOW + " has been unbanned.");
					}
					plugin.bannedPlayers.remove(args[0]);
					plugin.banlist.set("banned-players." + args[0], null);
					plugin.saveBanlist();
				}
				else {
					sender.sendMessage(ChatColor.YELLOW + "Player is not found.");
				}
			}	
		}
		return true;
	}
}
