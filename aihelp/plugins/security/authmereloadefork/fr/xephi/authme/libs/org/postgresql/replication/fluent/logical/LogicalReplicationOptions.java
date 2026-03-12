package fr.xephi.authme.libs.org.postgresql.replication.fluent.logical;

import fr.xephi.authme.libs.org.postgresql.replication.fluent.CommonOptions;
import java.util.Properties;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface LogicalReplicationOptions extends CommonOptions {
   @Nullable
   String getSlotName();

   Properties getSlotOptions();
}
