package fr.xephi.authme.libs.ch.jalu.datasourcecolumns.sqlimplementation.statementgenerator;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface ConnectionSupplier {
   Connection get() throws SQLException;
}
