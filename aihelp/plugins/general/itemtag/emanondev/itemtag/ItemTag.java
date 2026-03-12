package emanondev.itemtag;

import emanondev.itemedit.APlugin;
import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.command.ReloadCommand;
import emanondev.itemedit.compability.Hooks;
import emanondev.itemedit.storage.ServerStorage;
import emanondev.itemedit.utility.VersionUtils;
import emanondev.itemtag.actions.ActionHandler;
import emanondev.itemtag.actions.ActionsUtility;
import emanondev.itemtag.actions.DelayedAction;
import emanondev.itemtag.actions.MessageAction;
import emanondev.itemtag.actions.PermissionAction;
import emanondev.itemtag.actions.PlayerAsOpCommandAction;
import emanondev.itemtag.actions.PlayerCommandAction;
import emanondev.itemtag.actions.ServerCommandAction;
import emanondev.itemtag.actions.SoundAction;
import emanondev.itemtag.activity.ActivityManager;
import emanondev.itemtag.activity.target.TargetManager;
import emanondev.itemtag.command.ItemTagCommand;
import emanondev.itemtag.command.ItemTagUpdateOldItem;
import emanondev.itemtag.command.itemtag.SecurityUtil;
import emanondev.itemtag.compability.Placeholders;
import emanondev.itemtag.equipmentchange.EquipmentChangeListener;
import emanondev.itemtag.equipmentchange.EquipmentChangeListenerBase;
import emanondev.itemtag.equipmentchange.EquipmentChangeListenerUpTo1_13;
import emanondev.itemtag.equipmentchange.EquipmentChangeListenerUpTo1_8;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemTag extends APlugin {
   private static ItemTag plugin = null;
   private static TagManager tagManager = null;
   private static boolean USE_NBTAPI;
   private EquipmentChangeListenerBase equipChangeListener;
   private TargetManager targetManager;

   public static ItemTag get() {
      return plugin;
   }

   public static TagItem getTagItem(@Nullable ItemStack item) {
      return (TagItem)(USE_NBTAPI ? new NBTAPITagItem(item) : new SpigotTagItem(item));
   }

   /** @deprecated */
   @Deprecated
   public TagManager getTagManager() {
      return tagManager;
   }

   public void onLoad() {
      plugin = this;
   }

   public void enable() {
      try {
         this.initDataPersistance();
      } catch (Exception var4) {
         Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error while enabling ItemTag, disabling it");
         var4.printStackTrace();
         Bukkit.getServer().getPluginManager().disablePlugin(this);
         return;
      }

      try {
         if (VersionUtils.isVersionUpTo(1, 8, 9)) {
            this.equipChangeListener = new EquipmentChangeListenerUpTo1_8();
         } else if (VersionUtils.isVersionUpTo(1, 12, 2)) {
            this.equipChangeListener = new EquipmentChangeListenerUpTo1_13();
         } else {
            this.equipChangeListener = new EquipmentChangeListener();
         }

         this.equipChangeListener.reload();
         this.registerCommand(new ItemTagCommand(), Collections.singletonList("it"));
         (new ReloadCommand(this)).register();
         this.registerCommand("itemtagupdateolditem", new ItemTagUpdateOldItem(), (List)null);
         ActionHandler.clearActions();
         ActionHandler.registerAction(new DelayedAction());
         ActionHandler.registerAction(new PermissionAction());
         ActionHandler.registerAction(new PlayerCommandAction());
         ActionHandler.registerAction(new PlayerAsOpCommandAction());
         ActionHandler.registerAction(new ServerCommandAction());
         ActionHandler.registerAction(new SoundAction());
         ActionHandler.registerAction(new MessageAction());
         if (Hooks.isPAPIEnabled()) {
            try {
               this.log("Hooking into PlaceholderApi");
               (new Placeholders()).register();
            } catch (Throwable var2) {
               var2.printStackTrace();
            }
         }
      } catch (Exception var3) {
         Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error while enabling ItemTag, disabling it");
         var3.printStackTrace();
         Bukkit.getServer().getPluginManager().disablePlugin(this);
      }

   }

   public void reload() {
      this.equipChangeListener.reload();
      Aliases.reload();
      ActivityManager.reload();
      ItemTagCommand.get().reload();
   }

   public void disable() {
   }

   protected void updateConfigurations(int oldConfigVersion) {
      if (oldConfigVersion <= 4) {
         ServerStorage storage = ItemEdit.get().getServerStorage();
         this.log("Updating storage");
         Iterator var3 = storage.getIds().iterator();

         label47:
         while(true) {
            String id;
            TagItem tagItem;
            do {
               if (!var3.hasNext()) {
                  break label47;
               }

               id = (String)var3.next();
               ItemStack item = storage.getItem(id);
               tagItem = getTagItem(item);
            } while(!ActionsUtility.hasActions(tagItem));

            List<String> actions = new ArrayList(ActionsUtility.getActions(tagItem));
            boolean updating = false;

            for(int i = 0; i < actions.size(); ++i) {
               String action = (String)actions.get(i);
               String prefix = null;
               if (action.startsWith("commandasop%%:%%")) {
                  prefix = "commandasop%%:%%";
               } else if (action.startsWith("servercommand%%:%%")) {
                  prefix = "servercommand%%:%%";
               }

               if (prefix != null) {
                  actions.set(i, prefix + "-pin" + SecurityUtil.generateControlKey(action.substring(prefix.length())) + " " + action.substring(prefix.length()));
                  updating = true;
               }
            }

            if (updating) {
               this.log("Updated item &e" + id);
               ActionsUtility.setActions(tagItem, actions);
               storage.setItem(id, tagItem.getItem());
            }

            this.log("&cWARNING");
            this.log("A severe security bug was patched, items from (/serveritem or /si)");
            this.log("have been updated to match security standards, however items inside");
            this.log("players inventories haven't been updated and may stop working if they");
            this.log("had any actions of servercommand or commandasop kind");
            this.log("If you need more info feel free to ask for support on our discord");
            this.log("Discord: https://discord.gg/w5HVCDPtRp");
         }
      }

      if (oldConfigVersion <= 5) {
         this.getConfig().set("flag.vanishcurse.override_keepinventory", false);
      }

   }

   private void initDataPersistance() throws Exception {
      String var1 = this.getConfig().getString("data.preference", "SPIGOT").toUpperCase(Locale.ENGLISH);
      byte var2 = -1;
      switch(var1.hashCode()) {
      case -1998373670:
         if (var1.equals("NBTAPI")) {
            var2 = 0;
         }
         break;
      case -1842620576:
         if (var1.equals("SPIGOT")) {
            var2 = 1;
         }
      }

      switch(var2) {
      case 0:
         try {
            this.initNBTAPI();
         } catch (Exception var4) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "NBTAPI is selected as data.preference but it's not installed/working, if you wish to use NBTAPI get the plugin at www.spigotmc.org/resources/7939/");
            this.initDefault();
         }

         return;
      case 1:
         this.initDefault();
         return;
      default:
         Bukkit.getConsoleSender().sendMessage(ChatColor.RED + this.getConfig().getString("data.preference", "SPIGOT") + " is selected as data.preference but it's unknown");
         this.initDefault();
      }
   }

   private void initNBTAPI() throws Exception {
      new NBTAPITagItem(new ItemStack(Material.STONE));
      USE_NBTAPI = true;
      tagManager = new NBTAPITagManager();
      this.log("Data using NBTAPI");
   }

   private void initSpigotPersistentDataAPI() throws Exception {
      USE_NBTAPI = false;
      tagManager = new SpigotTagManager();
      this.log("Data using Spigot PersistentDataContainer");
   }

   private void initDefault() throws Exception {
      if (!VersionUtils.isVersionAfter(1, 14)) {
         try {
            this.initNBTAPI();
         } catch (Exception var3) {
            String error = "NBTAPI is required on this server version check www.spigotmc.org/resources/7939/";
            this.enableWithError(error);
         }
      } else {
         this.initSpigotPersistentDataAPI();
      }

   }

   public EquipmentChangeListenerBase getEquipChangeListener() {
      return this.equipChangeListener;
   }

   public TargetManager getTargetManager() {
      return this.targetManager;
   }
}
