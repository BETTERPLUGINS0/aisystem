package fr.xephi.authme.message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MessagePathHelper {
   public static final String DEFAULT_LANGUAGE = "en";
   public static final String MESSAGES_FOLDER = "messages/";
   public static final String DEFAULT_MESSAGES_FILE = createMessageFilePath("en");
   private static final Pattern MESSAGE_FILE_PATTERN = Pattern.compile("messages_([a-z]+)\\.yml");
   private static final Pattern HELP_MESSAGES_FILE = Pattern.compile("help_[a-z]+\\.yml");

   private MessagePathHelper() {
   }

   public static String createMessageFilePath(String languageCode) {
      return "messages/messages_" + languageCode + ".yml";
   }

   public static String createHelpMessageFilePath(String languageCode) {
      return "messages/help_" + languageCode + ".yml";
   }

   public static boolean isMessagesFile(String filename) {
      return MESSAGE_FILE_PATTERN.matcher(filename).matches();
   }

   public static String getLanguageIfIsMessagesFile(String filename) {
      Matcher matcher = MESSAGE_FILE_PATTERN.matcher(filename);
      return matcher.matches() ? matcher.group(1) : null;
   }

   public static boolean isHelpFile(String filename) {
      return HELP_MESSAGES_FILE.matcher(filename).matches();
   }
}
