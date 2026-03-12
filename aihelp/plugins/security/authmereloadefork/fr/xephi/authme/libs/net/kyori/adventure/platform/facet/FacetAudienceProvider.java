package fr.xephi.authme.libs.net.kyori.adventure.platform.facet;

import fr.xephi.authme.libs.net.kyori.adventure.audience.Audience;
import fr.xephi.authme.libs.net.kyori.adventure.audience.ForwardingAudience;
import fr.xephi.authme.libs.net.kyori.adventure.identity.Identity;
import fr.xephi.authme.libs.net.kyori.adventure.key.Key;
import fr.xephi.authme.libs.net.kyori.adventure.permission.PermissionChecker;
import fr.xephi.authme.libs.net.kyori.adventure.platform.AudienceProvider;
import fr.xephi.authme.libs.net.kyori.adventure.pointer.Pointered;
import fr.xephi.authme.libs.net.kyori.adventure.pointer.Pointers;
import fr.xephi.authme.libs.net.kyori.adventure.text.renderer.ComponentRenderer;
import fr.xephi.authme.libs.net.kyori.adventure.util.TriState;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public abstract class FacetAudienceProvider<V, A extends FacetAudience<V>> implements AudienceProvider, ForwardingAudience {
   protected static final Locale DEFAULT_LOCALE;
   protected final ComponentRenderer<Pointered> componentRenderer;
   private final Audience console;
   private final Audience player;
   protected final Map<V, A> viewers;
   private final Map<UUID, A> players;
   private final Set<A> consoles;
   private A empty;
   private volatile boolean closed;

   protected FacetAudienceProvider(@NotNull final ComponentRenderer<Pointered> componentRenderer) {
      this.componentRenderer = (ComponentRenderer)Objects.requireNonNull(componentRenderer, "component renderer");
      this.viewers = new ConcurrentHashMap();
      this.players = new ConcurrentHashMap();
      this.consoles = new CopyOnWriteArraySet();
      this.console = new ForwardingAudience() {
         @NotNull
         public Iterable<? extends Audience> audiences() {
            return FacetAudienceProvider.this.consoles;
         }

         @NotNull
         public Pointers pointers() {
            return FacetAudienceProvider.this.consoles.size() == 1 ? ((FacetAudience)FacetAudienceProvider.this.consoles.iterator().next()).pointers() : Pointers.empty();
         }
      };
      this.player = Audience.audience((Iterable)this.players.values());
      this.closed = false;
   }

   public void addViewer(@NotNull final V viewer) {
      if (!this.closed) {
         A audience = (FacetAudience)this.viewers.computeIfAbsent(Objects.requireNonNull(viewer, "viewer"), (v) -> {
            return this.createAudience(Collections.singletonList(v));
         });
         FacetPointers.Type type = (FacetPointers.Type)audience.getOrDefault(FacetPointers.TYPE, FacetPointers.Type.OTHER);
         if (type == FacetPointers.Type.PLAYER) {
            UUID id = (UUID)audience.getOrDefault(Identity.UUID, (Object)null);
            if (id != null) {
               this.players.putIfAbsent(id, audience);
            }
         } else if (type == FacetPointers.Type.CONSOLE) {
            this.consoles.add(audience);
         }

      }
   }

   public void removeViewer(@NotNull final V viewer) {
      A audience = (FacetAudience)this.viewers.remove(viewer);
      if (audience != null) {
         FacetPointers.Type type = (FacetPointers.Type)audience.getOrDefault(FacetPointers.TYPE, FacetPointers.Type.OTHER);
         if (type == FacetPointers.Type.PLAYER) {
            UUID id = (UUID)audience.getOrDefault(Identity.UUID, (Object)null);
            if (id != null) {
               this.players.remove(id);
            }
         } else if (type == FacetPointers.Type.CONSOLE) {
            this.consoles.remove(audience);
         }

         audience.close();
      }
   }

   public void refreshViewer(@NotNull final V viewer) {
      A audience = (FacetAudience)this.viewers.get(viewer);
      if (audience != null) {
         audience.refresh();
      }

   }

   @NotNull
   protected abstract A createAudience(@NotNull final Collection<V> viewers);

   @NotNull
   public Iterable<? extends Audience> audiences() {
      return this.viewers.values();
   }

   @NotNull
   public Audience all() {
      return this;
   }

   @NotNull
   public Audience console() {
      return this.console;
   }

   @NotNull
   public Audience players() {
      return this.player;
   }

   @NotNull
   public Audience player(@NotNull final UUID playerId) {
      return (Audience)this.players.getOrDefault(playerId, this.empty());
   }

   @NotNull
   private A empty() {
      if (this.empty == null) {
         this.empty = this.createAudience(Collections.emptyList());
      }

      return this.empty;
   }

   @NotNull
   public Audience filter(@NotNull final Predicate<V> predicate) {
      return Audience.audience(filter(this.viewers.entrySet(), (entry) -> {
         return predicate.test(entry.getKey());
      }, Entry::getValue));
   }

   @NotNull
   private Audience filterPointers(@NotNull final Predicate<Pointered> predicate) {
      return Audience.audience(filter(this.viewers.entrySet(), (entry) -> {
         return predicate.test((Pointered)entry.getValue());
      }, Entry::getValue));
   }

   @NotNull
   public Audience permission(@NotNull final String permission) {
      return this.filterPointers((pointers) -> {
         return ((PermissionChecker)pointers.get(PermissionChecker.POINTER).orElse(PermissionChecker.always(TriState.FALSE))).test(permission);
      });
   }

   @NotNull
   public Audience world(@NotNull final Key world) {
      return this.filterPointers((pointers) -> {
         return world.equals(pointers.getOrDefault(FacetPointers.WORLD, (Object)null));
      });
   }

   @NotNull
   public Audience server(@NotNull final String serverName) {
      return this.filterPointers((pointers) -> {
         return serverName.equals(pointers.getOrDefault(FacetPointers.SERVER, (Object)null));
      });
   }

   public void close() {
      this.closed = true;
      Iterator var1 = this.viewers.keySet().iterator();

      while(var1.hasNext()) {
         V viewer = var1.next();
         this.removeViewer(viewer);
      }

   }

   @NotNull
   private static <T, V> Iterable<V> filter(@NotNull final Iterable<T> input, @NotNull final Predicate<T> filter, @NotNull final Function<T, V> transformer) {
      return new Iterable<V>() {
         @NotNull
         public Iterator<V> iterator() {
            return new Iterator<V>() {
               private final Iterator<T> parent = input.iterator();
               private V next;

               {
                  this.populate();
               }

               private void populate() {
                  this.next = null;

                  Object next;
                  do {
                     if (!this.parent.hasNext()) {
                        return;
                     }

                     next = this.parent.next();
                  } while(!filter.test(next));

                  this.next = transformer.apply(next);
               }

               public boolean hasNext() {
                  return this.next != null;
               }

               public V next() {
                  if (this.next == null) {
                     throw new NoSuchElementException();
                  } else {
                     V next = this.next;
                     this.populate();
                     return next;
                  }
               }
            };
         }

         public void forEach(final Consumer<? super V> action) {
            Iterator var2 = input.iterator();

            while(var2.hasNext()) {
               T each = var2.next();
               if (filter.test(each)) {
                  action.accept(transformer.apply(each));
               }
            }

         }
      };
   }

   static {
      DEFAULT_LOCALE = Locale.US;
   }
}
