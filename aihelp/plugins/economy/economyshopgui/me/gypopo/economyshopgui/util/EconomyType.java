package me.gypopo.economyshopgui.util;

public enum EconomyType {
   EXP("EXP"),
   ITEM("Item"),
   VAULT("Vault"),
   LEVELS("Levels"),
   ECOBITS("EcoBits"),
   COINS_ENGINE("CoinsEngine"),
   PLAYER_POINTS("PlayerPoints"),
   GEMS_ECONOMY("GemsEconomy"),
   ULTRA_ECONOMY("UltraEconomy"),
   VOTING_PLUGIN("VotingPlugin"),
   ZESSENTIALS("zEssentials");

   private final String name;

   private EconomyType(String param3) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public static EcoType getFromString(String type) {
      if (type == null) {
         return null;
      } else {
         String var2 = type.split(":")[0];
         byte var3 = -1;
         switch(var2.hashCode()) {
         case -1787232961:
            if (var2.equals("COINS_ENGINE")) {
               var3 = 2;
            }
            break;
         case -1135756609:
            if (var2.equals("ZESSENTIALS")) {
               var3 = 7;
            }
            break;
         case -129339391:
            if (var2.equals("PLAYER_POINTS")) {
               var3 = 0;
            }
            break;
         case 69117:
            if (var2.equals("EXP")) {
               var3 = 6;
            }
            break;
         case 81443346:
            if (var2.equals("VAULT")) {
               var3 = 1;
            }
            break;
         case 362825699:
            if (var2.equals("GEMS_ECONOMY")) {
               var3 = 3;
            }
            break;
         case 778683723:
            if (var2.equals("VOTING_PLUGIN")) {
               var3 = 5;
            }
            break;
         case 2141162603:
            if (var2.equals("ULTRA_ECONOMY")) {
               var3 = 4;
            }
         }

         EcoType economyType;
         switch(var3) {
         case 0:
            economyType = new EcoType(PLAYER_POINTS);
            break;
         case 1:
            economyType = new EcoType(VAULT);
            break;
         case 2:
            if (type.contains(":")) {
               economyType = new EcoType(COINS_ENGINE, type.split(":")[1]);
            } else {
               economyType = new EcoType(COINS_ENGINE);
            }
            break;
         case 3:
            if (type.contains(":")) {
               economyType = new EcoType(GEMS_ECONOMY, type.split(":")[1]);
            } else {
               economyType = new EcoType(GEMS_ECONOMY);
            }
            break;
         case 4:
            if (type.contains(":")) {
               economyType = new EcoType(ULTRA_ECONOMY, type.split(":")[1]);
            } else {
               economyType = new EcoType(ULTRA_ECONOMY);
            }
            break;
         case 5:
            economyType = new EcoType(VOTING_PLUGIN);
            break;
         case 6:
            economyType = new EcoType(EXP);
            break;
         case 7:
            if (type.contains(":")) {
               economyType = new EcoType(ZESSENTIALS, type.split(":")[1]);
            } else {
               economyType = new EcoType(ZESSENTIALS);
            }
            break;
         default:
            economyType = null;
         }

         return economyType;
      }
   }

   // $FF: synthetic method
   private static EconomyType[] $values() {
      return new EconomyType[]{EXP, ITEM, VAULT, LEVELS, ECOBITS, COINS_ENGINE, PLAYER_POINTS, GEMS_ECONOMY, ULTRA_ECONOMY, VOTING_PLUGIN, ZESSENTIALS};
   }
}
