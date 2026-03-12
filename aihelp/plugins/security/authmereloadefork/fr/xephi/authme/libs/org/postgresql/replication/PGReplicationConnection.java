package fr.xephi.authme.libs.org.postgresql.replication;

import fr.xephi.authme.libs.org.postgresql.replication.fluent.ChainedCreateReplicationSlotBuilder;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.ChainedStreamBuilder;
import java.sql.SQLException;

public interface PGReplicationConnection {
   ChainedStreamBuilder replicationStream();

   ChainedCreateReplicationSlotBuilder createReplicationSlot();

   void dropReplicationSlot(String var1) throws SQLException;
}
