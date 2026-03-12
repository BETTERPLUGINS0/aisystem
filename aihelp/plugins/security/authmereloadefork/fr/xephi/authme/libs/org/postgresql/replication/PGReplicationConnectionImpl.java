package fr.xephi.authme.libs.org.postgresql.replication;

import fr.xephi.authme.libs.org.postgresql.core.BaseConnection;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.ChainedCreateReplicationSlotBuilder;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.ChainedStreamBuilder;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.ReplicationCreateSlotBuilder;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.ReplicationStreamBuilder;
import java.sql.SQLException;
import java.sql.Statement;

public class PGReplicationConnectionImpl implements PGReplicationConnection {
   private final BaseConnection connection;

   public PGReplicationConnectionImpl(BaseConnection connection) {
      this.connection = connection;
   }

   public ChainedStreamBuilder replicationStream() {
      return new ReplicationStreamBuilder(this.connection);
   }

   public ChainedCreateReplicationSlotBuilder createReplicationSlot() {
      return new ReplicationCreateSlotBuilder(this.connection);
   }

   public void dropReplicationSlot(String slotName) throws SQLException {
      if (slotName != null && !slotName.isEmpty()) {
         Statement statement = this.connection.createStatement();

         try {
            statement.execute("DROP_REPLICATION_SLOT " + slotName);
         } finally {
            statement.close();
         }

      } else {
         throw new IllegalArgumentException("Replication slot name can't be null or empty");
      }
   }
}
