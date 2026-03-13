package com.ryandw11.structure.utils;

public final class SpawnYConversion {
   public static String convertSpawnYValue(String value) {
      if (value.equalsIgnoreCase("top")) {
         return "top";
      } else if (value.equalsIgnoreCase("ocean_floor")) {
         return "ocean_floor";
      } else {
         String v;
         if (value.contains("[")) {
            int num1;
            int num2;
            String[] out;
            if (value.startsWith("+")) {
               v = value.replace("[", "").replace("]", "").replace("+", "");
               out = v.split("-");

               try {
                  num1 = Integer.parseInt(out[0]);
                  num2 = Integer.parseInt(out[1]);
                  return String.format("+[%s;%s]", num1, num2);
               } catch (ArrayIndexOutOfBoundsException | NumberFormatException var5) {
                  throw new IllegalArgumentException("Unable to convert SpawnY value since it is invalid!");
               }
            } else if (value.startsWith("-")) {
               v = value.replace("[", "").replace("]", "").replace("-", "");
               out = v.split("-");

               try {
                  num1 = Integer.parseInt(out[0]);
                  num2 = Integer.parseInt(out[1]);
                  return String.format("-[%s;%s]", num1, num2);
               } catch (ArrayIndexOutOfBoundsException | NumberFormatException var6) {
                  throw new IllegalArgumentException("Unable to convert SpawnY value since it is invalid!");
               }
            } else {
               v = value.replace("[", "").replace("]", "");
               out = v.split("-");

               try {
                  num1 = Integer.parseInt(out[0]);
                  num2 = Integer.parseInt(out[1]);
                  return String.format("[%s;%s]", num1, num2);
               } catch (ArrayIndexOutOfBoundsException | NumberFormatException var7) {
                  throw new IllegalArgumentException("Unable to convert SpawnY value since it is invalid!");
               }
            }
         } else if (value.startsWith("+")) {
            v = value.replace("+", "");

            try {
               return String.format("+[%s]", v);
            } catch (NumberFormatException var8) {
               throw new IllegalArgumentException("Unable to convert SpawnY value since it is invalid!");
            }
         } else if (value.startsWith("-")) {
            v = value.replace("-", "");

            try {
               int num = Integer.parseInt(v);
               return String.format("-[%s]", num);
            } catch (NumberFormatException var9) {
               throw new IllegalArgumentException("Unable to convert SpawnY value since it is invalid!");
            }
         } else {
            try {
               return value;
            } catch (NumberFormatException var10) {
               throw new IllegalArgumentException("Unable to convert SpawnY value since it is invalid!");
            }
         }
      }
   }
}
