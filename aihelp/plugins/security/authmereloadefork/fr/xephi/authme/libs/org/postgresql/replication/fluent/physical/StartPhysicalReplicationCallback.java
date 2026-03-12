package fr.xephi.authme.libs.org.postgresql.replication.fluent.physical;

import fr.xephi.authme.libs.org.postgresql.replication.PGReplicationStream;
import java.sql.SQLException;

public interface StartPhysicalReplicationCallback {
   PGReplicationStream start(PhysicalReplicationOptions var1) throws SQLException;
}
