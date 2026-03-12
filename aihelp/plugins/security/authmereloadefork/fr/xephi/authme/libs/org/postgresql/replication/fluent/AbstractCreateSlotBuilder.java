package fr.xephi.authme.libs.org.postgresql.replication.fluent;

import fr.xephi.authme.libs.org.postgresql.core.BaseConnection;
import fr.xephi.authme.libs.org.postgresql.core.ServerVersion;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import java.sql.SQLFeatureNotSupportedException;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractCreateSlotBuilder<T extends ChainedCommonCreateSlotBuilder<T>> implements ChainedCommonCreateSlotBuilder<T> {
   @Nullable
   protected String slotName;
   protected boolean temporaryOption;
   protected BaseConnection connection;

   protected AbstractCreateSlotBuilder(BaseConnection connection) {
      this.connection = connection;
   }

   protected abstract T self();

   public T withSlotName(String slotName) {
      this.slotName = slotName;
      return this.self();
   }

   public T withTemporaryOption() throws SQLFeatureNotSupportedException {
      if (!this.connection.haveMinimumServerVersion(ServerVersion.v10)) {
         throw new SQLFeatureNotSupportedException(GT.tr("Server does not support temporary replication slots"));
      } else {
         this.temporaryOption = true;
         return this.self();
      }
   }
}
