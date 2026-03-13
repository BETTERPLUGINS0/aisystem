package com.volmit.iris.core.pregenerator.methods;

import com.volmit.iris.core.pregenerator.PregenListener;
import com.volmit.iris.core.pregenerator.PregeneratorMethod;
import com.volmit.iris.util.mantle.Mantle;
import org.bukkit.World;

public class HybridPregenMethod implements PregeneratorMethod {
   private final PregeneratorMethod inWorld;
   private final World world;

   public HybridPregenMethod(World world, int threads) {
      this.world = var1;
      this.inWorld = new AsyncOrMedievalPregenMethod(var1, var2);
   }

   public String getMethod(int x, int z) {
      String var10000 = this.inWorld.getMethod(var1, var2);
      return "Hybrid<" + var10000 + ">";
   }

   public void init() {
      this.inWorld.init();
   }

   public void close() {
      this.inWorld.close();
   }

   public void save() {
      this.inWorld.save();
   }

   public boolean supportsRegions(int x, int z, PregenListener listener) {
      return this.inWorld.supportsRegions(var1, var2, var3);
   }

   public void generateRegion(int x, int z, PregenListener listener) {
      this.inWorld.generateRegion(var1, var2, var3);
   }

   public void generateChunk(int x, int z, PregenListener listener) {
      this.inWorld.generateChunk(var1, var2, var3);
   }

   public Mantle getMantle() {
      return this.inWorld.getMantle();
   }
}
