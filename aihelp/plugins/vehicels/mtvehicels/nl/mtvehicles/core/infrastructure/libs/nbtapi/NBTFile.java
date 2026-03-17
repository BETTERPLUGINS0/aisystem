/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.infrastructure.libs.nbtapi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTCompound;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTContainer;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTReflectionUtil;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.iface.NBTFileHandle;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ObjectCreator;

public class NBTFile
extends NBTCompound
implements NBTFileHandle {
    private final File file;
    private Object nbt;

    @Deprecated
    public NBTFile(File file) throws IOException {
        super(null, null);
        if (file == null) {
            throw new NullPointerException("File can't be null!");
        }
        this.file = file;
        if (file.exists()) {
            this.nbt = NBTReflectionUtil.readNBT(Files.newInputStream(file.toPath(), new OpenOption[0]));
        } else {
            this.nbt = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
            this.save();
        }
    }

    @Override
    public void save() throws IOException {
        try {
            this.getWriteLock().lock();
            NBTFile.saveTo(this.file, this);
        } finally {
            this.getWriteLock().unlock();
        }
    }

    @Override
    public File getFile() {
        return this.file;
    }

    @Override
    public Object getCompound() {
        return this.nbt;
    }

    @Override
    protected void setCompound(Object compound) {
        this.nbt = compound;
    }

    @Deprecated
    public static NBTCompound readFrom(File file) throws IOException {
        if (!file.exists()) {
            return new NBTContainer();
        }
        return new NBTContainer(NBTReflectionUtil.readNBT(Files.newInputStream(file.toPath(), new OpenOption[0])));
    }

    @Deprecated
    public static void saveTo(File file, NBTCompound nbt) throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            if (!file.createNewFile()) {
                throw new IOException("Unable to create file at " + file.getAbsolutePath());
            }
        }
        nbt.writeCompound(Files.newOutputStream(file.toPath(), new OpenOption[0]));
    }
}

