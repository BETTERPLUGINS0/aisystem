package fr.xephi.authme.libs.org.postgresql.plugin;

import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface AuthenticationPlugin {
   @Nullable
   char[] getPassword(AuthenticationRequestType var1) throws PSQLException;
}
