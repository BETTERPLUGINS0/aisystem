package com.lenis0012.bukkit.loginsecurity.util;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.storage.PlayerInventory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.logging.Level;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;

public class InventorySerializer {
   public static void deserializeInventory(PlayerInventory entry, org.bukkit.inventory.PlayerInventory inventory) {
      inventory.setHelmet((ItemStack)deserialize(entry.getHelmet()));
      inventory.setChestplate((ItemStack)deserialize(entry.getChestplate()));
      inventory.setLeggings((ItemStack)deserialize(entry.getLeggings()));
      inventory.setBoots((ItemStack)deserialize(entry.getBoots()));
      inventory.setContents((ItemStack[])deserialize(entry.getContents()));
   }

   private static Object deserialize(String item) {
      if (item == null) {
         return null;
      } else {
         BukkitObjectInputStream input = null;

         Object var3;
         try {
            byte[] bytes = Base64.getDecoder().decode(item);
            input = new BukkitObjectInputStream(new ByteArrayInputStream(bytes));
            var3 = input.readObject();
            return var3;
         } catch (Exception var13) {
            LoginSecurity.getInstance().getLogger().log(Level.SEVERE, "Failed to deserialize item", var13);
            var3 = null;
         } finally {
            if (input != null) {
               try {
                  input.close();
               } catch (IOException var12) {
               }
            }

         }

         return var3;
      }
   }
}
