package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

class PD<K, V> extends Node<K, V> implements NodeFactory<K, V> {
   protected static final long VALUE_OFFSET = UnsafeAccess.objectFieldOffset(PD.class, "value");
   volatile References.SoftValueReference<V> value;

   PD() {
   }

   PD(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      this(key, value, valueReferenceQueue, weight, now);
   }

   PD(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      UnsafeAccess.UNSAFE.putObject(this, VALUE_OFFSET, new References.SoftValueReference(keyReference, value, valueReferenceQueue));
   }

   public final Object getKeyReference() {
      References.SoftValueReference<V> valueRef = (References.SoftValueReference)this.getValueReference();
      return valueRef.getKeyReference();
   }

   public final K getKey() {
      References.SoftValueReference<V> valueRef = (References.SoftValueReference)this.getValueReference();
      return valueRef.getKeyReference();
   }

   public final V getValue() {
      Reference ref;
      Object referent;
      do {
         ref = (Reference)UnsafeAccess.UNSAFE.getObject(this, VALUE_OFFSET);
         referent = ref.get();
      } while(referent == null && ref != this.value);

      return referent;
   }

   public final Object getValueReference() {
      return UnsafeAccess.UNSAFE.getObject(this, VALUE_OFFSET);
   }

   public final void setValue(V value, ReferenceQueue<V> referenceQueue) {
      Reference<V> ref = (Reference)UnsafeAccess.UNSAFE.getObject(this, VALUE_OFFSET);
      UnsafeAccess.UNSAFE.putOrderedObject(this, VALUE_OFFSET, new References.SoftValueReference(this.getKeyReference(), value, referenceQueue));
      ref.clear();
   }

   public final boolean containsValue(Object value) {
      return this.getValue() == value;
   }

   public Node<K, V> newNode(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      return new PD(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
   }

   public Node<K, V> newNode(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      return new PD(keyReference, value, valueReferenceQueue, weight, now);
   }

   public boolean softValues() {
      return true;
   }

   public final boolean isAlive() {
      Object key = this.getKeyReference();
      return key != RETIRED_STRONG_KEY && key != DEAD_STRONG_KEY;
   }

   public final boolean isRetired() {
      return this.getKeyReference() == RETIRED_STRONG_KEY;
   }

   public final void retire() {
      References.SoftValueReference<V> valueRef = (References.SoftValueReference)this.getValueReference();
      valueRef.setKeyReference(RETIRED_STRONG_KEY);
      valueRef.clear();
   }

   public final boolean isDead() {
      return this.getKeyReference() == DEAD_STRONG_KEY;
   }

   public final void die() {
      References.SoftValueReference<V> valueRef = (References.SoftValueReference)this.getValueReference();
      valueRef.setKeyReference(DEAD_STRONG_KEY);
      valueRef.clear();
   }
}
