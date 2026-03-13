package com.volmit.iris.util.decree;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.decree.exceptions.DecreeParsingException;
import java.util.concurrent.atomic.AtomicReference;

public interface DecreeParameterHandler<T> extends DecreeExecutor {
   KList<T> getPossibilities();

   default boolean isDummy() {
      return false;
   }

   String toString(T t);

   default String toStringForce(Object t) {
      return this.toString(t);
   }

   default T parse(String in) throws DecreeParsingException {
      return this.parse(in, false);
   }

   T parse(String in, boolean force) throws DecreeParsingException;

   boolean supports(Class<?> type);

   default KList<T> getPossibilities(String input) {
      KList possible;
      if (input.trim().isEmpty()) {
         possible = this.getPossibilities();
         return possible == null ? new KList() : possible;
      } else {
         input = input.trim();
         possible = this.getPossibilities();
         KList<T> matches = new KList();
         if (possible != null && !possible.isEmpty()) {
            if (input.isEmpty()) {
               return this.getPossibilities();
            } else {
               KList<String> converted = possible.convert((v) -> {
                  return this.toString(v).trim();
               });

               for(int i = 0; i < converted.size(); ++i) {
                  String g = (String)converted.get(i);
                  if (g.equalsIgnoreCase(input) || g.toLowerCase().contains(input.toLowerCase()) || input.toLowerCase().contains(g.toLowerCase())) {
                     matches.add((Object)possible.get(i));
                  }
               }

               return matches;
            }
         } else {
            return matches;
         }
      }
   }

   default String getRandomDefault() {
      return "NOEXAMPLE";
   }

   default double getMultiplier(AtomicReference<String> g) {
      double multiplier = 1.0D;
      String in = (String)g.get();
      boolean valid = true;

      while(valid) {
         boolean trim = false;
         if (in.toLowerCase().endsWith("k")) {
            multiplier *= 1000.0D;
            trim = true;
         } else if (in.toLowerCase().endsWith("m")) {
            multiplier *= 1000000.0D;
            trim = true;
         } else if (in.toLowerCase().endsWith("h")) {
            multiplier *= 100.0D;
            trim = true;
         } else if (in.toLowerCase().endsWith("c")) {
            multiplier *= 16.0D;
            trim = true;
         } else if (in.toLowerCase().endsWith("r")) {
            multiplier *= 512.0D;
            trim = true;
         } else {
            valid = false;
         }

         if (trim) {
            in = in.substring(0, in.length() - 1);
         }
      }

      g.set(in);
      return multiplier;
   }
}
