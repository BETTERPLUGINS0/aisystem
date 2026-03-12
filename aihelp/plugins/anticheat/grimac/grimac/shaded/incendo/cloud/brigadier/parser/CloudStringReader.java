package ac.grim.grimac.shaded.incendo.cloud.brigadier.parser;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import com.mojang.brigadier.StringReader;
import org.checkerframework.checker.nullness.qual.NonNull;

final class CloudStringReader extends StringReader {
   private final CommandInput commandInput;

   @NonNull
   static CloudStringReader of(@NonNull final CommandInput commandInput) {
      return new CloudStringReader(commandInput);
   }

   private CloudStringReader(@NonNull final CommandInput commandInput) {
      super(commandInput.input());
      this.commandInput = commandInput;
      super.setCursor(commandInput.cursor());
   }

   public void setCursor(final int cursor) {
      super.setCursor(cursor);
      this.commandInput.cursor(cursor);
   }

   public char read() {
      super.read();
      return this.commandInput.read();
   }

   public void skip() {
      super.skip();
      this.commandInput.moveCursor(1);
   }
}
