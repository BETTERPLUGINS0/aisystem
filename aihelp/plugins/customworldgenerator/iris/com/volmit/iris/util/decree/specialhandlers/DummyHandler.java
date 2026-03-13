package com.volmit.iris.util.decree.specialhandlers;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.decree.DecreeParameterHandler;

public class DummyHandler implements DecreeParameterHandler<Object> {
   public KList getPossibilities() {
      return null;
   }

   public boolean isDummy() {
      return true;
   }

   public String toString(Object o) {
      return null;
   }

   public Object parse(String in, boolean force) {
      return null;
   }

   public boolean supports(Class type) {
      return false;
   }
}
