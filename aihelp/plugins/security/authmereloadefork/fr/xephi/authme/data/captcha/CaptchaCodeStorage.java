package fr.xephi.authme.data.captcha;

import fr.xephi.authme.util.RandomStringUtils;
import fr.xephi.authme.util.expiring.ExpiringMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CaptchaCodeStorage {
   private ExpiringMap<String, String> captchaCodes;
   private int captchaLength;

   public CaptchaCodeStorage(long expirationInMinutes, int captchaLength) {
      this.captchaCodes = new ExpiringMap(expirationInMinutes, TimeUnit.MINUTES);
      this.captchaLength = captchaLength;
   }

   public void setExpirationInMinutes(long expirationInMinutes) {
      this.captchaCodes.setExpiration(expirationInMinutes, TimeUnit.MINUTES);
   }

   public void setCaptchaLength(int captchaLength) {
      this.captchaLength = captchaLength;
   }

   public String getCodeOrGenerateNew(String name) {
      String code = (String)this.captchaCodes.get(name.toLowerCase(Locale.ROOT));
      return code == null ? this.generateCode(name) : code;
   }

   private String generateCode(String name) {
      String code = RandomStringUtils.generate(this.captchaLength);
      this.captchaCodes.put(name.toLowerCase(Locale.ROOT), code);
      return code;
   }

   public boolean checkCode(String name, String code) {
      String nameLowerCase = name.toLowerCase(Locale.ROOT);
      String savedCode = (String)this.captchaCodes.get(nameLowerCase);
      if (savedCode != null && savedCode.equalsIgnoreCase(code)) {
         this.captchaCodes.remove(nameLowerCase);
         return true;
      } else {
         this.generateCode(name);
         return false;
      }
   }

   public void removeExpiredEntries() {
      this.captchaCodes.removeExpiredEntries();
   }
}
