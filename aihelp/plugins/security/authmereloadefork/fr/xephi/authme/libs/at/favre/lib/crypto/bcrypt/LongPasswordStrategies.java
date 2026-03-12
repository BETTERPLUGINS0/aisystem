package fr.xephi.authme.libs.at.favre.lib.crypto.bcrypt;

import java.util.Objects;

public final class LongPasswordStrategies {
   private LongPasswordStrategies() {
   }

   public static LongPasswordStrategy truncate(BCrypt.Version version) {
      return new LongPasswordStrategy.TruncateStrategy(((BCrypt.Version)Objects.requireNonNull(version)).allowedMaxPwLength);
   }

   public static LongPasswordStrategy hashSha512(BCrypt.Version version) {
      return new LongPasswordStrategy.Sha512DerivationStrategy(((BCrypt.Version)Objects.requireNonNull(version)).allowedMaxPwLength);
   }

   public static LongPasswordStrategy strict(BCrypt.Version version) {
      return new LongPasswordStrategy.StrictMaxPasswordLengthStrategy(((BCrypt.Version)Objects.requireNonNull(version)).allowedMaxPwLength);
   }

   public static LongPasswordStrategy none() {
      return new LongPasswordStrategy.PassThroughStrategy();
   }
}
