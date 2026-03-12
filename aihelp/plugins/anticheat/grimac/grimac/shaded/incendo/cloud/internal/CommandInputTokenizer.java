package ac.grim.grimac.shaded.incendo.cloud.internal;

import java.util.LinkedList;
import java.util.StringTokenizer;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
public final class CommandInputTokenizer {
   private static final String DELIMITER = " ";
   private static final String EMPTY = "";
   private final CommandInputTokenizer.StringTokenizerFactory stringTokenizerFactory = new CommandInputTokenizer.StringTokenizerFactory();
   private final String input;

   public CommandInputTokenizer(@NonNull final String input) {
      this.input = input;
   }

   @NonNull
   public LinkedList<String> tokenize() {
      StringTokenizer stringTokenizer = this.stringTokenizerFactory.createStringTokenizer();
      LinkedList tokens = new LinkedList();

      while(stringTokenizer.hasMoreElements()) {
         tokens.add(stringTokenizer.nextToken());
      }

      if (this.input.endsWith(" ")) {
         tokens.add("");
      }

      return tokens;
   }

   private final class StringTokenizerFactory {
      private StringTokenizerFactory() {
      }

      @NonNull
      private StringTokenizer createStringTokenizer() {
         return new StringTokenizer(CommandInputTokenizer.this.input, " ");
      }

      // $FF: synthetic method
      StringTokenizerFactory(Object x1) {
         this();
      }
   }
}
