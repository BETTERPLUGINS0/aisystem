/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi;

import java.util.Set;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTCompound;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTCompoundList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTContainer;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTListCompound;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTReflectionUtil;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NbtApiException;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadWriteItemNBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadWriteNBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadableNBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.MinecraftVersion;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ClassWrapper;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ReflectionMethod;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NBTItem
extends NBTCompound
implements ReadWriteItemNBT {
    private ItemStack bukkitItem;
    private final boolean directApply;
    private final boolean finalizer;
    private ItemStack originalSrcStack = null;
    private Object cachedCompound = null;
    private boolean closed = false;

    @Deprecated
    public NBTItem(ItemStack itemStack) {
        this(itemStack, false);
    }

    protected NBTItem(ItemStack itemStack, boolean bl, boolean bl2, boolean bl3) {
        super(null, null, bl2);
        if (itemStack == null || itemStack.getType() == Material.AIR || itemStack.getAmount() <= 0) {
            throw new NullPointerException("ItemStack can't be null/air/amount of 0! This is not a NBTAPI bug!");
        }
        this.finalizer = bl3;
        if (bl3) {
            this.bukkitItem = itemStack;
            this.originalSrcStack = itemStack;
            this.directApply = false;
        } else if (bl2) {
            this.bukkitItem = itemStack;
            this.directApply = false;
        } else {
            this.directApply = bl;
            this.bukkitItem = itemStack.clone();
            if (bl) {
                this.originalSrcStack = itemStack;
            }
        }
    }

    @Deprecated
    public NBTItem(ItemStack itemStack, boolean bl) {
        super(null, null);
        if (itemStack == null || itemStack.getType() == Material.AIR || itemStack.getAmount() <= 0) {
            throw new NullPointerException("ItemStack can't be null/air/amount of 0! This is not a NBTAPI bug!");
        }
        this.finalizer = false;
        this.directApply = bl;
        this.bukkitItem = itemStack.clone();
        if (bl) {
            this.originalSrcStack = itemStack;
        }
    }

    @Override
    public Object getCompound() {
        if (this.closed) {
            throw new NbtApiException("Tried using closed NBT data!");
        }
        if (this.isReadOnly() && (this.cachedCompound != null || ClassWrapper.CRAFT_ITEMSTACK.getClazz().isAssignableFrom(this.bukkitItem.getClass()))) {
            if (this.cachedCompound == null) {
                this.cachedCompound = NBTReflectionUtil.getItemRootNBTTagCompound(NBTReflectionUtil.getCraftItemHandle(this.bukkitItem));
            }
            return this.cachedCompound;
        }
        if (this.finalizer) {
            if (this.cachedCompound == null) {
                this.updateCachedCompound();
            }
            return this.cachedCompound;
        }
        return NBTReflectionUtil.getItemRootNBTTagCompound(ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, this.bukkitItem));
    }

    private void updateCachedCompound() {
        if (this.finalizer) {
            this.cachedCompound = NBTReflectionUtil.getItemRootNBTTagCompound(ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, this.bukkitItem));
        }
    }

    protected void finalizeChanges() {
        if (!this.finalizer || this.cachedCompound == null) {
            return;
        }
        if (NBTReflectionUtil.getKeys(this).isEmpty()) {
            this.cachedCompound = null;
        }
        if (ClassWrapper.CRAFT_ITEMSTACK.getClazz().isAssignableFrom(this.originalSrcStack.getClass())) {
            Object object = NBTReflectionUtil.getCraftItemHandle(this.originalSrcStack);
            NBTReflectionUtil.setItemStackCompound(object, this.cachedCompound);
            this.bukkitItem = this.originalSrcStack;
        } else {
            Object object = ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, this.bukkitItem);
            NBTReflectionUtil.setItemStackCompound(object, this.cachedCompound);
            this.bukkitItem = (ItemStack)ReflectionMethod.ITEMSTACK_BUKKITMIRROR.run(null, object);
            this.originalSrcStack.setItemMeta(this.bukkitItem.getItemMeta());
        }
    }

    @Override
    protected void setClosed() {
        this.closed = true;
    }

    @Override
    protected boolean isClosed() {
        return this.closed;
    }

    @Override
    protected void setCompound(Object object) {
        if (this.isReadOnly()) {
            throw new NbtApiException("Tried setting data in read only mode!");
        }
        if (this.closed) {
            throw new NbtApiException("Tried using closed NBT data!");
        }
        if (this.finalizer) {
            this.cachedCompound = object;
            return;
        }
        if (object != null && ((Set)ReflectionMethod.COMPOUND_GET_KEYS.run(object, new Object[0])).isEmpty()) {
            object = null;
        }
        if (ClassWrapper.CRAFT_ITEMSTACK.getClazz().isAssignableFrom(this.bukkitItem.getClass())) {
            Object object2 = NBTReflectionUtil.getCraftItemHandle(this.bukkitItem);
            NBTReflectionUtil.setItemStackCompound(object2, object);
        } else {
            Object object3 = ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, this.bukkitItem);
            NBTReflectionUtil.setItemStackCompound(object3, object);
            this.bukkitItem = (ItemStack)ReflectionMethod.ITEMSTACK_BUKKITMIRROR.run(null, object3);
        }
    }

    @Deprecated
    public void applyNBT(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            throw new NullPointerException("ItemStack can't be null/Air! This is not a NBTAPI bug!");
        }
        NBTItem nBTItem = new NBTItem(new ItemStack(itemStack.getType()));
        nBTItem.mergeCompound(this);
        itemStack.setItemMeta(nBTItem.getItem().getItemMeta());
    }

    @Deprecated
    public void mergeNBT(ItemStack itemStack) {
        NBTItem nBTItem = new NBTItem(itemStack);
        nBTItem.mergeCompound(this);
        itemStack.setItemMeta(nBTItem.getItem().getItemMeta());
    }

    @Deprecated
    public void mergeCustomNBT(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            throw new NullPointerException("ItemStack can't be null/Air!");
        }
        if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
            NBT.modify(itemStack, readWriteItemNBT -> readWriteItemNBT.mergeCompound(this));
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        NBTReflectionUtil.getUnhandledNBTTags(itemMeta).putAll(NBTReflectionUtil.getUnhandledNBTTags(this.bukkitItem.getItemMeta()));
        itemStack.setItemMeta(itemMeta);
    }

    @Override
    @Deprecated
    public boolean hasCustomNbtData() {
        if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
            return this.hasNBTData();
        }
        this.finalizeChanges();
        ItemMeta itemMeta = this.bukkitItem.getItemMeta();
        return !NBTReflectionUtil.getUnhandledNBTTags(itemMeta).isEmpty();
    }

    @Override
    @Deprecated
    public void clearCustomNBT() {
        this.finalizeChanges();
        if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
            this.setCompound(null);
            return;
        }
        ItemMeta itemMeta = this.bukkitItem.getItemMeta();
        NBTReflectionUtil.getUnhandledNBTTags(itemMeta).clear();
        this.bukkitItem.setItemMeta(itemMeta);
        this.updateCachedCompound();
    }

    public ItemStack getItem() {
        return this.bukkitItem;
    }

    protected void setItem(ItemStack itemStack) {
        this.bukkitItem = itemStack;
    }

    @Override
    public boolean hasNBTData() {
        return this.getCompound() != null;
    }

    @Override
    public void modifyMeta(BiConsumer<ReadableNBT, ItemMeta> biConsumer) {
        this.finalizeChanges();
        ItemMeta itemMeta = this.bukkitItem.getItemMeta();
        biConsumer.accept(new NBTContainer(this.getResolvedObject()).setReadOnly(true), itemMeta);
        this.bukkitItem.setItemMeta(itemMeta);
        this.updateCachedCompound();
        if (this.directApply) {
            if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
                throw new NbtApiException("Direct apply mode meta changes don't work anymore in 1.20.5+. Please switch to the modern NBT.modify sytnax!");
            }
            this.applyNBT(this.originalSrcStack);
        }
    }

    @Override
    public <T extends ItemMeta> void modifyMeta(Class<T> clazz, BiConsumer<ReadableNBT, T> biConsumer) {
        this.finalizeChanges();
        ItemMeta itemMeta = this.bukkitItem.getItemMeta();
        biConsumer.accept(new NBTContainer(this.getResolvedObject()).setReadOnly(true), (ReadableNBT)itemMeta);
        this.bukkitItem.setItemMeta(itemMeta);
        this.updateCachedCompound();
        if (this.directApply) {
            this.applyNBT(this.originalSrcStack);
        }
    }

    @Deprecated
    public static NBTContainer convertItemtoNBT(ItemStack itemStack) {
        return NBTReflectionUtil.convertNMSItemtoNBTCompound(ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, itemStack));
    }

    @Nullable
    @Deprecated
    public static ItemStack convertNBTtoItem(NBTCompound nBTCompound) {
        return (ItemStack)ReflectionMethod.ITEMSTACK_BUKKITMIRROR.run(null, NBTReflectionUtil.convertNBTCompoundtoNMSItem(nBTCompound));
    }

    @Deprecated
    public static NBTContainer convertItemArraytoNBT(ItemStack[] itemStackArray) {
        NBTContainer nBTContainer = new NBTContainer();
        nBTContainer.setInteger("size", itemStackArray.length);
        NBTCompoundList nBTCompoundList = nBTContainer.getCompoundList("items");
        for (int i = 0; i < itemStackArray.length; ++i) {
            ItemStack itemStack = itemStackArray[i];
            if (itemStack == null || itemStack.getType() == Material.AIR) continue;
            NBTListCompound nBTListCompound = nBTCompoundList.addCompound();
            nBTListCompound.setInteger("Slot", i);
            nBTListCompound.mergeCompound(NBTItem.convertItemtoNBT(itemStack));
        }
        return nBTContainer;
    }

    @Nullable
    @Deprecated
    public static ItemStack[] convertNBTtoItemArray(NBTCompound nBTCompound) {
        if (!nBTCompound.hasTag("size")) {
            return null;
        }
        ItemStack[] itemStackArray = new ItemStack[nBTCompound.getInteger("size").intValue()];
        for (int i = 0; i < itemStackArray.length; ++i) {
            itemStackArray[i] = new ItemStack(Material.AIR);
        }
        if (!nBTCompound.hasTag("items")) {
            return itemStackArray;
        }
        NBTCompoundList nBTCompoundList = nBTCompound.getCompoundList("items");
        for (ReadWriteNBT readWriteNBT : nBTCompoundList) {
            if (!(readWriteNBT instanceof NBTCompound)) continue;
            int n = readWriteNBT.getInteger("Slot");
            itemStackArray[n] = NBTItem.convertNBTtoItem((NBTCompound)readWriteNBT);
        }
        return itemStackArray;
    }

    @Override
    protected void saveCompound() {
        if (this.directApply) {
            this.applyNBT(this.originalSrcStack);
        }
    }
}

