package me.kanlaki101.DeathBan.commands;

import me.kanlaki101.DeathBan.DeathBan;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class DBReload implements CommandExecutor {
	
	public static DeathBan plugin;
	public DBReload(DeathBan instance) {
		plugin = instance;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (commandLabel.equalsIgnoreCase("dbreload")) {
			if (args.length > 0) return true;
			if (sender instanceof ConsoleCommandSender) { //If the command is sent from console
				plugin.log.info("[DeathBan] Config reloaded.");
			}
			else {
				if (!plugin.isAuthorized(sender, "deathban.reload")) { //If not send from console, but player doesn't have permission
					sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
					return true;
				}
				sender.sendMessage(ChatColor.YELLOW + "[DeathBan] Config reloaded.");
			}
			plugin.loadConfig(); //Reload the configuration file
		}
		return true;
	}
	
}
