package me.SuperRonanCraft.BetterRTP.references.database;

import java.util.logging.Level;
import me.SuperRonanCraft.BetterRTP.BetterRTP;

public class Error {
   public static void execute(BetterRTP plugin, Exception ex) {
      plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
   }

   public static void close(BetterRTP plugin, Exception ex) {
      plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
   }
}
