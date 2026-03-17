package com.bergerkiller.bukkit.tc.attachments.ui.models.listing;

import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import java.util.Locale;
import org.bukkit.ChatColor;

public final class ListedDirectory extends ListedEntry {
   private final ListedNamespace namespace;
   private final String path;
   private final String name;
   private final String nameLowerCase;

   public ListedDirectory(ListedNamespace namespace, String path) {
      this.namespace = namespace;
      this.path = path;
      int lastIdx = path.lastIndexOf(47);
      if (lastIdx == -1) {
         this.name = path;
      } else {
         this.name = path.substring(lastIdx + 1);
      }

      this.nameLowerCase = this.name.toLowerCase(Locale.ENGLISH);
   }

   private ListedDirectory(ListedNamespace namespace, ListedDirectory directory) {
      this.namespace = namespace;
      this.path = directory.path;
      this.name = directory.name;
      this.nameLowerCase = directory.nameLowerCase;
   }

   public String name() {
      return this.name;
   }

   public String nameLowerCase() {
      return this.nameLowerCase;
   }

   public String path() {
      return this.path;
   }

   public String fullPath() {
      return this.namespace.fullPath() + this.path;
   }

   public int sortPriority() {
      return 2;
   }

   public ListedNamespace namespace() {
      return this.namespace;
   }

   public CommonItemStack createIconItem(DialogBuilder options) {
      return CommonItemStack.copyOf(options.getDirectoryIconItem()).setCustomNameMessage(ChatColor.YELLOW + this.name).addLoreMessage(ChatColor.WHITE.toString() + ChatColor.ITALIC + this.fullPath()).addLoreLine().addLoreMessage(ChatColor.DARK_GRAY + "Directory").addLoreMessage(ChatColor.DARK_GRAY + "< " + ChatColor.GRAY + this.nestedItemCount + ChatColor.DARK_GRAY + " Item models >");
   }

   public String toString() {
      return "Directory: " + this.path;
   }

   protected ListedDirectory cloneSelf(ListedNamespace namespace) {
      if (namespace == null) {
         throw new IllegalArgumentException("Namespace is required");
      } else {
         ListedDirectory clone = new ListedDirectory(namespace, this);
         clone.namespace.directories.put(clone.path, clone);
         return clone;
      }
   }

   protected ListedDirectory findOrCreateInRoot(ListedRoot root) {
      ListedEntry newParent = this.parent().findOrCreateInRoot(root);
      return newParent.namespace().findOrCreateDirectory(this.path);
   }
}
