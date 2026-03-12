package fr.xephi.authme.libs.org.postgresql.replication.fluent.logical;

import fr.xephi.authme.libs.org.postgresql.replication.PGReplicationStream;
import java.sql.SQLException;

public interface StartLogicalReplicationCallback {
   PGReplicationStream start(LogicalReplicationOptions var1) throws SQLException;
}
