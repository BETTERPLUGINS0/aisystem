package com.volmit.iris.util.nbt.mca.palette;

public interface MCABiomeContainer {
   int[] getData();

   void setBiome(int x, int y, int z, int id);

   int getBiome(int x, int y, int z);
}
