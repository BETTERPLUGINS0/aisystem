package ac.grim.grimac.shaded.incendo.cloud.parser.standard;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.BlockingSuggestionProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class LiteralParser<C> implements ArgumentParser<C, String>, BlockingSuggestionProvider.Strings<C> {
   private final Set<String> allAcceptedAliases;
   private final Set<String> alternativeAliases;
   private final String name;

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, String> literal(@NonNull final String name, @NonNull final String... aliases) {
      return ParserDescriptor.of(new LiteralParser(name, aliases), (Class)String.class);
   }

   private LiteralParser(@NonNull final String name, final String... aliases) {
      this.allAcceptedAliases = new TreeSet(String.CASE_INSENSITIVE_ORDER);
      this.alternativeAliases = new HashSet();
      validateNames(name, aliases);
      this.name = name;
      this.allAcceptedAliases.add(this.name);
      this.allAcceptedAliases.addAll(Arrays.asList(aliases));
      this.alternativeAliases.addAll(Arrays.asList(aliases));
   }

   @NonNull
   public ArgumentParseResult<String> parse(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      String string = commandInput.peekString();
      if (this.allAcceptedAliases.contains(string)) {
         commandInput.readString();
         return ArgumentParseResult.success(this.name);
      } else {
         return ArgumentParseResult.failure(new IllegalArgumentException(string));
      }
   }

   @NonNull
   public Iterable<String> stringSuggestions(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput input) {
      return Collections.singletonList(this.name);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public Collection<String> aliases() {
      return Collections.unmodifiableCollection(this.allAcceptedAliases);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public Collection<String> alternativeAliases() {
      return Collections.unmodifiableCollection(this.alternativeAliases);
   }

   public void insertAlias(@NonNull final String alias) {
      validateNames("valid", new String[]{alias});
      this.allAcceptedAliases.add(alias);
      this.alternativeAliases.add(alias);
   }

   private static void validateNames(final String name, final String[] aliases) {
      List<String> errors = null;
      errors = validateName(name, false, errors);
      String[] var3 = aliases;
      int var4 = aliases.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String alias = var3[var5];
         errors = validateName(alias, true, errors);
      }

      if (errors != null && !errors.isEmpty()) {
         throw new IllegalArgumentException(String.join("\n", errors));
      }
   }

   @Nullable
   private static List<String> validateName(@NonNull final String name, final boolean alias, @Nullable List<String> errors) {
      int found = name.codePoints().filter(Character::isWhitespace).findFirst().orElse(Integer.MIN_VALUE);
      if (found != Integer.MIN_VALUE) {
         if (errors == null) {
            errors = new ArrayList();
         }

         ((List)errors).add(String.format("%s '%s' is invalid: contains whitespace", alias ? "Alias" : "Name", name));
      }

      return (List)errors;
   }
}
