package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver;

import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.TagPattern;
import java.text.ChoiceFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;

public final class Formatter {
   private Formatter() {
   }

   @NotNull
   public static TagResolver number(@TagPattern @NotNull final String key, @NotNull final Number number) {
      return TagResolver.resolver(key, (argumentQueue, context) -> {
         Object decimalFormat;
         if (argumentQueue.hasNext()) {
            String locale = argumentQueue.pop().value();
            if (argumentQueue.hasNext()) {
               String format = argumentQueue.pop().value();
               decimalFormat = new DecimalFormat(format, new DecimalFormatSymbols(Locale.forLanguageTag(locale)));
            } else if (locale.contains(".")) {
               decimalFormat = new DecimalFormat(locale, DecimalFormatSymbols.getInstance());
            } else {
               decimalFormat = DecimalFormat.getInstance(Locale.forLanguageTag(locale));
            }
         } else {
            decimalFormat = DecimalFormat.getInstance();
         }

         return Tag.inserting(context.deserialize(((NumberFormat)decimalFormat).format(number)));
      });
   }

   @NotNull
   public static TagResolver date(@TagPattern @NotNull final String key, @NotNull final TemporalAccessor time) {
      return TagResolver.resolver(key, (argumentQueue, context) -> {
         String format = argumentQueue.popOr("Format expected.").value();
         return Tag.inserting(context.deserialize(DateTimeFormatter.ofPattern(format).format(time)));
      });
   }

   @NotNull
   public static TagResolver choice(@TagPattern @NotNull final String key, final Number number) {
      return TagResolver.resolver(key, (argumentQueue, context) -> {
         String format = argumentQueue.popOr("Format expected.").value();
         ChoiceFormat choiceFormat = new ChoiceFormat(format);
         return Tag.inserting(context.deserialize(choiceFormat.format(number)));
      });
   }

   public static TagResolver booleanChoice(@TagPattern @NotNull final String key, final boolean value) {
      return TagResolver.resolver(key, (argumentQueue, context) -> {
         String trueCase = argumentQueue.popOr("True format expected.").value();
         String falseCase = argumentQueue.popOr("False format expected.").value();
         return Tag.inserting(context.deserialize(value ? trueCase : falseCase));
      });
   }
}
