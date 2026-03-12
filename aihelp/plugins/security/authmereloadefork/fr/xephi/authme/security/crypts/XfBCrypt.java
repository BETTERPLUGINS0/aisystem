package fr.xephi.authme.security.crypts;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XfBCrypt extends BCryptBasedHash {
   public static final String SCHEME_CLASS = "XenForo_Authentication_Core12";
   private static final Pattern HASH_PATTERN = Pattern.compile("\"hash\";s.*\"(.*)?\"");

   XfBCrypt() {
      super(new BCryptHasher(fr.xephi.authme.libs.at.favre.lib.crypto.bcrypt.BCrypt.Version.VERSION_2A, 10));
   }

   public static String getHashFromBlob(byte[] blob) {
      String line = new String(blob);
      Matcher m = HASH_PATTERN.matcher(line);
      return m.find() ? m.group(1) : "*";
   }

   public static String serializeHash(String hash) {
      return "a:1:{s:4:\"hash\";s:" + hash.length() + ":\"" + hash + "\";}";
   }
}
