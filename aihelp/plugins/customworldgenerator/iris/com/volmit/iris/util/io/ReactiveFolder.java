package com.volmit.iris.util.io;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.function.Consumer3;
import java.io.File;
import java.util.Iterator;

public class ReactiveFolder {
   private final File folder;
   private final Consumer3<KList<File>, KList<File>, KList<File>> hotload;
   private FolderWatcher fw;
   private int checkCycle = 0;

   public ReactiveFolder(File folder, Consumer3<KList<File>, KList<File>, KList<File>> hotload) {
      this.folder = var1;
      this.hotload = var2;
      this.fw = new FolderWatcher(var1);
      this.fw.checkModified();
   }

   public void checkIgnore() {
      this.fw = new FolderWatcher(this.folder);
   }

   public boolean check() {
      boolean var1;
      label119: {
         ++this.checkCycle;
         var1 = false;
         if (this.checkCycle % 3 == 0) {
            if (!this.fw.checkModified()) {
               break label119;
            }
         } else if (!this.fw.checkModifiedFast()) {
            break label119;
         }

         Iterator var2 = this.fw.getCreated().iterator();

         File var3;
         label102:
         while(true) {
            do {
               if (!var2.hasNext()) {
                  break label102;
               }

               var3 = (File)var2.next();
            } while(!var3.getName().endsWith(".iob") && !var3.getName().endsWith(".json") && !var3.getName().endsWith(".kts"));

            if (!var3.getPath().contains(".iris") && !var3.getName().endsWith(".gradle.kts")) {
               var1 = true;
               break;
            }
         }

         if (!var1) {
            label118: {
               var2 = this.fw.getChanged().iterator();

               do {
                  do {
                     do {
                        if (!var2.hasNext()) {
                           break label118;
                        }

                        var3 = (File)var2.next();
                     } while(var3.getPath().contains(".iris"));
                  } while(var3.getName().endsWith(".gradle.kts"));
               } while(!var3.getName().endsWith(".iob") && !var3.getName().endsWith(".json") && !var3.getName().endsWith(".kts"));

               var1 = true;
            }
         }

         if (!var1) {
            label116: {
               var2 = this.fw.getDeleted().iterator();

               do {
                  do {
                     do {
                        if (!var2.hasNext()) {
                           break label116;
                        }

                        var3 = (File)var2.next();
                     } while(var3.getPath().contains(".iris"));
                  } while(var3.getName().endsWith(".gradle.kts"));
               } while(!var3.getName().endsWith(".iob") && !var3.getName().endsWith(".json") && !var3.getName().endsWith(".kts"));

               var1 = true;
            }
         }
      }

      if (var1) {
         this.hotload.accept(this.fw.getCreated(), this.fw.getChanged(), this.fw.getDeleted());
      }

      return this.fw.checkModified();
   }

   public void clear() {
      this.fw.clear();
   }
}
