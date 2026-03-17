package me.PM2.infinitevehicles.commands;

public enum LogLevel {
   INFO,
   ERROR;

   static final String LOG_PREFIX = "[ACF] ";

   // $FF: synthetic method
   private static LogLevel[] $values() {
      return new LogLevel[]{INFO, ERROR};
   }
}
