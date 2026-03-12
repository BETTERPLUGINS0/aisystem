package ac.grim.grimac.shaded.incendo.cloud.context;

import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.returnsreceiver.qual.This;

final class CommandInputImpl implements CommandInput {
   private final String input;
   private int cursor;

   CommandInputImpl(@NonNull final String input) {
      this(input, 0);
   }

   CommandInputImpl(@NonNull final String input, @NonNegative final int cursor) {
      this.input = input;
      this.cursor = cursor;
   }

   @NonNull
   public String input() {
      return this.input;
   }

   @NonNull
   public CommandInput appendString(@NonNull final String string) {
      return this.hasRemainingInput() && !this.remainingInput().endsWith(" ") ? new CommandInputImpl(String.format("%s %s", this.input, string), this.cursor) : new CommandInputImpl(this.input + string, this.cursor);
   }

   @NonNegative
   public int cursor() {
      return this.cursor;
   }

   public void moveCursor(final int chars) {
      if (this.cursor() + chars > this.length()) {
         throw new CommandInput.CursorOutOfBoundsException(this.cursor() + chars, this.length());
      } else {
         this.cursor += chars;
      }
   }

   @This
   @NonNull
   public CommandInput cursor(final int cursor) {
      if (cursor >= 0 && cursor <= this.length()) {
         this.cursor = cursor;
         return this;
      } else {
         throw new CommandInput.CursorOutOfBoundsException(cursor, this.length());
      }
   }

   @NonNull
   public CommandInput copy() {
      return new CommandInputImpl(this.input, this.cursor);
   }
}
