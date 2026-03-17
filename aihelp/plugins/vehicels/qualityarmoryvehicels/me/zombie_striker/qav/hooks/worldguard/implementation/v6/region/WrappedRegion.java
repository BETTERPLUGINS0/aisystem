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
package me.zombie_striker.qav.hooks.worldguard.implementation.v6.region;

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
import me.zombie_striker.qav.hooks.worldguard.flag.IWrappedFlag;
import me.zombie_striker.qav.hooks.worldguard.implementation.v6.flag.AbstractWrappedFlag;
import me.zombie_striker.qav.hooks.worldguard.implementation.v6.utility.WorldGuardFlagUtilities;
import me.zombie_striker.qav.hooks.worldguard.implementation.v6.utility.WorldGuardVectorUtilities;
import me.zombie_striker.qav.hooks.worldguard.region.IWrappedDomain;
import me.zombie_striker.qav.hooks.worldguard.region.IWrappedRegion;
import me.zombie_striker.qav.hooks.worldguard.selection.ICuboidSelection;
import me.zombie_striker.qav.hooks.worldguard.selection.IPolygonalSelection;
import me.zombie_striker.qav.hooks.worldguard.selection.ISelection;
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
                    return WrappedRegion.this.handle.getPoints().stream().map(blockVector2D -> new BlockVector(blockVector2D.toVector())).map(blockVector -> WorldGuardVectorUtilities.fromBlockVector(WrappedRegion.this.world, blockVector)).collect(Collectors.toSet());
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
        HashMap hashMap = new HashMap();
        this.handle.getFlags().forEach((flag, object) -> {
            if (object != null) {
                try {
                    Map.Entry<IWrappedFlag<?>, Object> entry = WorldGuardFlagUtilities.wrap(flag, object);
                    hashMap.put(entry.getKey(), entry.getValue());
                } catch (IllegalArgumentException illegalArgumentException) {
                    // empty catch block
                }
            }
        });
        return hashMap;
    }

    @Override
    public <T> Optional<T> getFlag(IWrappedFlag<T> iWrappedFlag) {
        AbstractWrappedFlag abstractWrappedFlag = (AbstractWrappedFlag)iWrappedFlag;
        return Optional.ofNullable(this.handle.getFlag(abstractWrappedFlag.getHandle())).map(object -> abstractWrappedFlag.fromWGValue(object));
    }

    @Override
    public <T> void setFlag(IWrappedFlag<T> iWrappedFlag, T t) {
        AbstractWrappedFlag abstractWrappedFlag = (AbstractWrappedFlag)iWrappedFlag;
        this.handle.setFlag(abstractWrappedFlag.getHandle(), abstractWrappedFlag.fromWrapperValue(t).orElse(null));
    }

    @Override
    public int getPriority() {
        return this.handle.getPriority();
    }

    @Override
    public void setPriority(int n) {
        this.handle.setPriority(n);
    }

    @Override
    public IWrappedDomain getOwners() {
        return new IWrappedDomain(){

            @Override
            public Set<UUID> getPlayers() {
                return WrappedRegion.this.handle.getOwners().getUniqueIds();
            }

            @Override
            public void addPlayer(UUID uUID) {
                WrappedRegion.this.handle.getOwners().addPlayer(uUID);
            }

            @Override
            public void removePlayer(UUID uUID) {
                WrappedRegion.this.handle.getOwners().removePlayer(uUID);
            }

            @Override
            public Set<String> getGroups() {
                return WrappedRegion.this.handle.getOwners().getGroups();
            }

            @Override
            public void addGroup(String string) {
                WrappedRegion.this.handle.getOwners().addGroup(string);
            }

            @Override
            public void removeGroup(String string) {
                WrappedRegion.this.handle.getOwners().removeGroup(string);
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
            public void addPlayer(UUID uUID) {
                WrappedRegion.this.handle.getMembers().addPlayer(uUID);
            }

            @Override
            public void removePlayer(UUID uUID) {
                WrappedRegion.this.handle.getMembers().removePlayer(uUID);
            }

            @Override
            public Set<String> getGroups() {
                return WrappedRegion.this.handle.getMembers().getGroups();
            }

            @Override
            public void addGroup(String string) {
                WrappedRegion.this.handle.getMembers().addGroup(string);
            }

            @Override
            public void removeGroup(String string) {
                WrappedRegion.this.handle.getMembers().removeGroup(string);
            }
        };
    }

    @Override
    public boolean contains(Location location) {
        return this.handle.contains((Vector)WorldGuardVectorUtilities.toBlockVector(location));
    }

    public WrappedRegion(World world, ProtectedRegion protectedRegion) {
        this.world = world;
        this.handle = protectedRegion;
    }

    public World getWorld() {
        return this.world;
    }

    public ProtectedRegion getHandle() {
        return this.handle;
    }
}

