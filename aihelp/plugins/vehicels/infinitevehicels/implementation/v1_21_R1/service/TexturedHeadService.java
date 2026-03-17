package implementation.v1_21_R1.service;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Collection;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_21_R1.block.CraftSkull;
import org.bukkit.craftbukkit.v1_21_R1.profile.CraftPlayerProfile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TexturedHeadService implements advancedplugins.pm2.cv.api.service.TexturedHeadService {
   @Nullable
   public String getTexture(@NotNull Block var1) {
      BlockState var2 = var1.getState();
      if (var2 instanceof CraftSkull) {
         CraftSkull var3 = (CraftSkull)var2;
         CraftPlayerProfile var4 = (CraftPlayerProfile)var3.getOwnerProfile();
         GameProfile var5 = var4 != null ? var4.buildGameProfile() : null;
         if (var5 == null) {
            return null;
         } else {
            Collection var6 = var5.getProperties().get("textures");
            Property var7 = var6.size() > 0 ? (Property)var6.iterator().next() : null;
            return var7 != null ? var7.value() : null;
         }
      } else {
         return null;
      }
   }

   public void applyTexture(@NotNull ItemStack var1, @NotNull String var2) {
      ItemMeta var4 = var1.getItemMeta();
      if (var4 instanceof SkullMeta) {
         SkullMeta var3 = (SkullMeta)var4;
         PlayerProfile var9 = Bukkit.createPlayerProfile(UUID.randomUUID());
         PlayerTextures var5 = var9.getTextures();
         URL var6 = null;

         try {
            var6 = getUrlFromBase64(var2);
         } catch (MalformedURLException var8) {
            throw new RuntimeException(var8);
         }

         var5.setSkin(var6);
         var9.setTextures(var5);
         var3.setOwnerProfile(var9);
         var1.setItemMeta(var3);
      }
   }

   public void applyTexture(@NotNull ItemStack var1, OfflinePlayer var2) {
      SkullMeta var3 = (SkullMeta)var1.getItemMeta();
      if (var3 != null) {
         var3.setOwnerProfile(var2.getPlayerProfile());
         var1.setItemMeta(var3);
      }
   }

   private static URL getUrlFromBase64(String var0) {
      String var1 = new String(Base64.getDecoder().decode(var0));
      return new URL(var1.substring("{\"textures\":{\"SKIN\":{\"url\":\"".length(), var1.length() - "\"}}}".length()));
   }
}
