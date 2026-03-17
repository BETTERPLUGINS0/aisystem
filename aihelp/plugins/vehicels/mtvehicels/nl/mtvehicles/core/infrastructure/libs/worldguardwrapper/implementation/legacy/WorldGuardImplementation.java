/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.minecraft.util.commands.CommandException
 *  com.sk89q.worldedit.BlockVector
 *  com.sk89q.worldedit.bukkit.WorldEditPlugin
 *  com.sk89q.worldedit.bukkit.selections.CuboidSelection
 *  com.sk89q.worldedit.bukkit.selections.Polygonal2DSelection
 *  com.sk89q.worldedit.bukkit.selections.Selection
 *  com.sk89q.worldguard.LocalPlayer
 *  com.sk89q.worldguard.bukkit.WorldGuardPlugin
 *  com.sk89q.worldguard.protection.ApplicableRegionSet
 *  com.sk89q.worldguard.protection.association.RegionAssociable
 *  com.sk89q.worldguard.protection.flags.DefaultFlag
 *  com.sk89q.worldguard.protection.flags.Flag
 *  com.sk89q.worldguard.protection.managers.RegionManager
 *  com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion
 *  com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion
 *  com.sk89q.worldguard.protection.regions.ProtectedRegion
 *  lombok.NonNull
 *  org.bukkit.Location
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.legacy;

