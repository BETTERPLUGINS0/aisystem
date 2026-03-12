package fr.xephi.authme.libs.org.postgresql.core;

import fr.xephi.authme.libs.org.postgresql.PGStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface BaseStatement extends PGStatement, Statement {
   ResultSet createDriverResultSet(Field[] var1, List<Tuple> var2) throws SQLException;

   ResultSet createResultSet(Query var1, Field[] var2, List<Tuple> var3, ResultCursor var4) throws SQLException;

   boolean executeWithFlags(String var1, int var2) throws SQLException;

   boolean executeWithFlags(CachedQuery var1, int var2) throws SQLException;

   boolean executeWithFlags(int var1) throws SQLException;
}
