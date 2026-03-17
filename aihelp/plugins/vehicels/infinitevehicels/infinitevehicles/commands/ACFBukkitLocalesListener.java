package me.PM2.infinitevehicles.commands;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.Locale;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLocaleChangeEvent;

class ACFBukkitLocalesListener implements Listener {
   private final BukkitCommandManager manager;
   private MethodHandle localeMethod1_8 = null;
   private boolean checkedLocaleMethod1_8 = false;

   ACFBukkitLocalesListener(BukkitCommandManager manager) {
      this.manager = var1;
   }

   @EventHandler
   void onLocaleChange(PlayerLocaleChangeEvent event) {
      if (this.manager.autoDetectFromClient) {
         Player var2 = var1.getPlayer();
         Locale var3 = null;

         try {
            var3 = var1.locale();
         } catch (NoSuchMethodError var10) {
            try {
               if (!var1.getLocale().equals(this.manager.issuersLocaleString.get(var2.getUniqueId()))) {
                  var3 = ACFBukkitUtil.stringToLocale(var1.getLocale());
               }
            } catch (NoSuchMethodError var9) {
               try {
                  if (!this.checkedLocaleMethod1_8) {
                     this.checkedLocaleMethod1_8 = true;
                     Lookup var6 = MethodHandles.lookup();
                     MethodType var7 = MethodType.methodType(String.class);
                     this.localeMethod1_8 = var6.findVirtual(PlayerLocaleChangeEvent.class, "getNewLocale", var7);
                  }

                  if (this.localeMethod1_8 != null) {
                     String var11 = this.localeMethod1_8.invoke(var1);
                     if (!var11.equals(this.manager.issuersLocaleString.get(var2.getUniqueId()))) {
                        var3 = ACFBukkitUtil.stringToLocale(var11);
                     }
                  }
               } catch (Throwable var8) {
                  this.manager.log(LogLevel.ERROR, "Error registering MethodHandle for LocaleChangeEvent", var8);
               }
            }
         }

         if (var3 != null) {
            this.manager.setPlayerLocale(var2, var3);
         }
      }
   }
}
