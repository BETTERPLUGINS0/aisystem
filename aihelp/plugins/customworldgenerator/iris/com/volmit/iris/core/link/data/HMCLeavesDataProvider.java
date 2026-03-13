package com.volmit.iris.core.link.data;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.link.ExternalDataProvider;
import com.volmit.iris.core.link.Identifier;
import com.volmit.iris.core.nms.container.Pair;
import com.volmit.iris.core.service.ExternalDataSVC;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.data.IrisCustomData;
import com.volmit.iris.util.reflect.WrappedField;
import com.volmit.iris.util.reflect.WrappedReturningMethod;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.function.Supplier;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class HMCLeavesDataProvider extends ExternalDataProvider {
   private Object apiInstance;
   private WrappedReturningMethod<Object, Material> worldBlockType;
   private WrappedReturningMethod<Object, Boolean> setCustomBlock;
   private Map<String, Object> blockDataMap = Map.of();
   private Map<String, Supplier<ItemStack>> itemDataField = Map.of();

   public HMCLeavesDataProvider() {
      super("HMCLeaves");
   }

   public String getPluginId() {
      return "HMCLeaves";
   }

   public void init() {
      try {
         this.worldBlockType = new WrappedReturningMethod(Class.forName("io.github.fisher2911.hmcleaves.data.BlockData"), "worldBlockType", new Class[0]);
         this.apiInstance = this.getApiInstance(Class.forName("io.github.fisher2911.hmcleaves.api.HMCLeavesAPI"));
         this.setCustomBlock = new WrappedReturningMethod(this.apiInstance.getClass(), "setCustomBlock", new Class[]{Location.class, String.class, Boolean.TYPE});
         Object var1 = this.getLeavesConfig(this.apiInstance.getClass());
         this.blockDataMap = this.getMap(var1, "blockDataMap");
         this.itemDataField = this.getMap(var1, "itemSupplierMap");
      } catch (Throwable var2) {
         Iris.error("Failed to initialize HMCLeavesDataProvider: " + var2.getMessage());
      }

   }

   @NotNull
   public BlockData getBlockData(@NotNull Identifier blockId, @NotNull KMap<String, String> state) {
      Object var3 = this.blockDataMap.get(var1.key());
      if (var3 == null) {
         throw new MissingResourceException("Failed to find BlockData!", var1.namespace(), var1.key());
      } else {
         Material var4 = (Material)this.worldBlockType.invoke(var3);
         if (var4 == null) {
            throw new MissingResourceException("Failed to find BlockData!", var1.namespace(), var1.key());
         } else {
            BlockData var5 = Bukkit.createBlockData(var4);
            if (IrisSettings.get().getGenerator().preventLeafDecay && var5 instanceof Leaves) {
               Leaves var6 = (Leaves)var5;
               var6.setPersistent(true);
            }

            return IrisCustomData.of(var5, ExternalDataSVC.buildState(var1, var2));
         }
      }
   }

   @NotNull
   public ItemStack getItemStack(@NotNull Identifier itemId, @NotNull KMap<String, Object> customNbt) {
      if (!this.itemDataField.containsKey(var1.key())) {
         throw new MissingResourceException("Failed to find ItemData!", var1.namespace(), var1.key());
      } else {
         return (ItemStack)((Supplier)this.itemDataField.get(var1.key())).get();
      }
   }

   public void processUpdate(@NotNull Engine engine, @NotNull Block block, @NotNull Identifier blockId) {
      Pair var4 = ExternalDataSVC.parseState(var3);
      var3 = (Identifier)var4.getA();
      Boolean var5 = (Boolean)this.setCustomBlock.invoke(this.apiInstance, var2.getLocation(), var3.key(), false);
      if (var5 != null && var5) {
         if (IrisSettings.get().getGenerator().preventLeafDecay) {
            BlockData var6 = var2.getBlockData();
            if (var6 instanceof Leaves) {
               Leaves var7 = (Leaves)var6;
               var7.setPersistent(true);
            }
         }
      } else {
         String var10000 = var3.key();
         Iris.warn("Failed to set custom block! " + var10000 + " " + var2.getX() + " " + var2.getY() + " " + var2.getZ());
      }

   }

   @NotNull
   public Collection<Identifier> getTypes(@NotNull DataType dataType) {
      return var1 == DataType.ENTITY ? List.of() : (var1 == DataType.BLOCK ? this.blockDataMap.keySet() : this.itemDataField.keySet()).stream().map((var0) -> {
         return new Identifier("hmcleaves", var0);
      }).filter(var1.asPredicate(this)).toList();
   }

   public boolean isValidProvider(@NotNull Identifier id, DataType dataType) {
      return var2 == DataType.ENTITY ? false : (var2 == DataType.ITEM ? this.itemDataField.keySet() : this.blockDataMap.keySet()).contains(var1.key());
   }

   private <C, T> Map<String, T> getMap(C config, String name) {
      WrappedField var3 = new WrappedField(var1.getClass(), var2);
      return (Map)var3.get(var1);
   }

   private <A> A getApiInstance(Class<A> apiClass) {
      WrappedReturningMethod var2 = new WrappedReturningMethod(var1, "getInstance", new Class[0]);
      return var2.invoke();
   }

   private <A, C> C getLeavesConfig(Class<A> apiClass) {
      WrappedReturningMethod var2 = new WrappedReturningMethod(var1, "getInstance", new Class[0]);
      WrappedField var3 = new WrappedField(var1, "config");
      return var3.get(var2.invoke());
   }
}
