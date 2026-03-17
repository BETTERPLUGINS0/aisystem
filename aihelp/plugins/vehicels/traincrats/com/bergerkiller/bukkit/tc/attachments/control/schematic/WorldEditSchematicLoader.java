package com.bergerkiller.bukkit.tc.attachments.control.schematic;

import com.bergerkiller.bukkit.common.Task;
import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.component.LibraryComponent;
import com.bergerkiller.bukkit.common.internal.CommonCapabilities;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.math.BlockVector3;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class WorldEditSchematicLoader implements LibraryComponent {
   private static final long SCHEMATIC_EXPIRE_TIME_MS = 1800000L;
   private static final long SCHEMATIC_EXPIRE_TASK_INTERVAL = 1200L;
   private final TrainCarts plugin;
   private final Path tcSchematicsPath;
   private final Object lock = new Object();
   private final Map<Path, WorldEditSchematicLoader.Schematic> loadedSchematicsByFile = new HashMap();
   private final Map<String, WorldEditSchematicLoader.Schematic> loadedSchematics = new HashMap();
   private final Map<String, List<WorldEditSchematicLoader.SchematicReader>> pendingSchematics = new LinkedHashMap();
   private volatile boolean isShuttingDown = false;
   private volatile WorldEditSchematicLoader.LoaderThread loaderThread = null;
   private volatile Task unloaderTask = null;
   private static final WorldEditSchematicLoader.ReaderState WAITING_STATE = new WorldEditSchematicLoader.ReaderState() {
      public boolean isDone() {
         return false;
      }

      public boolean hasError() {
         return false;
      }
   };
   private static final WorldEditSchematicLoader.ReaderState ABORTED_STATE = new WorldEditSchematicLoader.ReaderState() {
      public boolean isDone() {
         return true;
      }

      public boolean hasError() {
         return true;
      }
   };

   public WorldEditSchematicLoader(TrainCarts plugin) {
      this.plugin = plugin;
      this.tcSchematicsPath = plugin.getDataFile(new String[]{"schematics"}).toPath().toAbsolutePath();
      this.isShuttingDown = Bukkit.getPluginManager().getPlugin("WorldEdit") == null;
   }

   public boolean isEnabled() {
      return !this.isShuttingDown;
   }

   public void enable() {
      if (TCConfig.allowSchematicAttachment && CommonCapabilities.HAS_DISPLAY_ENTITY) {
         Plugin worldEdit = Bukkit.getPluginManager().getPlugin("WorldEdit");
         if (worldEdit != null && worldEdit.isEnabled()) {
            try {
               Files.createDirectories(this.tcSchematicsPath);
            } catch (Throwable var3) {
            }

            this.isShuttingDown = false;
            if (this.loaderThread == null) {
               this.loaderThread = new WorldEditSchematicLoader.LoaderThread();
               this.loaderThread.start();
            }

            this.unloaderTask = (new Task(this.plugin) {
               List<WorldEditSchematicLoader.Schematic> schematicsToUnload = new ArrayList();

               public void run() {
                  try {
                     long time = System.currentTimeMillis();
                     synchronized(WorldEditSchematicLoader.this.lock) {
                        Iterator var4 = WorldEditSchematicLoader.this.loadedSchematicsByFile.values().iterator();

                        while(true) {
                           WorldEditSchematicLoader.Schematic s;
                           if (!var4.hasNext()) {
                              var4 = this.schematicsToUnload.iterator();

                              while(var4.hasNext()) {
                                 s = (WorldEditSchematicLoader.Schematic)var4.next();
                                 s.remove(true);
                              }
                              break;
                           }

                           s = (WorldEditSchematicLoader.Schematic)var4.next();
                           if (s.canUnload(time)) {
                              this.schematicsToUnload.add(s);
                           }
                        }
                     }
                  } finally {
                     this.schematicsToUnload.clear();
                  }

               }
            }).start(1200L, 1200L);
         } else {
            this.isShuttingDown = true;
            this.unloadAllCurrentSchematics();
         }
      } else {
         this.isShuttingDown = true;
         this.unloadAllCurrentSchematics();
      }
   }

   public void disable() {
      this.isShuttingDown = true;
      if (this.loaderThread != null) {
         try {
            this.loaderThread.join(500L);
         } catch (Throwable var4) {
            this.plugin.log(Level.WARNING, "Schematic loader is still busy. Waiting for 15s...");

            try {
               this.loaderThread.join(15000L);
            } catch (Throwable var3) {
               this.plugin.log(Level.SEVERE, "Schematic loader is stuck! Resuming shutdown anyway...");
            }
         }

         this.loaderThread = null;
      }

      this.unloadAllCurrentSchematics();
      Task.stop(this.unloaderTask);
      this.unloaderTask = null;
   }

   private void unloadAllCurrentSchematics() {
      synchronized(this.lock) {
         this.pendingSchematics.values().forEach((l) -> {
            l.forEach((r) -> {
               r.state = ABORTED_STATE;
            });
         });
         this.pendingSchematics.clear();
         this.loadedSchematicsByFile.values().forEach((s) -> {
            s.activeReaders.forEach((r) -> {
               r.state = ABORTED_STATE;
            });
            s.activeReaders.clear();
         });
         this.loadedSchematicsByFile.clear();
         this.loadedSchematics.clear();
      }
   }

   public WorldEditSchematicLoader.SchematicReader startReading(String fileName) {
      if (!this.isShuttingDown && !fileName.isEmpty()) {
         synchronized(this.lock) {
            WorldEditSchematicLoader.Schematic loaded = (WorldEditSchematicLoader.Schematic)this.loadedSchematics.get(fileName);
            if (loaded != null && !loaded.wasModifiedSinceLoading()) {
               return loaded.addReader(new WorldEditSchematicLoader.SchematicReader(fileName, new WorldEditSchematicLoader.ReaderStateBusy(loaded)));
            } else {
               WorldEditSchematicLoader.SchematicReader reader = new WorldEditSchematicLoader.SchematicReader(fileName, WAITING_STATE);
               ((List)this.pendingSchematics.computeIfAbsent(fileName, (n) -> {
                  return new ArrayList();
               })).add(reader);
               this.lock.notifyAll();
               return reader;
            }
         }
      } else {
         return new WorldEditSchematicLoader.SchematicReader(fileName, ABORTED_STATE);
      }
   }

   private interface ReaderState {
      boolean isDone();

      boolean hasError();

      default void abort(WorldEditSchematicLoader.SchematicReader reader) {
      }

      default WorldEditSchematicLoader.SchematicBlock next() {
         return null;
      }
   }

   private class LoaderThread extends Thread {
      public LoaderThread() {
         this.setName("TrainCarts schematic loader thread");
         this.setDaemon(true);
      }

      public void run() {
         while(true) {
            String inputFileName;
            Path schematicFilePath;
            List readers;
            synchronized(WorldEditSchematicLoader.this.lock) {
               if (WorldEditSchematicLoader.this.isShuttingDown) {
                  WorldEditSchematicLoader.this.pendingSchematics.values().forEach((readerList) -> {
                     readerList.forEach(WorldEditSchematicLoader.SchematicReader::abort);
                  });
                  WorldEditSchematicLoader.this.pendingSchematics.clear();
                  return;
               }

               Iterator<Entry<String, List<WorldEditSchematicLoader.SchematicReader>>> iter = WorldEditSchematicLoader.this.pendingSchematics.entrySet().iterator();
               if (!iter.hasNext()) {
                  try {
                     WorldEditSchematicLoader.this.lock.wait(10000L);
                  } catch (InterruptedException var10) {
                  }
                  continue;
               }

               Entry<String, List<WorldEditSchematicLoader.SchematicReader>> loadEntry = (Entry)iter.next();
               inputFileName = (String)loadEntry.getKey();

               try {
                  schematicFilePath = Paths.get(inputFileName);
               } catch (InvalidPathException var13) {
                  schematicFilePath = null;
               }

               readers = (List)loadEntry.getValue();
            }

            List<Path> searchPaths = (List)Stream.of(WorldEdit.getInstance().getWorkingDirectoryPath("schematics").toAbsolutePath(), WorldEditSchematicLoader.this.tcSchematicsPath).filter((x$0) -> {
               return Files.isDirectory(x$0, new LinkOption[0]);
            }).collect(Collectors.toList());
            if (schematicFilePath == null) {
               searchPaths = Collections.emptyList();
            } else if (schematicFilePath.getParent() != null) {
               searchPaths = this.applyToSearchPaths(searchPaths, schematicFilePath.getParent());
               schematicFilePath = schematicFilePath.getFileName();
            }

            schematicFilePath = this.findFile(searchPaths, schematicFilePath);
            FileTime lastModifiedTime = null;
            if (schematicFilePath != null) {
               try {
                  lastModifiedTime = Files.getLastModifiedTime(schematicFilePath);
               } catch (Throwable var12) {
                  WorldEditSchematicLoader.this.plugin.getLogger().log(Level.WARNING, "Failed to read last modified date of " + schematicFilePath, var12);
               }
            }

            if (schematicFilePath != null && lastModifiedTime != null) {
               this.loadSchematic(inputFileName, schematicFilePath, lastModifiedTime, readers);
            } else {
               synchronized(WorldEditSchematicLoader.this.lock) {
                  WorldEditSchematicLoader.this.pendingSchematics.remove(inputFileName);
                  readers.forEach(WorldEditSchematicLoader.SchematicReader::abort);
               }
            }
         }
      }

      private void loadSchematic(String inputFileName, Path schematicFilePath, FileTime lastModifiedTime, List<WorldEditSchematicLoader.SchematicReader> readers) {
         WorldEditSchematicLoader.Schematic loadedSchematic;
         synchronized(WorldEditSchematicLoader.this.lock) {
            loadedSchematic = (WorldEditSchematicLoader.Schematic)WorldEditSchematicLoader.this.loadedSchematicsByFile.get(schematicFilePath);
            if (loadedSchematic != null) {
               if (loadedSchematic.lastModified.equals(lastModifiedTime)) {
                  WorldEditSchematicLoader.this.pendingSchematics.remove(inputFileName);
                  WorldEditSchematicLoader.this.loadedSchematics.put(inputFileName, loadedSchematic);
                  Objects.requireNonNull(loadedSchematic);
                  readers.forEach(loadedSchematic::addReader);
                  return;
               }

               loadedSchematic.remove(false);
            }
         }

         Clipboard clipboard = null;

         try {
            ClipboardFormat format = ClipboardFormats.findByFile(schematicFilePath.toFile());
            ClipboardReader reader = format.getReader(Files.newInputStream(schematicFilePath));

            try {
               clipboard = reader.read();
            } catch (Throwable var17) {
               if (reader != null) {
                  try {
                     reader.close();
                  } catch (Throwable var13) {
                     var17.addSuppressed(var13);
                  }
               }

               throw var17;
            }

            if (reader != null) {
               reader.close();
            }

            BlockVector3 dims = clipboard.getDimensions();
            loadedSchematic = WorldEditSchematicLoader.this.new Schematic(schematicFilePath, lastModifiedTime, new IntVector3(dims.getX(), dims.getY(), dims.getZ()));
         } catch (Throwable var18) {
            WorldEditSchematicLoader.this.plugin.getLogger().log(Level.SEVERE, "Failed to load schematic " + schematicFilePath, var18);
            clipboard = null;
            loadedSchematic = WorldEditSchematicLoader.this.new Schematic(schematicFilePath, lastModifiedTime, IntVector3.ZERO);
            loadedSchematic.done = true;
            loadedSchematic.error = true;
         }

         synchronized(WorldEditSchematicLoader.this.lock) {
            loadedSchematic.fileNames.add(inputFileName);
            WorldEditSchematicLoader.this.pendingSchematics.remove(inputFileName);
            WorldEditSchematicLoader.this.loadedSchematics.put(inputFileName, loadedSchematic);
            WorldEditSchematicLoader.this.loadedSchematicsByFile.put(schematicFilePath, loadedSchematic);
            Objects.requireNonNull(loadedSchematic);
            readers.forEach(loadedSchematic::addReader);
            if (!loadedSchematic.hasActiveReaders()) {
               loadedSchematic.remove(true);
               return;
            }
         }

         if (clipboard != null) {
            BlockVector3 min = clipboard.getMinimumPoint();

            try {
               WorldEditSchematicLoader.BlockIterator iter = new WorldEditSchematicLoader.BlockIterator(loadedSchematic.dimensions);
               int checkReadersCounter = 0;

               while(!iter.done) {
                  BlockData blockData = BlockData.fromBukkit(BukkitAdapter.adapt(clipboard.getBlock(min.add(iter.x, iter.y, iter.z))));
                  loadedSchematic.blockData[iter.index] = blockData;
                  iter.advance();
                  ++checkReadersCounter;
                  if (checkReadersCounter == 100) {
                     synchronized(WorldEditSchematicLoader.this.lock) {
                        if (WorldEditSchematicLoader.this.isShuttingDown) {
                           loadedSchematic.error = true;
                           break;
                        }

                        if (!loadedSchematic.hasActiveReaders()) {
                           loadedSchematic.error = true;
                           loadedSchematic.remove(true);
                           break;
                        }
                     }
                  }
               }
            } catch (Throwable var15) {
               WorldEditSchematicLoader.this.plugin.getLogger().log(Level.SEVERE, "Failed to load schematic " + schematicFilePath, var15);
               loadedSchematic.error = true;
            }

            loadedSchematic.done = true;
         }

      }

      private Path findFile(List<Path> searchPaths, Path fileName) {
         if (searchPaths.isEmpty()) {
            return null;
         } else {
            Iterator var3 = searchPaths.iterator();

            Path searchPathx;
            while(var3.hasNext()) {
               Path searchPath = (Path)var3.next();
               searchPathx = searchPath.resolve(fileName);
               if (Files.isRegularFile(searchPathx, new LinkOption[0])) {
                  return searchPathx;
               }
            }

            String baseNameToFind = this.findBaseName(fileName);
            Iterator var9 = searchPaths.iterator();

            while(var9.hasNext()) {
               searchPathx = (Path)var9.next();

               try {
                  Optional<Path> result = Files.list(searchPathx).filter((p) -> {
                     return this.findBaseName(p).equals(baseNameToFind);
                  }).filter((x$0) -> {
                     return Files.isRegularFile(x$0, new LinkOption[0]);
                  }).findFirst();
                  if (result.isPresent()) {
                     return ((Path)result.get()).toAbsolutePath();
                  }
               } catch (Throwable var7) {
                  WorldEditSchematicLoader.this.plugin.getLogger().log(Level.WARNING, "Failed to list schematics in " + searchPathx, var7);
               }
            }

            return null;
         }
      }

      private List<Path> applyToSearchPaths(List<Path> searchPaths, Path subDir) {
         if (!subDir.isAbsolute()) {
            return (List)searchPaths.stream().map((s) -> {
               Path sub = s.resolve(subDir).toAbsolutePath();
               return sub.startsWith(s) ? sub : null;
            }).filter(Objects::nonNull).filter((x$0) -> {
               return Files.isDirectory(x$0, new LinkOption[0]);
            }).collect(Collectors.toList());
         } else {
            boolean isAllowed = false;
            Iterator var4 = searchPaths.iterator();

            while(var4.hasNext()) {
               Path search = (Path)var4.next();
               if (subDir.startsWith(search)) {
                  isAllowed = true;
                  break;
               }
            }

            return isAllowed && Files.isDirectory(subDir, new LinkOption[0]) ? Collections.singletonList(subDir) : Collections.emptyList();
         }
      }

      private String findBaseName(Path path) {
         String name = path.getFileName().toString().toLowerCase(Locale.ENGLISH);
         if (name.endsWith(".schem")) {
            return name.substring(0, name.length() - 6);
         } else if (name.endsWith(".schematic")) {
            return name.substring(0, name.length() - 10);
         } else {
            return name.endsWith(".") ? name.substring(0, name.length() - 1) : name;
         }
      }
   }

   public class SchematicReader {
      private final String fileName;
      protected volatile WorldEditSchematicLoader.ReaderState state;

      private SchematicReader(String fileName, WorldEditSchematicLoader.ReaderState state) {
         this.fileName = fileName;
         this.state = state;
      }

      public String fileName() {
         return this.fileName;
      }

      public void abort() {
         synchronized(WorldEditSchematicLoader.this.lock) {
            this.state.abort(this);
            this.state = WorldEditSchematicLoader.ABORTED_STATE;
         }
      }

      public boolean isDone() {
         return this.state.isDone();
      }

      public boolean hasError() {
         return this.state.hasError();
      }

      public WorldEditSchematicLoader.SchematicBlock next() {
         return this.state.next();
      }

      // $FF: synthetic method
      SchematicReader(String x1, WorldEditSchematicLoader.ReaderState x2, Object x3) {
         this(x1, x2);
      }
   }

   public class Schematic {
      public final IntVector3 dimensions;
      public final Path schematicFilePath;
      protected final FileTime lastModified;
      protected long lastModifiedLastChecked;
      protected boolean wasModified;
      protected final BlockData[] blockData;
      protected boolean error;
      protected boolean done;
      protected long lastAccessed;
      protected final Set<WorldEditSchematicLoader.SchematicReader> activeReaders;
      protected final List<String> fileNames;

      private Schematic(Path schematicFilePath, FileTime lastModified, IntVector3 dimensions) {
         this.error = false;
         this.done = false;
         this.activeReaders = new HashSet();
         this.fileNames = new ArrayList();
         int numOfBlocks = dimensions.x * dimensions.y * dimensions.z;
         if (numOfBlocks > 1000000) {
            throw new IllegalArgumentException("Schematic is too big (>1 million blocks): " + dimensions);
         } else {
            this.dimensions = dimensions;
            this.schematicFilePath = schematicFilePath;
            this.lastModified = lastModified;
            this.wasModified = false;
            this.lastModifiedLastChecked = this.lastAccessed = System.currentTimeMillis();
            this.blockData = new BlockData[numOfBlocks];
         }
      }

      public boolean isDone() {
         return this.done;
      }

      public boolean hasError() {
         return this.error;
      }

      protected boolean wasModifiedSinceLoading() {
         if (this.wasModified) {
            return true;
         } else {
            long timeNow = System.currentTimeMillis();
            if (timeNow - this.lastModifiedLastChecked > 1000L) {
               try {
                  FileTime currTime = Files.getLastModifiedTime(this.schematicFilePath);
                  if (!currTime.equals(this.lastModified)) {
                     this.wasModified = true;
                     return true;
                  }

                  this.lastModifiedLastChecked = timeNow;
               } catch (Throwable var4) {
                  this.wasModified = true;
                  return true;
               }
            }

            return false;
         }
      }

      protected boolean canUnload(long currentTime) {
         return this.activeReaders.isEmpty() && currentTime - this.lastAccessed > 1800000L;
      }

      protected boolean hasActiveReaders() {
         return !this.activeReaders.isEmpty();
      }

      protected WorldEditSchematicLoader.SchematicReader addReader(WorldEditSchematicLoader.SchematicReader reader) {
         if (reader.state != WorldEditSchematicLoader.ABORTED_STATE) {
            reader.state = WorldEditSchematicLoader.this.new ReaderStateBusy(this);
            this.activeReaders.add(reader);
            this.lastAccessed = System.currentTimeMillis();
         }

         return reader;
      }

      protected void remove(boolean abortReaders) {
         Iterator var2 = this.fileNames.iterator();

         while(var2.hasNext()) {
            String fileName = (String)var2.next();
            WorldEditSchematicLoader.this.loadedSchematics.remove(fileName, this);
         }

         WorldEditSchematicLoader.this.loadedSchematicsByFile.remove(this.schematicFilePath, this);
         this.fileNames.clear();
         if (abortReaders) {
            WorldEditSchematicLoader.SchematicReader reader;
            for(var2 = this.activeReaders.iterator(); var2.hasNext(); reader.state = WorldEditSchematicLoader.ABORTED_STATE) {
               reader = (WorldEditSchematicLoader.SchematicReader)var2.next();
            }

            this.activeReaders.clear();
         }

      }

      // $FF: synthetic method
      Schematic(Path x1, FileTime x2, IntVector3 x3, Object x4) {
         this(x1, x2, x3);
      }
   }

   private class ReaderStateBusy extends WorldEditSchematicLoader.BlockIterator implements WorldEditSchematicLoader.ReaderState {
      public final WorldEditSchematicLoader.Schematic schematic;
      public boolean error;

      public ReaderStateBusy(WorldEditSchematicLoader.Schematic schematic) {
         super(schematic.dimensions);
         this.schematic = schematic;
         this.error = false;
      }

      public boolean isDone() {
         return this.done;
      }

      public boolean hasError() {
         return this.error;
      }

      public void abort(WorldEditSchematicLoader.SchematicReader reader) {
         this.schematic.activeReaders.remove(reader);
         this.schematic.lastAccessed = System.currentTimeMillis();
      }

      public WorldEditSchematicLoader.SchematicBlock next() {
         if (this.done) {
            return null;
         } else {
            BlockData data = this.schematic.blockData[this.index];
            if (data != null) {
               WorldEditSchematicLoader.SchematicBlock block = new WorldEditSchematicLoader.SchematicBlock(this.schematic, this.x, this.y, this.z, data);
               this.advance();
               return block;
            } else {
               this.done = this.schematic.isDone();
               this.error = this.schematic.hasError();
               return null;
            }
         }
      }
   }

   private static class BlockIterator {
      public final int x_max;
      public final int y_max;
      public final int z_max;
      public int x;
      public int y;
      public int z;
      public int index;
      public boolean done;

      public BlockIterator(IntVector3 dimensions) {
         this.x_max = dimensions.x;
         this.y_max = dimensions.y;
         this.z_max = dimensions.z;
         this.x = 0;
         this.y = 0;
         this.z = 0;
         this.index = 0;
         this.done = this.x_max == 0 || this.y_max == 0 || this.z_max == 0;
      }

      public void advance() {
         ++this.index;
         if (++this.x == this.x_max) {
            this.x = 0;
            if (++this.z == this.z_max) {
               this.z = 0;
               if (++this.y == this.y_max) {
                  this.done = true;
               }
            }
         }

      }
   }

   public static class SchematicBlock {
      public final WorldEditSchematicLoader.Schematic schematic;
      public final int x;
      public final int y;
      public final int z;
      public final BlockData blockData;

      public SchematicBlock(WorldEditSchematicLoader.Schematic schematic, int x, int y, int z, BlockData blockData) {
         this.schematic = schematic;
         this.x = x;
         this.y = y;
         this.z = z;
         this.blockData = blockData;
      }
   }
}
