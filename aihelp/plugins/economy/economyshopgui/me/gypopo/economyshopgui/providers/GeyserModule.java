package me.gypopo.economyshopgui.providers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.config.Config;
import me.gypopo.economyshopgui.methodes.SendMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.event.EventRegistrar;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomSkullsEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomSkullsEvent.SkullTextureType;

public class GeyserModule implements EventRegistrar {
   private final EconomyShopGUI plugin;

   public GeyserModule(EconomyShopGUI plugin) {
      this.plugin = plugin;
      GeyserApi.api().eventBus().register(this, this);
      GeyserApi.api().eventBus().subscribe(this, GeyserDefineCustomSkullsEvent.class, this::onDefineCustomSkulls);
   }

   public void onDefineCustomSkulls(GeyserDefineCustomSkullsEvent event) {
      GeyserModule.SkullTextures textures = this.scrapeSkullTextures();
      if (!textures.isEmpty()) {
         SendMessage.infoMessage("Registering " + textures.getSize() + " player head texture(s) for bedrock players...");
         Iterator var3 = textures.getTextures().iterator();

         String owner;
         while(var3.hasNext()) {
            owner = (String)var3.next();
            event.register(owner, SkullTextureType.PROFILE);
         }

         var3 = textures.getOwners().iterator();

         while(var3.hasNext()) {
            owner = (String)var3.next();
            event.register(owner, SkullTextureType.USERNAME);
         }

      }
   }

   private GeyserModule.SkullTextures scrapeSkullTextures() {
      GeyserModule.SkullTextures textures = new GeyserModule.SkullTextures();
      this.getNavBarTextures(textures, "main-menu-nav-bar");
      this.getNavBarTextures(textures, "shops-nav-bar");
      this.getNavBarTextures(textures, "transaction-screens-nav-bar");
      this.getNavBarTextures(textures, "sellgui-nav-bar");
      this.getNavBarTextures(textures, "shop-search-nav-bar");
      this.getNavBarTextures(textures, "buy-screen");
      this.getNavBarTextures(textures, "sell-screen");
      this.getNavBarTextures(textures, "buy-stacks-screen");
      Iterator var2 = ConfigManager.getSections().iterator();

      while(true) {
         String shop;
         Config conf;
         do {
            do {
               if (!var2.hasNext()) {
                  return textures;
               }

               shop = (String)var2.next();
            } while(!ConfigManager.getSection(shop).getBoolean("enabled", true));

            conf = ConfigManager.getShop(shop);
         } while(conf == null);

         this.getShopNavBarTextures(textures, shop);
         Iterator var5 = this.plugin.getConfigManager().getItemsRaw(shop).iterator();

         while(var5.hasNext()) {
            String item = (String)var5.next();
            String path = "pages." + item;
            String owner = conf.getString(path + ".skullowner");
            if (owner != null && !owner.isEmpty() && owner.matches("[a-zA-Z0-9_]{3,16}")) {
               textures.addOwner(owner);
            }
         }
      }
   }

   private void getShopNavBarTextures(GeyserModule.SkullTextures textures, String shop) {
      if (!ConfigManager.getSection(shop).getString("nav-bar.mode", "INHERIT").equalsIgnoreCase("DISABLED")) {
         ConfigurationSection section = ConfigManager.getSection(shop).getConfigurationSection("nav-bar.items");
         if (section != null && !section.getKeys(false).isEmpty()) {
            Iterator var4 = section.getKeys(false).iterator();

            while(var4.hasNext()) {
               String item = (String)var4.next();
               String path = "nav-bar.items." + item;
               String owner = ConfigManager.getSection(shop).getString(path + ".skullowner");
               if (owner != null && !owner.isEmpty() && owner.matches("[a-zA-Z0-9_]{3,16}")) {
                  textures.addOwner(owner);
               }
            }

         }
      }
   }

   private void getNavBarTextures(GeyserModule.SkullTextures textures, String path) {
      if (ConfigManager.getConfig().getBoolean(path + ".enabled", true)) {
         ConfigurationSection section = ConfigManager.getConfig().getConfigurationSection(path + ".items");
         if (section != null && !section.getKeys(false).isEmpty()) {
            Iterator var4 = section.getKeys(false).iterator();

            while(var4.hasNext()) {
               String item = (String)var4.next();
               String confPath = path + ".items." + item;
               String owner = ConfigManager.getConfig().getString(confPath + ".skullowner");
               if (owner != null && !owner.isEmpty() && owner.matches("[a-zA-Z0-9_]{3,16}")) {
                  textures.addOwner(owner);
               }
            }

         }
      }
   }

   private class SkullTextures {
      final Set<String> customTextures = new HashSet();
      final Set<String> skullOwners = new HashSet();

      public SkullTextures() {
      }

      public void addTexture(String texture) {
         this.customTextures.add(texture);
      }

      public void addOwner(String owner) {
         this.skullOwners.add(owner);
      }

      public int getSize() {
         return this.customTextures.size() + this.skullOwners.size();
      }

      public Set<String> getTextures() {
         return this.customTextures;
      }

      public Set<String> getOwners() {
         return this.skullOwners;
      }

      public boolean isEmpty() {
         return this.customTextures.isEmpty() && this.skullOwners.isEmpty();
      }
   }
}
