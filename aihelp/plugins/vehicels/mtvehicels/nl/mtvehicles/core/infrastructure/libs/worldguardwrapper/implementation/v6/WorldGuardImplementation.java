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
 *  com.sk89q.worldguard.protection.flags.BooleanFlag
 *  com.sk89q.worldguard.protection.flags.DoubleFlag
 *  com.sk89q.worldguard.protection.flags.EnumFlag
 *  com.sk89q.worldguard.protection.flags.Flag
 *  com.sk89q.worldguard.protection.flags.IntegerFlag
 *  com.sk89q.worldguard.protection.flags.LocationFlag
 *  com.sk89q.worldguard.protection.flags.StateFlag
 *  com.sk89q.worldguard.protection.flags.StringFlag
 *  com.sk89q.worldguard.protection.flags.VectorFlag
 *  com.sk89q.worldguard.protection.flags.registry.FlagConflictException
 *  com.sk89q.worldguard.protection.flags.registry.FlagRegistry
 *  com.sk89q.worldguard.protection.managers.RegionManager
 *  com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion
 *  com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion
 *  com.sk89q.worldguard.protection.regions.ProtectedRegion
 *  com.sk89q.worldguard.session.Session
 *  com.sk89q.worldguard.session.handler.Handler
 *  com.sk89q.worldguard.session.handler.Handler$Factory
 *  lombok.NonNull
 *  org.bukkit.Location
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.util.Vector
 */
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6;

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
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.DoubleFlag;
import com.sk89q.worldguard.protection.flags.EnumFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.IntegerFlag;
import com.sk89q.worldguard.protection.flags.LocationFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.VectorFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.WrappedState;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.handler.IHandler;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.IWorldGuardImplementation;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.flag.AbstractWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.handler.ProxyHandler;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.region.WrappedRegion;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.utility.WorldGuardFlagUtilities;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.utility.WorldGuardVectorUtilities;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegion;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegionSet;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.ICuboidSelection;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.IPolygonalSelection;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.ISelection;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.util.proxy.ProxyFactory;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class WorldGuardImplementation
implements IWorldGuardImplementation {
    private final WorldGuardPlugin worldGuardPlugin = WorldGuardPlugin.inst();
    private final WorldEditPlugin worldEditPlugin;
    private final FlagRegistry flagRegistry;

    public WorldGuardImplementation() {
        try {
            this.worldEditPlugin = this.worldGuardPlugin.getWorldEdit();
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }
        this.flagRegistry = this.worldGuardPlugin.getFlagRegistry();
    }

    private Optional<LocalPlayer> wrapPlayer(OfflinePlayer player) {
        return Optional.ofNullable(player).map(bukkitPlayer -> {
            if (bukkitPlayer.isOnline()) {
                return this.worldGuardPlugin.wrapPlayer((Player)bukkitPlayer);
            }
            return this.worldGuardPlugin.wrapOfflinePlayer(bukkitPlayer);
        });
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

    public IWrappedRegionSet wrapRegionSet(final @NonNull World world, final @NonNull ApplicableRegionSet regionSet) {
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
        return 6;
    }

    @Override
    public void registerHandler(final Supplier<IHandler> factory) {
        Constructor<?> handlerConstructor;
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setUseCache(false);
        proxyFactory.setSuperclass(ProxyHandler.class);
        try {
            Class<?> handlerClass = proxyFactory.createClass();
            handlerConstructor = handlerClass.getDeclaredConstructor(WorldGuardImplementation.class, IHandler.class, Session.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        this.worldGuardPlugin.getSessionManager().registerHandler((Handler.Factory)new Handler.Factory<Handler>(){

            public Handler create(Session session) {
                IHandler handler = (IHandler)factory.get();
                try {
                    return (Handler)handlerConstructor.newInstance(WorldGuardImplementation.this, handler, session);
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }, null);
    }

    @Override
    public <T> Optional<IWrappedFlag<T>> getFlag(String name, Class<T> type) {
        return Optional.ofNullable(this.flagRegistry.get(name)).map(flag -> WorldGuardFlagUtilities.wrap(flag, type));
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
        StateFlag flag;
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (type.equals(WrappedState.class)) {
            flag = new StateFlag(name, defaultValue == WrappedState.ALLOW);
        } else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
            flag = new BooleanFlag(name);
        } else if (type.equals(Double.class) || type.equals(Double.TYPE)) {
            flag = new DoubleFlag(name);
        } else if (type.equals(Enum.class)) {
            flag = new EnumFlag(name, type);
        } else if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
            flag = new IntegerFlag(name);
        } else if (type.equals(Location.class)) {
            flag = new LocationFlag(name);
        } else if (type.equals(String.class)) {
            flag = new StringFlag(name, (String)defaultValue);
        } else if (type.equals(Vector.class)) {
            flag = new VectorFlag(name);
        } else {
            throw new IllegalArgumentException("Unsupported flag type " + type.getName());
        }
        try {
            this.flagRegistry.register((Flag)flag);
            return Optional.of(WorldGuardFlagUtilities.wrap(flag, type));
        } catch (FlagConflictException flagConflictException) {
            return Optional.empty();
        }
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

