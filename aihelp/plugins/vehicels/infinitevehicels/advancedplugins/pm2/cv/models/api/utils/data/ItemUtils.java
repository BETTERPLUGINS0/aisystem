package advancedplugins.pm2.cv.models.api.utils.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Iterator;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Consumer;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class ItemUtils {
   public static byte[] encodeItemStack(ItemStack var0) {
      try {
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();

         byte[] var2;
         try {
            BukkitObjectOutputStream var3 = new BukkitObjectOutputStream(var1);

            try {
               var3.writeObject(var0);
               var2 = var1.toByteArray();
            } catch (Throwable var8) {
               try {
                  var3.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }

               throw var8;
            }

            var3.close();
         } catch (Throwable var9) {
            try {
               var1.close();
            } catch (Throwable var6) {
               var9.addSuppressed(var6);
            }

            throw var9;
         }

         var1.close();
         return var2;
      } catch (IOException var10) {
         throw new RuntimeException(var10);
      }
   }

   public static String encodeItemStackToString(ItemStack var0) {
      return encode(encodeItemStack(var0));
   }

   public static ItemStack decodeItemStack(byte[] var0) {
      try {
         ByteArrayInputStream var1 = new ByteArrayInputStream(var0);

         ItemStack var2;
         try {
            BukkitObjectInputStream var3 = new BukkitObjectInputStream(var1);

            try {
               var2 = (ItemStack)var3.readObject();
            } catch (Throwable var8) {
               try {
                  var3.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }

               throw var8;
            }

            var3.close();
         } catch (Throwable var9) {
            try {
               var1.close();
            } catch (Throwable var6) {
               var9.addSuppressed(var6);
            }

            throw var9;
         }

         var1.close();
         return var2;
      } catch (IOException | ClassNotFoundException var10) {
         throw new RuntimeException(var10);
      }
   }

   public static ItemStack decodeItemStack(String var0) {
      return decodeItemStack(decode(var0));
   }

   public static String encode(byte[] var0) {
      return Base64.getEncoder().encodeToString(var0);
   }

   public static byte[] decode(String var0) {
      try {
         return Base64.getDecoder().decode(var0);
      } catch (IllegalArgumentException var4) {
         try {
            return Base64Coder.decodeLines(var0);
         } catch (Exception var3) {
            throw var4;
         }
      }
   }

   public static void name(ItemStack var0, Component var1) {
      meta(var0, (var1x) -> {
         var1x.setDisplayNameComponent(ComponentUtil.base(var1));
      });
   }

   public static void lore(ItemStack var0, Component... var1) {
      ArrayList var2 = new ArrayList();
      Component[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Component var6 = var3[var5];
         if (var6 != null) {
            var2.add(ComponentUtil.base(var6));
         }
      }

      meta(var0, (var1x) -> {
         var1x.setLoreComponents(var2);
      });
   }

   public static void lore(ItemStack var0, Collection<Component> var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Component var4 = (Component)var3.next();
         if (var4 != null) {
            var2.add(ComponentUtil.base(var4));
         }
      }

      meta(var0, (var1x) -> {
         var1x.setLoreComponents(var2);
      });
   }

   public static <T extends ItemMeta> void meta(ItemStack var0, Consumer<T> var1) {
      try {
         ItemMeta var2 = var0.getItemMeta();
         var1.accept(var2);
         var0.setItemMeta(var2);
      } catch (ClassCastException var3) {
         var3.printStackTrace();
      }

   }
}
