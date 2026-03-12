package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.checkerframework.checker.nullness.qual.Nullable;

public class LiteralArgument extends CommandArgument<String> {
   public static final String FORMAT_PREFIX = "'";
   public static final String FORMAT_SUFFIX = "'";
   private final List<String> literals;

   public LiteralArgument(String name) {
      this(name, Collections.emptyList());
   }

   public LiteralArgument(String name, List<? extends String> aliases) {
      super(name);
      this.literals = new ArrayList(aliases.size() + 1);
      this.literals.add(name);
      Iterator var3 = aliases.iterator();

      while(var3.hasNext()) {
         String alias = (String)var3.next();
         Validate.notNull(alias, (String)"alias is null");
         alias = StringUtils.removeWhitespace(alias);

         assert alias != null;

         if (!alias.isEmpty()) {
            this.literals.add(alias);
         }
      }

   }

   public CommandArgument<String> setDisplayName(@Nullable String displayName) {
      if (displayName != null) {
         Validate.isTrue(this.literals.contains(displayName), "displayName does not match any of the literals of this LiteralArgument");
      }

      return super.setDisplayName(displayName);
   }

   public String getReducedFormat() {
      return "'" + this.getDisplayName() + "'";
   }

   public String parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      if (!argsReader.hasNext()) {
         throw this.missingArgumentError();
      } else {
         String argument = argsReader.next();
         String value = null;
         Iterator var6 = this.literals.iterator();

         while(var6.hasNext()) {
            String literal = (String)var6.next();
            if (argument.equalsIgnoreCase(literal)) {
               value = literal;
               break;
            }
         }

         if (value == null) {
            throw this.invalidArgumentError(argument);
         } else {
            return value;
         }
      }
   }

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      if (argsReader.getRemainingSize() != 1) {
         return Collections.emptyList();
      } else {
         List<String> suggestions = new ArrayList();
         String partialArg = argsReader.next().toLowerCase(Locale.ROOT);
         boolean skipName = !this.getName().equals(this.getDisplayName());
         Iterator var7 = this.literals.iterator();

         while(var7.hasNext()) {
            String literal = (String)var7.next();
            if (suggestions.size() >= 20) {
               break;
            }

            if ((!skipName || !literal.equals(this.getName())) && literal.toLowerCase(Locale.ROOT).startsWith(partialArg)) {
               suggestions.add(literal);
            }
         }

         return Collections.unmodifiableList(suggestions);
      }
   }
}
