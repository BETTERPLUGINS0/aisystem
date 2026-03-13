package com.volmit.iris.util.decree.handlers;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.decree.DecreeParameterHandler;
import com.volmit.iris.util.decree.exceptions.DecreeParsingException;
import com.volmit.iris.util.math.RNG;

public class ByteHandler implements DecreeParameterHandler<Byte> {
   public KList<Byte> getPossibilities() {
      return null;
   }

   public String toString(Byte aByte) {
      return var1.toString();
   }

   public Byte parse(String in, boolean force) {
      try {
         return Byte.parseByte(var1);
      } catch (Throwable var4) {
         throw new DecreeParsingException("Unable to parse byte \"" + var1 + "\"");
      }
   }

   public boolean supports(Class<?> type) {
      return var1.equals(Byte.class) || var1.equals(Byte.TYPE);
   }

   public String getRandomDefault() {
      return RNG.r.i(0, 127).makeConcatWithConstants<invokedynamic>(RNG.r.i(0, 127));
   }
}
