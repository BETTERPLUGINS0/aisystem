package com.lenis0012.bukkit.loginsecurity.util;

import com.lenis0012.bukkit.loginsecurity.LoginSecurityConfig;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

public class LoggingFilter extends AbstractFilter {
   private static final List<String> filteredWords = Arrays.asList("/register", "/login", "/changepassword", "/changepass");
   private final LoginSecurityConfig loginSecurityConfig;

   Result denyIfExposesPassword(String message) {
      if (message == null) {
         return Result.NEUTRAL;
      } else {
         message = message.toLowerCase();
         Iterator var2 = filteredWords.iterator();

         String registerShortcut;
         do {
            if (!var2.hasNext()) {
               if (this.loginSecurityConfig.isUseCommandShortcut()) {
                  String loginShortcut = this.loginSecurityConfig.getLoginCommandShortcut().trim() + ' ';
                  registerShortcut = this.loginSecurityConfig.getRegisterCommandShortcut().trim() + ' ';
                  if (message.startsWith(loginShortcut) || message.contains("issued server command: " + loginShortcut) || message.startsWith(registerShortcut) || message.contains("issued server command: " + registerShortcut)) {
                     return Result.DENY;
                  }
               }

               return Result.NEUTRAL;
            }

            registerShortcut = (String)var2.next();
         } while(!message.startsWith(registerShortcut) && !message.contains("issued server command: " + registerShortcut));

         return Result.DENY;
      }
   }

   public Result filter(LogEvent event) {
      return this.denyIfExposesPassword(event.getMessage().getFormattedMessage());
   }

   public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
      return this.denyIfExposesPassword(msg.getFormattedMessage());
   }

   public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
      return this.denyIfExposesPassword(msg.toString());
   }

   public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
      return this.denyIfExposesPassword(msg);
   }

   public LoggingFilter(LoginSecurityConfig loginSecurityConfig) {
      this.loginSecurityConfig = loginSecurityConfig;
   }
}
