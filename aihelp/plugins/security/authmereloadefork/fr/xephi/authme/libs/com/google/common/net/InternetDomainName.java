package fr.xephi.authme.libs.com.google.common.net;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Ascii;
import fr.xephi.authme.libs.com.google.common.base.CharMatcher;
import fr.xephi.authme.libs.com.google.common.base.Joiner;
import fr.xephi.authme.libs.com.google.common.base.Optional;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.base.Splitter;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableList;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.Immutable;
import fr.xephi.authme.libs.com.google.thirdparty.publicsuffix.PublicSuffixPatterns;
import fr.xephi.authme.libs.com.google.thirdparty.publicsuffix.PublicSuffixType;
import java.util.List;
import javax.annotation.CheckForNull;

@Immutable
@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
public final class InternetDomainName {
   private static final CharMatcher DOTS_MATCHER = CharMatcher.anyOf(".。．｡");
   private static final Splitter DOT_SPLITTER = Splitter.on('.');
   private static final Joiner DOT_JOINER = Joiner.on('.');
   private static final int NO_SUFFIX_FOUND = -1;
   private static final int MAX_PARTS = 127;
   private static final int MAX_LENGTH = 253;
   private static final int MAX_DOMAIN_PART_LENGTH = 63;
   private final String name;
   private final ImmutableList<String> parts;
   private final int publicSuffixIndex;
   private final int registrySuffixIndex;
   private static final CharMatcher DASH_MATCHER = CharMatcher.anyOf("-_");
   private static final CharMatcher DIGIT_MATCHER = CharMatcher.inRange('0', '9');
   private static final CharMatcher LETTER_MATCHER = CharMatcher.inRange('a', 'z').or(CharMatcher.inRange('A', 'Z'));
   private static final CharMatcher PART_CHAR_MATCHER;

   InternetDomainName(String name) {
      name = Ascii.toLowerCase(DOTS_MATCHER.replaceFrom(name, '.'));
      if (name.endsWith(".")) {
         name = name.substring(0, name.length() - 1);
      }

      Preconditions.checkArgument(name.length() <= 253, "Domain name too long: '%s':", (Object)name);
      this.name = name;
      this.parts = ImmutableList.copyOf(DOT_SPLITTER.split(name));
      Preconditions.checkArgument(this.parts.size() <= 127, "Domain has too many parts: '%s'", (Object)name);
      Preconditions.checkArgument(validateSyntax(this.parts), "Not a valid domain name: '%s'", (Object)name);
      this.publicSuffixIndex = this.findSuffixOfType(Optional.absent());
      this.registrySuffixIndex = this.findSuffixOfType(Optional.of(PublicSuffixType.REGISTRY));
   }

   private int findSuffixOfType(Optional<PublicSuffixType> desiredType) {
      int partsSize = this.parts.size();

      for(int i = 0; i < partsSize; ++i) {
         String ancestorName = DOT_JOINER.join((Iterable)this.parts.subList(i, partsSize));
         if (matchesType(desiredType, Optional.fromNullable((PublicSuffixType)PublicSuffixPatterns.EXACT.get(ancestorName)))) {
            return i;
         }

         if (PublicSuffixPatterns.EXCLUDED.containsKey(ancestorName)) {
            return i + 1;
         }

         if (matchesWildcardSuffixType(desiredType, ancestorName)) {
            return i;
         }
      }

      return -1;
   }

   @CanIgnoreReturnValue
   public static InternetDomainName from(String domain) {
      return new InternetDomainName((String)Preconditions.checkNotNull(domain));
   }

   private static boolean validateSyntax(List<String> parts) {
      int lastIndex = parts.size() - 1;
      if (!validatePart((String)parts.get(lastIndex), true)) {
         return false;
      } else {
         for(int i = 0; i < lastIndex; ++i) {
            String part = (String)parts.get(i);
            if (!validatePart(part, false)) {
               return false;
            }
         }

         return true;
      }
   }

