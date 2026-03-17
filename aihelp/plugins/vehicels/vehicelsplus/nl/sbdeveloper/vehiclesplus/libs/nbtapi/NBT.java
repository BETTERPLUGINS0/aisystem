/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  org.bukkit.block.BlockState
 *  org.bukkit.entity.Entity
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi;

import com.mojang.authlib.GameProfile;
import java.io.File;
import java.io.InputStream;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import javax.annotation.Nullable;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTCompound;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTContainer;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTEntity;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTFile;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTGameProfile;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTItem;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTTileEntity;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NbtApiException;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.NBTFileHandle;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadWriteItemNBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadWriteNBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadableItemNBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadableNBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadableNBTList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.MinecraftVersion;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ClassWrapper;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ReflectionMethod;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.wrapper.NBTProxy;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.wrapper.ProxyBuilder;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class NBT {
    private NBT() {
    }

    public static boolean preloadApi() {
        try {
            if (MinecraftVersion.getVersion() == MinecraftVersion.UNKNOWN) {
                NbtApiException.confirmedBroken = true;
                return false;
            }
            for (ClassWrapper enum_ : ClassWrapper.values()) {
                if (!enum_.isEnabled() || enum_.getClazz() != null) continue;
                NbtApiException.confirmedBroken = true;
                return false;
            }
            for (Enum enum_ : ReflectionMethod.values()) {
                if (!((ReflectionMethod)enum_).isCompatible() || ((ReflectionMethod)enum_).isLoaded()) continue;
                NbtApiException.confirmedBroken = true;
                return false;
            }
            return true;
        } catch (Exception exception) {
            NbtApiException.confirmedBroken = true;
            MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] Error during the selfcheck!", exception);
            return false;
        }
    }

    public static ReadableNBT readNbt(ItemStack itemStack) {
        return new NBTItem(itemStack.clone(), false, true, false);
    }

    public static <T> T get(ItemStack itemStack, Function<ReadableItemNBT, T> function) {
        NBTItem nBTItem = new NBTItem(itemStack, false, true, false);
        T t = function.apply(nBTItem);
        if (t instanceof ReadableNBT || t instanceof ReadableNBTList) {
            throw new NbtApiException("Tried returning part of the NBT to outside of the NBT scope!");
        }
        nBTItem.setClosed();
        return t;
    }

    public static void get(ItemStack itemStack, Consumer<ReadableItemNBT> consumer) {
        NBTItem nBTItem = new NBTItem(itemStack, false, true, false);
        consumer.accept(nBTItem);
        nBTItem.setClosed();
    }

    public static <T> T get(Entity entity, Function<ReadableNBT, T> function) {
        NBTEntity nBTEntity = new NBTEntity(entity, true);
        T t = function.apply(nBTEntity);
        if (t instanceof ReadableNBT || t instanceof ReadableNBTList) {
            throw new NbtApiException("Tried returning part of the NBT to outside of the NBT scope!");
        }
        nBTEntity.setClosed();
        return t;
    }

    public static void get(Entity entity, Consumer<ReadableNBT> consumer) {
        NBTEntity nBTEntity = new NBTEntity(entity, true);
        consumer.accept(nBTEntity);
        nBTEntity.setClosed();
    }

    public static <T> T get(BlockState blockState, Function<ReadableNBT, T> function) {
        NBTTileEntity nBTTileEntity = new NBTTileEntity(blockState, true);
        T t = function.apply(nBTTileEntity);
        if (t instanceof ReadableNBT || t instanceof ReadableNBTList) {
            throw new NbtApiException("Tried returning part of the NBT to outside of the NBT scope!");
        }
        nBTTileEntity.setClosed();
        return t;
    }

    public static void get(BlockState blockState, Consumer<ReadableNBT> consumer) {
        NBTTileEntity nBTTileEntity = new NBTTileEntity(blockState, true);
        consumer.accept(nBTTileEntity);
        nBTTileEntity.setClosed();
    }

    public static <T> T getPersistentData(Entity entity, Function<ReadableNBT, T> function) {
        T t = function.apply(new NBTEntity(entity).getPersistentDataContainer());
        if (t instanceof ReadableNBT || t instanceof ReadableNBTList) {
            throw new NbtApiException("Tried returning part of the NBT to outside of the NBT scope!");
        }
        return t;
    }

    public static <T> T getPersistentData(BlockState blockState, Function<ReadableNBT, T> function) {
        T t = function.apply(new NBTTileEntity(blockState).getPersistentDataContainer());
        if (t instanceof ReadableNBT || t instanceof ReadableNBTList) {
            throw new NbtApiException("Tried returning part of the NBT to outside of the NBT scope!");
        }
        return t;
    }

    public static <T> T modify(ItemStack itemStack, Function<ReadWriteItemNBT, T> function) {
        NBTItem nBTItem = new NBTItem(itemStack, false, false, true);
        T t = function.apply(nBTItem);
        nBTItem.finalizeChanges();
        if (t instanceof ReadableNBT || t instanceof ReadableNBTList) {
            throw new NbtApiException("Tried returning part of the NBT to outside of the NBT scope!");
        }
        nBTItem.setClosed();
        return t;
    }

    public static void modify(ItemStack itemStack, Consumer<ReadWriteItemNBT> consumer) {
        NBTItem nBTItem = new NBTItem(itemStack, false, false, true);
        consumer.accept(nBTItem);
        nBTItem.finalizeChanges();
        nBTItem.setClosed();
    }

    public static <T> T modify(Entity entity, Function<ReadWriteNBT, T> function) {
        NBTEntity nBTEntity = new NBTEntity(entity);
        NBTContainer nBTContainer = new NBTContainer(nBTEntity.getCompound());
        T t = function.apply(nBTContainer);
        nBTEntity.setCompound(nBTContainer.getCompound());
        if (t instanceof ReadableNBT || t instanceof ReadableNBTList) {
            throw new NbtApiException("Tried returning part of the NBT to outside of the NBT scope!");
        }
        nBTEntity.setClosed();
        return t;
    }

    public static void modifyComponents(ItemStack itemStack, Consumer<ReadWriteNBT> consumer) {
        if (!MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
            throw new NbtApiException("This method only works for 1.20.5+!");
        }
        ReadWriteNBT readWriteNBT = NBT.itemStackToNBT(itemStack);
        consumer.accept(readWriteNBT.getOrCreateCompound("components"));
        ItemStack itemStack2 = NBT.itemStackFromNBT(readWriteNBT);
        itemStack.setItemMeta(itemStack2.getItemMeta());
    }

    public static <T> T modifyComponents(ItemStack itemStack, Function<ReadWriteNBT, T> function) {
        if (!MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
            throw new NbtApiException("This method only works for 1.20.5+!");
        }
        ReadWriteNBT readWriteNBT = NBT.itemStackToNBT(itemStack);
        T t = function.apply(readWriteNBT.getOrCreateCompound("components"));
        ItemStack itemStack2 = NBT.itemStackFromNBT(readWriteNBT);
        itemStack.setItemMeta(itemStack2.getItemMeta());
        return t;
    }

    public static void getComponents(ItemStack itemStack, Consumer<ReadableNBT> consumer) {
        if (!MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
            throw new NbtApiException("This method only works for 1.20.5+!");
        }
        ReadWriteNBT readWriteNBT = NBT.itemStackToNBT(itemStack);
        consumer.accept(readWriteNBT.getOrCreateCompound("components"));
    }

    public static <T> T getComponents(ItemStack itemStack, Function<ReadableNBT, T> function) {
        if (!MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
            throw new NbtApiException("This method only works for 1.20.5+!");
        }
        ReadWriteNBT readWriteNBT = NBT.itemStackToNBT(itemStack);
        return function.apply(readWriteNBT.getOrCreateCompound("components"));
    }

    public static void modify(Entity entity, Consumer<ReadWriteNBT> consumer) {
        NBTEntity nBTEntity = new NBTEntity(entity);
        NBTContainer nBTContainer = new NBTContainer(nBTEntity.getCompound());
        consumer.accept(nBTContainer);
        nBTEntity.setCompound(nBTContainer.getCompound());
        nBTEntity.setClosed();
    }

    public static <T> T modifyPersistentData(Entity entity, Function<ReadWriteNBT, T> function) {
        T t = function.apply(new NBTEntity(entity).getPersistentDataContainer());
        if (t instanceof ReadableNBT || t instanceof ReadableNBTList) {
            throw new NbtApiException("Tried returning part of the NBT to outside of the NBT scope!");
        }
        return t;
    }

    public static void modifyPersistentData(Entity entity, Consumer<ReadWriteNBT> consumer) {
        consumer.accept(new NBTEntity(entity).getPersistentDataContainer());
    }

    public static <T> T modify(BlockState blockState, Function<ReadWriteNBT, T> function) {
        NBTTileEntity nBTTileEntity = new NBTTileEntity(blockState);
        NBTContainer nBTContainer = new NBTContainer(nBTTileEntity.getCompound());
        T t = function.apply(nBTContainer);
        nBTTileEntity.setCompound(nBTContainer.getCompound());
        if (t instanceof ReadableNBT || t instanceof ReadableNBTList) {
            throw new NbtApiException("Tried returning part of the NBT to outside of the NBT scope!");
        }
        nBTTileEntity.setClosed();
        return t;
    }

    public static void modify(BlockState blockState, Consumer<ReadWriteNBT> consumer) {
        NBTTileEntity nBTTileEntity = new NBTTileEntity(blockState);
        NBTContainer nBTContainer = new NBTContainer(nBTTileEntity.getCompound());
        consumer.accept(nBTContainer);
        nBTTileEntity.setCompound(nBTContainer.getCompound());
        nBTTileEntity.setClosed();
    }

    public static <T> T modifyPersistentData(BlockState blockState, Function<ReadWriteNBT, T> function) {
        T t = function.apply(new NBTTileEntity(blockState).getPersistentDataContainer());
        if (t instanceof ReadableNBT || t instanceof ReadableNBTList) {
            throw new NbtApiException("Tried returning part of the NBT to outside of the NBT scope!");
        }
        return t;
    }

    public static void modifyPersistentData(BlockState blockState, Consumer<ReadWriteNBT> consumer) {
        consumer.accept(new NBTTileEntity(blockState).getPersistentDataContainer());
    }

    public static ReadWriteNBT gameProfileToNBT(GameProfile gameProfile) {
        return NBTGameProfile.toNBT(gameProfile);
    }

    public static GameProfile gameProfileFromNBT(ReadableNBT readableNBT) {
        return NBTGameProfile.fromNBT((NBTCompound)readableNBT);
    }

    public static ReadWriteNBT itemStackToNBT(ItemStack itemStack) {
        return NBTItem.convertItemtoNBT(itemStack);
    }

    @Nullable
    public static ItemStack itemStackFromNBT(ReadableNBT readableNBT) {
        return NBTItem.convertNBTtoItem((NBTCompound)readableNBT);
    }

    public static ReadWriteNBT itemStackArrayToNBT(ItemStack[] itemStackArray) {
        return NBTItem.convertItemArraytoNBT(itemStackArray);
    }

    @Nullable
    public static ItemStack[] itemStackArrayFromNBT(ReadableNBT readableNBT) {
        return NBTItem.convertNBTtoItemArray((NBTCompound)readableNBT);
    }

    public static ReadWriteNBT createNBTObject() {
        return new NBTContainer();
    }

    public static ReadWriteNBT parseNBT(String string) {
        return new NBTContainer(string);
    }

    public static ReadWriteNBT readNBT(InputStream inputStream) {
        return new NBTContainer(inputStream);
    }

    public static ReadWriteNBT wrapNMSTag(Object object) {
        return new NBTContainer(object);
    }

    public static NBTFileHandle getFileHandle(File file) {
        return new NBTFile(file);
    }

    public static ReadWriteNBT readFile(File file) {
        return NBTFile.readFrom(file);
    }

    public static void writeFile(File file, ReadWriteNBT readWriteNBT) {
        NBTFile.saveTo(file, (NBTCompound)readWriteNBT);
    }

    public static <T extends NBTProxy> T readNbt(ItemStack itemStack, Class<T> clazz) {
        return new ProxyBuilder<T>(new NBTItem(itemStack, false, true, false), clazz).readOnly().build();
    }

    public static <T extends NBTProxy> T readNbt(Entity entity, Class<T> clazz) {
        return new ProxyBuilder<T>(new NBTEntity(entity, true), clazz).readOnly().build();
    }

    public static <T extends NBTProxy> T readNbt(BlockState blockState, Class<T> clazz) {
        return new ProxyBuilder<T>(new NBTTileEntity(blockState, true), clazz).readOnly().build();
    }

    public static <T, X extends NBTProxy> T modify(ItemStack itemStack, Class<X> clazz, Function<X, T> function) {
        NBTItem nBTItem = new NBTItem(itemStack, false, false, true);
        T t = function.apply(new ProxyBuilder<X>(nBTItem, clazz).build());
        nBTItem.finalizeChanges();
        if (t instanceof ReadableNBT || t instanceof ReadableNBTList) {
            throw new NbtApiException("Tried returning part of the NBT to outside of the NBT scope!");
        }
        nBTItem.setClosed();
        return t;
    }

    public static <X extends NBTProxy> void modify(ItemStack itemStack, Class<X> clazz, Consumer<X> consumer) {
        NBTItem nBTItem = new NBTItem(itemStack, false, false, true);
        consumer.accept(new ProxyBuilder<X>(nBTItem, clazz).build());
        nBTItem.finalizeChanges();
        nBTItem.setClosed();
    }

    public static <X extends NBTProxy> void modify(Entity entity, Class<X> clazz, Consumer<X> consumer) {
        NBTEntity nBTEntity = new NBTEntity(entity);
        NBTContainer nBTContainer = new NBTContainer(nBTEntity.getCompound());
        consumer.accept(new ProxyBuilder<X>(nBTContainer, clazz).build());
        nBTEntity.setCompound(nBTContainer.getCompound());
        nBTContainer.setClosed();
    }

    public static <T, X extends NBTProxy> T modify(Entity entity, Class<X> clazz, Function<X, T> function) {
        NBTEntity nBTEntity = new NBTEntity(entity);
        NBTContainer nBTContainer = new NBTContainer(nBTEntity.getCompound());
        T t = function.apply(new ProxyBuilder<X>(nBTContainer, clazz).build());
        nBTEntity.setCompound(nBTContainer.getCompound());
        nBTContainer.setClosed();
        return t;
    }

    public static <X extends NBTProxy> void modify(BlockState blockState, Class<X> clazz, Consumer<X> consumer) {
        NBTTileEntity nBTTileEntity = new NBTTileEntity(blockState);
        NBTContainer nBTContainer = new NBTContainer(nBTTileEntity.getCompound());
        consumer.accept(new ProxyBuilder<X>(nBTContainer, clazz).build());
        nBTTileEntity.setCompound(nBTContainer);
        nBTContainer.setClosed();
    }

    public static <T, X extends NBTProxy> T modify(BlockState blockState, Class<X> clazz, Function<X, T> function) {
        NBTTileEntity nBTTileEntity = new NBTTileEntity(blockState);
        NBTContainer nBTContainer = new NBTContainer(nBTTileEntity.getCompound());
        T t = function.apply(new ProxyBuilder<X>(nBTContainer, clazz).build());
        nBTTileEntity.setCompound(nBTContainer);
        nBTContainer.setClosed();
        return t;
    }
}

