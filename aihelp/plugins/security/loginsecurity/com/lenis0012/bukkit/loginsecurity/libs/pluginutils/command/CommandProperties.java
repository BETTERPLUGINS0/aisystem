package com.lenis0012.bukkit.loginsecurity.libs.pluginutils.command;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;

public class CommandProperties {
   private final Map<String, Object> properties;

   public CommandProperties(Map<String, Object> properties) {
      this.properties = properties;
   }

   public String getDescription() {
      return (String)this.get("description", (Object)"");
   }

   public List<String> getAliases() {
      Object object = this.properties.get("aliases");
      if (object == null) {
         return Lists.newArrayList();
      } else {
         return (List)(object instanceof List ? (List)object : Lists.newArrayList(new String[]{(String)object}));
      }
   }

   public String getPermission() {
      return (String)this.get("permission", (Class)null);
   }

   public String getPermissionMessage() {
      return (String)this.get("permission-message", (Object)"");
   }

   public String getUsage() {
      return (String)this.get("usage", (Object)"");
   }

   public <T> T get(String key, Class<T> type) {
      return type.cast(this.properties.get(key));
   }

   private <T> T get(String key, T def) {
      return this.properties.containsKey(key) ? this.properties.get(key) : def;
   }
}
