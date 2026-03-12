package fr.xephi.authme.libs.org.postgresql.replication.fluent;

import fr.xephi.authme.libs.org.postgresql.core.BaseConnection;
import fr.xephi.authme.libs.org.postgresql.core.ReplicationProtocol;
import fr.xephi.authme.libs.org.postgresql.replication.PGReplicationStream;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.logical.ChainedLogicalStreamBuilder;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.logical.LogicalReplicationOptions;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.logical.LogicalStreamBuilder;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.logical.StartLogicalReplicationCallback;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.physical.ChainedPhysicalStreamBuilder;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.physical.PhysicalReplicationOptions;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.physical.PhysicalStreamBuilder;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.physical.StartPhysicalReplicationCallback;
import java.sql.SQLException;

public class ReplicationStreamBuilder implements ChainedStreamBuilder {
   private final BaseConnection baseConnection;

   public ReplicationStreamBuilder(BaseConnection connection) {
      this.baseConnection = connection;
   }

   public ChainedLogicalStreamBuilder logical() {
      return new LogicalStreamBuilder(new StartLogicalReplicationCallback() {
         public PGReplicationStream start(LogicalReplicationOptions options) throws SQLException {
            ReplicationProtocol protocol = ReplicationStreamBuilder.this.baseConnection.getReplicationProtocol();
            return protocol.startLogical(options);
         }
      });
   }

   public ChainedPhysicalStreamBuilder physical() {
      return new PhysicalStreamBuilder(new StartPhysicalReplicationCallback() {
         public PGReplicationStream start(PhysicalReplicationOptions options) throws SQLException {
            ReplicationProtocol protocol = ReplicationStreamBuilder.this.baseConnection.getReplicationProtocol();
            return protocol.startPhysical(options);
         }
      });
   }
}
