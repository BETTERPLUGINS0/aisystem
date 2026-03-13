package me.casperge.realisticseasons.season;

import me.casperge.realisticseasons.data.LanguageManager;
import me.casperge.realisticseasons.data.MessageType;

public enum Season {
   WINTER("WINTER", 0) {
      public String toString() {
         return (String)LanguageManager.messages.get(MessageType.WINTER_DISPLAY);
      }
   },
   SPRING("SPRING", 2) {
      public String toString() {
         return (String)LanguageManager.messages.get(MessageType.SPRING_DISPLAY);
      }
   },
   SUMMER("SUMMER", 1) {
      public String toString() {
         return (String)LanguageManager.messages.get(MessageType.SUMMER_DISPLAY);
      }
   },
   FALL("FALL", 3) {
      public String toString() {
         return (String)LanguageManager.messages.get(MessageType.FALL_DISPLAY);
      }
   },
   DISABLED("DISABLED", 4) {
   },
   RESTORE("RESTORE", 5) {
   };

   private final String configName;
   private final int intValue;

   private Season(String param3, int param4) {
      this.configName = var3;
      this.intValue = var4;
   }

   public String getConfigName() {
      return this.configName;
   }

   public int intValue() {
      return this.intValue;
   }

   public static Season getSeason(String var0) {
      if (var0.equalsIgnoreCase("WINTER")) {
         return WINTER;
      } else if (var0.equalsIgnoreCase("SPRING")) {
         return SPRING;
      } else if (var0.equalsIgnoreCase("FALL")) {
         return FALL;
      } else if (var0.equalsIgnoreCase("SUMMER")) {
         return SUMMER;
      } else {
         return var0.equalsIgnoreCase("RESTORE") ? RESTORE : DISABLED;
      }
   }

   public Season getNextSeason() {
      switch(this.ordinal()) {
      case 0:
         return SPRING;
      case 1:
         return SUMMER;
      case 2:
         return FALL;
      case 3:
         return WINTER;
      case 4:
         return null;
      case 5:
         return null;
      default:
         return null;
      }
   }

   public Season getPreviousSeason() {
      switch(this.ordinal()) {
      case 0:
         return FALL;
      case 1:
         return WINTER;
      case 2:
         return SPRING;
      case 3:
         return SUMMER;
      case 4:
         return null;
      case 5:
         return null;
      default:
         return null;
      }
   }

   // $FF: synthetic method
   private static Season[] $values() {
      return new Season[]{WINTER, SPRING, SUMMER, FALL, DISABLED, RESTORE};
   }

   // $FF: synthetic method
   Season(String var3, int var4, Object var5) {
      this(var3, var4);
   }
}
