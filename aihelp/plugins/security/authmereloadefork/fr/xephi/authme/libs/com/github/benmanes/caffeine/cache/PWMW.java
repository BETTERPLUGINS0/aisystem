package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

import java.lang.ref.ReferenceQueue;

final class PWMW<K, V> extends PW<K, V> {
   int queueType;
   int weight;
   int policyWeight;
   Node<K, V> previousInAccessOrder;
   Node<K, V> nextInAccessOrder;

   PWMW() {
   }

   PWMW(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      super(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
      this.weight = weight;
   }

   PWMW(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
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

   public Node<K, V> getPreviousInAccessOrder() {
      return this.previousInAccessOrder;
   }

   public void setPreviousInAccessOrder(Node<K, V> previousInAccessOrder) {
      this.previousInAccessOrder = previousInAccessOrder;
   }

   public Node<K, V> getNextInAccessOrder() {
      return this.nextInAccessOrder;
   }

   public void setNextInAccessOrder(Node<K, V> nextInAccessOrder) {
      this.nextInAccessOrder = nextInAccessOrder;
   }

   public Node<K, V> newNode(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      return new PWMW(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
   }

   public Node<K, V> newNode(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      return new PWMW(keyReference, value, valueReferenceQueue, weight, now);
   }
}
