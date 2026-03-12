package fr.xephi.authme.libs.org.postgresql.replication.fluent.logical;

import fr.xephi.authme.libs.org.postgresql.replication.PGReplicationStream;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.ChainedCommonStreamBuilder;
import java.sql.SQLException;
import java.util.Properties;

public interface ChainedLogicalStreamBuilder extends ChainedCommonStreamBuilder<ChainedLogicalStreamBuilder> {
   PGReplicationStream start() throws SQLException;

   ChainedLogicalStreamBuilder withSlotOption(String var1, boolean var2);

   ChainedLogicalStreamBuilder withSlotOption(String var1, int var2);

   ChainedLogicalStreamBuilder withSlotOption(String var1, String var2);

   ChainedLogicalStreamBuilder withSlotOptions(Properties var1);
}
