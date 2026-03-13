package com.volmit.iris.util.plugin;

import com.volmit.iris.Iris;
import com.volmit.iris.util.collection.KList;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.bukkit.command.CommandSender;

public abstract class MortarPermission {
   private MortarPermission parent;

   public MortarPermission() {
      Field[] var1 = this.getClass().getDeclaredFields();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Field var4 = var1[var3];
         if (var4.isAnnotationPresent(Permission.class)) {
            try {
               MortarPermission var5 = (MortarPermission)var4.getType().getConstructor().newInstance();
               var5.setParent(this);
               var4.set(Modifier.isStatic(var4.getModifiers()) ? null : this, var5);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException | IllegalArgumentException var6) {
               var6.printStackTrace();
               Iris.reportError(var6);
            }
         }
      }

   }

   public KList<MortarPermission> getChildren() {
      KList var1 = new KList();
      Field[] var2 = this.getClass().getDeclaredFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field var5 = var2[var4];
         if (var5.isAnnotationPresent(Permission.class)) {
            try {
               var1.add((Object)((MortarPermission)var5.get(Modifier.isStatic(var5.getModifiers()) ? null : this)));
            } catch (IllegalAccessException | SecurityException | IllegalArgumentException var7) {
               var7.printStackTrace();
               Iris.reportError(var7);
            }
         }
      }

      return var1;
   }

   public String getFullNode() {
      if (this.hasParent()) {
         String var10000 = this.getParent().getFullNode();
         return var10000 + "." + this.getNode();
      } else {
         return this.getNode();
      }
   }

   protected abstract String getNode();

   public abstract String getDescription();

   public abstract boolean isDefault();

   public String toString() {
      return this.getFullNode();
   }

   public boolean hasParent() {
      return this.getParent() != null;
   }

   public MortarPermission getParent() {
      return this.parent;
   }

   public void setParent(MortarPermission parent) {
      this.parent = var1;
   }

   public boolean has(CommandSender sender) {
      return var1.hasPermission(this.getFullNode());
   }
}
