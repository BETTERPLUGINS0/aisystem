package com.volmit.iris.core.link.data;

import com.volmit.iris.core.link.ExternalDataProvider;
import com.volmit.iris.core.link.Identifier;
import com.volmit.iris.core.service.ExternalDataSVC;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.data.IrisCustomData;
import java.util.Collection;
import java.util.List;
import java.util.MissingResourceException;
import me.kryniowesegryderiusz.kgenerators.Main;
import me.kryniowesegryderiusz.kgenerators.api.KGeneratorsAPI;
import me.kryniowesegryderiusz.kgenerators.api.interfaces.IGeneratorLocation;
import me.kryniowesegryderiusz.kgenerators.api.objects.AbstractGeneratedObject;
import me.kryniowesegryderiusz.kgenerators.generators.generator.objects.Generator;
import me.kryniowesegryderiusz.kgenerators.generators.locations.objects.GeneratorLocation;
import me.kryniowesegryderiusz.kgenerators.generators.players.objects.GeneratorPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class KGeneratorsDataProvider extends ExternalDataProvider {
   public KGeneratorsDataProvider() {
      super("KGenerators");
   }

   public void init() {
   }

   @NotNull
   public BlockData getBlockData(@NotNull Identifier blockId, @NotNull KMap<String, String> state) {
      if (Main.getGenerators().get(var1.key()) == null) {
         throw new MissingResourceException("Failed to find BlockData!", var1.namespace(), var1.key());
      } else {
         return IrisCustomData.of(Material.STRUCTURE_VOID.createBlockData(), ExternalDataSVC.buildState(var1, var2));
      }
   }

   @NotNull
   public ItemStack getItemStack(@NotNull Identifier itemId, @NotNull KMap<String, Object> customNbt) {
      Generator var3 = Main.getGenerators().get(var1.key());
      if (var3 == null) {
         throw new MissingResourceException("Failed to find ItemData!", var1.namespace(), var1.key());
      } else {
         return var3.getGeneratorItem();
      }
   }

   public void processUpdate(@NotNull Engine engine, @NotNull Block block, @NotNull Identifier blockId) {
      if (var2.getType() == Material.STRUCTURE_VOID) {
         IGeneratorLocation var4 = KGeneratorsAPI.getLoadedGeneratorLocation(var2.getLocation());
         if (var4 == null) {
            var2.setBlockData(B.getAir(), false);
            Generator var5 = Main.getGenerators().get(var3.key());
            if (var5 != null) {
               GeneratorLocation var6 = new GeneratorLocation(-1, var5, var2.getLocation(), Main.getPlacedGenerators().getChunkInfo(var2.getChunk()), (GeneratorPlayer)null, (AbstractGeneratedObject)null);
               Main.getDatabases().getDb().saveGenerator(var6);
               Main.getPlacedGenerators().addLoaded(var6);
               Main.getSchedules().schedule(var6, true);
            }
         }
      }
   }

   @NotNull
   public Collection<Identifier> getTypes(@NotNull DataType dataType) {
      return var1 == DataType.ENTITY ? List.of() : Main.getGenerators().getAll().stream().map((var0) -> {
         return new Identifier("kgenerators", var0.getId());
      }).filter(var1.asPredicate(this)).toList();
   }

   public boolean isValidProvider(@NotNull Identifier id, DataType dataType) {
      return var2 == DataType.ENTITY ? false : "kgenerators".equalsIgnoreCase(var1.namespace());
   }
}
