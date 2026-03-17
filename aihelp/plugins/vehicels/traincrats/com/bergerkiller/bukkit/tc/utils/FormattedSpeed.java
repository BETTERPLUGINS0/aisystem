package com.bergerkiller.bukkit.tc.utils;

import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import java.util.Locale;

public class FormattedSpeed {
   public static final FormattedSpeed ZERO = of(0.0D);
   private final double _value;
   private final boolean _relative;
   private final double _unitMultiplier;
   private final String _unitName;

   public FormattedSpeed(double value, boolean isRelative, double unitMultiplier, String unitName) {
      this._value = value;
      this._relative = isRelative;
      this._unitMultiplier = unitMultiplier;
      this._unitName = unitName;
   }

   public double getValue() {
      return this._value;
   }

   public boolean isRelative() {
      return this._relative;
   }

   public double getUnitMultiplier() {
      return this._unitMultiplier;
   }

   public String getUnitName() {
      return this._unitName;
   }

   public static FormattedSpeed of(double value) {
      return new FormattedSpeed(value, false, 1.0D, "b/t");
   }

   public static FormattedSpeed parse(String velocityString, FormattedSpeed defaultValue) {
      String numberText = velocityString;
      String unitText = "";

      for(int i = 0; i < velocityString.length(); ++i) {
         char c = velocityString.charAt(i);
         if (!Character.isDigit(c) && c != '.' && c != ',' && c != ' ' && c != '-' && c != '+') {
            numberText = velocityString.substring(0, i);
            unitText = velocityString.substring(i).replace(" ", "").trim().toLowerCase(Locale.ENGLISH);
            break;
         }
      }

      boolean relative = numberText.startsWith("-") || numberText.startsWith("+");
      double value = ParseUtil.parseDouble(numberText, Double.NaN);
      if (Double.isNaN(value)) {
         return defaultValue;
      } else {
         double unitMultiplier = 1.0D;
         if (unitText.length() >= 3) {
            if (!unitText.equals("mph") && !unitText.equals("mphr")) {
               if (LogicUtil.contains(unitText, new String[]{"kmh", "kmph", "kmphr"})) {
                  unitText = "km/h";
               }
            } else {
               unitText = "mi/h";
            }

            int slashIndex = unitText.indexOf(47, 1);
            if (slashIndex != -1) {
               String num = unitText.substring(0, slashIndex);
               String den = unitText.substring(slashIndex + 1);
               if (!num.equals("k") && !num.equals("km")) {
                  if (num.equals("mi")) {
                     unitMultiplier *= 1609.344D;
                  } else if (num.equals("ft")) {
                     unitMultiplier *= 0.3048780487804878D;
                  }
               } else {
                  unitMultiplier *= 1000.0D;
               }

               if (LogicUtil.contains(den, new String[]{"s", "sec", "second"})) {
                  unitMultiplier /= 20.0D;
               } else if (LogicUtil.contains(den, new String[]{"h", "hr", "hour"})) {
                  unitMultiplier /= 72000.0D;
               }
            }

            value *= unitMultiplier;
         }

         return new FormattedSpeed(value, relative, unitMultiplier, unitText);
      }
   }
}
