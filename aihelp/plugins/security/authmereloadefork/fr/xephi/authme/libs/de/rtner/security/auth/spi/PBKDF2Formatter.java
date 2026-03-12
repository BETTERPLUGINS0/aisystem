package fr.xephi.authme.libs.de.rtner.security.auth.spi;

public interface PBKDF2Formatter {
   String toString(PBKDF2Parameters var1);

   boolean fromString(PBKDF2Parameters var1, String var2);
}
