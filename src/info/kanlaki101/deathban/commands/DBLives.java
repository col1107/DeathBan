package info.kanlaki101.deathban.commands;

import info.kanlaki101.deathban.DeathBan;
import info.kanlaki101.deathban.utilities.DBConfigHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DBLives implements CommandExecutor {
	
	public static DeathBan plugin;
	public DBLives(DeathBan instance) {
		plugin = instance;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("dblives")) {
			String cname = cmd.getName();
			String name = sender.getName().toLowerCase();
			DBConfigHandler.loadLives();
			if (args.length == 0) {
				if (!(sender instanceof Player)) {
					plugin.log.info("Usage: /" + cmd.getName() + " <player> - Checks how many lives a player has left.");
					plugin.log.info("Usage: /" + cmd.getName() + " <add:set> <player> <amount> - Add lives to a player, or set their total amount of lives.");
					return true;
				}
				sender.sendMessage(ChatColor.YELLOW + "You have " + DBConfigHandler.getLivesLeft(name) + " lives left.");
				sender.sendMessage(ChatColor.YELLOW + "Use: /" + cname + " <player> to check another players lives.");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("add")) {
				if (!plugin.isAuthorized(sender, "deathban.editlives")) {
					sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
					return true;
				}
				if (args.length > 3) return true;
				if (args.length < 3) {
					sender.sendMessage(ChatColor.YELLOW + "Usage: /" + cname + " add <player> <amount>");
					return true;
				}
				
				if (!DBConfigHandler.lives.contains("player-lives." + args[1].toLowerCase())) {
					sender.sendMessage(ChatColor.YELLOW + "The lives.yml file does not contain player.");
					sender.sendMessage(ChatColor.YELLOW + "Use: /" + cname + " set <player> <amount>");
					return true;
				}
				else {
					if (checkIfInt(args[2]) == true) {
						int currLives = DBConfigHandler.getLivesLeft(args[1].toLowerCase());
						int addAmount = Integer.parseInt(args[2]);
						int newLives = currLives + addAmount;
						
						DBConfigHandler.lives.set("player-lives." + args[1].toLowerCase(), newLives);
						DBConfigHandler.saveLives();
						sender.sendMessage(ChatColor.YELLOW + "Player: " + args[1] + " now has " + newLives + " lives left.");
						
						Player p = Bukkit.getServer().getPlayer(args[1]);
						if (p != null) {
							p.sendMessage(ChatColor.YELLOW + "You have been given " + addAmount + " lives. Total: " + newLives);
						}
						return true;
					}
					else {
						sender.sendMessage(ChatColor.YELLOW + "Amount must be an number");
						return true;
					}
				}
			}
			
			else if (args[0].equalsIgnoreCase("set")) {
				if (!plugin.isAuthorized(sender, "deathban.editlives")) {
					sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
					return true;
				}
				if (args.length > 3) return true;
				if (args.length < 3) {
					sender.sendMessage(ChatColor.YELLOW + "Usage: /" + cname + " set <player> <amount>");
					return true;
				}
				if (checkIfInt(args[2]) == true) {
					int setAmount = Integer.parseInt(args[2]);
					
					if (setAmount <= 0) {
						sender.sendMessage(ChatColor.YELLOW + "Amount must be above 0");
					}
					
					DBConfigHandler.lives.set("player-lives." + args[1].toLowerCase(), setAmount);
					DBConfigHandler.saveLives();
					sender.sendMessage(ChatColor.YELLOW + "Player: " + args[1] + " now has " + setAmount + " lives left.");
					
					Player p = Bukkit.getServer().getPlayer(args[1]);
					if (p != null) {
						p.sendMessage(ChatColor.YELLOW + "Your lives have been set to: " + setAmount);
					}
					return true;
				}
				else {
					sender.sendMessage(ChatColor.YELLOW + "Amount must be an number");
					return true;
				}
			}
			else {
				if (DBConfigHandler.lives.contains("player-lives." + args[0].toLowerCase())) {
					DBConfigHandler.loadLives();
					sender.sendMessage(ChatColor.YELLOW + "Player: " + args[0] + " has " + DBConfigHandler.getLivesLeft(args[0].toLowerCase()) + " lives left.");
					return true;
				}
				else {
					sender.sendMessage(ChatColor.YELLOW + "Player: " + args[0] + " not found.");
					return true;
				}
			}

		}
		return false;
	}
	
    public boolean checkIfInt(String in) {
        try {
            Integer.parseInt(in);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
	
}
