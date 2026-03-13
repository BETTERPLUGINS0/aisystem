package me.casperge.realisticseasons1_18_R2;

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
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.PacketPlayOutGameStateChange;
import net.minecraft.network.protocol.game.PacketPlayOutGameStateChange.a;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeFog;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_18_R2.CraftChunk;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class NmsCode_18_R2 implements NmsCode {
   private DedicatedServer dedicatedserver;

   public NmsCode_18_R2() {
      Server var1 = Bukkit.getServer();
      CraftServer var2 = (CraftServer)var1;
      this.dedicatedserver = var2.getServer();
   }

   public int getBiomeID(Biome var1) {
      BiomeBase var2 = (BiomeBase)CraftBlock.biomeToBiomeBase(this.dedicatedserver.aU().b(IRegistry.aP), var1).a();
      return this.dedicatedserver.aU().d(IRegistry.aP).a(var2);
   }

   public int getBiomeID(String var1) {
      ResourceKey var2;
      if (var1.contains(":")) {
         String[] var3 = var1.split(":");
         var2 = ResourceKey.a(IRegistry.aP, new MinecraftKey(var3[0].toLowerCase(), var3[1].toLowerCase()));
      } else {
         var2 = ResourceKey.a(IRegistry.aP, new MinecraftKey(var1.toLowerCase()));
      }

      IRegistry var5 = this.dedicatedserver.aU().b(IRegistry.aP);
      BiomeBase var4 = (BiomeBase)var5.a(var2);
      return this.dedicatedserver.aU().d(IRegistry.aP).a(var4);
   }

   public String getBiome(int var1) {
      if (var1 == 5555) {
         return "NONE";
      } else {
         IRegistry var2 = this.dedicatedserver.aU().b(IRegistry.aP);
         BiomeBase var3 = (BiomeBase)var2.a(var1);
         MinecraftKey var4 = var2.b(var3);
         String var5 = var4.b();
         if (var5 == null || var5 == "") {
            var5 = "minecraft";
         }

         String var6 = var4.a();
         return var5 + ":" + var6;
      }
   }

   public float getBiomeTemperature(String var1) {
      ResourceKey var2;
      if (var1.contains(":")) {
         String[] var3 = var1.split(":");
         var2 = ResourceKey.a(IRegistry.aP, new MinecraftKey(var3[0].toLowerCase(), var3[1].toLowerCase()));
      } else {
         var2 = ResourceKey.a(IRegistry.aP, new MinecraftKey(var1.toLowerCase()));
      }

      IRegistry var5 = this.dedicatedserver.aU().b(IRegistry.aP);
      BiomeBase var4 = (BiomeBase)var5.a(var2);
      return var4.i();
   }

   public List<String> getBiomes(String var1) {
      IRegistry var2 = this.dedicatedserver.aU().b(IRegistry.aP);
      ArrayList var3 = new ArrayList();
      Iterator var4 = var2.e().iterator();

      while(var4.hasNext()) {
         Entry var5 = (Entry)var4.next();
         MinecraftKey var6 = ((ResourceKey)var5.getKey()).a();
         String var7 = var6.a();
         String var8 = var6.b();
         if (var8 != null && var8.equals(var1)) {
            var3.add(var8 + ":" + var7);
         }
      }

      return var3;
   }

   public List<String> getCustomBiomes(String var1) {
      IRegistry var2 = this.dedicatedserver.aU().b(IRegistry.aP);
      ArrayList var3 = new ArrayList();
      Iterator var4 = var2.e().iterator();

      while(var4.hasNext()) {
         Entry var5 = (Entry)var4.next();
         MinecraftKey var6 = ((ResourceKey)var5.getKey()).a();
         String var7 = var6.a();
         String var8 = var6.b();
         if (var8 != null && !var8.equals("minecraft") && !var8.equals(var1)) {
            var3.add(var8 + ":" + var7);
         }
      }

      return var3;
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
         var2 = ResourceKey.a(IRegistry.aP, new MinecraftKey(var3[0].toLowerCase(), var3[1].toLowerCase()));
      } else {
         var2 = ResourceKey.a(IRegistry.aP, new MinecraftKey(var1.toLowerCase()));
      }

      IRegistry var10 = this.dedicatedserver.aU().b(IRegistry.aP);
      BiomeBase var4 = (BiomeBase)var10.a(var2);
      BiomeFog var5 = null;

      try {
         Field var6 = BiomeBase.class.getDeclaredField("m");
         var6.setAccessible(true);
         var5 = (BiomeFog)var6.get(var4);
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var9) {
         var9.printStackTrace();
      }

      int[] var11 = new int[]{this.getFieldInBiomea(var5, "b"), this.getFieldInBiomea(var5, "c"), this.getFieldInBiomea(var5, "d"), this.getFieldInBiomea(var5, "e"), -9999999, -9999999};
      Optional var7 = this.getOptionalFieldInBiomea(var5, "g");
      if (!var7.isEmpty()) {
         var11[4] = (Integer)var7.get();
      }

      Optional var8 = this.getOptionalFieldInBiomea(var5, "f");
      if (!var8.isEmpty()) {
         var11[5] = (Integer)var8.get();
      }

      return var11;
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

   public List<String> getAllBiomes() {
      IRegistry var1 = this.dedicatedserver.aU().b(IRegistry.aP);
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.e().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         MinecraftKey var5 = ((ResourceKey)var4.getKey()).a();
         String var6 = var5.a();
         String var7 = var5.b();
         var2.add(var7 + ":" + var6);
      }

      return var2;
   }

   public String getBiomeName(Biome var1) {
      BiomeBase var2 = (BiomeBase)CraftBlock.biomeToBiomeBase(this.dedicatedserver.aU().d(IRegistry.aP), var1).a();
      MinecraftKey var3 = this.dedicatedserver.aU().d(IRegistry.aP).b(var2);
      return var3.a();
   }

   public double getTPS() {
      return this.dedicatedserver.recentTps[0];
   }

   public Class<?> getCbClass(String var1) {
      String var10000 = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
      return Class.forName("org.bukkit.craftbukkit." + var10000 + "." + var1);
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
         var2 = new Biome[]{Biome.FLOWER_FOREST, Biome.MEADOW};
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
         var2 = new Biome[]{Biome.DRIPSTONE_CAVES, Biome.LUSH_CAVES};
         return var2;
      } else if (var1.equals("SWAMP")) {
         var2 = new Biome[]{Biome.SWAMP};
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
      try {
         RegistryMaterials var2;
         Field var3;
         if (var1) {
            var2 = (RegistryMaterials)this.dedicatedserver.aU().b(IRegistry.aP);
            var3 = var2.getClass().getDeclaredField("bL");
            var3.setAccessible(true);
            var3.set(var2, true);
         } else {
            var2 = (RegistryMaterials)this.dedicatedserver.aU().b(IRegistry.aP);
            var3 = var2.getClass().getDeclaredField("bL");
            var3.setAccessible(true);
            var3.set(var2, false);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void refreshChunk(Player var1, Chunk var2) {
      try {
         CraftChunk var3 = (CraftChunk)var2;
         net.minecraft.world.level.chunk.Chunk var4 = var3.getHandle();
         WorldServer var5 = ((CraftWorld)var2.getWorld()).getHandle();
         ClientboundLevelChunkWithLightPacket var6 = new ClientboundLevelChunkWithLightPacket(var4, var5.l_(), (BitSet)null, (BitSet)null, true);
         EntityPlayer var7 = ((CraftPlayer)var1).getHandle();
         Field var8 = var7.getClass().getDeclaredField("b");
         var8.setAccessible(true);
         PlayerConnection var9 = (PlayerConnection)var8.get(var7);
         var9.a(var6);
      } catch (IllegalArgumentException | SecurityException | NoSuchFieldException | IllegalAccessException var10) {
         var10.printStackTrace();
      }

   }

   public static void sendPacket(Player var0, Packet<?> var1) {
      if (!var0.hasMetadata("NPC")) {
         EntityPlayer var2 = ((CraftPlayer)var0).getHandle();

         try {
            Field var3 = var2.getClass().getDeclaredField("b");
            var3.setAccessible(true);
            PlayerConnection var4 = (PlayerConnection)var3.get(var2);
            var4.a(var1);
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
         MinecraftKey var7 = this.dedicatedserver.aU().d(IRegistry.aP).b(var6);
         return var7 == null ? "void" : var7.a();
      }
   }

   public void setFrozen(Player var1, boolean var2) {
      CraftPlayer var3 = (CraftPlayer)var1;
      var3.setFreezeTicks(var3.getMaxFreezeTicks());
      if (var2) {
         if (var1.getHealth() > 0.0D) {
            var1.damage(1.0D);
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

   public int getSectionCount(World var1) {
      WorldServer var2 = ((CraftWorld)var1).getHandle();
      return var2.ah();
   }
}
