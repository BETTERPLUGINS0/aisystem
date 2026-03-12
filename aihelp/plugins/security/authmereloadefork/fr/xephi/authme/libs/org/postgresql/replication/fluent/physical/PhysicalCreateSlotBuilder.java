package fr.xephi.authme.libs.org.postgresql.replication.fluent.physical;

import fr.xephi.authme.libs.org.postgresql.core.BaseConnection;
import fr.xephi.authme.libs.org.postgresql.replication.LogSequenceNumber;
import fr.xephi.authme.libs.org.postgresql.replication.ReplicationSlotInfo;
import fr.xephi.authme.libs.org.postgresql.replication.ReplicationType;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.AbstractCreateSlotBuilder;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PhysicalCreateSlotBuilder extends AbstractCreateSlotBuilder<ChainedPhysicalCreateSlotBuilder> implements ChainedPhysicalCreateSlotBuilder {
   public PhysicalCreateSlotBuilder(BaseConnection connection) {
      super(connection);
   }

   protected ChainedPhysicalCreateSlotBuilder self() {
      return this;
   }

   public ReplicationSlotInfo make() throws SQLException {
      if (this.slotName != null && !this.slotName.isEmpty()) {
         Statement statement = this.connection.createStatement();
         ResultSet result = null;
         ReplicationSlotInfo slotInfo = null;

         try {
            String sql = String.format("CREATE_REPLICATION_SLOT %s %s PHYSICAL", this.slotName, this.temporaryOption ? "TEMPORARY" : "");
            statement.execute(sql);
            result = statement.getResultSet();
            if (result == null || !result.next()) {
               throw new PSQLException(GT.tr("{0} returned no results"), PSQLState.OBJECT_NOT_IN_STATE);
            }

            slotInfo = new ReplicationSlotInfo((String)Nullness.castNonNull(result.getString("slot_name")), ReplicationType.PHYSICAL, LogSequenceNumber.valueOf((String)Nullness.castNonNull(result.getString("consistent_point"))), result.getString("snapshot_name"), result.getString("output_plugin"));
         } finally {
            if (result != null) {
               result.close();
            }

            statement.close();
         }

         return slotInfo;
      } else {
         throw new IllegalArgumentException("Replication slotName can't be null");
      }
   }
}
