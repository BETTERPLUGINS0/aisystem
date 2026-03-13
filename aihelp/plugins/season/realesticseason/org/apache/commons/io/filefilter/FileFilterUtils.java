package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;

public class FileFilterUtils {
   private static final IOFileFilter cvsFilter = notFileFilter(and(directoryFileFilter(), nameFileFilter("CVS")));
   private static final IOFileFilter svnFilter = notFileFilter(and(directoryFileFilter(), nameFileFilter(".svn")));

   public static IOFileFilter ageFileFilter(Date var0) {
      return new AgeFileFilter(var0);
   }

   public static IOFileFilter ageFileFilter(Date var0, boolean var1) {
      return new AgeFileFilter(var0, var1);
   }

   public static IOFileFilter ageFileFilter(File var0) {
      return new AgeFileFilter(var0);
   }

   public static IOFileFilter ageFileFilter(File var0, boolean var1) {
      return new AgeFileFilter(var0, var1);
   }

   public static IOFileFilter ageFileFilter(long var0) {
      return new AgeFileFilter(var0);
   }

   public static IOFileFilter ageFileFilter(long var0, boolean var2) {
      return new AgeFileFilter(var0, var2);
   }

   public static IOFileFilter and(IOFileFilter... var0) {
      return new AndFileFilter(toList(var0));
   }

   /** @deprecated */
   @Deprecated
   public static IOFileFilter andFileFilter(IOFileFilter var0, IOFileFilter var1) {
      return new AndFileFilter(var0, var1);
   }

   public static IOFileFilter asFileFilter(FileFilter var0) {
      return new DelegateFileFilter(var0);
   }

   public static IOFileFilter asFileFilter(FilenameFilter var0) {
      return new DelegateFileFilter(var0);
   }

   public static IOFileFilter directoryFileFilter() {
      return DirectoryFileFilter.DIRECTORY;
   }

   public static IOFileFilter falseFileFilter() {
      return FalseFileFilter.FALSE;
   }

   public static IOFileFilter fileFileFilter() {
      return FileFileFilter.INSTANCE;
   }

   public static File[] filter(IOFileFilter var0, File... var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("file filter is null");
      } else {
         return var1 == null ? FileUtils.EMPTY_FILE_ARRAY : (File[])((List)filterFiles(var0, Stream.of(var1), Collectors.toList())).toArray(FileUtils.EMPTY_FILE_ARRAY);
      }
   }

   private static <R, A> R filterFiles(IOFileFilter var0, Stream<File> var1, Collector<? super File, A, R> var2) {
      Objects.requireNonNull(var2, "collector");
      if (var0 == null) {
         throw new IllegalArgumentException("file filter is null");
      } else if (var1 == null) {
         return Stream.empty().collect(var2);
      } else {
         Objects.requireNonNull(var0);
         return var1.filter(var0::accept).collect(var2);
      }
   }

   public static File[] filter(IOFileFilter var0, Iterable<File> var1) {
      return (File[])filterList(var0, var1).toArray(FileUtils.EMPTY_FILE_ARRAY);
   }

   public static List<File> filterList(IOFileFilter var0, File... var1) {
      return Arrays.asList(filter(var0, var1));
   }

   public static List<File> filterList(IOFileFilter var0, Iterable<File> var1) {
      return var1 == null ? Collections.emptyList() : (List)filterFiles(var0, StreamSupport.stream(var1.spliterator(), false), Collectors.toList());
   }

   public static Set<File> filterSet(IOFileFilter var0, File... var1) {
      return new HashSet(Arrays.asList(filter(var0, var1)));
   }

   public static Set<File> filterSet(IOFileFilter var0, Iterable<File> var1) {
      return var1 == null ? Collections.emptySet() : (Set)filterFiles(var0, StreamSupport.stream(var1.spliterator(), false), Collectors.toSet());
   }

   public static IOFileFilter magicNumberFileFilter(byte[] var0) {
      return new MagicNumberFileFilter(var0);
   }

   public static IOFileFilter magicNumberFileFilter(byte[] var0, long var1) {
      return new MagicNumberFileFilter(var0, var1);
   }

   public static IOFileFilter magicNumberFileFilter(String var0) {
      return new MagicNumberFileFilter(var0);
   }

   public static IOFileFilter magicNumberFileFilter(String var0, long var1) {
      return new MagicNumberFileFilter(var0, var1);
   }

   public static IOFileFilter makeCVSAware(IOFileFilter var0) {
      return var0 == null ? cvsFilter : and(var0, cvsFilter);
   }

   public static IOFileFilter makeDirectoryOnly(IOFileFilter var0) {
      return var0 == null ? DirectoryFileFilter.DIRECTORY : DirectoryFileFilter.DIRECTORY.and(var0);
   }

   public static IOFileFilter makeFileOnly(IOFileFilter var0) {
      return var0 == null ? FileFileFilter.INSTANCE : FileFileFilter.INSTANCE.and(var0);
   }

   public static IOFileFilter makeSVNAware(IOFileFilter var0) {
      return var0 == null ? svnFilter : and(var0, svnFilter);
   }

   public static IOFileFilter nameFileFilter(String var0) {
      return new NameFileFilter(var0);
   }

   public static IOFileFilter nameFileFilter(String var0, IOCase var1) {
      return new NameFileFilter(var0, var1);
   }

   public static IOFileFilter notFileFilter(IOFileFilter var0) {
      return var0.negate();
   }

   public static IOFileFilter or(IOFileFilter... var0) {
      return new OrFileFilter(toList(var0));
   }

   /** @deprecated */
   @Deprecated
   public static IOFileFilter orFileFilter(IOFileFilter var0, IOFileFilter var1) {
      return new OrFileFilter(var0, var1);
   }

   public static IOFileFilter prefixFileFilter(String var0) {
      return new PrefixFileFilter(var0);
   }

   public static IOFileFilter prefixFileFilter(String var0, IOCase var1) {
      return new PrefixFileFilter(var0, var1);
   }

   public static IOFileFilter sizeFileFilter(long var0) {
      return new SizeFileFilter(var0);
   }

   public static IOFileFilter sizeFileFilter(long var0, boolean var2) {
      return new SizeFileFilter(var0, var2);
   }

   public static IOFileFilter sizeRangeFileFilter(long var0, long var2) {
      SizeFileFilter var4 = new SizeFileFilter(var0, true);
      SizeFileFilter var5 = new SizeFileFilter(var2 + 1L, false);
      return var4.and(var5);
   }

   public static IOFileFilter suffixFileFilter(String var0) {
      return new SuffixFileFilter(var0);
   }

   public static IOFileFilter suffixFileFilter(String var0, IOCase var1) {
      return new SuffixFileFilter(var0, var1);
   }

   public static List<IOFileFilter> toList(IOFileFilter... var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("The filters must not be null");
      } else {
         ArrayList var1 = new ArrayList(var0.length);

         for(int var2 = 0; var2 < var0.length; ++var2) {
            if (var0[var2] == null) {
               throw new IllegalArgumentException("The filter[" + var2 + "] is null");
            }

            var1.add(var0[var2]);
         }

         return var1;
      }
   }

   public static IOFileFilter trueFileFilter() {
      return TrueFileFilter.TRUE;
   }
}
