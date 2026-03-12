package fr.xephi.authme.libs.com.mysql.cj.protocol.a.result;

import fr.xephi.authme.libs.com.mysql.cj.protocol.ColumnDefinition;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ProtocolEntityFactory;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ResultsetRow;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ResultsetRows;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ResultsetRowsOwner;
import fr.xephi.authme.libs.com.mysql.cj.protocol.a.NativePacketPayload;

public abstract class AbstractResultsetRows implements ResultsetRows {
   protected static final int BEFORE_START_OF_ROWS = -1;
   protected ColumnDefinition metadata;
   protected int currentPositionInFetchedRows = -1;
   protected boolean wasEmpty = false;
   protected ResultsetRowsOwner owner;
   protected ProtocolEntityFactory<ResultsetRow, NativePacketPayload> rowFactory;

   public void setOwner(ResultsetRowsOwner rs) {
      this.owner = rs;
   }

   public ResultsetRowsOwner getOwner() {
      return this.owner;
   }

   public void setMetadata(ColumnDefinition columnDefinition) {
      this.metadata = columnDefinition;
   }

   public ColumnDefinition getMetadata() {
      return this.metadata;
   }

   public boolean wasEmpty() {
      return this.wasEmpty;
   }
}
