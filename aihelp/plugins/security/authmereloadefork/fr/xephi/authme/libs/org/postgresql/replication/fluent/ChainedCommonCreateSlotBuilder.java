package fr.xephi.authme.libs.org.postgresql.replication.fluent;

import fr.xephi.authme.libs.org.postgresql.replication.ReplicationSlotInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

public interface ChainedCommonCreateSlotBuilder<T extends ChainedCommonCreateSlotBuilder<T>> {
   T withSlotName(String var1);

   T withTemporaryOption() throws SQLFeatureNotSupportedException;

   ReplicationSlotInfo make() throws SQLException;
}
