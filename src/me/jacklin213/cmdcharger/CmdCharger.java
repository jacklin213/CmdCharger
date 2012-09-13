package me.jacklin213.cmdcharger;

import java.io.File;
import java.util.logging.Logger;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class CmdCharger extends JavaPlugin {

	public static CmdCharger plugin;
	PluginDescriptionFile pdfFile;
	private static final Logger log = Logger.getLogger("Minecraft");
	public static Economy econ = null;
	public static Permission perms = null;
	public static Chat chat = null;
	public CommandListener cl = new CommandListener(this);

	@Override
	public void onDisable() {
		log.info(String.format("[%s] Disabled Version %s", getDescription()
				.getName(), getDescription().getVersion()));
	}

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		if (!setupEconomy()) {
			log.warning(String.format(
					"[%s] - Disabled due to no Vault dependency found!",
					getDescription().getName()));
			log.info(String
					.format("[%s] - Download a chat , permissions , economy and vault before using this plugin!",
							getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		setupPermissions();
		setupChat();
		createconfig();
		pm.registerEvents(this.cl,this);
		log.info(String.format("[%s] Enabled Version %s", getDescription()
				.getName(), getDescription().getVersion()));
	}

	public void createconfig() {
		// Creates config.yml
		File configfile = new File(getDataFolder() + File.separator
				+ "config.yml");
		// If config.yml doesnt exit
		if (!configfile.exists()) {
			// Tells console its creating a config.yml
			this.getLogger().info("You don't have a config file!!!");
			this.getLogger().info("Generating config.yml.....");
			this.getConfig().options().copyDefaults(true);
			this.saveDefaultConfig();
		}

	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (commandLabel.equalsIgnoreCase("cmdcharge")
				|| commandLabel.equalsIgnoreCase("cc")) {
			if (args.length >= 3) {
				if (args[0].equalsIgnoreCase("testprice")) {
					String command = args[1];
					this.getConfig().set("Prices.Commands." + command, command);
					this.saveConfig();
					try {
						int price = Integer.parseInt(args[2]);
						this.getConfig().set(
								"Prices.Commands." + command + ".Price", price);
						this.saveConfig();
						sender.sendMessage("The price for " + command
								+ " has been set to:" + price);
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED
								+ "This is not a number! Try typing a number in");
					}
					return true;
				} else {
					sender.sendMessage("testfailed");
				}
			} else if (args.length <= 1) {
				if (args[0].equalsIgnoreCase("help")) {
					try {
						int page = Integer.parseInt(args[0]);

						if (page == 1) {
							sender.sendMessage(ChatColor.YELLOW
									+ " ------------ " + ChatColor.WHITE
									+ "Help: mcRP Skills (Page 1)"
									+ ChatColor.YELLOW + " ------------");
							sender.sendMessage(ChatColor.GOLD + "/cmdcharge"
									+ ChatColor.GRAY + " - " + ChatColor.WHITE
									+ "Shows info");
							sender.sendMessage(ChatColor.GOLD
									+ "/cmdcharge setprice [command] [price]"
									+ ChatColor.GRAY + " - " + ChatColor.WHITE
									+ "Set price for command!");

						} else {
							sender.sendMessage(ChatColor.RED
									+ " Invalid page number specified. There is only 1 page right now.");
						}
					} catch (NumberFormatException nfe) {
						sender.sendMessage(ChatColor.RED
								+ " Invalid page number specified. There is only 1 page right now.");
					}
				}

			}

			return info(sender);
		}
		return false;
	}

	public boolean info(CommandSender sender) {
		sender.sendMessage(ChatColor.DARK_GREEN
				+ "+------------------------------+");
		sender.sendMessage(ChatColor.DARK_AQUA
				+ "      CmdCharger: Charge people for using commands!");
		sender.sendMessage(ChatColor.GREEN + "      By jacklin213");
		sender.sendMessage(ChatColor.YELLOW + "      Version: "
				+ this.pdfFile.getVersion());
		sender.sendMessage(ChatColor.DARK_GREEN
				+ "+------------------------------+");
		return true;
	}

	// Vault api import crap
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer()
				.getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	private boolean setupChat() {
		RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager()
				.getRegistration(Chat.class);
		chat = rsp.getProvider();
		return chat != null;
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = getServer()
				.getServicesManager().getRegistration(Permission.class);
		perms = rsp.getProvider();
		return perms != null;
	}

}
