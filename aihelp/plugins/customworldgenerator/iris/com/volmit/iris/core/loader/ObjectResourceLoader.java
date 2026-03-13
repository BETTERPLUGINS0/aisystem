package com.volmit.iris.core.loader;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.engine.object.IrisObject;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.data.KCache;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.io.File;
import java.util.Iterator;

public class ObjectResourceLoader extends ResourceLoader<IrisObject> {
   public ObjectResourceLoader(File root, IrisData idm, String folderName, String resourceTypeName) {
      super(var1, var2, var3, var4, IrisObject.class);
      this.loadCache = new KCache(this::loadRaw, (long)IrisSettings.get().getPerformance().getObjectLoaderCacheSize());
   }

   public boolean supportsSchemas() {
      return false;
   }

   public long getSize() {
      return this.loadCache.getSize();
   }

   public long getTotalStorage() {
      return this.getSize();
   }

   protected IrisObject loadFile(File j, String name) {
      try {
         PrecisionStopwatch var3 = PrecisionStopwatch.start();
         IrisObject var4 = new IrisObject(0, 0, 0);
         var4.setLoadKey(var2);
         var4.setLoader(this.manager);
         var4.setLoadFile(var1);
         var4.read(var1);
         this.logLoad(var1, var4);
         tlt.addAndGet(var3.getMilliseconds());
         return var4;
      } catch (Throwable var5) {
         Iris.reportError(var5);
         String var10000 = this.resourceTypeName;
         Iris.warn("Couldn't read " + var10000 + " file: " + var1.getPath() + ": " + var5.getMessage());
         return null;
      }
   }

   public String[] getPossibleKeys() {
      if (this.possibleKeys != null) {
         return this.possibleKeys;
      } else {
         Iris.debug("Building " + this.resourceTypeName + " Possibility Lists");
         KSet var1 = new KSet(new String[0]);
         Iterator var2 = this.getFolders().iterator();

         while(var2.hasNext()) {
            File var3 = (File)var2.next();
            var1.addAll(this.getFiles(var3, ".iob", true));
         }

         this.possibleKeys = (String[])var1.toArray(new String[0]);
         return this.possibleKeys;
      }
   }

   private KList<String> getFiles(File dir, String ext, boolean skipDirName) {
      KList var4 = new KList();
      String var5 = var3 ? "" : var1.getName() + "/";
      File[] var6 = var1.listFiles();
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         File var9 = var6[var8];
         if (var9.isFile() && var9.getName().endsWith(var2)) {
            var4.add((Object)(var5 + var9.getName().replaceAll("\\Q" + var2 + "\\E", "")));
         } else if (var9.isDirectory()) {
            this.getFiles(var9, var2, false).forEach((var2x) -> {
               var4.add((Object)(var5 + var2x));
            });
         }
      }

      return var4;
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
            if (var7.isFile() && var7.getName().endsWith(".iob") && var7.getName().split("\\Q.\\E")[0].equals(var1)) {
               return var7;
            }
         }

         var8 = new File(var3, var1 + ".iob");
      } while(!var8.exists());

      return var8;
   }

   public IrisObject load(String name) {
      return this.load(var1, true);
   }

   private IrisObject loadRaw(String name) {
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
            if (var7.isFile() && var7.getName().endsWith(".iob") && var7.getName().split("\\Q.\\E")[0].equals(var1)) {
               return this.loadFile(var7, var1);
            }
         }

         var8 = new File(var3, var1 + ".iob");
      } while(!var8.exists());

      return this.loadFile(var8, var1);
   }

   public IrisObject load(String name, boolean warn) {
      return (IrisObject)this.loadCache.get(var1);
   }
}
