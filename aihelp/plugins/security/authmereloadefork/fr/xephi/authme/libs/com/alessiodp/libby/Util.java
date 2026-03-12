package fr.xephi.authme.libs.com.alessiodp.libby;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Util {
   private Util() {
      throw new UnsupportedOperationException("Util class.");
   }

   @NotNull
   public static String replaceWithDots(@NotNull String str) {
      return str.replace("{}", ".");
   }

   public static byte[] hexStringToByteArray(@NotNull String string) {
      int len = string.length();
      byte[] data = new byte[len / 2];

      for(int i = 0; i < len; i += 2) {
         data[i / 2] = (byte)((Character.digit(string.charAt(i), 16) << 4) + Character.digit(string.charAt(i + 1), 16));
      }

      return data;
   }

   @NotNull
   public static String craftPartialPath(@NotNull String artifactId, @NotNull String groupId, @NotNull String version) {
      return groupId.replace('.', '/') + '/' + artifactId + '/' + version + '/';
   }

   @NotNull
   public static String craftPath(@NotNull String partialPath, @NotNull String artifactId, @NotNull String version, @Nullable String classifier) {
      String path = partialPath + artifactId + '-' + version;
      if (classifier != null && !classifier.isEmpty()) {
         path = path + '-' + classifier;
      }

      return path + ".jar";
   }
}
