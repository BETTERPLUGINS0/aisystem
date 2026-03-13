package com.volmit.iris.util.decree.handlers;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.decree.DecreeParameterHandler;
import com.volmit.iris.util.decree.exceptions.DecreeParsingException;
import com.volmit.iris.util.math.RNG;
import java.util.concurrent.atomic.AtomicReference;

public class IntegerHandler implements DecreeParameterHandler<Integer> {
   public KList<Integer> getPossibilities() {
      return null;
   }

   public Integer parse(String in, boolean force) {
      try {
         AtomicReference var3 = new AtomicReference(var1);
         double var4 = this.getMultiplier(var3);
         return (int)(Integer.valueOf((String)var3.get()).doubleValue() * var4);
      } catch (Throwable var6) {
         throw new DecreeParsingException("Unable to parse integer \"" + var1 + "\"");
      }
   }

   public boolean supports(Class<?> type) {
      return var1.equals(Integer.class) || var1.equals(Integer.TYPE);
   }

   public String toString(Integer f) {
      return var1.toString();
   }

   public String getRandomDefault() {
      return RNG.r.i(0, 99).makeConcatWithConstants<invokedynamic>(RNG.r.i(0, 99));
   }
}
