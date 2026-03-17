/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTCompoundList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTContainer;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTItem;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTListCompound;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTReflectionUtil;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTType;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NbtApiException;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.NBTHandler;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadWriteNBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadWriteNBTList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadableNBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.CheckUtil;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.MinecraftVersion;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.PathUtil;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.UUIDUtil;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.Forge1710Mappings;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ReflectionMethod;
import org.bukkit.inventory.ItemStack;

public class NBTCompound
implements ReadWriteNBT {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = this.readWriteLock.readLock();
    private final Lock writeLock = this.readWriteLock.writeLock();
    private String compundName;
    private NBTCompound parent;
    private final boolean readOnly;
    private Object readOnlyCache;

    protected NBTCompound(NBTCompound nBTCompound, String string) {
        this(nBTCompound, string, false);
    }

    protected NBTCompound(NBTCompound nBTCompound, String string, boolean bl) {
        this.compundName = string;
        this.parent = nBTCompound;
        this.readOnly = bl;
    }

    protected Lock getReadLock() {
        return this.readLock;
    }

    protected Lock getWriteLock() {
        return this.writeLock;
    }

    protected void saveCompound() {
        if (this.parent != null) {
            this.parent.saveCompound();
        }
    }

    protected void setResolvedObject(Object object) {
        if (this.isClosed()) {
            throw new NbtApiException("Tried using closed NBT data!");
        }
        if (this.readOnly) {
            this.readOnlyCache = object;
        }
    }

    protected void setClosed() {
        if (this.parent != null) {
            this.parent.setClosed();
        }
    }

    protected boolean isClosed() {
        if (this.parent != null) {
            return this.parent.isClosed();
        }
        return false;
    }

    protected boolean isReadOnly() {
        return this.readOnly;
    }

    protected Object getResolvedObject() {
        if (this.isClosed()) {
            throw new NbtApiException("Tried using closed NBT data!");
        }
        if (this.readOnlyCache != null) {
            return this.readOnlyCache;
        }
        Object object = this.getCompound();
        if (object instanceof Optional) {
            object = ((Optional)object).orElse(null);
        }
        if (object == null) {
            return null;
        }
        if (!NBTReflectionUtil.validCompound(this)) {
            throw new NbtApiException("The Compound wasn't able to be linked back to the root!");
        }
        Object object2 = NBTReflectionUtil.getToCompount(object, this);
        if (this.readOnly) {
            this.readOnlyCache = object2;
        }
        return object2;
    }

    public String getName() {
        return this.compundName;
    }

    public Object getCompound() {
        return this.parent.getCompound();
    }

    protected void setCompound(Object object) {
        this.parent.setCompound(object);
    }

    public NBTCompound getParent() {
        return this.parent;
    }

    public void mergeCompound(NBTCompound nBTCompound) {
        if (nBTCompound == null) {
            return;
        }
        try {
            this.writeLock.lock();
            NBTReflectionUtil.mergeOtherNBTCompound(this, nBTCompound);
            this.saveCompound();
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public void mergeCompound(ReadableNBT readableNBT) {
        if (!(readableNBT instanceof NBTCompound)) {
            throw new NbtApiException("Unknown NBT object: " + readableNBT);
        }
        this.mergeCompound((NBTCompound)readableNBT);
    }

    @Override
    public void setString(String string, String string2) {
        try {
            this.writeLock.lock();
            NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_STRING, string, string2);
            this.saveCompound();
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public String getString(String string) {
        try {
            this.readLock.lock();
            String string2 = (String)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_STRING, string);
            return string2;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public void setInteger(String string, Integer n) {
        try {
            this.writeLock.lock();
            NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_INT, string, n);
            this.saveCompound();
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public Integer getInteger(String string) {
        try {
            this.readLock.lock();
            Integer n = (Integer)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_INT, string);
            return n;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public void setDouble(String string, Double d) {
        try {
            this.writeLock.lock();
            NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_DOUBLE, string, d);
            this.saveCompound();
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public Double getDouble(String string) {
        try {
            this.readLock.lock();
            Double d = (Double)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_DOUBLE, string);
            return d;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public void setByte(String string, Byte by) {
        try {
            this.writeLock.lock();
            NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_BYTE, string, by);
            this.saveCompound();
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public Byte getByte(String string) {
        try {
            this.readLock.lock();
            Byte by = (Byte)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_BYTE, string);
            return by;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public void setShort(String string, Short s) {
        try {
            this.writeLock.lock();
            NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_SHORT, string, s);
            this.saveCompound();
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public Short getShort(String string) {
        try {
            this.readLock.lock();
            Short s = (Short)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_SHORT, string);
            return s;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public void setLong(String string, Long l) {
        try {
            this.writeLock.lock();
            NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_LONG, string, l);
            this.saveCompound();
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public Long getLong(String string) {
        try {
            this.readLock.lock();
            Long l = (Long)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_LONG, string);
            return l;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public void setFloat(String string, Float f) {
        try {
            this.writeLock.lock();
            NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_FLOAT, string, f);
            this.saveCompound();
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public Float getFloat(String string) {
        try {
            this.readLock.lock();
            Float f = (Float)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_FLOAT, string);
            return f;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public void setByteArray(String string, byte[] byArray) {
        try {
            this.writeLock.lock();
            NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_BYTEARRAY, string, byArray);
            this.saveCompound();
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public byte[] getByteArray(String string) {
        try {
            this.readLock.lock();
            byte[] byArray = (byte[])NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_BYTEARRAY, string);
            return byArray;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public void setIntArray(String string, int[] nArray) {
        try {
            this.writeLock.lock();
            NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_INTARRAY, string, nArray);
            this.saveCompound();
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public int[] getIntArray(String string) {
        try {
            this.readLock.lock();
            int[] nArray = (int[])NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_INTARRAY, string);
            return nArray;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public void setLongArray(String string, long[] lArray) {
        CheckUtil.assertAvailable(MinecraftVersion.MC1_16_R1);
        try {
            this.writeLock.lock();
            NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_LONGARRAY, string, lArray);
            this.saveCompound();
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public long[] getLongArray(String string) {
        CheckUtil.assertAvailable(MinecraftVersion.MC1_16_R1);
        try {
            this.readLock.lock();
            long[] lArray = (long[])NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_LONGARRAY, string);
            return lArray;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public void setBoolean(String string, Boolean bl) {
        try {
            this.writeLock.lock();
            NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_BOOLEAN, string, bl);
            this.saveCompound();
        } finally {
            this.writeLock.unlock();
        }
    }

    protected void set(String string, Object object) {
        NBTReflectionUtil.set(this, string, object);
        this.saveCompound();
    }

    @Override
    public Boolean getBoolean(String string) {
        try {
            this.readLock.lock();
            Boolean bl = (Boolean)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_BOOLEAN, string);
            return bl;
        } finally {
            this.readLock.unlock();
        }
    }

    @Deprecated
    public void setObject(String string, Object object) {
        try {
            this.writeLock.lock();
            NBTReflectionUtil.setObject(this, string, object);
            this.saveCompound();
        } finally {
            this.writeLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Deprecated
    public <T> T getObject(String string, Class<T> clazz) {
        try {
            this.readLock.lock();
            T t = NBTReflectionUtil.getObject(this, string, clazz);
            return t;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public void setItemStack(String string, ItemStack itemStack) {
        try {
            this.writeLock.lock();
            this.removeKey(string);
            this.addCompound(string).mergeCompound(NBTItem.convertItemtoNBT(itemStack));
        } finally {
            this.writeLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public ItemStack getItemStack(String string) {
        try {
            this.readLock.lock();
            NBTCompound nBTCompound = this.getCompound(string);
            if (nBTCompound == null) {
                ItemStack itemStack = null;
                return itemStack;
            }
            ItemStack itemStack = NBTItem.convertNBTtoItem(nBTCompound);
            return itemStack;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public void setItemStackArray(String string, ItemStack[] itemStackArray) {
        try {
            this.writeLock.lock();
            this.removeKey(string);
            this.addCompound(string).mergeCompound(NBTItem.convertItemArraytoNBT(itemStackArray));
        } finally {
            this.writeLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public ItemStack[] getItemStackArray(String string) {
        try {
            this.readLock.lock();
            NBTCompound nBTCompound = this.getCompound(string);
            if (nBTCompound == null) {
                ItemStack[] itemStackArray = null;
                return itemStackArray;
            }
            ItemStack[] itemStackArray = NBTItem.convertNBTtoItemArray(nBTCompound);
            return itemStackArray;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public void setUUID(String string, UUID uUID) {
        try {
            this.writeLock.lock();
            if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R4)) {
                this.setIntArray(string, UUIDUtil.uuidToIntArray(uUID));
            } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_16_R1)) {
                NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_UUID, string, uUID);
            } else {
                this.setString(string, uUID.toString());
            }
            this.saveCompound();
        } finally {
            this.writeLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     */
    @Override
    public UUID getUUID(String string) {
        try {
            this.readLock.lock();
            NBTType nBTType = this.getType(string);
            if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R4) && nBTType == NBTType.NBTTagIntArray) {
                UUID uUID = UUIDUtil.uuidFromIntArray(this.getIntArray(string));
                return uUID;
            }
            if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_16_R1) && nBTType == NBTType.NBTTagIntArray) {
                UUID uUID = (UUID)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_UUID, string);
                return uUID;
            }
            if (nBTType == NBTType.NBTTagString) {
                try {
                    UUID uUID = UUID.fromString(this.getString(string));
                    return uUID;
                } catch (IllegalArgumentException illegalArgumentException) {
                    UUID uUID = null;
                    this.readLock.unlock();
                    return uUID;
                }
            }
            UUID uUID = null;
            return uUID;
            {
                catch (Throwable throwable) {
                    throw throwable;
                }
            }
        } finally {
            this.readLock.unlock();
        }
    }

    @Deprecated
    public Boolean hasKey(String string) {
        return this.hasTag(string);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean hasTag(String string) {
        try {
            this.readLock.lock();
            Boolean bl = (Boolean)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_HAS_KEY, string);
            if (bl == null) {
                boolean bl2 = false;
                return bl2;
            }
            boolean bl3 = bl;
            return bl3;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public void removeKey(String string) {
        try {
            this.writeLock.lock();
            NBTReflectionUtil.remove(this, string);
            this.saveCompound();
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public Set<String> getKeys() {
        try {
            this.readLock.lock();
            HashSet<String> hashSet = new HashSet<String>(NBTReflectionUtil.getKeys(this));
            return hashSet;
        } finally {
            this.readLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public NBTCompound addCompound(String string) {
        try {
            this.writeLock.lock();
            if (this.getType(string) == NBTType.NBTTagCompound) {
                NBTCompound nBTCompound = this.getCompound(string);
                return nBTCompound;
            }
            NBTReflectionUtil.addNBTTagCompound(this, string);
            NBTCompound nBTCompound = this.getCompound(string);
            if (nBTCompound == null) {
                throw new NbtApiException("Error while adding Compound, got null!");
            }
            this.saveCompound();
            NBTCompound nBTCompound2 = nBTCompound;
            return nBTCompound2;
        } finally {
            this.writeLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public NBTCompound getCompound(String string) {
        try {
            this.readLock.lock();
            if (this.getType(string) != NBTType.NBTTagCompound) {
                NBTCompound nBTCompound = null;
                return nBTCompound;
            }
            NBTCompound nBTCompound = new NBTCompound(this, string, this.readOnly);
            if (NBTReflectionUtil.validCompound(nBTCompound)) {
                NBTCompound nBTCompound2 = nBTCompound;
                return nBTCompound2;
            }
            NBTCompound nBTCompound3 = null;
            return nBTCompound3;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public NBTCompound getOrCreateCompound(String string) {
        return this.addCompound(string);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public NBTList<String> getStringList(String string) {
        try {
            this.writeLock.lock();
            NBTList<String> nBTList = NBTReflectionUtil.getList(this, string, NBTType.NBTTagString, String.class);
            this.saveCompound();
            NBTList<String> nBTList2 = nBTList;
            return nBTList2;
        } finally {
            this.writeLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public NBTList<Integer> getIntegerList(String string) {
        try {
            this.writeLock.lock();
            NBTList<Integer> nBTList = NBTReflectionUtil.getList(this, string, NBTType.NBTTagInt, Integer.class);
            this.saveCompound();
            NBTList<Integer> nBTList2 = nBTList;
            return nBTList2;
        } finally {
            this.writeLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public NBTList<int[]> getIntArrayList(String string) {
        try {
            this.writeLock.lock();
            NBTList<int[]> nBTList = NBTReflectionUtil.getList(this, string, NBTType.NBTTagIntArray, int[].class);
            this.saveCompound();
            NBTList<int[]> nBTList2 = nBTList;
            return nBTList2;
        } finally {
            this.writeLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public NBTList<UUID> getUUIDList(String string) {
        try {
            this.writeLock.lock();
            NBTList<UUID> nBTList = NBTReflectionUtil.getList(this, string, NBTType.NBTTagIntArray, UUID.class);
            this.saveCompound();
            NBTList<UUID> nBTList2 = nBTList;
            return nBTList2;
        } finally {
            this.writeLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public NBTList<Float> getFloatList(String string) {
        try {
            this.writeLock.lock();
            NBTList<Float> nBTList = NBTReflectionUtil.getList(this, string, NBTType.NBTTagFloat, Float.class);
            this.saveCompound();
            NBTList<Float> nBTList2 = nBTList;
            return nBTList2;
        } finally {
            this.writeLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public NBTList<Double> getDoubleList(String string) {
        try {
            this.writeLock.lock();
            NBTList<Double> nBTList = NBTReflectionUtil.getList(this, string, NBTType.NBTTagDouble, Double.class);
            this.saveCompound();
            NBTList<Double> nBTList2 = nBTList;
            return nBTList2;
        } finally {
            this.writeLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public NBTList<Long> getLongList(String string) {
        try {
            this.writeLock.lock();
            NBTList<Long> nBTList = NBTReflectionUtil.getList(this, string, NBTType.NBTTagLong, Long.class);
            this.saveCompound();
            NBTList<Long> nBTList2 = nBTList;
            return nBTList2;
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public NBTType getListType(String string) {
        try {
            this.readLock.lock();
            if (this.getType(string) != NBTType.NBTTagList) {
                NBTType nBTType = null;
                return nBTType;
            }
            NBTType nBTType = NBTReflectionUtil.getListType(this, string);
            return nBTType;
        } finally {
            this.readLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public NBTCompoundList getCompoundList(String string) {
        try {
            this.writeLock.lock();
            NBTCompoundList nBTCompoundList = (NBTCompoundList)NBTReflectionUtil.getList(this, string, NBTType.NBTTagCompound, NBTListCompound.class);
            this.saveCompound();
            NBTCompoundList nBTCompoundList2 = nBTCompoundList;
            return nBTCompoundList2;
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public <T> T getOrDefault(String string, T t) {
        if (t == null) {
            throw new NullPointerException("Default type in getOrDefault can't be null!");
        }
        if (!this.hasTag(string)) {
            return t;
        }
        Class<?> clazz = t.getClass();
        if (clazz == Boolean.class || clazz == Boolean.TYPE) {
            return (T)this.getBoolean(string);
        }
        if (clazz == Byte.class || clazz == Byte.TYPE) {
            return (T)this.getByte(string);
        }
        if (clazz == Short.class || clazz == Short.TYPE) {
            return (T)this.getShort(string);
        }
        if (clazz == Integer.class || clazz == Integer.TYPE) {
            return (T)this.getInteger(string);
        }
        if (clazz == Long.class || clazz == Long.TYPE) {
            return (T)this.getLong(string);
        }
        if (clazz == Float.class || clazz == Float.TYPE) {
            return (T)this.getFloat(string);
        }
        if (clazz == Double.class || clazz == Double.TYPE) {
            return (T)this.getDouble(string);
        }
        if (clazz == byte[].class) {
            return (T)this.getByteArray(string);
        }
        if (clazz == int[].class) {
            return (T)this.getIntArray(string);
        }
        if (clazz == long[].class) {
            return (T)this.getLongArray(string);
        }
        if (clazz == String.class) {
            return (T)this.getString(string);
        }
        if (clazz == UUID.class) {
            UUID uUID = this.getUUID(string);
            return (T)(uUID == null ? t : uUID);
        }
        if (clazz.isEnum()) {
            Object obj = this.getEnum(string, t.getClass());
            return (T)(obj == null ? t : obj);
        }
        throw new NbtApiException("Unsupported type for getOrDefault: " + clazz.getName());
    }

    @Override
    public <T> T getOrNull(String string, Class<?> clazz) {
        if (clazz == null) {
            throw new NullPointerException("Default type in getOrNull can't be null!");
        }
        if (!this.hasTag(string)) {
            return null;
        }
        if (clazz == Boolean.class || clazz == Boolean.TYPE) {
            return (T)this.getBoolean(string);
        }
        if (clazz == Byte.class || clazz == Byte.TYPE) {
            return (T)this.getByte(string);
        }
        if (clazz == Short.class || clazz == Short.TYPE) {
            return (T)this.getShort(string);
        }
        if (clazz == Integer.class || clazz == Integer.TYPE) {
            return (T)this.getInteger(string);
        }
        if (clazz == Long.class || clazz == Long.TYPE) {
            return (T)this.getLong(string);
        }
        if (clazz == Float.class || clazz == Float.TYPE) {
            return (T)this.getFloat(string);
        }
        if (clazz == Double.class || clazz == Double.TYPE) {
            return (T)this.getDouble(string);
        }
        if (clazz == byte[].class) {
            return (T)this.getByteArray(string);
        }
        if (clazz == int[].class) {
            return (T)this.getIntArray(string);
        }
        if (clazz == long[].class) {
            return (T)this.getLongArray(string);
        }
        if (clazz == String.class) {
            return (T)this.getString(string);
        }
        if (clazz == UUID.class) {
            return (T)this.getUUID(string);
        }
        if (clazz.isEnum()) {
            return (T)this.getEnum(string, clazz);
        }
        throw new NbtApiException("Unsupported type for getOrNull: " + clazz.getName());
    }

    @Override
    public <T> T resolveOrNull(String string, Class<?> clazz) {
        List<PathUtil.PathSegment> list = PathUtil.splitPath(string);
        NBTCompound nBTCompound = this;
        for (int i = 0; i < list.size() - 1; ++i) {
            PathUtil.PathSegment pathSegment = list.get(i);
            if (!pathSegment.hasIndex()) {
                if ((nBTCompound = nBTCompound.getCompound(pathSegment.getPath())) != null) continue;
                return null;
            }
            if (nBTCompound.getType(pathSegment.getPath()) != NBTType.NBTTagList || nBTCompound.getListType(pathSegment.getPath()) != NBTType.NBTTagCompound) continue;
            NBTCompoundList nBTCompoundList = nBTCompound.getCompoundList(pathSegment.getPath());
            nBTCompound = pathSegment.getIndex() >= 0 ? nBTCompoundList.get(pathSegment.getIndex()) : nBTCompoundList.get(nBTCompoundList.size() + pathSegment.getIndex());
        }
        PathUtil.PathSegment pathSegment = list.get(list.size() - 1);
        if (!pathSegment.hasIndex()) {
            return nBTCompound.getOrNull(pathSegment.getPath(), clazz);
        }
        return (T)this.getIndexedValue(nBTCompound, pathSegment, clazz);
    }

    @Override
    public <T> T resolveOrDefault(String string, T t) {
        List<PathUtil.PathSegment> list = PathUtil.splitPath(string);
        NBTCompound nBTCompound = this;
        for (int i = 0; i < list.size() - 1; ++i) {
            PathUtil.PathSegment pathSegment = list.get(i);
            if (!pathSegment.hasIndex()) {
                if ((nBTCompound = nBTCompound.getCompound(pathSegment.getPath())) != null) continue;
                return t;
            }
            if (nBTCompound.getType(pathSegment.getPath()) != NBTType.NBTTagList || nBTCompound.getListType(pathSegment.getPath()) != NBTType.NBTTagCompound) continue;
            NBTCompoundList nBTCompoundList = nBTCompound.getCompoundList(pathSegment.getPath());
            nBTCompound = pathSegment.getIndex() >= 0 ? nBTCompoundList.get(pathSegment.getIndex()) : nBTCompoundList.get(nBTCompoundList.size() + pathSegment.getIndex());
        }
        PathUtil.PathSegment pathSegment = list.get(list.size() - 1);
        if (!pathSegment.hasIndex()) {
            return nBTCompound.getOrDefault(pathSegment.getPath(), t);
        }
        return (T)this.getIndexedValue(nBTCompound, pathSegment, t.getClass());
    }

    private <T> T getIndexedValue(NBTCompound nBTCompound, PathUtil.PathSegment pathSegment, Class<T> clazz) {
        if (clazz == String.class) {
            if (nBTCompound.getType(pathSegment.getPath()) == NBTType.NBTTagList && nBTCompound.getListType(pathSegment.getPath()) == NBTType.NBTTagString) {
                if (pathSegment.getIndex() >= 0) {
                    return (T)nBTCompound.getStringList(pathSegment.getPath()).get(pathSegment.getIndex());
                }
                ReadWriteNBTList readWriteNBTList = nBTCompound.getStringList(pathSegment.getPath());
                return (T)readWriteNBTList.get(readWriteNBTList.size() + pathSegment.getIndex());
            }
            throw new NbtApiException("No fitting list/array found for " + pathSegment.getPath() + " of type " + clazz);
        }
        if (clazz == Integer.TYPE || clazz == Integer.class) {
            if (nBTCompound.getType(pathSegment.getPath()) == NBTType.NBTTagList && nBTCompound.getListType(pathSegment.getPath()) == NBTType.NBTTagInt) {
                if (pathSegment.getIndex() >= 0) {
                    return (T)nBTCompound.getIntegerList(pathSegment.getPath()).get(pathSegment.getIndex());
                }
                ReadWriteNBTList readWriteNBTList = nBTCompound.getIntegerList(pathSegment.getPath());
                return (T)readWriteNBTList.get(readWriteNBTList.size() + pathSegment.getIndex());
            }
            if (nBTCompound.getType(pathSegment.getPath()) == NBTType.NBTTagIntArray) {
                if (pathSegment.getIndex() >= 0) {
                    int[] nArray = nBTCompound.getIntArray(pathSegment.getPath());
                    if (nArray != null) {
                        return (T)Integer.valueOf(nArray[pathSegment.getIndex()]);
                    }
                } else {
                    int[] nArray = nBTCompound.getIntArray(pathSegment.getPath());
                    if (nArray != null) {
                        return (T)Integer.valueOf(nArray[nArray.length + pathSegment.getIndex()]);
                    }
                }
            }
            throw new NbtApiException("No fitting list/array found for " + pathSegment.getPath() + " of type " + clazz);
        }
        if (clazz == Long.TYPE || clazz == Long.class) {
            if (nBTCompound.getType(pathSegment.getPath()) == NBTType.NBTTagList && nBTCompound.getListType(pathSegment.getPath()) == NBTType.NBTTagLong) {
                if (pathSegment.getIndex() >= 0) {
                    return (T)nBTCompound.getLongList(pathSegment.getPath()).get(pathSegment.getIndex());
                }
                ReadWriteNBTList readWriteNBTList = nBTCompound.getLongList(pathSegment.getPath());
                return (T)readWriteNBTList.get(readWriteNBTList.size() + pathSegment.getIndex());
            }
            if (nBTCompound.getType(pathSegment.getPath()) == NBTType.NBTTagLongArray) {
                if (pathSegment.getIndex() >= 0) {
                    long[] lArray = nBTCompound.getLongArray(pathSegment.getPath());
                    if (lArray != null) {
                        return (T)Long.valueOf(lArray[pathSegment.getIndex()]);
                    }
                } else {
                    long[] lArray = nBTCompound.getLongArray(pathSegment.getPath());
                    if (lArray != null) {
                        return (T)Long.valueOf(lArray[lArray.length + pathSegment.getIndex()]);
                    }
                }
            }
            throw new NbtApiException("No fitting list/array found for " + pathSegment.getPath() + " of type " + clazz);
        }
        if (clazz == Float.TYPE || clazz == Float.class) {
            if (nBTCompound.getType(pathSegment.getPath()) == NBTType.NBTTagList && nBTCompound.getListType(pathSegment.getPath()) == NBTType.NBTTagFloat) {
                if (pathSegment.getIndex() >= 0) {
                    return (T)nBTCompound.getFloatList(pathSegment.getPath()).get(pathSegment.getIndex());
                }
                ReadWriteNBTList readWriteNBTList = nBTCompound.getFloatList(pathSegment.getPath());
                return (T)readWriteNBTList.get(readWriteNBTList.size() + pathSegment.getIndex());
            }
            throw new NbtApiException("No fitting list/array found for " + pathSegment.getPath() + " of type " + clazz);
        }
        if (clazz == Double.TYPE || clazz == Double.class) {
            if (nBTCompound.getType(pathSegment.getPath()) == NBTType.NBTTagList && nBTCompound.getListType(pathSegment.getPath()) == NBTType.NBTTagDouble) {
                if (pathSegment.getIndex() >= 0) {
                    return (T)nBTCompound.getDoubleList(pathSegment.getPath()).get(pathSegment.getIndex());
                }
                ReadWriteNBTList readWriteNBTList = nBTCompound.getDoubleList(pathSegment.getPath());
                return (T)readWriteNBTList.get(readWriteNBTList.size() + pathSegment.getIndex());
            }
            throw new NbtApiException("No fitting list/array found for " + pathSegment.getPath() + " of type " + clazz);
        }
        if (clazz == int[].class) {
            if (nBTCompound.getType(pathSegment.getPath()) == NBTType.NBTTagList && nBTCompound.getListType(pathSegment.getPath()) == NBTType.NBTTagIntArray) {
                if (pathSegment.getIndex() >= 0) {
                    return (T)nBTCompound.getIntArrayList(pathSegment.getPath()).get(pathSegment.getIndex());
                }
                ReadWriteNBTList readWriteNBTList = nBTCompound.getIntArrayList(pathSegment.getPath());
                return (T)readWriteNBTList.get(readWriteNBTList.size() + pathSegment.getIndex());
            }
            throw new NbtApiException("No fitting list/array found for " + pathSegment.getPath() + " of type " + clazz);
        }
        if (clazz == Byte.TYPE || clazz == Byte.class) {
            if (nBTCompound.getType(pathSegment.getPath()) == NBTType.NBTTagByteArray) {
                if (pathSegment.getIndex() >= 0) {
                    byte[] byArray = nBTCompound.getByteArray(pathSegment.getPath());
                    if (byArray != null) {
                        return (T)Byte.valueOf(byArray[pathSegment.getIndex()]);
                    }
                } else {
                    byte[] byArray = nBTCompound.getByteArray(pathSegment.getPath());
                    if (byArray != null) {
                        return (T)Byte.valueOf(byArray[byArray.length + pathSegment.getIndex()]);
                    }
                }
            }
            throw new NbtApiException("No fitting list/array found for " + pathSegment.getPath() + " of type " + clazz);
        }
        throw new NbtApiException("Unable to get indexed value for type " + clazz);
    }

    @Override
    public ReadWriteNBT resolveCompound(String string) {
        List<PathUtil.PathSegment> list = PathUtil.splitPath(string);
        NBTCompound nBTCompound = this;
        for (int i = 0; i < list.size(); ++i) {
            PathUtil.PathSegment pathSegment = list.get(i);
            if (!pathSegment.hasIndex()) {
                if ((nBTCompound = nBTCompound.getCompound(pathSegment.getPath())) != null) continue;
                return null;
            }
            if (nBTCompound.getType(pathSegment.getPath()) != NBTType.NBTTagList || nBTCompound.getListType(pathSegment.getPath()) != NBTType.NBTTagCompound) continue;
            NBTCompoundList nBTCompoundList = nBTCompound.getCompoundList(pathSegment.getPath());
            nBTCompound = pathSegment.getIndex() >= 0 ? nBTCompoundList.get(pathSegment.getIndex()) : nBTCompoundList.get(nBTCompoundList.size() + pathSegment.getIndex());
        }
        return nBTCompound;
    }

    @Override
    public ReadWriteNBT resolveOrCreateCompound(String string) {
        List<PathUtil.PathSegment> list = PathUtil.splitPath(string);
        NBTCompound nBTCompound = this;
        for (int i = 0; i < list.size(); ++i) {
            PathUtil.PathSegment pathSegment = list.get(i);
            if (!pathSegment.hasIndex()) {
                if ((nBTCompound = nBTCompound.getOrCreateCompound(pathSegment.getPath())) != null) continue;
                return null;
            }
            if (nBTCompound.getType(pathSegment.getPath()) != NBTType.NBTTagList || nBTCompound.getListType(pathSegment.getPath()) != NBTType.NBTTagCompound) continue;
            NBTCompoundList nBTCompoundList = nBTCompound.getCompoundList(pathSegment.getPath());
            nBTCompound = pathSegment.getIndex() >= 0 ? nBTCompoundList.get(pathSegment.getIndex()) : nBTCompoundList.get(nBTCompoundList.size() + pathSegment.getIndex());
        }
        return nBTCompound;
    }

    @Override
    public <E extends Enum<?>> void setEnum(String string, E e) {
        if (e == null) {
            this.removeKey(string);
            return;
        }
        this.setString(string, e.name());
    }

    @Override
    public <E extends Enum<E>> E getEnum(String string, Class<E> clazz) {
        if (string == null || clazz == null) {
            return null;
        }
        String string2 = this.getString(string);
        if (string2 == null) {
            return null;
        }
        try {
            return Enum.valueOf(clazz, string2);
        } catch (IllegalArgumentException illegalArgumentException) {
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public NBTType getType(String string) {
        try {
            this.readLock.lock();
            if (MinecraftVersion.getVersion() == MinecraftVersion.MC1_7_R4) {
                Object object = NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET, string);
                if (object == null) {
                    NBTType nBTType = null;
                    return nBTType;
                }
                NBTType nBTType = NBTType.valueOf(((Byte)ReflectionMethod.COMPOUND_OWN_TYPE_LEGACY.run(object, new Object[0])).byteValue());
                return nBTType;
            }
            if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R4)) {
                Object object = NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET, string);
                if (object == null) {
                    NBTType nBTType = null;
                    return nBTType;
                }
                NBTType nBTType = NBTType.fromName((String)ReflectionMethod.TAGTYPE_GET_NAME.run(ReflectionMethod.TAGTYPE_OWN_TYPE.run(object, new Object[0]), new Object[0]));
                return nBTType;
            }
            Object object = NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_TYPE, string);
            if (object == null) {
                NBTType nBTType = null;
                return nBTType;
            }
            NBTType nBTType = NBTType.valueOf(((Byte)object).byteValue());
            return nBTType;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public void writeCompound(OutputStream outputStream) {
        try {
            this.writeLock.lock();
            NBTReflectionUtil.writeApiNBT(this, outputStream);
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public <T> T get(String string, NBTHandler<T> nBTHandler) {
        return nBTHandler.get(this, string);
    }

    @Override
    public <T> void set(String string, T t, NBTHandler<T> nBTHandler) {
        nBTHandler.set(this, string, t);
    }

    @Override
    public String toString() {
        return this.asNBTString();
    }

    @Deprecated
    public String toString(String string) {
        return this.asNBTString();
    }

    @Override
    public void clearNBT() {
        for (String string : this.getKeys()) {
            this.removeKey(string);
        }
    }

    @Deprecated
    public String asNBTString() {
        try {
            this.readLock.lock();
            Object object = this.getResolvedObject();
            if (object == null) {
                String string = "{}";
                return string;
            }
            if (MinecraftVersion.isForgePresent() && MinecraftVersion.getVersion() == MinecraftVersion.MC1_7_R4) {
                String string = Forge1710Mappings.toString(object);
                return string;
            }
            String string = object.toString();
            return string;
        } finally {
            this.readLock.unlock();
        }
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (object instanceof NBTCompound) {
            NBTCompound nBTCompound = (NBTCompound)object;
            if (this.getKeys().equals(nBTCompound.getKeys())) {
                for (String string : this.getKeys()) {
                    if (NBTCompound.isEqual(this, nBTCompound, string)) continue;
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public NBTCompound extractDifference(ReadableNBT readableNBT) {
        if (this == readableNBT) {
            return new NBTContainer();
        }
        if (readableNBT instanceof NBTCompound) {
            return NBTCompound.saveDiff(new NBTContainer(), this, (NBTCompound)readableNBT);
        }
        throw new NbtApiException("Unknown NBT object: " + readableNBT);
    }

    private static NBTCompound saveDiff(NBTCompound nBTCompound, NBTCompound nBTCompound2, NBTCompound nBTCompound3) {
        for (String string : nBTCompound2.getKeys()) {
            NBTCompound.saveDiff(nBTCompound, nBTCompound2, nBTCompound3, string);
        }
        return nBTCompound;
    }

    private static void saveDiff(NBTCompound nBTCompound, NBTCompound nBTCompound2, NBTCompound nBTCompound3, String string) {
        boolean bl = nBTCompound2.getType(string) != nBTCompound3.getType(string);
        switch (nBTCompound2.getType(string)) {
            case NBTTagByte: {
                if (bl || !NBTCompound.isEqual(nBTCompound2, nBTCompound3, string)) {
                    nBTCompound.setByte(string, nBTCompound2.getByte(string));
                }
                return;
            }
            case NBTTagByteArray: {
                if (bl || !NBTCompound.isEqual(nBTCompound2, nBTCompound3, string)) {
                    nBTCompound.setByteArray(string, nBTCompound2.getByteArray(string));
                }
                return;
            }
            case NBTTagCompound: {
                NBTCompound nBTCompound4 = nBTCompound2.getCompound(string);
                if (nBTCompound4 == null) {
                    return;
                }
                if (bl) {
                    nBTCompound.addCompound(string).mergeCompound(nBTCompound4);
                } else {
                    NBTCompound nBTCompound5 = nBTCompound3.getCompound(string);
                    if (nBTCompound5 == null) {
                        nBTCompound.addCompound(string).mergeCompound(nBTCompound4);
                        return;
                    }
                    NBTCompound nBTCompound6 = nBTCompound4.extractDifference(nBTCompound5);
                    if (!nBTCompound6.getKeys().isEmpty()) {
                        nBTCompound.addCompound(string).mergeCompound(nBTCompound6);
                    }
                }
                return;
            }
            case NBTTagDouble: {
                if (bl || !NBTCompound.isEqual(nBTCompound2, nBTCompound3, string)) {
                    nBTCompound.setDouble(string, nBTCompound2.getDouble(string));
                }
                return;
            }
            case NBTTagEnd: {
                return;
            }
            case NBTTagFloat: {
                if (bl || !NBTCompound.isEqual(nBTCompound2, nBTCompound3, string)) {
                    nBTCompound.setFloat(string, nBTCompound2.getFloat(string));
                }
                return;
            }
            case NBTTagInt: {
                if (bl || !NBTCompound.isEqual(nBTCompound2, nBTCompound3, string)) {
                    nBTCompound.setInteger(string, nBTCompound2.getInteger(string));
                }
                return;
            }
            case NBTTagIntArray: {
                if (bl || !NBTCompound.isEqual(nBTCompound2, nBTCompound3, string)) {
                    nBTCompound.setIntArray(string, nBTCompound2.getIntArray(string));
                }
                return;
            }
            case NBTTagList: {
                if (bl || !NBTCompound.isEqual(nBTCompound2, nBTCompound3, string)) {
                    nBTCompound.set(string, NBTReflectionUtil.getEntry(nBTCompound2, string));
                }
                return;
            }
            case NBTTagLong: {
                if (bl || !NBTCompound.isEqual(nBTCompound2, nBTCompound3, string)) {
                    nBTCompound.setLong(string, nBTCompound2.getLong(string));
                }
                return;
            }
            case NBTTagShort: {
                if (bl || !NBTCompound.isEqual(nBTCompound2, nBTCompound3, string)) {
                    nBTCompound.setShort(string, nBTCompound2.getShort(string));
                }
                return;
            }
            case NBTTagString: {
                if (bl || !NBTCompound.isEqual(nBTCompound2, nBTCompound3, string)) {
                    nBTCompound.setString(string, nBTCompound2.getString(string));
                }
                return;
            }
            case NBTTagLongArray: {
                if (bl || !NBTCompound.isEqual(nBTCompound2, nBTCompound3, string)) {
                    nBTCompound.setLongArray(string, nBTCompound2.getLongArray(string));
                }
                return;
            }
        }
    }

    private static boolean isEqual(NBTCompound nBTCompound, NBTCompound nBTCompound2, String string) {
        if (nBTCompound.getType(string) != nBTCompound2.getType(string)) {
            return false;
        }
        switch (nBTCompound.getType(string)) {
            case NBTTagByte: {
                return nBTCompound.getByte(string).equals(nBTCompound2.getByte(string));
            }
            case NBTTagByteArray: {
                return Arrays.equals(nBTCompound.getByteArray(string), nBTCompound2.getByteArray(string));
            }
            case NBTTagCompound: {
                NBTCompound nBTCompound3 = nBTCompound.getCompound(string);
                return nBTCompound3 != null && nBTCompound3.equals(nBTCompound2.getCompound(string));
            }
            case NBTTagDouble: {
                return nBTCompound.getDouble(string).equals(nBTCompound2.getDouble(string));
            }
            case NBTTagEnd: {
                return true;
            }
            case NBTTagFloat: {
                return nBTCompound.getFloat(string).equals(nBTCompound2.getFloat(string));
            }
            case NBTTagInt: {
                return nBTCompound.getInteger(string).equals(nBTCompound2.getInteger(string));
            }
            case NBTTagIntArray: {
                return Arrays.equals(nBTCompound.getIntArray(string), nBTCompound2.getIntArray(string));
            }
            case NBTTagList: {
                return NBTReflectionUtil.getEntry(nBTCompound, string).toString().equals(NBTReflectionUtil.getEntry(nBTCompound2, string).toString());
            }
            case NBTTagLong: {
                return nBTCompound.getLong(string).equals(nBTCompound2.getLong(string));
            }
            case NBTTagShort: {
                return nBTCompound.getShort(string).equals(nBTCompound2.getShort(string));
            }
            case NBTTagString: {
                return nBTCompound.getString(string).equals(nBTCompound2.getString(string));
            }
            case NBTTagLongArray: {
                return Arrays.equals(nBTCompound.getLongArray(string), nBTCompound2.getLongArray(string));
            }
        }
        return false;
    }
}

