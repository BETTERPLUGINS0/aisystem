package fr.xephi.authme.libs.org.postgresql.core.v3.replication;

import fr.xephi.authme.libs.org.postgresql.copy.CopyDual;
import fr.xephi.authme.libs.org.postgresql.core.PGStream;
import fr.xephi.authme.libs.org.postgresql.core.QueryExecutor;
import fr.xephi.authme.libs.org.postgresql.core.ReplicationProtocol;
import fr.xephi.authme.libs.org.postgresql.replication.PGReplicationStream;
import fr.xephi.authme.libs.org.postgresql.replication.ReplicationType;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.CommonOptions;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.logical.LogicalReplicationOptions;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.physical.PhysicalReplicationOptions;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class V3ReplicationProtocol implements ReplicationProtocol {
   private static final Logger LOGGER = Logger.getLogger(V3ReplicationProtocol.class.getName());
   private final QueryExecutor queryExecutor;
   private final PGStream pgStream;

   public V3ReplicationProtocol(QueryExecutor queryExecutor, PGStream pgStream) {
      this.queryExecutor = queryExecutor;
      this.pgStream = pgStream;
   }

   public PGReplicationStream startLogical(LogicalReplicationOptions options) throws SQLException {
      String query = this.createStartLogicalQuery(options);
      return this.initializeReplication(query, options, ReplicationType.LOGICAL);
   }

   public PGReplicationStream startPhysical(PhysicalReplicationOptions options) throws SQLException {
      String query = this.createStartPhysicalQuery(options);
      return this.initializeReplication(query, options, ReplicationType.PHYSICAL);
   }

   private PGReplicationStream initializeReplication(String query, CommonOptions options, ReplicationType replicationType) throws SQLException {
      LOGGER.log(Level.FINEST, " FE=> StartReplication(query: {0})", query);
      this.configureSocketTimeout(options);
      CopyDual copyDual = (CopyDual)this.queryExecutor.startCopy(query, true);
      return new V3PGReplicationStream((CopyDual)Nullness.castNonNull(copyDual), options.getStartLSNPosition(), (long)options.getStatusInterval(), replicationType);
   }

   private String createStartPhysicalQuery(PhysicalReplicationOptions options) {
      StringBuilder builder = new StringBuilder();
      builder.append("START_REPLICATION");
      if (options.getSlotName() != null) {
         builder.append(" SLOT ").append(options.getSlotName());
      }

      builder.append(" PHYSICAL ").append(options.getStartLSNPosition().asString());
      return builder.toString();
   }

   private String createStartLogicalQuery(LogicalReplicationOptions options) {
      StringBuilder builder = new StringBuilder();
      builder.append("START_REPLICATION SLOT ").append(options.getSlotName()).append(" LOGICAL ").append(options.getStartLSNPosition().asString());
      Properties slotOptions = options.getSlotOptions();
      if (slotOptions.isEmpty()) {
         return builder.toString();
      } else {
         builder.append(" (");
         boolean isFirst = true;

         String name;
         for(Iterator var5 = slotOptions.stringPropertyNames().iterator(); var5.hasNext(); builder.append('"').append(name).append('"').append(" ").append('\'').append(slotOptions.getProperty(name)).append('\'')) {
            name = (String)var5.next();
            if (isFirst) {
               isFirst = false;
            } else {
               builder.append(", ");
            }
         }

         builder.append(")");
         return builder.toString();
      }
   }

   private void configureSocketTimeout(CommonOptions options) throws PSQLException {
      if (options.getStatusInterval() != 0) {
         try {
            int previousTimeOut = this.pgStream.getSocket().getSoTimeout();
            int minimalTimeOut;
            if (previousTimeOut > 0) {
               minimalTimeOut = Math.min(previousTimeOut, options.getStatusInterval());
            } else {
               minimalTimeOut = options.getStatusInterval();
            }

            this.pgStream.getSocket().setSoTimeout(minimalTimeOut);
            this.pgStream.setMinStreamAvailableCheckDelay(0);
         } catch (IOException var4) {
            throw new PSQLException(GT.tr("The connection attempt failed."), PSQLState.CONNECTION_UNABLE_TO_CONNECT, var4);
         }
      }
   }
}
