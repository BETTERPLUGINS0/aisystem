/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region;

import java.util.Map;
import java.util.Optional;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.IWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedDomain;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.ISelection;
import org.bukkit.Location;

public interface IWrappedRegion {
    public ISelection getSelection();

    public String getId();

    public Map<IWrappedFlag<?>, Object> getFlags();

    public <T> Optional<T> getFlag(IWrappedFlag<T> var1);

    public <T> void setFlag(IWrappedFlag<T> var1, T var2);

    public int getPriority();

    public void setPriority(int var1);

    public IWrappedDomain getOwners();

    public IWrappedDomain getMembers();

    public boolean contains(Location var1);
}

