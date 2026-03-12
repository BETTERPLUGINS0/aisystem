package me.SuperRonanCraft.BetterRTP.references.database;

import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.versions.AsyncHandler;

public class DatabaseHandler {
   private final DatabasePlayers databasePlayers = new DatabasePlayers();
   private final DatabaseCooldowns databaseCooldowns = new DatabaseCooldowns();
   private final DatabaseQueue databaseQueue = new DatabaseQueue();
   private final DatabaseChunkData databaseChunks = new DatabaseChunkData();

   public void load() {
      AsyncHandler.async(() -> {
         this.databasePlayers.load();
         this.databaseCooldowns.load();
         this.databaseQueue.load();
         this.databaseChunks.load();
      });
   }

   public static DatabasePlayers getPlayers() {
      return BetterRTP.getInstance().getDatabaseHandler().getDatabasePlayers();
   }

   public static DatabaseCooldowns getCooldowns() {
      return BetterRTP.getInstance().getDatabaseHandler().getDatabaseCooldowns();
   }

   public static DatabaseQueue getQueue() {
      return BetterRTP.getInstance().getDatabaseHandler().getDatabaseQueue();
   }

   public DatabasePlayers getDatabasePlayers() {
      return this.databasePlayers;
   }

   public DatabaseCooldowns getDatabaseCooldowns() {
      return this.databaseCooldowns;
   }

   public DatabaseQueue getDatabaseQueue() {
      return this.databaseQueue;
   }

   public DatabaseChunkData getDatabaseChunks() {
      return this.databaseChunks;
   }
}
