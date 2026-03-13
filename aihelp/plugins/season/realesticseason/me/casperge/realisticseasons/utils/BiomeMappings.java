package me.casperge.realisticseasons.utils;

import me.casperge.realisticseasons.RealisticSeasons;
import org.bukkit.block.Biome;

public class BiomeMappings {
   private RealisticSeasons main;

   public BiomeMappings(RealisticSeasons var1) {
      this.main = var1;
   }

   public int[] getBiomeIDs(String var1) {
      Biome[] var2 = this.main.getNMSUtils().getAssociatedBiomes(var1);
      int[] var3 = new int[var2.length];

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var3[var4] = this.main.getNMSUtils().getBiomeID(var2[var4]);
      }

      return var3;
   }

   public String[] getBiomeNames(String var1) {
      Biome[] var2 = this.main.getNMSUtils().getAssociatedBiomes(var1);
      String[] var3 = new String[var2.length];

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var3[var4] = this.main.getNMSUtils().getBiomeName(var2[var4]);
      }

      return var3;
   }

   public static String getGrassHex(String var0) {
      byte var2 = -1;
      switch(var0.hashCode()) {
      case -2095516483:
         if (var0.equals("JUNGLE")) {
            var2 = 1;
         }
         break;
      case -1932438551:
         if (var0.equals("PLAINS")) {
            var2 = 12;
         }
         break;
      case -1704274328:
         if (var0.equals("SAVANNA")) {
            var2 = 14;
         }
         break;
      case -1649691386:
         if (var0.equals("DARK_FOREST")) {
            var2 = 5;
         }
         break;
      case -1361454333:
         if (var0.equals("BADLANDS")) {
            var2 = 0;
         }
         break;
      case -1260330880:
         if (var0.equals("MUSHROOM_FIELDS")) {
            var2 = 11;
         }
         break;
      case -1050917018:
         if (var0.equals("MOUNTAINS")) {
            var2 = 10;
         }
         break;
      case -770796927:
         if (var0.equals("FLOWER_FOREST")) {
            var2 = 7;
         }
         break;
      case -752201556:
         if (var0.equals("BIRCH_FOREST")) {
            var2 = 3;
         }
         break;
      case -183194225:
         if (var0.equals("SNOWY_TUNDRA")) {
            var2 = 15;
         }
         break;
      case 63072579:
         if (var0.equals("BEACH")) {
            var2 = 2;
         }
         break;
      case 75022558:
         if (var0.equals("OCEAN")) {
            var2 = 4;
         }
         break;
      case 77988332:
         if (var0.equals("RIVER")) {
            var2 = 13;
         }
         break;
      case 79308992:
         if (var0.equals("SWAMP")) {
            var2 = 16;
         }
         break;
      case 79584598:
         if (var0.equals("TAIGA")) {
            var2 = 9;
         }
         break;
      case 2013046805:
         if (var0.equals("DESERT")) {
            var2 = 6;
         }
         break;
      case 2079510557:
         if (var0.equals("FOREST")) {
            var2 = 8;
         }
      }

      switch(var2) {
      case 0:
         return "9E814D";
      case 1:
         return "59C93C";
      case 2:
         return "91BD59";
      case 3:
         return "88BB67";
      case 4:
         return "8EB971";
      case 5:
         return "507A32";
      case 6:
         return "BFB755";
      case 7:
         return "79C05A";
      case 8:
         return "79C05A";
      case 9:
         return "86B783";
      case 10:
         return "8AB689";
      case 11:
         return "55C93F";
      case 12:
         return "91BD59";
      case 13:
         return "8EB971";
      case 14:
         return "BFB755";
      case 15:
         return "80B497";
      case 16:
         return "6A7039";
      default:
         return "CUSTOM";
      }
   }

   public static String getFoliageColor(String var0) {
      byte var2 = -1;
      switch(var0.hashCode()) {
      case -2095516483:
         if (var0.equals("JUNGLE")) {
            var2 = 1;
         }
         break;
      case -1932438551:
         if (var0.equals("PLAINS")) {
            var2 = 12;
         }
         break;
      case -1704274328:
         if (var0.equals("SAVANNA")) {
            var2 = 14;
         }
         break;
      case -1649691386:
         if (var0.equals("DARK_FOREST")) {
            var2 = 5;
         }
         break;
      case -1361454333:
         if (var0.equals("BADLANDS")) {
            var2 = 0;
         }
         break;
      case -1260330880:
         if (var0.equals("MUSHROOM_FIELDS")) {
            var2 = 11;
         }
         break;
      case -1050917018:
         if (var0.equals("MOUNTAINS")) {
            var2 = 10;
         }
         break;
      case -770796927:
         if (var0.equals("FLOWER_FOREST")) {
            var2 = 7;
         }
         break;
      case -752201556:
         if (var0.equals("BIRCH_FOREST")) {
            var2 = 3;
         }
         break;
      case -183194225:
         if (var0.equals("SNOWY_TUNDRA")) {
            var2 = 15;
         }
         break;
      case 63072579:
         if (var0.equals("BEACH")) {
            var2 = 2;
         }
         break;
      case 75022558:
         if (var0.equals("OCEAN")) {
            var2 = 4;
         }
         break;
      case 77988332:
         if (var0.equals("RIVER")) {
            var2 = 13;
         }
         break;
      case 79308992:
         if (var0.equals("SWAMP")) {
            var2 = 16;
         }
         break;
      case 79584598:
         if (var0.equals("TAIGA")) {
            var2 = 9;
         }
         break;
      case 2013046805:
         if (var0.equals("DESERT")) {
            var2 = 6;
         }
         break;
      case 2079510557:
         if (var0.equals("FOREST")) {
            var2 = 8;
         }
      }

      switch(var2) {
      case 0:
         return "9E814D";
      case 1:
         return "30BB0B";
      case 2:
         return "77AB2F";
      case 3:
         return "6BA941";
      case 4:
         return "71A74D";
      case 5:
         return "59AE30";
      case 6:
         return "AEA42A";
      case 7:
         return "59AE30";
      case 8:
         return "59AE30";
      case 9:
         return "68A464";
      case 10:
         return "6DA36B";
      case 11:
         return "2BBB0F";
      case 12:
         return "77AB2F";
      case 13:
         return "71A74D";
      case 14:
         return "AEA42A";
      case 15:
         return "60A17B";
      case 16:
         return "6A7039";
      default:
         return "CUSTOM";
      }
   }
}
