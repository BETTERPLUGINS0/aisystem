package com.volmit.iris.core.loader;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.engine.object.IrisScript;
import com.volmit.iris.util.data.KCache;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ScriptResourceLoader extends ResourceLoader<IrisScript> {
   public ScriptResourceLoader(File root, IrisData idm, String folderName, String resourceTypeName) {
      super(var1, var2, var3, var4, IrisScript.class);
      this.loadCache = new KCache(this::loadRaw, (long)IrisSettings.get().getPerformance().getScriptLoaderCacheSize());
   }

   public boolean supportsSchemas() {
      return false;
   }

   public long getSize() {
      return this.loadCache.getSize();
   }

   protected IrisScript loadFile(File j, String name) {
      try {
         PrecisionStopwatch var3 = PrecisionStopwatch.start();
         IrisScript var4 = new IrisScript(IO.readAll(var1));
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

   public String[] getPossibleKeys() {
      if (this.possibleKeys != null) {
         return this.possibleKeys;
      } else {
         Iris.debug("Building " + this.resourceTypeName + " Possibility Lists");
         HashSet var1 = new HashSet();
         Iterator var2 = this.getFolders().iterator();

         while(var2.hasNext()) {
            File var3 = (File)var2.next();
            if (var3.isDirectory()) {
               var1.addAll(this.getKeysInDirectory(var3));
            }
         }

         this.possibleKeys = (String[])var1.toArray(new String[0]);
         return this.possibleKeys;
      }
   }

   private Set<String> getKeysInDirectory(File directory) {
      HashSet var2 = new HashSet();
      File[] var3 = var1.listFiles();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         File var6 = var3[var5];
         if (var6.isFile() && var6.getName().endsWith(".kts")) {
            var2.add(var6.getName().replaceAll("\\Q.kts\\E", ""));
         } else if (var6.isDirectory()) {
            var2.addAll(this.getKeysInDirectory(var6));
         }
      }

      return var2;
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
            if (var7.isFile() && var7.getName().endsWith(".kts") && var7.getName().split("\\Q.\\E")[0].equals(var1)) {
               return var7;
            }
         }

         var8 = new File(var3, var1 + ".kts");
      } while(!var8.exists());

      return var8;
   }

   private IrisScript loadRaw(String name) {
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
            if (var7.isFile() && var7.getName().endsWith(".kts") && var7.getName().split("\\Q.\\E")[0].equals(var1)) {
               return this.loadFile(var7, var1);
            }
         }

         var8 = new File(var3, var1 + ".kts");
      } while(!var8.exists());

      return this.loadFile(var8, var1);
   }

   public IrisScript load(String name, boolean warn) {
      return (IrisScript)this.loadCache.get(var1);
   }
}
