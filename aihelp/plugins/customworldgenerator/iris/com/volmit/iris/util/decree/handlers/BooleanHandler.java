package com.volmit.iris.util.decree.handlers;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.decree.DecreeParameterHandler;
import com.volmit.iris.util.decree.exceptions.DecreeParsingException;
import com.volmit.iris.util.math.M;

public class BooleanHandler implements DecreeParameterHandler<Boolean> {
   public KList<Boolean> getPossibilities() {
      return null;
   }

   public String toString(Boolean aByte) {
      return var1.toString();
   }

   public Boolean parse(String in, boolean force) {
      try {
         return !var1.equals("null") && !var1.equals("other") && !var1.equals("flip") ? Boolean.parseBoolean(var1) : null;
      } catch (Throwable var4) {
         throw new DecreeParsingException("Unable to parse boolean \"" + var1 + "\"");
      }
   }

   public boolean supports(Class<?> type) {
      return var1.equals(Boolean.class) || var1.equals(Boolean.TYPE);
   }

   public String getRandomDefault() {
      return M.r(0.5D).makeConcatWithConstants<invokedynamic>(M.r(0.5D));
   }
}
