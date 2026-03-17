package com.bergerkiller.bukkit.tc.attachments.config;

import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.tc.properties.SavedClaim;
import com.bergerkiller.bukkit.tc.utils.SetCallbackCollector;
import com.bergerkiller.bukkit.tc.utils.modularconfiguration.ModularConfigurationEntry;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.command.CommandSender;

public class SavedAttachmentModel extends AttachmentModel implements SavedAttachmentModelStore.ModelUsing {
   private final ModularConfigurationEntry<SavedAttachmentModel> entry;

   SavedAttachmentModel(ModularConfigurationEntry<SavedAttachmentModel> entry) {
      super(entry.getConfig());
      this.entry = entry;
   }

   public SavedAttachmentModelStore getModule() {
      return this.entry.isRemoved() ? null : SavedAttachmentModelStore.createModule(this.entry.getModule());
   }

   public boolean isNone() {
      return this.entry.isRemoved();
   }

   public String getName() {
      return this.entry.getName();
   }

   public boolean isEmpty() {
      if (this.entry.isRemoved()) {
         return true;
      } else {
         Iterator var1 = this.entry.getConfig().getKeys().iterator();

         String key;
         do {
            if (!var1.hasNext()) {
               return true;
            }

            key = (String)var1.next();
         } while(LogicUtil.contains(key, new String[]{"claims", "editor", "savedName"}));

         return false;
      }
   }

   public Set<SavedClaim> getClaims() {
      return this.entry.isRemoved() ? Collections.emptySet() : SavedClaim.loadClaims(this.entry.getConfig());
   }

   public void setClaims(Collection<SavedClaim> claims) {
      if (!this.entry.isRemoved()) {
         SavedClaim.saveClaims(this.entry.getWritableConfig(), claims);
      }

   }

   public boolean hasPermission(CommandSender sender) {
      return this.entry.isRemoved() || SavedClaim.hasPermission(this.entry.getConfig(), sender);
   }

   public void getUsedModels(SetCallbackCollector<SavedAttachmentModel> collector) {
      if (!this.isNone()) {
         super.getUsedModels(collector);
      }

   }

   public int hashCode() {
      return this.entry.hashCode();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o instanceof SavedAttachmentModel) {
         return ((SavedAttachmentModel)o).entry == this.entry;
      } else {
         return false;
      }
   }
}
