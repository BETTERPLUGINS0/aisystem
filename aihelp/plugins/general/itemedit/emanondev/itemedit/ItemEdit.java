package emanondev.itemedit;

import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.ItemEditImportCommand;
import emanondev.itemedit.command.ItemEditInfoCommand;
import emanondev.itemedit.command.ItemStorageCommand;
import emanondev.itemedit.command.ReloadCommand;
import emanondev.itemedit.command.ServerItemCommand;
import emanondev.itemedit.compability.DungeonMMOItemProvider;
import emanondev.itemedit.compability.Hooks;
import emanondev.itemedit.compability.ItemBridgeItemProvider;
import emanondev.itemedit.compability.MythicMobsListener;
import emanondev.itemedit.compability.Placeholders;
import emanondev.itemedit.compability.ShopGuiPlusItemProvider;
import emanondev.itemedit.gui.Gui;
import emanondev.itemedit.gui.GuiHandler;
import emanondev.itemedit.storage.PlayerStorage;
import emanondev.itemedit.storage.ServerStorage;
import emanondev.itemedit.storage.StorageType;
import emanondev.itemedit.storage.mongo.MongoPlayerStorage;
import emanondev.itemedit.storage.mongo.MongoServerStorage;
import emanondev.itemedit.storage.mongo.MongoStorage;
import emanondev.itemedit.storage.yaml.YmlPlayerStorage;
import emanondev.itemedit.storage.yaml.YmlServerStorage;
import emanondev.itemedit.utility.InventoryUtils;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemEdit extends APlugin {
   private static ItemEdit plugin = null;
   private PlayerStorage playerStorage;
   private ServerStorage serverStorage;
   @Nullable
   private MongoStorage mongoStorage;

   public static ItemEdit get() {
      return plugin;
   }

   public void onLoad() {
      plugin = this;
   }

   @NotNull
   public StorageType getStorageType() {
      return (StorageType)StorageType.byName((String)this.getConfig().load("storage.type", "", String.class)).orElse(StorageType.YAML);
   }

   public void enable() {
      if (Util.hasMiniMessageAPI()) {
         get().log("Hooking into <rainbow>MiniMessageAPI</rainbow><white> see https://webui.advntr.dev/");
      }

      Aliases.reload();
      Bukkit.getPluginManager().registerEvents(new GuiHandler(), this);
      StorageType storageType = this.getStorageType();
      this.log("Selected Storage Type: " + storageType.name());
      switch(storageType) {
      case YAML:
         this.playerStorage = new YmlPlayerStorage();
         this.serverStorage = new YmlServerStorage();
         break;
      case MONGODB:
         String connectionString = (String)this.getConfig().load("storage.mongodb.uri", "mongodb://127.0.0.1:27017", String.class);
         String database = (String)this.getConfig().load("storage.mongodb.database", "itemedit", String.class);
         String collectionPrefix = (String)this.getConfig().load("storage.mongodb.collection_prefix", "itemedit-", String.class);
         this.mongoStorage = new MongoStorage(connectionString, database, collectionPrefix);
         this.playerStorage = new MongoPlayerStorage(this.mongoStorage, this.getLogger());
         this.serverStorage = new MongoServerStorage(this.mongoStorage);
         break;
      default:
         this.enableWithError("Selected storage type is invalid, please fix it: open plugins/ItemEdit/config.yml and set storage: -> type: 'YAML' then restart the server");
         return;
      }

      this.initCommands();
      this.initHooks();
   }

   public void disable() {
      Iterator var1 = Bukkit.getOnlinePlayers().iterator();

      while(var1.hasNext()) {
         Player p = (Player)var1.next();
         if (InventoryUtils.getTopInventory(p).getHolder() instanceof Gui) {
            p.closeInventory();
         }
      }

      if (this.mongoStorage != null) {
         this.mongoStorage.close();
      }

   }

   public void reload() {
      Aliases.reload();
      ItemEditCommand.get().reload();
      ItemStorageCommand.get().reload();
      ServerItemCommand.get().reload();
      this.getPlayerStorage().reload();
      this.getServerStorage().reload();
   }

   protected void updateConfigurations(int oldConfigVersion) {
      YMLConfig conf = this.getConfig();
      YMLConfig aliases = this.getConfig("aliases.yml");
      if (oldConfigVersion <= 3) {
         conf.set("check-updates", true);
      }

      if (oldConfigVersion <= 4) {
         conf.set("storage.type", "YAML");
         conf.set("storage.mongodb.uri", "mongodb://127.0.0.1:27017");
         conf.set("storage.mongodb.database", "itemedit");
         conf.set("storage.mongodb.collection_prefix", "itemedit");
         String[] var4 = new String[]{"quartz", "redstone", "emerald", "copper", "iron", "lapis", "diamond", "gold", "netherite", "amethyst"};
         int var5 = var4.length;

         int var6;
         String name;
         for(var6 = 0; var6 < var5; ++var6) {
            name = var4[var6];
            aliases.set("trim_material.minecraft:" + name.toLowerCase(Locale.ENGLISH), name.toLowerCase(Locale.ENGLISH));
         }

         var4 = new String[]{"rib", "snout", "wild", "coast", "spire", "wayfinder", "shaper", "tide", "silence", "vex", "sentry", "dune", "raiser", "eye", "host", "ward"};
         var5 = var4.length;

         for(var6 = 0; var6 < var5; ++var6) {
            name = var4[var6];
            aliases.set("trim_pattern.minecraft:" + name.toLowerCase(Locale.ENGLISH), name.toLowerCase(Locale.ENGLISH));
         }
      }

      if (oldConfigVersion <= 6) {
         conf.set("blocked.lore-line-limit", 16);
      }

      if (oldConfigVersion <= 7) {
         aliases.set("attribute.generic_max_absorption", "max_absorption");
      }

   }

   protected boolean addLanguagesMetrics() {
      return true;
   }

   @NotNull
   protected Predicate<Player> languagesMetricsIsAdmin() {
      return (p) -> {
         return p.hasPermission("itemedit.admin");
      };
   }

   @NotNull
   protected Predicate<Player> languagesMetricsIsUser() {
      return (p) -> {
         return p.hasPermission("itemedit.creativeuser") || p.hasPermission("itemedit.itemedit.rename") || p.hasPermission("itemedit.itemedit.lore") || p.hasPermission("itemedit.itemedit.color");
      };
   }

   private void initHooks() {
      if (Hooks.isPAPIEnabled()) {
         try {
            this.log("Hooking into PlaceHolderAPI");
            (new Placeholders()).register();
         } catch (Throwable var6) {
            var6.printStackTrace();
         }
      }

      if (Hooks.isShopGuiPlusEnabled()) {
         try {
            this.log("Hooking into ShopGuiPlus");
            (new ShopGuiPlusItemProvider()).register();
         } catch (Throwable var5) {
            var5.printStackTrace();
         }
      }

      if (Hooks.isMythicMobsEnabled()) {
         try {
            this.log("Hooking into MythicMobs");
            this.registerListener(new MythicMobsListener());
         } catch (Throwable var4) {
            var4.printStackTrace();
         }
      }

      if (Hooks.isItemBridgeEnabled()) {
         try {
            this.log("Hooking into ItemBridge");
            ItemBridgeItemProvider.setup(this);
         } catch (Throwable var3) {
            var3.printStackTrace();
         }
      }

      if (Hooks.isDungeonMMOEnabled()) {
         try {
            this.log("Hooking into DungeonMMO");
            DungeonMMOItemProvider.register();
         } catch (Throwable var2) {
            var2.printStackTrace();
         }
      }

   }

   private void initCommands() {
      this.registerCommand(new ItemEditCommand(), Collections.singletonList("ie"));
      this.registerCommand(new ItemStorageCommand(), Collections.singletonList("is"));
      this.registerCommand(new ServerItemCommand(), Collections.singletonList("si"));
      this.registerCommand("itemeditinfo", new ItemEditInfoCommand(), (List)null);
      (new ReloadCommand(this)).register();
      this.registerCommand("itemeditimport", new ItemEditImportCommand(), (List)null);
   }

   public PlayerStorage getPlayerStorage() {
      return this.playerStorage;
   }

   public ServerStorage getServerStorage() {
      return this.serverStorage;
   }
}
