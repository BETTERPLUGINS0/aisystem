package com.bergerkiller.bukkit.tc.attachments.api;

import com.bergerkiller.bukkit.common.collections.StringMapCaseInsensitive;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentEmpty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AttachmentTypeRegistry {
   private final Map<String, AttachmentType> _types = new StringMapCaseInsensitive();
   private static final AttachmentTypeRegistry _instance = new AttachmentTypeRegistry();

   public static AttachmentTypeRegistry instance() {
      return _instance;
   }

   public synchronized List<AttachmentType> all() {
      ArrayList<AttachmentType> result = new ArrayList(this._types.values());
      Collections.sort(result, (t1, t2) -> {
         int comp = Double.compare(t1.getSortPriority(), t2.getSortPriority());
         return comp != 0 ? comp : t1.getName().compareTo(t2.getName());
      });
      return result;
   }

   public synchronized AttachmentType fromConfig(ConfigurationNode config) {
      return this.find((String)config.get("type", "EMPTY"));
   }

   public void toConfig(ConfigurationNode config, AttachmentType type) {
      config.set("type", type.getID());
   }

   public void toDefaultConfig(ConfigurationNode config, AttachmentType type) {
      this.toConfig(config, type);
      type.getDefaultConfig(config);
   }

   public synchronized AttachmentType find(String id) {
      return (AttachmentType)this._types.get(id);
   }

   public synchronized AttachmentType findOrEmpty(String id) {
      return (AttachmentType)this._types.getOrDefault(id, CartAttachmentEmpty.TYPE);
   }

   public synchronized void register(AttachmentType type) {
      this._types.put(type.getID(), type);
      type.onRegister(this);
   }

   public synchronized void unregister(AttachmentType type) {
      AttachmentType removed = (AttachmentType)this._types.remove(type.getID());
      if (removed != null) {
         if (removed != type) {
            this._types.put(removed.getID(), removed);
         } else {
            removed.onUnregister(this);
         }
      }

   }

   public synchronized void unregisterAll() {
      List<AttachmentType> removed = new ArrayList(this._types.values());
      this._types.clear();
      Iterator var2 = removed.iterator();

      while(var2.hasNext()) {
         AttachmentType removedType = (AttachmentType)var2.next();
         removedType.onUnregister(this);
      }

   }
}
