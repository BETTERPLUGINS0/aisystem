package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

import java.lang.ref.ReferenceQueue;

final class FWAWMS<K, V> extends FWAW<K, V> {
   int queueType;

   FWAWMS() {
   }

   FWAWMS(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      super(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
   }

   FWAWMS(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      super(keyReference, value, valueReferenceQueue, weight, now);
   }

   public int getQueueType() {
      return this.queueType;
   }

   public void setQueueType(int queueType) {
      this.queueType = queueType;
   }

   public Node<K, V> newNode(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      return new FWAWMS(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
   }

   public Node<K, V> newNode(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      return new FWAWMS(keyReference, value, valueReferenceQueue, weight, now);
   }
}
