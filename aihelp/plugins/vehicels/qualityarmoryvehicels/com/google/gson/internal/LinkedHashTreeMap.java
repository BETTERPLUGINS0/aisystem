/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.google.gson.internal;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public final class LinkedHashTreeMap<K, V>
extends AbstractMap<K, V>
implements Serializable {
    private static final Comparator<Comparable> NATURAL_ORDER = new Comparator<Comparable>(){

        @Override
        public int compare(Comparable comparable, Comparable comparable2) {
            return comparable.compareTo(comparable2);
        }
    };
    Comparator<? super K> comparator;
    Node<K, V>[] table;
    final Node<K, V> header;
    int size = 0;
    int modCount = 0;
    int threshold;
    private EntrySet entrySet;
    private KeySet keySet;

    public LinkedHashTreeMap() {
        this(NATURAL_ORDER);
    }

    public LinkedHashTreeMap(Comparator<? super K> comparator) {
        this.comparator = comparator != null ? comparator : NATURAL_ORDER;
        this.header = new Node();
        this.table = new Node[16];
        this.threshold = this.table.length / 2 + this.table.length / 4;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public V get(Object object) {
        Node<K, V> node = this.findByObject(object);
        return node != null ? (V)node.value : null;
    }

    @Override
    public boolean containsKey(Object object) {
        return this.findByObject(object) != null;
    }

    @Override
    public V put(K k, V v) {
        if (k == null) {
            throw new NullPointerException("key == null");
        }
        Node<K, V> node = this.find(k, true);
        Object v2 = node.value;
        node.value = v;
        return v2;
    }

    @Override
    public void clear() {
        Arrays.fill(this.table, null);
        this.size = 0;
        ++this.modCount;
        Node<K, V> node = this.header;
        Node node2 = node.next;
        while (node2 != node) {
            Node node3 = node2.next;
            node2.prev = null;
            node2.next = null;
            node2 = node3;
        }
        node.prev = node;
        node.next = node.prev;
    }

    @Override
    public V remove(Object object) {
        Node<K, V> node = this.removeInternalByKey(object);
        return node != null ? (V)node.value : null;
    }

    Node<K, V> find(K k, boolean bl) {
        Node<K, V> node;
        Object object;
        Comparator<K> comparator = this.comparator;
        Node<K, V>[] nodeArray = this.table;
        int n = LinkedHashTreeMap.secondaryHash(k.hashCode());
        int n2 = n & nodeArray.length - 1;
        Node<K, V> node2 = nodeArray[n2];
        int n3 = 0;
        if (node2 != null) {
            object = comparator == NATURAL_ORDER ? (Comparable)k : null;
            while (true) {
                int n4 = n3 = object != null ? object.compareTo(node2.key) : comparator.compare(k, node2.key);
                if (n3 == 0) {
                    return node2;
                }
                Node node3 = node = n3 < 0 ? node2.left : node2.right;
                if (node == null) break;
                node2 = node;
            }
        }
        if (!bl) {
            return null;
        }
        object = this.header;
        if (node2 == null) {
            if (comparator == NATURAL_ORDER && !(k instanceof Comparable)) {
                throw new ClassCastException(k.getClass().getName() + " is not Comparable");
            }
            node = new Node<K, V>(node2, k, n, object, ((Node)object).prev);
            nodeArray[n2] = node;
        } else {
            node = new Node<K, V>(node2, k, n, object, ((Node)object).prev);
            if (n3 < 0) {
                node2.left = node;
            } else {
                node2.right = node;
            }
            this.rebalance(node2, true);
        }
        if (this.size++ > this.threshold) {
            this.doubleCapacity();
        }
        ++this.modCount;
        return node;
    }

    Node<K, V> findByObject(Object object) {
        try {
            return object != null ? this.find(object, false) : null;
        } catch (ClassCastException classCastException) {
            return null;
        }
    }

    Node<K, V> findByEntry(Map.Entry<?, ?> entry) {
        Node<K, V> node = this.findByObject(entry.getKey());
        boolean bl = node != null && this.equal(node.value, entry.getValue());
        return bl ? node : null;
    }

    private boolean equal(Object object, Object object2) {
        return object == object2 || object != null && object.equals(object2);
    }

    private static int secondaryHash(int n) {
        n ^= n >>> 20 ^ n >>> 12;
        return n ^ n >>> 7 ^ n >>> 4;
    }

    void removeInternal(Node<K, V> node, boolean bl) {
        if (bl) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            node.prev = null;
            node.next = null;
        }
        Node node2 = node.left;
        Node node3 = node.right;
        Node node4 = node.parent;
        if (node2 != null && node3 != null) {
            Node node5 = node2.height > node3.height ? node2.last() : node3.first();
            this.removeInternal(node5, false);
            int n = 0;
            node2 = node.left;
            if (node2 != null) {
                n = node2.height;
                node5.left = node2;
                node2.parent = node5;
                node.left = null;
            }
            int n2 = 0;
            node3 = node.right;
            if (node3 != null) {
                n2 = node3.height;
                node5.right = node3;
                node3.parent = node5;
                node.right = null;
            }
            node5.height = Math.max(n, n2) + 1;
            this.replaceInParent(node, node5);
            return;
        }
        if (node2 != null) {
            this.replaceInParent(node, node2);
            node.left = null;
        } else if (node3 != null) {
            this.replaceInParent(node, node3);
            node.right = null;
        } else {
            this.replaceInParent(node, null);
        }
        this.rebalance(node4, false);
        --this.size;
        ++this.modCount;
    }

    Node<K, V> removeInternalByKey(Object object) {
        Node<K, V> node = this.findByObject(object);
        if (node != null) {
            this.removeInternal(node, true);
        }
        return node;
    }

    private void replaceInParent(Node<K, V> node, Node<K, V> node2) {
        Node node3 = node.parent;
        node.parent = null;
        if (node2 != null) {
            node2.parent = node3;
        }
        if (node3 != null) {
            if (node3.left == node) {
                node3.left = node2;
            } else {
                assert (node3.right == node);
                node3.right = node2;
            }
        } else {
            int n = node.hash & this.table.length - 1;
            this.table[n] = node2;
        }
    }

    private void rebalance(Node<K, V> node, boolean bl) {
        Node<K, V> node2 = node;
        while (node2 != null) {
            int n;
            Node node3;
            int n2;
            int n3;
            Node node4;
            Node node5;
            int n4;
            Node node6 = node2.left;
            int n5 = node6 != null ? node6.height : 0;
            int n6 = n5 - (n4 = (node5 = node2.right) != null ? node5.height : 0);
            if (n6 == -2) {
                node4 = node5.left;
                n3 = node4 != null ? node4.height : 0;
                n = n3 - (n2 = (node3 = node5.right) != null ? node3.height : 0);
                if (n == -1 || n == 0 && !bl) {
                    this.rotateLeft(node2);
                } else {
                    assert (n == 1);
                    this.rotateRight(node5);
                    this.rotateLeft(node2);
                }
                if (bl) {
                    break;
                }
            } else if (n6 == 2) {
                node4 = node6.left;
                n3 = node4 != null ? node4.height : 0;
                n = n3 - (n2 = (node3 = node6.right) != null ? node3.height : 0);
                if (n == 1 || n == 0 && !bl) {
                    this.rotateRight(node2);
                } else {
                    assert (n == -1);
                    this.rotateLeft(node6);
                    this.rotateRight(node2);
                }
                if (bl) {
                    break;
                }
            } else if (n6 == 0) {
                node2.height = n5 + 1;
                if (bl) {
                    break;
                }
            } else {
                assert (n6 == -1 || n6 == 1);
                node2.height = Math.max(n5, n4) + 1;
                if (!bl) break;
            }
            node2 = node2.parent;
        }
    }

    private void rotateLeft(Node<K, V> node) {
        Node node2 = node.left;
        Node node3 = node.right;
        Node node4 = node3.left;
        Node node5 = node3.right;
        node.right = node4;
        if (node4 != null) {
            node4.parent = node;
        }
        this.replaceInParent(node, node3);
        node3.left = node;
        node.parent = node3;
        node.height = Math.max(node2 != null ? node2.height : 0, node4 != null ? node4.height : 0) + 1;
        node3.height = Math.max(node.height, node5 != null ? node5.height : 0) + 1;
    }

    private void rotateRight(Node<K, V> node) {
        Node node2 = node.left;
        Node node3 = node.right;
        Node node4 = node2.left;
        Node node5 = node2.right;
        node.left = node5;
        if (node5 != null) {
            node5.parent = node;
        }
        this.replaceInParent(node, node2);
        node2.right = node;
        node.parent = node2;
        node.height = Math.max(node3 != null ? node3.height : 0, node5 != null ? node5.height : 0) + 1;
        node2.height = Math.max(node.height, node4 != null ? node4.height : 0) + 1;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        EntrySet entrySet = this.entrySet;
        return entrySet != null ? entrySet : (this.entrySet = new EntrySet());
    }

    @Override
    public Set<K> keySet() {
        KeySet keySet = this.keySet;
        return keySet != null ? keySet : (this.keySet = new KeySet());
    }

    private void doubleCapacity() {
        this.table = LinkedHashTreeMap.doubleCapacity(this.table);
        this.threshold = this.table.length / 2 + this.table.length / 4;
    }

    static <K, V> Node<K, V>[] doubleCapacity(Node<K, V>[] nodeArray) {
        int n = nodeArray.length;
        Node[] nodeArray2 = new Node[n * 2];
        AvlIterator<K, V> avlIterator = new AvlIterator<K, V>();
        AvlBuilder avlBuilder = new AvlBuilder();
        AvlBuilder avlBuilder2 = new AvlBuilder();
        for (int i = 0; i < n; ++i) {
            Node node;
            Node<K, V> node2 = nodeArray[i];
            if (node2 == null) continue;
            avlIterator.reset(node2);
            int n2 = 0;
            int n3 = 0;
            while ((node = avlIterator.next()) != null) {
                if ((node.hash & n) == 0) {
                    ++n2;
                    continue;
                }
                ++n3;
            }
            avlBuilder.reset(n2);
            avlBuilder2.reset(n3);
            avlIterator.reset(node2);
            while ((node = avlIterator.next()) != null) {
                if ((node.hash & n) == 0) {
                    avlBuilder.add(node);
                    continue;
                }
                avlBuilder2.add(node);
            }
            nodeArray2[i] = n2 > 0 ? avlBuilder.root() : null;
            nodeArray2[i + n] = n3 > 0 ? avlBuilder2.root() : null;
        }
        return nodeArray2;
    }

    private Object writeReplace() {
        return new LinkedHashMap(this);
    }

    final class KeySet
    extends AbstractSet<K> {
        KeySet() {
        }

        @Override
        public int size() {
            return LinkedHashTreeMap.this.size;
        }

        @Override
        public Iterator<K> iterator() {
            return new LinkedTreeMapIterator<K>(){

                @Override
                public K next() {
                    return this.nextNode().key;
                }
            };
        }

        @Override
        public boolean contains(Object object) {
            return LinkedHashTreeMap.this.containsKey(object);
        }

        @Override
        public boolean remove(Object object) {
            return LinkedHashTreeMap.this.removeInternalByKey(object) != null;
        }

        @Override
        public void clear() {
            LinkedHashTreeMap.this.clear();
        }
    }

    final class EntrySet
    extends AbstractSet<Map.Entry<K, V>> {
        EntrySet() {
        }

        @Override
        public int size() {
            return LinkedHashTreeMap.this.size;
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new LinkedTreeMapIterator<Map.Entry<K, V>>(){

                @Override
                public Map.Entry<K, V> next() {
                    return this.nextNode();
                }
            };
        }

        @Override
        public boolean contains(Object object) {
            return object instanceof Map.Entry && LinkedHashTreeMap.this.findByEntry((Map.Entry)object) != null;
        }

        @Override
        public boolean remove(Object object) {
            if (!(object instanceof Map.Entry)) {
                return false;
            }
            Node node = LinkedHashTreeMap.this.findByEntry((Map.Entry)object);
            if (node == null) {
                return false;
            }
            LinkedHashTreeMap.this.removeInternal(node, true);
            return true;
        }

        @Override
        public void clear() {
            LinkedHashTreeMap.this.clear();
        }
    }

    private abstract class LinkedTreeMapIterator<T>
    implements Iterator<T> {
        Node<K, V> next;
        Node<K, V> lastReturned;
        int expectedModCount;

        LinkedTreeMapIterator() {
            this.next = LinkedHashTreeMap.this.header.next;
            this.lastReturned = null;
            this.expectedModCount = LinkedHashTreeMap.this.modCount;
        }

        @Override
        public final boolean hasNext() {
            return this.next != LinkedHashTreeMap.this.header;
        }

        final Node<K, V> nextNode() {
            Node node = this.next;
            if (node == LinkedHashTreeMap.this.header) {
                throw new NoSuchElementException();
            }
            if (LinkedHashTreeMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            this.next = node.next;
            this.lastReturned = node;
            return this.lastReturned;
        }

        @Override
        public final void remove() {
            if (this.lastReturned == null) {
                throw new IllegalStateException();
            }
            LinkedHashTreeMap.this.removeInternal(this.lastReturned, true);
            this.lastReturned = null;
            this.expectedModCount = LinkedHashTreeMap.this.modCount;
        }
    }

    static final class AvlBuilder<K, V> {
        private Node<K, V> stack;
        private int leavesToSkip;
        private int leavesSkipped;
        private int size;

        AvlBuilder() {
        }

        void reset(int n) {
            int n2 = Integer.highestOneBit(n) * 2 - 1;
            this.leavesToSkip = n2 - n;
            this.size = 0;
            this.leavesSkipped = 0;
            this.stack = null;
        }

        void add(Node<K, V> node) {
            node.right = null;
            node.parent = null;
            node.left = null;
            node.height = 1;
            if (this.leavesToSkip > 0 && (this.size & 1) == 0) {
                ++this.size;
                --this.leavesToSkip;
                ++this.leavesSkipped;
            }
            node.parent = this.stack;
            this.stack = node;
            ++this.size;
            if (this.leavesToSkip > 0 && (this.size & 1) == 0) {
                ++this.size;
                --this.leavesToSkip;
                ++this.leavesSkipped;
            }
            int n = 4;
            while ((this.size & n - 1) == n - 1) {
                Node node2;
                Node<K, V> node3;
                if (this.leavesSkipped == 0) {
                    node3 = this.stack;
                    node2 = node3.parent;
                    Node node4 = node2.parent;
                    node2.parent = node4.parent;
                    this.stack = node2;
                    node2.left = node4;
                    node2.right = node3;
                    node2.height = node3.height + 1;
                    node4.parent = node2;
                    node3.parent = node2;
                } else if (this.leavesSkipped == 1) {
                    node3 = this.stack;
                    node2 = node3.parent;
                    this.stack = node2;
                    node2.right = node3;
                    node2.height = node3.height + 1;
                    node3.parent = node2;
                    this.leavesSkipped = 0;
                } else if (this.leavesSkipped == 2) {
                    this.leavesSkipped = 0;
                }
                n *= 2;
            }
        }

        Node<K, V> root() {
            Node<K, V> node = this.stack;
            if (node.parent != null) {
                throw new IllegalStateException();
            }
            return node;
        }
    }

    static class AvlIterator<K, V> {
        private Node<K, V> stackTop;

        AvlIterator() {
        }

        void reset(Node<K, V> node) {
            Node<K, V> node2 = null;
            Node<K, V> node3 = node;
            while (node3 != null) {
                node3.parent = node2;
                node2 = node3;
                node3 = node3.left;
            }
            this.stackTop = node2;
        }

        public Node<K, V> next() {
            Node<K, V> node = this.stackTop;
            if (node == null) {
                return null;
            }
            Node<K, V> node2 = node;
            node = node2.parent;
            node2.parent = null;
            Node node3 = node2.right;
            while (node3 != null) {
                node3.parent = node;
                node = node3;
                node3 = node3.left;
            }
            this.stackTop = node;
            return node2;
        }
    }

    static final class Node<K, V>
    implements Map.Entry<K, V> {
        Node<K, V> parent;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> next;
        Node<K, V> prev;
        final K key;
        final int hash;
        V value;
        int height;

        Node() {
            this.key = null;
            this.hash = -1;
            this.next = this.prev = this;
        }

        Node(Node<K, V> node, K k, int n, Node<K, V> node2, Node<K, V> node3) {
            this.parent = node;
            this.key = k;
            this.hash = n;
            this.height = 1;
            this.next = node2;
            this.prev = node3;
            node3.next = this;
            node2.prev = this;
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V v) {
            V v2 = this.value;
            this.value = v;
            return v2;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry)object;
                return (this.key == null ? entry.getKey() == null : this.key.equals(entry.getKey())) && (this.value == null ? entry.getValue() == null : this.value.equals(entry.getValue()));
            }
            return false;
        }

        @Override
        public int hashCode() {
            return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
        }

        public String toString() {
            return this.key + "=" + this.value;
        }

        public Node<K, V> first() {
            Node<K, V> node = this;
            Node<K, V> node2 = node.left;
            while (node2 != null) {
                node = node2;
                node2 = node.left;
            }
            return node;
        }

        public Node<K, V> last() {
            Node<K, V> node = this;
            Node<K, V> node2 = node.right;
            while (node2 != null) {
                node = node2;
                node2 = node.right;
            }
            return node;
        }
    }
}

