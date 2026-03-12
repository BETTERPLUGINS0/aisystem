package fr.xephi.authme.libs.org.postgresql;

import java.sql.SQLException;

public interface PGStatement {
   long DATE_POSITIVE_INFINITY = 9223372036825200000L;
   long DATE_NEGATIVE_INFINITY = -9223372036832400000L;
   long DATE_POSITIVE_SMALLER_INFINITY = 185543533774800000L;
   long DATE_NEGATIVE_SMALLER_INFINITY = -185543533774800000L;

   long getLastOID() throws SQLException;

   /** @deprecated */
   @Deprecated
   void setUseServerPrepare(boolean var1) throws SQLException;

   boolean isUseServerPrepare();

   void setPrepareThreshold(int var1) throws SQLException;

   int getPrepareThreshold();

   void setAdaptiveFetch(boolean var1);

   boolean getAdaptiveFetch();
}
