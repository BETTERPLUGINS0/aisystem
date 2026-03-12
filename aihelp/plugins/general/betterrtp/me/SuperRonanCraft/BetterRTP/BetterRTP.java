package me.SuperRonanCraft.BetterRTP;

import java.util.List;
import me.SuperRonanCraft.BetterRTP.player.PlayerInfo;
import me.SuperRonanCraft.BetterRTP.player.commands.Commands;
import me.SuperRonanCraft.BetterRTP.player.events.EventListener;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP;
import me.SuperRonanCraft.BetterRTP.references.Permissions;
import me.SuperRonanCraft.BetterRTP.references.RTPLogger;
import me.SuperRonanCraft.BetterRTP.references.WarningHandler;
import me.SuperRonanCraft.BetterRTP.references.database.DatabaseHandler;
import me.SuperRonanCraft.BetterRTP.references.depends.DepEconomy;
import me.SuperRonanCraft.BetterRTP.references.depends.DepPlaceholderAPI;
import me.SuperRonanCraft.BetterRTP.references.file.Files;
import me.SuperRonanCraft.BetterRTP.references.invs.RTPInventories;
import me.SuperRonanCraft.BetterRTP.references.messages.Message_RTP;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesCore;
import me.SuperRonanCraft.BetterRTP.references.player.playerdata.PlayerDataManager;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.CooldownHandler;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.QueueHandler;
import me.SuperRonanCraft.BetterRTP.references.settings.Settings;
import me.SuperRonanCraft.BetterRTP.references.web.Metrics;
import me.SuperRonanCraft.BetterRTP.references.web.Updater;
import me.SuperRonanCraft.BetterRTP.versions.FoliaHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterRTP extends JavaPlugin {
   private final Permissions perms = new Permissions();
   private final DepEconomy eco = new DepEconomy();
   private final Commands cmd = new Commands(this);
   private final RTP RTP = new RTP();
   private final EventListener listener = new EventListener();
   private static BetterRTP instance;
   private final Files files = new Files();
   private final RTPInventories invs = new RTPInventories();
   private final PlayerInfo pInfo = new PlayerInfo();
   private final PlayerDataManager playerDataManager = new PlayerDataManager();
   private final Settings settings = new Settings();
   private final CooldownHandler cooldowns = new CooldownHandler();
   private final QueueHandler queue = new QueueHandler();
   private final DatabaseHandler databaseHandler = new DatabaseHandler();
   private final WarningHandler warningHandler = new WarningHandler();
   private boolean PlaceholderAPI;
   private final RTPLogger rtpLogger = new RTPLogger();
   private final FoliaHandler foliaHandler = new FoliaHandler();

   public void onEnable() {
      instance = this;
      this.registerDependencies();
      this.loadAll();
      new Updater(this);
      new Metrics(this);
      this.listener.registerEvents(this);
      this.queue.registerEvents(this);

      try {
         (new DepPlaceholderAPI()).register();
      } catch (NoClassDefFoundError var2) {
      }

   }

   public void onDisable() {
      this.invs.closeAll();
      this.queue.unload();
      this.rtpLogger.unload();
   }

   private void registerDependencies() {
      this.PlaceholderAPI = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
   }

   public boolean onCommand(CommandSender sendi, Command cmd, String label, String[] args) {
      try {
         this.cmd.commandExecuted(sendi, label, args);
      } catch (NullPointerException var6) {
         var6.printStackTrace();
         Message_RTP.sms(sendi, "&cERROR &7Seems like your Administrator did not update their language file!");
      }

      return true;
   }

   public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
      return this.cmd.onTabComplete(sender, args);
   }

   public void reload(CommandSender sendi) {
      this.invs.closeAll();
      this.loadAll();
      MessagesCore.RELOAD.send(sendi);
   }

   private void loadAll() {
      this.foliaHandler.load();
      this.playerDataManager.clear();
      this.files.loadAll();
      this.settings.load();
      this.cooldowns.load();
      this.databaseHandler.load();
      this.rtpLogger.setup(this);
      this.invs.load();
      this.RTP.load();
      this.cmd.load();
      this.listener.load();
      this.eco.load();
      this.perms.register();
      this.queue.load();
   }

   public static void debug(String str) {
      getInstance().getLogger().info(str);
   }

   public Permissions getPerms() {
      return this.perms;
   }

   public DepEconomy getEco() {
      return this.eco;
   }

   public Commands getCmd() {
      return this.cmd;
   }

   public RTP getRTP() {
      return this.RTP;
   }

   public static BetterRTP getInstance() {
      return instance;
   }

   public Files getFiles() {
      return this.files;
   }

   public RTPInventories getInvs() {
      return this.invs;
   }

   public PlayerInfo getPInfo() {
      return this.pInfo;
   }

   public PlayerDataManager getPlayerDataManager() {
      return this.playerDataManager;
   }

   public Settings getSettings() {
      return this.settings;
   }

   public CooldownHandler getCooldowns() {
      return this.cooldowns;
   }

   public QueueHandler getQueue() {
      return this.queue;
   }

   public DatabaseHandler getDatabaseHandler() {
      return this.databaseHandler;
   }

   public WarningHandler getWarningHandler() {
      return this.warningHandler;
   }

   public boolean isPlaceholderAPI() {
      return this.PlaceholderAPI;
   }

   public RTPLogger getRtpLogger() {
      return this.rtpLogger;
   }

   public FoliaHandler getFoliaHandler() {
      return this.foliaHandler;
   }
}
