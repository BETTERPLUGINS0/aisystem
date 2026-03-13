package com.volmit.iris.engine.object;

import com.volmit.iris.core.nms.container.Pair;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.scheduling.J;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.Generated;
import lombok.NonNull;
import org.apache.commons.io.function.IOFunction;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Tag;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Sign;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LegacyTileData extends TileData {
   private static final Map<Integer, Pair<LegacyTileData.Builder, IOFunction<DataInputStream, LegacyTileData.Handler>>> legacy = Map.of(0, new Pair(LegacyTileData.SignHandler::fromBukkit, LegacyTileData.SignHandler::new), 1, new Pair(LegacyTileData.SpawnerHandler::fromBukkit, LegacyTileData.SpawnerHandler::new), 2, new Pair(LegacyTileData.BannerHandler::fromBukkit, LegacyTileData.BannerHandler::new));
   private static final AtomicCache<Tag<Material>> SIGNS = new AtomicCache();
   private final int id;
   private final LegacyTileData.Handler handler;

   public LegacyTileData(DataInputStream in) {
      this.id = var1.readShort();
      Pair var2 = (Pair)legacy.get(this.id);
      if (var2 == null) {
         throw new IOException("Unknown tile type: " + this.id);
      } else {
         this.handler = (LegacyTileData.Handler)((IOFunction)var2.getB()).apply(var1);
      }
   }

   private LegacyTileData(int id, LegacyTileData.Handler handler) {
      this.id = var1;
      this.handler = var2;
   }

   @Nullable
   public static LegacyTileData fromBukkit(@NonNull BlockState tileState) {
      if (var0 == null) {
         throw new NullPointerException("tileState is marked non-null but is null");
      } else {
         Material var1 = var0.getType();
         Iterator var2 = legacy.keySet().iterator();

         Integer var3;
         LegacyTileData.Handler var5;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            var3 = (Integer)var2.next();
            Pair var4 = (Pair)legacy.get(var3);
            var5 = ((LegacyTileData.Builder)var4.getA()).apply(var0, var1);
         } while(var5 == null);

         return new LegacyTileData(var3, var5);
      }
   }

   @NonNull
   public KMap<String, Object> getProperties() {
      return new KMap();
   }

   @NonNull
   public Material getMaterial() {
      return this.handler.getMaterial();
   }

   public boolean isApplicable(BlockData data) {
      return this.handler.isApplicable(var1);
   }

   public void toBukkit(Block block) {
      J.s(() -> {
         this.handler.toBukkit(var1);
      });
   }

   public void toBinary(DataOutputStream out) {
      var1.writeShort(this.id);
      this.handler.toBinary(var1);
   }

   public TileData clone() {
      return this;
   }

   private static Tag<Material> signsTag() {
      return (Tag)SIGNS.aquire(() -> {
         Tag var0 = Bukkit.getTag("blocks", NamespacedKey.minecraft("all_signs"), Material.class);
         return var0 != null ? var0 : new Tag<Material>() {
            public boolean isTagged(@NotNull Material item) {
               return var1.getKey().getKey().endsWith("_sign");
            }

            @NotNull
            public Set<Material> getValues() {
               return (Set)StreamSupport.stream(Registry.MATERIAL.spliterator(), false).filter(this::isTagged).collect(Collectors.toUnmodifiableSet());
            }

            @NotNull
            public NamespacedKey getKey() {
               return NamespacedKey.minecraft("all_signs");
            }
         };
      });
   }

   @Generated
   public String toString() {
      int var10000 = this.id;
      return "LegacyTileData(id=" + var10000 + ", handler=" + String.valueOf(this.handler) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof LegacyTileData)) {
         return false;
      } else {
         LegacyTileData var2 = (LegacyTileData)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.id != var2.id) {
            return false;
         } else {
            LegacyTileData.Handler var3 = this.handler;
            LegacyTileData.Handler var4 = var2.handler;
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof LegacyTileData;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var4 = var2 * 59 + this.id;
      LegacyTileData.Handler var3 = this.handler;
      var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }

   private interface Handler {
      Material getMaterial();

      boolean isApplicable(BlockData data);

      void toBinary(DataOutputStream out) throws IOException;

      void toBukkit(Block block);
   }

   @FunctionalInterface
   private interface Builder {
      @Nullable
      LegacyTileData.Handler apply(@NonNull BlockState blockState, @NonNull Material type);
   }

   private static class BannerHandler implements LegacyTileData.Handler {
      private final KList<Pattern> patterns;
      private final DyeColor baseColor;

      private BannerHandler(DataInputStream in) {
         this.baseColor = DyeColor.values()[var1.readByte()];
         this.patterns = new KList();
         byte var2 = var1.readByte();

         for(int var3 = 0; var3 < var2; ++var3) {
            DyeColor var4 = DyeColor.values()[var1.readByte()];
            PatternType var5 = PatternType.values()[var1.readByte()];
            this.patterns.add((Object)(new Pattern(var4, var5)));
         }

      }

      private static LegacyTileData.BannerHandler fromBukkit(BlockState blockState, Material type) {
         if (Tag.BANNERS.isTagged(var1) && var0 instanceof Banner) {
            Banner var2 = (Banner)var0;
            return new LegacyTileData.BannerHandler(new KList(var2.getPatterns()), var2.getBaseColor());
         } else {
            return null;
         }
      }

      public Material getMaterial() {
         return Material.WHITE_BANNER;
      }

      public boolean isApplicable(BlockData data) {
         return Tag.BANNERS.isTagged(var1.getMaterial());
      }

      public void toBinary(DataOutputStream out) {
         var1.writeByte(this.baseColor.ordinal());
         var1.writeByte(this.patterns.size());
         Iterator var2 = this.patterns.iterator();

         while(var2.hasNext()) {
            Pattern var3 = (Pattern)var2.next();
            var1.writeByte(var3.getColor().ordinal());
            var1.writeByte(var3.getPattern().ordinal());
         }

      }

      public void toBukkit(Block block) {
         Banner var2 = (Banner)var1.getState();
         var2.setBaseColor(this.baseColor);
         var2.setPatterns(this.patterns);
         var2.update();
      }

      @Generated
      public String toString() {
         String var10000 = String.valueOf(this.patterns);
         return "LegacyTileData.BannerHandler(patterns=" + var10000 + ", baseColor=" + String.valueOf(this.baseColor) + ")";
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof LegacyTileData.BannerHandler)) {
            return false;
         } else {
            LegacyTileData.BannerHandler var2 = (LegacyTileData.BannerHandler)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else {
               KList var3 = this.patterns;
               KList var4 = var2.patterns;
               if (var3 == null) {
                  if (var4 != null) {
                     return false;
                  }
               } else if (!var3.equals(var4)) {
                  return false;
               }

               DyeColor var5 = this.baseColor;
               DyeColor var6 = var2.baseColor;
               if (var5 == null) {
                  if (var6 != null) {
                     return false;
                  }
               } else if (!var5.equals(var6)) {
                  return false;
               }

               return true;
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof LegacyTileData.BannerHandler;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         KList var3 = this.patterns;
         int var5 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
         DyeColor var4 = this.baseColor;
         var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
         return var5;
      }

      @Generated
      public BannerHandler(final KList<Pattern> patterns, final DyeColor baseColor) {
         this.patterns = var1;
         this.baseColor = var2;
      }
   }

   private static class SpawnerHandler implements LegacyTileData.Handler {
      private final EntityType type;

      private SpawnerHandler(DataInputStream in) {
         this.type = EntityType.values()[var1.readShort()];
      }

      private static LegacyTileData.SpawnerHandler fromBukkit(BlockState blockState, Material material) {
         if (var1 == Material.SPAWNER && var0 instanceof CreatureSpawner) {
            CreatureSpawner var2 = (CreatureSpawner)var0;
            return new LegacyTileData.SpawnerHandler(var2.getSpawnedType());
         } else {
            return null;
         }
      }

      public Material getMaterial() {
         return Material.SPAWNER;
      }

      public boolean isApplicable(BlockData data) {
         return var1.getMaterial() == Material.SPAWNER;
      }

      public void toBinary(DataOutputStream out) {
         var1.writeShort(this.type.ordinal());
      }

      public void toBukkit(Block block) {
         CreatureSpawner var2 = (CreatureSpawner)var1.getState();
         var2.setSpawnedType(this.type);
         var2.update();
      }

      @Generated
      public String toString() {
         return "LegacyTileData.SpawnerHandler(type=" + String.valueOf(this.type) + ")";
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof LegacyTileData.SpawnerHandler)) {
            return false;
         } else {
            LegacyTileData.SpawnerHandler var2 = (LegacyTileData.SpawnerHandler)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else {
               EntityType var3 = this.type;
               EntityType var4 = var2.type;
               if (var3 == null) {
                  if (var4 != null) {
                     return false;
                  }
               } else if (!var3.equals(var4)) {
                  return false;
               }

               return true;
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof LegacyTileData.SpawnerHandler;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         EntityType var3 = this.type;
         int var4 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
         return var4;
      }

      @Generated
      public SpawnerHandler(final EntityType type) {
         this.type = var1;
      }
   }

   private static class SignHandler implements LegacyTileData.Handler {
      private final String line1;
      private final String line2;
      private final String line3;
      private final String line4;
      private final DyeColor dyeColor;

      private SignHandler(DataInputStream in) {
         this.line1 = var1.readUTF();
         this.line2 = var1.readUTF();
         this.line3 = var1.readUTF();
         this.line4 = var1.readUTF();
         this.dyeColor = DyeColor.values()[var1.readByte()];
      }

      private static LegacyTileData.SignHandler fromBukkit(BlockState blockState, Material type) {
         if (LegacyTileData.signsTag().isTagged(var1) && var0 instanceof Sign) {
            Sign var2 = (Sign)var0;
            return new LegacyTileData.SignHandler(var2.getLine(0), var2.getLine(1), var2.getLine(2), var2.getLine(3), var2.getColor());
         } else {
            return null;
         }
      }

      public Material getMaterial() {
         return Material.OAK_SIGN;
      }

      public boolean isApplicable(BlockData data) {
         return LegacyTileData.signsTag().isTagged(var1.getMaterial());
      }

      public void toBinary(DataOutputStream out) {
         var1.writeUTF(this.line1);
         var1.writeUTF(this.line2);
         var1.writeUTF(this.line3);
         var1.writeUTF(this.line4);
         var1.writeByte(this.dyeColor.ordinal());
      }

      public void toBukkit(Block block) {
         Sign var2 = (Sign)var1.getState();
         var2.setLine(0, this.line1);
         var2.setLine(1, this.line2);
         var2.setLine(2, this.line3);
         var2.setLine(3, this.line4);
         var2.setColor(this.dyeColor);
         var2.update();
      }

      @Generated
      public String toString() {
         String var10000 = this.line1;
         return "LegacyTileData.SignHandler(line1=" + var10000 + ", line2=" + this.line2 + ", line3=" + this.line3 + ", line4=" + this.line4 + ", dyeColor=" + String.valueOf(this.dyeColor) + ")";
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof LegacyTileData.SignHandler)) {
            return false;
         } else {
            LegacyTileData.SignHandler var2 = (LegacyTileData.SignHandler)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else {
               label71: {
                  String var3 = this.line1;
                  String var4 = var2.line1;
                  if (var3 == null) {
                     if (var4 == null) {
                        break label71;
                     }
                  } else if (var3.equals(var4)) {
                     break label71;
                  }

                  return false;
               }

               String var5 = this.line2;
               String var6 = var2.line2;
               if (var5 == null) {
                  if (var6 != null) {
                     return false;
                  }
               } else if (!var5.equals(var6)) {
                  return false;
               }

               label57: {
                  String var7 = this.line3;
                  String var8 = var2.line3;
                  if (var7 == null) {
                     if (var8 == null) {
                        break label57;
                     }
                  } else if (var7.equals(var8)) {
                     break label57;
                  }

                  return false;
               }

               String var9 = this.line4;
               String var10 = var2.line4;
               if (var9 == null) {
                  if (var10 != null) {
                     return false;
                  }
               } else if (!var9.equals(var10)) {
                  return false;
               }

               DyeColor var11 = this.dyeColor;
               DyeColor var12 = var2.dyeColor;
               if (var11 == null) {
                  if (var12 == null) {
                     return true;
                  }
               } else if (var11.equals(var12)) {
                  return true;
               }

               return false;
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof LegacyTileData.SignHandler;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         String var3 = this.line1;
         int var8 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
         String var4 = this.line2;
         var8 = var8 * 59 + (var4 == null ? 43 : var4.hashCode());
         String var5 = this.line3;
         var8 = var8 * 59 + (var5 == null ? 43 : var5.hashCode());
         String var6 = this.line4;
         var8 = var8 * 59 + (var6 == null ? 43 : var6.hashCode());
         DyeColor var7 = this.dyeColor;
         var8 = var8 * 59 + (var7 == null ? 43 : var7.hashCode());
         return var8;
      }

      @Generated
      public SignHandler(final String line1, final String line2, final String line3, final String line4, final DyeColor dyeColor) {
         this.line1 = var1;
         this.line2 = var2;
         this.line3 = var3;
         this.line4 = var4;
         this.dyeColor = var5;
      }
   }
}