   private static boolean validatePart(String part, boolean isFinalPart) {
      if (part.length() >= 1 && part.length() <= 63) {
         String asciiChars = CharMatcher.ascii().retainFrom(part);
         if (!PART_CHAR_MATCHER.matchesAllOf(asciiChars)) {
            return false;
         } else if (!DASH_MATCHER.matches(part.charAt(0)) && !DASH_MATCHER.matches(part.charAt(part.length() - 1))) {
            return !isFinalPart || !DIGIT_MATCHER.matches(part.charAt(0));
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public ImmutableList<String> parts() {
      return this.parts;
   }

   public boolean isPublicSuffix() {
      return this.publicSuffixIndex == 0;
   }

   public boolean hasPublicSuffix() {
      return this.publicSuffixIndex != -1;
   }

   @CheckForNull
   public InternetDomainName publicSuffix() {
      return this.hasPublicSuffix() ? this.ancestor(this.publicSuffixIndex) : null;
   }

   public boolean isUnderPublicSuffix() {
      return this.publicSuffixIndex > 0;
   }

   public boolean isTopPrivateDomain() {
      return this.publicSuffixIndex == 1;
   }

   public InternetDomainName topPrivateDomain() {
      if (this.isTopPrivateDomain()) {
         return this;
      } else {
         Preconditions.checkState(this.isUnderPublicSuffix(), "Not under a public suffix: %s", (Object)this.name);
         return this.ancestor(this.publicSuffixIndex - 1);
      }
   }

   public boolean isRegistrySuffix() {
      return this.registrySuffixIndex == 0;
   }

   public boolean hasRegistrySuffix() {
      return this.registrySuffixIndex != -1;
   }

   @CheckForNull
   public InternetDomainName registrySuffix() {
      return this.hasRegistrySuffix() ? this.ancestor(this.registrySuffixIndex) : null;
   }

   public boolean isUnderRegistrySuffix() {
      return this.registrySuffixIndex > 0;
   }

   public boolean isTopDomainUnderRegistrySuffix() {
      return this.registrySuffixIndex == 1;
   }

   public InternetDomainName topDomainUnderRegistrySuffix() {
      if (this.isTopDomainUnderRegistrySuffix()) {
         return this;
      } else {
         Preconditions.checkState(this.isUnderRegistrySuffix(), "Not under a registry suffix: %s", (Object)this.name);
         return this.ancestor(this.registrySuffixIndex - 1);
      }
   }

   public boolean hasParent() {
      return this.parts.size() > 1;
   }

   public InternetDomainName parent() {
      Preconditions.checkState(this.hasParent(), "Domain '%s' has no parent", (Object)this.name);
      return this.ancestor(1);
   }

   private InternetDomainName ancestor(int levels) {
      return from(DOT_JOINER.join((Iterable)this.parts.subList(levels, this.parts.size())));
   }

   public InternetDomainName child(String leftParts) {
      String var2 = (String)Preconditions.checkNotNull(leftParts);
      String var3 = this.name;
      return from((new StringBuilder(1 + String.valueOf(var2).length() + String.valueOf(var3).length())).append(var2).append(".").append(var3).toString());
   }

   public static boolean isValid(String name) {
      try {
         InternetDomainName unused = from(name);
         return true;
      } catch (IllegalArgumentException var2) {
         return false;
      }
   }

   private static boolean matchesWildcardSuffixType(Optional<PublicSuffixType> desiredType, String domain) {
      List<String> pieces = DOT_SPLITTER.limit(2).splitToList(domain);
      return pieces.size() == 2 && matchesType(desiredType, Optional.fromNullable((PublicSuffixType)PublicSuffixPatterns.UNDER.get(pieces.get(1))));
   }

   private static boolean matchesType(Optional<PublicSuffixType> desiredType, Optional<PublicSuffixType> actualType) {
      return desiredType.isPresent() ? desiredType.equals(actualType) : actualType.isPresent();
   }

   public String toString() {
      return this.name;
   }

   public boolean equals(@CheckForNull Object object) {
      if (object == this) {
         return true;
      } else if (object instanceof InternetDomainName) {
         InternetDomainName that = (InternetDomainName)object;
         return this.name.equals(that.name);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   static {
      PART_CHAR_MATCHER = DIGIT_MATCHER.or(LETTER_MATCHER).or(DASH_MATCHER);
   }
}
