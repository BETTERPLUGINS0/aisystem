/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.NonNull
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import lombok.NonNull;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.IWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.handler.IHandler;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegion;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegionSet;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.ICuboidSelection;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.IPolygonalSelection;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.ISelection;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public interface IWorldGuardImplementation {
    public JavaPlugin getWorldGuardPlugin();

    public int getApiVersion();

    public void registerHandler(Supplier<IHandler> var1);

    public <T> Optional<T> queryFlag(Player var1, @NonNull Location var2, @NonNull IWrappedFlag<T> var3);

    public Map<IWrappedFlag<?>, Object> queryApplicableFlags(Player var1, Location var2);

    public <T> Optional<IWrappedFlag<T>> getFlag(String var1, Class<T> var2);

    public <T> Optional<IWrappedFlag<T>> registerFlag(@NonNull String var1, @NonNull Class<T> var2, T var3);

    default public <T> Optional<IWrappedFlag<T>> registerFlag(@NonNull String name, @NonNull Class<T> type) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return this.registerFlag(name, type, null);
    }

    public Optional<IWrappedRegion> getRegion(@NonNull World var1, @NonNull String var2);

    public Map<String, IWrappedRegion> getRegions(@NonNull World var1);

    public Set<IWrappedRegion> getRegions(@NonNull Location var1);

    public Set<IWrappedRegion> getRegions(@NonNull Location var1, @NonNull Location var2);

    public Optional<IWrappedRegionSet> getRegionSet(@NonNull Location var1);

    public Optional<IWrappedRegion> addRegion(@NonNull String var1, @NonNull List<Location> var2, int var3, int var4);

    default public Optional<IWrappedRegion> addCuboidRegion(@NonNull String id, @NonNull Location point1, @NonNull Location point2) {
        if (id == null) {
            throw new NullPointerException("id is marked non-null but is null");
        }
        if (point1 == null) {
            throw new NullPointerException("point1 is marked non-null but is null");
        }
        if (point2 == null) {
            throw new NullPointerException("point2 is marked non-null but is null");
        }
        return this.addRegion(id, Arrays.asList(point1, point2), 0, 0);
    }

    default public Optional<IWrappedRegion> addRegion(@NonNull String id, @NonNull ISelection selection) {
        if (id == null) {
            throw new NullPointerException("id is marked non-null but is null");
        }
        if (selection == null) {
            throw new NullPointerException("selection is marked non-null but is null");
        }
        if (selection instanceof ICuboidSelection) {
            ICuboidSelection sel = (ICuboidSelection)selection;
            return this.addCuboidRegion(id, sel.getMinimumPoint(), sel.getMaximumPoint());
        }
        if (selection instanceof IPolygonalSelection) {
            IPolygonalSelection sel = (IPolygonalSelection)selection;
            return this.addRegion(id, new ArrayList<Location>(sel.getPoints()), sel.getMinimumY(), sel.getMaximumY());
        }
        throw new UnsupportedOperationException("Unknown " + selection.getClass().getSimpleName() + " selection type!");
    }

    public Optional<Set<IWrappedRegion>> removeRegion(@NonNull World var1, @NonNull String var2);

    public Optional<ISelection> getPlayerSelection(@NonNull Player var1);
}

