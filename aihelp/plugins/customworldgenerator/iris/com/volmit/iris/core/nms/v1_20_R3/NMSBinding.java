package com.volmit.iris.core.nms.v1_20_R3;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.volmit.iris.Iris;
import com.volmit.iris.core.link.Identifier;
import com.volmit.iris.core.nms.INMSBinding;
import com.volmit.iris.core.nms.container.BiomeColor;
import com.volmit.iris.core.nms.container.BlockProperty;
import com.volmit.iris.core.nms.container.StructurePlacement;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.IrisJigsawStructurePlacement;
import com.volmit.iris.engine.platform.PlatformChunkGenerator;
import com.volmit.iris.util.agent.Agent;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.mantle.Mantle;
import com.volmit.iris.util.math.Vector3d;
import com.volmit.iris.util.matter.MatterBiomeInject;
import com.volmit.iris.util.nbt.mca.NBTWorld;
import com.volmit.iris.util.nbt.mca.palette.MCABiomeContainer;
import com.volmit.iris.util.nbt.mca.palette.MCAChunkBiomeContainer;
import com.volmit.iris.util.nbt.mca.palette.MCAGlobalPalette;
import com.volmit.iris.util.nbt.mca.palette.MCAIdMap;
import com.volmit.iris.util.nbt.mca.palette.MCAIdMapper;
import com.volmit.iris.util.nbt.mca.palette.MCAPalette;
import com.volmit.iris.util.nbt.mca.palette.MCAPaletteAccess;
import com.volmit.iris.util.nbt.mca.palette.MCAPalettedContainer;
import com.volmit.iris.util.nbt.mca.palette.MCAWrappedPalettedContainer;
import com.volmit.iris.util.nbt.tag.CompoundTag;
import com.volmit.iris.util.scheduling.J;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.Advice.Argument;
import net.bytebuddy.asm.Advice.OnMethodEnter;
import net.bytebuddy.asm.Advice.OnNonDefaultValue;
import net.bytebuddy.asm.Advice.This;
import net.bytebuddy.matcher.ElementMatchers;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.core.RegistryBlockID;
import net.minecraft.core.Holder.c;
import net.minecraft.core.IRegistryCustom.Dimension;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTList;
import net.minecraft.nbt.NBTNumber;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagEnd;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.commands.data.CommandDataAccessorTile;
import net.minecraft.server.level.PlayerChunkMap;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.level.progress.WorldLoadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ITileEntity;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.IBlockState;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.dimension.WorldDimension;
import net.minecraft.world.level.levelgen.ChunkProviderFlat;
import net.minecraft.world.level.levelgen.flat.GeneratorSettingsFlat;
import net.minecraft.world.level.levelgen.flat.WorldGenFlatLayerInfo;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.storage.WorldDataServer;
import net.minecraft.world.level.storage.Convertable.ConversionSession;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_20_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_20_R3.block.CraftBlockStates;
import org.bukkit.craftbukkit.v1_20_R3.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R3.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_20_R3.util.CraftNamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class NMSBinding implements INMSBinding {
   private final KMap<Biome, Object> baseBiomeCache = new KMap();
   private final BlockData AIR;
   private final AtomicCache<MCAIdMap<BiomeBase>> biomeMapCache;
   private final AtomicBoolean injected;
   private final AtomicCache<MCAIdMapper<IBlockData>> registryCache;
   private final AtomicCache<MCAPalette<IBlockData>> globalCache;
   private final AtomicCache<IRegistryCustom> registryAccess;
   private final AtomicCache<Method> byIdRef;
   private Field biomeStorageCache;

   public NMSBinding() {
      this.AIR = Material.AIR.createBlockData();
      this.biomeMapCache = new AtomicCache();
      this.injected = new AtomicBoolean();
      this.registryCache = new AtomicCache();
      this.globalCache = new AtomicCache();
      this.registryAccess = new AtomicCache();
      this.byIdRef = new AtomicCache();
      this.biomeStorageCache = null;
   }

   private static Object getFor(Class<?> type, Object source) {
      Object var2 = fieldFor(var0, var1);
      return var2 != null ? var2 : invokeFor(var0, var1);
   }

   private static Object invokeFor(Class<?> returns, Object in) {
      Method[] var2 = var1.getClass().getMethods();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Method var5 = var2[var4];
         if (var5.getReturnType().equals(var0)) {
            var5.setAccessible(true);

            try {
               String var10000 = var0.getSimpleName();
               Iris.debug("[NMS] Found " + var10000 + " in " + var1.getClass().getSimpleName() + "." + var5.getName() + "()");
               return var5.invoke(var1);
            } catch (Throwable var7) {
               var7.printStackTrace();
            }
         }
      }

      return null;
   }

   private static Object fieldFor(Class<?> returns, Object in) {
      return fieldForClass(var0, var1.getClass(), var1);
   }

   private static <T> T fieldForClass(Class<T> returnType, Class<?> sourceType, Object in) {
      Field[] var3 = var1.getDeclaredFields();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field var6 = var3[var5];
         if (var6.getType().equals(var0)) {
            var6.setAccessible(true);

            try {
               String var10000 = var0.getSimpleName();
               Iris.debug("[NMS] Found " + var10000 + " in " + var1.getSimpleName() + "." + var6.getName());
               return var6.get(var2);
            } catch (IllegalAccessException var8) {
               var8.printStackTrace();
            }
         }
      }

      return null;
   }

   private static Class<?> getClassType(Class<?> type, int ordinal) {
      return var0.getDeclaredClasses()[var1];
   }

   public boolean hasTile(Material material) {
      return !CraftBlockState.class.equals(CraftBlockStates.getBlockStateType(var1));
   }

   public boolean hasTile(Location l) {
      return ((CraftWorld)var1.getWorld()).getHandle().getBlockEntity(new BlockPosition(var1.getBlockX(), var1.getBlockY(), var1.getBlockZ()), false) != null;
   }

   public KMap<String, Object> serializeTile(Location location) {
      TileEntity var2 = ((CraftWorld)var1.getWorld()).getHandle().getBlockEntity(new BlockPosition(var1.getBlockX(), var1.getBlockY(), var1.getBlockZ()), false);
      if (var2 == null) {
         return null;
      } else {
         NBTTagCompound var3 = var2.q();
         return (KMap)this.convertFromTag(var3, 0, 64);
      }
   }

   @Contract(
      value = "null, _, _ -> null",
      pure = true
   )
   private Object convertFromTag(NBTBase tag, int depth, int maxDepth) {
      if (var1 != null && var2 <= var3) {
         Iterator var6;
         NBTBase var8;
         if (var1 instanceof NBTList) {
            NBTList var11 = (NBTList)var1;
            KList var12 = new KList();
            var6 = var11.iterator();

            while(var6.hasNext()) {
               Object var13 = var6.next();
               if (var13 instanceof NBTBase) {
                  var8 = (NBTBase)var13;
                  var12.add((Object)this.convertFromTag(var8, var2 + 1, var3));
               } else {
                  var12.add((Object)var13);
               }
            }

            return var12;
         } else if (var1 instanceof NBTTagCompound) {
            NBTTagCompound var10 = (NBTTagCompound)var1;
            KMap var5 = new KMap();
            var6 = var10.e().iterator();

            while(var6.hasNext()) {
               String var7 = (String)var6.next();
               var8 = var10.c(var7);
               if (var8 != null) {
                  Object var9 = this.convertFromTag(var8, var2 + 1, var3);
                  if (var9 != null) {
                     var5.put(var7, var9);
                  }
               }
            }

            return var5;
         } else if (var1 instanceof NBTNumber) {
            NBTNumber var4 = (NBTNumber)var1;
            return var4.l();
         } else {
            return var1.t_();
         }
      } else {
         return null;
      }
   }

   public void deserializeTile(KMap<String, Object> map, Location pos) {
      NBTTagCompound var3 = (NBTTagCompound)this.convertToTag(var1, 0, 64);
      WorldServer var4 = ((CraftWorld)var2.getWorld()).getHandle();
      BlockPosition var5 = new BlockPosition(var2.getBlockX(), var2.getBlockY(), var2.getBlockZ());
      J.s(() -> {
         this.merge(var4, var5, var3);
      });
   }

   private void merge(WorldServer level, BlockPosition blockPos, NBTTagCompound tag) {
      TileEntity var4 = var1.c_(var2);
      if (var4 == null) {
         Iris.warn("[NMS] BlockEntity not found at " + String.valueOf(var2));
         IBlockData var5 = var1.a_(var2);
         if (!var5.t()) {
            return;
         }

         var4 = ((ITileEntity)var5.b()).a(var2, var5);
      }

      CommandDataAccessorTile var6 = new CommandDataAccessorTile(var4, var2);
      var6.a(var6.a().a(var3));
   }

   private NBTBase convertToTag(Object object, int depth, int maxDepth) {
      if (var1 != null && var2 <= var3) {
         Iterator var6;
         if (var1 instanceof Map) {
            Map var16 = (Map)var1;
            NBTTagCompound var11 = new NBTTagCompound();
            var6 = var16.entrySet().iterator();

            while(var6.hasNext()) {
               Entry var17 = (Entry)var6.next();
               var11.a(var17.getKey().toString(), this.convertToTag(var17.getValue(), var2 + 1, var3));
            }

            return var11;
         } else if (!(var1 instanceof List)) {
            if (var1 instanceof Byte) {
               Byte var15 = (Byte)var1;
               return NBTTagByte.a(var15);
            } else if (var1 instanceof Short) {
               Short var14 = (Short)var1;
               return NBTTagShort.a(var14);
            } else if (var1 instanceof Integer) {
               Integer var13 = (Integer)var1;
               return NBTTagInt.a(var13);
            } else if (var1 instanceof Long) {
               Long var12 = (Long)var1;
               return NBTTagLong.a(var12);
            } else if (var1 instanceof Float) {
               Float var10 = (Float)var1;
               return NBTTagFloat.a(var10);
            } else if (var1 instanceof Double) {
               Double var9 = (Double)var1;
               return NBTTagDouble.a(var9);
            } else if (var1 instanceof String) {
               String var8 = (String)var1;
               return NBTTagString.a(var8);
            } else {
               return NBTTagEnd.b;
            }
         } else {
            List var4 = (List)var1;
            NBTTagList var5 = new NBTTagList();
            var6 = var4.iterator();

            while(var6.hasNext()) {
               Object var7 = var6.next();
               var5.add(this.convertToTag(var7, var2 + 1, var3));
            }

            return var5;
         }
      } else {
         return NBTTagEnd.b;
      }
   }

   public CompoundTag serializeEntity(Entity location) {
      return null;
   }

   public Entity deserializeEntity(CompoundTag s, Location newPosition) {
      return null;
   }

   public boolean supportsCustomHeight() {
      return true;
   }

   private IRegistryCustom registry() {
      return (IRegistryCustom)this.registryAccess.aquire(() -> {
         return (IRegistryCustom)getFor(Dimension.class, ((CraftServer)Bukkit.getServer()).getHandle().b());
      });
   }

   private IRegistry<BiomeBase> getCustomBiomeRegistry() {
      return (IRegistry)this.registry().c(Registries.at).orElse((Object)null);
   }

   private IRegistry<Block> getBlockRegistry() {
      return (IRegistry)this.registry().c(Registries.f).orElse((Object)null);
   }

   public Object getBiomeBaseFromId(int id) {
      return this.getCustomBiomeRegistry().c(var1);
   }

   public int getMinHeight(World world) {
      return var1.getMinHeight();
   }

   public boolean supportsCustomBiomes() {
      return true;
   }

   public int getTrueBiomeBaseId(Object biomeBase) {
      return this.getCustomBiomeRegistry().a((BiomeBase)((Holder)var1).a());
   }

   public Object getTrueBiomeBase(Location location) {
      return ((CraftWorld)var1.getWorld()).getHandle().t(new BlockPosition(var1.getBlockX(), var1.getBlockY(), var1.getBlockZ()));
   }

   public String getTrueBiomeBaseKey(Location location) {
      return this.getKeyForBiomeBase(this.getTrueBiomeBase(var1));
   }

   public Object getCustomBiomeBaseFor(String mckey) {
      return this.getCustomBiomeRegistry().a(new MinecraftKey(var1));
   }

   public Object getCustomBiomeBaseHolderFor(String mckey) {
      return this.getCustomBiomeRegistry().c(this.getTrueBiomeBaseId(this.getCustomBiomeRegistry().a(new MinecraftKey(var1)))).get();
   }

   public int getBiomeBaseIdForKey(String key) {
      return this.getCustomBiomeRegistry().a((BiomeBase)this.getCustomBiomeRegistry().a(new MinecraftKey(var1)));
   }

   public String getKeyForBiomeBase(Object biomeBase) {
      return this.getCustomBiomeRegistry().b((BiomeBase)var1).a();
   }

   public Object getBiomeBase(World world, Biome biome) {
      return biomeToBiomeBase((IRegistry)((CraftWorld)var1).getHandle().I_().c(Registries.at).orElse((Object)null), var2);
   }

   public Object getBiomeBase(Object registry, Biome biome) {
      Object var3 = this.baseBiomeCache.get(var2);
      if (var3 != null) {
         return var3;
      } else {
         Holder var4 = biomeToBiomeBase((IRegistry)var1, var2);
         if (var4 == null) {
            return biomeToBiomeBase((IRegistry)var1, Biome.PLAINS);
         } else {
            this.baseBiomeCache.put(var2, var4);
            return var4;
         }
      }
   }

   public KList<Biome> getBiomes() {
      return (new KList(Biome.values())).qadd(Biome.CHERRY_GROVE).qdel(Biome.CUSTOM);
   }

   public boolean isBukkit() {
      return true;
   }

   public int getBiomeId(Biome biome) {
      Iterator var2 = Bukkit.getWorlds().iterator();

      World var3;
      do {
         if (!var2.hasNext()) {
            return var1.ordinal();
         }

         var3 = (World)var2.next();
      } while(!var3.getEnvironment().equals(Environment.NORMAL));

      IRegistry var4 = (IRegistry)((CraftWorld)var3).getHandle().I_().c(Registries.at).orElse((Object)null);
      return var4.a((BiomeBase)this.getBiomeBase((Object)var4, var1));
   }

   private MCAIdMap<BiomeBase> getBiomeMapping() {
      return (MCAIdMap)this.biomeMapCache.aquire(() -> {
         return new MCAIdMap<BiomeBase>() {
            @NotNull
            public Iterator<BiomeBase> iterator() {
               return NMSBinding.this.getCustomBiomeRegistry().iterator();
            }

            public int getId(BiomeBase paramT) {
               return NMSBinding.this.getCustomBiomeRegistry().a(var1);
            }

            public BiomeBase byId(int paramInt) {
               return (BiomeBase)NMSBinding.this.getBiomeBaseFromId(var1);
            }
         };
      });
   }

   @NotNull
   private MCABiomeContainer getBiomeContainerInterface(MCAIdMap<BiomeBase> biomeMapping, MCAChunkBiomeContainer<BiomeBase> base) {
      return new MCABiomeContainer(this) {
         public int[] getData() {
            return var2.writeBiomes();
         }

         public void setBiome(int x, int y, int z, int id) {
            var2.setBiome(var1x, var2x, var3, (BiomeBase)var1.byId(var4));
         }

         public int getBiome(int x, int y, int z) {
            return var1.getId((BiomeBase)var2.getBiome(var1x, var2x, var3));
         }
      };
   }

   public MCABiomeContainer newBiomeContainer(int min, int max) {
      MCAChunkBiomeContainer var3 = new MCAChunkBiomeContainer(this.getBiomeMapping(), var1, var2);
      return this.getBiomeContainerInterface(this.getBiomeMapping(), var3);
   }

   public MCABiomeContainer newBiomeContainer(int min, int max, int[] data) {
      MCAChunkBiomeContainer var4 = new MCAChunkBiomeContainer(this.getBiomeMapping(), var1, var2, var3);
      return this.getBiomeContainerInterface(this.getBiomeMapping(), var4);
   }

   public int countCustomBiomes() {
      AtomicInteger var1 = new AtomicInteger(0);
      this.getCustomBiomeRegistry().e().forEach((var1x) -> {
         if (!var1x.b().equals("minecraft")) {
            var1.incrementAndGet();
            Iris.debug("Custom Biome: " + String.valueOf(var1x));
         }
      });
      return var1.get();
   }

   public boolean supportsDataPacks() {
      return true;
   }

   public void setBiomes(int cx, int cz, World world, Hunk<Object> biomes) {
      Chunk var5 = ((CraftWorld)var3).getHandle().d(var1, var2);
      var4.iterateSync((var1x, var2x, var3x, var4x) -> {
         var5.setBiome(var1x, var2x, var3x, (Holder)var4x);
      });
      var5.a(true);
   }

   public void forceBiomeInto(int x, int y, int z, Object somethingVeryDirty, BiomeGrid chunk) {
      try {
         IChunkAccess var6 = (IChunkAccess)this.getFieldForBiomeStorage(var5).get(var5);
         Holder var7 = (Holder)var4;
         var6.setBiome(var1, var2, var3, var7);
      } catch (IllegalAccessException var8) {
         Iris.reportError(var8);
         var8.printStackTrace();
      }

   }

   private Field getFieldForBiomeStorage(Object storage) {
      Field var2 = this.biomeStorageCache;
      if (var2 != null) {
         return var2;
      } else {
         try {
            var2 = var1.getClass().getDeclaredField("biome");
            var2.setAccessible(true);
            return var2;
         } catch (Throwable var4) {
            Iris.reportError(var4);
            var4.printStackTrace();
            Iris.error(var1.getClass().getCanonicalName());
            this.biomeStorageCache = var2;
            return null;
         }
      }
   }

   public MCAPaletteAccess createPalette() {
      MCAIdMapper var1 = (MCAIdMapper)this.registryCache.aquireNasty(() -> {
         Field var0 = RegistryBlockID.class.getDeclaredField("tToId");
         Field var1 = RegistryBlockID.class.getDeclaredField("idToT");
         Field var2 = RegistryBlockID.class.getDeclaredField("nextId");
         var0.setAccessible(true);
         var1.setAccessible(true);
         var2.setAccessible(true);
         RegistryBlockID var3 = Block.q;
         int var4 = var2.getInt(var3);
         Object2IntMap var5 = (Object2IntMap)var0.get(var3);
         List var6 = (List)var1.get(var3);
         return new MCAIdMapper(var5, var6, var4);
      });
      MCAPalette var2 = (MCAPalette)this.globalCache.aquireNasty(() -> {
         return new MCAGlobalPalette(var1, ((CraftBlockData)this.AIR).getState());
      });
      MCAPalettedContainer var3 = new MCAPalettedContainer(var2, var1, (var0) -> {
         return ((CraftBlockData)NBTWorld.getBlockData(var0)).getState();
      }, (var0) -> {
         return NBTWorld.getCompound(CraftBlockData.fromData(var0));
      }, ((CraftBlockData)this.AIR).getState());
      return new MCAWrappedPalettedContainer(var3, (var0) -> {
         return NBTWorld.getCompound(CraftBlockData.fromData(var0));
      }, (var0) -> {
         return ((CraftBlockData)NBTWorld.getBlockData(var0)).getState();
      });
   }

   public void injectBiomesFromMantle(org.bukkit.Chunk e, Mantle mantle) {
      IChunkAccess var3 = ((CraftChunk)var1).getHandle(ChunkStatus.n);
      AtomicInteger var4 = new AtomicInteger();
      AtomicInteger var5 = new AtomicInteger();
      var2.iterateChunk(var1.getX(), var1.getZ(), MatterBiomeInject.class, (var5x, var6, var7, var8) -> {
         if (var8 != null) {
            if (var8.isCustom()) {
               var3.setBiome(var5x, var6, var7, (Holder)this.getCustomBiomeRegistry().c(var8.getBiomeId()).get());
               var4.getAndIncrement();
            } else {
               var3.setBiome(var5x, var6, var7, (Holder)this.getBiomeBase(var1.getWorld(), var8.getBiome()));
               var5.getAndIncrement();
            }
         }

      });
   }

   public ItemStack applyCustomNbt(ItemStack itemStack, KMap<String, Object> customNbt) {
      if (var2 != null && !var2.isEmpty()) {
         net.minecraft.world.item.ItemStack var3 = CraftItemStack.asNMSCopy(var1);

         try {
            NBTTagCompound var4 = MojangsonParser.a((new JSONObject(var2)).toString());
            var4.a(var3.w());
            var3.c(var4);
         } catch (CommandSyntaxException var5) {
            throw new IllegalArgumentException(var5);
         }

         return CraftItemStack.asBukkitCopy(var3);
      } else {
         return var1;
      }
   }

   public void inject(long seed, Engine engine, World world) {
      PlayerChunkMap var5 = ((CraftWorld)var4).getHandle().l().a;
      ResourceKey var6 = (ResourceKey)var5.q.ad().e().orElse((Object)null);
      if (var6 != null && !var6.a().b().equals("iris")) {
         Iris.error("Loaded world %s with invalid dimension type! (%s)", var4.getName(), var6.a().toString());
      }

      var5.t = new IrisChunkGenerator(var5.t, var1, var3, var4);
   }

   public Vector3d getBoundingbox(EntityType entity) {
      Field[] var2 = EntityTypes.class.getDeclaredFields();
      Field[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field var6 = var3[var5];
         if (Modifier.isStatic(var6.getModifiers()) && var6.getType().equals(EntityTypes.class)) {
            try {
               EntityTypes var7 = (EntityTypes)var6.get((Object)null);
               if (var7.g().equals("entity.minecraft." + var1.name().toLowerCase())) {
                  Vector var8 = new Vector();
                  var8.add(var7.l());
                  var7.n();
                  Vector3d var9 = new Vector3d((double)var7.k(), (double)var7.l(), (double)var7.k());
                  return var9;
               }
            } catch (IllegalAccessException var10) {
               Iris.error("Unable to get entity dimensions!");
               var10.printStackTrace();
            }
         }
      }

      return null;
   }

   public Entity spawnEntity(Location location, EntityType type, SpawnReason reason) {
      return ((CraftWorld)var1.getWorld()).spawn(var1, var2.getEntityClass(), (Consumer)null, var3);
   }

   public Color getBiomeColor(Location location, BiomeColor type) {
      WorldServer var3 = ((CraftWorld)var1.getWorld()).getHandle();
      Holder var4 = var3.t(new BlockPosition(var1.getBlockX(), var1.getBlockY(), var1.getBlockZ()));
      BiomeBase var5 = (BiomeBase)var4.a();
      if (var5 == null) {
         Optional var10002 = var4.e();
         throw new IllegalArgumentException("Invalid biome: " + String.valueOf(var10002.orElse((Object)null)));
      } else {
         int var10000;
         switch(var2) {
         case FOG:
            var10000 = var5.e();
            break;
         case WATER:
            var10000 = var5.i();
            break;
         case WATER_FOG:
            var10000 = var5.j();
            break;
         case SKY:
            var10000 = var5.a();
            break;
         case FOLIAGE:
            var10000 = var5.f();
            break;
         case GRASS:
            var10000 = var5.a((double)var1.getBlockX(), (double)var1.getBlockZ());
            break;
         default:
            throw new MatchException((String)null, (Throwable)null);
         }

         int var6 = var10000;
         if (var6 == 0) {
            if (BiomeColor.FOLIAGE == var2 && var5.h().e().isEmpty()) {
               return null;
            }

            if (BiomeColor.GRASS == var2 && var5.h().f().isEmpty()) {
               return null;
            }
         }

         return new Color(var6, true);
      }
   }

   public KList<String> getStructureKeys() {
      KList var1 = new KList();
      IRegistry var2 = (IRegistry)this.registry().c(Registries.aD).orElse((Object)null);
      if (var2 == null) {
         return var1;
      } else {
         Stream var10000 = var2.e().stream().map(MinecraftKey::toString);
         Objects.requireNonNull(var1);
         var10000.forEach(var1::add);
         var10000 = var2.i().map(Pair::getFirst).map(TagKey::b).map(MinecraftKey::toString).map((var0) -> {
            return "#" + var0;
         });
         Objects.requireNonNull(var1);
         var10000.forEach(var1::add);
         return var1;
      }
   }

   private static Field getField(Class<?> clazz, Class<?> fieldType) {
      try {
         Field[] var2 = var0.getDeclaredFields();
         int var7 = var2.length;

         for(int var4 = 0; var4 < var7; ++var4) {
            Field var5 = var2[var4];
            if (var5.getType().equals(var1)) {
               return var5;
            }
         }

         throw new NoSuchFieldException(var1.getName());
      } catch (NoSuchFieldException var6) {
         Class var3 = var0.getSuperclass();
         if (var3 == null) {
            throw var6;
         } else {
            return getField(var3, var1);
         }
      }
   }

   public static Holder<BiomeBase> biomeToBiomeBase(IRegistry<BiomeBase> registry, Biome biome) {
      return var0.f(ResourceKey.a(Registries.at, CraftNamespacedKey.toMinecraft(var1.getKey())));
   }

   public boolean missingDimensionTypes(String... keys) {
      IRegistry var2 = this.registry().d(Registries.ay);
      Stream var10000 = Arrays.stream(var1).map((var0) -> {
         return new MinecraftKey("iris", var0);
      });
      Objects.requireNonNull(var2);
      return !var10000.allMatch(var2::c);
   }

   public boolean injectBukkit() {
      if (this.injected.getAndSet(true)) {
         return true;
      } else {
         try {
            Iris.info("Injecting Bukkit");
            ByteBuddy var1 = new ByteBuddy();
            var1.redefine(WorldServer.class).visit(Advice.to(NMSBinding.ServerLevelAdvice.class).on(ElementMatchers.isConstructor().and(ElementMatchers.takesArguments(new Class[]{MinecraftServer.class, Executor.class, ConversionSession.class, WorldDataServer.class, ResourceKey.class, WorldDimension.class, WorldLoadListener.class, Boolean.TYPE, Long.TYPE, List.class, Boolean.TYPE, RandomSequences.class, Environment.class, ChunkGenerator.class, BiomeProvider.class})))).make().load(WorldServer.class.getClassLoader(), Agent.installed());
            Iterator var2 = List.of(IChunkAccess.class, ProtoChunk.class).iterator();

            while(var2.hasNext()) {
               Class var3 = (Class)var2.next();
               var1.redefine(var3).visit(Advice.to(NMSBinding.ChunkAccessAdvice.class).on(ElementMatchers.isMethod().and(ElementMatchers.takesArguments(new Class[]{Short.TYPE, Integer.TYPE})))).make().load(var3.getClassLoader(), Agent.installed());
            }

            return true;
         } catch (Throwable var4) {
            Iris.error(String.valueOf(C.RED) + "Failed to inject Bukkit");
            var4.printStackTrace();
            return false;
         }
      }
   }

   public KMap<Material, List<BlockProperty>> getBlockProperties() {
      KMap var1 = new KMap();

      Block var3;
      IBlockData var4;
      for(Iterator var2 = this.registry().d(Registries.f).iterator(); var2.hasNext(); var1.put(CraftMagicNumbers.getMaterial(var3), var3.n().d().stream().map((var2x) -> {
         return this.createProperty(var2x, var4);
      }).toList())) {
         var3 = (Block)var2.next();
         var4 = var3.o();
         if (var4 == null) {
            var4 = (IBlockData)var3.n().b();
         }
      }

      return var1;
   }

   private <T extends Comparable<T>> BlockProperty createProperty(IBlockState<T> property, IBlockData state) {
      String var10002 = var1.f();
      Class var10003 = var1.g();
      Comparable var10004 = var2.c(var1);
      Collection var10005 = var1.a();
      Objects.requireNonNull(var1);
      return new BlockProperty(var10002, var10003, var10004, var10005, var1::a);
   }

   public void placeStructures(org.bukkit.Chunk chunk) {
      CraftChunk var2 = (CraftChunk)var1;
      WorldServer var3 = var2.getCraftWorld().getHandle();
      IChunkAccess var4 = ((CraftChunk)var1).getHandle(ChunkStatus.n);
      var3.l().g().a(var3, var4, var3.a());
   }

   public KMap<Identifier, StructurePlacement> collectStructures() {
      IRegistry var1 = this.registry().d(Registries.aF);
      IRegistry var2 = this.registry().d(Registries.ai);
      Stream var10000 = var1.f().stream();
      Objects.requireNonNull(var1);
      return (KMap)var10000.map(var1::b).filter(Optional::isPresent).map(Optional::get).map((var1x) -> {
         StructureSet var2x = (StructureSet)var1x.a();
         net.minecraft.world.level.levelgen.structure.placement.StructurePlacement var3 = var2x.b();
         MinecraftKey var4 = var1x.g().a();
         Object var5;
         if (var3 instanceof RandomSpreadStructurePlacement) {
            RandomSpreadStructurePlacement var7 = (RandomSpreadStructurePlacement)var3;
            StructurePlacement.RandomSpread.RandomSpreadBuilder var10000 = StructurePlacement.RandomSpread.builder().separation(var7.b()).spacing(var7.a());
            IrisJigsawStructurePlacement.SpreadType var10001;
            switch(var7.c()) {
            case a:
               var10001 = IrisJigsawStructurePlacement.SpreadType.LINEAR;
               break;
            case b:
               var10001 = IrisJigsawStructurePlacement.SpreadType.TRIANGULAR;
               break;
            default:
               throw new MatchException((String)null, (Throwable)null);
            }

            var5 = var10000.spreadType(var10001);
         } else {
            if (!(var3 instanceof ConcentricRingsStructurePlacement)) {
               Iris.warn("Unsupported structure placement for set " + String.valueOf(var4) + " with type " + String.valueOf(var2.b(var3.e())));
               return null;
            }

            ConcentricRingsStructurePlacement var6 = (ConcentricRingsStructurePlacement)var3;
            var5 = StructurePlacement.ConcentricRings.builder().distance(var6.a()).spread(var6.b()).count(var6.c());
         }

         return new com.volmit.iris.core.nms.container.Pair(new Identifier(var4.b(), var4.a()), ((StructurePlacement.StructurePlacementBuilder)var5).salt(var3.f).frequency(var3.e).structures(var2x.a().stream().map((var0) -> {
            return new StructurePlacement.Structure(var0.b(), (String)var0.a().e().map(ResourceKey::a).map(MinecraftKey::toString).orElse((Object)null), var0.a().c().map(TagKey::b).map(MinecraftKey::toString).toList());
         }).filter(StructurePlacement.Structure::isValid).toList()).build());
      }).filter(Objects::nonNull).collect(Collectors.toMap(com.volmit.iris.core.nms.container.Pair::getA, com.volmit.iris.core.nms.container.Pair::getB, (var0, var1x) -> {
         return var0;
      }, KMap::new));
   }

   public WorldDimension levelStem(IRegistryCustom access, ChunkGenerator raw) {
      if (var2 instanceof PlatformChunkGenerator) {
         PlatformChunkGenerator var3 = (PlatformChunkGenerator)var2;
         MinecraftKey var4 = new MinecraftKey("iris", var3.getTarget().getDimension().getDimensionTypeKey());
         c var5 = var1.b(Registries.ay).b(ResourceKey.a(Registries.ay, var4));
         return new WorldDimension(var5, this.chunkGenerator(var1));
      } else {
         throw new IllegalStateException("Generator is not platform chunk generator!");
      }
   }

   private net.minecraft.world.level.chunk.ChunkGenerator chunkGenerator(IRegistryCustom access) {
      GeneratorSettingsFlat var2 = new GeneratorSettingsFlat(Optional.empty(), var1.d(Registries.at).f(Biomes.a), List.of());
      var2.e().add(new WorldGenFlatLayerInfo(1, Blocks.a));
      var2.g();
      return new ChunkProviderFlat(var2);
   }

   private static class ServerLevelAdvice {
      @OnMethodEnter
      static void enter(@Argument(0) MinecraftServer server, @Argument(3) WorldDataServer levelData, @Argument(value = 5,readOnly = false) WorldDimension levelStem, @Argument(12) Environment env, @Argument(13) ChunkGenerator gen) {
         if (var4 != null && var4.getClass().getPackageName().startsWith("com.volmit.iris")) {
            try {
               Object var5 = Class.forName("com.volmit.iris.core.nms.INMS", true, Bukkit.getPluginManager().getPlugin("Iris").getClass().getClassLoader()).getDeclaredMethod("get").invoke((Object)null);
               var2 = (WorldDimension)var5.getClass().getDeclaredMethod("levelStem", IRegistryCustom.class, ChunkGenerator.class).invoke(var5, var0.aZ(), var4);
               var1.customDimensions = null;
            } catch (Throwable var7) {
               RuntimeException var10000 = new RuntimeException;
               Throwable var10003;
               if (var7 instanceof InvocationTargetException) {
                  InvocationTargetException var6 = (InvocationTargetException)var7;
                  var10003 = var6.getCause();
               } else {
                  var10003 = var7;
               }

               var10000.<init>("Iris failed to replace the levelStem", var10003);
               throw var10000;
            }
         }
      }
   }

   private static class ChunkAccessAdvice {
      @OnMethodEnter(
         skipOn = OnNonDefaultValue.class
      )
      static boolean enter(@This IChunkAccess access, @Argument(1) int index) {
         return var1 >= var0.n().length;
      }
   }
}
