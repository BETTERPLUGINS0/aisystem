package com.volmit.iris.core.link;

import com.volmit.iris.core.link.data.DataType;
import com.volmit.iris.core.nms.container.BiomeColor;
import com.volmit.iris.core.nms.container.BlockProperty;
import com.volmit.iris.core.nms.container.Pair;
import com.volmit.iris.engine.data.cache.Cache;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.math.RNG;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.MissingResourceException;
import lombok.Generated;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ExternalDataProvider implements Listener {
   @NonNull
   private final String pluginId;
   protected static List<BlockProperty> YAW_FACE_BIOME_PROPERTIES;

   @Nullable
   public Plugin getPlugin() {
      return Bukkit.getPluginManager().getPlugin(this.pluginId);
   }

   public boolean isReady() {
      return this.getPlugin() != null && this.getPlugin().isEnabled();
   }

   public abstract void init();

   @NotNull
   public BlockData getBlockData(@NotNull Identifier blockId) {
      return this.getBlockData(var1, new KMap());
   }

   @NotNull
   public BlockData getBlockData(@NotNull Identifier blockId, @NotNull KMap<String, String> state) {
      throw new MissingResourceException("Failed to find BlockData!", var1.namespace(), var1.key());
   }

   @NotNull
   public List<BlockProperty> getBlockProperties(@NotNull Identifier blockId) {
      return List.of();
   }

   @NotNull
   public ItemStack getItemStack(@NotNull Identifier itemId) {
      return this.getItemStack(var1, new KMap());
   }

   @NotNull
   public ItemStack getItemStack(@NotNull Identifier itemId, @NotNull KMap<String, Object> customNbt) {
      throw new MissingResourceException("Failed to find ItemData!", var1.namespace(), var1.key());
   }

   public void processUpdate(@NotNull Engine engine, @NotNull Block block, @NotNull Identifier blockId) {
   }

   @Nullable
   public Entity spawnMob(@NotNull Location location, @NotNull Identifier entityId) {
      throw new MissingResourceException("Failed to find Entity!", var2.namespace(), var2.key());
   }

   @NotNull
   public abstract Collection<Identifier> getTypes(@NotNull DataType dataType);

   public abstract boolean isValidProvider(@NotNull Identifier id, DataType dataType);

   protected static Pair<Float, BlockFace> parseYawAndFace(@NotNull Engine engine, @NotNull Block block, @NotNull KMap<String, String> state) {
      float var3 = 0.0F;
      BlockFace var4 = BlockFace.NORTH;
      long var5 = var0.getSeedManager().getSeed() + Cache.key(var1.getX(), var1.getZ()) + (long)var1.getY();
      RNG var7 = new RNG(var5);
      if ("true".equals(var2.get("randomYaw"))) {
         var3 = var7.f(0.0F, 360.0F);
      } else if (var2.containsKey("yaw")) {
         var3 = Float.parseFloat((String)var2.get("yaw"));
      }

      if ("true".equals(var2.get("randomFace"))) {
         BlockFace[] var8 = BlockFace.values();
         var4 = var8[var7.i(0, var8.length - 1)];
      } else if (var2.containsKey("face")) {
         var4 = BlockFace.valueOf(((String)var2.get("face")).toUpperCase());
      }

      if (var4 == BlockFace.SELF) {
         var4 = BlockFace.NORTH;
      }

      return new Pair(var3, var4);
   }

   @NonNull
   @Generated
   public String getPluginId() {
      return this.pluginId;
   }

   @Generated
   public ExternalDataProvider(@NonNull final String pluginId) {
      if (var1 == null) {
         throw new NullPointerException("pluginId is marked non-null but is null");
      } else {
         this.pluginId = var1;
      }
   }

   static {
      YAW_FACE_BIOME_PROPERTIES = List.of(BlockProperty.ofEnum(BiomeColor.class, "matchBiome", (Enum)null), BlockProperty.ofBoolean("randomYaw", false), BlockProperty.ofFloat("yaw", 0.0F, 0.0F, 360.0F, false, true), BlockProperty.ofBoolean("randomFace", true), new BlockProperty("face", BlockFace.class, BlockFace.NORTH, Arrays.asList(BlockFace.values()).subList(0, BlockFace.values().length - 1), Enum::name));
   }
}
