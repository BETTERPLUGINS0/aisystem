/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTCompound;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTType;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NbtApiException;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadWriteNBTList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.MinecraftVersion;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ReflectionMethod;

public abstract class NBTList<T>
implements List<T>,
ReadWriteNBTList<T> {
    private String listName;
    private NBTCompound parent;
    private NBTType type;
    protected Object listObject;

    protected NBTList(NBTCompound nBTCompound, String string, NBTType nBTType, Object object) {
        this.parent = nBTCompound;
        this.listName = string;
        this.type = nBTType;
        this.listObject = object;
    }

    public String getName() {
        return this.listName;
    }

    public NBTCompound getParent() {
        return this.parent;
    }

    private void validateClosed() {
        if (this.parent.isClosed()) {
            throw new NbtApiException("Tried using closed NBT data!");
        }
    }

    private void validateWritable() {
        if (this.getParent().isReadOnly()) {
            throw new NbtApiException("Tried setting data in read only mode!");
        }
    }

    protected void save() {
        this.validateClosed();
        this.parent.set(this.listName, this.listObject);
    }

    protected abstract Object asTag(T var1);

    @Override
    public boolean add(T t) {
        this.validateClosed();
        this.validateWritable();
        try {
            this.parent.getWriteLock().lock();
            if (MinecraftVersion.getVersion().getVersionId() >= MinecraftVersion.MC1_14_R1.getVersionId()) {
                ReflectionMethod.LIST_ADD.run(this.listObject, this.size(), this.asTag(t));
            } else {
                ReflectionMethod.LEGACY_LIST_ADD.run(this.listObject, this.asTag(t));
            }
            this.save();
            boolean bl = true;
            return bl;
        } catch (Exception exception) {
            throw new NbtApiException(exception);
        } finally {
            this.parent.getWriteLock().unlock();
        }
    }

    @Override
    public void add(int n, T t) {
        this.validateClosed();
        this.validateWritable();
        try {
            this.parent.getWriteLock().lock();
            if (MinecraftVersion.getVersion().getVersionId() >= MinecraftVersion.MC1_14_R1.getVersionId()) {
                ReflectionMethod.LIST_ADD.run(this.listObject, n, this.asTag(t));
            } else {
                ReflectionMethod.LEGACY_LIST_ADD.run(this.listObject, this.asTag(t));
            }
            this.save();
        } catch (Exception exception) {
            throw new NbtApiException(exception);
        } finally {
            this.parent.getWriteLock().unlock();
        }
    }

    @Override
    public T set(int n, T t) {
        this.validateClosed();
        this.validateWritable();
        try {
            this.parent.getWriteLock().lock();
            Object e = this.get(n);
            ReflectionMethod.LIST_SET.run(this.listObject, n, this.asTag(t));
            this.save();
            Object e2 = e;
            return (T)e2;
        } catch (Exception exception) {
            throw new NbtApiException(exception);
        } finally {
            this.parent.getWriteLock().unlock();
        }
    }

    @Override
    public T remove(int n) {
        this.validateClosed();
        this.validateWritable();
        try {
            this.parent.getWriteLock().lock();
            Object e = this.get(n);
            ReflectionMethod.LIST_REMOVE_KEY.run(this.listObject, n);
            this.save();
            Object e2 = e;
            return (T)e2;
        } catch (Exception exception) {
            throw new NbtApiException(exception);
        } finally {
            this.parent.getWriteLock().unlock();
        }
    }

    @Override
    public int size() {
        this.validateClosed();
        try {
            this.parent.getReadLock().lock();
            int n = (Integer)ReflectionMethod.LIST_SIZE.run(this.listObject, new Object[0]);
            return n;
        } catch (Exception exception) {
            throw new NbtApiException(exception);
        } finally {
            this.parent.getReadLock().unlock();
        }
    }

    @Override
    public NBTType getType() {
        return this.type;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public void clear() {
        while (!this.isEmpty()) {
            this.remove(0);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean contains(Object object) {
        this.validateClosed();
        try {
            int n;
            this.parent.getReadLock().lock();
            for (n = 0; n < this.size(); n += 1) {
                if (!object.equals(this.get(n))) continue;
                boolean bl = true;
                return bl;
            }
            n = 0;
            return n != 0;
        } finally {
            this.parent.getReadLock().unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int indexOf(Object object) {
        this.validateClosed();
        try {
            int n;
            this.parent.getReadLock().lock();
            for (n = 0; n < this.size(); ++n) {
                if (!object.equals(this.get(n))) continue;
                int n2 = n;
                return n2;
            }
            n = -1;
            return n;
        } finally {
            this.parent.getReadLock().unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean addAll(Collection<? extends T> collection) {
        this.validateClosed();
        try {
            this.parent.getWriteLock().lock();
            int n = this.size();
            for (T t : collection) {
                this.add(t);
            }
            boolean bl = n != this.size();
            return bl;
        } finally {
            this.parent.getWriteLock().unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean addAll(int n, Collection<? extends T> collection) {
        this.validateClosed();
        try {
            this.parent.getWriteLock().lock();
            int n2 = this.size();
            for (T t : collection) {
                this.add(n++, t);
            }
            boolean bl = n2 != this.size();
            return bl;
        } finally {
            this.parent.getWriteLock().unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean containsAll(Collection<?> collection) {
        this.validateClosed();
        try {
            this.parent.getReadLock().lock();
            for (Object obj : collection) {
                if (this.contains(obj)) continue;
                boolean bl = false;
                return bl;
            }
            boolean bl = true;
            return bl;
        } finally {
            this.parent.getReadLock().unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int lastIndexOf(Object object) {
        this.validateClosed();
        try {
            int n;
            this.parent.getReadLock().lock();
            int n2 = -1;
            for (n = 0; n < this.size(); ++n) {
                if (!object.equals(this.get(n))) continue;
                n2 = n;
            }
            n = n2;
            return n;
        } finally {
            this.parent.getReadLock().unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean removeAll(Collection<?> collection) {
        this.validateClosed();
        try {
            this.parent.getWriteLock().lock();
            int n = this.size();
            for (Object obj : collection) {
                this.remove(obj);
            }
            boolean bl = n != this.size();
            return bl;
        } finally {
            this.parent.getWriteLock().unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean retainAll(Collection<?> collection) {
        this.validateClosed();
        try {
            this.parent.getWriteLock().lock();
            int n = this.size();
            for (Object obj : collection) {
                for (int i = 0; i < this.size(); ++i) {
                    if (obj.equals(this.get(i))) continue;
                    this.remove(i--);
                }
            }
            boolean bl = n != this.size();
            return bl;
        } finally {
            this.parent.getWriteLock().unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean remove(Object object) {
        this.validateClosed();
        try {
            this.parent.getWriteLock().lock();
            int n = this.size();
            int n2 = this.indexOf(object);
            if (n2 != -1) {
                this.remove(n2);
            }
            boolean bl = n != this.size();
            return bl;
        } finally {
            this.parent.getWriteLock().unlock();
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>(){
            private int index = -1;

            @Override
            public boolean hasNext() {
                return NBTList.this.size() > this.index + 1;
            }

            @Override
            public T next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return NBTList.this.get(++this.index);
            }

            @Override
            public void remove() {
                NBTList.this.remove(this.index);
                --this.index;
            }
        };
    }

    @Override
    public ListIterator<T> listIterator() {
        return this.listIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(final int n) {
        final NBTList nBTList = this;
        return new ListIterator<T>(){
            int index;
            {
                this.index = n - 1;
            }

            @Override
            public void add(T t) {
                nBTList.add(this.index, t);
            }

            @Override
            public boolean hasNext() {
                return NBTList.this.size() > this.index + 1;
            }

            @Override
            public boolean hasPrevious() {
                return this.index >= 0 && this.index <= NBTList.this.size();
            }

            @Override
            public T next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return NBTList.this.get(++this.index);
            }

            @Override
            public int nextIndex() {
                return this.index + 1;
            }

            @Override
            public T previous() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException("Id: " + (this.index - 1));
                }
                return NBTList.this.get(this.index--);
            }

            @Override
            public int previousIndex() {
                return this.index - 1;
            }

            @Override
            public void remove() {
                nBTList.remove(this.index);
                --this.index;
            }

            @Override
            public void set(T t) {
                nBTList.set(this.index, t);
            }
        };
    }

    @Override
    public Object[] toArray() {
        this.validateClosed();
        try {
            this.parent.getReadLock().lock();
            Object[] objectArray = new Object[this.size()];
            for (int i = 0; i < this.size(); ++i) {
                objectArray[i] = this.get(i);
            }
            Object[] objectArray2 = objectArray;
            return objectArray2;
        } finally {
            this.parent.getReadLock().unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public <E> E[] toArray(E[] EArray) {
        this.validateClosed();
        try {
            this.parent.getReadLock().lock();
            Object[] objectArray = Arrays.copyOf(EArray, this.size());
            Arrays.fill(objectArray, null);
            Class<?> clazz = EArray.getClass().getComponentType();
            for (int i = 0; i < this.size(); ++i) {
                Object e = this.get(i);
                if (!clazz.isInstance(e)) {
                    throw new ArrayStoreException("The array does not match the objects stored in the List.");
                }
                objectArray[i] = this.get(i);
            }
            Object[] objectArray2 = objectArray;
            return objectArray2;
        } finally {
            this.parent.getReadLock().unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public List<T> subList(int n, int n2) {
        this.validateClosed();
        try {
            this.parent.getReadLock().lock();
            ArrayList arrayList = new ArrayList();
            for (int i = n; i < n2; ++i) {
                arrayList.add(this.get(i));
            }
            ArrayList arrayList2 = arrayList;
            return arrayList2;
        } finally {
            this.parent.getReadLock().unlock();
        }
    }

    @Override
    public boolean removeIf(Predicate<? super T> predicate) {
        return List.super.removeIf(predicate);
    }

    public String toString() {
        this.validateClosed();
        try {
            this.parent.getReadLock().lock();
            String string = this.listObject.toString();
            return string;
        } finally {
            this.parent.getReadLock().unlock();
        }
    }
}

