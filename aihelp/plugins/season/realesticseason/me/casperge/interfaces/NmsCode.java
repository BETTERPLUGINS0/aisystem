package me.casperge.interfaces;

import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface NmsCode {
   Class<?> getNmsPacketClass(String var1) throws ClassNotFoundException;

   void refreshChunk(Player var1, Chunk var2);

   void setBlockInNMSChunk(World var1, int var2, int var3, int var4, int var5, byte var6, boolean var7);

   int getBiomeID(String var1);

   String getBiomeName(Location var1);

   String getBiomeName(int var1, int var2, int var3, World var4);

   String getBiomeName(Biome var1);

   List<String> getBiomes(String var1);

   List<String> getCustomBiomes(String var1);

   List<String> getAllBiomes();

   String getBiome(int var1);

   void setFrozen(Player var1, boolean var2);

   boolean isInPowderedSnow(Entity var1);

   Biome[] getAssociatedBiomes(String var1);

   void sendGameStateChangePacket(int var1, float var2, Player var3);

   int getBiomeID(Biome var1);

   int getMinHeight(World var1);

   int getMaxHeight(World var1);

   void changeRegistryLock(boolean var1);

   double getTPS();

   float getBiomeTemperature(String var1);

   int[] getBiomeColors(String var1);

   int getSectionCount(World var1);
}
