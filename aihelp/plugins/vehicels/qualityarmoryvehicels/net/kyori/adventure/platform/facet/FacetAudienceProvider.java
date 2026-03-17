/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.platform.facet;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.permission.PermissionChecker;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.facet.FacetAudience;
import net.kyori.adventure.platform.facet.FacetPointers;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import net.kyori.adventure.util.TriState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public abstract class FacetAudienceProvider<V, A extends FacetAudience<V>>
implements AudienceProvider,
ForwardingAudience {
    protected static final Locale DEFAULT_LOCALE = Locale.US;
    protected final ComponentRenderer<Pointered> componentRenderer;
    private final Audience console;
    private final Audience player;
    protected final Map<V, A> viewers;
    private final Map<UUID, A> players;
    private final Set<A> consoles;
    private A empty;
    private volatile boolean closed;

    protected FacetAudienceProvider(@NotNull ComponentRenderer<Pointered> componentRenderer) {
        this.componentRenderer = Objects.requireNonNull(componentRenderer, "component renderer");
        this.viewers = new ConcurrentHashMap<V, A>();
        this.players = new ConcurrentHashMap<UUID, A>();
        this.consoles = new CopyOnWriteArraySet<A>();
        this.console = new ForwardingAudience(){

            @Override
            @NotNull
            public Iterable<? extends Audience> audiences() {
                return FacetAudienceProvider.this.consoles;
            }

            @Override
            @NotNull
            public Pointers pointers() {
                if (FacetAudienceProvider.this.consoles.size() == 1) {
                    return ((FacetAudience)FacetAudienceProvider.this.consoles.iterator().next()).pointers();
                }
                return Pointers.empty();
            }
        };
        this.player = Audience.audience(this.players.values());
        this.closed = false;
    }

    public void addViewer(@NotNull V v) {
        if (this.closed) {
            return;
        }
        FacetAudience facetAudience = this.viewers.computeIfAbsent((FacetAudience)Objects.requireNonNull(v, "viewer"), (Function<FacetAudience, A>)((Function<Object, FacetAudience>)object -> this.createAudience(Collections.singletonList(object))));
        FacetPointers.Type type = facetAudience.getOrDefault(FacetPointers.TYPE, FacetPointers.Type.OTHER);
        if (type == FacetPointers.Type.PLAYER) {
            @Nullable UUID uUID = facetAudience.getOrDefault(Identity.UUID, null);
            if (uUID != null) {
                this.players.putIfAbsent(uUID, facetAudience);
            }
        } else if (type == FacetPointers.Type.CONSOLE) {
            this.consoles.add(facetAudience);
        }
    }

    public void removeViewer(@NotNull V v) {
        FacetAudience facetAudience = (FacetAudience)this.viewers.remove(v);
        if (facetAudience == null) {
            return;
        }
        FacetPointers.Type type = facetAudience.getOrDefault(FacetPointers.TYPE, FacetPointers.Type.OTHER);
        if (type == FacetPointers.Type.PLAYER) {
            @Nullable UUID uUID = facetAudience.getOrDefault(Identity.UUID, null);
            if (uUID != null) {
                this.players.remove(uUID);
            }
        } else if (type == FacetPointers.Type.CONSOLE) {
            this.consoles.remove(facetAudience);
        }
        facetAudience.close();
    }

    public void refreshViewer(@NotNull V v) {
        FacetAudience facetAudience = (FacetAudience)this.viewers.get(v);
        if (facetAudience != null) {
            facetAudience.refresh();
        }
    }

    @NotNull
    protected abstract A createAudience(@NotNull Collection<V> var1);

    @Override
    @NotNull
    public Iterable<? extends Audience> audiences() {
        return this.viewers.values();
    }

    @Override
    @NotNull
    public Audience all() {
        return this;
    }

    @Override
    @NotNull
    public Audience console() {
        return this.console;
    }

    @Override
    @NotNull
    public Audience players() {
        return this.player;
    }

    @Override
    @NotNull
    public Audience player(@NotNull UUID uUID) {
        return (Audience)this.players.getOrDefault(uUID, this.empty());
    }

    @NotNull
    private A empty() {
        if (this.empty == null) {
            this.empty = this.createAudience(Collections.emptyList());
        }
        return this.empty;
    }

    @NotNull
    public Audience filter(@NotNull Predicate<V> predicate) {
        return Audience.audience(FacetAudienceProvider.filter(this.viewers.entrySet(), entry -> predicate.test(entry.getKey()), Map.Entry::getValue));
    }

    @NotNull
    private Audience filterPointers(@NotNull Predicate<Pointered> predicate) {
        return Audience.audience(FacetAudienceProvider.filter(this.viewers.entrySet(), entry -> predicate.test((Pointered)entry.getValue()), Map.Entry::getValue));
    }

    @Override
    @NotNull
    public Audience permission(@NotNull String string) {
        return this.filterPointers(pointered -> pointered.get(PermissionChecker.POINTER).orElse(PermissionChecker.always(TriState.FALSE)).test(string));
    }

    @Override
    @NotNull
    public Audience world(@NotNull Key key) {
        return this.filterPointers(pointered -> key.equals(pointered.getOrDefault(FacetPointers.WORLD, null)));
    }

    @Override
    @NotNull
    public Audience server(@NotNull String string) {
        return this.filterPointers(pointered -> string.equals(pointered.getOrDefault(FacetPointers.SERVER, null)));
    }

    @Override
    public void close() {
        this.closed = true;
        for (V v : this.viewers.keySet()) {
            this.removeViewer(v);
        }
    }

    @NotNull
    private static <T, V> Iterable<V> filter(final @NotNull Iterable<T> iterable, final @NotNull Predicate<T> predicate, final @NotNull Function<T, V> function) {
        return new Iterable<V>(){

            @Override
            @NotNull
            public Iterator<V> iterator() {
                return new Iterator<V>(){
                    private final Iterator<T> parent;
                    private V next;
                    {
                        this.parent = iterable.iterator();
                        this.populate();
                    }

                    private void populate() {
                        this.next = null;
                        while (this.parent.hasNext()) {
                            Object t = this.parent.next();
                            if (!predicate.test(t)) continue;
                            this.next = function.apply(t);
                            return;
                        }
                    }

                    @Override
                    public boolean hasNext() {
                        return this.next != null;
                    }

                    @Override
                    public V next() {
                        if (this.next == null) {
                            throw new NoSuchElementException();
                        }
                        Object v = this.next;
                        this.populate();
                        return v;
                    }
                };
            }

            @Override
            public void forEach(Consumer<? super V> consumer) {
                for (Object t : iterable) {
                    if (!predicate.test(t)) continue;
                    consumer.accept(function.apply(t));
                }
            }
        };
    }
}

