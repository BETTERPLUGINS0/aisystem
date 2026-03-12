package me.SuperRonanCraft.BetterRTP.versions;

import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.lib.folialib.FoliaLib;
import me.SuperRonanCraft.BetterRTP.lib.folialib.impl.ServerImplementation;

public class FoliaHandler {
   private ServerImplementation SERVER_IMPLEMENTATION;

   public void load() {
      this.SERVER_IMPLEMENTATION = (new FoliaLib(BetterRTP.getInstance())).getImpl();
   }

   public ServerImplementation get() {
      return this.SERVER_IMPLEMENTATION;
   }
}
