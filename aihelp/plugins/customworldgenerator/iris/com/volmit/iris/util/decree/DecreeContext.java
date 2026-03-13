package com.volmit.iris.util.decree;

import com.volmit.iris.util.plugin.VolmitSender;

public class DecreeContext {
   private static final ThreadLocal<VolmitSender> context = new ThreadLocal();

   public static VolmitSender get() {
      return (VolmitSender)context.get();
   }

   public static void touch(VolmitSender c) {
      context.set(var0);
   }

   public static void remove() {
      context.remove();
   }
}
