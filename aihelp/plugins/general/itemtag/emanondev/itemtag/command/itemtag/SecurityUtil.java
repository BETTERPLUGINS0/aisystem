package emanondev.itemtag.command.itemtag;

import emanondev.itemedit.YMLConfig;
import emanondev.itemtag.ItemTag;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class SecurityUtil {
   private static UUID uuid;

   @NotNull
   private static UUID getUUID() {
      if (uuid == null) {
         YMLConfig config = ItemTag.get().getConfig("security.yml");

         try {
            String txt = config.getString("server_uuid", (String)null);
            if (txt != null) {
               uuid = UUID.fromString(txt);
            } else {
               uuid = UUID.randomUUID();
               config.set("server_uuid", uuid.toString());
               config.save();
            }
         } catch (Exception var2) {
            var2.printStackTrace();
            uuid = UUID.randomUUID();
            config.set("server_uuid", uuid.toString());
            config.save();
         }
      }

      return uuid;
   }

   public static String generateControlKey(@NotNull String text) {
      try {
         MessageDigest digest = MessageDigest.getInstance("SHA-256");
         byte[] encodedHash = digest.digest((getUUID() + text).getBytes(StandardCharsets.UTF_8));
         StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
         byte[] var4 = encodedHash;
         int var5 = encodedHash.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            byte b = var4[var6];
            String hex = Integer.toHexString(255 & b);
            if (hex.length() == 1) {
               hexString.append('0');
            }

            hexString.append(hex);
         }

         return hexString.toString();
      } catch (NoSuchAlgorithmException var9) {
         throw new IllegalStateException("Platform doesn't support SHA-256");
      }
   }

   public static boolean verifyControlKey(@NotNull String text, @NotNull String controlKey) {
      return controlKey.equals(generateControlKey(text));
   }
}
