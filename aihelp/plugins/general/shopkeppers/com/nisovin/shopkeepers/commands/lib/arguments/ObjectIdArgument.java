package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class ObjectIdArgument<I> extends CommandArgument<I> {
   protected static final Pattern ARGUMENTS_SEPARATOR_PATTERN = Pattern.compile(" ", 16);
   protected final CommandArgument<I> idArgument;
   protected final ArgumentFilter<? super I> filter;
   protected final int minimumCompletionInput;

   public ObjectIdArgument(String name, CommandArgument<I> idArgument, ArgumentFilter<? super I> filter, int minimumCompletionInput) {
      super(name);
      Validate.notNull(idArgument, (String)"idArgument is null");
      Validate.notNull(filter, (String)"filter is null");
      Validate.isTrue(minimumCompletionInput >= 0, "minimumCompletionInput cannot be negative");
      this.idArgument = idArgument;
      this.filter = filter;
      this.minimumCompletionInput = minimumCompletionInput;
      idArgument.setParent(this);
   }

   @NonNull
   public I parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      if (!argsReader.hasNext()) {
         throw this.missingArgumentError();
      } else {
         int startIndex = argsReader.getCursor();
         I id = this.idArgument.parseValue(input, context, argsReader);
         if (!this.filter.test(input, context, id)) {
            int endIndex = argsReader.getCursor();
            List<? extends String> parsedArgs = argsReader.getArgs().subList(startIndex + 1, endIndex + 1);
            String parsedArgsString = String.join(" ", parsedArgs);
            throw this.filter.rejectedArgumentException(this, parsedArgsString, id);
         } else {
            return id;
         }
      }
   }

   protected abstract String toString(@NonNull I var1);

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      if (argsReader.getRemainingSize() == 0) {
         return Collections.emptyList();
      } else {
         int startIndex = argsReader.getCursor() + 1;

         try {
            this.idArgument.parseValue(input, context, argsReader);
            if (argsReader.getRemainingSize() > 0) {
               return Collections.emptyList();
            }
         } catch (ArgumentParseException var10) {
         }

         List<? extends String> args = argsReader.getArgs();
         int endIndex = args.size();
         int argsCount = endIndex - startIndex;

         assert argsCount > 0;

         String idPrefix;
         if (argsCount == 1) {
            idPrefix = (String)args.get(startIndex);
         } else {
            List<? extends String> parsedArgs = args.subList(startIndex, endIndex);
            idPrefix = String.join(" ", parsedArgs);
         }

         return this.complete(input, context, idPrefix, argsCount);
      }
   }

   protected List<? extends String> complete(CommandInput input, CommandContextView context, String idPrefix, int argsCount) {
      List<String> suggestions = new ArrayList();
      Iterator var6 = this.getCompletionSuggestions(input, context, idPrefix).iterator();

      while(var6.hasNext()) {
         I id = var6.next();
         if (suggestions.size() >= 20) {
            break;
         }

         if (this.filter.test(input, context, id)) {
            String idString = this.toString(id);
            if (!StringUtils.isEmpty(idString)) {
               if (argsCount > 1) {
                  String[] idStringParts = ARGUMENTS_SEPARATOR_PATTERN.split(idString, argsCount);
                  if (idStringParts.length == argsCount) {
                     idString = idStringParts[argsCount - 1];
                  }
               }

               suggestions.add(idString);
            }
         }
      }

      return Collections.unmodifiableList(suggestions);
   }

   protected abstract Iterable<? extends I> getCompletionSuggestions(CommandInput var1, CommandContextView var2, String var3);
}
