package com.lenis0012.bukkit.loginsecurity.modules.captcha;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.modules.Module;
import com.lenis0012.bukkit.loginsecurity.util.MetaData;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class CaptchaManager extends Module<LoginSecurity> implements Listener {
   private final Random random = new Random();
   private MapView view;
   private Method setMapIdMethod;
   private Method setMapViewMethod;
   private int mapViewId;
   private boolean failedToLoadMapView = false;

   public CaptchaManager(LoginSecurity plugin) {
      super(plugin);
   }

   public void enable() {
      this.view = Bukkit.createMap((World)Bukkit.getWorlds().get(0));
      Iterator var1 = this.view.getRenderers().iterator();

      while(var1.hasNext()) {
         MapRenderer renderer = (MapRenderer)var1.next();
         this.view.removeRenderer(renderer);
      }

      this.view.addRenderer(new CaptchaRenderer());
      this.register(this);

      try {
         this.setMapViewMethod = MapMeta.class.getMethod("setMapView", MapView.class);
         LoginSecurity.getInstance().getLogger().log(Level.INFO, "Using 1.13+ map captcha renderer");
      } catch (Exception var9) {
         try {
            this.setMapIdMethod = MapMeta.class.getMethod("setMapId", Integer.TYPE);

            try {
               Method[] var10 = MapView.class.getMethods();
               int var3 = var10.length;

               for(int var4 = 0; var4 < var3; ++var4) {
                  Method method = var10[var4];
                  if (method.getName().equals("getId")) {
                     Object rawMapId = method.invoke(this.view);
                     if (rawMapId instanceof Integer) {
                        this.mapViewId = (Integer)rawMapId;
                     } else {
                        if (!(rawMapId instanceof Short)) {
                           throw new RuntimeException("Unknown map ID type " + rawMapId.getClass().getName());
                        }

                        this.mapViewId = (Short)rawMapId;
                     }
                  }
               }
            } catch (Exception var7) {
               LoginSecurity.getInstance().getLogger().log(Level.WARNING, "Failed to load captcha map", var7);
            }

            LoginSecurity.getInstance().getLogger().log(Level.INFO, "Using legacy 1.12 map captcha renderer");
         } catch (Exception var8) {
         }
      }

   }

   public void disable() {
   }

   public void giveMapItem(Player player, Runnable callback) {
      if (this.failedToLoadMapView) {
         callback.run();
      } else {
         Material itemType = Material.getMaterial("FILLED_MAP");
         if (itemType == null) {
            itemType = Material.MAP;
         }

         ItemStack item = new ItemStack(itemType, 1, (short)this.mapViewId);
         ItemMeta meta = item.getItemMeta();
         if (this.setMapViewMethod != null) {
            try {
               this.setMapViewMethod.invoke(meta, this.view);
            } catch (Exception var8) {
               LoginSecurity.getInstance().getLogger().log(Level.WARNING, "Failed to set map", var8);
            }
         } else if (this.setMapIdMethod != null) {
            try {
               this.setMapIdMethod.invoke(meta, this.mapViewId);
            } catch (Exception var7) {
               LoginSecurity.getInstance().getLogger().log(Level.WARNING, "Failed to set map", var7);
            }
         }

         meta.setDisplayName("Captcha [Enter In Chat]");
         item.setItemMeta(meta);
         MetaData.set(player, "ls_captcha_callback", callback);
         MetaData.set(player, "ls_captcha_value", this.randomCaptcha(5));
         player.setItemInHand(item);
         this.view.setCenterX(player.getLocation().getBlockX());
         this.view.setCenterZ(player.getLocation().getBlockZ());
         player.sendMap(this.view);
      }
   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent event) {
      Player player = event.getPlayer();
      MetaData.unset(player, "ls_captcha_value");
      MetaData.unset(player, "ls_captcha_callback");
      MetaData.unset(player, "ls_captcha_set");
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onPlayerChat(AsyncPlayerChatEvent event) {
      final Player player = event.getPlayer();
      if (MetaData.has(player, "ls_captcha_callback")) {
         String captcha = (String)MetaData.get(player, "ls_captcha_value", String.class);
         if (!event.getMessage().trim().equalsIgnoreCase(captcha)) {
            Bukkit.getScheduler().runTask(this.plugin, new Runnable() {
               public void run() {
                  player.kickPlayer("Wrong captcha! Please try again.");
               }
            });
         } else {
            Runnable callback = (Runnable)MetaData.get(player, "ls_captcha_callback", Runnable.class);
            MetaData.unset(player, "ls_captcha_callback");
            MetaData.unset(player, "ls_captcha_value");
            Material itemType = Material.getMaterial("FILLED_MAP");
            if (itemType == null) {
               itemType = Material.MAP;
            }

            player.getInventory().remove(itemType);
            callback.run();
         }
      }
   }

   private String randomCaptcha(int length) {
      String sheet = "ABCDEFHIJKLMNOPQRSTUVWXYZ123456789";
      StringBuilder builder = new StringBuilder();

      for(int i = 0; i < length; ++i) {
         builder.append("ABCDEFHIJKLMNOPQRSTUVWXYZ123456789".charAt(this.random.nextInt("ABCDEFHIJKLMNOPQRSTUVWXYZ123456789".length())));
      }

      return builder.toString();
   }
}
