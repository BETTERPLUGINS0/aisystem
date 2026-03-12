package com.nisovin.shopkeepers.commands.lib.argument.ambiguity;

import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.text.TextBuilder;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AmbiguousInputHandler<O> {
   private static final String PLACEHOLDER_HEADER = "header";
   private static final String PLACEHOLDER_ENTRY = "entry";
   private static final String PLACEHOLDER_MORE = "more";
   protected static final int DEFAULT_MAX_ENTRIES = 5;
   protected final String input;
   private final Iterable<? extends O> matches;
   private final int maxEntries;
   private boolean alreadyProcessed;
   @Nullable
   private O firstMatch;
   @Nullable
   private Text errorMsg;

   public AmbiguousInputHandler(String input, Iterable<? extends O> matches) {
      this(input, matches, 5);
   }

   public AmbiguousInputHandler(String input, Iterable<? extends O> matches, int maxEntries) {
      this.alreadyProcessed = false;
      this.firstMatch = null;
      this.errorMsg = null;
      Validate.notNull(input, (String)"input is null");
      Validate.notNull(matches, (String)"matches is null");
      this.input = input;
      this.matches = matches;
      this.maxEntries = maxEntries;
   }

   private void processMatches() {
      if (!this.alreadyProcessed) {
         this.alreadyProcessed = true;
         Iterator<? extends O> matchesIterator = this.matches.iterator();
         if (matchesIterator.hasNext()) {
            this.firstMatch = matchesIterator.next();
            if (matchesIterator.hasNext()) {
               this.errorMsg = this.buildErrorMessage(this.firstMatch, matchesIterator);

               assert this.errorMsg != null;

            }
         }
      }
   }

   @Nullable
   protected abstract Text getHeaderText();

   protected abstract Text getEntryText(@Nullable O var1, int var2);

   @Nullable
   protected abstract Text getMoreText();

   protected Text buildErrorMessage(O firstMatch, Iterator<? extends O> furtherMatches) {
      assert furtherMatches != null && furtherMatches.hasNext();

      Map<String, Object> arguments = new HashMap();
      TextBuilder errorMsgBuilder = Text.text("");
      Text header = this.getHeaderText();
      if (header != null) {
         errorMsgBuilder = errorMsgBuilder.placeholder("header");
         arguments.put("header", header);
      }

      int index = 1;
      Object match = firstMatch;

      Text errorMsg;
      while(true) {
         if (index > this.maxEntries) {
            errorMsg = this.getMoreText();
            if (errorMsg != null) {
               errorMsgBuilder = errorMsgBuilder.newline().reset().placeholder("more");
               arguments.put("more", errorMsg);
            }
            break;
         }

         errorMsg = this.getEntryText(match, index);
         String entryPlaceholderKey = "entry" + index;
         errorMsgBuilder = errorMsgBuilder.newline().reset().placeholder(entryPlaceholderKey);
         arguments.put(entryPlaceholderKey, errorMsg);
         if (!furtherMatches.hasNext()) {
            break;
         }

         match = furtherMatches.next();
         ++index;
      }

      errorMsg = errorMsgBuilder.buildRoot();
      errorMsg.setPlaceholderArguments((Map)arguments);
      return errorMsg;
   }

   @Nullable
   public final O getFirstMatch() {
      this.processMatches();
      return this.firstMatch;
   }

   public final boolean isInputAmbiguous() {
      return this.getErrorMsg() != null;
   }

   @Nullable
   public final Text getErrorMsg() {
      this.processMatches();
      return this.errorMsg;
   }
}
