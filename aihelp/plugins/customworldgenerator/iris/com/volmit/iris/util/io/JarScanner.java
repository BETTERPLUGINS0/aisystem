package com.volmit.iris.util.io;

import com.volmit.iris.Iris;
import com.volmit.iris.util.collection.KSet;
import java.io.File;
import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JarScanner {
   private final KSet<Class<?>> classes;
   private final File jar;
   private final String superPackage;
   private final boolean report;

   public JarScanner(File jar, String superPackage, boolean report) {
      this.jar = var1;
      this.classes = new KSet(new Class[0]);
      this.superPackage = var2;
      this.report = var3;
   }

   public JarScanner(File jar, String superPackage) {
      this(var1, var2, true);
   }

   public void scan() {
      this.classes.clear();
      FileInputStream var1 = new FileInputStream(this.jar);
      ZipInputStream var2 = new ZipInputStream(var1);

      for(ZipEntry var3 = var2.getNextEntry(); var3 != null; var3 = var2.getNextEntry()) {
         if (!var3.isDirectory() && var3.getName().endsWith(".class") && !var3.getName().contains("$")) {
            String var4 = var3.getName().replaceAll("/", ".").replace(".class", "");
            if (var4.startsWith(this.superPackage)) {
               try {
                  Class var5 = Class.forName(var4);
                  this.classes.add(var5);
               } catch (Throwable var6) {
                  if (this.report) {
                     Iris.reportError(var6);
                     var6.printStackTrace();
                  }
               }
            }
         }
      }

      var2.close();
   }

   public void scanAll() {
      this.classes.clear();
      FileInputStream var1 = new FileInputStream(this.jar);
      ZipInputStream var2 = new ZipInputStream(var1);

      for(ZipEntry var3 = var2.getNextEntry(); var3 != null; var3 = var2.getNextEntry()) {
         if (!var3.isDirectory() && var3.getName().endsWith(".class")) {
            String var4 = var3.getName().replaceAll("/", ".").replace(".class", "");
            if (var4.startsWith(this.superPackage)) {
               try {
                  Class var5 = Class.forName(var4);
                  this.classes.add(var5);
               } catch (Throwable var6) {
                  if (this.report) {
                     Iris.reportError(var6);
                     var6.printStackTrace();
                  }
               }
            }
         }
      }

   }

   public KSet<Class<?>> getClasses() {
      return this.classes;
   }

   public File getJar() {
      return this.jar;
   }
}
