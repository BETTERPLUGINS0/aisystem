package me.casperge.realisticseasons1_16_R3;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;
import me.casperge.interfaces.NmsCode;
import net.minecraft.server.v1_16_R3.BiomeBase;
import net.minecraft.server.v1_16_R3.BiomeFog;
import net.minecraft.server.v1_16_R3.Block;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.Chunk;
import net.minecraft.server.v1_16_R3.ChunkSection;
import net.minecraft.server.v1_16_R3.DedicatedServer;
import net.minecraft.server.v1_16_R3.IBlockData;
import net.minecraft.server.v1_16_R3.IRegistry;
import net.minecraft.server.v1_16_R3.IRegistryWritable;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutBlockChange;
import net.minecraft.server.v1_16_R3.PacketPlayOutGameStateChange;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import net.minecraft.server.v1_16_R3.ResourceKey;
import net.minecraft.server.v1_16_R3.WorldServer;
import net.minecraft.server.v1_16_R3.PacketPlayOutGameStateChange.a;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NmsCode_16_R3 implements NmsCode {
   ItemStack bukkitStack;
   net.minecraft.server.v1_16_R3.ItemStack nmsStack;
   private DedicatedServer dedicatedserver;

   public NmsCode_16_R3() {
      this.bukkitStack = new ItemStack(Material.BONE_MEAL);
      this.nmsStack = CraftItemStack.asNMSCopy(this.bukkitStack);
      Server var1 = Bukkit.getServer();
      CraftServer var2 = (CraftServer)var1;
      this.dedicatedserver = var2.getServer();
   }

   public double getTPS() {
      return this.dedicatedserver.recentTps[0];
   }

   public int getBiomeID(Biome var1) {
      BiomeBase var2 = CraftBlock.biomeToBiomeBase(this.dedicatedserver.getCustomRegistry().b(IRegistry.ay), var1);
      return this.dedicatedserver.getCustomRegistry().b(IRegistry.ay).a(var2);
   }

   public int getBiomeID(String var1) {
      ResourceKey var2;
      if (var1.contains(":")) {
         String[] var3 = var1.split(":");
         var2 = ResourceKey.a(IRegistry.ay, new MinecraftKey(var3[0].toLowerCase(), var3[1].toLowerCase()));
      } else {
         var2 = ResourceKey.a(IRegistry.ay, new MinecraftKey(var1.toLowerCase()));
      }

      IRegistryWritable var5 = this.dedicatedserver.getCustomRegistry().b(IRegistry.ay);
      BiomeBase var4 = (BiomeBase)var5.a(var2);
      return this.dedicatedserver.getCustomRegistry().b(IRegistry.ay).a(var4);
   }

   public String getBiome(int var1) {
      if (var1 == 5555) {
         return "NONE";
      } else {
         IRegistryWritable var2 = this.dedicatedserver.getCustomRegistry().b(IRegistry.ay);
         BiomeBase var3 = (BiomeBase)var2.fromId(var1);
         MinecraftKey var4 = var2.getKey(var3);
         String var5 = var4.getNamespace();
         if (var5 == null || var5 == "") {
            var5 = "minecraft";
         }

         String var6 = var4.getKey();
         return var5 + ":" + var6;
      }
   }

   public int[] getBiomeColors(String var1) {
      ResourceKey var2;
      if (var1.contains(":")) {
         String[] var3 = var1.split(":");
         var2 = ResourceKey.a(IRegistry.ay, new MinecraftKey(var3[0].toLowerCase(), var3[1].toLowerCase()));
      } else {
         var2 = ResourceKey.a(IRegistry.ay, new MinecraftKey(var1.toLowerCase()));
      }

      IRegistryWritable var10 = this.dedicatedserver.getCustomRegistry().b(IRegistry.ay);
      BiomeBase var4 = (BiomeBase)var10.a(var2);
      BiomeFog var5 = null;

      try {
         Field var6 = BiomeBase.class.getDeclaredField("p");
         var6.setAccessible(true);
         var5 = (BiomeFog)var6.get(var4);
      } catch (IllegalAccessException | NoSuchFieldException | SecurityException | IllegalArgumentException var9) {
         var9.printStackTrace();
      }

      int[] var11 = new int[]{this.getFieldInBiomea(var5, "b"), this.getFieldInBiomea(var5, "c"), this.getFieldInBiomea(var5, "d"), this.getFieldInBiomea(var5, "e"), -9999999, -9999999};
      Optional var7 = this.getOptionalFieldInBiomea(var5, "g");
      if (var7.isPresent()) {
         var11[4] = (Integer)var7.get();
      }

      Optional var8 = this.getOptionalFieldInBiomea(var5, "f");
      if (var8.isPresent()) {
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

   public float getBiomeTemperature(String var1) {
      ResourceKey var2;
      if (var1.contains(":")) {
         String[] var3 = var1.split(":");
         var2 = ResourceKey.a(IRegistry.ay, new MinecraftKey(var3[0].toLowerCase(), var3[1].toLowerCase()));
      } else {
         var2 = ResourceKey.a(IRegistry.ay, new MinecraftKey(var1.toLowerCase()));
      }

      IRegistryWritable var5 = this.dedicatedserver.getCustomRegistry().b(IRegistry.ay);
      BiomeBase var4 = (BiomeBase)var5.a(var2);
      return var4.k();
   }

   public String getBiomeName(Biome var1) {
      BiomeBase var2 = CraftBlock.biomeToBiomeBase(this.dedicatedserver.getCustomRegistry().b(IRegistry.ay), var1);
      MinecraftKey var3 = this.dedicatedserver.getCustomRegistry().b(IRegistry.ay).getKey(var2);
      return var3.getKey();
   }

   public Biome[] getAssociatedBiomes(String var1) {
      Biome[] var2;
      if (var1.equals("BADLANDS")) {
         var2 = new Biome[]{Biome.BADLANDS, Biome.MODIFIED_BADLANDS_PLATEAU, Biome.MODIFIED_WOODED_BADLANDS_PLATEAU, Biome.WOODED_BADLANDS_PLATEAU, Biome.ERODED_BADLANDS, Biome.BADLANDS_PLATEAU};
         return var2;
      } else if (var1.equals("JUNGLE")) {
         var2 = new Biome[]{Biome.JUNGLE, Biome.BAMBOO_JUNGLE, Biome.BAMBOO_JUNGLE_HILLS, Biome.JUNGLE_EDGE, Biome.JUNGLE_HILLS, Biome.MODIFIED_JUNGLE, Biome.MODIFIED_JUNGLE_EDGE};
         return var2;
      } else if (var1.equals("BEACH")) {
         var2 = new Biome[]{Biome.BEACH, Biome.STONE_SHORE};
         return var2;
      } else if (var1.equals("BIRCH_FOREST")) {
         var2 = new Biome[]{Biome.BIRCH_FOREST, Biome.BIRCH_FOREST_HILLS, Biome.TALL_BIRCH_FOREST, Biome.TALL_BIRCH_HILLS};
         return var2;
      } else if (var1.equals("OCEAN")) {
         var2 = new Biome[]{Biome.OCEAN, Biome.COLD_OCEAN, Biome.DEEP_COLD_OCEAN, Biome.DEEP_LUKEWARM_OCEAN, Biome.DEEP_OCEAN, Biome.LUKEWARM_OCEAN, Biome.WARM_OCEAN, Biome.DEEP_WARM_OCEAN};
         return var2;
      } else if (var1.equals("DARK_FOREST")) {
         var2 = new Biome[]{Biome.DARK_FOREST, Biome.DARK_FOREST_HILLS};
         return var2;
      } else if (var1.equals("DESERT")) {
         var2 = new Biome[]{Biome.DESERT, Biome.DESERT_HILLS, Biome.DESERT_LAKES};
         return var2;
      } else if (var1.equals("FLOWER_FOREST")) {
         var2 = new Biome[]{Biome.FLOWER_FOREST};
         return var2;
      } else if (var1.equals("CAVES")) {
         var2 = new Biome[0];
         return var2;
      } else if (var1.equals("FOREST")) {
         var2 = new Biome[]{Biome.FOREST, Biome.WOODED_HILLS};
         return var2;
      } else if (var1.equals("TAIGA")) {
         var2 = new Biome[]{Biome.TAIGA, Biome.GIANT_SPRUCE_TAIGA, Biome.GIANT_SPRUCE_TAIGA_HILLS, Biome.GIANT_TREE_TAIGA, Biome.GIANT_TREE_TAIGA_HILLS, Biome.TAIGA_HILLS, Biome.TAIGA_MOUNTAINS};
         return var2;
      } else if (var1.equals("MOUNTAINS")) {
         var2 = new Biome[]{Biome.MOUNTAINS, Biome.GRAVELLY_MOUNTAINS, Biome.MODIFIED_GRAVELLY_MOUNTAINS, Biome.WOODED_MOUNTAINS};
         return var2;
      } else if (var1.equals("MUSHROOM_FIELDS")) {
         var2 = new Biome[]{Biome.MUSHROOM_FIELDS, Biome.MUSHROOM_FIELD_SHORE};
         return var2;
      } else if (var1.equals("PLAINS")) {
         var2 = new Biome[]{Biome.PLAINS, Biome.SUNFLOWER_PLAINS};
         return var2;
      } else if (var1.equals("RIVER")) {
         var2 = new Biome[]{Biome.RIVER};
         return var2;
      } else if (var1.equals("SAVANNA")) {
         var2 = new Biome[]{Biome.SAVANNA, Biome.SAVANNA_PLATEAU, Biome.SHATTERED_SAVANNA, Biome.SHATTERED_SAVANNA_PLATEAU};
         return var2;
      } else if (var1.equals("FROZEN_MOUNTAINS")) {
         var2 = new Biome[0];
         return var2;
      } else if (var1.equals("FROZEN_BIOMES")) {
         var2 = new Biome[]{Biome.SNOWY_BEACH, Biome.SNOWY_MOUNTAINS, Biome.DEEP_FROZEN_OCEAN, Biome.FROZEN_OCEAN, Biome.SNOWY_TAIGA, Biome.SNOWY_TAIGA_HILLS, Biome.SNOWY_TAIGA_MOUNTAINS, Biome.SNOWY_TUNDRA, Biome.FROZEN_RIVER, Biome.ICE_SPIKES};
         return var2;
      } else if (var1.equals("SWAMP")) {
         var2 = new Biome[]{Biome.SWAMP_HILLS, Biome.SWAMP};
         return var2;
      } else {
         var2 = new Biome[]{Biome.PLAINS};
         return var2;
      }
   }

   public void changeRegistryLock(boolean var1) {
   }

   public void setBlockInNativeChunkSection(World var1, int var2, int var3, int var4, int var5, byte var6) {
      WorldServer var7 = ((CraftWorld)var1).getHandle();
      Chunk var8 = var7.getChunkAt(var2 >> 4, var4 >> 4);
      IBlockData var9 = Block.getByCombinedId(var5 + (var6 << 12));
      ChunkSection var10 = var8.getSections()[var3 >> 4];
      if (var10 == var8.a()) {
         var10 = new ChunkSection(var3 >> 4 << 4);
         var8.getSections()[var3 >> 4] = var10;
      }

      var10.setType(var2 & 15, var3 & 15, var4 & 15, var9);
   }

   public void sendManualChange(org.bukkit.block.Block var1, Player var2) {
      BlockPosition var3 = new BlockPosition(var1.getLocation().getBlockX(), var1.getLocation().getBlockY(), var1.getLocation().getBlockZ());
      PacketPlayOutBlockChange var4 = new PacketPlayOutBlockChange(((CraftWorld)var1.getWorld()).getHandle(), var3);
      ((CraftPlayer)var2).getHandle().playerConnection.sendPacket(var4);
   }

   public Class<?> getNmsPacketClass(String var1) {
      return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + var1);
   }

   public void refreshChunk(Player var1, org.bukkit.Chunk var2) {
      try {
         Object var3 = this.getCbClass("CraftChunk").cast(var2);
         Object var4 = var3.getClass().getMethod("getHandle").invoke(var3);
         Object var5 = this.getNmsPacketClass("PacketPlayOutMapChunk").getConstructor(var4.getClass(), Integer.TYPE).newInstance(var4, 65535);
         Object var6 = var1.getClass().getMethod("getHandle").invoke(var1);
         Object var7 = var6.getClass().getField("playerConnection").get(var6);
         var7.getClass().getMethod("sendPacket", this.getNmsPacketClass("Packet")).invoke(var7, var5);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException | NoSuchFieldException | ClassNotFoundException var8) {
         var8.printStackTrace();
      }

   }

   public static void sendPacket(Player var0, Packet<?> var1) {
      if (!var0.hasMetadata("NPC")) {
         try {
            Object var3 = var0.getClass().getMethod("getHandle").invoke(var0);
            PlayerConnection var2 = (PlayerConnection)var3.getClass().getField("playerConnection").get(var3);
            var2.sendPacket(var1);
         } catch (IllegalAccessException | NoSuchFieldException | SecurityException | InvocationTargetException | NoSuchMethodException | IllegalArgumentException var4) {
            var4.printStackTrace();
         }

      }
   }

   public Class<?> getCbClass(String var1) {
      return Class.forName("org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + var1);
   }

   public void setBlockInNMSChunk(World var1, int var2, int var3, int var4, int var5, byte var6, boolean var7) {
      try {
         Object var8 = this.getCbClass("CraftWorld").cast(var1);
         Object var9 = var8.getClass().getMethod("getHandle").invoke(var8);
         Object var10 = this.getNmsClass("BlockPosition").getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE).newInstance(var2, var3, var4);
         Object var11 = this.getNmsClass("Block").getMethod("getByCombinedId", Integer.TYPE).invoke(this.getNmsClass("Block"), var5 + (var6 << 12));
         var9.getClass().getMethod("setTypeAndData", var10.getClass(), var11.getClass(), Integer.TYPE).invoke(var9, var10, var11, var7 ? 3 : 2);
      } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException | ClassNotFoundException | IllegalAccessException var12) {
         var12.printStackTrace();
      }

   }

   private Class<?> getNmsClass(String var1) {
      return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + var1);
   }

   public String getBiomeName(Location var1) {
      return this.getBiomeName(var1.getBlockX(), var1.getBlockY(), var1.getBlockZ(), var1.getWorld());
   }

   public List<String> getCustomBiomes(String var1) {
      IRegistryWritable var2 = this.dedicatedserver.getCustomRegistry().b(IRegistry.ay);
      ArrayList var3 = new ArrayList();
      Iterator var4 = var2.d().iterator();

      while(var4.hasNext()) {
         Entry var5 = (Entry)var4.next();
         MinecraftKey var6 = ((ResourceKey)var5.getKey()).a();
         String var7 = var6.getKey();
         String var8 = var6.getNamespace();
         if (var8 != null && !var8.equals("minecraft") && !var8.equals(var1)) {
            var3.add(var8 + ":" + var7);
         }
      }

      return var3;
   }

   public List<String> getBiomes(String var1) {
      IRegistryWritable var2 = this.dedicatedserver.getCustomRegistry().b(IRegistry.ay);
      ArrayList var3 = new ArrayList();
      Iterator var4 = var2.d().iterator();

      while(var4.hasNext()) {
         Entry var5 = (Entry)var4.next();
         MinecraftKey var6 = ((ResourceKey)var5.getKey()).a();
         String var7 = var6.getKey();
         String var8 = var6.getNamespace();
         if (var8 != null && var8.equals(var1)) {
            var3.add(var8 + ":" + var7);
         }
      }

      return var3;
   }

   public List<String> getAllBiomes() {
      IRegistryWritable var1 = this.dedicatedserver.getCustomRegistry().b(IRegistry.ay);
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.d().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         MinecraftKey var5 = ((ResourceKey)var4.getKey()).a();
         String var6 = var5.getKey();
         String var7 = var5.getNamespace();
         var2.add(var7 + ":" + var6);
      }

      return var2;
   }

   public String getBiomeName(int var1, int var2, int var3, World var4) {
      WorldServer var5 = ((CraftWorld)var4).getHandle();
      BiomeBase var6 = var5.getBiome(var1 >> 2, var2 >> 2, var3 >> 2);
      if (var6 == null) {
         return "void";
      } else {
         MinecraftKey var7 = this.dedicatedserver.getCustomRegistry().b(IRegistry.ay).getKey(var6);
         return var7 == null ? "void" : var7.getKey();
      }
   }

   public void setFrozen(Player var1, boolean var2) {
      if (var2) {
         if (var1.getHealth() <= 1.0D) {
            var1.setHealth(0.0D);
         } else {
            var1.damage(1.0D);
         }

      }
   }

   public boolean isInPowderedSnow(Entity var1) {
      return false;
   }

   public void sendGameStateChangePacket(int var1, float var2, Player var3) {
      PacketPlayOutGameStateChange var4 = new PacketPlayOutGameStateChange(new a(var1), var2);

      try {
         Object var5 = var3.getClass().getMethod("getHandle").invoke(var3);
         Object var6 = var5.getClass().getField("playerConnection").get(var5);
         var6.getClass().getMethod("sendPacket", this.getNmsPacketClass("Packet")).invoke(var6, var4);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException | ClassNotFoundException var7) {
         var7.printStackTrace();
      }

   }

   public int getMinHeight(World var1) {
      return 0;
   }

   public int getMaxHeight(World var1) {
      return 256;
   }

   public int getSectionCount(World var1) {
      Bukkit.getLogger().severe("[RealisticSeasons] ERROR: unsupported getSectionCount call in " + this.getClass().getName());
      return 0;
   }
}
