package com.volmit.iris.core.nms;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.nms.v1X.NMSBinding1X;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;

public class INMS {
   private static final INMS.Version CURRENT = Boolean.getBoolean("iris.no-version-limit") ? new INMS.Version(Integer.MAX_VALUE, Integer.MAX_VALUE, (String)null) : new INMS.Version(21, 11, (String)null);
   private static final List<INMS.Version> REVISION = List.of(new INMS.Version(21, 11, "v1_21_R7"), new INMS.Version(21, 9, "v1_21_R6"), new INMS.Version(21, 6, "v1_21_R5"), new INMS.Version(21, 5, "v1_21_R4"), new INMS.Version(21, 4, "v1_21_R3"), new INMS.Version(21, 2, "v1_21_R2"), new INMS.Version(21, 0, "v1_21_R1"), new INMS.Version(20, 5, "v1_20_R4"));
   private static final List<INMS.Version> PACKS = List.of(new INMS.Version(21, 5, "31100"), new INMS.Version(21, 4, "31020"), new INMS.Version(21, 2, "31000"), new INMS.Version(20, 1, "3910"));
   private static final INMSBinding binding = bind();
   public static final String OVERWORLD_TAG;

   public static INMSBinding get() {
      return binding;
   }

   public static String getNMSTag() {
      if (IrisSettings.get().getGeneral().isDisableNMS()) {
         return "BUKKIT";
      } else {
         try {
            String var0 = Bukkit.getServer().getClass().getCanonicalName();
            return var0.equals("org.bukkit.craftbukkit.CraftServer") ? getTag(REVISION, "BUKKIT") : var0.split("\\Q.\\E")[3];
         } catch (Throwable var1) {
            Iris.reportError(var1);
            Iris.error("Failed to determine server nms version!");
            var1.printStackTrace();
            return "BUKKIT";
         }
      }
   }

   private static INMSBinding bind() {
      String var0 = getNMSTag();
      Iris.info("Locating NMS Binding for " + var0);

      try {
         Class var1 = Class.forName("com.volmit.iris.core.nms." + var0 + ".NMSBinding");

         try {
            Object var2 = var1.getConstructor().newInstance();
            if (var2 instanceof INMSBinding) {
               INMSBinding var3 = (INMSBinding)var2;
               Iris.info("Craftbukkit " + var0 + " <-> " + var2.getClass().getSimpleName() + " Successfully Bound");
               return var3;
            }
         } catch (Throwable var4) {
            Iris.reportError(var4);
            var4.printStackTrace();
         }
      } catch (NoClassDefFoundError | ClassNotFoundException var5) {
      }

      Iris.info("Craftbukkit " + var0 + " <-> " + NMSBinding1X.class.getSimpleName() + " Successfully Bound");
      Iris.warn("Note: Some features of Iris may not work the same since you are on an unsupported version of Minecraft.");
      Iris.warn("Note: If this is a new version, expect an update soon.");
      return new NMSBinding1X();
   }

   private static String getTag(List<INMS.Version> versions, String def) {
      String[] var2 = Bukkit.getServer().getBukkitVersion().split("-")[0].split("\\.", 3);
      int var3 = 0;
      int var4 = 0;
      if (var2.length > 2) {
         var3 = Integer.parseInt(var2[1]);
         var4 = Integer.parseInt(var2[2]);
      } else if (var2.length == 2) {
         var3 = Integer.parseInt(var2[1]);
      }

      if (CURRENT.major >= var3 && CURRENT.minor >= var4) {
         Iterator var5 = var0.iterator();

         INMS.Version var6;
         do {
            if (!var5.hasNext()) {
               return var1;
            }

            var6 = (INMS.Version)var5.next();
         } while(var6.major > var3 || var6.minor > var4);

         return var6.tag;
      } else {
         return ((INMS.Version)var0.getFirst()).tag;
      }
   }

   static {
      OVERWORLD_TAG = getTag(PACKS, "3910");
   }

   private static record Version(int major, int minor, String tag) {
      private Version(int major, int minor, String tag) {
         this.major = var1;
         this.minor = var2;
         this.tag = var3;
      }

      public int major() {
         return this.major;
      }

      public int minor() {
         return this.minor;
      }

      public String tag() {
         return this.tag;
      }
   }
}
