package com.bergerkiller.bukkit.tc.attachments.ui.models.listing;

import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.bukkit.ChatColor;

public final class ListedNamespace extends ListedEntry {
   private final String name;
   private final String nameLowerCase;
   final Map<String, ListedDirectory> directories;

   public ListedNamespace(String namespace) {
      this.name = namespace;
      this.nameLowerCase = namespace.toLowerCase(Locale.ENGLISH);
      this.directories = new HashMap();
   }

   private ListedNamespace(ListedNamespace namespace) {
      this.name = namespace.name;
      this.nameLowerCase = namespace.nameLowerCase;
      this.directories = new HashMap(namespace.directories.size());
   }

   protected ListedDirectory findOrCreateDirectory(String path) {
      ListedDirectory entry = this.initDirectory(path);
      if (entry.parent() == null) {
         ListedDirectory d = entry;

         while(true) {
            int d_path_end = d.path().lastIndexOf(47);
            if (d_path_end == -1) {
               d.setParent(this);
               break;
            }

            String parent_dir_path = d.path().substring(0, d_path_end);
            ListedDirectory dp = this.initDirectory(parent_dir_path);
            d.setParent(dp);
            if (dp.parent() != null) {
               break;
            }

            d = dp;
         }
      }

      return entry;
   }

   private ListedDirectory initDirectory(String path) {
      return (ListedDirectory)this.directories.computeIfAbsent(path, (p) -> {
         return new ListedDirectory(this, p);
      });
   }

   public String name() {
      return this.name;
   }

   public String nameLowerCase() {
      return this.nameLowerCase;
   }

   public String fullPath() {
      return this.name;
   }

   public int sortPriority() {
      return 1;
   }

   public ListedNamespace namespace() {
      return this;
   }

   public CommonItemStack createIconItem(DialogBuilder options) {
      return CommonItemStack.copyOf(options.getNamespaceIconItem()).setCustomNameMessage(ChatColor.YELLOW + this.name).addLoreLine().addLoreMessage(ChatColor.DARK_GRAY + "Namespace").addLoreMessage(ChatColor.DARK_GRAY + "< " + ChatColor.GRAY + this.nestedItemCount + ChatColor.DARK_GRAY + " Item models >");
   }

   public String toString() {
      return "Namespace: " + this.name;
   }

   protected ListedNamespace cloneSelf(ListedNamespace namespace) {
      if (namespace != null) {
         throw new IllegalArgumentException("Namespace entries cannot be in a namespace");
      } else {
         return new ListedNamespace(this);
      }
   }

   protected ListedEntry findOrCreateInRoot(ListedRoot root) {
      return root.findOrCreateNamespace(this.name);
   }
}
