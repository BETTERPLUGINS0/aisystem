package com.bergerkiller.bukkit.tc.utils.modularconfiguration;

public class EntryRemovedException extends UnsupportedOperationException {
   public EntryRemovedException() {
      super("Entry was removed from all modules");
   }
}
