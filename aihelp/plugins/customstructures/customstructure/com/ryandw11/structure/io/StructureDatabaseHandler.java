package com.ryandw11.structure.io;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.exceptions.RateLimitException;
import com.ryandw11.structure.exceptions.StructureDatabaseException;
import com.ryandw11.structure.exceptions.StructureNotFoundException;
import com.ryandw11.structure.io.sql.DistanceFunction;
import com.ryandw11.structure.structure.Structure;
import com.ryandw11.structure.utils.Pair;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.sqlite.Function;

public class StructureDatabaseHandler extends BukkitRunnable {
   private final Map<Location, Structure> structuresToSave = new ConcurrentHashMap();
   private final List<Pair<Location, CompletableFuture<Structure>>> structuresToGet = new CopyOnWriteArrayList();
   private final List<Pair<Structure, CompletableFuture<List<Location>>>> locationsToGet = new CopyOnWriteArrayList();
   private final List<Pair<NearbyStructuresRequest, CompletableFuture<NearbyStructuresResponse>>> findNearby = new CopyOnWriteArrayList();
   private final Connection connection;
   private final CustomStructures plugin;

   public StructureDatabaseHandler(CustomStructures plugin) {
      this.plugin = plugin;
      File dataDirectory = new File(String.valueOf(plugin.getDataFolder()) + "/data/");
      if (!dataDirectory.exists() && !dataDirectory.mkdir()) {
         throw new StructureDatabaseException("Unable to create 'data' folder. Does the plugin have the correct permissions?");
      } else {
         try {
            this.connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", String.valueOf(plugin.getDataFolder()) + "/data/structures.db"));
            Function.create(this.connection, "DIST", new DistanceFunction());
            Statement statement = this.connection.createStatement();
            statement.setQueryTimeout(30);
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Structures (\n    id INTEGER PRIMARY KEY,\n    name VARCHAR(100) NOT NULL,\n    x DOUBLE NOT NULL,\n    y DOUBLE NOT NULL,\n    z DOUBLE NOT NULL,\n    world VARCHAR(300) NOT NULL\n)\n");
            statement.close();
         } catch (SQLException var4) {
            if (plugin.isDebug()) {
               var4.printStackTrace();
            }

            throw new StructureDatabaseException("Unable to connect to SQLite database.");
         }
      }
   }

   public void addStructure(Location loc, Structure structure) {
      this.structuresToSave.put(loc, structure);
   }

   public CompletableFuture<Structure> getStructure(Location location) {
      CompletableFuture<Structure> completableFuture = new CompletableFuture();
      this.structuresToGet.add(Pair.of(location, completableFuture));
      return completableFuture;
   }

