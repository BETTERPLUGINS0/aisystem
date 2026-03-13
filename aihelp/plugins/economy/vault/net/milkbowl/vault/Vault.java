package net.milkbowl.vault;

import com.nijikokun.register.payment.Methods;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.logging.Logger;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.chat.plugins.Chat_DroxPerms;
import net.milkbowl.vault.chat.plugins.Chat_GroupManager;
import net.milkbowl.vault.chat.plugins.Chat_OverPermissions;
import net.milkbowl.vault.chat.plugins.Chat_Permissions3;
import net.milkbowl.vault.chat.plugins.Chat_PermissionsEx;
import net.milkbowl.vault.chat.plugins.Chat_Privileges;
import net.milkbowl.vault.chat.plugins.Chat_TotalPermissions;
import net.milkbowl.vault.chat.plugins.Chat_bPermissions;
import net.milkbowl.vault.chat.plugins.Chat_bPermissions2;
import net.milkbowl.vault.chat.plugins.Chat_iChat;
import net.milkbowl.vault.chat.plugins.Chat_mChat;
import net.milkbowl.vault.chat.plugins.Chat_mChatSuite;
import net.milkbowl.vault.chat.plugins.Chat_rscPermissions;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.plugins.Economy_BOSE7;
import net.milkbowl.vault.economy.plugins.Economy_CommandsEX;
import net.milkbowl.vault.economy.plugins.Economy_Craftconomy3;
import net.milkbowl.vault.economy.plugins.Economy_CurrencyCore;
import net.milkbowl.vault.economy.plugins.Economy_DigiCoin;
import net.milkbowl.vault.economy.plugins.Economy_Dosh;
import net.milkbowl.vault.economy.plugins.Economy_EconXP;
import net.milkbowl.vault.economy.plugins.Economy_Essentials;
import net.milkbowl.vault.economy.plugins.Economy_GoldIsMoney2;
import net.milkbowl.vault.economy.plugins.Economy_GoldenChestEconomy;
import net.milkbowl.vault.economy.plugins.Economy_Gringotts;
import net.milkbowl.vault.economy.plugins.Economy_McMoney;
import net.milkbowl.vault.economy.plugins.Economy_MiConomy;
import net.milkbowl.vault.economy.plugins.Economy_MineConomy;
import net.milkbowl.vault.economy.plugins.Economy_Minefaconomy;
import net.milkbowl.vault.economy.plugins.Economy_MultiCurrency;
import net.milkbowl.vault.economy.plugins.Economy_SDFEconomy;
import net.milkbowl.vault.economy.plugins.Economy_TAEcon;
import net.milkbowl.vault.economy.plugins.Economy_XPBank;
import net.milkbowl.vault.economy.plugins.Economy_eWallet;
import net.milkbowl.vault.economy.plugins.Economy_iConomy6;
import net.milkbowl.vault.metrics.bukkit.Metrics;
import net.milkbowl.vault.permission.Permission;
import net.milkbowl.vault.permission.plugins.Permission_DroxPerms;
import net.milkbowl.vault.permission.plugins.Permission_GroupManager;
import net.milkbowl.vault.permission.plugins.Permission_KPerms;
import net.milkbowl.vault.permission.plugins.Permission_OverPermissions;
import net.milkbowl.vault.permission.plugins.Permission_Permissions3;
import net.milkbowl.vault.permission.plugins.Permission_PermissionsBukkit;
import net.milkbowl.vault.permission.plugins.Permission_PermissionsEx;
import net.milkbowl.vault.permission.plugins.Permission_Privileges;
import net.milkbowl.vault.permission.plugins.Permission_SimplyPerms;
import net.milkbowl.vault.permission.plugins.Permission_Starburst;
import net.milkbowl.vault.permission.plugins.Permission_SuperPerms;
import net.milkbowl.vault.permission.plugins.Permission_TotalPermissions;
import net.milkbowl.vault.permission.plugins.Permission_Xperms;
import net.milkbowl.vault.permission.plugins.Permission_bPermissions;
import net.milkbowl.vault.permission.plugins.Permission_bPermissions2;
import net.milkbowl.vault.permission.plugins.Permission_rscPermissions;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Vault extends JavaPlugin {
   private static final String VAULT_BUKKIT_URL = "https://dev.bukkit.org/projects/Vault";
   private static Logger log;
   private Permission perms;
   private String newVersionTitle = "";
   private double newVersion = 0.0D;
   private double currentVersion = 0.0D;
   private String currentVersionTitle = "";
   private ServicesManager sm;
   private Vault plugin;

   public void onDisable() {
      this.getServer().getServicesManager().unregisterAll(this);
      Bukkit.getScheduler().cancelTasks(this);
   }

   public void onEnable() {
      this.plugin = this;
      log = this.getLogger();
      this.currentVersionTitle = this.getDescription().getVersion().split("-")[0];
      this.currentVersion = Double.valueOf(this.currentVersionTitle.replaceFirst("\\.", ""));
      this.sm = this.getServer().getServicesManager();
      this.getConfig().addDefault("update-check", true);
      this.getConfig().options().copyDefaults(true);
      this.saveConfig();
      this.loadEconomy();
      this.loadPermission();
      this.loadChat();
      this.getCommand("vault-info").setExecutor(this);
      this.getCommand("vault-convert").setExecutor(this);
      this.getServer().getPluginManager().registerEvents(new Vault.VaultListener(), this);
      this.getServer().getScheduler().runTask(this, new Runnable() {
         public void run() {
            org.bukkit.permissions.Permission perm = Vault.this.getServer().getPluginManager().getPermission("vault.update");
            if (perm == null) {
               perm = new org.bukkit.permissions.Permission("vault.update");
               perm.setDefault(PermissionDefault.OP);
               Vault.this.plugin.getServer().getPluginManager().addPermission(perm);
            }

            perm.setDescription("Allows a user or the console to check for vault updates");
            Vault.this.getServer().getScheduler().runTaskTimerAsynchronously(Vault.this.plugin, new Runnable() {
               public void run() {
                  if (Vault.this.getServer().getConsoleSender().hasPermission("vault.update") && Vault.this.getConfig().getBoolean("update-check", true)) {
                     try {
                        Vault.log.info("Checking for Updates ... ");
                        Vault.this.newVersion = Vault.this.updateCheck(Vault.this.currentVersion);
                        if (Vault.this.newVersion > Vault.this.currentVersion) {
                           Vault.log.warning("Stable Version: " + Vault.this.newVersionTitle + " is out! You are still running version: " + Vault.this.currentVersionTitle);
                           Vault.log.warning("Update at: https://dev.bukkit.org/projects/vault");
                        } else if (Vault.this.currentVersion > Vault.this.newVersion) {
                           Vault.log.info("Stable Version: " + Vault.this.newVersionTitle + " | Current Version: " + Vault.this.currentVersionTitle);
                        } else {
                           Vault.log.info("No new version available");
                        }
                     } catch (Exception var2) {
                     }
                  }

               }
            }, 0L, 432000L);
         }
      });
      Metrics metrics = new Metrics(this);
      this.findCustomData(metrics);
      log.info(String.format("Enabled Version %s", this.getDescription().getVersion()));
   }

   private void loadChat() {
      this.hookChat("PermissionsEx", Chat_PermissionsEx.class, ServicePriority.Highest, "ru.tehkode.permissions.bukkit.PermissionsEx");
      this.hookChat("mChatSuite", Chat_mChatSuite.class, ServicePriority.Highest, "in.mDev.MiracleM4n.mChatSuite.mChatSuite");
      this.hookChat("mChat", Chat_mChat.class, ServicePriority.Highest, "net.D3GN.MiracleM4n.mChat");
      this.hookChat("OverPermissions", Chat_OverPermissions.class, ServicePriority.Highest, "com.overmc.overpermissions.internal.OverPermissions");
      this.hookChat("DroxPerms", Chat_DroxPerms.class, ServicePriority.Lowest, "de.hydrox.bukkit.DroxPerms.DroxPerms");
      this.hookChat("bPermssions2", Chat_bPermissions2.class, ServicePriority.Highest, "de.bananaco.bpermissions.api.ApiLayer");
      this.hookChat("bPermissions", Chat_bPermissions.class, ServicePriority.Normal, "de.bananaco.permissions.info.InfoReader");
      this.hookChat("GroupManager", Chat_GroupManager.class, ServicePriority.Normal, "org.anjocaido.groupmanager.GroupManager");
      this.hookChat("Permissions3", Chat_Permissions3.class, ServicePriority.Normal, "com.nijiko.permissions.ModularControl");
      this.hookChat("iChat", Chat_iChat.class, ServicePriority.Low, "net.TheDgtl.iChat.iChat");
      this.hookChat("Privileges", Chat_Privileges.class, ServicePriority.Normal, "net.krinsoft.privileges.Privileges");
      this.hookChat("rscPermissions", Chat_rscPermissions.class, ServicePriority.Normal, "ru.simsonic.rscPermissions.MainPluginClass");
      this.hookChat("TotalPermissions", Chat_TotalPermissions.class, ServicePriority.Normal, "net.ar97.totalpermissions.TotalPermissions");
   }

   private void loadEconomy() {
      this.hookEconomy("MiConomy", Economy_MiConomy.class, ServicePriority.Normal, "com.gmail.bleedobsidian.miconomy.Main");
      this.hookEconomy("MineFaConomy", Economy_Minefaconomy.class, ServicePriority.Normal, "me.coniin.plugins.minefaconomy.Minefaconomy");
      this.hookEconomy("MultiCurrency", Economy_MultiCurrency.class, ServicePriority.Normal, "me.ashtheking.currency.Currency", "me.ashtheking.currency.CurrencyList");
      this.hookEconomy("MineConomy", Economy_MineConomy.class, ServicePriority.Normal, "me.mjolnir.mineconomy.MineConomy");
      this.hookEconomy("McMoney", Economy_McMoney.class, ServicePriority.Normal, "boardinggamer.mcmoney.McMoneyAPI");
      this.hookEconomy("CraftConomy3", Economy_Craftconomy3.class, ServicePriority.Normal, "com.greatmancode.craftconomy3.tools.interfaces.BukkitLoader");
      this.hookEconomy("eWallet", Economy_eWallet.class, ServicePriority.Normal, "me.ethan.eWallet.ECO");
      this.hookEconomy("BOSEconomy7", Economy_BOSE7.class, ServicePriority.Normal, "cosine.boseconomy.BOSEconomy", "cosine.boseconomy.CommandHandler");
      this.hookEconomy("CurrencyCore", Economy_CurrencyCore.class, ServicePriority.Normal, "is.currency.Currency");
      this.hookEconomy("Gringotts", Economy_Gringotts.class, ServicePriority.Normal, "org.gestern.gringotts.Gringotts");
      this.hookEconomy("Essentials Economy", Economy_Essentials.class, ServicePriority.Low, "com.earth2me.essentials.api.Economy", "com.earth2me.essentials.api.NoLoanPermittedException", "com.earth2me.essentials.api.UserDoesNotExistException");
      this.hookEconomy("iConomy 6", Economy_iConomy6.class, ServicePriority.High, "com.iCo6.iConomy");
      this.hookEconomy("EconXP", Economy_EconXP.class, ServicePriority.Normal, "ca.agnate.EconXP.EconXP");
      this.hookEconomy("GoldIsMoney2", Economy_GoldIsMoney2.class, ServicePriority.Normal, "com.flobi.GoldIsMoney2.GoldIsMoney");
      this.hookEconomy("GoldenChestEconomy", Economy_GoldenChestEconomy.class, ServicePriority.Normal, "me.igwb.GoldenChest.GoldenChestEconomy");
      this.hookEconomy("Dosh", Economy_Dosh.class, ServicePriority.Normal, "com.gravypod.Dosh.Dosh");
      this.hookEconomy("CommandsEX", Economy_CommandsEX.class, ServicePriority.Normal, "com.github.zathrus_writer.commandsex.api.EconomyAPI");
      this.hookEconomy("SDFEconomy", Economy_SDFEconomy.class, ServicePriority.Normal, "com.github.omwah.SDFEconomy.SDFEconomy");
      this.hookEconomy("XPBank", Economy_XPBank.class, ServicePriority.Normal, "com.gmail.mirelatrue.xpbank.XPBank");
      this.hookEconomy("TAEcon", Economy_TAEcon.class, ServicePriority.Normal, "net.teamalpha.taecon.TAEcon");
      this.hookEconomy("DigiCoin", Economy_DigiCoin.class, ServicePriority.Normal, "co.uk.silvania.cities.digicoin.DigiCoin");
   }

   private void loadPermission() {
      this.hookPermission("Starburst", Permission_Starburst.class, ServicePriority.Highest, "com.dthielke.starburst.StarburstPlugin");
      this.hookPermission("PermissionsEx", Permission_PermissionsEx.class, ServicePriority.Highest, "ru.tehkode.permissions.bukkit.PermissionsEx");
      this.hookPermission("OverPermissions", Permission_OverPermissions.class, ServicePriority.Highest, "com.overmc.overpermissions.internal.OverPermissions");
      this.hookPermission("PermissionsBukkit", Permission_PermissionsBukkit.class, ServicePriority.Normal, "com.platymuus.bukkit.permissions.PermissionsPlugin");
      this.hookPermission("DroxPerms", Permission_DroxPerms.class, ServicePriority.High, "de.hydrox.bukkit.DroxPerms.DroxPerms");
      this.hookPermission("SimplyPerms", Permission_SimplyPerms.class, ServicePriority.Highest, "net.crystalyx.bukkit.simplyperms.SimplyPlugin");
      this.hookPermission("bPermissions 2", Permission_bPermissions2.class, ServicePriority.Highest, "de.bananaco.bpermissions.api.WorldManager");
      this.hookPermission("Privileges", Permission_Privileges.class, ServicePriority.Highest, "net.krinsoft.privileges.Privileges");
      this.hookPermission("bPermissions", Permission_bPermissions.class, ServicePriority.High, "de.bananaco.permissions.SuperPermissionHandler");
      this.hookPermission("GroupManager", Permission_GroupManager.class, ServicePriority.High, "org.anjocaido.groupmanager.GroupManager");
      this.hookPermission("Permissions 3 (Yeti)", Permission_Permissions3.class, ServicePriority.Normal, "com.nijiko.permissions.ModularControl");
      this.hookPermission("Xperms", Permission_Xperms.class, ServicePriority.Low, "com.github.sebc722.Xperms");
      this.hookPermission("TotalPermissions", Permission_TotalPermissions.class, ServicePriority.Normal, "net.ae97.totalpermissions.TotalPermissions");
      this.hookPermission("rscPermissions", Permission_rscPermissions.class, ServicePriority.Normal, "ru.simsonic.rscPermissions.MainPluginClass");
      this.hookPermission("KPerms", Permission_KPerms.class, ServicePriority.Normal, "com.lightniinja.kperms.KPermsPlugin");
      Permission perms = new Permission_SuperPerms(this);
      this.sm.register(Permission.class, perms, this, ServicePriority.Lowest);
      log.info(String.format("[Permission] SuperPermissions loaded as backup permission system."));
      this.perms = (Permission)this.sm.getRegistration(Permission.class).getProvider();
   }

   private void hookChat(String name, Class<? extends Chat> hookClass, ServicePriority priority, String... packages) {
      try {
         if (packagesExists(packages)) {
            Chat chat = (Chat)hookClass.getConstructor(Plugin.class, Permission.class).newInstance(this, this.perms);
            this.sm.register(Chat.class, chat, this, priority);
            log.info(String.format("[Chat] %s found: %s", name, chat.isEnabled() ? "Loaded" : "Waiting"));
         }
      } catch (Exception var6) {
         log.severe(String.format("[Chat] There was an error hooking %s - check to make sure you're using a compatible version!", name));
      }

   }

   private void hookEconomy(String name, Class<? extends Economy> hookClass, ServicePriority priority, String... packages) {
      try {
         if (packagesExists(packages)) {
            Economy econ = (Economy)hookClass.getConstructor(Plugin.class).newInstance(this);
            this.sm.register(Economy.class, econ, this, priority);
            log.info(String.format("[Economy] %s found: %s", name, econ.isEnabled() ? "Loaded" : "Waiting"));
         }
      } catch (Exception var6) {
         log.severe(String.format("[Economy] There was an error hooking %s - check to make sure you're using a compatible version!", name));
      }

   }

   private void hookPermission(String name, Class<? extends Permission> hookClass, ServicePriority priority, String... packages) {
      try {
         if (packagesExists(packages)) {
            Permission perms = (Permission)hookClass.getConstructor(Plugin.class).newInstance(this);
            this.sm.register(Permission.class, perms, this, priority);
            log.info(String.format("[Permission] %s found: %s", name, perms.isEnabled() ? "Loaded" : "Waiting"));
         }
      } catch (Exception var6) {
         log.severe(String.format("[Permission] There was an error hooking %s - check to make sure you're using a compatible version!", name));
      }

   }

   public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
      if (!sender.hasPermission("vault.admin")) {
         sender.sendMessage("You do not have permission to use that command!");
         return true;
      } else if (command.getName().equalsIgnoreCase("vault-info")) {
         this.infoCommand(sender);
         return true;
      } else if (command.getName().equalsIgnoreCase("vault-convert")) {
         this.convertCommand(sender, args);
         return true;
      } else {
         sender.sendMessage("Vault Commands:");
         sender.sendMessage("  /vault-info - Displays information about Vault");
         sender.sendMessage("  /vault-convert [economy1] [economy2] - Converts from one Economy to another");
         return true;
      }
   }

   private void convertCommand(CommandSender sender, String[] args) {
      Collection<RegisteredServiceProvider<Economy>> econs = this.getServer().getServicesManager().getRegistrations(Economy.class);
      if (econs != null && econs.size() >= 2) {
         if (args.length != 2) {
            sender.sendMessage("You must specify only the economy to convert from and the economy to convert to. (names should not contain spaces)");
         } else {
            Economy econ1 = null;
            Economy econ2 = null;
            String economies = "";

            String econName;
            for(Iterator var7 = econs.iterator(); var7.hasNext(); economies = economies + econName) {
               RegisteredServiceProvider<Economy> econ = (RegisteredServiceProvider)var7.next();
               econName = ((Economy)econ.getProvider()).getName().replace(" ", "");
               if (econName.equalsIgnoreCase(args[0])) {
                  econ1 = (Economy)econ.getProvider();
               } else if (econName.equalsIgnoreCase(args[1])) {
                  econ2 = (Economy)econ.getProvider();
               }

               if (economies.length() > 0) {
                  economies = economies + ", ";
               }
            }

            if (econ1 == null) {
               sender.sendMessage("Could not find " + args[0] + " loaded on the server, check your spelling.");
               sender.sendMessage("Valid economies are: " + economies);
            } else if (econ2 == null) {
               sender.sendMessage("Could not find " + args[1] + " loaded on the server, check your spelling.");
               sender.sendMessage("Valid economies are: " + economies);
            } else {
               sender.sendMessage("This may take some time to convert, expect server lag.");
               OfflinePlayer[] var13 = Bukkit.getServer().getOfflinePlayers();
               int var14 = var13.length;

               for(int var15 = 0; var15 < var14; ++var15) {
                  OfflinePlayer op = var13[var15];
                  if (econ1.hasAccount(op) && !econ2.hasAccount(op)) {
                     econ2.createPlayerAccount(op);
                     double diff = econ1.getBalance(op) - econ2.getBalance(op);
                     if (diff > 0.0D) {
                        econ2.depositPlayer(op, diff);
                     } else if (diff < 0.0D) {
                        econ2.withdrawPlayer(op, -diff);
                     }
                  }
               }

               sender.sendMessage("Converson complete, please verify the data before using it.");
            }
         }
      } else {
         sender.sendMessage("You must have at least 2 economies loaded to convert.");
      }
   }

   private void infoCommand(CommandSender sender) {
      String registeredEcons = null;
      Collection<RegisteredServiceProvider<Economy>> econs = this.getServer().getServicesManager().getRegistrations(Economy.class);
      Iterator var4 = econs.iterator();

      while(var4.hasNext()) {
         RegisteredServiceProvider<Economy> econ = (RegisteredServiceProvider)var4.next();
         Economy e = (Economy)econ.getProvider();
         if (registeredEcons == null) {
            registeredEcons = e.getName();
         } else {
            registeredEcons = registeredEcons + ", " + e.getName();
         }
      }

      String registeredPerms = null;
      Collection<RegisteredServiceProvider<Permission>> perms = this.getServer().getServicesManager().getRegistrations(Permission.class);
      Iterator var16 = perms.iterator();

      while(var16.hasNext()) {
         RegisteredServiceProvider<Permission> perm = (RegisteredServiceProvider)var16.next();
         Permission p = (Permission)perm.getProvider();
         if (registeredPerms == null) {
            registeredPerms = p.getName();
         } else {
            registeredPerms = registeredPerms + ", " + p.getName();
         }
      }

      String registeredChats = null;
      Collection<RegisteredServiceProvider<Chat>> chats = this.getServer().getServicesManager().getRegistrations(Chat.class);
      Iterator var19 = chats.iterator();

      while(var19.hasNext()) {
         RegisteredServiceProvider<Chat> chat = (RegisteredServiceProvider)var19.next();
         Chat c = (Chat)chat.getProvider();
         if (registeredChats == null) {
            registeredChats = c.getName();
         } else {
            registeredChats = registeredChats + ", " + c.getName();
         }
      }

      RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
      Economy econ = null;
      if (rsp != null) {
         econ = (Economy)rsp.getProvider();
      }

      Permission perm = null;
      RegisteredServiceProvider<Permission> rspp = this.getServer().getServicesManager().getRegistration(Permission.class);
      if (rspp != null) {
         perm = (Permission)rspp.getProvider();
      }

      Chat chat = null;
      RegisteredServiceProvider<Chat> rspc = this.getServer().getServicesManager().getRegistration(Chat.class);
      if (rspc != null) {
         chat = (Chat)rspc.getProvider();
      }

      sender.sendMessage(String.format("[%s] Vault v%s Information", this.getDescription().getName(), this.getDescription().getVersion()));
      sender.sendMessage(String.format("[%s] Economy: %s [%s]", this.getDescription().getName(), econ == null ? "None" : econ.getName(), registeredEcons));
      sender.sendMessage(String.format("[%s] Permission: %s [%s]", this.getDescription().getName(), perm == null ? "None" : perm.getName(), registeredPerms));
      sender.sendMessage(String.format("[%s] Chat: %s [%s]", this.getDescription().getName(), chat == null ? "None" : chat.getName(), registeredChats));
   }

   private static boolean packagesExists(String... packages) {
      try {
         String[] var1 = packages;
         int var2 = packages.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            String pkg = var1[var3];
            Class.forName(pkg);
         }

         return true;
      } catch (Exception var5) {
         return false;
      }
   }

   public double updateCheck(double currentVersion) {
      try {
         URL url = new URL("https://api.curseforge.com/servermods/files?projectids=33184");
         URLConnection conn = url.openConnection();
         conn.setReadTimeout(5000);
         conn.addRequestProperty("User-Agent", "Vault Update Checker");
         conn.setDoOutput(true);
         BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
         String response = reader.readLine();
         JSONArray array = (JSONArray)JSONValue.parse(response);
         if (array.size() == 0) {
            this.getLogger().warning("No files found, or Feed URL is bad.");
            return currentVersion;
         } else {
            this.newVersionTitle = ((String)((JSONObject)array.get(array.size() - 1)).get("name")).replace("Vault", "").trim();
            return Double.valueOf(this.newVersionTitle.replaceFirst("\\.", "").trim());
         }
      } catch (Exception var8) {
         log.info("There was an issue attempting to check for the latest version.");
         return currentVersion;
      }
   }

   private void findCustomData(Metrics metrics) {
      RegisteredServiceProvider<Economy> rspEcon = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
      Economy econ = null;
      if (rspEcon != null) {
         econ = (Economy)rspEcon.getProvider();
      }

      final String econName = econ != null ? econ.getName() : "No Economy";
      metrics.addCustomChart(new Metrics.SimplePie("economy", new Callable<String>() {
         public String call() {
            return econName;
         }
      }));
      final String permName = ((Permission)Bukkit.getServer().getServicesManager().getRegistration(Permission.class).getProvider()).getName();
      metrics.addCustomChart(new Metrics.SimplePie("permission", new Callable<String>() {
         public String call() {
            return permName;
         }
      }));
      RegisteredServiceProvider<Chat> rspChat = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
      Chat chat = null;
      if (rspChat != null) {
         chat = (Chat)rspChat.getProvider();
      }

      final String chatName = chat != null ? chat.getName() : "No Chat";
      metrics.addCustomChart(new Metrics.SimplePie("chat", new Callable<String>() {
         public String call() {
            return chatName;
         }
      }));
   }

   public class VaultListener implements Listener {
      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPlayerJoin(PlayerJoinEvent event) {
         Player player = event.getPlayer();
         if (Vault.this.perms.has(player, "vault.update")) {
            try {
               if (Vault.this.newVersion > Vault.this.currentVersion) {
                  player.sendMessage("Vault " + Vault.this.newVersionTitle + " is out! You are running " + Vault.this.currentVersionTitle);
                  player.sendMessage("Update Vault at: https://dev.bukkit.org/projects/Vault");
               }
            } catch (Exception var4) {
            }
         }

      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginEnable(PluginEnableEvent event) {
         if (event.getPlugin().getDescription().getName().equals("Register") && Vault.packagesExists("com.nijikokun.register.payment.Methods") && !Methods.hasMethod()) {
            try {
               Method m = Methods.class.getMethod("addMethod", Methods.class);
               m.setAccessible(true);
               m.invoke((Object)null, "Vault", new VaultEco());
               if (!Methods.setPreferred("Vault")) {
                  Vault.log.info("Unable to hook register");
               } else {
                  Vault.log.info("[Vault] - Successfully injected Vault methods into Register.");
               }
            } catch (SecurityException var3) {
               Vault.log.info("Unable to hook register");
            } catch (NoSuchMethodException var4) {
               Vault.log.info("Unable to hook register");
            } catch (IllegalArgumentException var5) {
               Vault.log.info("Unable to hook register");
            } catch (IllegalAccessException var6) {
               Vault.log.info("Unable to hook register");
            } catch (InvocationTargetException var7) {
               Vault.log.info("Unable to hook register");
            }
         }

      }
   }
}
