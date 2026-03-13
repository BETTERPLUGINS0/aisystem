package com.volmit.iris.util.decree.handlers;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.decree.DecreeParameterHandler;

public class StringHandler implements DecreeParameterHandler<String> {
   public KList<String> getPossibilities() {
      return null;
   }

   public String toString(String s) {
      return var1;
   }

   public String parse(String in, boolean force) {
      return var1;
   }

   public boolean supports(Class<?> type) {
      return var1.equals(String.class);
   }

   public String getRandomDefault() {
      return (String)(new KList()).qadd("text").qadd("string").qadd("blah").qadd("derp").qadd("yolo").getRandom();
   }
}
