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
package me.zombie_striker.qav.hooks.worldguard.implementation.legacy;

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
import me.zombie_striker.qav.hooks.worldguard.flag.IWrappedFlag;
import me.zombie_striker.qav.hooks.worldguard.handler.IHandler;
import me.zombie_striker.qav.hooks.worldguard.implementation.IWorldGuardImplementation;
import me.zombie_striker.qav.hooks.worldguard.implementation.legacy.flag.AbstractWrappedFlag;
import me.zombie_striker.qav.hooks.worldguard.implementation.legacy.region.WrappedRegion;
import me.zombie_striker.qav.hooks.worldguard.implementation.legacy.utility.WorldGuardFlagUtilities;
import me.zombie_striker.qav.hooks.worldguard.implementation.legacy.utility.WorldGuardVectorUtilities;
import me.zombie_striker.qav.hooks.worldguard.region.IWrappedRegion;
import me.zombie_striker.qav.hooks.worldguard.region.IWrappedRegionSet;
import me.zombie_striker.qav.hooks.worldguard.selection.ICuboidSelection;
import me.zombie_striker.qav.hooks.worldguard.selection.IPolygonalSelection;
import me.zombie_striker.qav.hooks.worldguard.selection.ISelection;
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
        } catch (CommandException commandException) {
            throw new RuntimeException(commandException);
        }
    }

    private Optional<LocalPlayer> wrapPlayer(OfflinePlayer offlinePlayer2) {
        return Optional.ofNullable(offlinePlayer2).map(offlinePlayer -> offlinePlayer.isOnline() ? this.worldGuardPlugin.wrapPlayer((Player)offlinePlayer) : this.worldGuardPlugin.wrapOfflinePlayer(offlinePlayer));
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
        return this.getWorldManager(Objects.requireNonNull(location.getWorld())).map(regionManager -> regionManager.getApplicableRegions(location));
    }

    private Optional<ApplicableRegionSet> getApplicableRegions(@NonNull Location location, @NonNull Location location2) {
        if (location == null) {
            throw new NullPointerException("minimum is marked non-null but is null");
        }
        if (location2 == null) {
            throw new NullPointerException("maximum is marked non-null but is null");
        }
        return this.getWorldManager(Objects.requireNonNull(location.getWorld())).map(regionManager -> regionManager.getApplicableRegions((ProtectedRegion)new ProtectedCuboidRegion("temp", WorldGuardVectorUtilities.toBlockVector(location), WorldGuardVectorUtilities.toBlockVector(location2))));
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

    private IWrappedRegionSet wrapRegionSet(final @NonNull World world, final @NonNull ApplicableRegionSet applicableRegionSet) {
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
        return -6;
    }

    @Override
    public void registerHandler(Supplier<IHandler> supplier) {
        throw new UnsupportedOperationException("Custom flag handlers aren't supported in this version of WorldGuard!");
    }

    @Override
    public <T> Optional<IWrappedFlag<T>> getFlag(String string, Class<T> clazz) {
        for (Flag flag : DefaultFlag.getFlags()) {
            if (!flag.getName().equalsIgnoreCase(string)) continue;
            return Optional.of(WorldGuardFlagUtilities.wrap(flag, clazz));
        }
        return Optional.empty();
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
        if (string == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        if (clazz == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        throw new UnsupportedOperationException("Custom flags aren't supported in this version of WorldGuard!");
    }

    @Override
    public Optional<IWrappedRegion> getRegion(@NonNull World world, @NonNull String string) {
        if (world == null) {
            throw new NullPointerException("world is marked non-null but is null");
        }
        if (string == null) {
            throw new NullPointerException("id is marked non-null but is null");
        }
        return this.getWorldManager(world).map(regionManager -> regionManager.getRegion(string)).map(protectedRegion -> new WrappedRegion(world, (ProtectedRegion)protectedRegion));
    }

    @Override
    public Map<String, IWrappedRegion> getRegions(@NonNull World world) {
        if (world == null) {
            throw new NullPointerException("world is marked non-null but is null");
        }
        RegionManager regionManager = this.worldGuardPlugin.getRegionManager(world);
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
        HashSet<IWrappedRegion> hashSet = new HashSet<IWrappedRegion>();
        if (applicableRegionSet == null) {
            return hashSet;
        }
        applicableRegionSet.forEach(protectedRegion -> hashSet.add(new WrappedRegion(location.getWorld(), (ProtectedRegion)protectedRegion)));
        return hashSet;
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
        HashSet<IWrappedRegion> hashSet = new HashSet<IWrappedRegion>();
        if (applicableRegionSet == null) {
            return hashSet;
        }
        applicableRegionSet.forEach(protectedRegion -> hashSet.add(new WrappedRegion(location.getWorld(), (ProtectedRegion)protectedRegion)));
        return hashSet;
    }

    @Override
    public Optional<IWrappedRegionSet> getRegionSet(@NonNull Location location) {
        if (location == null) {
            throw new NullPointerException("location is marked non-null but is null");
        }
        return this.getApplicableRegions(location).map(applicableRegionSet -> this.wrapRegionSet(Objects.requireNonNull(location.getWorld()), (ApplicableRegionSet)applicableRegionSet));
    }

    @Override
    public Optional<IWrappedRegion> addRegion(@NonNull String string, @NonNull List<Location> list, int n, int n2) {
        if (string == null) {
            throw new NullPointerException("id is marked non-null but is null");
        }
        if (list == null) {
            throw new NullPointerException("points is marked non-null but is null");
        }
        World world = Objects.requireNonNull(list.get(0).getWorld());
        Object object = list.size() == 2 ? new ProtectedCuboidRegion(string, WorldGuardVectorUtilities.toBlockVector(list.get(0)), WorldGuardVectorUtilities.toBlockVector(list.get(1))) : new ProtectedPolygonalRegion(string, WorldGuardVectorUtilities.toBlockVector2DList(list), n, n2);
        Optional<RegionManager> optional = this.getWorldManager(world);
        if (optional.isPresent()) {
            optional.get().addRegion((ProtectedRegion)object);
            return Optional.of(new WrappedRegion(world, (ProtectedRegion)object));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Set<IWrappedRegion>> removeRegion(@NonNull World world, @NonNull String string) {
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
                        return ((Polygonal2DSelection)this.val$selection).getNativePoints().stream().map(blockVector2D -> new BlockVector(blockVector2D.toVector())).map(blockVector -> WorldGuardVectorUtilities.fromBlockVector(this.val$selection.getWorld(), blockVector)).collect(Collectors.toSet());
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

