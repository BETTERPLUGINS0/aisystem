package fr.xephi.authme.libs.org.postgresql.replication.fluent.logical;

import fr.xephi.authme.libs.org.postgresql.replication.fluent.ChainedCommonCreateSlotBuilder;

public interface ChainedLogicalCreateSlotBuilder extends ChainedCommonCreateSlotBuilder<ChainedLogicalCreateSlotBuilder> {
   ChainedLogicalCreateSlotBuilder withOutputPlugin(String var1);
}
