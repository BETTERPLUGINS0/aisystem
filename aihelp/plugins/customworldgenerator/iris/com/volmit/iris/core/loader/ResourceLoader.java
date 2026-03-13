package com.volmit.iris.core.loader;

import com.google.common.util.concurrent.AtomicDouble;
import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.project.SchemaBuilder;
import com.volmit.iris.core.service.PreservationSVC;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.MeteredCache;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.data.KCache;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.io.CustomOutputStream;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.json.JSONArray;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.parallel.BurstExecutor;
import com.volmit.iris.util.parallel.MultiBurst;
import com.volmit.iris.util.scheduling.ChronoLatch;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import lombok.Generated;

public class ResourceLoader<T extends IrisRegistrant> implements MeteredCache {
   public static final AtomicDouble tlt = new AtomicDouble(0.0D);
   private static final int CACHE_SIZE = 100000;
   protected final AtomicCache<KList<File>> folderCache;
   protected KSet<String> firstAccess;
   protected File root;
   protected String folderName;
   protected String resourceTypeName;
   protected KCache<String, T> loadCache;
   protected Class<? extends T> objectClass;
   protected String cname;
   protected String[] possibleKeys = null;
   protected IrisData manager;
   protected AtomicInteger loads;
   protected ChronoLatch sec;

   public ResourceLoader(File root, IrisData manager, String folderName, String resourceTypeName, Class<? extends T> objectClass) {
      this.manager = var2;
      this.firstAccess = new KSet(new String[0]);
      this.folderCache = new AtomicCache();
      this.sec = new ChronoLatch(5000L);
      this.loads = new AtomicInteger();
      this.objectClass = var5;
      this.cname = var5.getCanonicalName();
      this.resourceTypeName = var4;
      this.root = var1;
      this.folderName = var3;
      this.loadCache = new KCache(this::loadRaw, (long)IrisSettings.get().getPerformance().getResourceLoaderCacheSize());
      String var10000 = String.valueOf(C.GREEN);
      Iris.debug("Loader<" + var10000 + var4 + String.valueOf(C.LIGHT_PURPLE) + "> created in " + String.valueOf(C.RED) + "IDM/" + var2.getId() + String.valueOf(C.LIGHT_PURPLE) + " on " + String.valueOf(C.GRAY) + var2.getDataFolder().getPath());
      ((PreservationSVC)Iris.service(PreservationSVC.class)).registerCache(this);
   }

   public JSONObject buildSchema() {
      String var10000 = this.objectClass.getSimpleName();
      Iris.debug("Building Schema " + var10000 + " " + this.root.getPath());
      JSONObject var1 = new JSONObject();
      KList var2 = new KList();

      for(int var3 = 1; var3 < 8; ++var3) {
         String var10001 = this.folderName;
         var2.add((Object)("/" + var10001 + Form.repeat("/*", var3) + ".json"));
      }

      var1.put("fileMatch", (Object)(new JSONArray(var2.toArray())));
      var1.put("url", (Object)("./.iris/schema/" + this.getFolderName() + "-schema.json"));
      File var4 = new File(this.getManager().getDataFolder(), ".iris/schema/" + this.getFolderName() + "-schema.json");
      J.attemptAsync(() -> {
         IO.writeAll(var4, (Object)(new SchemaBuilder(this.objectClass, this.manager)).construct().toString(4));
      });
      return var1;
   }

   public File findFile(String name) {
      Iterator var2 = this.getFolders(var1).iterator();

      File var8;
      do {
         if (!var2.hasNext()) {
            Iris.warn("Couldn't find " + this.resourceTypeName + ": " + var1);
            return null;
         }

         File var3 = (File)var2.next();
         File[] var4 = var3.listFiles();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File var7 = var4[var6];
            if (var7.isFile() && var7.getName().endsWith(".json") && var7.getName().split("\\Q.\\E")[0].equals(var1)) {
               return var7;
            }
         }

         var8 = new File(var3, var1 + ".json");
      } while(!var8.exists());

