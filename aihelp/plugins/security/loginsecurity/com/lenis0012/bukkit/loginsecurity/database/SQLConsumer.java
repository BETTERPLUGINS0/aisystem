package com.lenis0012.bukkit.loginsecurity.database;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLConsumer<T> {
   void accept(T var1) throws SQLException;
}
