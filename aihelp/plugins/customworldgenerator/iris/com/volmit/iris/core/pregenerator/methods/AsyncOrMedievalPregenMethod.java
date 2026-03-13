package com.volmit.iris.core.pregenerator.methods;

import com.volmit.iris.core.pregenerator.PregenListener;
import com.volmit.iris.core.pregenerator.PregeneratorMethod;
import com.volmit.iris.util.mantle.Mantle;
import com.volmit.iris.util.paper.PaperLib;
import org.bukkit.World;

public class AsyncOrMedievalPregenMethod implements PregeneratorMethod {
   private final PregeneratorMethod method;

   public AsyncOrMedievalPregenMethod(World world, int threads) {
      this.method = (PregeneratorMethod)(PaperLib.isPaper() ? new AsyncPregenMethod(var1, var2) : new MedievalPregenMethod(var1));
   }

   public void init() {
      this.method.init();
   }

   public void close() {
      this.method.close();
   }

   public void save() {
      this.method.save();
   }

   public String getMethod(int x, int z) {
      return this.method.getMethod(var1, var2);
   }

   public boolean supportsRegions(int x, int z, PregenListener listener) {
      return false;
   }

   public void generateRegion(int x, int z, PregenListener listener) {
      throw new UnsupportedOperationException();
   }

   public void generateChunk(int x, int z, PregenListener listener) {
      this.method.generateChunk(var1, var2, var3);
   }

   public Mantle getMantle() {
      return this.method.getMantle();
   }
}
