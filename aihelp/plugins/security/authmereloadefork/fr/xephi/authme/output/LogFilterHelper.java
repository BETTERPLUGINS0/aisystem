package fr.xephi.authme.output;

import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.util.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

final class LogFilterHelper {
   @VisibleForTesting
   static final List<String> COMMANDS_TO_SKIP = withAndWithoutAuthMePrefix("/login ", "/l ", "/log ", "/register ", "/reg ", "/unregister ", "/unreg ", "/changepassword ", "/cp ", "/changepass ", "/authme register ", "/authme reg ", "/authme r ", "/authme changepassword ", "/authme password ", "/authme changepass ", "/authme cp ", "/email setpassword ");
   private static final String ISSUED_COMMAND_TEXT = "issued server command:";

   private LogFilterHelper() {
   }

   static boolean isSensitiveAuthMeCommand(String message) {
      if (message == null) {
         return false;
      } else {
         String lowerMessage = message.toLowerCase(Locale.ROOT);
         return lowerMessage.contains("issued server command:") && StringUtils.containsAny(lowerMessage, COMMANDS_TO_SKIP);
      }
   }

   private static List<String> withAndWithoutAuthMePrefix(String... commands) {
      List<String> commandList = new ArrayList(commands.length * 2);
      String[] var2 = commands;
      int var3 = commands.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String command = var2[var4];
         commandList.add(command);
         commandList.add(command.substring(0, 1) + "authme:" + command.substring(1));
      }

      return Collections.unmodifiableList(commandList);
   }
}
