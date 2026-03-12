package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

import java.lang.ref.ReferenceQueue;

final class FWRMS<K, V> extends FWR<K, V> {
   int queueType;
   Node<K, V> previousInAccessOrder;
   Node<K, V> nextInAccessOrder;

   FWRMS() {
   }

   FWRMS(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      super(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
   }

   FWRMS(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      super(keyReference, value, valueReferenceQueue, weight, now);
   }

   public int getQueueType() {
      return this.queueType;
   }

   public void setQueueType(int queueType) {
      this.queueType = queueType;
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
      return new FWRMS(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
   }

   public Node<K, V> newNode(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      return new FWRMS(keyReference, value, valueReferenceQueue, weight, now);
   }
}
