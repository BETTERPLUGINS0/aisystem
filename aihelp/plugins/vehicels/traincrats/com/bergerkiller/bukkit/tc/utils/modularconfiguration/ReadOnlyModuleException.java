package com.bergerkiller.bukkit.tc.utils.modularconfiguration;

public class ReadOnlyModuleException extends UnsupportedOperationException {
   public ReadOnlyModuleException() {
      super("This module is read-only");
   }
}
