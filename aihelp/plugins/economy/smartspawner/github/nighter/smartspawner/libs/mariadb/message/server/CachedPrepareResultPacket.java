package github.nighter.smartspawner.libs.mariadb.message.server;

import github.nighter.smartspawner.libs.mariadb.BasePreparedStatement;
import github.nighter.smartspawner.libs.mariadb.client.Client;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.socket.Reader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public final class CachedPrepareResultPacket extends PrepareResultPacket {
   private final AtomicBoolean closing = new AtomicBoolean();
   private final AtomicBoolean cached = new AtomicBoolean();
   private final List<BasePreparedStatement> statements = new ArrayList();

   public CachedPrepareResultPacket(ReadableByteBuf buffer, Reader reader, Context context) throws IOException {
      super(buffer, reader, context);
   }

   public void close(Client con) throws SQLException {
      if (!this.cached.get() && this.closing.compareAndSet(false, true)) {
         con.closePrepare(this);
      }

   }

   public void decrementUse(Client con, BasePreparedStatement preparedStatement) throws SQLException {
      this.statements.remove(preparedStatement);
      if (this.statements.isEmpty() && !this.cached.get()) {
         this.close(con);
      }

   }

   public void incrementUse(BasePreparedStatement preparedStatement) {
      if (!this.closing.get()) {
         if (preparedStatement != null) {
            this.statements.add(preparedStatement);
         }

      }
   }

   public void unCache(Client con) {
      this.cached.set(false);
      if (this.statements.size() <= 0) {
         try {
            this.close(con);
         } catch (SQLException var3) {
         }
      }

   }

   public boolean cache() {
      return this.closing.get() ? false : this.cached.compareAndSet(false, true);
   }

   public int getStatementId() {
      return this.statementId;
   }

   public void reset() {
      this.statementId = -1;
      Iterator var1 = this.statements.iterator();

      while(var1.hasNext()) {
         BasePreparedStatement stmt = (BasePreparedStatement)var1.next();
         stmt.reset();
      }

   }
}
