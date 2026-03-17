package com.bergerkiller.bukkit.tc.attachments.ui.models.listing;

import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class ListedEntry implements Comparable<ListedEntry> {
   private ListedEntry parent = null;
   private List<ListedEntry> children = Collections.emptyList();
   protected boolean childrenNeedSorting = true;
   protected int nestedItemCount = 0;

   public abstract CommonItemStack createIconItem(DialogBuilder var1);

   public abstract String name();

   public abstract String nameLowerCase();

   public abstract String fullPath();

   public abstract int sortPriority();

   public abstract ListedNamespace namespace();

   public final ListedEntry parent() {
      return this.parent;
   }

   public final int nestedItemCount() {
      return this.nestedItemCount;
   }

   public final List<ListedEntry> children() {
      if (this.childrenNeedSorting) {
         this.childrenNeedSorting = false;
         if (!this.children.isEmpty()) {
            this.children.sort((a, b) -> {
               return Integer.compare(a.nestedItemCount, b.nestedItemCount);
            });
         }
      }

      return this.children;
   }

   public List<ListedItemModel> explode() {
      List<ListedItemModel> items = new ArrayList(this.nestedItemCount);
      this.fillItems(items);
      Collections.sort(items);
      return items;
   }

   protected void fillItems(List<ListedItemModel> items) {
      Iterator var2 = this.children().iterator();

      while(var2.hasNext()) {
         ListedEntry child = (ListedEntry)var2.next();
         child.fillItems(items);
      }

   }

   public final List<ListedEntry> matchWithPathPrefix(Iterable<String> pathParts) {
      return this.matchAgainstPath(pathParts, false);
   }

   public final Optional<ListedEntry> findAtPath(Iterable<String> pathParts) {
      List<ListedEntry> result = this.matchAgainstPath(pathParts, true);
      return result.isEmpty() ? Optional.empty() : Optional.of((ListedEntry)result.get(0));
   }

   private final List<ListedEntry> matchAgainstPath(Iterable<String> pathParts, boolean exact) {
      Iterator<String> iter = pathParts.iterator();
      if (!iter.hasNext()) {
         return Collections.singletonList(this);
      } else {
         ListedEntry curr = this;

         while(true) {
            String token = (String)iter.next();
            Optional r;
            if (!iter.hasNext()) {
               if (exact) {
                  r = curr.findChildByName(token);
                  return (List)r.map(Collections::singletonList).orElse(Collections.emptyList());
               }

               String tokenLowerCase = token.toLowerCase(Locale.ENGLISH);
               List<ListedEntry> result = new ArrayList(3);
               Iterator var8 = curr.children().iterator();

               while(var8.hasNext()) {
                  ListedEntry e = (ListedEntry)var8.next();
                  if (e.nameLowerCase().startsWith(tokenLowerCase)) {
                     result.add(e);
                  }
               }

               return result;
            }

            r = curr.findChildByName(token);
            if (!r.isPresent()) {
               return Collections.emptyList();
            }

            curr = (ListedEntry)r.get();
         }
      }
   }

   public final Optional<ListedEntry> findChildByName(String name) {
      String nameLowerCase = name.toLowerCase(Locale.ENGLISH);
      Optional<ListedEntry> result = Optional.empty();
      Iterator var4 = this.children().iterator();

      while(var4.hasNext()) {
         ListedEntry e = (ListedEntry)var4.next();
         if (e.nameLowerCase().equals(nameLowerCase)) {
            result = Optional.of(e);
            if (!(e instanceof ListedItemModel)) {
               break;
            }
         }
      }

      return result;
   }

   public final List<ListedEntry> matchChildrenNameContains(String token) {
      String tokenLower = token.toLowerCase(Locale.ENGLISH);
      ArrayList<ListedEntry> result = new ArrayList(10);
      Iterator var4 = this.children().iterator();

      while(var4.hasNext()) {
         ListedEntry child = (ListedEntry)var4.next();
         child.fillMatchingContains(tokenLower, result);
      }

      return result;
   }

   private void fillMatchingContains(String tokenLower, List<ListedEntry> result) {
      if (this.nameLowerCase().contains(tokenLower)) {
         result.add(this);
      } else {
         Iterator var3 = this.children().iterator();

         while(var3.hasNext()) {
            ListedEntry child = (ListedEntry)var3.next();
            child.fillMatchingContains(tokenLower, result);
         }
      }

   }

   public final ListedEntry compactIf(boolean condition) {
      return condition ? this.compact() : this;
   }

   public final ListedEntry compact() {
      ListedEntry e;
      for(e = this; e.children().size() == 1; e = (ListedEntry)e.children().get(0)) {
      }

      return e;
   }

   public final List<? extends ListedEntry> displayedItems(int numDisplayed) {
      return this.displayedItems(numDisplayed, true);
   }

   public final List<? extends ListedEntry> displayedItems(int numDisplayed, boolean compact) {
      if (!compact) {
         return this.children();
      } else {
         int numChildren = this.children().size();
         if (numChildren >= numDisplayed) {
            return (List)this.children().stream().map(ListedEntry::compact).sorted().collect(Collectors.toList());
         } else if (this.nestedItemCount <= numDisplayed) {
            return this.explode();
         } else {
            int spaceRemaining = numDisplayed - numChildren;
            List<ListedEntry> entries = new ArrayList(numDisplayed);
            Iterator var6 = this.children().iterator();

            while(true) {
               while(var6.hasNext()) {
                  ListedEntry child = (ListedEntry)var6.next();
                  ListedEntry e = child.compact();
                  if (e.nestedItemCount > 1 && e.nestedItemCount - 1 <= spaceRemaining) {
                     spaceRemaining -= e.nestedItemCount - 1;
                     entries.addAll(e.explode());
                  } else {
                     entries.add(e);
                  }
               }

               Collections.sort(entries);
               return entries;
            }
         }
      }
   }

   public int compareTo(ListedEntry o) {
      int sortOrder = Integer.compare(this.sortPriority(), o.sortPriority());
      return sortOrder != 0 ? sortOrder : this.name().compareTo(o.name());
   }

   protected void setParent(ListedEntry parent) {
      if (this.parent != parent) {
         if (this.parent != null) {
            this.parent.children.remove(this);
            this.parent.updateNestedItemCount(-this.nestedItemCount);
         }

         this.parent = parent;
         if (parent.children.isEmpty()) {
            parent.children = new ArrayList();
         }

         parent.children.add(this);
         parent.childrenNeedSorting = true;
         parent.updateNestedItemCount(this.nestedItemCount);
      }

   }

   protected void updateNestedItemCount(int increase) {
      for(ListedEntry e = this; e != null; e = e.parent) {
         e.nestedItemCount += increase;
      }

   }

   protected final ListedEntry assignCloneTo(ListedEntry newParent) {
      ListedEntry clone = this.unsafeClone(newParent);
      clone.parent = null;
      clone.setParent(newParent);
      return clone;
   }

   private final ListedEntry unsafeClone(ListedEntry newParent) {
      ListedEntry clone = this.cloneSelf(newParent == null ? null : newParent.namespace());
      clone.parent = newParent;
      clone.nestedItemCount = this.nestedItemCount;
      List<ListedEntry> selfChildren = this.children();
      if (!selfChildren.isEmpty()) {
         clone.children = new ArrayList(selfChildren.size());
         Iterator var4 = selfChildren.iterator();

         while(var4.hasNext()) {
            ListedEntry child = (ListedEntry)var4.next();
            clone.children.add(child.unsafeClone(clone));
         }

         clone.childrenNeedSorting = false;
      }

      return clone;
   }

   protected abstract ListedEntry findOrCreateInRoot(ListedRoot var1);

   protected final ListedEntry assignToRoot(ListedRoot root) {
      ListedEntry parent = this.parent().findOrCreateInRoot(root);
      return this.assignCloneTo(parent);
   }

   protected abstract ListedEntry cloneSelf(ListedNamespace var1);

   public static List<String> tokenizePath(String path) {
      if (path.isEmpty()) {
         return new ArrayList();
      } else {
         int firstPartEnd = StringUtil.firstIndexOf(path, new char[]{'/', '\\', ':'});
         if (path.charAt(firstPartEnd) == ':' && path.length() >= firstPartEnd) {
            path = path.substring(0, firstPartEnd + 1) + "/" + path.substring(firstPartEnd + 1);
         }

         return (List)Arrays.stream(path.split("/|\\\\")).filter((s) -> {
            return !s.isEmpty();
         }).collect(Collectors.toCollection(ArrayList::new));
      }
   }
}
