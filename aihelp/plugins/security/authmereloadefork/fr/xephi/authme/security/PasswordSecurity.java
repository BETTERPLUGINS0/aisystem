package fr.xephi.authme.security;

import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.events.PasswordEncryptionEvent;
import fr.xephi.authme.initialization.Reloadable;
import fr.xephi.authme.libs.ch.jalu.injector.factory.Factory;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.security.crypts.EncryptionMethod;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import javax.annotation.PostConstruct;
import org.bukkit.plugin.PluginManager;

public class PasswordSecurity implements Reloadable {
   @Inject
   private Settings settings;
   @Inject
   private DataSource dataSource;
   @Inject
   private PluginManager pluginManager;
   @Inject
   private Factory<EncryptionMethod> encryptionMethodFactory;
   private EncryptionMethod encryptionMethod;
   private Collection<HashAlgorithm> legacyAlgorithms;

   @PostConstruct
   public void reload() {
      HashAlgorithm algorithm = (HashAlgorithm)this.settings.getProperty(SecuritySettings.PASSWORD_HASH);
      this.encryptionMethod = this.initializeEncryptionMethodWithEvent(algorithm);
      this.legacyAlgorithms = (Collection)this.settings.getProperty(SecuritySettings.LEGACY_HASHES);
   }

   public HashedPassword computeHash(String password, String playerName) {
      String playerLowerCase = playerName.toLowerCase(Locale.ROOT);
      return this.encryptionMethod.computeHash(password, playerLowerCase);
   }

   public boolean comparePassword(String password, String playerName) {
      HashedPassword auth = this.dataSource.getPassword(playerName);
      return auth != null && this.comparePassword(password, auth, playerName);
   }

   public boolean comparePassword(String password, HashedPassword hashedPassword, String playerName) {
      String playerLowerCase = playerName.toLowerCase(Locale.ROOT);
      return methodMatches(this.encryptionMethod, password, hashedPassword, playerLowerCase) || this.compareWithLegacyHashes(password, hashedPassword, playerLowerCase);
   }

   private boolean compareWithLegacyHashes(String password, HashedPassword hashedPassword, String playerName) {
      Iterator var4 = this.legacyAlgorithms.iterator();

      EncryptionMethod method;
      do {
         if (!var4.hasNext()) {
            return false;
         }

         HashAlgorithm algorithm = (HashAlgorithm)var4.next();
         method = this.initializeEncryptionMethod(algorithm);
      } while(!methodMatches(method, password, hashedPassword, playerName));

      this.hashAndSavePasswordWithNewAlgorithm(password, playerName);
      return true;
   }

   private static boolean methodMatches(EncryptionMethod method, String password, HashedPassword hashedPassword, String playerName) {
      return method != null && (!method.hasSeparateSalt() || hashedPassword.getSalt() != null) && method.comparePassword(password, hashedPassword, playerName);
   }

   private EncryptionMethod initializeEncryptionMethodWithEvent(HashAlgorithm algorithm) {
      EncryptionMethod method = this.initializeEncryptionMethod(algorithm);
      PasswordEncryptionEvent event = new PasswordEncryptionEvent(method);
      this.pluginManager.callEvent(event);
      return event.getMethod();
   }

   private EncryptionMethod initializeEncryptionMethod(HashAlgorithm algorithm) {
      return !HashAlgorithm.CUSTOM.equals(algorithm) && !HashAlgorithm.PLAINTEXT.equals(algorithm) ? (EncryptionMethod)this.encryptionMethodFactory.newInstance(algorithm.getClazz()) : null;
   }

   private void hashAndSavePasswordWithNewAlgorithm(String password, String playerName) {
      HashedPassword hashedPassword = this.encryptionMethod.computeHash(password, playerName);
      this.dataSource.updatePassword(playerName, hashedPassword);
   }
}
