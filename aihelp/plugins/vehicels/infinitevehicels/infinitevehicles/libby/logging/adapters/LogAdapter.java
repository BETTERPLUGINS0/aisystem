package me.PM2.infinitevehicles.libby.logging.adapters;

import me.PM2.infinitevehicles.libby.logging.LogLevel;

public interface LogAdapter {
   void log(LogLevel level, String message);

   void log(LogLevel level, String message, Throwable throwable);
}
