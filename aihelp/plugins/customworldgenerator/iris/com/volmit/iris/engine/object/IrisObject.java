package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.PlacedObject;
import com.volmit.iris.engine.framework.placer.HeightmapObjectPlacer;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.context.IrisContext;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.data.IrisCustomData;
import com.volmit.iris.util.data.VectorMap;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.interpolation.IrisInterpolation;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.math.AxisAlignedBB;
import com.volmit.iris.util.math.BlockPosition;
import com.volmit.iris.util.math.Position2;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.math.Vector3i;
import com.volmit.iris.util.matter.MatterMarker;
import com.volmit.iris.util.parallel.BurstExecutor;
import com.volmit.iris.util.parallel.MultiBurst;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import com.volmit.iris.util.scheduling.jobs.Job;
import com.volmit.iris.util.stream.ProceduralStream;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;
import java.util.stream.StreamSupport;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

public class IrisObject extends IrisRegistrant {
   protected static final Vector HALF = new Vector(0.5D, 0.5D, 0.5D);
   protected static final BlockData AIR = B.get("CAVE_AIR");
   protected static final BlockData VAIR = B.get("VOID_AIR");
   protected static final BlockData VAIR_DEBUG = B.get("COBWEB");
   protected static final BlockData[] SNOW_LAYERS = new BlockData[]{B.get("minecraft:snow[layers=1]"), B.get("minecraft:snow[layers=2]"), B.get("minecraft:snow[layers=3]"), B.get("minecraft:snow[layers=4]"), B.get("minecraft:snow[layers=5]"), B.get("minecraft:snow[layers=6]"), B.get("minecraft:snow[layers=7]"), B.get("minecraft:snow[layers=8]")};
   protected final transient Lock readLock;
   protected final transient Lock writeLock;
   protected transient volatile boolean smartBored;
   protected transient AtomicCache<AxisAlignedBB> aabb;
   private VectorMap<BlockData> blocks;
   private VectorMap<TileData> states;
   private int w;
   private int d;
   private int h;
   private transient Vector3i center;
   private transient Vector3i shrinkOffset;

   public IrisObject(int w, int h, int d) {
      this.smartBored = false;
      this.aabb = new AtomicCache();
      this.blocks = new VectorMap();
      this.states = new VectorMap();
      this.w = var1;
      this.h = var2;
      this.d = var3;
      this.center = new Vector3i(var1 / 2, var2 / 2, var3 / 2);
      this.shrinkOffset = new Vector3i(0, 0, 0);
      ReentrantReadWriteLock var4 = new ReentrantReadWriteLock();
      this.readLock = var4.readLock();
      this.writeLock = var4.writeLock();
   }

   public IrisObject() {
      this(0, 0, 0);
   }

   public static BlockVector getCenterForSize(BlockVector size) {
      return new BlockVector(var0.getX() / 2.0D, var0.getY() / 2.0D, var0.getZ() / 2.0D);
   }

   public static AxisAlignedBB getAABBFor(BlockVector size) {
      BlockVector var1 = new BlockVector(var0.getX() / 2.0D, var0.getY() / 2.0D, var0.getZ() / 2.0D);
      return new AxisAlignedBB(new IrisPosition((new BlockVector(0, 0, 0)).subtract(var1).toBlockVector()), new IrisPosition((new BlockVector(var0.getX() - 1.0D, var0.getY() - 1.0D, var0.getZ() - 1.0D)).subtract(var1).toBlockVector()));
   }

   public static BlockVector sampleSize(File file) {
      FileInputStream var1 = new FileInputStream(var0);
      DataInputStream var2 = new DataInputStream(var1);
      BlockVector var3 = new BlockVector(var2.readInt(), var2.readInt(), var2.readInt());
      Objects.requireNonNull(var2);
      Iris.later(var2::close);
      return var3;
   }

   private static List<BlockVector> blocksBetweenTwoPoints(Vector loc1, Vector loc2) {
      ArrayList var2 = new ArrayList();
      int var3 = Math.max(var0.getBlockX(), var1.getBlockX());
      int var4 = Math.min(var0.getBlockX(), var1.getBlockX());
      int var5 = Math.max(var0.getBlockY(), var1.getBlockY());
      int var6 = Math.min(var0.getBlockY(), var1.getBlockY());
      int var7 = Math.max(var0.getBlockZ(), var1.getBlockZ());
      int var8 = Math.min(var0.getBlockZ(), var1.getBlockZ());

      for(int var9 = var4; var9 <= var3; ++var9) {
         for(int var10 = var8; var10 <= var7; ++var10) {
            for(int var11 = var6; var11 <= var5; ++var11) {
               var2.add(new BlockVector(var9, var11, var10));
            }
         }
      }

      return var2;
   }

   public AxisAlignedBB getAABB() {
      return (AxisAlignedBB)this.aabb.aquire(() -> {
         return getAABBFor(new BlockVector(this.w, this.h, this.d));
      });
   }

