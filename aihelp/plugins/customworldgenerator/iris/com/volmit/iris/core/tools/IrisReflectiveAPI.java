package com.volmit.iris.core.tools;

import com.volmit.iris.util.data.B;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

public class IrisReflectiveAPI {
   public static boolean isIrisWorld(World world) {
      return IrisToolbelt.isIrisWorld(var0);
   }

   public static boolean isIrisStudioWorld(World world) {
      return IrisToolbelt.isIrisStudioWorld(var0);
   }

   public static void registerCustomBlockData(String namespace, String key, BlockData blockData) {
      B.registerCustomBlockData(var0, var1, var2);
   }

   public static void retainMantleData(String classname) {
      IrisToolbelt.retainMantleDataForSlice(var0);
   }

   public static void setMantleData(World world, int x, int y, int z, Object data) {
      IrisToolbelt.access(var0).getEngine().getMantle().getMantle().set(var1, var2, var3, var4);
   }

   public static void deleteMantleData(World world, int x, int y, int z, Class c) {
      IrisToolbelt.access(var0).getEngine().getMantle().getMantle().remove(var1, var2, var3, var4);
   }

   public static Object getMantleData(World world, int x, int y, int z, Class c) {
      return IrisToolbelt.access(var0).getEngine().getMantle().getMantle().get(var1, var2, var3, var4);
   }
}
