package ac.grim.grimac.shaded.incendo.cloud.bukkit.parser;

import ac.grim.grimac.shaded.incendo.cloud.bukkit.BukkitCaptionKeys;
import ac.grim.grimac.shaded.incendo.cloud.caption.CaptionVariable;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.exception.parsing.ParserException;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.BlockingSuggestionProvider;
import java.util.ArrayList;
import java.util.List;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class EnchantmentParser<C> implements ArgumentParser<C, Enchantment>, BlockingSuggestionProvider.Strings<C> {
   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> ParserDescriptor<C, Enchantment> enchantmentParser() {
      return ParserDescriptor.of(new EnchantmentParser(), (Class)Enchantment.class);
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, Enchantment> enchantmentComponent() {
      return CommandComponent.builder().parser(enchantmentParser());
   }

   @NonNull
   public ArgumentParseResult<Enchantment> parse(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      String input = commandInput.peekString();

      NamespacedKey key;
      try {
         if (input.contains(":")) {
            key = new NamespacedKey(commandInput.readUntilAndSkip(':'), commandInput.readString());
         } else {
            key = NamespacedKey.minecraft(commandInput.readString());
         }
      } catch (Exception var6) {
         return ArgumentParseResult.failure(new EnchantmentParser.EnchantmentParseException(input, commandContext));
      }

      Enchantment enchantment = Enchantment.getByKey(key);
      return enchantment == null ? ArgumentParseResult.failure(new EnchantmentParser.EnchantmentParseException(input, commandContext)) : ArgumentParseResult.success(enchantment);
   }

   @NonNull
   public Iterable<String> stringSuggestions(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput input) {
      List<String> completions = new ArrayList();
      Enchantment[] var4 = Enchantment.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Enchantment value = var4[var6];
         if (value.getKey().getNamespace().equals("minecraft")) {
            completions.add(value.getKey().getKey());
         } else {
            completions.add(value.getKey().toString());
         }
      }

      return completions;
   }

   public static final class EnchantmentParseException extends ParserException {
      private final String input;

      public EnchantmentParseException(@NonNull final String input, @NonNull final CommandContext<?> context) {
         super(EnchantmentParser.class, context, BukkitCaptionKeys.ARGUMENT_PARSE_FAILURE_ENCHANTMENT, CaptionVariable.of("input", input));
         this.input = input;
      }

      @NonNull
      public String input() {
         return this.input;
      }
   }
}
