package com.volmit.iris.core.link.data;

import com.nexomc.nexo.api.NexoBlocks;
import com.nexomc.nexo.api.NexoFurniture;
import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import com.volmit.iris.core.link.ExternalDataProvider;
import com.volmit.iris.core.link.Identifier;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.core.nms.container.BiomeColor;
import com.volmit.iris.core.nms.container.BlockProperty;
import com.volmit.iris.core.nms.container.Pair;
import com.volmit.iris.core.service.ExternalDataSVC;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.data.IrisCustomData;
import java.awt.Color;
import java.util.Collection;
import java.util.List;
import java.util.MissingResourceException;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.NotNull;

public class NexoDataProvider extends ExternalDataProvider {
   public NexoDataProvider() {
      super("Nexo");
   }

   public void init() {
   }

   @NotNull
   public BlockData getBlockData(@NotNull Identifier blockId, @NotNull KMap<String, String> state) {
      if (!NexoItems.exists(var1.key())) {
         throw new MissingResourceException("Failed to find BlockData!", var1.namespace(), var1.key());
      } else {
         Identifier var3 = ExternalDataSVC.buildState(var1, var2);
         if (NexoBlocks.isCustomBlock(var1.key())) {
            BlockData var4 = NexoBlocks.blockData(var1.key());
            if (var4 == null) {
               throw new MissingResourceException("Failed to find BlockData!", var1.namespace(), var1.key());
            } else {
               return IrisCustomData.of(var4, var3);
            }
         } else if (NexoFurniture.isFurniture(var1.key())) {
            return IrisCustomData.of(B.getAir(), var3);
         } else {
            throw new MissingResourceException("Failed to find BlockData!", var1.namespace(), var1.key());
         }
      }
   }

   @NotNull
   public List<BlockProperty> getBlockProperties(@NotNull Identifier blockId) {
      if (!NexoItems.exists(var1.key())) {
         throw new MissingResourceException("Failed to find BlockData!", var1.namespace(), var1.key());
      } else {
         return NexoFurniture.isFurniture(var1.key()) ? YAW_FACE_BIOME_PROPERTIES : List.of();
      }
   }

   @NotNull
   public ItemStack getItemStack(@NotNull Identifier itemId, @NotNull KMap<String, Object> customNbt) {
      ItemBuilder var3 = NexoItems.itemFromId(var1.key());
      if (var3 == null) {
         throw new MissingResourceException("Failed to find ItemData!", var1.namespace(), var1.key());
      } else {
         try {
            return var3.build();
         } catch (Exception var5) {
            var5.printStackTrace();
            throw new MissingResourceException("Failed to find ItemData!", var1.namespace(), var1.key());
         }
      }
   }

   public void processUpdate(@NotNull Engine engine, @NotNull Block block, @NotNull Identifier blockId) {
      Pair var4 = ExternalDataSVC.parseState(var3);
      KMap var5 = (KMap)var4.getB();
      var3 = (Identifier)var4.getA();
      if (NexoBlocks.isCustomBlock(var3.key())) {
         NexoBlocks.place(var3.key(), var2.getLocation());
      } else if (NexoFurniture.isFurniture(var3.key())) {
         Pair var6 = parseYawAndFace(var1, var2, var5);
         ItemDisplay var7 = NexoFurniture.place(var3.key(), var2.getLocation(), (Float)var6.getA(), (BlockFace)var6.getB());
         if (var7 != null) {
            ItemStack var8 = var7.getItemStack();
            if (var8 != null) {
               BiomeColor var9 = null;

               try {
                  var9 = BiomeColor.valueOf(((String)var5.get("matchBiome")).toUpperCase());
               } catch (IllegalArgumentException | NullPointerException var18) {
               }

               if (var9 != null) {
                  Color var10 = INMS.get().getBiomeColor(var2.getLocation(), var9);
                  if (var10 == null) {
                     return;
                  }

                  org.bukkit.Color var11 = org.bukkit.Color.fromARGB(var10.getAlpha(), var10.getRed(), var10.getGreen(), var10.getBlue());
                  ItemMeta var12 = var8.getItemMeta();
                  byte var14 = 0;
                  switch(var12.typeSwitch<invokedynamic>(var12, var14)) {
                  case -1:
                  default:
                     break;
                  case 0:
                     LeatherArmorMeta var15 = (LeatherArmorMeta)var12;
                     var15.setColor(var11);
                     break;
                  case 1:
                     PotionMeta var16 = (PotionMeta)var12;
                     var16.setColor(var11);
                     break;
                  case 2:
                     MapMeta var17 = (MapMeta)var12;
                     var17.setColor(var11);
                  }

                  var8.setItemMeta(var12);
               }

               var7.setItemStack(var8);
            }
         }
      }
   }

   @NotNull
   public Collection<Identifier> getTypes(@NotNull DataType dataType) {
      return var1 == DataType.ENTITY ? List.of() : NexoItems.itemNames().stream().map((var0) -> {
         return new Identifier("nexo", var0);
      }).filter(var1.asPredicate(this)).toList();
   }

   public boolean isValidProvider(@NotNull Identifier id, DataType dataType) {
      return var2 == DataType.ENTITY ? false : "nexo".equalsIgnoreCase(var1.namespace());
   }
}
