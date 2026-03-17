/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.OfflinePlayer
 */
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.IWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegion;
import org.bukkit.OfflinePlayer;

public interface IWrappedRegionSet
extends Iterable<IWrappedRegion> {
    public boolean isVirtual();

    public <V> Optional<V> queryValue(OfflinePlayer var1, IWrappedFlag<V> var2);

    public <V> Collection<V> queryAllValues(OfflinePlayer var1, IWrappedFlag<V> var2);

    public boolean isOwnerOfAll(OfflinePlayer var1);

    public boolean isMemberOfAll(OfflinePlayer var1);

    public int size();

    public Set<IWrappedRegion> getRegions();
}

