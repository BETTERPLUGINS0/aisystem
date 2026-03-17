package me.PM2.infinitevehicles.commands;

import java.util.concurrent.atomic.AtomicInteger;

public class MessageType {
   private static final AtomicInteger counter = new AtomicInteger(1);
   public static final MessageType INFO = new MessageType();
   public static final MessageType SYNTAX = new MessageType();
   public static final MessageType ERROR = new MessageType();
   public static final MessageType HELP = new MessageType();
   private final int id;

   public MessageType() {
      this.id = counter.getAndIncrement();
   }

   public int hashCode() {
      return this.id;
   }

   public boolean equals(Object o) {
      return this == var1;
   }
}
