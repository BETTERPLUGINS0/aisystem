package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

import java.lang.ref.ReferenceQueue;

final class FSAMW<K, V> extends FSA<K, V> {
   int queueType;
   int weight;
   int policyWeight;

   FSAMW() {
   }

   FSAMW(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      super(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
      this.weight = weight;
   }

   FSAMW(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      super(keyReference, value, valueReferenceQueue, weight, now);
      this.weight = weight;
   }

   public int getQueueType() {
      return this.queueType;
   }

   public void setQueueType(int queueType) {
      this.queueType = queueType;
   }

   public int getWeight() {
      return this.weight;
   }

   public void setWeight(int weight) {
      this.weight = weight;
   }

   public int getPolicyWeight() {
      return this.policyWeight;
   }

   public void setPolicyWeight(int policyWeight) {
      this.policyWeight = policyWeight;
   }

   public Node<K, V> newNode(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      return new FSAMW(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
   }

   public Node<K, V> newNode(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      return new FSAMW(keyReference, value, valueReferenceQueue, weight, now);
   }
}
