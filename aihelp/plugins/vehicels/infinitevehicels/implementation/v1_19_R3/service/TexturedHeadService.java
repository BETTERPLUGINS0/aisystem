package implementation.v1_19_R3.service;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.Collection;
import java.util.UUID;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_19_R3.block.CraftSkull;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_19_R3.profile.CraftPlayerProfile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TexturedHeadService implements advancedplugins.pm2.cv.api.service.TexturedHeadService {
   @Nullable
   public String getTexture(@NotNull Block var1) {
      BlockState var2 = var1.getState();
      if (!(var2 instanceof CraftSkull)) {
         return null;
      } else {
         CraftSkull var3 = (CraftSkull)var2;
         CraftPlayerProfile var4 = (CraftPlayerProfile)var3.getOwnerProfile();
         GameProfile var5 = var4 != null ? var4.buildGameProfile() : null;
         if (var5 == null) {
            return null;
         } else {
            Collection var6 = var5.getProperties().get("textures");
            Property var7 = var6.size() > 0 ? (Property)var6.iterator().next() : null;
            return var7 != null ? var7.getValue() : null;
         }
      }
   }

   public void applyTexture(@NotNull ItemStack var1, @NotNull String var2) {
      if (var1.getItemMeta() instanceof SkullMeta) {
         net.minecraft.world.item.ItemStack var3 = CraftItemStack.asNMSCopy(var1);
         NBTTagCompound var4 = var3.v();
         NBTTagCompound var5 = new NBTTagCompound();
         var5.a("Value", var2);
         NBTTagList var6 = new NBTTagList();
         var6.add(var5);
         NBTTagCompound var7 = new NBTTagCompound();
         var7.a("textures", var6);
         NBTTagCompound var8 = var4.p("SkullOwner");
         var8.a("Id", UUID.randomUUID());
         var8.a("Properties", var7);
         var4.a("SkullOwner", var8);
         var1.setItemMeta(CraftItemStack.asBukkitCopy(var3).getItemMeta());
      }
   }

   public void applyTexture(@NotNull ItemStack var1, OfflinePlayer var2) {
      SkullMeta var3 = (SkullMeta)var1.getItemMeta();
      if (var3 != null) {
         var3.setOwnerProfile(var2.getPlayerProfile());
         var1.setItemMeta(var3);
      }
   }
}
