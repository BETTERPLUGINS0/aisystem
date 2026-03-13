package com.volmit.iris.core.link.data;

import com.volmit.iris.Iris;
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
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.utils.serialize.Chroma;
import io.lumine.mythiccrucible.MythicCrucible;
import io.lumine.mythiccrucible.items.CrucibleItem;
import io.lumine.mythiccrucible.items.ItemManager;
import io.lumine.mythiccrucible.items.blocks.CustomBlockItemContext;
import io.lumine.mythiccrucible.items.furniture.FurnitureItemContext;
import java.awt.Color;
import java.util.Collection;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Optional;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MythicCrucibleDataProvider extends ExternalDataProvider {
   private ItemManager itemManager;

   public MythicCrucibleDataProvider() {
      super("MythicCrucible");
   }

   public void init() {
      Iris.info("Setting up MythicCrucible Link...");

      try {
         this.itemManager = MythicCrucible.inst().getItemManager();
      } catch (Exception var2) {
         Iris.error("Failed to set up MythicCrucible Link: Unable to fetch MythicCrucible instance!");
      }

   }

   @NotNull
   public BlockData getBlockData(@NotNull Identifier blockId, @NotNull KMap<String, String> state) {
      CrucibleItem var3 = (CrucibleItem)this.itemManager.getItem(var1.key()).orElseThrow(() -> {
         return new MissingResourceException("Failed to find BlockData!", var1.namespace(), var1.key());
      });
      CustomBlockItemContext var4 = var3.getBlockData();
      FurnitureItemContext var5 = var3.getFurnitureData();
      if (var5 != null) {
         return IrisCustomData.of(B.getAir(), ExternalDataSVC.buildState(var1, var2));
      } else if (var4 != null) {
         return var4.getBlockData();
      } else {
         throw new MissingResourceException("Failed to find BlockData!", var1.namespace(), var1.key());
      }
   }

   @NotNull
   public List<BlockProperty> getBlockProperties(@NotNull Identifier blockId) {
      CrucibleItem var2 = (CrucibleItem)this.itemManager.getItem(var1.key()).orElseThrow(() -> {
         return new MissingResourceException("Failed to find BlockData!", var1.namespace(), var1.key());
      });
      if (var2.getFurnitureData() != null) {
         return YAW_FACE_BIOME_PROPERTIES;
      } else if (var2.getBlockData() != null) {
         return List.of();
      } else {
         throw new MissingResourceException("Failed to find BlockData!", var1.namespace(), var1.key());
      }
   }

   @NotNull
   public ItemStack getItemStack(@NotNull Identifier itemId, @NotNull KMap<String, Object> customNbt) {
      Optional var3 = this.itemManager.getItem(var1.key());
      return BukkitAdapter.adapt(((CrucibleItem)var3.orElseThrow(() -> {
         return new MissingResourceException("Failed to find ItemData!", var1.namespace(), var1.key());
      })).getMythicItem().generateItemStack(1));
   }

   @NotNull
   public Collection<Identifier> getTypes(@NotNull DataType dataType) {
      return this.itemManager.getItems().stream().map((var0) -> {
         return new Identifier("crucible", var0.getInternalName());
      }).filter(var1.asPredicate(this)).toList();
   }

   public void processUpdate(@NotNull Engine engine, @NotNull Block block, @NotNull Identifier blockId) {
      Pair var4 = ExternalDataSVC.parseState(var3);
      KMap var5 = (KMap)var4.getB();
      var3 = (Identifier)var4.getA();
      Optional var6 = this.itemManager.getItem(var3.key());
      if (!var6.isEmpty()) {
         FurnitureItemContext var7 = ((CrucibleItem)var6.get()).getFurnitureData();
         if (var7 != null) {
            Pair var8 = parseYawAndFace(var1, var2, var5);
            BiomeColor var9 = null;
            Chroma var10 = null;

            try {
               var9 = BiomeColor.valueOf(((String)var5.get("matchBiome")).toUpperCase());
            } catch (IllegalArgumentException | NullPointerException var12) {
            }

            if (var9 != null) {
               Color var11 = INMS.get().getBiomeColor(var2.getLocation(), var9);
               if (var11 == null) {
                  return;
               }

               var10 = Chroma.of(var11.getRGB());
            }

            var7.place(var2, (BlockFace)var8.getB(), (Float)var8.getA(), var10);
         }
      }
   }

   public boolean isValidProvider(@NotNull Identifier key, DataType dataType) {
      return var2 == DataType.ENTITY ? false : var1.namespace().equalsIgnoreCase("crucible");
   }
}
