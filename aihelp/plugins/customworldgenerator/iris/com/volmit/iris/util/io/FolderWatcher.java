package com.volmit.iris.util.io;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import java.io.File;
import java.util.Iterator;

public class FolderWatcher extends FileWatcher {
   private KMap<File, FolderWatcher> watchers;
   private KList<File> changed;
   private KList<File> created;
   private KList<File> deleted;

   public FolderWatcher(File file) {
      super(var1);
   }

   protected void readProperties() {
      if (this.watchers == null) {
         this.watchers = new KMap();
         this.changed = new KList();
         this.created = new KList();
         this.deleted = new KList();
      }

      if (this.file.isDirectory()) {
         File[] var1 = this.file.listFiles();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            File var4 = var1[var3];
            if (!this.watchers.containsKey(var4)) {
               this.watchers.put(var4, new FolderWatcher(var4));
            }
         }

         Iterator var5 = this.watchers.k().iterator();

         while(var5.hasNext()) {
            File var6 = (File)var5.next();
            if (!var6.exists()) {
               this.watchers.remove(var6);
            }
         }
      } else {
         super.readProperties();
      }

   }

   public boolean checkModified() {
      this.changed.clear();
      this.created.clear();
      this.deleted.clear();
      if (!this.file.isDirectory()) {
         return super.checkModified();
      } else {
         KMap var1 = this.watchers.copy();
         this.readProperties();
         Iterator var2 = var1.keySet().iterator();

         File var3;
         while(var2.hasNext()) {
            var3 = (File)var2.next();
            if (!this.watchers.containsKey(var3)) {
               this.deleted.add((Object)var3);
            }
         }

         var2 = this.watchers.keySet().iterator();

         while(var2.hasNext()) {
            var3 = (File)var2.next();
            if (!var1.containsKey(var3)) {
               this.created.add((Object)var3);
            } else {
               FolderWatcher var4 = (FolderWatcher)this.watchers.get(var3);
               if (var4.checkModified()) {
                  this.changed.add((Object)var4.file);
               }

               this.changed.addAll(var4.getChanged());
               this.created.addAll(var4.getCreated());
               this.deleted.addAll(var4.getDeleted());
            }
         }

         return !this.changed.isEmpty() || !this.created.isEmpty() || !this.deleted.isEmpty();
      }
   }

   public boolean checkModifiedFast() {
      if (this.watchers != null && !this.watchers.isEmpty()) {
         this.changed.clear();
         this.created.clear();
         this.deleted.clear();
         if (!this.file.isDirectory()) {
            return super.checkModified();
         } else {
            Iterator var1 = this.watchers.keySet().iterator();

            while(var1.hasNext()) {
               File var2 = (File)var1.next();
               FolderWatcher var3 = (FolderWatcher)this.watchers.get(var2);
               if (var3.checkModifiedFast()) {
                  this.changed.add((Object)var3.file);
               }

               this.changed.addAll(var3.getChanged());
               this.created.addAll(var3.getCreated());
               this.deleted.addAll(var3.getDeleted());
            }

            return !this.changed.isEmpty() || !this.created.isEmpty() || !this.deleted.isEmpty();
         }
      } else {
         return this.checkModified();
      }
   }

   public KMap<File, FolderWatcher> getWatchers() {
      return this.watchers;
   }

   public KList<File> getChanged() {
      return this.changed;
   }

   public KList<File> getCreated() {
      return this.created;
   }

   public KList<File> getDeleted() {
      return this.deleted;
   }

   public void clear() {
      this.watchers.clear();
      this.changed.clear();
      this.deleted.clear();
      this.created.clear();
   }
}
