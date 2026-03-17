/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.infrastructure.libs.nbtapi.iface;

import java.io.File;
import java.io.IOException;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.iface.ReadWriteNBT;

public interface NBTFileHandle
extends ReadWriteNBT {
    public void save() throws IOException;

    public File getFile();
}

