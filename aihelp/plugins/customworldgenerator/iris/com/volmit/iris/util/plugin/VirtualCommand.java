package com.volmit.iris.util.plugin;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.reflect.V;
import java.lang.reflect.Field;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;

public class VirtualCommand {
   private final ICommand command;
   private final String tag;
   private final KMap<KList<String>, VirtualCommand> children;

   public VirtualCommand(ICommand command) {
      this(var1, "");
   }

   public VirtualCommand(ICommand command, String tag) {
      this.command = var1;
      this.children = new KMap();
      this.tag = var2;
      Field[] var3 = var1.getClass().getDeclaredFields();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field var6 = var3[var5];
         if (var6.isAnnotationPresent(Command.class)) {
            try {
               Command var7 = (Command)var6.getAnnotation(Command.class);
               ICommand var8 = (ICommand)var6.getType().getConstructor().newInstance();
               (new V(var1, true, true)).set(var6.getName(), var8);
               this.children.put(var8.getAllNodes(), new VirtualCommand(var8, var7.value().trim().isEmpty() ? var2 : var7.value().trim()));
            } catch (Exception var9) {
               Iris.reportError(var9);
               var9.printStackTrace();
            }
         }
      }

   }

   public String getTag() {
      return this.tag;
   }

   public ICommand getCommand() {
      return this.command;
   }

   public KMap<KList<String>, VirtualCommand> getChildren() {
      return this.children;
   }

   public boolean hit(CommandSender sender, KList<String> chain) {
      return this.hit(var1, var2, (String)null);
   }

   public boolean hit(CommandSender sender, KList<String> chain, String label) {
      VolmitSender var4 = new VolmitSender(var1);
      var4.setTag(this.tag);
      if (var3 != null) {
         var4.setCommand(var3);
      }

      if (var2.isEmpty()) {
         return !this.checkPermissions(var1, this.command) ? true : this.command.handle(var4, new String[0]);
      } else {
         String var5 = (String)var2.get(0);
         Iterator var6 = this.children.k().iterator();

         while(var6.hasNext()) {
            KList var7 = (KList)var6.next();
            Iterator var8 = var7.iterator();

            while(var8.hasNext()) {
               String var9 = (String)var8.next();
               if (var9.equalsIgnoreCase(var5)) {
                  var4.setCommand((String)var2.get(0));
                  VirtualCommand var10 = (VirtualCommand)this.children.get(var7);
                  KList var11 = var2.copy();
                  var11.remove(0);
                  if (var10.hit(var1, var11, var4.getCommand())) {
                     if (var4.isPlayer() && IrisSettings.get().getGeneral().isCommandSounds()) {
                        var4.player().getWorld().playSound(var4.player().getLocation(), Sound.ITEM_AXE_STRIP, 0.35F, 1.8F);
                     }

                     return true;
                  }
               }
            }
         }

         if (!this.checkPermissions(var1, this.command)) {
            return true;
         } else {
            return this.command.handle(var4, (String[])var2.toArray(new String[0]));
         }
      }
   }

   public KList<String> hitTab(CommandSender sender, KList<String> chain, String label) {
      VolmitSender var4 = new VolmitSender(var1);
      var4.setTag(this.tag);
      if (var3 != null) {
         var4.setCommand(var3);
      }

      if (var2.isEmpty()) {
         return !this.checkPermissions(var1, this.command) ? null : this.command.handleTab(var4, new String[0]);
      } else {
         String var5 = (String)var2.get(0);
         Iterator var6 = this.children.k().iterator();

         while(var6.hasNext()) {
            KList var7 = (KList)var6.next();
            Iterator var8 = var7.iterator();

            while(var8.hasNext()) {
               String var9 = (String)var8.next();
               if (var9.equalsIgnoreCase(var5)) {
                  var4.setCommand((String)var2.get(0));
                  VirtualCommand var10 = (VirtualCommand)this.children.get(var7);
                  KList var11 = var2.copy();
                  var11.remove(0);
                  KList var12 = var10.hitTab(var1, var11, var4.getCommand());
                  if (var12 != null) {
                     return var12;
                  }
               }
            }
         }

         if (!this.checkPermissions(var1, this.command)) {
            return null;
         } else {
            return this.command.handleTab(var4, (String[])var2.toArray(new String[0]));
         }
      }
   }

   private boolean checkPermissions(CommandSender sender, ICommand command2) {
      boolean var3 = false;
      Iterator var4 = this.command.getRequiredPermissions().iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         if (!var1.hasPermission(var5)) {
            var3 = true;
            Bukkit.getScheduler().scheduleSyncDelayedTask(Iris.instance, () -> {
               String var10001 = String.valueOf(C.WHITE);
               var1.sendMessage("- " + var10001 + var5);
            }, 0L);
         }
      }

      if (var3) {
         var1.sendMessage("Insufficient Permissions");
         return false;
      } else {
         return true;
      }
   }
}
