package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

class FD<K, V> extends Node<K, V> implements NodeFactory<K, V> {
   protected static final long VALUE_OFFSET = UnsafeAccess.objectFieldOffset(FD.class, "value");
   volatile References.SoftValueReference<V> value;

   FD() {
   }

   FD(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      this(new References.WeakKeyReference(key, keyReferenceQueue), value, valueReferenceQueue, weight, now);
   }

   FD(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      UnsafeAccess.UNSAFE.putObject(this, VALUE_OFFSET, new References.SoftValueReference(keyReference, value, valueReferenceQueue));
   }

   public final Object getKeyReference() {
      References.SoftValueReference<V> valueRef = (References.SoftValueReference)this.getValueReference();
      return valueRef.getKeyReference();
   }

   public final K getKey() {
      References.SoftValueReference<V> valueRef = (References.SoftValueReference)this.getValueReference();
      References.WeakKeyReference<K> keyRef = (References.WeakKeyReference)valueRef.getKeyReference();
      return keyRef.get();
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
      return new FD(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
   }

   public Node<K, V> newNode(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
      return new FD(keyReference, value, valueReferenceQueue, weight, now);
   }

   public Object newLookupKey(Object key) {
      return new References.LookupKeyReference(key);
   }

   public Object newReferenceKey(K key, ReferenceQueue<K> referenceQueue) {
      return new References.WeakKeyReference(key, referenceQueue);
   }

   public boolean softValues() {
      return true;
   }

   public final boolean isAlive() {
      Object key = this.getKeyReference();
      return key != RETIRED_WEAK_KEY && key != DEAD_WEAK_KEY;
   }

   public final boolean isRetired() {
      return this.getKeyReference() == RETIRED_WEAK_KEY;
   }

   public final void retire() {
      References.SoftValueReference<V> valueRef = (References.SoftValueReference)this.getValueReference();
      References.WeakKeyReference<K> keyRef = (References.WeakKeyReference)valueRef.getKeyReference();
      keyRef.clear();
      valueRef.setKeyReference(RETIRED_WEAK_KEY);
      valueRef.clear();
   }

   public final boolean isDead() {
      return this.getKeyReference() == DEAD_WEAK_KEY;
   }

   public final void die() {
      References.SoftValueReference<V> valueRef = (References.SoftValueReference)this.getValueReference();
      References.WeakKeyReference<K> keyRef = (References.WeakKeyReference)valueRef.getKeyReference();
      keyRef.clear();
      valueRef.setKeyReference(DEAD_WEAK_KEY);
      valueRef.clear();
   }
}
