package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

import java.lang.ref.ReferenceQueue;

class FSA<K, V> extends FS<K, V> {
   protected static final long ACCESS_TIME_OFFSET = UnsafeAccess.objectFieldOffset(FSA.class, "accessTime");
   volatile long accessTime;
   Node<K, V> previousInAccessOrder;
   Node<K, V> nextInAccessOrder;

   FSA() {
   }

   FSA(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      super(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
      UnsafeAccess.UNSAFE.putLong(this, ACCESS_TIME_OFFSET, now);
   }

   FSA(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      super(keyReference, value, valueReferenceQueue, weight, now);
      UnsafeAccess.UNSAFE.putLong(this, ACCESS_TIME_OFFSET, now);
   }

   public Node<K, V> getPreviousInVariableOrder() {
      return this.previousInAccessOrder;
   }

   public void setPreviousInVariableOrder(Node<K, V> previousInAccessOrder) {
      this.previousInAccessOrder = previousInAccessOrder;
   }

   public Node<K, V> getNextInVariableOrder() {
      return this.nextInAccessOrder;
   }

   public void setNextInVariableOrder(Node<K, V> nextInAccessOrder) {
      this.nextInAccessOrder = nextInAccessOrder;
   }

   public long getVariableTime() {
      return UnsafeAccess.UNSAFE.getLong(this, ACCESS_TIME_OFFSET);
   }

   public void setVariableTime(long accessTime) {
      UnsafeAccess.UNSAFE.putLong(this, ACCESS_TIME_OFFSET, accessTime);
   }

   public boolean casVariableTime(long expect, long update) {
      return this.accessTime == expect && UnsafeAccess.UNSAFE.compareAndSwapLong(this, ACCESS_TIME_OFFSET, expect, update);
   }

   public final long getAccessTime() {
      return UnsafeAccess.UNSAFE.getLong(this, ACCESS_TIME_OFFSET);
   }

   public final void setAccessTime(long accessTime) {
      UnsafeAccess.UNSAFE.putLong(this, ACCESS_TIME_OFFSET, accessTime);
   }

   public final Node<K, V> getPreviousInAccessOrder() {
      return this.previousInAccessOrder;
   }

   public final void setPreviousInAccessOrder(Node<K, V> previousInAccessOrder) {
      this.previousInAccessOrder = previousInAccessOrder;
   }

   public final Node<K, V> getNextInAccessOrder() {
      return this.nextInAccessOrder;
   }

   public final void setNextInAccessOrder(Node<K, V> nextInAccessOrder) {
      this.nextInAccessOrder = nextInAccessOrder;
   }

   public Node<K, V> newNode(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      return new FSA(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
   }

   public Node<K, V> newNode(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      return new FSA(keyReference, value, valueReferenceQueue, weight, now);
   }
}
