package me.jacklin213.cmdcharger;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

public class CommandListener implements Listener {
	CmdCharger plugin;
	
	public CommandListener(CmdCharger instance){
		plugin = instance;
	}
	
	@EventHandler
	public void onCommand(ServerCommandEvent event, CommandSender sender){
		String command = event.getCommand();
		String player = sender.getName();
		boolean cmd = plugin.getConfig().get("Prices.Commands") == command;
		int cmdprice = Integer.parseInt((String) plugin.getConfig().get("Prices.Commands." + command + "Price"));
		if(cmd){
			withdrawPlayer(player, cmdprice);
		}
		
	}
	
	EconomyResponse withdrawPlayer(String playerName, int cmdprice){
		return null;
		
	}
}
