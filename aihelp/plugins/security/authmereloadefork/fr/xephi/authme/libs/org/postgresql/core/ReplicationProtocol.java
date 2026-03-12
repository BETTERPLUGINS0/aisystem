package fr.xephi.authme.libs.org.postgresql.core;

import fr.xephi.authme.libs.org.postgresql.replication.PGReplicationStream;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.logical.LogicalReplicationOptions;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.physical.PhysicalReplicationOptions;
import java.sql.SQLException;

public interface ReplicationProtocol {
   PGReplicationStream startLogical(LogicalReplicationOptions var1) throws SQLException;

   PGReplicationStream startPhysical(PhysicalReplicationOptions var1) throws SQLException;
}
