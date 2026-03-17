/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.BlockVector
 *  com.sk89q.worldedit.Vector
 *  com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion
 *  com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion
 *  com.sk89q.worldguard.protection.regions.ProtectedRegion
 *  org.bukkit.Location
 *  org.bukkit.World
 */
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.region;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.IWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.flag.AbstractWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.utility.WorldGuardFlagUtilities;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.utility.WorldGuardVectorUtilities;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedDomain;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegion;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.ICuboidSelection;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.IPolygonalSelection;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.ISelection;
import org.bukkit.Location;
import org.bukkit.World;

public class WrappedRegion
implements IWrappedRegion {
    private final World world;
    private final ProtectedRegion handle;

    @Override
    public ISelection getSelection() {
        if (this.handle instanceof ProtectedCuboidRegion) {
            return new ICuboidSelection(){

                @Override
                public Location getMinimumPoint() {
                    return WorldGuardVectorUtilities.fromBlockVector(WrappedRegion.this.world, WrappedRegion.this.handle.getMinimumPoint());
                }

                @Override
                public Location getMaximumPoint() {
                    return WorldGuardVectorUtilities.fromBlockVector(WrappedRegion.this.world, WrappedRegion.this.handle.getMaximumPoint());
                }
            };
        }
        if (this.handle instanceof ProtectedPolygonalRegion) {
            return new IPolygonalSelection(){

                @Override
                public Set<Location> getPoints() {
                    return WrappedRegion.this.handle.getPoints().stream().map(vector -> new BlockVector(vector.toVector())).map(vector -> WorldGuardVectorUtilities.fromBlockVector(WrappedRegion.this.world, vector)).collect(Collectors.toSet());
                }

                @Override
                public int getMinimumY() {
                    return WrappedRegion.this.handle.getMinimumPoint().getBlockY();
                }

                @Override
                public int getMaximumY() {
                    return WrappedRegion.this.handle.getMaximumPoint().getBlockY();
                }
            };
        }
        throw new UnsupportedOperationException("Unsupported " + this.handle.getClass().getSimpleName() + " region!");
    }

    @Override
    public String getId() {
        return this.handle.getId();
    }

    @Override
    public Map<IWrappedFlag<?>, Object> getFlags() {
        HashMap result = new HashMap();
        this.handle.getFlags().forEach((flag, value) -> {
            if (value != null) {
                try {
                    Map.Entry<IWrappedFlag<?>, Object> wrapped = WorldGuardFlagUtilities.wrap(flag, value);
                    result.put(wrapped.getKey(), wrapped.getValue());
                } catch (IllegalArgumentException illegalArgumentException) {
                    // empty catch block
                }
            }
        });
        return result;
    }

    @Override
    public <T> Optional<T> getFlag(IWrappedFlag<T> flag) {
        AbstractWrappedFlag wrappedFlag = (AbstractWrappedFlag)flag;
        return Optional.ofNullable(this.handle.getFlag(wrappedFlag.getHandle())).map(value -> wrappedFlag.fromWGValue(value));
    }

    @Override
    public <T> void setFlag(IWrappedFlag<T> flag, T value) {
        AbstractWrappedFlag wrappedFlag = (AbstractWrappedFlag)flag;
        this.handle.setFlag(wrappedFlag.getHandle(), wrappedFlag.fromWrapperValue(value).orElse(null));
    }

    @Override
    public int getPriority() {
        return this.handle.getPriority();
    }

    @Override
    public void setPriority(int priority) {
        this.handle.setPriority(priority);
    }

    @Override
    public IWrappedDomain getOwners() {
        return new IWrappedDomain(){

            @Override
            public Set<UUID> getPlayers() {
                return WrappedRegion.this.handle.getOwners().getUniqueIds();
            }

            @Override
            public void addPlayer(UUID uuid) {
                WrappedRegion.this.handle.getOwners().addPlayer(uuid);
            }

            @Override
            public void removePlayer(UUID uuid) {
                WrappedRegion.this.handle.getOwners().removePlayer(uuid);
            }

            @Override
            public Set<String> getGroups() {
                return WrappedRegion.this.handle.getOwners().getGroups();
            }

            @Override
            public void addGroup(String name) {
                WrappedRegion.this.handle.getOwners().addGroup(name);
            }

            @Override
            public void removeGroup(String name) {
                WrappedRegion.this.handle.getOwners().removeGroup(name);
            }
        };
    }

    @Override
    public IWrappedDomain getMembers() {
        return new IWrappedDomain(){

            @Override
            public Set<UUID> getPlayers() {
                return WrappedRegion.this.handle.getMembers().getUniqueIds();
            }

            @Override
            public void addPlayer(UUID uuid) {
                WrappedRegion.this.handle.getMembers().addPlayer(uuid);
            }

            @Override
            public void removePlayer(UUID uuid) {
                WrappedRegion.this.handle.getMembers().removePlayer(uuid);
            }

            @Override
            public Set<String> getGroups() {
                return WrappedRegion.this.handle.getMembers().getGroups();
            }

            @Override
            public void addGroup(String name) {
                WrappedRegion.this.handle.getMembers().addGroup(name);
            }

            @Override
            public void removeGroup(String name) {
                WrappedRegion.this.handle.getMembers().removeGroup(name);
            }
        };
    }

    @Override
    public boolean contains(Location location) {
        return this.handle.contains((Vector)WorldGuardVectorUtilities.toBlockVector(location));
    }

    public WrappedRegion(World world, ProtectedRegion handle) {
        this.world = world;
        this.handle = handle;
    }

    public World getWorld() {
        return this.world;
    }

    public ProtectedRegion getHandle() {
        return this.handle;
    }
}

