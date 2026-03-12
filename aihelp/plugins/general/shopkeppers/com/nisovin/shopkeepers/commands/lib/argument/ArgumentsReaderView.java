package com.nisovin.shopkeepers.commands.lib.argument;

import com.nisovin.shopkeepers.util.java.Validate;
import java.util.List;
import java.util.NoSuchElementException;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ArgumentsReaderView {
   private final ArgumentsReader argsReader;

   public ArgumentsReaderView(ArgumentsReader argsReader) {
      Validate.notNull(argsReader, (String)"argsReader is null");
      this.argsReader = argsReader;
   }

   public List<? extends String> getArgs() {
      return this.argsReader.getArgs();
   }

   public int getSize() {
      return this.argsReader.getSize();
   }

   public int getRemainingSize() {
      return this.argsReader.getRemainingSize();
   }

   public int getCursor() {
      return this.argsReader.getCursor();
   }

   public boolean hasCurrent() {
      return this.argsReader.hasCurrent();
   }

   public String current() throws NoSuchElementException {
      return this.argsReader.current();
   }

   @Nullable
   public String currentIfPresent() {
      return this.argsReader.currentIfPresent();
   }

   public boolean hasNext() {
      return this.argsReader.hasNext();
   }

   public String peek() throws NoSuchElementException {
      return this.argsReader.peek();
   }

   @Nullable
   public String peekIfPresent() {
      return this.argsReader.peekIfPresent();
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("ArgumentsReaderView [argsReader=");
      builder.append(this.argsReader);
      builder.append("]");
      return builder.toString();
   }

   public int hashCode() {
      return this.argsReader.hashCode();
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (!(obj instanceof ArgumentsReaderView)) {
         return false;
      } else {
         ArgumentsReaderView other = (ArgumentsReaderView)obj;
         return this.argsReader.equals(other.argsReader);
      }
   }
}
