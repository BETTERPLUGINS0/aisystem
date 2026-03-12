package fr.xephi.authme.libs.com.mysql.cj.protocol.a;

import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.WrongArgumentException;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ProtocolEntity;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ProtocolEntityFactory;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Resultset;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ResultsetRows;
import fr.xephi.authme.libs.com.mysql.cj.protocol.a.result.NativeResultset;
import fr.xephi.authme.libs.com.mysql.cj.protocol.a.result.OkPacket;

public class ResultsetFactory implements ProtocolEntityFactory<Resultset, NativePacketPayload> {
   private Resultset.Type type;
   private Resultset.Concurrency concurrency;

   public ResultsetFactory(Resultset.Type type, Resultset.Concurrency concurrency) {
      this.type = Resultset.Type.FORWARD_ONLY;
      this.concurrency = Resultset.Concurrency.READ_ONLY;
      this.type = type;
      this.concurrency = concurrency;
   }

   public Resultset.Type getResultSetType() {
      return this.type;
   }

   public Resultset.Concurrency getResultSetConcurrency() {
      return this.concurrency;
   }

   public Resultset createFromProtocolEntity(ProtocolEntity protocolEntity) {
      if (protocolEntity instanceof OkPacket) {
         return new NativeResultset((OkPacket)protocolEntity);
      } else if (protocolEntity instanceof ResultsetRows) {
         return new NativeResultset((ResultsetRows)protocolEntity);
      } else {
         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, "Unknown ProtocolEntity class " + protocolEntity);
      }
   }
}
