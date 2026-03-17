package com.bergerkiller.bukkit.tc.properties;

import com.bergerkiller.bukkit.tc.properties.api.PropertyParseResult;

public interface IParsable {
   /** @deprecated */
   @Deprecated
   default boolean parseSet(String key, String args) {
      return this.parseAndSet(key, args).isSuccessful();
   }

   PropertyParseResult<?> parseAndSet(String var1, String var2);
}
