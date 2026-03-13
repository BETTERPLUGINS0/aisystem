package com.volmit.iris.util.decree.handlers;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.decree.DecreeParameterHandler;
import com.volmit.iris.util.decree.exceptions.DecreeParsingException;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.math.RNG;
import java.util.concurrent.atomic.AtomicReference;

public class DoubleHandler implements DecreeParameterHandler<Double> {
   public KList<Double> getPossibilities() {
      return null;
   }

   public Double parse(String in, boolean force) {
      try {
         AtomicReference var3 = new AtomicReference(var1);
         double var4 = this.getMultiplier(var3);
         return Double.parseDouble((String)var3.get()) * var4;
      } catch (Throwable var6) {
         throw new DecreeParsingException("Unable to parse double \"" + var1 + "\"");
      }
   }

   public boolean supports(Class<?> type) {
      return var1.equals(Double.class) || var1.equals(Double.TYPE);
   }

   public String toString(Double f) {
      return var1.toString();
   }

   public String getRandomDefault() {
      double var10000 = RNG.r.d(0.0D, 99.99D);
      return Form.f(var10000, 1).makeConcatWithConstants<invokedynamic>(Form.f(var10000, 1));
   }
}
