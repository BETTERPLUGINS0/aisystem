package com.volmit.iris.util.plugin;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.format.C;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Iterator;
import org.bukkit.Sound;

public abstract class MortarCommand implements ICommand {
   private final KList<MortarCommand> children;
   private final KList<String> nodes;
   private final KList<String> requiredPermissions;
   private final String node;
   private String category = "";
   private String description;

   public MortarCommand(String node, String... nodes) {
      this.node = var1;
      this.nodes = new KList(var2);
      this.requiredPermissions = new KList();
      this.children = this.buildChildren();
      this.description = "No Description";
   }

   public KList<String> handleTab(VolmitSender sender, String[] args) {
      KList var3 = new KList();
      if (var2.length == 0) {
         Iterator var4 = this.getChildren().iterator();

         while(var4.hasNext()) {
            MortarCommand var5 = (MortarCommand)var4.next();
            var3.add((Object)var5.getNode());
         }
      }

      this.addTabOptions(var1, var2, var3);
      if (var3.isEmpty()) {
         return null;
      } else {
         if (var1.isPlayer() && IrisSettings.get().getGeneral().isCommandSounds()) {
            var1.playSound(Sound.ENTITY_ITEM_FRAME_ROTATE_ITEM, 0.25F, 1.7F);
         }

         return var3;
      }
   }

   public abstract void addTabOptions(VolmitSender sender, String[] args, KList<String> list);

   public void printHelp(VolmitSender sender) {
      boolean var2 = false;
      Iterator var3 = this.getChildren().iterator();

      while(var3.hasNext()) {
         MortarCommand var4 = (MortarCommand)var3.next();
         Iterator var5 = var4.getRequiredPermissions().iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            if (!var1.hasPermission(var6)) {
            }
         }

         var2 = true;
         String var10001 = String.valueOf(C.GREEN);
         var1.sendMessage(var10001 + var4.getNode() + " <font:minecraft:uniform>" + (this.getArgsUsage().trim().isEmpty() ? "" : String.valueOf(C.WHITE) + var4.getArgsUsage()) + String.valueOf(C.GRAY) + " - " + var4.getDescription());
      }

      if (!var2) {
         var1.sendMessage("There are either no sub-commands or you do not have permission to use them.");
      }

      if (var1.isPlayer() && IrisSettings.get().getGeneral().isCommandSounds()) {
         var1.playSound(Sound.ITEM_BOOK_PAGE_TURN, 0.28F, 1.4F);
         var1.playSound(Sound.ITEM_AXE_STRIP, 0.35F, 1.7F);
      }

   }

   protected abstract String getArgsUsage();

   public String getDescription() {
      return this.description;
   }

   protected void setDescription(String description) {
      this.description = var1;
   }

   protected void requiresPermission(MortarPermission node) {
      if (var1 != null) {
         this.requiresPermission(var1.toString());
      }
   }

   protected void requiresPermission(String node) {
      if (var1 != null) {
         this.requiredPermissions.add((Object)var1);
      }
   }

   public void rejectAny(int past, VolmitSender sender, String[] a) {
      if (var3.length > var1) {
         int var4 = var1;
         StringBuilder var5 = new StringBuilder();
         String[] var6 = var3;
         int var7 = var3.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String var9 = var6[var8];
            --var4;
            if (var4 < 0) {
               var5.append(var9).append(", ");
            }
         }

         if (!var5.toString().trim().isEmpty()) {
            var2.sendMessage("Parameters Ignored: " + String.valueOf(var5));
         }
      }

   }

   public String getNode() {
      return this.node;
   }

   public KList<String> getNodes() {
      return this.nodes;
   }

   public KList<String> getAllNodes() {
      return this.getNodes().copy().qadd(this.getNode());
   }

   public void addNode(String node) {
      this.getNodes().add((Object)var1);
   }

   public KList<MortarCommand> getChildren() {
      return this.children;
   }

   private KList<MortarCommand> buildChildren() {
      KList var1 = new KList();
      Field[] var2 = this.getClass().getDeclaredFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field var5 = var2[var4];
         if (var5.isAnnotationPresent(Command.class)) {
            try {
               var5.setAccessible(true);
               MortarCommand var6 = (MortarCommand)var5.getType().getConstructor().newInstance();
               Command var7 = (Command)var5.getAnnotation(Command.class);
               if (!var7.value().trim().isEmpty()) {
                  var6.setCategory(var7.value().trim());
               } else {
                  var6.setCategory(this.getCategory());
               }

               var1.add((Object)var6);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException | IllegalArgumentException var8) {
               Iris.reportError(var8);
               var8.printStackTrace();
            }
         }
      }

      var1.sort(Comparator.comparing(MortarCommand::getNode));
      return var1;
   }

   public KList<String> getRequiredPermissions() {
      return this.requiredPermissions;
   }

   public String getCategory() {
      return this.category;
   }

   public void setCategory(String category) {
      this.category = var1;
   }
}
