DeathBan:
=
		Ever wanted to play Minecraft in hardcore mode? If so, you've come to the right place.
		This plugin is designed to ban users when they die. The time is configurable!
		The date they will be unbanned is displayed when they die, and when they try to join.

Features:
--
		* Hardcore Mode 
			Ban players when they die.
			
		* Ban for PVP only
			Ability to change whether players are banned only for being killed by a player
			
		* System based off of a set number of lives
			Set the number of lives a player will get when they join the server. 
			When they die, they lose lives, and when their lives hit 0, they are banned.
		
		* Configurable ban length
			Choose how long they are banned for. Time is in minutes.
	
		* Configurable death messages
			Change what messages are shown when a player dies in console, in-game, etc.

Commands:
--
		* dbreload:
			Description: Reload the configuration file
			Usage: /dbreload

		* dbunban:
			Description: Remove the specified player from the ban list.
			Usage: /dbunban <player>
		
		* dblives:
			Description: View/Edit a players amount of lives
			Usage: /dblives <player> - View a players lives remaining
				   /dblives <add:set> <player> <amount> - Add to, or set a players number of lives.
			
Permissions:
--
		* deathban.exception:
			Prevent the player from being banned off of the server upon death

		* deathban.reload:
			Gives the user the ability to reload the configuration file
			
		* deathban.unban:
			Gives the user the ability to unban players
		
		* deathban.editlives:
			Gives the user the ability to edit a players amount of lives
 
About:
--
	This plugin was made as a request. Here: http://forums.bukkit.org/threads/req-deathban-plugin.52734/
	System based off of lives was implemented by request. Here: http://forums.bukkit.org/threads/improved-deathban-will-pay.54479/