   public CompletableFuture<NearbyStructuresResponse> findNearby(NearbyStructuresRequest request) {
      CompletableFuture<NearbyStructuresResponse> completableFuture = new CompletableFuture();
      if (this.findNearby.size() <= 5) {
         this.findNearby.add(Pair.of(request, completableFuture));
      } else {
         Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            completableFuture.completeExceptionally(new RateLimitException("The maximum amount of requests has been hit."));
         }, 5L);
      }

      return completableFuture;
   }

   public CompletableFuture<List<Location>> getStructureLocations(Structure structure) {
      CompletableFuture<List<Location>> completableFuture = new CompletableFuture();
      this.locationsToGet.add(Pair.of(structure, completableFuture));
      return completableFuture;
   }

   public void run() {
      Iterator var1 = this.structuresToSave.entrySet().iterator();

      PreparedStatement statement;
      while(var1.hasNext()) {
         Entry<Location, Structure> entry = (Entry)var1.next();
         String worldName = ((World)Objects.requireNonNull(((Location)entry.getKey()).getWorld())).getName();

         try {
            statement = this.connection.prepareStatement("INSERT INTO Structures (name, x, y, z, world) VALUES (?, ?, ?, ?, ?)");
            statement.setString(1, ((Structure)entry.getValue()).getName());
            statement.setDouble(2, (double)((Location)entry.getKey()).getBlockX());
            statement.setDouble(3, (double)((Location)entry.getKey()).getBlockY());
            statement.setDouble(4, (double)((Location)entry.getKey()).getBlockZ());
            statement.setString(5, worldName);
            statement.executeUpdate();
            statement.close();
         } catch (SQLException var10) {
            if (this.plugin.isDebug()) {
               this.plugin.getLogger().warning("An error was encountered when attempting to save a structure to the structure database!");
               var10.printStackTrace();
            }
         }
      }

      this.structuresToSave.clear();
      var1 = this.structuresToGet.iterator();

      Structure statement;
      Pair pair;
      while(var1.hasNext()) {
         pair = (Pair)var1.next();

         try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT name FROM Structures WHERE x = ? AND y = ? AND z = ? AND world = ?");
            statement.setDouble(1, (double)((Location)pair.getLeft()).getBlockX());
            statement.setDouble(2, (double)((Location)pair.getLeft()).getBlockY());
            statement.setDouble(3, (double)((Location)pair.getLeft()).getBlockZ());
            statement.setString(4, ((World)Objects.requireNonNull(((Location)pair.getLeft()).getWorld())).getName());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
               statement = this.plugin.getStructureHandler().getStructure(resultSet.getString("name"));
               if (statement != null) {
                  ((CompletableFuture)pair.getRight()).complete(statement);
               } else {
                  ((CompletableFuture)pair.getRight()).completeExceptionally(new StructureNotFoundException("Retrieved structure is not loaded!"));
               }
            } else {
               ((CompletableFuture)pair.getRight()).completeExceptionally(new StructureNotFoundException("Cannot find structure with the provided location."));
            }
         } catch (SQLException var9) {
            ((CompletableFuture)pair.getRight()).completeExceptionally(new StructureDatabaseException("An error was encountered when attempting to retrieve a structure from the structure database!"));
            if (this.plugin.isDebug()) {
               this.plugin.getLogger().warning("An error was encountered when attempting to retrieve a structure from the structure database!");
               var9.printStackTrace();
            }
         }
      }

      this.structuresToGet.clear();
      var1 = this.locationsToGet.iterator();

      ArrayList result;
      while(var1.hasNext()) {
         pair = (Pair)var1.next();
         result = new ArrayList();

         try {
            statement = this.connection.prepareStatement("SELECT * FROM Structures WHERE name = ?");
            statement.setString(1, ((Structure)pair.getLeft()).getName());
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
               result.add(new Location(Bukkit.getWorld(resultSet.getString("world")), resultSet.getDouble("x"), resultSet.getDouble("y"), resultSet.getDouble("z")));
            }

            ((CompletableFuture)pair.getRight()).complete(result);
         } catch (SQLException var8) {
            ((CompletableFuture)pair.getRight()).completeExceptionally(new StructureDatabaseException("An error was encountered when attempting to retrieve structures from the structure database!"));
            if (this.plugin.isDebug()) {
               this.plugin.getLogger().warning("An error was encountered when attempting to retrieve structures from the structure database!");
               var8.printStackTrace();
            }
         }
      }

      this.locationsToGet.clear();
      var1 = this.findNearby.iterator();

      while(var1.hasNext()) {
         pair = (Pair)var1.next();

         try {
            result = new ArrayList();
            NearbyStructuresRequest nearbyStructuresRequest = (NearbyStructuresRequest)pair.getLeft();
            statement = null;
            PreparedStatement statement;
            if (nearbyStructuresRequest.hasName()) {
               statement = this.connection.prepareStatement("SELECT *, DIST(?, ?, ?, x, y, z) AS dist FROM Structures WHERE name = ? AND world = ? ORDER BY dist ASC LIMIT ?");
               statement.setInt(1, nearbyStructuresRequest.getLocation().getBlockX());
               statement.setInt(2, nearbyStructuresRequest.getLocation().getBlockY());
               statement.setInt(3, nearbyStructuresRequest.getLocation().getBlockZ());
               statement.setString(4, nearbyStructuresRequest.getName());
               statement.setString(5, ((World)Objects.requireNonNull(nearbyStructuresRequest.getLocation().getWorld())).getName());
               statement.setInt(6, nearbyStructuresRequest.getLimit());
            } else {
               statement = this.connection.prepareStatement("SELECT *, DIST(?, ?, ?, x, y, z) AS dist FROM Structures WHERE world = ? ORDER BY dist ASC LIMIT ?");
               statement.setInt(1, nearbyStructuresRequest.getLocation().getBlockX());
               statement.setInt(2, nearbyStructuresRequest.getLocation().getBlockY());
               statement.setInt(3, nearbyStructuresRequest.getLocation().getBlockZ());
               statement.setString(4, ((World)Objects.requireNonNull(nearbyStructuresRequest.getLocation().getWorld())).getName());
               statement.setInt(5, nearbyStructuresRequest.getLimit());
            }

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
               result.add(new NearbyStructuresResponse.NearbyStructureContainer(new Location(Bukkit.getWorld(resultSet.getString("world")), resultSet.getDouble("x"), resultSet.getDouble("y"), resultSet.getDouble("z")), this.plugin.getStructureHandler().getStructure(resultSet.getString("name")), resultSet.getDouble("dist")));
            }

            ((CompletableFuture)pair.getRight()).complete(new NearbyStructuresResponse(result));
         } catch (SQLException var7) {
            ((CompletableFuture)pair.getRight()).completeExceptionally(new StructureDatabaseException("An error was encountered when attempting to retrieve structures from the structure database!"));
            if (this.plugin.isDebug()) {
               this.plugin.getLogger().warning("An error was encountered when attempting to retrieve structures from the structure database! (Nearby)");
               var7.printStackTrace();
            }
         }
      }

      this.findNearby.clear();
   }

   public synchronized void cancel() throws IllegalStateException {
      this.run();
      super.cancel();

      try {
         this.connection.close();
      } catch (SQLException var2) {
         if (this.plugin.isDebug()) {
            this.plugin.getLogger().warning("An error was encountered when attempting to close the database connection!");
            var2.printStackTrace();
         }
      }

   }
}