   public void ensureSmartBored(boolean debug) {
      if (!this.smartBored) {
         PrecisionStopwatch var2 = PrecisionStopwatch.start();
         BlockData var3 = var1 ? VAIR_DEBUG : VAIR;
         this.writeLock.lock();
         AtomicInteger var4 = new AtomicInteger();
         if (this.blocks.isEmpty()) {
            this.writeLock.unlock();
            Iris.warn("Cannot Smart Bore " + this.getLoadKey() + " because it has 0 blocks in it.");
            this.smartBored = true;
         } else {
            BlockVector var5 = new BlockVector(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
            BlockVector var6 = new BlockVector(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
            Iterator var7 = this.blocks.keys().iterator();

            while(var7.hasNext()) {
               BlockVector var8 = (BlockVector)var7.next();
               var5.setX(Math.max(var8.getX(), var5.getX()));
               var6.setX(Math.min(var8.getX(), var6.getX()));
               var5.setY(Math.max(var8.getY(), var5.getY()));
               var6.setY(Math.min(var8.getY(), var6.getY()));
               var5.setZ(Math.max(var8.getZ(), var5.getZ()));
               var6.setZ(Math.min(var8.getZ(), var6.getZ()));
            }

            BurstExecutor var10 = MultiBurst.burst.burst();

            int var11;
            for(var11 = var6.getBlockY(); var11 <= var5.getBlockY(); ++var11) {
               var10.queue(() -> {
                  for(int var6x = var6.getBlockZ(); var6x <= var5.getBlockZ(); ++var6x) {
                     int var7 = Integer.MAX_VALUE;
                     int var8 = Integer.MIN_VALUE;

                     int var9;
                     for(var9 = var6.getBlockX(); var9 <= var5.getBlockX(); ++var9) {
                        if (this.blocks.containsKey(new Vector3i(var9, var11, var6x))) {
                           var7 = Math.min(var9, var7);
                           var8 = Math.max(var9, var8);
                        }
                     }

                     if (var7 != Integer.MAX_VALUE && var8 != Integer.MIN_VALUE) {
                        for(var9 = var7; var9 <= var8; ++var9) {
                           Vector3i var10 = new Vector3i(var9, var11, var6x);
                           if (!var3.equals(this.blocks.get(var10))) {
                              this.blocks.computeIfAbsent(var10, (var1) -> {
                                 return var3;
                              });
                              var4.getAndIncrement();
                           }
                        }
                     }
                  }

               });
            }

            for(var11 = var6.getBlockX(); var11 <= var5.getBlockX(); ++var11) {
               var10.queue(() -> {
                  for(int var6x = var6.getBlockZ(); var6x <= var5.getBlockZ(); ++var6x) {
                     int var7 = Integer.MAX_VALUE;
                     int var8 = Integer.MIN_VALUE;

                     int var9;
                     for(var9 = var6.getBlockY(); var9 <= var5.getBlockY(); ++var9) {
                        if (this.blocks.containsKey(new Vector3i(var11, var9, var6x))) {
                           var7 = Math.min(var9, var7);
                           var8 = Math.max(var9, var8);
                        }
                     }

                     if (var7 != Integer.MAX_VALUE && var8 != Integer.MIN_VALUE) {
                        for(var9 = var7; var9 <= var8; ++var9) {
                           Vector3i var10 = new Vector3i(var11, var9, var6x);
                           if (!var3.equals(this.blocks.get(var10))) {
                              this.blocks.computeIfAbsent(var10, (var1) -> {
                                 return var3;
                              });
                              var4.getAndIncrement();
                           }
                        }
                     }
                  }

               });
            }

            for(var11 = var6.getBlockX(); var11 <= var5.getBlockX(); ++var11) {
               var10.queue(() -> {
                  for(int var6x = var6.getBlockY(); var6x <= var5.getBlockY(); ++var6x) {
                     int var7 = Integer.MAX_VALUE;
                     int var8 = Integer.MIN_VALUE;

                     int var9;
                     for(var9 = var6.getBlockZ(); var9 <= var5.getBlockZ(); ++var9) {
                        if (this.blocks.containsKey(new Vector3i(var11, var6x, var9))) {
                           var7 = Math.min(var9, var7);
                           var8 = Math.max(var9, var8);
                        }
                     }

                     if (var7 != Integer.MAX_VALUE && var8 != Integer.MIN_VALUE) {
                        for(var9 = var7; var9 <= var8; ++var9) {
                           Vector3i var10 = new Vector3i(var11, var6x, var9);
                           if (!var3.equals(this.blocks.get(var10))) {
                              this.blocks.computeIfAbsent(var10, (var1) -> {
                                 return var3;
                              });
                              var4.getAndIncrement();
                           }
                        }
                     }
                  }

               });
            }

            var10.complete();
            this.smartBored = true;
            this.writeLock.unlock();
            String var10000 = this.getLoadKey();
            Iris.debug("Smart Bore: " + var10000 + " in " + Form.duration(var2.getMilliseconds(), 2) + " (" + Form.f(var4.get()) + ")");
         }
      }
   }

   public synchronized IrisObject copy() {
      IrisObject var1 = new IrisObject(this.w, this.h, this.d);
      var1.setLoadKey(var1.getLoadKey());
      var1.setLoader(this.getLoader());
      var1.setLoadFile(this.getLoadFile());
      var1.setCenter(this.getCenter().clone());
      this.blocks.forEach((var1x, var2) -> {
         var1.blocks.put(var1x.clone(), var2.clone());
      });
      this.states.forEach((var1x, var2) -> {
         var1.states.put(var1x.clone(), var2.clone());
      });
      return var1;
   }

   public void readLegacy(InputStream in) {
      DataInputStream var2 = new DataInputStream(var1);
      this.w = var2.readInt();
      this.h = var2.readInt();
      this.d = var2.readInt();
      this.center = new Vector3i(this.w / 2, this.h / 2, this.d / 2);
      int var3 = var2.readInt();

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         this.blocks.put(new Vector3i(var2.readShort(), var2.readShort(), var2.readShort()), B.get(var2.readUTF()));
      }

      if (var2.available() != 0) {
         try {
            var4 = var2.readInt();

            for(int var5 = 0; var5 < var4; ++var5) {
               this.states.put(new Vector3i(var2.readShort(), var2.readShort(), var2.readShort()), TileData.read(var2));
            }
         } catch (Throwable var6) {
            Iris.reportError(var6);
         }

      }
   }

   public void read(InputStream in) {
      DataInputStream var2 = new DataInputStream(var1);
      this.w = var2.readInt();
      this.h = var2.readInt();
      this.d = var2.readInt();
      if (!var2.readUTF().equals("Iris V2 IOB;")) {
         throw new IrisObject.HeaderException();
      } else {
         this.center = new Vector3i(this.w / 2, this.h / 2, this.d / 2);
         short var3 = var2.readShort();
         KList var5 = new KList();

         int var4;
         for(var4 = 0; var4 < var3; ++var4) {
            var5.add((Object)var2.readUTF());
         }

         int var6 = var2.readInt();

         for(var4 = 0; var4 < var6; ++var4) {
            this.blocks.put(new Vector3i(var2.readShort(), var2.readShort(), var2.readShort()), B.get((String)var5.get(var2.readShort())));
         }

         var6 = var2.readInt();

         for(var4 = 0; var4 < var6; ++var4) {
            this.states.put(new Vector3i(var2.readShort(), var2.readShort(), var2.readShort()), TileData.read(var2));
         }

      }
   }

   public void write(OutputStream o) {
      DataOutputStream var2 = new DataOutputStream(var1);
      var2.writeInt(this.w);
      var2.writeInt(this.h);
      var2.writeInt(this.d);
      var2.writeUTF("Iris V2 IOB;");
      KList var3 = new KList();
      Iterator var4 = this.blocks.values().iterator();

      while(var4.hasNext()) {
         BlockData var5 = (BlockData)var4.next();
         var3.addIfMissing(var5.getAsString());
      }

      var2.writeShort(var3.size());
      var4 = var3.iterator();

      while(var4.hasNext()) {
         String var8 = (String)var4.next();
         var2.writeUTF(var8);
      }

      var2.writeInt(this.blocks.size());
      VectorMap.EntryIterator var7 = this.blocks.iterator();

      BlockVector var6;
      Entry var9;
      while(var7.hasNext()) {
         var9 = (Entry)var7.next();
         var6 = (BlockVector)var9.getKey();
         var2.writeShort(var6.getBlockX());
         var2.writeShort(var6.getBlockY());
         var2.writeShort(var6.getBlockZ());
         var2.writeShort(var3.indexOf(((BlockData)var9.getValue()).getAsString()));
      }

      var2.writeInt(this.states.size());
      var7 = this.states.iterator();

      while(var7.hasNext()) {
         var9 = (Entry)var7.next();
         var6 = (BlockVector)var9.getKey();
         var2.writeShort(var6.getBlockX());
         var2.writeShort(var6.getBlockY());
         var2.writeShort(var6.getBlockZ());
         ((TileData)var9.getValue()).toBinary(var2);
      }

   }

   public void write(OutputStream o, VolmitSender sender) {
      final AtomicReference var3 = new AtomicReference();
      final CountDownLatch var4 = new CountDownLatch(1);
      (new Job() {
         private int total;
         private int c;

         {
            this.total = IrisObject.this.blocks.size() * 3 + IrisObject.this.states.size();
            this.c = 0;
         }

         public String getName() {
            return "Saving Object";
         }

         public void execute() {
            try {
               DataOutputStream var1x = new DataOutputStream(var1);
               var1x.writeInt(IrisObject.this.w);
               var1x.writeInt(IrisObject.this.h);
               var1x.writeInt(IrisObject.this.d);
               var1x.writeUTF("Iris V2 IOB;");
               KList var2 = new KList();

               Iterator var3x;
               for(var3x = IrisObject.this.blocks.values().iterator(); var3x.hasNext(); ++this.c) {
                  BlockData var4x = (BlockData)var3x.next();
                  var2.addIfMissing(var4x.getAsString());
               }

               this.total -= IrisObject.this.blocks.size() - var2.size();
               var1x.writeShort(var2.size());

               for(var3x = var2.iterator(); var3x.hasNext(); ++this.c) {
                  String var12 = (String)var3x.next();
                  var1x.writeUTF(var12);
               }

               var1x.writeInt(IrisObject.this.blocks.size());

               BlockVector var5;
               VectorMap.EntryIterator var11;
               Entry var13;
               for(var11 = IrisObject.this.blocks.iterator(); var11.hasNext(); ++this.c) {
                  var13 = (Entry)var11.next();
                  var5 = (BlockVector)var13.getKey();
                  var1x.writeShort(var5.getBlockX());
                  var1x.writeShort(var5.getBlockY());
                  var1x.writeShort(var5.getBlockZ());
                  var1x.writeShort(var2.indexOf(((BlockData)var13.getValue()).getAsString()));
               }

               var1x.writeInt(IrisObject.this.states.size());

               for(var11 = IrisObject.this.states.iterator(); var11.hasNext(); ++this.c) {
                  var13 = (Entry)var11.next();
                  var5 = (BlockVector)var13.getKey();
                  var1x.writeShort(var5.getBlockX());
                  var1x.writeShort(var5.getBlockY());
                  var1x.writeShort(var5.getBlockZ());
                  ((TileData)var13.getValue()).toBinary(var1x);
               }
            } catch (IOException var9) {
               var3.set(var9);
            } finally {
               var4.countDown();
            }

         }

         public void completeWork() {
         }

         public int getTotalWork() {
            return this.total;
         }

         public int getWorkCompleted() {
            return this.c;
         }
      }).execute(var2, true, () -> {
      });

      try {
         var4.await();
      } catch (InterruptedException var6) {
      }

      if (var3.get() != null) {
         throw (IOException)var3.get();
      }
   }

   public void read(File file) {
      try {
         BufferedInputStream var2 = new BufferedInputStream(new FileInputStream(var1));

         try {
            this.read((InputStream)var2);
         } catch (Throwable var9) {
            try {
               var2.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }

            throw var9;
         }

         var2.close();
      } catch (Throwable var10) {
         if (!(var10 instanceof IrisObject.HeaderException)) {
            Iris.reportError(var10);
         }

         BufferedInputStream var3 = new BufferedInputStream(new FileInputStream(var1));

         try {
            this.readLegacy(var3);
         } catch (Throwable var7) {
            try {
               var3.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }

            throw var7;
         }

         var3.close();
      }

   }

   public void write(File file) {
      if (var1 != null) {
         FileOutputStream var2 = new FileOutputStream(var1);
         this.write((OutputStream)var2);
         var2.close();
      }
   }

   public void write(File file, VolmitSender sender) {
      if (var1 != null) {
         FileOutputStream var3 = new FileOutputStream(var1);
         this.write((OutputStream)var3, var2);
         var3.close();
      }
   }

   public void shrinkwrap() {
      if (!this.blocks.isEmpty()) {
         BlockVector var1 = new BlockVector(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
         BlockVector var2 = new BlockVector(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
         Iterator var3 = this.blocks.keys().iterator();

         while(var3.hasNext()) {
            BlockVector var4 = (BlockVector)var3.next();
            var1.setX(Math.min(var1.getX(), var4.getX()));
            var1.setY(Math.min(var1.getY(), var4.getY()));
            var1.setZ(Math.min(var1.getZ(), var4.getZ()));
            var2.setX(Math.max(var2.getX(), var4.getX()));
            var2.setY(Math.max(var2.getY(), var4.getY()));
            var2.setZ(Math.max(var2.getZ(), var4.getZ()));
         }

         this.w = var2.getBlockX() - var1.getBlockX() + 1;
         this.h = var2.getBlockY() - var1.getBlockY() + 1;
         this.d = var2.getBlockZ() - var1.getBlockZ() + 1;
         this.center = new Vector3i(this.w / 2, this.h / 2, this.d / 2);
         Vector3i var6 = new Vector3i(-this.center.getBlockX() - var1.getBlockX(), -this.center.getBlockY() - var1.getBlockY(), -this.center.getBlockZ() - var1.getBlockZ());
         if (var6.getBlockX() != 0 || var6.getBlockY() != 0 || var6.getBlockZ() != 0) {
            VectorMap var7 = new VectorMap();
            VectorMap var5 = new VectorMap();
            this.blocks.forEach((var2x, var3x) -> {
               var2x.add(var6);
               var7.put(var2x, var3x);
            });
            this.states.forEach((var2x, var3x) -> {
               var2x.add(var6);
               var5.put(var2x, var3x);
            });
            this.shrinkOffset = var6;
            this.blocks = var7;
            this.states = var5;
         }
      }
   }

   public void clean() {
      VectorMap var1 = new VectorMap();
      var1.putAll(this.blocks);
      VectorMap var2 = new VectorMap();
      var2.putAll(this.states);
      this.blocks = var1;
      this.states = var2;
   }

   public Vector3i getSigned(int x, int y, int z) {
      if (var1 < this.w && var2 < this.h && var3 < this.d) {
         return (Vector3i)(new Vector3i(var1, var2, var3)).subtract(this.center);
      } else {
         throw new RuntimeException(var1 + " " + var2 + " " + var3 + " exceeds limit of " + this.w + " " + this.h + " " + this.d);
      }
   }

   public void setUnsigned(int x, int y, int z, BlockData block) {
      Vector3i var5 = this.getSigned(var1, var2, var3);
      if (var4 == null) {
         this.blocks.remove(var5);
         this.states.remove(var5);
      } else {
         this.blocks.put(var5, var4);
      }

   }

   public void setUnsigned(int x, int y, int z, Block block, boolean legacy) {
      Vector3i var6 = this.getSigned(var1, var2, var3);
      if (var4 == null) {
         this.blocks.remove(var6);
         this.states.remove(var6);
      } else {
         BlockData var7 = var4.getBlockData();
         this.blocks.put(var6, var7);
         TileData var8 = TileData.getTileState(var4, var5);
         if (var8 != null) {
            Iris.debug("Saved State " + String.valueOf(var6));
            this.states.put(var6, var8);
         }
      }

   }

   public int place(int x, int z, IObjectPlacer placer, IrisObjectPlacement config, RNG rng, IrisData rdata) {
      return this.place(var1, -1, var2, var3, var4, var5, var6);
   }

   public int place(int x, int z, IObjectPlacer placer, IrisObjectPlacement config, RNG rng, CarveResult c, IrisData rdata) {
      return this.place(var1, -1, var2, var3, var4, var5, (BiConsumer)null, var6, var7);
   }

   public int place(int x, int yv, int z, IObjectPlacer placer, IrisObjectPlacement config, RNG rng, IrisData rdata) {
      return this.place(var1, var2, var3, var4, var5, var6, (BiConsumer)null, (CarveResult)null, var7);
   }

   public int place(Location loc, IObjectPlacer placer, IrisObjectPlacement config, RNG rng, IrisData rdata) {
      return this.place(var1.getBlockX(), var1.getBlockY(), var1.getBlockZ(), var2, var3, var4, var5);
   }

   public int place(int x, int yv, int z, IObjectPlacer oplacer, IrisObjectPlacement config, RNG rng, BiConsumer<BlockPosition, BlockData> listener, CarveResult c, IrisData rdata) {
      Object var10 = var5.getHeightmap() != null ? new HeightmapObjectPlacer(var4.getEngine() == null ? IrisContext.get().getEngine() : var4.getEngine(), var6, var1, var2, var3, var5, var4) : var4;
      if (var9 != null) {
         if (!var5.getSlopeCondition().isDefault() && !var5.getSlopeCondition().isValid((Double)var9.getEngine().getComplex().getSlopeStream().get((double)var1, (double)var3)) && !var5.isForcePlace()) {
            return -1;
         }

         short var11 = 0;
         ProceduralStream var12 = var9.getEngine().getComplex().getHeightStream();
         if (var5.isRotateTowardsSlope()) {
            double var13 = (Double)var12.get((double)var1, (double)((float)var3 + (float)this.d / 2.0F));
            double var15 = (Double)var12.get((double)((float)var1 + (float)this.w / 2.0F), (double)var3);
            double var17 = (Double)var12.get((double)var1, (double)((float)var3 - (float)this.d / 2.0F));
            double var19 = (Double)var12.get((double)((float)var1 - (float)this.w / 2.0F), (double)var3);
            double var21 = Math.min(Math.min(var13, var15), Math.min(var17, var19));
            if (var21 == var13) {
               var11 = 0;
            } else if (var21 == var15) {
               var11 = 90;
            } else if (var21 == var17) {
               var11 = 180;
            } else if (var21 == var19) {
               var11 = 270;
            }

            double var23 = var5.getRotation().getYAxis().getMin() + (double)var11;
            if (var23 == 0.0D) {
               var5.getRotation().setYAxis(new IrisAxisRotationClamp(false, false, 0.0D, 0.0D, 90.0D));
               var5.getRotation().setEnabled(var5.getRotation().canRotateX() || var5.getRotation().canRotateZ());
            } else {
               var5.getRotation().setYAxis(new IrisAxisRotationClamp(true, false, var23, var23, 90.0D));
               var5.getRotation().setEnabled(true);
            }
         }
      }

      if (var5.isSmartBore()) {
         this.ensureSmartBored(((IObjectPlacer)var10).isDebugSmartBore());
      }

      boolean var43 = !var5.getWarp().isFlat();
      boolean var42 = var5.getMode().equals(ObjectPlaceMode.STILT) || var5.getMode().equals(ObjectPlaceMode.FAST_STILT) || var5.getMode() == ObjectPlaceMode.MIN_STILT || var5.getMode() == ObjectPlaceMode.FAST_MIN_STILT || var5.getMode() == ObjectPlaceMode.CENTER_STILT;
      KMap var44 = var5.getSnow() > 0.0D ? new KMap() : null;
      int var14 = var6.imax() / 1000;
      int var45 = var6.imax() / 1000;
      int var16 = var6.imax() / 1000;
      int var46 = var5.getRotation().rotate(new BlockVector(0, this.getCenter().getBlockY(), 0), var14, var45, var16).getBlockY();
      int var18 = var5.getTranslate().translate(new BlockVector(0, this.getCenter().getBlockY(), 0), var5.getRotation(), var14, var45, var16).getBlockY();
      int var47 = -1;
      int var22 = var5.getTranslate().getYRandom();
      var22 = var22 > 0 ? var6.i(0, var22) : (var22 < 0 ? var6.i(var22, 0) : var22);
      boolean var49 = false;
      BlockVector var24;
      BlockVector var25;
      int var26;
      int var27;
      int var28;
      int var29;
      int var30;
      int var31;
      int var32;
      int var33;
      if (var5.isFromBottom()) {
         var47 = this.getH() + 1 + var46;
         if (!var5.isForcePlace() && (((IObjectPlacer)var10).isCarved(var1, var47, var3) || ((IObjectPlacer)var10).isCarved(var1, var47 - 1, var3) || ((IObjectPlacer)var10).isCarved(var1, var47 - 2, var3) || ((IObjectPlacer)var10).isCarved(var1, var47 - 3, var3))) {
            var49 = true;
         }
      } else if (var2 < 0) {
         if (!var5.getMode().equals(ObjectPlaceMode.CENTER_HEIGHT) && var5.getMode() != ObjectPlaceMode.CENTER_STILT) {
            int var34;
            if (var5.getMode().equals(ObjectPlaceMode.MAX_HEIGHT) || var5.getMode().equals(ObjectPlaceMode.STILT)) {
               var24 = new BlockVector(var5.getTranslate().getX(), var5.getTranslate().getY(), var5.getTranslate().getZ());
               var25 = var5.getRotation().rotate(new BlockVector(this.getW(), this.getH(), this.getD()), var14, var45, var16).clone();
               var26 = var25.getBlockX() / 2 + var24.getBlockX();
               var27 = Math.min(var1 - var26, var1 + var26);
               var28 = Math.max(var1 - var26, var1 + var26);
               var29 = var25.getBlockZ() / 2 + var24.getBlockZ();
               var30 = Math.min(var3 - var29, var3 + var29);
               var31 = Math.max(var3 - var29, var3 + var29);

               for(var32 = var27; var32 <= var28; ++var32) {
                  for(var33 = var30; var33 <= var31; ++var33) {
                     var34 = ((IObjectPlacer)var10).getHighest(var32, var33, this.getLoader(), var5.isUnderwater()) + var46;
                     if (!var5.isForcePlace() && (((IObjectPlacer)var10).isCarved(var32, var34, var33) || ((IObjectPlacer)var10).isCarved(var32, var34 - 1, var33) || ((IObjectPlacer)var10).isCarved(var32, var34 - 2, var33) || ((IObjectPlacer)var10).isCarved(var32, var34 - 3, var33))) {
                        var49 = true;
                        break;
                     }

                     if (var34 > var47) {
                        var47 = var34;
                     }
                  }
               }
            } else {
               int var35;
               int var36;
               if (var5.getMode().equals(ObjectPlaceMode.FAST_MAX_HEIGHT) || var5.getMode().equals(ObjectPlaceMode.FAST_STILT)) {
                  var24 = new BlockVector(var5.getTranslate().getX(), var5.getTranslate().getY(), var5.getTranslate().getZ());
                  var25 = var5.getRotation().rotate(new BlockVector(this.getW(), this.getH(), this.getD()), var14, var45, var16).clone();
                  var26 = var25.getBlockX() / 2;
                  var27 = var26 + var24.getBlockX();
                  var28 = Math.min(var1 - var27, var1 + var27);
                  var29 = Math.max(var1 - var27, var1 + var27);
                  var30 = var25.getBlockZ() / 2;
                  var31 = var30 + var24.getBlockZ();
                  var32 = Math.min(var3 - var31, var3 + var31);
                  var33 = Math.max(var3 - var31, var3 + var31);

                  for(var34 = var28; var34 <= var29; var34 += Math.abs(var26) + 1) {
                     for(var35 = var32; var35 <= var33; var35 += Math.abs(var30) + 1) {
                        var36 = ((IObjectPlacer)var10).getHighest(var34, var35, this.getLoader(), var5.isUnderwater()) + var46;
                        if (!var5.isForcePlace() && (((IObjectPlacer)var10).isCarved(var34, var36, var35) || ((IObjectPlacer)var10).isCarved(var34, var36 - 1, var35) || ((IObjectPlacer)var10).isCarved(var34, var36 - 2, var35) || ((IObjectPlacer)var10).isCarved(var34, var36 - 3, var35))) {
                           var49 = true;
                           break;
                        }

                        if (var36 > var47) {
                           var47 = var36;
                        }
                     }
                  }
               } else if (var5.getMode().equals(ObjectPlaceMode.MIN_HEIGHT) || var5.getMode() == ObjectPlaceMode.MIN_STILT) {
                  var47 = var9.getEngine().getHeight() + 1;
                  var24 = new BlockVector(var5.getTranslate().getX(), var5.getTranslate().getY(), var5.getTranslate().getZ());
                  var25 = var5.getRotation().rotate(new BlockVector(this.getW(), this.getH(), this.getD()), var14, var45, var16).clone();
                  var26 = var25.getBlockX() / 2 + var24.getBlockX();
                  var27 = Math.min(var1 - var26, var1 + var26);
                  var28 = Math.max(var1 - var26, var1 + var26);
                  var29 = var25.getBlockZ() / 2 + var24.getBlockZ();
                  var30 = Math.min(var3 - var29, var3 + var29);
                  var31 = Math.max(var3 - var29, var3 + var29);

                  for(var32 = var27; var32 <= var28; ++var32) {
                     for(var33 = var30; var33 <= var31; ++var33) {
                        var34 = ((IObjectPlacer)var10).getHighest(var32, var33, this.getLoader(), var5.isUnderwater()) + var46;
                        if (!var5.isForcePlace() && (((IObjectPlacer)var10).isCarved(var32, var34, var33) || ((IObjectPlacer)var10).isCarved(var32, var34 - 1, var33) || ((IObjectPlacer)var10).isCarved(var32, var34 - 2, var33) || ((IObjectPlacer)var10).isCarved(var32, var34 - 3, var33))) {
                           var49 = true;
                           break;
                        }

                        if (var34 < var47) {
                           var47 = var34;
                        }
                     }
                  }
               } else if (var5.getMode().equals(ObjectPlaceMode.FAST_MIN_HEIGHT) || var5.getMode() == ObjectPlaceMode.FAST_MIN_STILT) {
                  var47 = var9.getEngine().getHeight() + 1;
                  var24 = new BlockVector(var5.getTranslate().getX(), var5.getTranslate().getY(), var5.getTranslate().getZ());
                  var25 = var5.getRotation().rotate(new BlockVector(this.getW(), this.getH(), this.getD()), var14, var45, var16).clone();
                  var26 = var25.getBlockX() / 2;
                  var27 = var26 + var24.getBlockX();
                  var28 = Math.min(var1 - var27, var1 + var27);
                  var29 = Math.max(var1 - var27, var1 + var27);
                  var30 = var25.getBlockZ() / 2;
                  var31 = var30 + var24.getBlockZ();
                  var32 = Math.min(var3 - var31, var3 + var31);
                  var33 = Math.max(var3 - var31, var3 + var31);

                  for(var34 = var28; var34 <= var29; var34 += Math.abs(var26) + 1) {
                     for(var35 = var32; var35 <= var33; var35 += Math.abs(var30) + 1) {
                        var36 = ((IObjectPlacer)var10).getHighest(var34, var35, this.getLoader(), var5.isUnderwater()) + var46;
                        if (!var5.isForcePlace() && (((IObjectPlacer)var10).isCarved(var34, var36, var35) || ((IObjectPlacer)var10).isCarved(var34, var36 - 1, var35) || ((IObjectPlacer)var10).isCarved(var34, var36 - 2, var35) || ((IObjectPlacer)var10).isCarved(var34, var36 - 3, var35))) {
                           var49 = true;
                           break;
                        }

                        if (var36 < var47) {
                           var47 = var36;
                        }
                     }
                  }
               } else if (var5.getMode().equals(ObjectPlaceMode.PAINT)) {
                  var47 = ((IObjectPlacer)var10).getHighest(var1, var3, this.getLoader(), var5.isUnderwater()) + var46;
                  if (!var5.isForcePlace() && (((IObjectPlacer)var10).isCarved(var1, var47, var3) || ((IObjectPlacer)var10).isCarved(var1, var47 - 1, var3) || ((IObjectPlacer)var10).isCarved(var1, var47 - 2, var3) || ((IObjectPlacer)var10).isCarved(var1, var47 - 3, var3))) {
                     var49 = true;
                  }
               }
            }
         } else {
            var47 = (var8 != null ? var8.getSurface() : ((IObjectPlacer)var10).getHighest(var1, var3, this.getLoader(), var5.isUnderwater())) + var46;
            if (!var5.isForcePlace() && (((IObjectPlacer)var10).isCarved(var1, var47, var3) || ((IObjectPlacer)var10).isCarved(var1, var47 - 1, var3) || ((IObjectPlacer)var10).isCarved(var1, var47 - 2, var3) || ((IObjectPlacer)var10).isCarved(var1, var47 - 3, var3))) {
               var49 = true;
            }
         }
      } else {
         var47 = var2;
         if (!var5.isForcePlace() && (((IObjectPlacer)var10).isCarved(var1, var2, var3) || ((IObjectPlacer)var10).isCarved(var1, var2 - 1, var3) || ((IObjectPlacer)var10).isCarved(var1, var2 - 2, var3) || ((IObjectPlacer)var10).isCarved(var1, var2 - 3, var3))) {
            var49 = true;
         }
      }

      if (var2 >= 0 && var5.isBottom()) {
         var47 += Math.floorDiv(this.h, 2);
         if (!var5.isForcePlace()) {
            var49 = ((IObjectPlacer)var10).isCarved(var1, var47, var3) || ((IObjectPlacer)var10).isCarved(var1, var47 - 1, var3) || ((IObjectPlacer)var10).isCarved(var1, var47 - 2, var3) || ((IObjectPlacer)var10).isCarved(var1, var47 - 3, var3);
         }
      }

      if (var49 && !var5.isForcePlace()) {
         return -1;
      } else if (var2 < 0 && !var5.isForcePlace() && !var5.isUnderwater() && !var5.isOnwater() && ((IObjectPlacer)var10).isUnderwater(var1, var3)) {
         return -1;
      } else if (!var5.isForcePlace() && var8 != null && Math.max(0, this.h + var22 + var18) + 1 >= var8.getHeight()) {
         return -1;
      } else if (!var5.isForcePlace() && var5.isUnderwater() && var47 + var46 + var18 >= ((IObjectPlacer)var10).getFluidHeight()) {
         return -1;
      } else if (!var5.isForcePlace() && !var5.getClamp().canPlace(var47 + var46 + var18, var47 - var46 + var18)) {
         return -1;
      } else {
         if (!var5.isForcePlace() && (!var5.getAllowedCollisions().isEmpty() || !var5.getForbiddenCollisions().isEmpty())) {
            Engine var50 = var9.getEngine();
            var25 = new BlockVector(var5.getTranslate().getX(), var5.getTranslate().getY(), var5.getTranslate().getZ());

            for(var26 = var1 - Math.floorDiv(this.w, 2) + (int)var25.getX(); var26 <= var1 + Math.floorDiv(this.w, 2) - (this.w % 2 == 0 ? 1 : 0) + (int)var25.getX(); ++var26) {
               for(var27 = var47 - Math.floorDiv(this.h, 2) + (int)var25.getY(); var27 <= var47 + Math.floorDiv(this.h, 2) - (this.h % 2 == 0 ? 1 : 0) + (int)var25.getY(); ++var27) {
                  for(var28 = var3 - Math.floorDiv(this.d, 2) + (int)var25.getZ(); var28 <= var3 + Math.floorDiv(this.d, 2) - (this.d % 2 == 0 ? 1 : 0) + (int)var25.getX(); ++var28) {
                     PlacedObject var61 = var50.getObjectPlacement(var26, var27, var28);
                     if (var61 != null) {
                        IrisObject var66 = var61.getObject();
                        if (var66 != null) {
                           String var68 = var66.getLoadKey();
                           if (var68 != null && var5.getForbiddenCollisions().contains(var68) && !var5.getAllowedCollisions().contains(var68)) {
                              return -1;
                           }
                        }
                     }
                  }
               }
            }
         }

         if (var5.isBore()) {
            var24 = new BlockVector(var5.getTranslate().getX(), var5.getTranslate().getY(), var5.getTranslate().getZ());

            for(int var52 = var1 - Math.floorDiv(this.w, 2) + (int)var24.getX(); var52 <= var1 + Math.floorDiv(this.w, 2) - (this.w % 2 == 0 ? 1 : 0) + (int)var24.getX(); ++var52) {
               for(var26 = var47 - Math.floorDiv(this.h, 2) - var5.getBoreExtendMinY() + (int)var24.getY(); var26 <= var47 + Math.floorDiv(this.h, 2) + var5.getBoreExtendMaxY() - (this.h % 2 == 0 ? 1 : 0) + (int)var24.getY(); ++var26) {
                  for(var27 = var3 - Math.floorDiv(this.d, 2) + (int)var24.getZ(); var27 <= var3 + Math.floorDiv(this.d, 2) - (this.d % 2 == 0 ? 1 : 0) + (int)var24.getX(); ++var27) {
                     ((IObjectPlacer)var10).set(var52, var26, var27, AIR);
                  }
               }
            }
         }

         int var51 = Integer.MAX_VALUE;
         var47 += var22;
         this.readLock.lock();
         KMap var53 = null;

         int var10000;
         int var20;
         int var48;
         Iterator var57;
         BlockVector var62;
         BlockData var64;
         Iterator var69;
         Iterator var75;
         BlockData var78;
         Iterator var80;
         MultipleFacing var84;
         BlockData var85;
         BlockFace var86;
         try {
            if (var5.getMarkers().isNotEmpty() && ((IObjectPlacer)var10).getEngine() != null) {
               var53 = new KMap();
               KList var54 = (KList)StreamSupport.stream(this.blocks.keys().spliterator(), false).collect(KList.collector());
               var57 = var5.getMarkers().iterator();

               label698:
               while(true) {
                  IrisObjectMarker var59;
                  IrisMarker var63;
                  do {
                     if (!var57.hasNext()) {
                        break label698;
                     }

                     var59 = (IrisObjectMarker)var57.next();
                     var63 = (IrisMarker)this.getLoader().getMarkerLoader().load(var59.getMarker());
                  } while(var63 == null);

                  var30 = var59.getMaximumMarkers();
                  var69 = var54.shuffle().iterator();

                  label696:
                  while(var69.hasNext()) {
                     BlockVector var72 = (BlockVector)var69.next();
                     if (var30 <= 0) {
                        break;
                     }

                     BlockData var74 = (BlockData)this.blocks.get(var72);
                     Iterator var77 = var59.getMark(var9).iterator();

                     while(true) {
                        boolean var37;
                        boolean var81;
                        do {
                           while(true) {
                              if (!var77.hasNext()) {
                                 continue label696;
                              }

                              var78 = (BlockData)var77.next();
                              if (var30 <= 0) {
                                 continue label696;
                              }

                              if (var59.isExact()) {
                                 if (var78.matches(var74)) {
                                    break;
                                 }
                              } else if (var78.getMaterial().equals(var74.getMaterial())) {
                                 break;
                              }
                           }

                           var81 = !this.blocks.containsKey((BlockVector)var72.clone().add(new BlockVector(0, 1, 0)));
                           var37 = !this.blocks.containsKey((BlockVector)var72.clone().add(new BlockVector(0, 2, 0)));
                        } while(var63.isEmptyAbove() && (!var81 || !var37));

                        var53.put(var72, var59.getMarker());
                        --var30;
                     }
                  }
               }
            }

            VectorMap.EntryIterator var55 = this.blocks.iterator();

            label655:
            while(true) {
               TileData var67;
               BlockData var73;
               boolean var88;
               do {
                  label618:
                  do {
                     if (!var55.hasNext()) {
                        break label655;
                     }

                     Entry var60 = (Entry)var55.next();
                     var62 = (BlockVector)var60.getKey();
                     var67 = null;

                     try {
                        var64 = (BlockData)var60.getValue();
                        var67 = (TileData)this.states.get(var62);
                     } catch (Throwable var40) {
                        Iris.reportError(var40);
                        var10000 = var62.getBlockX();
                        Iris.warn("Failed to read block node " + var10000 + "," + var62.getBlockY() + "," + var62.getBlockZ() + " in object " + this.getLoadKey() + " (cme)");
                        var64 = AIR;
                     }

                     if (var64 == null) {
                        var10000 = var62.getBlockX();
                        Iris.warn("Failed to read block node " + var10000 + "," + var62.getBlockY() + "," + var62.getBlockZ() + " in object " + this.getLoadKey() + " (null)");
                        var64 = AIR;
                     }

                     BlockVector var70 = var62.clone();
                     var73 = var64.clone();
                     var70 = var5.getRotation().rotate(var70.clone(), var14, var45, var16).clone();
                     var70 = var5.getTranslate().translate(var70.clone(), var5.getRotation(), var14, var45, var16).clone();
                     if (var42 && var70.getBlockY() < var51 && !B.isAir(var73)) {
                        var51 = var70.getBlockY();
                     }

                     if (((IObjectPlacer)var10).isPreventingDecay() && var73 instanceof Leaves && !((Leaves)var73).isPersistent()) {
                        ((Leaves)var73).setPersistent(true);
                     }

                     var75 = var5.getEdit().iterator();

                     label605:
                     while(true) {
                        IrisObjectReplace var79;
                        do {
                           if (!var75.hasNext()) {
                              var73 = var5.getRotation().rotate(var73, var14, var45, var16);
                              var20 = var1 + (int)Math.round(var70.getX());
                              var33 = var47 + (int)Math.round(var70.getY());
                              var48 = var3 + (int)Math.round(var70.getZ());
                              if (var43) {
                                 var20 = (int)((double)var20 + var5.warp(var6, var70.getX() + (double)var1, var70.getY() + (double)var47, var70.getZ() + (double)var3, this.getLoader()));
                                 var48 = (int)((double)var48 + var5.warp(var6, var70.getZ() + (double)var3, var70.getY() + (double)var47, var70.getX() + (double)var1, this.getLoader()));
                              }

                              if (var2 < 0 && var5.getMode().equals(ObjectPlaceMode.PAINT) && !B.isVineBlock(var73)) {
                                 var33 = (int)Math.round(var70.getY()) + Math.floorDiv(this.h, 2) + ((IObjectPlacer)var10).getHighest(var20, var48, this.getLoader(), var5.isUnderwater());
                              }

                              if (var44 != null) {
                                 Position2 var82 = new Position2(var20, var48);
                                 if (!var44.containsKey(var82)) {
                                    var44.put(var82, var33);
                                 }

                                 if ((Integer)var44.get(var82) < var33) {
                                    var44.put(var82, var33);
                                 }
                              }
                              continue label618;
                           }

                           var79 = (IrisObjectReplace)var75.next();
                        } while(!var6.chance((double)var79.getChance()));

                        var80 = var79.getFind(var9).iterator();

                        while(true) {
                           while(true) {
                              if (!var80.hasNext()) {
                                 continue label605;
                              }

                              BlockData var83 = (BlockData)var80.next();
                              if (var79.isExact()) {
                                 if (var83.matches(var73)) {
                                    break;
                                 }
                              } else if (var83.getMaterial().equals(var73.getMaterial())) {
                                 break;
                              }
                           }

                           var85 = var79.getReplace(var6, var70.getX() + (double)var1, var70.getY() + (double)var47, var70.getZ() + (double)var3, var9).clone();
                           if (var85.getMaterial() == var73.getMaterial() && !(var85 instanceof IrisCustomData) && !(var73 instanceof IrisCustomData)) {
                              var73 = var73.merge(var85);
                           } else {
                              var73 = var85;
                           }

                           Optional var38 = var79.getReplace().getTile(var6, (double)var1, (double)var47, (double)var3, var9);
                           if (var38.isPresent()) {
                              var67 = (TileData)var38.get();
                           }
                        }
                     }
                  } while(var5.isMeld() && !((IObjectPlacer)var10).isSolid(var20, var33, var48));

                  if ((var5.isWaterloggable() || var5.isUnderwater()) && var33 <= ((IObjectPlacer)var10).getFluidHeight() && var73 instanceof Waterlogged) {
                     ((Waterlogged)var73).setWaterlogged(true);
                  }

                  if (B.isVineBlock(var73)) {
                     var84 = (MultipleFacing)var73;
                     var80 = var84.getAllowedFaces().iterator();

                     while(var80.hasNext()) {
                        var86 = (BlockFace)var80.next();
                        var85 = ((IObjectPlacer)var10).get(var20 + var86.getModX(), var33 + var86.getModY(), var48 + var86.getModZ());
                        if (B.isSolid(var85) && !B.isVineBlock(var85)) {
                           var84.setFace(var86, true);
                        }
                     }
                  }

                  if (var7 != null) {
                     var7.accept(new BlockPosition(var20, var33, var48), var73);
                  }

                  if (var53 != null && var53.containsKey(var62)) {
                     ((IObjectPlacer)var10).getEngine().getMantle().getMantle().set(var20, var33, var48, (Object)(new MatterMarker((String)var53.get(var62))));
                  }

                  boolean var87 = B.isSolid(((IObjectPlacer)var10).get(var20, var33, var48)) && B.isVineBlock(var73);
                  var88 = !var73.getMaterial().equals(Material.AIR) && !var73.getMaterial().equals(Material.CAVE_AIR) && !var87;
               } while(!(var73 instanceof IrisCustomData) && !var88);

               ((IObjectPlacer)var10).set(var20, var33, var48, var73);
               if (var67 != null) {
                  ((IObjectPlacer)var10).setTile(var20, var33, var48, var67);
               }
            }
         } catch (Throwable var41) {
            var41.printStackTrace();
            Iris.reportError(var41);
         }

         this.readLock.unlock();
         if (var42) {
            this.readLock.lock();
            IrisStiltSettings var56 = var5.getStiltSettings();
            var57 = this.blocks.keys().iterator();

            label572:
            while(true) {
               BlockVector var71;
               do {
                  label547:
                  do {
                     do {
                        if (!var57.hasNext()) {
                           this.readLock.unlock();
                           break label572;
                        }

                        var62 = (BlockVector)var57.next();
                        if (var56 != null && var56.getPalette() != null) {
                           var64 = var5.getStiltSettings().getPalette().get(var6, (double)var1, (double)var47, (double)var3, var9);
                        } else {
                           try {
                              var64 = (BlockData)this.blocks.get(var62);
                           } catch (Throwable var39) {
                              Iris.reportError(var39);
                              var10000 = var62.getBlockX();
                              Iris.warn("Failed to read block node " + var10000 + "," + var62.getBlockY() + "," + var62.getBlockZ() + " in object " + this.getLoadKey() + " (stilt cme)");
                              var64 = AIR;
                           }

                           if (var64 == null) {
                              var10000 = var62.getBlockX();
                              Iris.warn("Failed to read block node " + var10000 + "," + var62.getBlockY() + "," + var62.getBlockZ() + " in object " + this.getLoadKey() + " (stilt null)");
                              var64 = AIR;
                           }
                        }

                        var71 = var62.clone();
                        var71 = var5.getRotation().rotate(var71.clone(), var14, var45, var16).clone();
                        var71 = var5.getTranslate().translate(var71.clone(), var5.getRotation(), var14, var45, var16).clone();
                        var64 = var5.getRotation().rotate(var64, var14, var45, var16);
                     } while(var71.getBlockY() != var51);

                     var69 = var5.getEdit().iterator();

                     label542:
                     while(true) {
                        IrisObjectReplace var76;
                        do {
                           if (!var69.hasNext()) {
                              continue label547;
                           }

                           var76 = (IrisObjectReplace)var69.next();
                        } while(!var6.chance((double)var76.getChance()));

                        var75 = var76.getFind(var9).iterator();

                        while(true) {
                           while(true) {
                              if (!var75.hasNext()) {
                                 continue label542;
                              }

                              BlockData var89 = (BlockData)var75.next();
                              if (var76.isExact()) {
                                 if (var89.matches(var64)) {
                                    break;
                                 }
                              } else if (var89.getMaterial().equals(var64.getMaterial())) {
                                 break;
                              }
                           }

                           var78 = var76.getReplace(var6, var71.getX() + (double)var1, var71.getY() + (double)var47, var71.getZ() + (double)var3, var9).clone();
                           if (var78.getMaterial() == var64.getMaterial()) {
                              var64 = var64.merge(var78);
                           } else {
                              var64 = var78;
                           }
                        }
                     }
                  } while(var64 == null);
               } while(B.isAir(var64));

               var20 = var1 + (int)Math.round(var71.getX());
               var48 = var3 + (int)Math.round(var71.getZ());
               if (var43) {
                  var20 = (int)((double)var20 + var5.warp(var6, var71.getX() + (double)var1, var71.getY() + (double)var47, var71.getZ() + (double)var3, this.getLoader()));
                  var48 = (int)((double)var48 + var5.warp(var6, var71.getZ() + (double)var3, var71.getY() + (double)var47, var71.getX() + (double)var1, this.getLoader()));
               }

               var31 = ((IObjectPlacer)var10).getHighest(var20, var48, this.getLoader(), true);
               if ((var5.isWaterloggable() || var5.isUnderwater()) && var31 <= ((IObjectPlacer)var10).getFluidHeight() && var64 instanceof Waterlogged) {
                  ((Waterlogged)var64).setWaterlogged(true);
               }

               if (var2 >= 0 && var5.isBottom()) {
                  var47 += Math.floorDiv(this.h, 2);
               }

               var32 = var31 - 1;
               if (var56 != null) {
                  var32 -= var5.getStiltSettings().getOverStilt() - var6.i(0, var5.getStiltSettings().getYRand());
                  if (var56.getYMax() != 0) {
                     var32 -= Math.min(var5.getStiltSettings().getYMax() - (var51 + var47 - var31), 0);
                  }
               }

               for(var33 = var51 + var47; var33 > var32; --var33) {
                  if (B.isVineBlock(var64)) {
                     var84 = (MultipleFacing)var64;
                     var80 = var84.getAllowedFaces().iterator();

                     while(var80.hasNext()) {
                        var86 = (BlockFace)var80.next();
                        var85 = ((IObjectPlacer)var10).get(var20 + var86.getModX(), var33 + var86.getModY(), var48 + var86.getModZ());
                        if (B.isSolid(var85) && !B.isVineBlock(var85)) {
                           var84.setFace(var86, true);
                        }
                     }
                  }

                  ((IObjectPlacer)var10).set(var20, var33, var48, var64);
               }
            }
         }

         if (var44 != null) {
            RNG var58 = var6.nextParallelRNG(3468854);
            var57 = var44.k().iterator();

            while(var57.hasNext()) {
               Position2 var65 = (Position2)var57.next();
               var29 = var65.getX();
               var30 = (Integer)var44.get(var65);
               var31 = var65.getZ();
               if (var5.getSnow() > 0.0D) {
                  var32 = var58.i(0, (int)(var5.getSnow() * 7.0D));
                  ((IObjectPlacer)var10).set(var29, var30 + 1, var31, SNOW_LAYERS[Math.max(Math.min(var32, 7), 0)]);
               }
            }
         }

         return var47;
      }
   }

   public IrisObject rotateCopy(IrisObjectRotation rt) {
      IrisObject var2 = this.copy();
      var2.rotate(var1, 0, 0, 0);
      return var2;
   }

   public void rotate(IrisObjectRotation r, int spinx, int spiny, int spinz) {
      this.writeLock.lock();
      VectorMap var5 = new VectorMap();
      VectorMap.EntryIterator var6 = this.blocks.iterator();

      while(var6.hasNext()) {
         Entry var7 = (Entry)var6.next();
         var5.put(var1.rotate((BlockVector)var7.getKey(), var2, var3, var4), var1.rotate((BlockData)var7.getValue(), var2, var3, var4));
      }

      VectorMap var9 = new VectorMap();
      VectorMap.EntryIterator var10 = this.states.iterator();

      while(var10.hasNext()) {
         Entry var8 = (Entry)var10.next();
         var9.put(var1.rotate((BlockVector)var8.getKey(), var2, var3, var4), (TileData)var8.getValue());
      }

      this.blocks = var5;
      this.states = var9;
      this.shrinkwrap();
      this.writeLock.unlock();
   }

   public void place(Location at) {
      this.readLock.lock();
      VectorMap.EntryIterator var2 = this.blocks.iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         BlockVector var4 = (BlockVector)var3.getKey();
         Block var5 = var1.clone().add(0.0D, this.getCenter().getY(), 0.0D).add(var4).getBlock();
         var5.setBlockData((BlockData)Objects.requireNonNull((BlockData)var3.getValue()), false);
         if (this.states.containsKey(var4)) {
            Iris.info(((TileData)Objects.requireNonNull((TileData)this.states.get(var4))).toString());
            ((TileData)Objects.requireNonNull((TileData)this.states.get(var4))).toBukkitTry(var5);
         }
      }

      this.readLock.unlock();
   }

   public void placeCenterY(Location at) {
      this.readLock.lock();
      VectorMap.EntryIterator var2 = this.blocks.iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         BlockVector var4 = (BlockVector)var3.getKey();
         Block var5 = var1.clone().add(this.getCenter().getX(), this.getCenter().getY(), this.getCenter().getZ()).add(var4).getBlock();
         var5.setBlockData((BlockData)Objects.requireNonNull((BlockData)var3.getValue()), false);
         if (this.states.containsKey(var4)) {
            ((TileData)Objects.requireNonNull((TileData)this.states.get(var4))).toBukkitTry(var5);
         }
      }

      this.readLock.unlock();
   }

   public void unplaceCenterY(Location at) {
      this.readLock.lock();
      Iterator var2 = this.blocks.keys().iterator();

      while(var2.hasNext()) {
         BlockVector var3 = (BlockVector)var2.next();
         var1.clone().add(this.getCenter().getX(), this.getCenter().getY(), this.getCenter().getZ()).add(var3).getBlock().setBlockData(AIR, false);
      }

      this.readLock.unlock();
   }

   public IrisObject scaled(double scale, IrisObjectPlacementScaleInterpolator interpolation) {
      Vector var4 = new Vector(var1 - 1.0D, var1 - 1.0D, var1 - 1.0D);
      var1 = Math.max(0.001D, Math.min(50.0D, var1));
      if (var1 < 1.0D) {
         var1 -= 1.0E-4D;
      }

      IrisPosition var5 = this.getAABB().max();
      IrisPosition var6 = this.getAABB().min();
      VectorMap var7 = new VectorMap();
      Object var8 = this.getCenter();
      if (this.getH() == 2) {
         var8 = ((Vector)var8).setY((double)((Vector)var8).getBlockY() + 0.5D);
      }

      if (this.getW() == 2) {
         var8 = ((Vector)var8).setX((double)((Vector)var8).getBlockX() + 0.5D);
      }

      if (this.getD() == 2) {
         var8 = ((Vector)var8).setZ((double)((Vector)var8).getBlockZ() + 0.5D);
      }

      IrisObject var9 = new IrisObject((int)Math.ceil((double)this.w * var1 + var1 * 2.0D), (int)Math.ceil((double)this.h * var1 + var1 * 2.0D), (int)Math.ceil((double)this.d * var1 + var1 * 2.0D));
      this.readLock.lock();
      VectorMap.EntryIterator var10 = this.blocks.iterator();

      Entry var11;
      while(var10.hasNext()) {
         var11 = (Entry)var10.next();
         BlockData var12 = (BlockData)var11.getValue();
         var7.put(((BlockVector)var11.getKey()).clone().add(HALF).subtract((Vector)var8).multiply(var1).add(var4).toBlockVector(), var12);
      }

      this.readLock.unlock();
      var10 = var7.iterator();

      while(true) {
         while(var10.hasNext()) {
            var11 = (Entry)var10.next();
            BlockVector var15 = (BlockVector)var11.getKey();
            if (var1 > 1.0D) {
               Iterator var13 = blocksBetweenTwoPoints(var15.clone().add((Vector)var8), var15.clone().add((Vector)var8).add(var4)).iterator();

               while(var13.hasNext()) {
                  BlockVector var14 = (BlockVector)var13.next();
                  var9.blocks.put(var14, (BlockData)var11.getValue());
               }
            } else {
               var9.setUnsigned(var15.getBlockX(), var15.getBlockY(), var15.getBlockZ(), (BlockData)var11.getValue());
            }
         }

         if (var1 > 1.0D) {
            switch(var3) {
            case TRILINEAR:
               var9.trilinear((int)Math.round(var1));
               break;
            case TRICUBIC:
               var9.tricubic((int)Math.round(var1));
               break;
            case TRIHERMITE:
               var9.trihermite((int)Math.round(var1));
            }
         }

         return var9;
      }
   }

   public void trilinear(int rad) {
      this.writeLock.lock();
      VectorMap var2 = this.blocks;
      VectorMap var3 = new VectorMap();
      BlockVector var4 = this.getAABB().minbv();
      BlockVector var5 = this.getAABB().maxbv();

      for(int var6 = var4.getBlockX(); var6 <= var5.getBlockX(); ++var6) {
         for(int var7 = var4.getBlockY(); var7 <= var5.getBlockY(); ++var7) {
            for(int var8 = var4.getBlockZ(); var8 <= var5.getBlockZ(); ++var8) {
               if (IrisInterpolation.getTrilinear(var6, var7, var8, (double)var1, (var1x, var3x, var5x) -> {
                  BlockData var7 = (BlockData)var2.get(new BlockVector((int)var1x, (int)var3x, (int)var5x));
                  return var7 != null && !var7.getMaterial().isAir() ? 1.0D : 0.0D;
               }) >= 0.5D) {
                  var3.put(new BlockVector(var6, var7, var8), this.nearestBlockData(var6, var7, var8));
               } else {
                  var3.put(new BlockVector(var6, var7, var8), AIR);
               }
            }
         }
      }

      this.blocks = var3;
      this.writeLock.unlock();
   }

   public void tricubic(int rad) {
      this.writeLock.lock();
      VectorMap var2 = this.blocks;
      VectorMap var3 = new VectorMap();
      BlockVector var4 = this.getAABB().minbv();
      BlockVector var5 = this.getAABB().maxbv();

      for(int var6 = var4.getBlockX(); var6 <= var5.getBlockX(); ++var6) {
         for(int var7 = var4.getBlockY(); var7 <= var5.getBlockY(); ++var7) {
            for(int var8 = var4.getBlockZ(); var8 <= var5.getBlockZ(); ++var8) {
               if (IrisInterpolation.getTricubic(var6, var7, var8, (double)var1, (var1x, var3x, var5x) -> {
                  BlockData var7 = (BlockData)var2.get(new BlockVector((int)var1x, (int)var3x, (int)var5x));
                  return var7 != null && !var7.getMaterial().isAir() ? 1.0D : 0.0D;
               }) >= 0.5D) {
                  var3.put(new BlockVector(var6, var7, var8), this.nearestBlockData(var6, var7, var8));
               } else {
                  var3.put(new BlockVector(var6, var7, var8), AIR);
               }
            }
         }
      }

      this.blocks = var3;
      this.writeLock.unlock();
   }

   public void trihermite(int rad) {
      this.trihermite(var1, 0.0D, 0.0D);
   }

   public void trihermite(int rad, double tension, double bias) {
      this.writeLock.lock();
      VectorMap var6 = this.blocks;
      VectorMap var7 = new VectorMap();
      BlockVector var8 = this.getAABB().minbv();
      BlockVector var9 = this.getAABB().maxbv();

      for(int var10 = var8.getBlockX(); var10 <= var9.getBlockX(); ++var10) {
         for(int var11 = var8.getBlockY(); var11 <= var9.getBlockY(); ++var11) {
            for(int var12 = var8.getBlockZ(); var12 <= var9.getBlockZ(); ++var12) {
               if (IrisInterpolation.getTrihermite(var10, var11, var12, (double)var1, (var1x, var3, var5) -> {
                  BlockData var7 = (BlockData)var6.get(new BlockVector((int)var1x, (int)var3, (int)var5));
                  return var7 != null && !var7.getMaterial().isAir() ? 1.0D : 0.0D;
               }, var2, var4) >= 0.5D) {
                  var7.put(new BlockVector(var10, var11, var12), this.nearestBlockData(var10, var11, var12));
               } else {
                  var7.put(new BlockVector(var10, var11, var12), AIR);
               }
            }
         }
      }

      this.blocks = var7;
      this.writeLock.unlock();
   }

   private BlockData nearestBlockData(int x, int y, int z) {
      BlockVector var4 = new BlockVector(var1, var2, var3);
      this.readLock.lock();
      BlockData var5 = (BlockData)this.blocks.get(var4);
      if (var5 != null && !var5.getMaterial().isAir()) {
         return var5;
      } else {
         double var6 = Double.MAX_VALUE;
         VectorMap.EntryIterator var8 = this.blocks.iterator();

         while(var8.hasNext()) {
            Entry var9 = (Entry)var8.next();
            BlockData var10 = (BlockData)var9.getValue();
            if (!var10.getMaterial().isAir()) {
               double var11 = ((BlockVector)var9.getKey()).distanceSquared(var4);
               if (var11 < var6) {
                  var6 = var11;
                  var5 = var10;
               }
            }
         }

         this.readLock.unlock();
         return var5;
      }
   }

   public int volume() {
      return this.blocks.size();
   }

   public String getFolderName() {
      return "objects";
   }

   public String getTypeName() {
      return "Object";
   }

   public void scanForErrors(JSONObject p, VolmitSender sender) {
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisObject)) {
         return false;
      } else {
         IrisObject var2 = (IrisObject)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getW() != var2.getW()) {
            return false;
         } else if (this.getD() != var2.getD()) {
            return false;
         } else if (this.getH() != var2.getH()) {
            return false;
         } else {
            VectorMap var3 = this.getBlocks();
            VectorMap var4 = var2.getBlocks();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            VectorMap var5 = this.getStates();
            VectorMap var6 = var2.getStates();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisObject;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var5 = var2 * 59 + this.getW();
      var5 = var5 * 59 + this.getD();
      var5 = var5 * 59 + this.getH();
      VectorMap var3 = this.getBlocks();
      var5 = var5 * 59 + (var3 == null ? 43 : var3.hashCode());
      VectorMap var4 = this.getStates();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public boolean isSmartBored() {
      return this.smartBored;
   }

   @Generated
   public IrisObject setSmartBored(final boolean smartBored) {
      this.smartBored = var1;
      return this;
   }

   @Generated
   public IrisObject setAabb(final AtomicCache<AxisAlignedBB> aabb) {
      this.aabb = var1;
      return this;
   }

   @Generated
   public VectorMap<BlockData> getBlocks() {
      return this.blocks;
   }

   @Generated
   public VectorMap<TileData> getStates() {
      return this.states;
   }

   @Generated
   public int getW() {
      return this.w;
   }

   @Generated
   public IrisObject setW(final int w) {
      this.w = var1;
      return this;
   }

   @Generated
   public int getD() {
      return this.d;
   }

   @Generated
   public IrisObject setD(final int d) {
      this.d = var1;
      return this;
   }

   @Generated
   public int getH() {
      return this.h;
   }

   @Generated
   public IrisObject setH(final int h) {
      this.h = var1;
      return this;
   }

   @Generated
   public Vector3i getCenter() {
      return this.center;
   }

   @Generated
   public IrisObject setCenter(final Vector3i center) {
      this.center = var1;
      return this;
   }

   @Generated
   public Vector3i getShrinkOffset() {
      return this.shrinkOffset;
   }

   private static class HeaderException extends IOException {
      public HeaderException() {
         super("Invalid Header");
      }
   }
}
