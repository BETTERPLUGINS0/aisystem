package com.bergerkiller.bukkit.tc.tickets;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import com.bergerkiller.bukkit.common.map.MapDisplay;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Ticket {
   private String _name;
   private String _realm = "";
   private boolean _playerBound = false;
   private int _maxNumberOfUses = 1;
   private long _expirationTime = -1L;
   private String _backgroundImagePath = "";
   private ConfigurationNode _properties = new ConfigurationNode();

   Ticket(String name) {
      this._name = name;
   }

   public String getName() {
      return this._name;
   }

   public boolean setName(String name) {
      if (this._name.equals(name)) {
         return true;
      } else {
         String oldName = this._name;
         this._name = name;
         if (TicketStore.renameTicket(oldName, this._name)) {
            return true;
         } else {
            this._name = oldName;
            return false;
         }
      }
   }

   public boolean remove() {
      return TicketStore.removeTicket(this._name);
   }

   public String getRealm() {
      return this._realm;
   }

   public void setRealm(String realm) {
      this._realm = realm;
   }

   public String getBackgroundImagePath() {
      return this._backgroundImagePath;
   }

   public void setBackgroundImagePath(String path) {
      this._backgroundImagePath = path;
   }

   public void setBackgroundImagePluginPath(JavaPlugin plugin, String path) {
      this._backgroundImagePath = plugin.getName() + ":" + path;
   }

   public MapTexture loadBackgroundImage() {
      if (this._backgroundImagePath.isEmpty()) {
         return getDefaultBackgroundImage();
      } else {
         int index = this._backgroundImagePath.indexOf(58);
         MapTexture bg;
         if (index != -1) {
            String pluginName = this._backgroundImagePath.substring(0, index);
            Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
            if (plugin instanceof JavaPlugin) {
               try {
                  bg = MapTexture.loadPluginResource((JavaPlugin)plugin, this._backgroundImagePath.substring(index + 1));
                  if (bg.getWidth() >= 128 && bg.getHeight() >= 128) {
                     return bg;
                  }
               } catch (RuntimeException var7) {
               }

               return getDefaultBackgroundImage();
            }
         }

         File imagesDir = TrainCarts.plugin.getDataFile(new String[]{"images"});
         File imageFile = new File(this._backgroundImagePath);
         if (!imageFile.isAbsolute()) {
            imageFile = new File(imagesDir, this._backgroundImagePath);
         }

         if (!TCConfig.allowExternalTicketImagePaths) {
            boolean validLocation;
            try {
               File a = imageFile.getAbsoluteFile().getCanonicalFile();
               File b = imagesDir.getAbsoluteFile().getCanonicalFile();
               validLocation = a.toPath().startsWith(b.toPath());
            } catch (IOException var9) {
               validLocation = false;
            }

            if (!validLocation) {
               return getDefaultBackgroundImage();
            }
         }

         try {
            bg = MapTexture.fromImageFile(imageFile.getAbsolutePath());
            if (bg.getWidth() >= 128 && bg.getHeight() >= 128) {
               return bg;
            }
         } catch (RuntimeException var8) {
         }

         return getDefaultBackgroundImage();
      }
   }

   public static MapTexture getDefaultBackgroundImage() {
      return TrainCarts.plugin.loadTexture("com/bergerkiller/bukkit/tc/textures/tickets/train_ticket_bg.png");
   }

   public boolean isPlayerBound() {
      return this._playerBound;
   }

   public void setPlayerBound(boolean playerBound) {
      this._playerBound = playerBound;
   }

   public int getMaxNumberOfUses() {
      return this._maxNumberOfUses;
   }

   public void setMaxNumberOfUses(int maxNumberOfUses) {
      this._maxNumberOfUses = maxNumberOfUses;
   }

   public long getExpirationTime() {
      return this._expirationTime;
   }

   public void setExpirationTime(long expirationTimeMillis) {
      this._expirationTime = expirationTimeMillis;
   }

   public ConfigurationNode getProperties() {
      return this._properties;
   }

   public void setProperties(ConfigurationNode properties) {
      this._properties = properties.clone();
      TicketStore.markChanged();
   }

   public void load(ConfigurationNode config) {
      this._realm = (String)config.get("ticketRealm", "");
      this._playerBound = (Boolean)config.get("playerBound", false);
      this._maxNumberOfUses = (Integer)config.get("maxNumberOfUses", 1);
      this._expirationTime = (Long)config.get("expirationTimeMillis", -1L);
      this._backgroundImagePath = (String)config.get("backgroundImagePath", "");
      this._properties = config.getNode("properties").clone();
   }

   public void save(ConfigurationNode config) {
      config.set("ticketRealm", this._realm);
      config.set("playerBound", this._playerBound);
      config.set("maxNumberOfUses", this._maxNumberOfUses);
      config.set("expirationTimeMillis", this._expirationTime);
      config.set("backgroundImagePath", this._backgroundImagePath);
      ConfigurationNode savedProps = config.getNode("properties");
      Iterator var3 = this._properties.getValues().entrySet().iterator();

      while(var3.hasNext()) {
         Entry<String, Object> entry = (Entry)var3.next();
         savedProps.set((String)entry.getKey(), entry.getValue());
      }

   }

   public ItemStack createItem(Player owner, ItemStack baseItem) {
      return CommonItemStack.copyOf(baseItem).updateCustomData((tag) -> {
         tag.putValue("plugin", "TrainCarts");
         tag.putValue("ticketName", this.getName());
         tag.putValue("ticketCreationTime", System.currentTimeMillis());
         tag.putValue("ticketNumberOfUses", 0);
         tag.putUUID("ticketOwner", owner.getUniqueId());
         tag.putValue("ticketOwnerName", owner.getDisplayName());
      }).setCustomNameMessage("Train Ticket for " + this.getName()).toBukkit();
   }

   public ItemStack createItem(Player owner) {
      return CommonItemStack.of(MapDisplay.createMapItem(TCTicketDisplay.class)).updateCustomData((tag) -> {
         tag.putValue("plugin", "TrainCarts");
         tag.putValue("ticketName", this.getName());
         tag.putValue("ticketCreationTime", System.currentTimeMillis());
         tag.putValue("ticketNumberOfUses", 0);
         tag.putUUID("ticketOwner", owner.getUniqueId());
         tag.putValue("ticketOwnerName", owner.getDisplayName());
      }).setCustomNameMessage("Train Ticket for " + this.getName()).toBukkit();
   }
}
