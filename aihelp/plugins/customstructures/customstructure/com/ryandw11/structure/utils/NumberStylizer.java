package com.ryandw11.structure.utils;

import com.ryandw11.structure.exceptions.StructureConfigurationException;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

public final class NumberStylizer {
   public static int getStylizedInt(String input) {
      if (input.contains(";")) {
         String v = input.replace("[", "").replace("]", "");
         String[] out = v.split(";");

         try {
            int num1 = Integer.parseInt(out[0]);
            int num2 = Integer.parseInt(out[1]);
            return ThreadLocalRandom.current().nextInt(num1, num2 + 1);
         } catch (ArrayIndexOutOfBoundsException | NumberFormatException var5) {
            return 1;
         }
      } else {
         try {
            return Integer.parseInt(input);
         } catch (NumberFormatException var6) {
            return 1;
         }
      }
   }

   public static Pair<Integer, Integer> parseRangedInput(String input) {
      if (input.contains(";")) {
         String v = input.replace("[", "").replace("]", "");
         String[] out = v.split(";");

         try {
            int num1 = Integer.parseInt(out[0]);
            int num2 = Integer.parseInt(out[1]);
            if (num1 > num2) {
               throw new NumberFormatException("Invalid Number Format: Number 1 is greater than Number 2!");
            } else {
               return Pair.of(num1, num2);
            }
         } catch (ArrayIndexOutOfBoundsException | NumberFormatException var5) {
            throw new NumberFormatException("Invalid Number Format: Input is not a range.");
         }
      } else {
         throw new NumberFormatException("Invalid Number Format: Input is not a range.");
      }
   }

   public static int retrieveRangedInput(String input) {
      if (input.contains(";")) {
         String v = input.replace("[", "").replace("]", "");
         String[] out = v.split(";");

         try {
            int num1 = Integer.parseInt(out[0]);
            int num2 = Integer.parseInt(out[1]);
            if (num1 > num2) {
               throw new NumberFormatException("Invalid Number Format: Number 1 is greater than Number 2!");
            } else {
               return ThreadLocalRandom.current().nextInt(num1, num2 + 1);
            }
         } catch (ArrayIndexOutOfBoundsException | NumberFormatException var5) {
            throw new NumberFormatException("Invalid Number Format: Input is not a range.");
         }
      } else {
         throw new NumberFormatException("Invalid Number Format: Input is not a range.");
      }
   }

   public static int getStylizedSpawnY(String value, @Nullable Location location) {
      boolean top = value.equalsIgnoreCase("top");
      if (location == null) {
         if (top) {
            throw new StructureConfigurationException("A structure that can spawn in the void must have an absolute spawn y value. Top is not absolute.");
         }

         if (value.startsWith("+")) {
            throw new StructureConfigurationException("A structure that can spawn in the void must have an absolute spawn y value. Relative value is not absolute.");
         }

         if (value.startsWith("-")) {
            throw new StructureConfigurationException("A structure that can spawn in the void must have an absolute spawn y value. Relative value is not absolute.");
         }
      }

      int currentHeight = -1;
      if (location != null) {
         currentHeight = location.getBlockY();
      }

      if (top) {
         return currentHeight;
      } else {
         String v;
         if (value.contains(";")) {
            int num1;
            int num2;
            int randomValue;
            String[] out;
            if (value.startsWith("+")) {
               v = value.replace("[", "").replace("]", "").replace("+", "");
               out = v.split(";");

               try {
                  num1 = Integer.parseInt(out[0]);
                  num2 = Integer.parseInt(out[1]);
                  if (num1 > num2) {
                     throw new StructureConfigurationException("SpawnY Value 1 must be greater than value 2 in '[value1;value2]'.");
                  } else {
                     randomValue = ThreadLocalRandom.current().nextInt(num1, num2 + 1);
                     return currentHeight + randomValue;
                  }
               } catch (ArrayIndexOutOfBoundsException | NumberFormatException var9) {
                  return currentHeight;
               }
            } else if (value.startsWith("-")) {
               v = value.replace("[", "").replace("]", "").replace("-", "");
               out = v.split(";");

               try {
                  num1 = Integer.parseInt(out[0]);
                  num2 = Integer.parseInt(out[1]);
                  if (num1 > num2) {
                     throw new StructureConfigurationException("SpawnY Value 1 must be greater than value 2 in '[value1;value2]'.");
                  } else {
                     randomValue = ThreadLocalRandom.current().nextInt(num1, num2 + 1);
                     return currentHeight - randomValue;
                  }
               } catch (ArrayIndexOutOfBoundsException | NumberFormatException var10) {
                  return currentHeight;
               }
            } else {
               v = value.replace("[", "").replace("]", "");
               out = v.split(";");

               try {
                  num1 = Integer.parseInt(out[0]);
                  num2 = Integer.parseInt(out[1]);
                  if (num1 > num2) {
                     throw new StructureConfigurationException("SpawnY Value 1 must be greater than value 2 in '[value1;value2]'.");
                  } else {
                     return ThreadLocalRandom.current().nextInt(num1, num2 + 1);
                  }
               } catch (ArrayIndexOutOfBoundsException | NumberFormatException var11) {
                  return currentHeight;
               }
            }
         } else {
            int num;
            if (value.startsWith("+[")) {
               v = value.replace("+", "").replace("[", "").replace("]", "");

               try {
                  num = Integer.parseInt(v);
                  return currentHeight + num;
               } catch (NumberFormatException var12) {
                  return currentHeight;
               }
            } else if (value.startsWith("-[")) {
               v = value.replace("-", "").replace("[", "").replace("]", "");

               try {
                  num = Integer.parseInt(v);
                  return currentHeight - num;
               } catch (NumberFormatException var13) {
                  return currentHeight;
               }
            } else {
               try {
                  return Integer.parseInt(value);
               } catch (NumberFormatException var14) {
                  return currentHeight;
               }
            }
         }
      }
   }
}
