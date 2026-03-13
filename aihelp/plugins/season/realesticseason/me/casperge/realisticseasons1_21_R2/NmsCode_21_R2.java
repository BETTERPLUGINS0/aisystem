package me.casperge.realisticseasons1_21_R2;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;
import me.casperge.interfaces.NmsCode;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistry;
import net.minecraft.core.RegistryMaterials;
import net.minecraft.core.Holder.c;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.PacketPlayOutGameStateChange;
import net.minecraft.network.protocol.game.PacketPlayOutGameStateChange.a;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeFog;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_21_R2.CraftChunk;
import org.bukkit.craftbukkit.v1_21_R2.CraftServer;
import org.bukkit.craftbukkit.v1_21_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R2.block.CraftBiome;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftPlayer;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Wolf.Variant;

public class NmsCode_21_R2 implements NmsCode {
   private DedicatedServer dedicatedserver;
   int test = 0;

   public NmsCode_21_R2() {
      Server var1 = Bukkit.getServer();
      CraftServer var2 = (CraftServer)var1;
      this.dedicatedserver = var2.getServer();
   }

   public int getBiomeID(Biome var1) {
      BiomeBase var2 = CraftBiome.bukkitToMinecraft(var1);
      return this.getBiomeRegistry().a(var2);
   }

   public int getBiomeID(String var1) {
      ResourceKey var2;
      if (var1.contains(":")) {
         String[] var3 = var1.split(":");
         var2 = ResourceKey.a(Registries.aI, MinecraftKey.a(var3[0].toLowerCase(), var3[1].toLowerCase()));
      } else {
         var2 = ResourceKey.a(Registries.aI, MinecraftKey.b(var1.toLowerCase()));
      }

      BiomeBase var4 = (BiomeBase)((c)this.getBiomeRegistry().a(var2).get()).a();
      return this.getBiomeRegistry().a(var4);
   }

   public String getBiome(int var1) {
      if (var1 == 5555) {
         return "NONE";
      } else {
         BiomeBase var2 = (BiomeBase)this.getBiomeRegistry().a(var1);
         MinecraftKey var3 = this.getBiomeRegistry().b(var2);
         String var4 = var3.b();
         if (var4 == null || var4 == "") {
            var4 = "minecraft";
         }

         String var5 = var3.a();
         return var4 + ":" + var5;
      }
   }

   private int getFieldInBiomea(BiomeFog var1, String var2) {
      try {
         Field var3 = var1.getClass().getDeclaredField(var2);
         var3.setAccessible(true);
         return (Integer)var3.get(var1);
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var4) {
         var4.printStackTrace();
         return 6;
      }
   }

   public static void setWolfVariant(Wolf var0, int var1) {
      switch(var1) {
      case 0:
         var0.setVariant(Variant.ASHEN);
         break;
      case 1:
         var0.setVariant(Variant.BLACK);
         break;
      case 2:
         var0.setVariant(Variant.CHESTNUT);
         break;
      case 3:
         var0.setVariant(Variant.PALE);
         break;
      case 4:
         var0.setVariant(Variant.RUSTY);
         break;
      case 5:
         var0.setVariant(Variant.SNOWY);
         break;
      case 6:
         var0.setVariant(Variant.SPOTTED);
         break;
      case 7:
         var0.setVariant(Variant.STRIPED);
         break;
      case 8:
         var0.setVariant(Variant.WOODS);
      }

   }

   public int[] getBiomeColors(String var1) {
      if (var1.equals("SNOWY_TUNDRA")) {
         var1 = "SNOWY_PLAINS";
      }

      if (var1.equals("MOUNTAINS")) {
         var1 = "WINDSWEPT_HILLS";
      }

      ResourceKey var2;
      if (var1.contains(":")) {
         String[] var3 = var1.split(":");
         var2 = ResourceKey.a(Registries.aI, MinecraftKey.a(var3[0].toLowerCase(), var3[1].toLowerCase()));
      } else {
         var2 = ResourceKey.a(Registries.aI, MinecraftKey.b(var1.toLowerCase()));
      }

      BiomeBase var9 = (BiomeBase)((c)this.getBiomeRegistry().a(var2).get()).a();
      BiomeFog var4 = null;

      try {
         Field var5 = BiomeBase.class.getDeclaredField("l");
         var5.setAccessible(true);
         var4 = (BiomeFog)var5.get(var9);
      } catch (Exception var8) {
         Bukkit.getLogger().info(var1);
         var8.printStackTrace();
      }

      int[] var10 = new int[]{this.getFieldInBiomea(var4, "b"), this.getFieldInBiomea(var4, "c"), this.getFieldInBiomea(var4, "d"), this.getFieldInBiomea(var4, "e"), -9999999, -9999999};
      Optional var6 = this.getOptionalFieldInBiomea(var4, "g");
      if (!var6.isEmpty()) {
         var10[4] = (Integer)var6.get();
      }

      Optional var7 = this.getOptionalFieldInBiomea(var4, "f");
      if (!var7.isEmpty()) {
         var10[5] = (Integer)var7.get();
      }

      return var10;
   }

