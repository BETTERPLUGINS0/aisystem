package com.nisovin.shopkeepers.commands.lib.argument;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.List;
import java.util.NoSuchElementException;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ArgumentsReader {
   private final List<? extends String> args;
   private int cursor = -1;
   private final ArgumentsReaderView view = new ArgumentsReaderView((ArgumentsReader)Unsafe.initialized(this));

   public ArgumentsReader(CommandInput commandInput) {
      Validate.notNull(commandInput, (String)"commandInput is null");
      this.args = commandInput.getArguments();
   }

   protected ArgumentsReader(List<? extends String> args) {
      this.args = args;
   }

   public List<? extends String> getArgs() {
      return this.args;
   }

   public int getSize() {
      return this.args.size();
   }

   public int getRemainingSize() {
      return this.getSize() - (this.cursor + 1);
   }

   public int getCursor() {
      return this.cursor;
   }

   public void setCursor(int cursor) {
      Validate.isTrue(cursor >= -1 && cursor < this.args.size(), "cursor is out of bounds");
      this.internalSetCursor(cursor);
   }

   private void internalSetCursor(int cursor) {
      assert cursor >= -1 && cursor < this.args.size();

      this.cursor = cursor;
   }

   public boolean hasCurrent() {
      return this.cursor >= 0;
   }

   private void checkHasCurrent() throws NoSuchElementException {
      if (!this.hasCurrent()) {
         throw new NoSuchElementException("No current argument available!");
      }
   }

   public String current() throws NoSuchElementException {
      this.checkHasCurrent();
      return (String)this.args.get(this.cursor);
   }

   @Nullable
   public String currentIfPresent() {
      return !this.hasCurrent() ? null : (String)this.args.get(this.cursor);
   }

   public boolean hasNext() {
      return this.cursor + 1 < this.args.size();
   }

   private void checkHasNext() throws NoSuchElementException {
      if (!this.hasNext()) {
         throw new NoSuchElementException("No next argument available!");
      }
   }

   public String next() throws NoSuchElementException {
      this.checkHasNext();
      this.internalSetCursor(this.cursor + 1);
      return (String)this.args.get(this.cursor);
   }

   @Nullable
   public String nextIfPresent() {
      if (!this.hasNext()) {
         return null;
      } else {
         this.internalSetCursor(this.cursor + 1);
         return (String)this.args.get(this.cursor);
      }
   }

   public String peek() throws NoSuchElementException {
      this.checkHasNext();
      return (String)this.args.get(this.cursor + 1);
   }

   @Nullable
   public String peekIfPresent() {
      return !this.hasNext() ? null : (String)this.args.get(this.cursor + 1);
   }

   public ArgumentsReader createSnapshot() {
      ArgumentsReader copy = new ArgumentsReader(this.args);
      copy.cursor = this.cursor;
      return copy;
   }

   public void setState(ArgumentsReader otherReader) {
      Validate.notNull(otherReader, (String)"otherReader is null");
      Validate.isTrue(otherReader.args == this.args, "otherReader references different arguments");
      this.internalSetCursor(otherReader.cursor);
   }

   public ArgumentsReaderView getView() {
      return this.view;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("ArgumentsReader [args=");
      builder.append(this.args);
      builder.append(", cursor=");
      builder.append(this.cursor);
      builder.append("]");
      return builder.toString();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + System.identityHashCode(this.args);
      result = 31 * result + this.cursor;
      return result;
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (!(obj instanceof ArgumentsReader)) {
         return false;
      } else {
         ArgumentsReader other = (ArgumentsReader)obj;
         if (this.args != other.args) {
            return false;
         } else {
            return this.cursor == other.cursor;
         }
      }
   }
}
