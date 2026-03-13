/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.utils.collections;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class UniquePriorityQueue<E>
extends PriorityQueue<E> {
    private final Set<E> set = new HashSet();

    public UniquePriorityQueue(Comparator<E> comparator) {
        super(comparator);
    }

    @Override
    public boolean offer(E e) {
        if (this.set.add(e)) {
            return super.offer(e);
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        return this.offer(e);
    }

    @Override
    public E poll() {
        Object e = super.poll();
        if (e != null) {
            this.set.remove(e);
        }
        return e;
    }

    @Override
    public boolean remove(Object object) {
        boolean bl = super.remove(object);
        if (bl) {
            this.set.remove(object);
        }
        return bl;
    }
}