      return var8;
   }

   public void logLoad(File path, T t) {
      this.loads.getAndIncrement();
      if (this.loads.get() == 1) {
         this.sec.flip();
      }

      if (this.sec.flip()) {
         J.a(() -> {
            String var10000 = String.valueOf(C.WHITE);
            Iris.verbose("Loaded " + var10000 + this.loads.get() + " " + this.resourceTypeName + (this.loads.get() == 1 ? "" : "s") + String.valueOf(C.GRAY) + " (" + Form.f(this.getLoadCache().getSize()) + " " + this.resourceTypeName + (this.loadCache.getSize() == 1L ? "" : "s") + " Loaded)");
            this.loads.set(0);
         });
      }

      String var10000 = String.valueOf(C.GREEN);
      Iris.debug("Loader<" + var10000 + this.resourceTypeName + String.valueOf(C.LIGHT_PURPLE) + "> iload " + String.valueOf(C.YELLOW) + var2.getLoadKey() + String.valueOf(C.LIGHT_PURPLE) + " in " + String.valueOf(C.GRAY) + var2.getLoadFile().getPath() + String.valueOf(C.LIGHT_PURPLE) + " TLT: " + String.valueOf(C.RED) + Form.duration(tlt.get(), 2));
   }

   public void failLoad(File path, Throwable e) {
      J.a(() -> {
         String var10000 = this.resourceTypeName;
         Iris.warn("Couldn't Load " + var10000 + " file: " + var1.getPath() + ": " + var2.getMessage());
      });
   }

   private KList<File> matchAllFiles(File root, Predicate<File> f) {
      KList var3 = new KList();
      this.matchFiles(var1, var3, var2);
      return var3;
   }

   private void matchFiles(File at, KList<File> files, Predicate<File> f) {
      if (var1.isDirectory()) {
         File[] var4 = var1.listFiles();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File var7 = var4[var6];
            this.matchFiles(var7, var2, var3);
         }
      } else if (var3.test(var1)) {
         var2.add((Object)var1);
      }

   }

   public String[] getPossibleKeys() {
      if (this.possibleKeys != null) {
         return this.possibleKeys;
      } else {
         KList var1 = this.getFolders();
         if (var1 == null) {
            this.possibleKeys = new String[0];
            return this.possibleKeys;
         } else {
            HashSet var2 = new HashSet();
            Iterator var3 = var1.iterator();

            while(var3.hasNext()) {
               File var4 = (File)var3.next();
               Iterator var5 = this.matchAllFiles(var4, (var0) -> {
                  return var0.getName().endsWith(".json");
               }).iterator();

               while(var5.hasNext()) {
                  File var6 = (File)var5.next();
                  var2.add(var4.toURI().relativize(var6.toURI()).getPath().replaceAll("\\Q.json\\E", ""));
               }
            }

            KList var7 = new KList(var2);
            this.possibleKeys = (String[])var7.toArray(new String[0]);
            return this.possibleKeys;
         }
      }
   }

   public long count() {
      return this.loadCache.getSize();
   }

   protected T loadFile(File j, String name) {
      try {
         PrecisionStopwatch var3 = PrecisionStopwatch.start();
         IrisRegistrant var4 = (IrisRegistrant)this.getManager().getGson().fromJson(this.preprocess(new JSONObject(IO.readAll(var1))).toString(0), this.objectClass);
         var4.setLoadKey(var2);
         var4.setLoadFile(var1);
         var4.setLoader(this.manager);
         this.getManager().preprocessObject(var4);
         this.logLoad(var1, var4);
         tlt.addAndGet(var3.getMilliseconds());
         return var4;
      } catch (Throwable var5) {
         Iris.reportError(var5);
         this.failLoad(var1, var5);
         return null;
      }
   }

   protected JSONObject preprocess(JSONObject j) {
      return var1;
   }

   public Stream<T> streamAll() {
      return this.streamAll(Arrays.stream(this.getPossibleKeys()));
   }

   public Stream<T> streamAll(Stream<String> s) {
      return var1.map(this::load);
   }

   public KList<T> loadAll(KList<String> s) {
      KList var2 = new KList();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         IrisRegistrant var5 = this.load(var4);
         if (var5 != null) {
            var2.add((Object)var5);
         }
      }

      return var2;
   }

   public KList<T> loadAllParallel(KList<String> s) {
      KList var2 = new KList();
      BurstExecutor var3 = MultiBurst.ioBurst.burst(var1.size());
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         var3.queue(() -> {
            IrisRegistrant var3 = this.load(var5);
            if (var3 != null) {
               synchronized(var2) {
                  var2.add((Object)var3);
               }
            }
         });
      }

      var3.complete();
      return var2;
   }

   public KList<T> loadAll(KList<String> s, Consumer<T> postLoad) {
      KList var3 = new KList();
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         IrisRegistrant var6 = this.load(var5);
         if (var6 != null) {
            var3.add((Object)var6);
            var2.accept(var6);
         }
      }

      return var3;
   }

   public KList<T> loadAll(String[] s) {
      KList var2 = new KList();
      String[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         IrisRegistrant var7 = this.load(var6);
         if (var7 != null) {
            var2.add((Object)var7);
         }
      }

      return var2;
   }

   public T load(String name) {
      return this.load(var1, true);
   }

   private T loadRaw(String name) {
      Iterator var2 = this.getFolders(var1).iterator();

      File var8;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         File var3 = (File)var2.next();
         File[] var4 = var3.listFiles();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File var7 = var4[var6];
            if (var7.isFile() && var7.getName().endsWith(".json") && var7.getName().split("\\Q.\\E")[0].equals(var1)) {
               return this.loadFile(var7, var1);
            }
         }

         var8 = new File(var3, var1 + ".json");
      } while(!var8.exists());

      return this.loadFile(var8, var1);
   }

   public T load(String name, boolean warn) {
      if (var1 == null) {
         return null;
      } else if (var1.trim().isEmpty()) {
         return null;
      } else {
         KSet var3 = this.firstAccess;
         if (var3 != null) {
            this.firstAccess.add(var1);
         }

         return (IrisRegistrant)this.loadCache.get(var1);
      }
   }

   public void loadFirstAccess(Engine engine) {
      long var10000 = Math.abs(var1.getSeedManager().getSeed() + (long)var1.getDimension().getVersion() + (long)var1.getDimension().getLoadKey().hashCode());
      String var2 = "DIM" + var10000;
      File var3 = Iris.instance.getDataFile(new String[]{"prefetch/" + var2 + "/" + Math.abs(this.getFolderName().hashCode()) + ".ipfch"});
      if (var3.exists()) {
         FileInputStream var4 = new FileInputStream(var3);
         GZIPInputStream var5 = new GZIPInputStream(var4);
         DataInputStream var6 = new DataInputStream(var5);
         int var7 = var6.readInt();
         KList var8 = new KList();

         for(int var9 = 0; var9 < var7; ++var9) {
            var8.add((Object)var6.readUTF());
         }

         var6.close();
         Iris.info("Loading " + var8.size() + " prefetch " + this.getFolderName());
         this.firstAccess = null;
         this.loadAllParallel(var8);
      }
   }

   public void saveFirstAccess(Engine engine) {
      if (this.firstAccess != null) {
         long var10000 = Math.abs(var1.getSeedManager().getSeed() + (long)var1.getDimension().getVersion() + (long)var1.getDimension().getLoadKey().hashCode());
         String var2 = "DIM" + var10000;
         File var3 = Iris.instance.getDataFile(new String[]{"prefetch/" + var2 + "/" + Math.abs(this.getFolderName().hashCode()) + ".ipfch"});
         var3.getParentFile().mkdirs();
         FileOutputStream var4 = new FileOutputStream(var3);
         CustomOutputStream var5 = new CustomOutputStream(var4, 9);
         DataOutputStream var6 = new DataOutputStream(var5);
         KSet var7 = this.firstAccess;
         this.firstAccess = null;
         var6.writeInt(var7.size());
         Iterator var8 = var7.iterator();

         while(var8.hasNext()) {
            String var9 = (String)var8.next();
            var6.writeUTF(var9);
         }

         var6.flush();
         var6.close();
      }
   }

   public KList<File> getFolders() {
      return (KList)this.folderCache.aquire(() -> {
         KList var1 = new KList();
         File[] var2 = this.root.listFiles();
         if (var2 == null) {
            throw new IllegalStateException("Failed to list files in " + String.valueOf(this.root));
         } else {
            File[] var3 = var2;
            int var4 = var2.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               File var6 = var3[var5];
               if (var6.isDirectory() && var6.getName().equals(this.folderName)) {
                  var1.add((Object)var6);
                  break;
               }
            }

            return var1;
         }
      });
   }

   public KList<File> getFolders(String rc) {
      KList var2 = this.getFolders().copy();
      if (var1.contains(":")) {
         Iterator var3 = var2.copy().iterator();

         while(var3.hasNext()) {
            File var4 = (File)var3.next();
            if (!var1.startsWith(var4.getName() + ":")) {
               var2.remove(var4);
            }
         }
      }

      return var2;
   }

   public void clearCache() {
      this.possibleKeys = null;
      this.loadCache.invalidate();
      this.folderCache.reset();
   }

   public File fileFor(T b) {
      Iterator var2 = this.getFolders().iterator();

      File var8;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         File var3 = (File)var2.next();
         File[] var4 = var3.listFiles();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File var7 = var4[var6];
            if (var7.isFile() && var7.getName().endsWith(".json") && var7.getName().split("\\Q.\\E")[0].equals(var1.getLoadKey())) {
               return var7;
            }
         }

         var8 = new File(var3, var1.getLoadKey() + ".json");
      } while(!var8.exists());

      return var8;
   }

   public boolean isLoaded(String next) {
      return this.loadCache.contains(var1);
   }

   public void clearList() {
      this.folderCache.reset();
      this.possibleKeys = null;
   }

   public KList<String> getPossibleKeys(String arg) {
      KList var2 = new KList();
      String[] var3 = this.getPossibleKeys();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         if (var6.equalsIgnoreCase(var1) || var6.toLowerCase(Locale.ROOT).startsWith(var1.toLowerCase(Locale.ROOT)) || var6.toLowerCase(Locale.ROOT).contains(var1.toLowerCase(Locale.ROOT)) || var1.toLowerCase(Locale.ROOT).contains(var6.toLowerCase(Locale.ROOT))) {
            var2.add((Object)var6);
         }
      }

      return var2;
   }

   public boolean supportsSchemas() {
      return true;
   }

   public void clean() {
   }

   public long getSize() {
      return this.loadCache.getSize();
   }

   public KCache<?, ?> getRawCache() {
      return this.loadCache;
   }

   public long getMaxSize() {
      return this.loadCache.getMaxSize();
   }

   public boolean isClosed() {
      return this.getManager().isClosed();
   }

   public long getTotalStorage() {
      return this.getSize();
   }

   @Generated
   public AtomicCache<KList<File>> getFolderCache() {
      return this.folderCache;
   }

   @Generated
   public KSet<String> getFirstAccess() {
      return this.firstAccess;
   }

   @Generated
   public File getRoot() {
      return this.root;
   }

   @Generated
   public String getFolderName() {
      return this.folderName;
   }

   @Generated
   public String getResourceTypeName() {
      return this.resourceTypeName;
   }

   @Generated
   public KCache<String, T> getLoadCache() {
      return this.loadCache;
   }

   @Generated
   public Class<? extends T> getObjectClass() {
      return this.objectClass;
   }

   @Generated
   public String getCname() {
      return this.cname;
   }

   @Generated
   public IrisData getManager() {
      return this.manager;
   }

   @Generated
   public AtomicInteger getLoads() {
      return this.loads;
   }

   @Generated
   public ChronoLatch getSec() {
      return this.sec;
   }

   @Generated
   public void setFirstAccess(final KSet<String> firstAccess) {
      this.firstAccess = var1;
   }

   @Generated
   public void setRoot(final File root) {
      this.root = var1;
   }

   @Generated
   public void setFolderName(final String folderName) {
      this.folderName = var1;
   }

   @Generated
   public void setResourceTypeName(final String resourceTypeName) {
      this.resourceTypeName = var1;
   }

   @Generated
   public void setLoadCache(final KCache<String, T> loadCache) {
      this.loadCache = var1;
   }

   @Generated
   public void setObjectClass(final Class<? extends T> objectClass) {
      this.objectClass = var1;
   }

   @Generated
   public void setCname(final String cname) {
      this.cname = var1;
   }

   @Generated
   public void setPossibleKeys(final String[] possibleKeys) {
      this.possibleKeys = var1;
   }

   @Generated
   public void setManager(final IrisData manager) {
      this.manager = var1;
   }

   @Generated
   public void setLoads(final AtomicInteger loads) {
      this.loads = var1;
   }

   @Generated
   public void setSec(final ChronoLatch sec) {
      this.sec = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof ResourceLoader)) {
         return false;
      } else {
         ResourceLoader var2 = (ResourceLoader)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            label135: {
               AtomicCache var3 = this.getFolderCache();
               AtomicCache var4 = var2.getFolderCache();
               if (var3 == null) {
                  if (var4 == null) {
                     break label135;
                  }
               } else if (var3.equals(var4)) {
                  break label135;
               }

               return false;
            }

            KSet var5 = this.getFirstAccess();
            KSet var6 = var2.getFirstAccess();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            label121: {
               File var7 = this.getRoot();
               File var8 = var2.getRoot();
               if (var7 == null) {
                  if (var8 == null) {
                     break label121;
                  }
               } else if (var7.equals(var8)) {
                  break label121;
               }

               return false;
            }

            String var9 = this.getFolderName();
            String var10 = var2.getFolderName();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            label107: {
               String var11 = this.getResourceTypeName();
               String var12 = var2.getResourceTypeName();
               if (var11 == null) {
                  if (var12 == null) {
                     break label107;
                  }
               } else if (var11.equals(var12)) {
                  break label107;
               }

               return false;
            }

            KCache var13 = this.getLoadCache();
            KCache var14 = var2.getLoadCache();
            if (var13 == null) {
               if (var14 != null) {
                  return false;
               }
            } else if (!var13.equals(var14)) {
               return false;
            }

            label93: {
               Class var15 = this.getObjectClass();
               Class var16 = var2.getObjectClass();
               if (var15 == null) {
                  if (var16 == null) {
                     break label93;
                  }
               } else if (var15.equals(var16)) {
                  break label93;
               }

               return false;
            }

            label86: {
               String var17 = this.getCname();
               String var18 = var2.getCname();
               if (var17 == null) {
                  if (var18 == null) {
                     break label86;
                  }
               } else if (var17.equals(var18)) {
                  break label86;
               }

               return false;
            }

            if (!Arrays.deepEquals(this.getPossibleKeys(), var2.getPossibleKeys())) {
               return false;
            } else {
               AtomicInteger var19 = this.getLoads();
               AtomicInteger var20 = var2.getLoads();
               if (var19 == null) {
                  if (var20 != null) {
                     return false;
                  }
               } else if (!var19.equals(var20)) {
                  return false;
               }

               ChronoLatch var21 = this.getSec();
               ChronoLatch var22 = var2.getSec();
               if (var21 == null) {
                  if (var22 != null) {
                     return false;
                  }
               } else if (!var21.equals(var22)) {
                  return false;
               }

               return true;
            }
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof ResourceLoader;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      AtomicCache var3 = this.getFolderCache();
      int var13 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      KSet var4 = this.getFirstAccess();
      var13 = var13 * 59 + (var4 == null ? 43 : var4.hashCode());
      File var5 = this.getRoot();
      var13 = var13 * 59 + (var5 == null ? 43 : var5.hashCode());
      String var6 = this.getFolderName();
      var13 = var13 * 59 + (var6 == null ? 43 : var6.hashCode());
      String var7 = this.getResourceTypeName();
      var13 = var13 * 59 + (var7 == null ? 43 : var7.hashCode());
      KCache var8 = this.getLoadCache();
      var13 = var13 * 59 + (var8 == null ? 43 : var8.hashCode());
      Class var9 = this.getObjectClass();
      var13 = var13 * 59 + (var9 == null ? 43 : var9.hashCode());
      String var10 = this.getCname();
      var13 = var13 * 59 + (var10 == null ? 43 : var10.hashCode());
      var13 = var13 * 59 + Arrays.deepHashCode(this.getPossibleKeys());
      AtomicInteger var11 = this.getLoads();
      var13 = var13 * 59 + (var11 == null ? 43 : var11.hashCode());
      ChronoLatch var12 = this.getSec();
      var13 = var13 * 59 + (var12 == null ? 43 : var12.hashCode());
      return var13;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getFolderCache());
      return "ResourceLoader(folderCache=" + var10000 + ", firstAccess=" + String.valueOf(this.getFirstAccess()) + ", root=" + String.valueOf(this.getRoot()) + ", folderName=" + this.getFolderName() + ", resourceTypeName=" + this.getResourceTypeName() + ", loadCache=" + String.valueOf(this.getLoadCache()) + ", objectClass=" + String.valueOf(this.getObjectClass()) + ", cname=" + this.getCname() + ", possibleKeys=" + Arrays.deepToString(this.getPossibleKeys()) + ", loads=" + String.valueOf(this.getLoads()) + ", sec=" + String.valueOf(this.getSec()) + ")";
   }
}
