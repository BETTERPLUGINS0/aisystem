package com.bergerkiller.bukkit.tc.attachments.ui.models.listing;

import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ListedRoot extends ListedEntry {
   final Map<String, ListedNamespace> namespacesByName;
   final List<ListedItemModel> allListedItems;
   final Map<CommonItemStack, ListedItemModel> allListedBareItemStacks;

   public ListedRoot() {
      this.namespacesByName = new HashMap();
      this.allListedItems = new ArrayList();
      this.allListedBareItemStacks = new LinkedHashMap();
   }

   private ListedRoot(ListedRoot root) {
      this.namespacesByName = new HashMap(root.namespacesByName.size());
      this.allListedItems = new ArrayList(root.allListedItems.size());
      this.allListedBareItemStacks = new LinkedHashMap(root.allListedBareItemStacks.size());
   }

   public String name() {
      return "";
   }

   public String nameLowerCase() {
      return "";
   }

   public String fullPath() {
      return "";
   }

   public int sortPriority() {
      return 0;
   }

   public CommonItemStack createIconItem(DialogBuilder options) {
      return CommonItemStack.empty();
   }

   public ListedNamespace namespace() {
      return null;
   }

   public List<ListedNamespace> namespaces() {
      return this.children();
   }

   public List<ListedItemModel> itemModels() {
      return this.allListedItems;
   }

   public Map<CommonItemStack, ListedItemModel> bareItemStacks() {
      return this.allListedBareItemStacks;
   }

   public String toString() {
      return "<ROOT>";
   }

   public ListedItemModel addListedItem(String path, CommonItemStack item, String credit) {
      int namespaceStart = path.indexOf(58);
      String pathWithoutNamespace;
      String fullPath;
      String namespaceName;
      if (namespaceStart == -1) {
         namespaceName = "minecraft:";
         pathWithoutNamespace = path;
         fullPath = namespaceName + path;
      } else {
         namespaceName = path.substring(0, namespaceStart + 1);
         pathWithoutNamespace = path.substring(namespaceStart + 1);
         fullPath = path;
      }

      ListedNamespace namespace = this.findOrCreateNamespace(namespaceName);
      int directoryPathEnd = pathWithoutNamespace.lastIndexOf(47);
      Object containingEntry;
      String name;
      if (directoryPathEnd == -1) {
         containingEntry = namespace;
         name = pathWithoutNamespace;
      } else {
         String directoryPath = pathWithoutNamespace.substring(0, directoryPathEnd);
         containingEntry = namespace.findOrCreateDirectory(directoryPath);
         name = pathWithoutNamespace.substring(directoryPathEnd + 1);
      }

      ListedItemModel entry = new ListedItemModel(fullPath, pathWithoutNamespace, name, credit, item);
      entry.setParent((ListedEntry)containingEntry);
      this.allListedItems.add(entry);
      this.allListedBareItemStacks.put(entry.bareItem(), entry);
      return entry;
   }

   protected ListedRoot cloneSelf(ListedNamespace namespace) {
      if (namespace != null) {
         throw new IllegalArgumentException("Root entries cannot be in a namespace");
      } else {
         return new ListedRoot(this);
      }
   }

   protected ListedEntry findOrCreateInRoot(ListedRoot root) {
      return root;
   }

   protected ListedNamespace findOrCreateNamespace(String namespace) {
      ListedNamespace entry = (ListedNamespace)this.namespacesByName.computeIfAbsent(namespace, ListedNamespace::new);
      if (entry.parent() == null) {
         entry.setParent(this);
      }

      return entry;
   }
}
