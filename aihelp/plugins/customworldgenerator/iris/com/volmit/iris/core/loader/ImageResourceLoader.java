package com.volmit.iris.core.loader;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.engine.object.IrisImage;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.data.KCache;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.Set;
import javax.imageio.ImageIO;

public class ImageResourceLoader extends ResourceLoader<IrisImage> {
   public ImageResourceLoader(File root, IrisData idm, String folderName, String resourceTypeName) {
      super(var1, var2, var3, var4, IrisImage.class);
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

   protected IrisImage loadFile(File j, String name) {
      try {
         PrecisionStopwatch var3 = PrecisionStopwatch.start();
         BufferedImage var4 = ImageIO.read(var1);
         IrisImage var5 = new IrisImage(var4);
         var5.setLoadFile(var1);
         var5.setLoader(this.manager);
         var5.setLoadKey(var2);
         this.logLoad(var1, var5);
         tlt.addAndGet(var3.getMilliseconds());
         return var5;
      } catch (Throwable var6) {
         Iris.reportError(var6);
         String var10000 = this.resourceTypeName;
         Iris.warn("Couldn't read " + var10000 + " file: " + var1.getPath() + ": " + var6.getMessage());
         return null;
      }
   }

   void getPNGFiles(File directory, Set<String> m) {
      File[] var3 = var1.listFiles();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         File var6 = var3[var5];
         if (var6.isFile() && var6.getName().endsWith(".png")) {
            var2.add(var6.getName().replaceAll("\\Q.png\\E", ""));
         } else if (var6.isDirectory()) {
            this.getPNGFiles(var6, var2);
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
            this.getPNGFiles(var3, var1);
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
            if (var7.isFile() && var7.getName().endsWith(".png") && var7.getName().split("\\Q.\\E")[0].equals(var1)) {
               return var7;
            }
         }

         var8 = new File(var3, var1 + ".png");
      } while(!var8.exists());

      return var8;
   }

   public IrisImage load(String name) {
      return this.load(var1, true);
   }

   private IrisImage loadRaw(String name) {
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
            if (var7.isFile() && var7.getName().endsWith(".png") && var7.getName().split("\\Q.\\E")[0].equals(var1)) {
               return this.loadFile(var7, var1);
            }
         }

         var8 = new File(var3, var1 + ".png");
      } while(!var8.exists());

      return this.loadFile(var8, var1);
   }

   public IrisImage load(String name, boolean warn) {
      return (IrisImage)this.loadCache.get(var1);
   }
}
