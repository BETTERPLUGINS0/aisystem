/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.scopedpool;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SoftValueHashMap<K, V>
implements Map<K, V> {
    private Map<K, SoftValueRef<K, V>> hash;
    private ReferenceQueue<V> queue = new ReferenceQueue();

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        this.processQueue();
        HashSet<Map.Entry<K, V>> hashSet = new HashSet<Map.Entry<K, V>>();
        for (Map.Entry<K, SoftValueRef<K, V>> entry : this.hash.entrySet()) {
            hashSet.add(new AbstractMap.SimpleImmutableEntry(entry.getKey(), entry.getValue().get()));
        }
        return hashSet;
    }

    private void processQueue() {
        if (!this.hash.isEmpty()) {
            Reference<V> reference;
            while ((reference = this.queue.poll()) != null) {
                if (!(reference instanceof SoftValueRef)) continue;
                SoftValueRef softValueRef = (SoftValueRef)reference;
                if (reference != this.hash.get(softValueRef.key)) continue;
                this.hash.remove(softValueRef.key);
            }
        }
    }

    public SoftValueHashMap(int n, float f) {
        this.hash = new ConcurrentHashMap<K, SoftValueRef<K, V>>(n, f);
    }

    public SoftValueHashMap(int n) {
        this.hash = new ConcurrentHashMap<K, SoftValueRef<K, V>>(n);
    }

    public SoftValueHashMap() {
        this.hash = new ConcurrentHashMap<K, SoftValueRef<K, V>>();
    }

    public SoftValueHashMap(Map<K, V> map) {
        this(Math.max(2 * map.size(), 11), 0.75f);
        this.putAll(map);
    }

    @Override
    public int size() {
        this.processQueue();
        return this.hash.size();
    }

    @Override
    public boolean isEmpty() {
        this.processQueue();
        return this.hash.isEmpty();
    }

    @Override
    public boolean containsKey(Object object) {
        this.processQueue();
        return this.hash.containsKey(object);
    }

    @Override
    public V get(Object object) {
        this.processQueue();
        return this.valueOrNull(this.hash.get(object));
    }

    @Override
    public V put(K k, V v) {
        this.processQueue();
        return this.valueOrNull(this.hash.put(k, SoftValueRef.create(k, v, this.queue)));
    }

    @Override
    public V remove(Object object) {
        this.processQueue();
        return this.valueOrNull(this.hash.remove(object));
    }

    @Override
    public void clear() {
        this.processQueue();
        this.hash.clear();
    }

    @Override
    public boolean containsValue(Object object) {
        this.processQueue();
        if (null == object) {
            return false;
        }
        for (SoftValueRef<K, V> softValueRef : this.hash.values()) {
            if (null == softValueRef || !object.equals(softValueRef.get())) continue;
            return true;
        }
        return false;
    }

    @Override
    public Set<K> keySet() {
        this.processQueue();
        return this.hash.keySet();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        this.processQueue();
        for (K k : map.keySet()) {
            this.put(k, map.get(k));
        }
    }

    @Override
    public Collection<V> values() {
        this.processQueue();
        ArrayList arrayList = new ArrayList();
        for (SoftValueRef<K, V> softValueRef : this.hash.values()) {
            arrayList.add(softValueRef.get());
        }
        return arrayList;
    }

    private V valueOrNull(SoftValueRef<K, V> softValueRef) {
        if (null == softValueRef) {
            return null;
        }
        return (V)softValueRef.get();
    }

    private static class SoftValueRef<K, V>
    extends SoftReference<V> {
        public K key;

        private SoftValueRef(K k, V v, ReferenceQueue<V> referenceQueue) {
            super(v, referenceQueue);
            this.key = k;
        }

        private static <K, V> SoftValueRef<K, V> create(K k, V v, ReferenceQueue<V> referenceQueue) {
            if (v == null) {
                return null;
            }
            return new SoftValueRef<K, V>(k, v, referenceQueue);
        }
    }
}

