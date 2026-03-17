/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.handler;

import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.NBTHandler;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadWriteNBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadableNBT;
import org.bukkit.inventory.ItemStack;

public class NBTHandlers {
    public static final NBTHandler<ItemStack> ITEM_STACK = new NBTHandler<ItemStack>(){

        @Override
        public boolean fuzzyMatch(Object object) {
            return object instanceof ItemStack;
        }

        @Override
        public void set(ReadWriteNBT readWriteNBT, String string, ItemStack itemStack) {
            readWriteNBT.removeKey(string);
            ReadWriteNBT readWriteNBT2 = readWriteNBT.getOrCreateCompound(string);
            readWriteNBT2.mergeCompound(NBT.itemStackToNBT(itemStack));
        }

        @Override
        public ItemStack get(ReadableNBT readableNBT, String string) {
            ReadableNBT readableNBT2 = readableNBT.getCompound(string);
            if (readableNBT2 != null) {
                return NBT.itemStackFromNBT(readableNBT2);
            }
            return null;
        }
    };
    public static final NBTHandler<ReadableNBT> STORE_READABLE_TAG = new NBTHandler<ReadableNBT>(){

        @Override
        public boolean fuzzyMatch(Object object) {
            return object instanceof ReadableNBT;
        }

        @Override
        public void set(ReadWriteNBT readWriteNBT, String string, ReadableNBT readableNBT) {
            readWriteNBT.removeKey(string);
            readWriteNBT.getOrCreateCompound(string).mergeCompound(readableNBT);
        }

        @Override
        public ReadableNBT get(ReadableNBT readableNBT, String string) {
            ReadableNBT readableNBT2 = readableNBT.getCompound(string);
            if (readableNBT2 != null) {
                ReadWriteNBT readWriteNBT = NBT.createNBTObject();
                readWriteNBT.mergeCompound(readableNBT2);
                return readWriteNBT;
            }
            return null;
        }
    };
    public static final NBTHandler<ReadWriteNBT> STORE_READWRITE_TAG = new NBTHandler<ReadWriteNBT>(){

        @Override
        public boolean fuzzyMatch(Object object) {
            return object instanceof ReadWriteNBT;
        }

        @Override
        public void set(ReadWriteNBT readWriteNBT, String string, ReadWriteNBT readWriteNBT2) {
            readWriteNBT.removeKey(string);
            readWriteNBT.getOrCreateCompound(string).mergeCompound(readWriteNBT2);
        }

        @Override
        public ReadWriteNBT get(ReadableNBT readableNBT, String string) {
            ReadableNBT readableNBT2 = readableNBT.getCompound(string);
            if (readableNBT2 != null) {
                ReadWriteNBT readWriteNBT = NBT.createNBTObject();
                readWriteNBT.mergeCompound(readableNBT2);
                return readWriteNBT;
            }
            return null;
        }
    };
}

