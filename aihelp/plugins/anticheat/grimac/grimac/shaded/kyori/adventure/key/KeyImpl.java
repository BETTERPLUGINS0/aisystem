package ac.grim.grimac.shaded.kyori.adventure.key;

import ac.grim.grimac.shaded.intellij.lang.annotations.RegExp;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.Stream;

final class KeyImpl implements Key {
   static final Comparator<? super Key> COMPARATOR = Comparator.comparing(Key::value).thenComparing(Key::namespace);
   @RegExp
   static final String NAMESPACE_PATTERN = "[a-z0-9_\\-.]+";
   @RegExp
   static final String VALUE_PATTERN = "[a-z0-9_\\-./]+";
   private final String namespace;
   private final String value;

   KeyImpl(@NotNull final String namespace, @NotNull final String value) {
      checkError("namespace", namespace, namespace, value, Key.checkNamespace(namespace), "[a-z0-9_\\-.]+");
      checkError("value", value, namespace, value, Key.checkValue(value), "[a-z0-9_\\-./]+");
      this.namespace = (String)Objects.requireNonNull(namespace, "namespace");
      this.value = (String)Objects.requireNonNull(value, "value");
   }

   private static void checkError(final String name, final String checkPart, final String namespace, final String value, final OptionalInt index, final String pattern) {
      if (index.isPresent()) {
         int indexValue = index.getAsInt();
         char character = checkPart.charAt(indexValue);
         throw new InvalidKeyException(namespace, value, String.format("Non " + pattern + " character in %s of Key[%s] at index %d ('%s', bytes: %s)", name, asString(namespace, value), indexValue, character, Arrays.toString(String.valueOf(character).getBytes(StandardCharsets.UTF_8))));
      }
   }

   static boolean allowedInNamespace(final char character) {
      return character == '_' || character == '-' || character >= 'a' && character <= 'z' || character >= '0' && character <= '9' || character == '.';
   }

   static boolean allowedInValue(final char character) {
      return character == '_' || character == '-' || character >= 'a' && character <= 'z' || character >= '0' && character <= '9' || character == '.' || character == '/';
   }

   @NotNull
   public String namespace() {
      return this.namespace;
   }

   @NotNull
   public String value() {
      return this.value;
   }

   @NotNull
   public String asString() {
      return asString(this.namespace, this.value);
   }

   @NotNull
   private static String asString(@NotNull final String namespace, @NotNull final String value) {
      return namespace + ':' + value;
   }

   @NotNull
   public String toString() {
      return this.asString();
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("namespace", this.namespace), ExaminableProperty.of("value", this.value));
   }

   public boolean equals(final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof Key)) {
         return false;
      } else {
         Key that = (Key)other;
         return Objects.equals(this.namespace, that.namespace()) && Objects.equals(this.value, that.value());
      }
   }

   public int hashCode() {
      int result = this.namespace.hashCode();
      result = 31 * result + this.value.hashCode();
      return result;
   }

   public int compareTo(@NotNull final Key that) {
      return Key.super.compareTo(that);
   }
}
