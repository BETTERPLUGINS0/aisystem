/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis;

import java.util.NoSuchElementException;

class IntQueue {
    private Entry head;
    private Entry tail;

    IntQueue() {
    }

    void add(int n) {
        Entry entry = new Entry(n);
        if (this.tail != null) {
            this.tail.next = entry;
        }
        this.tail = entry;
        if (this.head == null) {
            this.head = entry;
        }
    }

    boolean isEmpty() {
        return this.head == null;
    }

    int take() {
        if (this.head == null) {
            throw new NoSuchElementException();
        }
        int n = this.head.value;
        this.head = this.head.next;
        if (this.head == null) {
            this.tail = null;
        }
        return n;
    }

    private static class Entry {
        private Entry next;
        private int value;

        private Entry(int n) {
            this.value = n;
        }
    }
}

