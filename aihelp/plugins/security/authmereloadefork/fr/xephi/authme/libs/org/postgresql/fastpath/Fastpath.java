package fr.xephi.authme.libs.org.postgresql.fastpath;

import fr.xephi.authme.libs.org.postgresql.core.BaseConnection;
import fr.xephi.authme.libs.org.postgresql.core.ParameterList;
import fr.xephi.authme.libs.org.postgresql.core.QueryExecutor;
import fr.xephi.authme.libs.org.postgresql.util.ByteConverter;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.checkerframework.checker.nullness.qual.Nullable;

/** @deprecated */
@Deprecated
public class Fastpath {
   private static final long NUM_OIDS = 4294967296L;
   private final Map<String, Integer> func = new HashMap();
   private final QueryExecutor executor;
   private final BaseConnection connection;

   public Fastpath(BaseConnection conn) {
      this.connection = conn;
      this.executor = conn.getQueryExecutor();
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public Object fastpath(int fnId, boolean resultType, FastpathArg[] args) throws SQLException {
      byte[] returnValue = this.fastpath(fnId, args);
      if (resultType && returnValue != null) {
         if (returnValue.length == 4) {
            return ByteConverter.int4(returnValue, 0);
         } else if (returnValue.length == 8) {
            return ByteConverter.int8(returnValue, 0);
         } else {
            throw new PSQLException(GT.tr("Fastpath call {0} - No result was returned and we expected a numeric.", fnId), PSQLState.NO_DATA);
         }
      } else {
         return returnValue;
      }
   }

   @Nullable
   public byte[] fastpath(int fnId, FastpathArg[] args) throws SQLException {
      ParameterList params = this.executor.createFastpathParameters(args.length);

      for(int i = 0; i < args.length; ++i) {
         args[i].populateParameter(params, i + 1);
      }

      return this.executor.fastpathCall(fnId, params, this.connection.getAutoCommit());
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public Object fastpath(String name, boolean resulttype, FastpathArg[] args) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "Fastpath: calling {0}", name);
      return this.fastpath(this.getID(name), resulttype, args);
   }

   @Nullable
   public byte[] fastpath(String name, FastpathArg[] args) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "Fastpath: calling {0}", name);
      return this.fastpath(this.getID(name), args);
   }

   public int getInteger(String name, FastpathArg[] args) throws SQLException {
      byte[] returnValue = this.fastpath(name, args);
      if (returnValue == null) {
         throw new PSQLException(GT.tr("Fastpath call {0} - No result was returned and we expected an integer.", name), PSQLState.NO_DATA);
      } else if (returnValue.length == 4) {
         return ByteConverter.int4(returnValue, 0);
      } else {
         throw new PSQLException(GT.tr("Fastpath call {0} - No result was returned or wrong size while expecting an integer.", name), PSQLState.NO_DATA);
      }
   }

   public long getLong(String name, FastpathArg[] args) throws SQLException {
      byte[] returnValue = this.fastpath(name, args);
      if (returnValue == null) {
         throw new PSQLException(GT.tr("Fastpath call {0} - No result was returned and we expected a long.", name), PSQLState.NO_DATA);
      } else if (returnValue.length == 8) {
         return ByteConverter.int8(returnValue, 0);
      } else {
         throw new PSQLException(GT.tr("Fastpath call {0} - No result was returned or wrong size while expecting a long.", name), PSQLState.NO_DATA);
      }
   }

   public long getOID(String name, FastpathArg[] args) throws SQLException {
      long oid = (long)this.getInteger(name, args);
      if (oid < 0L) {
         oid += 4294967296L;
      }

      return oid;
   }

   @Nullable
   public byte[] getData(String name, FastpathArg[] args) throws SQLException {
      return this.fastpath(name, args);
   }

   public void addFunction(String name, int fnid) {
      this.func.put(name, fnid);
   }

   public void addFunctions(ResultSet rs) throws SQLException {
      while(rs.next()) {
         this.func.put((String)Nullness.castNonNull(rs.getString(1)), rs.getInt(2));
      }

   }

   public int getID(String name) throws SQLException {
      Integer id = (Integer)this.func.get(name);
      if (id == null) {
         throw new PSQLException(GT.tr("The fastpath function {0} is unknown.", name), PSQLState.UNEXPECTED_ERROR);
      } else {
         return id;
      }
   }

   public static FastpathArg createOIDArg(long oid) {
      if (oid > 2147483647L) {
         oid -= 4294967296L;
      }

      return new FastpathArg((int)oid);
   }
}
