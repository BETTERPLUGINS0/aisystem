/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.IncompleteRegionException
 *  com.sk89q.worldedit.WorldEdit
 *  com.sk89q.worldedit.bukkit.BukkitAdapter
 *  com.sk89q.worldedit.bukkit.BukkitWorld
 *  com.sk89q.worldedit.math.BlockVector2
 *  com.sk89q.worldedit.math.BlockVector3
 *  com.sk89q.worldedit.regions.CuboidRegion
 *  com.sk89q.worldedit.regions.Polygonal2DRegion
 *  com.sk89q.worldedit.regions.Region
 *  com.sk89q.worldedit.session.SessionOwner
 *  com.sk89q.worldedit.world.World
 *  com.sk89q.worldguard.LocalPlayer
 *  com.sk89q.worldguard.WorldGuard
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
package me.zombie_striker.qav.hooks.worldguard.implementation.v7;

import com.google.common.collect.Iterators;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.SessionOwner;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
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
import me.zombie_striker.qav.hooks.worldguard.flag.IWrappedFlag;
import me.zombie_striker.qav.hooks.worldguard.flag.WrappedState;
import me.zombie_striker.qav.hooks.worldguard.handler.IHandler;
import me.zombie_striker.qav.hooks.worldguard.implementation.IWorldGuardImplementation;
import me.zombie_striker.qav.hooks.worldguard.implementation.v7.flag.AbstractWrappedFlag;
import me.zombie_striker.qav.hooks.worldguard.implementation.v7.handler.ProxyHandler;
import me.zombie_striker.qav.hooks.worldguard.implementation.v7.region.WrappedRegion;
import me.zombie_striker.qav.hooks.worldguard.implementation.v7.utility.WorldGuardFlagUtilities;
import me.zombie_striker.qav.hooks.worldguard.region.IWrappedRegion;
import me.zombie_striker.qav.hooks.worldguard.region.IWrappedRegionSet;
import me.zombie_striker.qav.hooks.worldguard.selection.ICuboidSelection;
import me.zombie_striker.qav.hooks.worldguard.selection.IPolygonalSelection;
import me.zombie_striker.qav.hooks.worldguard.selection.ISelection;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.ProxyFactory;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class WorldGuardImplementation
implements IWorldGuardImplementation {
    private final WorldGuard core = WorldGuard.getInstance();
    private final FlagRegistry flagRegistry = this.core.getFlagRegistry();

    private Optional<LocalPlayer> wrapPlayer(OfflinePlayer offlinePlayer2) {
        return Optional.ofNullable(offlinePlayer2).map(offlinePlayer -> {
            if (offlinePlayer.isOnline()) {
                return WorldGuardPlugin.inst().wrapPlayer((Player)offlinePlayer);
            }
            return WorldGuardPlugin.inst().wrapOfflinePlayer(offlinePlayer);
        });
    }

    private Optional<RegionManager> getWorldManager(@NonNull org.bukkit.World world) {
        if (world == null) {
            throw new NullPointerException("world is marked non-null but is null");
        }
        return Optional.ofNullable(this.core.getPlatform().getRegionContainer().get(BukkitAdapter.adapt((org.bukkit.World)world)));
    }

    private Optional<ApplicableRegionSet> getApplicableRegions(@NonNull Location location) {
        if (location == null) {
            throw new NullPointerException("location is marked non-null but is null");
        }
        return this.getWorldManager(Objects.requireNonNull(location.getWorld())).map(regionManager -> regionManager.getApplicableRegions(BukkitAdapter.asBlockVector((Location)location)));
    }

    private Optional<ApplicableRegionSet> getApplicableRegions(@NonNull Location location, @NonNull Location location2) {
        if (location == null) {
            throw new NullPointerException("minimum is marked non-null but is null");
        }
        if (location2 == null) {
            throw new NullPointerException("maximum is marked non-null but is null");
        }
        return this.getWorldManager(Objects.requireNonNull(location.getWorld())).map(regionManager -> regionManager.getApplicableRegions((ProtectedRegion)new ProtectedCuboidRegion("temp", BukkitAdapter.asBlockVector((Location)location), BukkitAdapter.asBlockVector((Location)location2))));
    }

    private <V> Optional<V> queryValue(Player player, @NonNull Location location, @NonNull Flag<V> flag) {
        if (location == null) {
            throw new NullPointerException("location is marked non-null but is null");
        }
        if (flag == null) {
            throw new NullPointerException("flag is marked non-null but is null");
        }
        return this.getApplicableRegions(location).map(applicableRegionSet -> applicableRegionSet.queryValue((RegionAssociable)this.wrapPlayer((OfflinePlayer)player).orElse(null), flag));
    }

    public IWrappedRegionSet wrapRegionSet(final @NonNull org.bukkit.World world, final @NonNull ApplicableRegionSet applicableRegionSet) {
        if (world == null) {
            throw new NullPointerException("world is marked non-null but is null");
        }
        if (applicableRegionSet == null) {
            throw new NullPointerException("regionSet is marked non-null but is null");
        }
        return new IWrappedRegionSet(){

            @Override
            public Iterator<IWrappedRegion> iterator() {
                return Iterators.transform(applicableRegionSet.iterator(), protectedRegion -> new WrappedRegion(world, (ProtectedRegion)protectedRegion));
            }

            @Override
            public boolean isVirtual() {
                return applicableRegionSet.isVirtual();
            }

            @Override
            public <V> Optional<V> queryValue(OfflinePlayer offlinePlayer, IWrappedFlag<V> iWrappedFlag) {
                LocalPlayer localPlayer = WorldGuardImplementation.this.wrapPlayer(offlinePlayer).orElse(null);
                AbstractWrappedFlag abstractWrappedFlag = (AbstractWrappedFlag)iWrappedFlag;
                return Optional.ofNullable(applicableRegionSet.queryValue((RegionAssociable)localPlayer, abstractWrappedFlag.getHandle())).flatMap(abstractWrappedFlag::fromWGValue);
            }

            @Override
            public <V> Collection<V> queryAllValues(OfflinePlayer offlinePlayer, IWrappedFlag<V> iWrappedFlag) {
                LocalPlayer localPlayer = WorldGuardImplementation.this.wrapPlayer(offlinePlayer).orElse(null);
                AbstractWrappedFlag abstractWrappedFlag = (AbstractWrappedFlag)iWrappedFlag;
                return applicableRegionSet.queryAllValues((RegionAssociable)localPlayer, abstractWrappedFlag.getHandle()).stream().map(abstractWrappedFlag::fromWGValue).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
            }

            @Override
            public boolean isOwnerOfAll(OfflinePlayer offlinePlayer) {
                LocalPlayer localPlayer = WorldGuardImplementation.this.wrapPlayer(offlinePlayer).orElse(null);
                return applicableRegionSet.isOwnerOfAll(localPlayer);
            }

            @Override
            public boolean isMemberOfAll(OfflinePlayer offlinePlayer) {
                LocalPlayer localPlayer = WorldGuardImplementation.this.wrapPlayer(offlinePlayer).orElse(null);
                return applicableRegionSet.isMemberOfAll(localPlayer);
            }

            @Override
            public int size() {
                return applicableRegionSet.size();
            }

            @Override
            public Set<IWrappedRegion> getRegions() {
                return applicableRegionSet.getRegions().stream().map(protectedRegion -> new WrappedRegion(world, (ProtectedRegion)protectedRegion)).collect(Collectors.toSet());
            }
        };
    }

    @Override
    public JavaPlugin getWorldGuardPlugin() {
        return WorldGuardPlugin.inst();
    }

    @Override
    public int getApiVersion() {
        return 7;
    }

    @Override
    public void registerHandler(final Supplier<IHandler> supplier) {
        Constructor<?> constructor;
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setUseCache(false);
        proxyFactory.setSuperclass(ProxyHandler.class);
        try {
            Class<?> clazz = proxyFactory.createClass();
            constructor = clazz.getDeclaredConstructor(WorldGuardImplementation.class, IHandler.class, Session.class);
        } catch (NoSuchMethodException noSuchMethodException) {
            throw new RuntimeException(noSuchMethodException);
        }
        this.core.getPlatform().getSessionManager().registerHandler((Handler.Factory)new Handler.Factory<Handler>(){

            public Handler create(Session session) {
                IHandler iHandler = (IHandler)supplier.get();
                try {
                    return (Handler)constructor.newInstance(WorldGuardImplementation.this, iHandler, session);
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException reflectiveOperationException) {
                    throw new RuntimeException(reflectiveOperationException);
                }
            }
        }, null);
    }

    @Override
    public <T> Optional<IWrappedFlag<T>> getFlag(String string, Class<T> clazz) {
        return Optional.ofNullable(this.flagRegistry.get(string)).map(flag -> WorldGuardFlagUtilities.wrap(flag, clazz));
    }

    @Override
    public <T> Optional<T> queryFlag(Player player, @NonNull Location location, @NonNull IWrappedFlag<T> iWrappedFlag) {
        if (location == null) {
            throw new NullPointerException("location is marked non-null but is null");
        }
        if (iWrappedFlag == null) {
            throw new NullPointerException("flag is marked non-null but is null");
        }
        AbstractWrappedFlag abstractWrappedFlag = (AbstractWrappedFlag)iWrappedFlag;
        return this.queryValue(player, location, abstractWrappedFlag.getHandle()).flatMap(abstractWrappedFlag::fromWGValue);
    }

    @Override
    public Map<IWrappedFlag<?>, Object> queryApplicableFlags(Player player, Location location) {
        ApplicableRegionSet applicableRegionSet = this.getApplicableRegions(location).orElse(null);
        if (applicableRegionSet == null) {
            return Collections.emptyMap();
        }
        LocalPlayer localPlayer = this.wrapPlayer((OfflinePlayer)player).orElse(null);
        HashMap hashMap = new HashMap();
        HashSet<String> hashSet = new HashSet<String>();
        for (ProtectedRegion protectedRegion : applicableRegionSet.getRegions()) {
            for (Flag flag : protectedRegion.getFlags().keySet()) {
                Object object;
                if (!hashSet.add(flag.getName()) || (object = applicableRegionSet.queryValue((RegionAssociable)localPlayer, flag)) == null) continue;
                try {
                    Map.Entry<IWrappedFlag<?>, Object> entry = WorldGuardFlagUtilities.wrap(flag, object);
                    hashMap.put(entry.getKey(), entry.getValue());
                } catch (IllegalArgumentException illegalArgumentException) {}
            }
        }
        return hashMap;
    }

    @Override
    public <T> Optional<IWrappedFlag<T>> registerFlag(@NonNull String string, @NonNull Class<T> clazz, T t) {
        StateFlag stateFlag;
        if (string == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        if (clazz == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (clazz.equals(WrappedState.class)) {
            stateFlag = new StateFlag(string, t == WrappedState.ALLOW);
        } else if (clazz.equals(Boolean.class) || clazz.equals(Boolean.TYPE)) {
            stateFlag = new BooleanFlag(string);
        } else if (clazz.equals(Double.class) || clazz.equals(Double.TYPE)) {
            stateFlag = new DoubleFlag(string);
        } else if (clazz.equals(Enum.class)) {
            stateFlag = new EnumFlag(string, clazz);
        } else if (clazz.equals(Integer.class) || clazz.equals(Integer.TYPE)) {
            stateFlag = new IntegerFlag(string);
        } else if (clazz.equals(Location.class)) {
            stateFlag = new LocationFlag(string);
        } else if (clazz.equals(String.class)) {
            stateFlag = new StringFlag(string, (String)t);
        } else if (clazz.equals(Vector.class)) {
            stateFlag = new VectorFlag(string);
        } else {
            throw new IllegalArgumentException("Unsupported flag type " + clazz.getName());
        }
        try {
            this.flagRegistry.register((Flag)stateFlag);
            return Optional.of(WorldGuardFlagUtilities.wrap(stateFlag, clazz));
        } catch (FlagConflictException flagConflictException) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<IWrappedRegion> getRegion(@NonNull org.bukkit.World world, @NonNull String string) {
        if (world == null) {
            throw new NullPointerException("world is marked non-null but is null");
        }
        if (string == null) {
            throw new NullPointerException("id is marked non-null but is null");
        }
        return this.getWorldManager(world).map(regionManager -> regionManager.getRegion(string)).map(protectedRegion -> new WrappedRegion(world, (ProtectedRegion)protectedRegion));
    }

    @Override
    public Map<String, IWrappedRegion> getRegions(@NonNull org.bukkit.World world) {
        if (world == null) {
            throw new NullPointerException("world is marked non-null but is null");
        }
        RegionManager regionManager = this.core.getPlatform().getRegionContainer().get((World)new BukkitWorld(world));
        if (regionManager == null) {
            return Collections.emptyMap();
        }
        Map map = regionManager.getRegions();
        HashMap<String, IWrappedRegion> hashMap = new HashMap<String, IWrappedRegion>();
        map.forEach((string, protectedRegion) -> hashMap.put((String)string, new WrappedRegion(world, (ProtectedRegion)protectedRegion)));
        return hashMap;
    }

    @Override
    public Set<IWrappedRegion> getRegions(@NonNull Location location) {
        if (location == null) {
            throw new NullPointerException("location is marked non-null but is null");
        }
        ApplicableRegionSet applicableRegionSet = this.getApplicableRegions(location).orElse(null);
        if (applicableRegionSet == null) {
            return Collections.emptySet();
        }
        return applicableRegionSet.getRegions().stream().map(protectedRegion -> new WrappedRegion(location.getWorld(), (ProtectedRegion)protectedRegion)).collect(Collectors.toSet());
    }

    @Override
    public Set<IWrappedRegion> getRegions(@NonNull Location location, @NonNull Location location2) {
        if (location == null) {
            throw new NullPointerException("minimum is marked non-null but is null");
        }
        if (location2 == null) {
            throw new NullPointerException("maximum is marked non-null but is null");
        }
        ApplicableRegionSet applicableRegionSet = this.getApplicableRegions(location, location2).orElse(null);
        if (applicableRegionSet == null) {
            return Collections.emptySet();
        }
        return applicableRegionSet.getRegions().stream().map(protectedRegion -> new WrappedRegion(location.getWorld(), (ProtectedRegion)protectedRegion)).collect(Collectors.toSet());
    }

    @Override
    public Optional<IWrappedRegionSet> getRegionSet(@NonNull Location location) {
        if (location == null) {
            throw new NullPointerException("location is marked non-null but is null");
        }
        return Optional.empty();
    }

    @Override
    public Optional<IWrappedRegion> addRegion(@NonNull String string, @NonNull List<Location> list, int n, int n2) {
        Object object;
        ProtectedCuboidRegion protectedCuboidRegion;
        if (string == null) {
            throw new NullPointerException("id is marked non-null but is null");
        }
        if (list == null) {
            throw new NullPointerException("points is marked non-null but is null");
        }
        org.bukkit.World world = Objects.requireNonNull(list.get(0).getWorld());
        if (list.size() == 2) {
            protectedCuboidRegion = new ProtectedCuboidRegion(string, BukkitAdapter.asBlockVector((Location)list.get(0)), BukkitAdapter.asBlockVector((Location)list.get(1)));
        } else {
            object = list.stream().map(location -> BukkitAdapter.asBlockVector((Location)location).toBlockVector2()).collect(Collectors.toList());
            protectedCuboidRegion = new ProtectedPolygonalRegion(string, object, n, n2);
        }
        object = this.getWorldManager(world);
        if (((Optional)object).isPresent()) {
            ((RegionManager)((Optional)object).get()).addRegion((ProtectedRegion)protectedCuboidRegion);
            return Optional.of(new WrappedRegion(world, (ProtectedRegion)protectedCuboidRegion));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Set<IWrappedRegion>> removeRegion(@NonNull org.bukkit.World world, @NonNull String string) {
        if (world == null) {
            throw new NullPointerException("world is marked non-null but is null");
        }
        if (string == null) {
            throw new NullPointerException("id is marked non-null but is null");
        }
        Optional<Set> optional = this.getWorldManager(world).map(regionManager -> regionManager.removeRegion(string));
        return optional.map(set -> set.stream().map(protectedRegion -> new WrappedRegion(world, (ProtectedRegion)protectedRegion)).collect(Collectors.toSet()));
    }

    @Override
    public Optional<ISelection> getPlayerSelection(@NonNull Player player) {
        Region region2;
        if (player == null) {
            throw new NullPointerException("player is marked non-null but is null");
        }
        try {
            region2 = WorldEdit.getInstance().getSessionManager().get((SessionOwner)BukkitAdapter.adapt((Player)player)).getSelection(BukkitAdapter.adapt((org.bukkit.World)player.getWorld()));
        } catch (IncompleteRegionException incompleteRegionException) {
            region2 = null;
        }
        return Optional.ofNullable(region2).map(region -> {
            final org.bukkit.World world = Optional.ofNullable(region.getWorld()).map(BukkitAdapter::adapt).orElse(null);
            if (world == null) {
                return null;
            }
            if (region instanceof CuboidRegion) {
                return new ICuboidSelection(){
                    final /* synthetic */ Region val$selection;
                    {
                        this.val$selection = region;
                    }

                    @Override
                    public Location getMinimumPoint() {
                        return BukkitAdapter.adapt((org.bukkit.World)world, (BlockVector3)this.val$selection.getMinimumPoint());
                    }

                    @Override
                    public Location getMaximumPoint() {
                        return BukkitAdapter.adapt((org.bukkit.World)world, (BlockVector3)this.val$selection.getMaximumPoint());
                    }
                };
            }
            if (region instanceof Polygonal2DRegion) {
                return new IPolygonalSelection(){
                    final /* synthetic */ Region val$selection;
                    final /* synthetic */ org.bukkit.World val$world;
                    {
                        this.val$selection = region;
                        this.val$world = world;
                    }

                    @Override
                    public Set<Location> getPoints() {
                        return ((Polygonal2DRegion)this.val$selection).getPoints().stream().map(BlockVector2::toBlockVector3).map(blockVector3 -> BukkitAdapter.adapt((org.bukkit.World)this.val$world, (BlockVector3)blockVector3)).collect(Collectors.toSet());
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
            throw new UnsupportedOperationException("Unsupported " + region.getClass().getSimpleName() + " selection!");
        });
    }
}

