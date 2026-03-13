package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Function;
import java.util.regex.Pattern;
import org.apache.commons.io.IOCase;

public class RegexFileFilter extends AbstractFileFilter implements Serializable {
   private static final long serialVersionUID = 4269646126155225062L;
   private final Pattern pattern;
   private final Function<Path, String> pathToString;

   private static Pattern compile(String var0, int var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("Pattern is missing");
      } else {
         return Pattern.compile(var0, var1);
      }
   }

   private static int toFlags(IOCase var0) {
      return IOCase.isCaseSensitive(var0) ? 2 : 0;
   }

   public RegexFileFilter(Pattern var1) {
      this(var1, (var0) -> {
         return var0.getFileName().toString();
      });
   }

   public RegexFileFilter(Pattern var1, Function<Path, String> var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("Pattern is missing");
      } else {
         this.pattern = var1;
         this.pathToString = var2;
      }
   }

   public RegexFileFilter(String var1) {
      this(var1, 0);
   }

   public RegexFileFilter(String var1, int var2) {
      this(compile(var1, var2));
   }

   public RegexFileFilter(String var1, IOCase var2) {
      this(compile(var1, toFlags(var2)));
   }

   public boolean accept(File var1, String var2) {
      return this.pattern.matcher(var2).matches();
   }

   public FileVisitResult accept(Path var1, BasicFileAttributes var2) {
      return toFileVisitResult(this.pattern.matcher((CharSequence)this.pathToString.apply(var1)).matches(), var1);
   }

   public String toString() {
      return "RegexFileFilter [pattern=" + this.pattern + "]";
   }
}
