package com.bergerkiller.bukkit.tc.attachments.api;

import com.bergerkiller.bukkit.common.utils.StreamUtil;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.bukkit.tc.commands.selector.SelectorCondition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class AttachmentNameLookup {
   public static final AttachmentNameLookup EMPTY = new AttachmentNameLookup(Collections.emptyList(), Collections.emptyList(), Collections.emptyMap());
   private final List<Attachment> all;
   private final List<Attachment> parents;
   private final Map<String, List<Attachment>> byName;
   private final List<String> names;
   private boolean valid;

   private AttachmentNameLookup(AttachmentNameLookup original) {
      this.valid = true;
      this.all = original.all;
      this.parents = original.parents;
      this.byName = original.byName;
      this.names = original.names;
      this.valid = original.valid;
   }

   private AttachmentNameLookup(List<Attachment> all, List<Attachment> parents, Map<String, List<Attachment>> byName) {
      this.valid = true;
      this.all = all;
      this.parents = parents;
      this.byName = byName;
      this.names = byName.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList(byName.keySet()));
   }

   private static void makeListsImmutable(Map<String, List<Attachment>> attachments) {
      Iterator var1 = attachments.entrySet().iterator();

      while(var1.hasNext()) {
         Entry<String, List<Attachment>> e = (Entry)var1.next();
         e.setValue(Collections.unmodifiableList((List)e.getValue()));
      }

   }

   private static void fill(List<Attachment> all, Map<String, List<Attachment>> attachments, Attachment attachment) {
      Iterator var3 = attachment.getNames().iterator();

      while(var3.hasNext()) {
         String name = (String)var3.next();
         ((List)attachments.computeIfAbsent(name, (n) -> {
            return new ArrayList(4);
         })).add(attachment);
      }

      all.add(attachment);
      var3 = attachment.getChildren().iterator();

      while(var3.hasNext()) {
         Attachment child = (Attachment)var3.next();
         fill(all, attachments, child);
      }

   }

   public boolean isValid() {
      return this.valid;
   }

   public void invalidate() {
      this.valid = false;
   }

   public List<String> names() {
      return this.names;
   }

   public List<String> names(Predicate<Attachment> filter) {
      return (List)this.byName.entrySet().stream().filter((e) -> {
         return containsMatching((List)e.getValue(), filter);
      }).map(Entry::getKey).collect(StreamUtil.toUnmodifiableList());
   }

   public List<Attachment> get(String name) {
      return (List)this.byName.getOrDefault(name, Collections.emptyList());
   }

   public <T extends Attachment> List<T> getOfType(String name, Class<T> type) {
      Objects.requireNonNull(type);
      return this.get(name, type::isInstance);
   }

   public List<Attachment> get(String name, Predicate<Attachment> filter) {
      return Util.filterList(this.get(name), filter);
   }

   public <T extends Attachment> List<T> allOfType(Class<T> type) {
      Objects.requireNonNull(type);
      return this.all(type::isInstance);
   }

   public List<Attachment> all(Predicate<Attachment> filter) {
      return Util.filterList(this.all, filter);
   }

   public List<Attachment> all() {
      return this.all;
   }

   public List<Attachment> parents() {
      return this.parents;
   }

   public List<Attachment> parents(Predicate<Attachment> filter) {
      return (List)this.parents.stream().filter(filter).collect(StreamUtil.toUnmodifiableList());
   }

   public Stream<Entity> matchSeatSelector(CommandSender sender, SelectorCondition condition) {
      List seats;
      if (condition.hasKeyPath()) {
         seats = this.getOfType(condition.getKeyPath(), CartAttachmentSeat.class);
      } else {
         seats = this.allOfType(CartAttachmentSeat.class);
      }

      if (condition.isBoolean()) {
         return seats.stream().map(CartAttachmentSeat::getEntity).filter(Objects::nonNull);
      } else {
         boolean includePlayer = condition.matchesText("@p");
         return seats.stream().map(CartAttachmentSeat::getEntity).filter((e) -> {
            return e instanceof Player;
         }).filter((p) -> {
            return includePlayer && p == sender || condition.matchesText(((Player)p).getName());
         });
      }
   }

   public List<String> selectNames(AttachmentSelector<?> selector, Set<Attachment> excluding) {
      switch(selector.strategy()) {
      case NONE:
         return Collections.emptyList();
      case PARENTS:
         return Util.filterAndMultiMapList(this.parents, (a) -> {
            return selector.matches(a) && !excluding.contains(a);
         }, Attachment::getNames);
      default:
         Predicate<Attachment> filter = (a) -> {
            return selector.matchesExceptName(a) && !excluding.contains(a);
         };
         return selector.nameFilter().isPresent() ? Util.filterAndMultiMapList(this.get((String)selector.nameFilter().get()), filter, Attachment::getNames) : this.names(filter);
      }
   }

   public <T> List<T> selectValues(AttachmentSelector<T> selector, Set<Attachment> excluding) {
      switch(selector.strategy()) {
      case NONE:
         return Collections.emptyList();
      case PARENTS:
         return this.parents((a) -> {
            return selector.matches(a) && !excluding.contains(a);
         });
      default:
         Predicate<Attachment> filter = (a) -> {
            return selector.matchesExceptName(a) && !excluding.contains(a);
         };
         return selector.nameFilter().isPresent() ? this.get((String)selector.nameFilter().get(), filter) : this.all(filter);
      }
   }

   private static boolean containsMatching(List<Attachment> attachments, Predicate<Attachment> filter) {
      Iterator var2 = attachments.iterator();

      Attachment attachment;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         attachment = (Attachment)var2.next();
      } while(!filter.test(attachment));

      return true;
   }

   public static AttachmentNameLookup create(Attachment root) {
      Map<String, List<Attachment>> attachments = new HashMap();
      List<Attachment> all = new ArrayList();
      fill(all, attachments, root);
      makeListsImmutable(attachments);
      Attachment p;
      List parents;
      if ((p = root.getParent()) != null) {
         List<Attachment> parents = new ArrayList();
         parents.add(root);
         parents.add(p);

         while((p = p.getParent()) != null) {
            parents.add(p);
         }

         parents = Collections.unmodifiableList(parents);
      } else {
         parents = Collections.singletonList(root);
      }

      return new AttachmentNameLookup(Collections.unmodifiableList(all), parents, attachments);
   }

   public static AttachmentNameLookup merge(Collection<AttachmentNameLookup> nameLookups) {
      if (nameLookups.isEmpty()) {
         return EMPTY;
      } else if (nameLookups.size() == 1) {
         return (AttachmentNameLookup)nameLookups.iterator().next();
      } else {
         Map<String, List<Attachment>> resultByName = new HashMap(32);
         List<Attachment> resultAll = new ArrayList(64);
         List<Attachment> resultParents = new ArrayList(16);
         Iterator var4 = nameLookups.iterator();

         while(var4.hasNext()) {
            AttachmentNameLookup lookup = (AttachmentNameLookup)var4.next();
            if (!lookup.byName.isEmpty()) {
               Iterator var6 = lookup.byName.entrySet().iterator();

               while(var6.hasNext()) {
                  Entry<String, List<Attachment>> e = (Entry)var6.next();
                  ((List)resultByName.computeIfAbsent((String)e.getKey(), (n) -> {
                     return new ArrayList();
                  })).addAll((Collection)e.getValue());
               }
            }

            resultAll.addAll(lookup.all);
            resultParents.addAll(lookup.parents);
         }

         makeListsImmutable(resultByName);
         return new AttachmentNameLookup.AttachmentNameLookupMerged(Collections.unmodifiableList(resultAll), Collections.unmodifiableList(resultParents), resultByName, nameLookups);
      }
   }

   // $FF: synthetic method
   AttachmentNameLookup(AttachmentNameLookup x0, Object x1) {
      this(x0);
   }

   // $FF: synthetic method
   AttachmentNameLookup(List x0, List x1, Map x2, Object x3) {
      this(x0, x1, x2);
   }

   static {
      EMPTY.invalidate();
   }

   private static class AttachmentNameLookupMerged extends AttachmentNameLookup {
      private final Collection<AttachmentNameLookup> originalLookups;

      private AttachmentNameLookupMerged(AttachmentNameLookup original, Collection<AttachmentNameLookup> originalLookups) {
         super(original, null);
         this.originalLookups = originalLookups;
      }

      private AttachmentNameLookupMerged(List<Attachment> all, List<Attachment> parents, Map<String, List<Attachment>> byName, Collection<AttachmentNameLookup> originalLookups) {
         super(all, parents, byName, null);
         this.originalLookups = originalLookups;
      }

      public boolean isValid() {
         if (!super.isValid()) {
            return false;
         } else {
            Iterator var1 = this.originalLookups.iterator();

            AttachmentNameLookup lookup;
            do {
               if (!var1.hasNext()) {
                  return true;
               }

               lookup = (AttachmentNameLookup)var1.next();
            } while(lookup.isValid());

            this.invalidate();
            return false;
         }
      }

      // $FF: synthetic method
      AttachmentNameLookupMerged(List x0, List x1, Map x2, Collection x3, Object x4) {
         this(x0, x1, x2, x3);
      }
   }

   /** @deprecated */
   @Deprecated
   public static final class NameGroup<T extends Attachment> implements Iterable<T> {
      private static final AttachmentNameLookup.NameGroup<Attachment> NONE;
      private final AttachmentSelection<T> selection;

      public static <T extends Attachment> AttachmentNameLookup.NameGroup<T> of(AttachmentNameLookup.Supplier lookupSupplier, String name, Class<T> type) {
         return new AttachmentNameLookup.NameGroup(lookupSupplier.getSelection(AttachmentSelector.named(AttachmentSelector.SearchStrategy.CHILDREN, name).withType(type)));
      }

      public static <T extends Attachment> AttachmentNameLookup.NameGroup<T> none() {
         return NONE;
      }

      private NameGroup(AttachmentSelection<T> selection) {
         this.selection = selection;
      }

      public List<T> values() {
         return this.selection.values();
      }

      public void sync() {
         this.selection.sync();
      }

      public Iterator<T> iterator() {
         return this.selection.iterator();
      }

      public void forEach(Consumer<? super T> action) {
         this.selection.forEach(action);
      }

      static {
         NONE = new AttachmentNameLookup.NameGroup(AttachmentSelection.NONE);
      }
   }

   private static final class SelectionImpl<T> implements AttachmentSelection<T> {
      private final AttachmentNameLookup.Supplier lookupSupplier;
      private final AttachmentSelector<T> selector;
      private AttachmentNameLookup cachedLookup;
      private Set<Attachment> cachedExcluding;
      private List<T> values = null;
      private List<String> names = null;

      public SelectionImpl(AttachmentNameLookup.Supplier lookupSupplier, AttachmentSelector<T> selector) {
         if (lookupSupplier == null) {
            throw new IllegalArgumentException("Lookup Supplier is null");
         } else if (selector == null) {
            throw new IllegalArgumentException("Attachment Selector is null");
         } else {
            this.lookupSupplier = lookupSupplier;
            this.cachedExcluding = Collections.emptySet();
            this.selector = selector;
            this.cachedLookup = AttachmentNameLookup.EMPTY;
            this.sync();
         }
      }

      public AttachmentSelector<T> selector() {
         return this.selector;
      }

      public List<String> names() {
         List names;
         if ((names = this.names) != null) {
            return names;
         } else {
            synchronized(this) {
               return (names = this.names) != null ? names : (this.names = this.cachedLookup.selectNames(this.selector, this.cachedExcluding));
            }
         }
      }

      public List<T> values() {
         List values;
         if ((values = this.values) != null) {
            return values;
         } else {
            synchronized(this) {
               return (values = this.values) != null ? values : (this.values = this.cachedLookup.selectValues(this.selector, this.cachedExcluding));
            }
         }
      }

      public boolean sync() {
         if (this.cachedLookup.isValid()) {
            return false;
         } else {
            AttachmentNameLookup lookup = this.lookupSupplier.getNameLookup(this.selector.strategy());
            Set<Attachment> excluding = this.selector.isExcludingSelf() ? this.lookupSupplier.getSelfFilterOfNameLookup() : Collections.emptySet();
            synchronized(this) {
               this.cachedLookup = lookup;
               this.cachedExcluding = excluding;
               this.values = null;
               this.names = null;
               return true;
            }
         }
      }
   }

   @FunctionalInterface
   public interface Supplier {
      AttachmentNameLookup getNameLookup();

      default AttachmentNameLookup getNameLookup(AttachmentSelector.SearchStrategy strategy) {
         return this.getNameLookup();
      }

      default Set<Attachment> getSelfFilterOfNameLookup() {
         return Collections.emptySet();
      }

      default <T> AttachmentSelection<T> getSelection(AttachmentSelector<T> selector) {
         return new AttachmentNameLookup.SelectionImpl(this, selector);
      }

      static <T> AttachmentSelection<T> getSelection(final AttachmentSelector<T> selector, final java.util.function.Supplier<Collection<? extends AttachmentNameLookup.Supplier>> suppliers) {
         AttachmentNameLookup.Supplier deferMerged = new AttachmentNameLookup.Supplier() {
            public AttachmentNameLookup getNameLookup() {
               Collection<? extends AttachmentNameLookup.Supplier> currSuppliers = (Collection)suppliers.get();
               if (currSuppliers.isEmpty()) {
                  return AttachmentNameLookup.EMPTY;
               } else if (currSuppliers.size() == 1) {
                  return ((AttachmentNameLookup.Supplier)currSuppliers.iterator().next()).getNameLookup(selector.strategy());
               } else {
                  List<AttachmentNameLookup> lookups = new ArrayList(currSuppliers.size());
                  Iterator var3 = currSuppliers.iterator();

                  while(var3.hasNext()) {
                     AttachmentNameLookup.Supplier supplier = (AttachmentNameLookup.Supplier)var3.next();
                     lookups.add(supplier.getNameLookup(selector.strategy()));
                  }

                  return AttachmentNameLookup.merge(lookups);
               }
            }

            public Set<Attachment> getSelfFilterOfNameLookup() {
               Collection<? extends AttachmentNameLookup.Supplier> currSuppliers = (Collection)suppliers.get();
               if (currSuppliers.isEmpty()) {
                  return Collections.emptySet();
               } else if (currSuppliers.size() == 1) {
                  return ((AttachmentNameLookup.Supplier)currSuppliers.iterator().next()).getSelfFilterOfNameLookup();
               } else {
                  Set<Attachment> excluding = new HashSet();
                  Iterator var3 = currSuppliers.iterator();

                  while(var3.hasNext()) {
                     AttachmentNameLookup.Supplier supplier = (AttachmentNameLookup.Supplier)var3.next();
                     excluding.addAll(supplier.getSelfFilterOfNameLookup());
                  }

                  return Collections.unmodifiableSet(excluding);
               }
            }
         };
         return deferMerged.getSelection(selector);
      }
   }
}
