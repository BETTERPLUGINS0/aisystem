package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

import java.util.function.Consumer;

enum DisabledBuffer implements Buffer<Object> {
   INSTANCE;

   public int offer(Object e) {
      return 0;
   }

   public void drainTo(Consumer<Object> consumer) {
   }

   public int size() {
      return 0;
   }

   public int reads() {
      return 0;
   }

   public int writes() {
      return 0;
   }
}
