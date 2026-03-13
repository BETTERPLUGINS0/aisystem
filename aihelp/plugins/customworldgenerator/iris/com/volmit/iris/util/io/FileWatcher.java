package com.volmit.iris.util.io;

import java.io.File;

public class FileWatcher {
   protected final File file;
   private long lastModified;
   private long size;

   public FileWatcher(File file) {
      this.file = var1;
      this.readProperties();
   }

   protected void readProperties() {
      boolean var1 = this.file.exists();
      this.lastModified = var1 ? this.file.lastModified() : -1L;
      this.size = var1 ? (this.file.isDirectory() ? -2L : this.file.length()) : -1L;
   }

   public boolean checkModified() {
      long var1 = this.lastModified;
      long var3 = this.size;
      boolean var5 = false;
      this.readProperties();
      if (this.lastModified != var1 || var3 != this.size) {
         var5 = true;
      }

      return var5;
   }
}
