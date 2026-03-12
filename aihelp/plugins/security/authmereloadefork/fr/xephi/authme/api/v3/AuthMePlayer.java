package fr.xephi.authme.api.v3;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface AuthMePlayer {
   String getName();

   Optional<UUID> getUuid();

   Optional<String> getEmail();

   Instant getRegistrationDate();

   Optional<String> getRegistrationIpAddress();

   Optional<Instant> getLastLoginDate();

   Optional<String> getLastLoginIpAddress();
}
