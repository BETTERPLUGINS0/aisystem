package advancedplugins.pm2.cv.models.core.command;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Color;

public final class ModelOptionParser {
   public Boolean hideSelfDisguise = false;
   private Boolean doDamageTint;
   private Boolean lockPitch;
   private Boolean lockYaw;
   private Boolean showHitbox;
   private Boolean showShadow;
   private Double stepHeight;
   private Double scale;
   private Double hitboxScale;
   private Integer viewRadius;
   private Color color;

   public static ModelOptionParser parse(int var0, String[] var1) {
      ModelOptionParser var2 = new ModelOptionParser();
      if (var1.length > var0) {
         String[] var3 = (String[])Arrays.copyOfRange(var1, var0, var1.length);

         for(int var4 = 0; var4 < var3.length; ++var4) {
            int var5 = getOptionId(var3[var4]);
            switch(var5) {
            case 0:
               ++var4;
               var2.doDamageTint = getNextBoolean(var3, var4);
               break;
            case 1:
               ++var4;
               var2.lockPitch = getNextBoolean(var3, var4);
               break;
            case 2:
               ++var4;
               var2.lockYaw = getNextBoolean(var3, var4);
               break;
            case 3:
               ++var4;
               var2.showHitbox = getNextBoolean(var3, var4);
               break;
            case 4:
               ++var4;
               var2.showShadow = getNextBoolean(var3, var4);
               break;
            case 5:
               ++var4;
               var2.stepHeight = getNextDouble(var3, var4);
               break;
            case 6:
               ++var4;
               var2.scale = getNextDouble(var3, var4);
               break;
            case 7:
               ++var4;
               var2.hitboxScale = getNextDouble(var3, var4);
               break;
            case 8:
               ++var4;
               var2.viewRadius = getNextInteger(var3, var4);
               break;
            case 9:
               ++var4;
               var2.hideSelfDisguise = getNextBoolean(var3, var4);
               break;
            case 10:
               ++var4;
               String var6 = getNextString(var3, var4);
               if (var6 != null && var6.startsWith("#")) {
                  var6 = var6.substring(1);
               }

               if (var6 != null) {
                  try {
                     var2.color = Color.fromRGB(Integer.parseInt(var6, 16));
                  } catch (NumberFormatException var8) {
                  }
               }
            }
         }
      }

      return var2;
   }

   private static int getOptionId(String var0) {
      String var1 = var0.toLowerCase();
      byte var2 = -1;
      byte var4 = -1;
      switch(var1.hashCode()) {
      case -1010217229:
         if (var1.equals("stepheight")) {
            var4 = 5;
         }
         break;
      case -992585865:
         if (var1.equals("viewradius")) {
            var4 = 8;
         }
         break;
      case -914587403:
         if (var1.equals("lockpitch")) {
            var4 = 1;
         }
         break;
      case 94842723:
         if (var1.equals("color")) {
            var4 = 10;
         }
         break;
      case 109250890:
         if (var1.equals("scale")) {
            var4 = 6;
         }
         break;
      case 338721124:
         if (var1.equals("lockyaw")) {
            var4 = 2;
         }
         break;
      case 651274677:
         if (var1.equals("dodamagetint")) {
            var4 = 0;
         }
         break;
      case 945827469:
         if (var1.equals("hideselfdisguise")) {
            var4 = 9;
         }
         break;
      case 1374209685:
         if (var1.equals("showhitbox")) {
            var4 = 3;
         }
         break;
      case 1687642717:
         if (var1.equals("showshadow")) {
            var4 = 4;
         }
         break;
      case 1700381426:
         if (var1.equals("hitboxscale")) {
            var4 = 7;
         }
      }

      byte var10000;
      switch(var4) {
      case 0:
         var10000 = 0;
         break;
      case 1:
         var10000 = 1;
         break;
      case 2:
         var10000 = 2;
         break;
      case 3:
         var10000 = 3;
         break;
      case 4:
         var10000 = 4;
         break;
      case 5:
         var10000 = 5;
         break;
      case 6:
         var10000 = 6;
         break;
      case 7:
         var10000 = 7;
         break;
      case 8:
         var10000 = 8;
         break;
      case 9:
         var10000 = 9;
         break;
      case 10:
         var10000 = 10;
         break;
      default:
         var10000 = var2;
      }

      var2 = var10000;
      return var2;
   }

   public static List<String> getTabCompletion(int var0, String[] var1) {
      String[] var2 = (String[])Arrays.copyOfRange(var1, var0, var1.length);
      ArrayList var3 = new ArrayList();
      if (var2.length == 1) {
         var3.addAll(Arrays.asList("doDamageTint", "lockPitch", "lockYaw", "showHitbox", "showShadow", "stepHeight", "scale", "hitboxScale", "viewRadius", "hideSelfDisguise", "color"));
      } else {
         String var4 = var2[var2.length - 2].toLowerCase();
         if (!isValueExpected(var4)) {
            var3.addAll(Arrays.asList("doDamageTint", "lockPitch", "lockYaw", "showHitbox", "showShadow", "stepHeight", "scale", "hitboxScale", "viewRadius", "hideSelfDisguise", "color"));
         }
      }

      return var3;
   }

   private static boolean isValueExpected(String var0) {
      return var0.matches("stepheight|scale|hitboxscale|viewradius|color");
   }

   private static Boolean getNextBoolean(String[] var0, int var1) {
      if (var1 >= var0.length) {
         return true;
      } else {
         String var2 = var0[var1].toLowerCase();
         return !var2.equals("false");
      }
   }

   private static Double getNextDouble(String[] var0, int var1) {
      try {
         return var1 < var0.length ? Double.parseDouble(var0[var1]) : null;
      } catch (NumberFormatException var3) {
         return null;
      }
   }

   private static Integer getNextInteger(String[] var0, int var1) {
      try {
         return var1 < var0.length ? Integer.parseInt(var0[var1]) : null;
      } catch (NumberFormatException var3) {
         return null;
      }
   }

   private static String getNextString(String[] var0, int var1) {
      return var1 < var0.length ? var0[var1] : null;
   }

   public void applyDisguiseOptions(IVisualModel var1) {
      if (this.scale != null) {
         var1.setScale(this.scale);
      }

      if (this.hitboxScale != null) {
         var1.setHitboxScale(this.hitboxScale);
      }

      if (this.doDamageTint != null) {
         var1.setCanHurt(this.doDamageTint);
      }

      if (this.lockPitch != null) {
         var1.setLockPitch(this.lockPitch);
      }

      if (this.lockYaw != null) {
         var1.setLockYaw(this.lockYaw);
      }

      if (this.showHitbox != null) {
         var1.setHitboxVisible(this.showHitbox);
      }

      if (this.showShadow != null) {
         var1.setShadowVisible(this.showShadow);
      }

      if (this.viewRadius != null) {
         var1.getModeledEntity().getBase().setRenderRadius(this.viewRadius);
      }

      if (this.stepHeight != null) {
         var1.getModeledEntity().getBase().setMaxStepHeight(this.stepHeight);
      }

      if (this.color != null) {
         var1.setDefaultTint(this.color);
      }

   }
}
