package fr.xephi.authme.libs.org.postgresql.replication.fluent.physical;

import fr.xephi.authme.libs.org.postgresql.replication.PGReplicationStream;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.ChainedCommonStreamBuilder;
import java.sql.SQLException;

public interface ChainedPhysicalStreamBuilder extends ChainedCommonStreamBuilder<ChainedPhysicalStreamBuilder> {
   PGReplicationStream start() throws SQLException;
}
