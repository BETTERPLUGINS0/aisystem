package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

enum SingletonWeigher implements Weigher<Object, Object> {
   INSTANCE;

   public int weigh(Object key, Object value) {
      return 1;
   }
}