import com.google.common.collect.Iterators;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Polygonal2DSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.NonNull;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.IWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.handler.IHandler;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.IWorldGuardImplementation;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.legacy.flag.AbstractWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.legacy.region.WrappedRegion;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.legacy.utility.WorldGuardFlagUtilities;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.legacy.utility.WorldGuardVectorUtilities;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegion;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegionSet;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.ICuboidSelection;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.IPolygonalSelection;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.ISelection;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldGuardImplementation
implements IWorldGuardImplementation {
    private final WorldGuardPlugin worldGuardPlugin = WorldGuardPlugin.inst();
    private final WorldEditPlugin worldEditPlugin;

    public WorldGuardImplementation() {
        try {
            this.worldEditPlugin = this.worldGuardPlugin.getWorldEdit();
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<LocalPlayer> wrapPlayer(OfflinePlayer player) {
        return Optional.ofNullable(player).map(bukkitPlayer -> bukkitPlayer.isOnline() ? this.worldGuardPlugin.wrapPlayer((Player)bukkitPlayer) : this.worldGuardPlugin.wrapOfflinePlayer(bukkitPlayer));
    }

    private Optional<RegionManager> getWorldManager(@NonNull World world) {
        if (world == null) {
            throw new NullPointerException("world is marked non-null but is null");
        }
        return Optional.ofNullable(this.worldGuardPlugin.getRegionManager(world));
    }

    private Optional<ApplicableRegionSet> getApplicableRegions(@NonNull Location location) {
        if (location == null) {
            throw new NullPointerException("location is marked non-null but is null");
        }
        return this.getWorldManager(Objects.requireNonNull(location.getWorld())).map(manager -> manager.getApplicableRegions(location));
    }

    private Optional<ApplicableRegionSet> getApplicableRegions(@NonNull Location minimum, @NonNull Location maximum) {
        if (minimum == null) {
            throw new NullPointerException("minimum is marked non-null but is null");
        }
        if (maximum == null) {
            throw new NullPointerException("maximum is marked non-null but is null");
        }
        return this.getWorldManager(Objects.requireNonNull(minimum.getWorld())).map(manager -> manager.getApplicableRegions((ProtectedRegion)new ProtectedCuboidRegion("temp", WorldGuardVectorUtilities.toBlockVector(minimum), WorldGuardVectorUtilities.toBlockVector(maximum))));
    }

    private <V> Optional<V> queryValue(Player player, @NonNull Location location, @NonNull Flag<V> flag) {
        if (location == null) {
            throw new NullPointerException("location is marked non-null but is null");
        }
        if (flag == null) {
            throw new NullPointerException("flag is marked non-null but is null");
        }
        return this.getApplicableRegions(location).map(applicableRegions -> applicableRegions.queryValue((RegionAssociable)this.wrapPlayer((OfflinePlayer)player).orElse(null), flag));
    }

    private IWrappedRegionSet wrapRegionSet(final @NonNull World world, final @NonNull ApplicableRegionSet regionSet) {
        if (world == null) {
            throw new NullPointerException("world is marked non-null but is null");
        }
        if (regionSet == null) {
            throw new NullPointerException("regionSet is marked non-null but is null");
        }
        return new IWrappedRegionSet(){

            @Override
            public Iterator<IWrappedRegion> iterator() {
                return Iterators.transform(regionSet.iterator(), region -> new WrappedRegion(world, (ProtectedRegion)region));
            }

            @Override
            public boolean isVirtual() {
                return regionSet.isVirtual();
            }

            @Override
            public <V> Optional<V> queryValue(OfflinePlayer subject, IWrappedFlag<V> flag) {
                LocalPlayer subjectHandle = WorldGuardImplementation.this.wrapPlayer(subject).orElse(null);
                AbstractWrappedFlag wrappedFlag = (AbstractWrappedFlag)flag;
                return Optional.ofNullable(regionSet.queryValue((RegionAssociable)subjectHandle, wrappedFlag.getHandle())).flatMap(wrappedFlag::fromWGValue);
            }

            @Override
            public <V> Collection<V> queryAllValues(OfflinePlayer subject, IWrappedFlag<V> flag) {
                LocalPlayer subjectHandle = WorldGuardImplementation.this.wrapPlayer(subject).orElse(null);
                AbstractWrappedFlag wrappedFlag = (AbstractWrappedFlag)flag;
                return regionSet.queryAllValues((RegionAssociable)subjectHandle, wrappedFlag.getHandle()).stream().map(wrappedFlag::fromWGValue).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
            }

            @Override
            public boolean isOwnerOfAll(OfflinePlayer player) {
                LocalPlayer playerHandle = WorldGuardImplementation.this.wrapPlayer(player).orElse(null);
                return regionSet.isOwnerOfAll(playerHandle);
            }

            @Override
            public boolean isMemberOfAll(OfflinePlayer player) {
                LocalPlayer playerHandle = WorldGuardImplementation.this.wrapPlayer(player).orElse(null);
                return regionSet.isMemberOfAll(playerHandle);
            }

            @Override
            public int size() {
                return regionSet.size();
            }

            @Override
            public Set<IWrappedRegion> getRegions() {
                return regionSet.getRegions().stream().map(region -> new WrappedRegion(world, (ProtectedRegion)region)).collect(Collectors.toSet());
            }
        };
    }

    @Override
    public JavaPlugin getWorldGuardPlugin() {
        return WorldGuardPlugin.inst();
    }

    @Override
    public int getApiVersion() {
        return -6;
    }

    @Override
    public void registerHandler(Supplier<IHandler> factory) {
        throw new UnsupportedOperationException("Custom flag handlers aren't supported in this version of WorldGuard!");
    }

    @Override
    public <T> Optional<IWrappedFlag<T>> getFlag(String name, Class<T> type) {
        for (Flag currentFlag : DefaultFlag.getFlags()) {
            if (!currentFlag.getName().equalsIgnoreCase(name)) continue;
            return Optional.of(WorldGuardFlagUtilities.wrap(currentFlag, type));
        }
        return Optional.empty();
    }

    @Override
    public <T> Optional<T> queryFlag(Player player, @NonNull Location location, @NonNull IWrappedFlag<T> flag) {
        if (location == null) {
            throw new NullPointerException("location is marked non-null but is null");
        }
        if (flag == null) {
            throw new NullPointerException("flag is marked non-null but is null");
        }
        AbstractWrappedFlag wrappedFlag = (AbstractWrappedFlag)flag;
        return this.queryValue(player, location, wrappedFlag.getHandle()).flatMap(wrappedFlag::fromWGValue);
    }

    @Override
    public Map<IWrappedFlag<?>, Object> queryApplicableFlags(Player player, Location location) {
        ApplicableRegionSet applicableSet = this.getApplicableRegions(location).orElse(null);
        if (applicableSet == null) {
            return Collections.emptyMap();
        }
        LocalPlayer localPlayer = this.wrapPlayer((OfflinePlayer)player).orElse(null);
        HashMap flags = new HashMap();
        HashSet<String> seen = new HashSet<String>();
        for (ProtectedRegion region : applicableSet.getRegions()) {
            for (Flag flag : region.getFlags().keySet()) {
                Object value;
                if (!seen.add(flag.getName()) || (value = applicableSet.queryValue((RegionAssociable)localPlayer, flag)) == null) continue;
                try {
                    Map.Entry<IWrappedFlag<?>, Object> wrapped = WorldGuardFlagUtilities.wrap(flag, value);
                    flags.put(wrapped.getKey(), wrapped.getValue());
                } catch (IllegalArgumentException illegalArgumentException) {}
            }
        }
        return flags;
    }

    @Override
    public <T> Optional<IWrappedFlag<T>> registerFlag(@NonNull String name, @NonNull Class<T> type, T defaultValue) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        throw new UnsupportedOperationException("Custom flags aren't supported in this version of WorldGuard!");
    }

    @Override
    public Optional<IWrappedRegion> getRegion(@NonNull World world, @NonNull String id) {
        if (world == null) {
            throw new NullPointerException("world is marked non-null but is null");
        }
        if (id == null) {
            throw new NullPointerException("id is marked non-null but is null");
        }
        return this.getWorldManager(world).map(regionManager -> regionManager.getRegion(id)).map(region -> new WrappedRegion(world, (ProtectedRegion)region));
    }

    @Override
    public Map<String, IWrappedRegion> getRegions(@NonNull World world) {
        if (world == null) {
            throw new NullPointerException("world is marked non-null but is null");
        }
        RegionManager regionManager = this.worldGuardPlugin.getRegionManager(world);
        Map regions = regionManager.getRegions();
        HashMap<String, IWrappedRegion> map = new HashMap<String, IWrappedRegion>();
        regions.forEach((name, region) -> map.put((String)name, new WrappedRegion(world, (ProtectedRegion)region)));
        return map;
    }

    @Override
    public Set<IWrappedRegion> getRegions(@NonNull Location location) {
        if (location == null) {
            throw new NullPointerException("location is marked non-null but is null");
        }
        ApplicableRegionSet regionSet = this.getApplicableRegions(location).orElse(null);
        HashSet<IWrappedRegion> set = new HashSet<IWrappedRegion>();
        if (regionSet == null) {
            return set;
        }
        regionSet.forEach(region -> set.add(new WrappedRegion(location.getWorld(), (ProtectedRegion)region)));
        return set;
    }

    @Override
    public Set<IWrappedRegion> getRegions(@NonNull Location minimum, @NonNull Location maximum) {
        if (minimum == null) {
            throw new NullPointerException("minimum is marked non-null but is null");
        }
        if (maximum == null) {
            throw new NullPointerException("maximum is marked non-null but is null");
        }
        ApplicableRegionSet regionSet = this.getApplicableRegions(minimum, maximum).orElse(null);
        HashSet<IWrappedRegion> set = new HashSet<IWrappedRegion>();
        if (regionSet == null) {
            return set;
        }
        regionSet.forEach(region -> set.add(new WrappedRegion(minimum.getWorld(), (ProtectedRegion)region)));
        return set;
    }

    @Override
    public Optional<IWrappedRegionSet> getRegionSet(@NonNull Location location) {
        if (location == null) {
            throw new NullPointerException("location is marked non-null but is null");
        }
        return this.getApplicableRegions(location).map(regionSet -> this.wrapRegionSet(Objects.requireNonNull(location.getWorld()), (ApplicableRegionSet)regionSet));
    }

    @Override
    public Optional<IWrappedRegion> addRegion(@NonNull String id, @NonNull List<Location> points, int minY, int maxY) {
        if (id == null) {
            throw new NullPointerException("id is marked non-null but is null");
        }
        if (points == null) {
            throw new NullPointerException("points is marked non-null but is null");
        }
        World world = Objects.requireNonNull(points.get(0).getWorld());
        Object region = points.size() == 2 ? new ProtectedCuboidRegion(id, WorldGuardVectorUtilities.toBlockVector(points.get(0)), WorldGuardVectorUtilities.toBlockVector(points.get(1))) : new ProtectedPolygonalRegion(id, WorldGuardVectorUtilities.toBlockVector2DList(points), minY, maxY);
        Optional<RegionManager> manager = this.getWorldManager(world);
        if (manager.isPresent()) {
            manager.get().addRegion((ProtectedRegion)region);
            return Optional.of(new WrappedRegion(world, (ProtectedRegion)region));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Set<IWrappedRegion>> removeRegion(@NonNull World world, @NonNull String id) {
        if (world == null) {
            throw new NullPointerException("world is marked non-null but is null");
        }
        if (id == null) {
            throw new NullPointerException("id is marked non-null but is null");
        }
        Optional<Set> set = this.getWorldManager(world).map(manager -> manager.removeRegion(id));
        return set.map(protectedRegions -> protectedRegions.stream().map(region -> new WrappedRegion(world, (ProtectedRegion)region)).collect(Collectors.toSet()));
    }

    @Override
    public Optional<ISelection> getPlayerSelection(@NonNull Player player) {
        if (player == null) {
            throw new NullPointerException("player is marked non-null but is null");
        }
        return Optional.ofNullable(this.worldEditPlugin.getSelection(player)).map(selection -> {
            if (selection instanceof CuboidSelection) {
                return new ICuboidSelection(){
                    final /* synthetic */ Selection val$selection;
                    {
                        this.val$selection = selection;
                    }

                    @Override
                    public Location getMinimumPoint() {
                        return this.val$selection.getMinimumPoint();
                    }

                    @Override
                    public Location getMaximumPoint() {
                        return this.val$selection.getMaximumPoint();
                    }
                };
            }
            if (selection instanceof Polygonal2DSelection) {
                return new IPolygonalSelection(){
                    final /* synthetic */ Selection val$selection;
                    {
                        this.val$selection = selection;
                    }

                    @Override
                    public Set<Location> getPoints() {
                        return ((Polygonal2DSelection)this.val$selection).getNativePoints().stream().map(vector -> new BlockVector(vector.toVector())).map(vector -> WorldGuardVectorUtilities.fromBlockVector(this.val$selection.getWorld(), vector)).collect(Collectors.toSet());
                    }

                    @Override
                    public int getMinimumY() {
                        return this.val$selection.getMinimumPoint().getBlockY();
                    }

                    @Override
                    public int getMaximumY() {
                        return this.val$selection.getMaximumPoint().getBlockY();
                    }
                };
            }
            throw new UnsupportedOperationException("Unsupported " + selection.getClass().getSimpleName() + " selection!");
        });
    }
}

