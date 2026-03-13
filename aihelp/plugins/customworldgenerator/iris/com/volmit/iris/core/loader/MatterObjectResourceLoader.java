package com.volmit.iris.core.loader;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.engine.object.matter.IrisMatterObject;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.data.KCache;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.io.File;
import java.util.Iterator;

public class MatterObjectResourceLoader extends ResourceLoader<IrisMatterObject> {
   private String[] possibleKeys;

   public MatterObjectResourceLoader(File root, IrisData idm, String folderName, String resourceTypeName) {
      super(var1, var2, var3, var4, IrisMatterObject.class);
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

   protected IrisMatterObject loadFile(File j, String name) {
      try {
         PrecisionStopwatch var3 = PrecisionStopwatch.start();
         IrisMatterObject var4 = IrisMatterObject.from(var1);
         var4.setLoadKey(var2);
         var4.setLoader(this.manager);
         var4.setLoadFile(var1);
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

   private void findMatFiles(File dir, KSet<String> m) {
      File[] var3 = var1.listFiles();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         File var6 = var3[var5];
         if (var6.isFile() && var6.getName().endsWith(".mat")) {
            var2.add(var6.getName().replaceAll("\\Q.mat\\E", ""));
         } else if (var6.isDirectory()) {
            this.findMatFiles(var6, var2);
         }
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
            this.findMatFiles(var3, var1);
         }

         KList var4 = new KList(var1);
         this.possibleKeys = (String[])var4.toArray(new String[0]);
         return this.possibleKeys;
      }
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
            if (var7.isFile() && var7.getName().endsWith(".mat") && var7.getName().split("\\Q.\\E")[0].equals(var1)) {
               return var7;
            }
         }

         var8 = new File(var3, var1 + ".mat");
      } while(!var8.exists());

      return var8;
   }

   public IrisMatterObject load(String name) {
      return this.load(var1, true);
   }

   private IrisMatterObject loadRaw(String name) {
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
            if (var7.isFile() && var7.getName().endsWith(".mat") && var7.getName().split("\\Q.\\E")[0].equals(var1)) {
               return this.loadFile(var7, var1);
            }
         }

         var8 = new File(var3, var1 + ".mat");
      } while(!var8.exists());

      return this.loadFile(var8, var1);
   }

   public IrisMatterObject load(String name, boolean warn) {
      return (IrisMatterObject)this.loadCache.get(var1);
   }
}
