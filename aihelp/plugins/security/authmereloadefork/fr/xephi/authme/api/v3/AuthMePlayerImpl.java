package fr.xephi.authme.api.v3;

import fr.xephi.authme.data.auth.PlayerAuth;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

class AuthMePlayerImpl implements AuthMePlayer {
   private String name;
   private UUID uuid;
   private String email;
   private Instant registrationDate;
   private String registrationIpAddress;
   private Instant lastLoginDate;
   private String lastLoginIpAddress;

   static Optional<AuthMePlayer> fromPlayerAuth(PlayerAuth playerAuth) {
      if (playerAuth == null) {
         return Optional.empty();
      } else {
         AuthMePlayerImpl authMeUser = new AuthMePlayerImpl();
         authMeUser.name = playerAuth.getRealName();
         authMeUser.uuid = playerAuth.getUuid();
         authMeUser.email = (String)nullIfDefault(playerAuth.getEmail(), "your@email.com");
         Long lastLoginMillis = (Long)nullIfDefault(playerAuth.getLastLogin(), 0L);
         authMeUser.registrationDate = toInstant(playerAuth.getRegistrationDate());
         authMeUser.registrationIpAddress = playerAuth.getRegistrationIp();
         authMeUser.lastLoginDate = toInstant(lastLoginMillis);
         authMeUser.lastLoginIpAddress = (String)nullIfDefault(playerAuth.getLastIp(), "127.0.0.1");
         return Optional.of(authMeUser);
      }
   }

   public String getName() {
      return this.name;
   }

   public Optional<UUID> getUuid() {
      return Optional.ofNullable(this.uuid);
   }

   public Optional<String> getEmail() {
      return Optional.ofNullable(this.email);
   }

   public Instant getRegistrationDate() {
      return this.registrationDate;
   }

   public Optional<String> getRegistrationIpAddress() {
      return Optional.ofNullable(this.registrationIpAddress);
   }

   public Optional<Instant> getLastLoginDate() {
      return Optional.ofNullable(this.lastLoginDate);
   }

   public Optional<String> getLastLoginIpAddress() {
      return Optional.ofNullable(this.lastLoginIpAddress);
   }

   private static Instant toInstant(Long epochMillis) {
      return epochMillis == null ? null : Instant.ofEpochMilli(epochMillis);
   }

   private static <T> T nullIfDefault(T value, T defaultValue) {
      return defaultValue.equals(value) ? null : value;
   }
}
