package fr.xephi.authme.libs.org.picketbox.util;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.plugins.PBEUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class StringUtil {
   public static final String PROPERTY_DEFAULT_SEPARATOR = "::";

   public static boolean isNotNull(String str) {
      return str != null && !"".equals(str.trim());
   }

   public static boolean isNullOrEmpty(String str) {
      return str == null || str.isEmpty();
   }

   public static String getSystemPropertyAsString(String str) {
      if (str == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("str");
      } else {
         if (str.contains("${")) {
            Pattern pattern = Pattern.compile("\\$\\{([^}]+)}");
            Matcher matcher = pattern.matcher(str);
            StringBuffer buffer = new StringBuffer();
            String sysPropertyValue = null;

            while(matcher.find()) {
               String subString = matcher.group(1);
               String defaultValue = "";
               if (subString.contains("::")) {
                  int index = subString.indexOf("::");
                  defaultValue = subString.substring(index + "::".length());
                  subString = subString.substring(0, index);
               }

               sysPropertyValue = SecurityActions.getSystemProperty(subString, defaultValue);
               if (sysPropertyValue.isEmpty()) {
                  throw PicketBoxMessages.MESSAGES.missingSystemProperty(matcher.group(1));
               }

               matcher.appendReplacement(buffer, sysPropertyValue.replace("\\", "\\\\"));
            }

            matcher.appendTail(buffer);
            str = buffer.toString();
         }

         return str;
      }
   }

   public static void match(String first, String second) {
      if (!first.equals(second)) {
         throw PicketBoxMessages.MESSAGES.failedToMatchStrings(first, second);
      }
   }

   public static List<String> tokenize(String str) {
      List<String> list = new ArrayList();
      StringTokenizer tokenizer = new StringTokenizer(str, ",");

      while(tokenizer.hasMoreTokens()) {
         list.add(tokenizer.nextToken());
      }

      return list;
   }

   public static String decode(String maskedString, String salt, int iterationCount) throws Exception {
      String PASS_MASK_PREFIX = "MASK-";
      String pbeAlgo = "PBEwithMD5andDES";
      if (maskedString.startsWith(PASS_MASK_PREFIX)) {
         SecretKeyFactory factory = SecretKeyFactory.getInstance(pbeAlgo);
         char[] password = "somearbitrarycrazystringthatdoesnotmatter".toCharArray();
         PBEParameterSpec cipherSpec = new PBEParameterSpec(salt.getBytes(), iterationCount);
         PBEKeySpec keySpec = new PBEKeySpec(password);
         SecretKey cipherKey = factory.generateSecret(keySpec);
         maskedString = maskedString.substring(PASS_MASK_PREFIX.length());
         String decodedValue = PBEUtils.decode64(maskedString, pbeAlgo, cipherKey, cipherSpec);
         maskedString = decodedValue;
      }

      return maskedString;
   }
}
