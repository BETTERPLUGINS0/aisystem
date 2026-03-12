package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

import java.lang.ref.ReferenceQueue;

class PDW<K, V> extends PD<K, V> {
   protected static final long WRITE_TIME_OFFSET = UnsafeAccess.objectFieldOffset(PDW.class, "writeTime");
   volatile long writeTime;
   Node<K, V> previousInWriteOrder;
   Node<K, V> nextInWriteOrder;

   PDW() {
   }

   PDW(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      super(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
      UnsafeAccess.UNSAFE.putLong(this, WRITE_TIME_OFFSET, now);
   }

   PDW(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      super(keyReference, value, valueReferenceQueue, weight, now);
      UnsafeAccess.UNSAFE.putLong(this, WRITE_TIME_OFFSET, now);
   }

   public Node<K, V> getPreviousInVariableOrder() {
      return this.previousInWriteOrder;
   }

   public void setPreviousInVariableOrder(Node<K, V> previousInWriteOrder) {
      this.previousInWriteOrder = previousInWriteOrder;
   }

   public Node<K, V> getNextInVariableOrder() {
      return this.nextInWriteOrder;
   }

   public void setNextInVariableOrder(Node<K, V> nextInWriteOrder) {
      this.nextInWriteOrder = nextInWriteOrder;
   }

   public long getVariableTime() {
      return UnsafeAccess.UNSAFE.getLong(this, WRITE_TIME_OFFSET);
   }

   public void setVariableTime(long writeTime) {
      UnsafeAccess.UNSAFE.putLong(this, WRITE_TIME_OFFSET, writeTime);
   }

   public boolean casVariableTime(long expect, long update) {
      return this.writeTime == expect && UnsafeAccess.UNSAFE.compareAndSwapLong(this, WRITE_TIME_OFFSET, expect, update);
   }

   public final long getWriteTime() {
      return UnsafeAccess.UNSAFE.getLong(this, WRITE_TIME_OFFSET);
   }

   public final void setWriteTime(long writeTime) {
      UnsafeAccess.UNSAFE.putLong(this, WRITE_TIME_OFFSET, writeTime);
   }

   public final Node<K, V> getPreviousInWriteOrder() {
      return this.previousInWriteOrder;
   }

   public final void setPreviousInWriteOrder(Node<K, V> previousInWriteOrder) {
      this.previousInWriteOrder = previousInWriteOrder;
   }

   public final Node<K, V> getNextInWriteOrder() {
      return this.nextInWriteOrder;
   }

   public final void setNextInWriteOrder(Node<K, V> nextInWriteOrder) {
      this.nextInWriteOrder = nextInWriteOrder;
   }

   public Node<K, V> newNode(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      return new PDW(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
   }

   public Node<K, V> newNode(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      return new PDW(keyReference, value, valueReferenceQueue, weight, now);
   }
}
