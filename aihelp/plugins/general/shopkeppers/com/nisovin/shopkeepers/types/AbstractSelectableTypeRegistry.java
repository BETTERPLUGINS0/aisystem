package com.nisovin.shopkeepers.types;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.types.SelectableTypeRegistry;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractSelectableTypeRegistry<T extends AbstractSelectableType> extends AbstractTypeRegistry<T> implements SelectableTypeRegistry<T> {
   private final Map<String, AbstractSelectableTypeRegistry.Link<T>> links = new HashMap();
   @Nullable
   private T first = null;
   @Nullable
   private T last = null;
   protected final Map<String, T> selections = new HashMap();

   protected AbstractSelectableTypeRegistry() {
   }

   public void register(@NonNull T type) {
      super.register((AbstractType)type);
      if (this.first == null) {
         this.first = type;
      }

      AbstractSelectableTypeRegistry.Link<T> link = new AbstractSelectableTypeRegistry.Link();
      this.links.put(type.getIdentifier(), link);
      if (this.last != null) {
         AbstractSelectableTypeRegistry.Link<T> lastLink = (AbstractSelectableTypeRegistry.Link)Unsafe.assertNonNull((AbstractSelectableTypeRegistry.Link)this.links.get(this.last.getIdentifier()));
         lastLink.next = type;
         link.prev = this.last;
      }

      this.last = type;
   }

   @Nullable
   protected T getFirst() {
      return this.first;
   }

   @Nullable
   protected T getLast() {
      return this.last;
   }

   @Nullable
   protected T getNext(@Nullable T current) {
      AbstractSelectableTypeRegistry.Link<T> link = current != null ? (AbstractSelectableTypeRegistry.Link)this.links.get(current.getIdentifier()) : null;
      if (link == null) {
         return this.first;
      } else {
         return link.next == null ? this.first : (AbstractSelectableType)link.next;
      }
   }

   @Nullable
   protected T getPrevious(@Nullable T current) {
      AbstractSelectableTypeRegistry.Link<T> link = current != null ? (AbstractSelectableTypeRegistry.Link)this.links.get(current.getIdentifier()) : null;
      if (link == null) {
         return this.first;
      } else {
         return link.prev == null ? this.last : (AbstractSelectableType)link.prev;
      }
   }

   public boolean canBeSelected(Player player, @NonNull T type) {
      assert player != null && type != null;

      return type.isEnabled() && type.hasPermission(player);
   }

   protected T getNext(Player player, T current) {
      assert player != null;

      T next = current;

      int count;
      for(count = this.getRegisteredTypes().size(); count > 0; --count) {
         next = (AbstractSelectableType)Unsafe.assertNonNull(this.getNext(next));
         if (this.canBeSelected(player, next)) {
            break;
         }
      }

      if (count == 0) {
         if (current != null && !this.canBeSelected(player, current)) {
            next = null;
         } else {
            next = current;
         }
      }

      return next;
   }

   protected T getPrevious(Player player, T current) {
      assert player != null;

      T previous = current;

      int count;
      for(count = this.getRegisteredTypes().size(); count > 0; --count) {
         previous = (AbstractSelectableType)Unsafe.assertNonNull(this.getPrevious(previous));
         if (this.canBeSelected(player, previous)) {
            break;
         }
      }

      if (count == 0) {
         if (current != null && !this.canBeSelected(player, current)) {
            previous = null;
         } else {
            previous = current;
         }
      }

      return previous;
   }

   @Nullable
   public T getDefaultSelection(Player player) {
      return this.getNext(player, (AbstractSelectableType)null);
   }

   public T getSelection(Player player) {
      Validate.notNull(player, (String)"player is null");
      String playerName = (String)Unsafe.assertNonNull(player.getName());
      T current = (AbstractSelectableType)this.selections.get(playerName);
      if (current == null || !this.canBeSelected(player, current)) {
         current = this.getNext(player, current);
      }

      return current;
   }

   public T selectNext(Player player) {
      Validate.notNull(player, (String)"player is null");
      String playerName = (String)Unsafe.assertNonNull(player.getName());
      T current = (AbstractSelectableType)this.selections.get(playerName);
      T next = this.getNext(player, current);
      if (next != null) {
         this.selections.put(playerName, next);
         this.onSelect(next, player);
      }

      return next;
   }

   public T selectPrevious(Player player) {
      Validate.notNull(player, (String)"player is null");
      String playerName = (String)Unsafe.assertNonNull(player.getName());
      T current = (AbstractSelectableType)this.selections.get(playerName);
      T prev = this.getPrevious(player, current);
      if (prev != null) {
         this.selections.put(playerName, prev);
         this.onSelect(prev, player);
      }

      return prev;
   }

   protected void onSelect(@NonNull T type, Player selectedBy) {
      type.onSelect(selectedBy);
   }

   public void clearSelection(Player player) {
      Validate.notNull(player, (String)"player is null");
      String playerName = (String)Unsafe.assertNonNull(player.getName());
      this.selections.remove(playerName);
   }

   public void clearAllSelections() {
      this.selections.clear();
   }

   private static class Link<T> {
      @Nullable
      private T prev = null;
      @Nullable
      private T next = null;
   }
}
