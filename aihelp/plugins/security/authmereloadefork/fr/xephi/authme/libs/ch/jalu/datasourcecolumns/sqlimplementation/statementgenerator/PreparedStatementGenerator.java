package fr.xephi.authme.libs.ch.jalu.datasourcecolumns.sqlimplementation.statementgenerator;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementGenerator extends AutoCloseable {
   PreparedStatement createStatement() throws SQLException;

   void close() throws SQLException;
}
