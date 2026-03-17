package me.PM2.infinitevehicles.locales;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageKey implements MessageKeyProvider {
   private static final AtomicInteger counter = new AtomicInteger(1);
   private static final Map<String, MessageKey> keyMap = new ConcurrentHashMap();
   private final int id;
   private final String key;

   private MessageKey(String key) {
      this.id = counter.getAndIncrement();
      this.key = var1;
   }

   public static MessageKey of(String key) {
      return (MessageKey)keyMap.computeIfAbsent(var0.toLowerCase().intern(), MessageKey::new);
   }

   public int hashCode() {
      return this.id;
   }

   public boolean equals(Object o) {
      return this == var1;
   }

   public String getKey() {
      return this.key;
   }

   public MessageKey getMessageKey() {
      return this;
   }
}
