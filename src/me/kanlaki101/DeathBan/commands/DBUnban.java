package me.kanlaki101.DeathBan.commands;

import me.kanlaki101.DeathBan.DeathBan;

import org.bukkit.Bukkit;
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
			
			//If player does not have permission, or is no console
			if (!plugin.isAuthorized(sender, "deathban.unban") || (!(sender instanceof ConsoleCommandSender))) {
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
				return true;
			}
			
			if (args.length == 0) { //If they do not specify a player to unban
				sender.sendMessage(ChatColor.YELLOW + "You must specify a player to unban!");
				return true;
			}
			if (args.length > 1) return true; //If there are too many arguments.
			if (args.length == 1) {
				if (!plugin.banlist.contains("banned-players." + args[0])) { //If player is not banned
					sender.sendMessage(ChatColor.YELLOW + "Player is not found.");
					return true;
				}
				if (sender instanceof ConsoleCommandSender) { //If the command is sent from console
					plugin.log.info("Player: "+ args[0] +" has been unbanned."); //Log to console and send to in-game chat
					Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "Console has unbanned: " + ChatColor.WHITE + args[0]);
				}
				else { //If sent from in-game, sender has permission, and the player is banned.
					sender.sendMessage(ChatColor.YELLOW + "Player: " + ChatColor.WHITE + args[0] + ChatColor.YELLOW + " has been unbanned.");
				}
				
				plugin.bannedPlayers.remove(args[0]); //remove from the banlist
				plugin.banlist.set("banned-players." + args[0], null);
				plugin.saveBanlist();
			}	
		}
		return true;
	}
}
