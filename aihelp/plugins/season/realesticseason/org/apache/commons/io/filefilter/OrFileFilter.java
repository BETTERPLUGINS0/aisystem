package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class OrFileFilter extends AbstractFileFilter implements ConditionalFileFilter, Serializable {
   private static final long serialVersionUID = 5767770777065432721L;
   private final List<IOFileFilter> fileFilters;

   public OrFileFilter() {
      this(0);
   }

   private OrFileFilter(ArrayList<IOFileFilter> var1) {
      this.fileFilters = (List)Objects.requireNonNull(var1, "initialList");
   }

   private OrFileFilter(int var1) {
      this(new ArrayList(var1));
   }

   public OrFileFilter(IOFileFilter... var1) {
      this(((IOFileFilter[])Objects.requireNonNull(var1, "fileFilters")).length);
      this.addFileFilter(var1);
   }

   public OrFileFilter(IOFileFilter var1, IOFileFilter var2) {
      this(2);
      this.addFileFilter(var1);
      this.addFileFilter(var2);
   }

   public OrFileFilter(List<IOFileFilter> var1) {
      this(new ArrayList((Collection)Objects.requireNonNull(var1, "fileFilters")));
   }

   public boolean accept(File var1) {
      Iterator var2 = this.fileFilters.iterator();

      IOFileFilter var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (IOFileFilter)var2.next();
      } while(!var3.accept(var1));

      return true;
   }

   public boolean accept(File var1, String var2) {
      Iterator var3 = this.fileFilters.iterator();

      IOFileFilter var4;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         var4 = (IOFileFilter)var3.next();
      } while(!var4.accept(var1, var2));

      return true;
   }

   public FileVisitResult accept(Path var1, BasicFileAttributes var2) {
      Iterator var3 = this.fileFilters.iterator();

      IOFileFilter var4;
      do {
         if (!var3.hasNext()) {
            return FileVisitResult.TERMINATE;
         }

         var4 = (IOFileFilter)var3.next();
      } while(var4.accept(var1, var2) != FileVisitResult.CONTINUE);

      return FileVisitResult.CONTINUE;
   }

   public void addFileFilter(IOFileFilter var1) {
      this.fileFilters.add((IOFileFilter)Objects.requireNonNull(var1, "fileFilter"));
   }

   public void addFileFilter(IOFileFilter... var1) {
      IOFileFilter[] var2 = (IOFileFilter[])Objects.requireNonNull(var1, "fileFilters");
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         IOFileFilter var5 = var2[var4];
         this.addFileFilter(var5);
      }

   }

   public List<IOFileFilter> getFileFilters() {
      return Collections.unmodifiableList(this.fileFilters);
   }

   public boolean removeFileFilter(IOFileFilter var1) {
      return this.fileFilters.remove(var1);
   }

   public void setFileFilters(List<IOFileFilter> var1) {
      this.fileFilters.clear();
      this.fileFilters.addAll((Collection)Objects.requireNonNull(var1, "fileFilters"));
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append("(");
      if (this.fileFilters != null) {
         for(int var2 = 0; var2 < this.fileFilters.size(); ++var2) {
            if (var2 > 0) {
               var1.append(",");
            }

            var1.append(this.fileFilters.get(var2));
         }
      }

      var1.append(")");
      return var1.toString();
   }
}
