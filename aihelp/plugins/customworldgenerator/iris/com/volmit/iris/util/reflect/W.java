package com.volmit.iris.util.reflect;

import java.lang.StackWalker.Option;
import lombok.Generated;

public class W {
   private static final StackWalker stack;

   @Generated
   public static StackWalker getStack() {
      return stack;
   }

   static {
      stack = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE);
   }
}