   private Optional<Integer> getOptionalFieldInBiomea(BiomeFog var1, String var2) {
      try {
         Field var3 = var1.getClass().getDeclaredField(var2);
         var3.setAccessible(true);
         return (Optional)var3.get(var1);
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var4) {
         var4.printStackTrace();
         return Optional.of(22222);
      }
   }

   public float getBiomeTemperature(String var1) {
      ResourceKey var2;
      if (var1.contains(":")) {
         String[] var3 = var1.split(":");
         var2 = ResourceKey.a(Registries.aI, MinecraftKey.a(var3[0].toLowerCase(), var3[1].toLowerCase()));
      } else {
         var2 = ResourceKey.a(Registries.aI, MinecraftKey.b(var1.toLowerCase()));
      }

      BiomeBase var4 = (BiomeBase)((c)this.getBiomeRegistry().a(var2).get()).a();
      return var4.g();
   }

   public List<String> getCustomBiomes(String var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.getBiomeRegistry().k().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         MinecraftKey var5 = ((ResourceKey)var4.getKey()).a();
         String var6 = var5.a();
         String var7 = var5.b();
         if (var7 != null && !var7.equals("minecraft") && !var7.equals(var1)) {
            var2.add(var7 + ":" + var6);
         }
      }

      return var2;
   }

   public List<String> getBiomes(String var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.getBiomeRegistry().k().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         MinecraftKey var5 = ((ResourceKey)var4.getKey()).a();
         String var6 = var5.a();
         String var7 = var5.b();
         if (var7 != null && var7.equals(var1)) {
            var2.add(var7 + ":" + var6);
         }
      }

      return var2;
   }

   public List<String> getAllBiomes() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.getBiomeRegistry().k().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         MinecraftKey var4 = ((ResourceKey)var3.getKey()).a();
         String var5 = var4.a();
         String var6 = var4.b();
         var1.add(var6 + ":" + var5);
      }

      return var1;
   }

   public String getBiomeName(Biome var1) {
      BiomeBase var2 = CraftBiome.bukkitToMinecraft(var1);
      MinecraftKey var3 = this.getBiomeRegistry().b(var2);
      return var3.a();
   }

   public double getTPS() {
      return this.dedicatedserver.recentTps[0];
   }

   public Class<?> getCbClass(String var1) {
      return Class.forName("org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + var1);
   }

   public Biome[] getAssociatedBiomes(String var1) {
      Biome[] var2;
      if (var1.equals("BADLANDS")) {
         var2 = new Biome[]{Biome.BADLANDS, Biome.WOODED_BADLANDS, Biome.ERODED_BADLANDS};
         return var2;
      } else if (var1.equals("JUNGLE")) {
         var2 = new Biome[]{Biome.JUNGLE, Biome.BAMBOO_JUNGLE, Biome.SPARSE_JUNGLE};
         return var2;
      } else if (var1.equals("BEACH")) {
         var2 = new Biome[]{Biome.BEACH, Biome.STONY_SHORE};
         return var2;
      } else if (var1.equals("BIRCH_FOREST")) {
         var2 = new Biome[]{Biome.BIRCH_FOREST, Biome.OLD_GROWTH_BIRCH_FOREST};
         return var2;
      } else if (var1.equals("OCEAN")) {
         var2 = new Biome[]{Biome.OCEAN, Biome.COLD_OCEAN, Biome.DEEP_COLD_OCEAN, Biome.DEEP_LUKEWARM_OCEAN, Biome.DEEP_OCEAN, Biome.LUKEWARM_OCEAN, Biome.WARM_OCEAN};
         return var2;
      } else if (var1.equals("DARK_FOREST")) {
         var2 = new Biome[]{Biome.DARK_FOREST};
         return var2;
      } else if (var1.equals("DESERT")) {
         var2 = new Biome[]{Biome.DESERT};
         return var2;
      } else if (var1.equals("FLOWER_FOREST")) {
         var2 = new Biome[]{Biome.FLOWER_FOREST, Biome.MEADOW, Biome.CHERRY_GROVE};
         return var2;
      } else if (var1.equals("FOREST")) {
         var2 = new Biome[]{Biome.FOREST};
         return var2;
      } else if (var1.equals("TAIGA")) {
         var2 = new Biome[]{Biome.TAIGA, Biome.OLD_GROWTH_SPRUCE_TAIGA, Biome.OLD_GROWTH_PINE_TAIGA};
         return var2;
      } else if (var1.equals("FROZEN_MOUNTAINS")) {
         var2 = new Biome[]{Biome.JAGGED_PEAKS, Biome.FROZEN_PEAKS, Biome.SNOWY_SLOPES, Biome.GROVE};
         return var2;
      } else if (var1.equals("MOUNTAINS")) {
         var2 = new Biome[]{Biome.WINDSWEPT_HILLS, Biome.WINDSWEPT_GRAVELLY_HILLS, Biome.WINDSWEPT_FOREST, Biome.STONY_PEAKS};
         return var2;
      } else if (var1.equals("MUSHROOM_FIELDS")) {
         var2 = new Biome[]{Biome.MUSHROOM_FIELDS};
         return var2;
      } else if (var1.equals("PLAINS")) {
         var2 = new Biome[]{Biome.PLAINS, Biome.SUNFLOWER_PLAINS};
         return var2;
      } else if (var1.equals("RIVER")) {
         var2 = new Biome[]{Biome.RIVER};
         return var2;
      } else if (var1.equals("SAVANNA")) {
         var2 = new Biome[]{Biome.SAVANNA, Biome.SAVANNA_PLATEAU, Biome.WINDSWEPT_SAVANNA};
         return var2;
      } else if (var1.equals("FROZEN_BIOMES")) {
         var2 = new Biome[]{Biome.SNOWY_BEACH, Biome.SNOWY_TAIGA, Biome.SNOWY_PLAINS, Biome.FROZEN_RIVER, Biome.ICE_SPIKES, Biome.DEEP_FROZEN_OCEAN, Biome.FROZEN_OCEAN};
         return var2;
      } else if (var1.equals("CAVES")) {
         var2 = new Biome[]{Biome.DRIPSTONE_CAVES, Biome.LUSH_CAVES, Biome.DEEP_DARK};
         return var2;
      } else if (var1.equals("SWAMP")) {
         var2 = new Biome[]{Biome.SWAMP, Biome.MANGROVE_SWAMP};
         return var2;
      } else {
         var2 = new Biome[]{Biome.PLAINS};
         return var2;
      }
   }

   public Class<?> getNmsPacketClass(String var1) {
      return Class.forName("net.minecraft.network.protocol." + var1);
   }

   public void changeRegistryLock(boolean var1) {
      RegistryMaterials var2 = (RegistryMaterials)this.getBiomeRegistry();

      try {
         Class var3 = Class.forName("net.minecraft.core.RegistryMaterials");
         Field[] var4 = var3.getDeclaredFields();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Field var7 = var4[var6];
            if (var7.getType() == Boolean.TYPE) {
               var7.setAccessible(true);
               var7.setBoolean(var2, var1);
            }
         }
      } catch (IllegalAccessException | ClassNotFoundException var8) {
         var8.printStackTrace();
      }

   }

   public void refreshChunk(Player var1, Chunk var2) {
      try {
         CraftChunk var3 = (CraftChunk)var2;
         net.minecraft.world.level.chunk.Chunk var4 = (net.minecraft.world.level.chunk.Chunk)var3.getHandle(ChunkStatus.n);
         WorldServer var5 = ((CraftWorld)var2.getWorld()).getHandle();
         ClientboundLevelChunkWithLightPacket var6 = new ClientboundLevelChunkWithLightPacket(var4, var5.C_(), (BitSet)null, (BitSet)null);
         EntityPlayer var7 = ((CraftPlayer)var1).getHandle();
         Field var8 = var7.getClass().getDeclaredField("f");
         var8.setAccessible(true);
         ServerPlayerConnection var9 = (ServerPlayerConnection)var8.get(var7);
         var9.b(var6);
      } catch (IllegalArgumentException | SecurityException | NoSuchFieldException | IllegalAccessException var10) {
         var10.printStackTrace();
      }

   }

   public static void sendPacket(Player var0, Packet<?> var1) {
      if (!var0.hasMetadata("NPC")) {
         EntityPlayer var2 = ((CraftPlayer)var0).getHandle();

         try {
            Field var3 = var2.getClass().getDeclaredField("f");
            var3.setAccessible(true);
            ServerPlayerConnection var4 = (ServerPlayerConnection)var3.get(var2);
            var4.b(var1);
         } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var5) {
            var5.printStackTrace();
         }

      }
   }

   public void setBlockInNMSChunk(World var1, int var2, int var3, int var4, int var5, byte var6, boolean var7) {
      WorldServer var8 = ((CraftWorld)var1).getHandle();
      BlockPosition var9 = new BlockPosition(var2, var3, var4);
      IBlockData var10 = Block.a(var5 + (var6 << 12));
      var8.a(var9, var10, var7 ? 3 : 2);
   }

   public String getBiomeName(Location var1) {
      return this.getBiomeName(var1.getBlockX(), var1.getBlockY(), var1.getBlockZ(), var1.getWorld());
   }

   public String getBiomeName(int var1, int var2, int var3, World var4) {
      WorldServer var5 = ((CraftWorld)var4).getHandle();
      BiomeBase var6 = (BiomeBase)var5.getNoiseBiome(var1 >> 2, var2 >> 2, var3 >> 2).a();
      if (var6 == null) {
         return "void";
      } else {
         MinecraftKey var7 = this.getBiomeRegistry().b(var6);
         return var7 == null ? "void" : var7.a();
      }
   }

   public void setFrozen(Player var1, boolean var2) {
      CraftPlayer var3 = (CraftPlayer)var1;
      var3.setFreezeTicks(var3.getMaxFreezeTicks());
      if (var2) {
         if (var1.getHealth() > 0.0D) {
            var1.damage(1.0D, DamageSource.builder(DamageType.FREEZE).build());
         } else {
            var3.setFreezeTicks(0);
         }

      }
   }

   public boolean isInPowderedSnow(Entity var1) {
      return var1.getLocation().getBlock().getType() == Material.POWDER_SNOW;
   }

   public void sendGameStateChangePacket(int var1, float var2, Player var3) {
      PacketPlayOutGameStateChange var4 = new PacketPlayOutGameStateChange(new a(var1), var2);

      try {
         Object var5 = var3.getClass().getMethod("getHandle").invoke(var3);
         Field var6 = var5.getClass().getDeclaredField("b");
         var6.setAccessible(true);
         Object var7 = var6.get(var5);
         var7.getClass().getMethod("sendPacket", this.getNmsPacketClass("Packet")).invoke(var7, var4);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException | ClassNotFoundException var8) {
         var8.printStackTrace();
      }

   }

   public int getMinHeight(World var1) {
      return var1.getMinHeight();
   }

   public int getMaxHeight(World var1) {
      return var1.getMaxHeight();
   }

   private IRegistry<BiomeBase> getBiomeRegistry() {
      return this.dedicatedserver.ba().e(Registries.aI);
   }

   public void resendChunks(List<Chunk> var1) {
      WorldServer var2 = ((CraftWorld)((Chunk)var1.get(0)).getWorld()).getHandle();
      ArrayList var3 = new ArrayList();
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         Chunk var5 = (Chunk)var4.next();
         net.minecraft.world.level.chunk.Chunk var6 = (net.minecraft.world.level.chunk.Chunk)((CraftChunk)var5).getHandle(ChunkStatus.n);
         var3.add(var6);
      }

      var2.m().a.a(var3);
   }

   public int getSectionCount(World var1) {
      WorldServer var2 = ((CraftWorld)var1).getHandle();
      return var2.an();
   }
}
