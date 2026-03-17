/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.block.BlockState
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi;

import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTCompound;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTContainer;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTReflectionUtil;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NbtApiException;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.CheckUtil;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.MinecraftVersion;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;

public class NBTTileEntity
extends NBTCompound {
    private final BlockState tile;
    private final boolean readonly;
    private final Object compound;
    private boolean closed = false;

    protected NBTTileEntity(BlockState blockState, boolean bl) {
        super(null, null);
        if (blockState == null || MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_8_R3) && !blockState.isPlaced()) {
            throw new NullPointerException("Tile can't be null/not placed!");
        }
        this.tile = blockState;
        this.readonly = bl;
        this.compound = bl ? this.getCompound() : null;
    }

    @Deprecated
    public NBTTileEntity(BlockState blockState) {
        super(null, null);
        if (blockState == null || MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_8_R3) && !blockState.isPlaced()) {
            throw new NullPointerException("Tile can't be null/not placed!");
        }
        this.readonly = false;
        this.compound = null;
        this.tile = blockState;
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
    protected boolean isReadOnly() {
        return this.readonly;
    }

    @Override
    public Object getCompound() {
        if (this.readonly && this.compound != null) {
            return this.compound;
        }
        if (!Bukkit.isPrimaryThread()) {
            throw new NbtApiException("BlockEntity NBT needs to be accessed sync!");
        }
        return NBTReflectionUtil.getTileEntityNBTTagCompound(this.tile);
    }

    @Override
    protected void setCompound(Object object) {
        if (this.readonly) {
            throw new NbtApiException("Tried setting data in read only mode!");
        }
        if (!Bukkit.isPrimaryThread()) {
            throw new NbtApiException("BlockEntity NBT needs to be accessed sync!");
        }
        NBTReflectionUtil.setTileEntityNBTTagCompound(this.tile, object);
    }

    public NBTCompound getPersistentDataContainer() {
        CheckUtil.assertAvailable(MinecraftVersion.MC1_14_R1);
        if (this.hasTag("PublicBukkitValues")) {
            return this.getCompound("PublicBukkitValues");
        }
        NBTContainer nBTContainer = new NBTContainer();
        nBTContainer.addCompound("PublicBukkitValues").setString("__nbtapi", "Marker to make the PersistentDataContainer have content");
        this.mergeCompound(nBTContainer);
        return this.getCompound("PublicBukkitValues");
    }
}

