/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package me.zombie_striker.qav.hooks.worldguard.region;

import java.util.Map;
import java.util.Optional;
import me.zombie_striker.qav.hooks.worldguard.flag.IWrappedFlag;
import me.zombie_striker.qav.hooks.worldguard.region.IWrappedDomain;
import me.zombie_striker.qav.hooks.worldguard.selection.ISelection;
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

