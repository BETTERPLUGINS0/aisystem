package fr.xephi.authme.libs.org.jboss.security.auth.spi;

public interface InputValidator {
   void validateUsernameAndPassword(String var1, String var2) throws InputValidationException;
}
