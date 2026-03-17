/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.persistence.PersistentDataContainer
 */
package nl.mtvehicles.core.infrastructure.libs.nbtapi;

import java.util.Map;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTCompound;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ReflectionMethod;
import org.bukkit.persistence.PersistentDataContainer;

public class NBTPersistentDataContainer
extends NBTCompound {
    private final PersistentDataContainer container;

    public NBTPersistentDataContainer(PersistentDataContainer container) {
        super(null, null);
        this.container = container;
    }

    @Override
    public Object getCompound() {
        return ReflectionMethod.CRAFT_PERSISTENT_DATA_CONTAINER_TO_TAG.run(this.container, new Object[0]);
    }

    @Override
    protected void setCompound(Object compound) {
        Map map = (Map)ReflectionMethod.CRAFT_PERSISTENT_DATA_CONTAINER_GET_MAP.run(this.container, new Object[0]);
        map.clear();
        ReflectionMethod.CRAFT_PERSISTENT_DATA_CONTAINER_PUT_ALL.run(this.container, compound);
    }
}

