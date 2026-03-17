/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.wrapper;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadWriteNBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadWriteNBTCompoundList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.wrapper.NBTProxy;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.wrapper.ProxyBuilder;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.wrapper.ProxyList;

class ProxiedList<E extends NBTProxy>
implements ProxyList<E> {
    private final ReadWriteNBTCompoundList nbt;
    private final Class<E> proxy;

    public ProxiedList(ReadWriteNBTCompoundList readWriteNBTCompoundList, Class<E> clazz) {
        this.nbt = readWriteNBTCompoundList;
        this.proxy = clazz;
    }

    @Override
    public E get(int n) {
        ReadWriteNBT readWriteNBT = (ReadWriteNBT)this.nbt.get(n);
        return new ProxyBuilder<E>(readWriteNBT, this.proxy).build();
    }

    @Override
    public int size() {
        return this.nbt.size();
    }

    @Override
    public void remove(int n) {
        this.nbt.remove(n);
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public E addCompound() {
        ReadWriteNBT readWriteNBT = this.nbt.addCompound();
        return new ProxyBuilder<E>(readWriteNBT, this.proxy).build();
    }

    @Override
    public boolean isEmpty() {
        return this.nbt.isEmpty();
    }

    private class Itr
    implements Iterator<E> {
        int cursor = 0;
        int lastRet = -1;

        private Itr() {
        }

        @Override
        public boolean hasNext() {
            return this.cursor != ProxiedList.this.size();
        }

        @Override
        public E next() {
            try {
                int n = this.cursor;
                Object e = ProxiedList.this.get(n);
                this.lastRet = n;
                this.cursor = n + 1;
                return e;
            } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            if (this.lastRet < 0) {
                throw new IllegalStateException();
            }
            try {
                ProxiedList.this.remove(this.lastRet);
                if (this.lastRet < this.cursor) {
                    --this.cursor;
                }
                this.lastRet = -1;
            } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                throw new ConcurrentModificationException();
            }
        }
    }
}

