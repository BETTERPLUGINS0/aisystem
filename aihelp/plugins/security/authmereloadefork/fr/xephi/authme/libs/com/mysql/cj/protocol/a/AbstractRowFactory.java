package fr.xephi.authme.libs.com.mysql.cj.protocol.a;

import fr.xephi.authme.libs.com.mysql.cj.conf.RuntimeProperty;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionInterceptor;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ColumnDefinition;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ProtocolEntityFactory;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Resultset;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ResultsetRow;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ValueDecoder;

public abstract class AbstractRowFactory implements ProtocolEntityFactory<ResultsetRow, NativePacketPayload> {
   protected ColumnDefinition columnDefinition;
   protected Resultset.Concurrency resultSetConcurrency;
   protected boolean canReuseRowPacketForBufferRow;
   protected RuntimeProperty<Integer> useBufferRowSizeThreshold;
   protected ExceptionInterceptor exceptionInterceptor;
   protected ValueDecoder valueDecoder;

   public boolean canReuseRowPacketForBufferRow() {
      return this.canReuseRowPacketForBufferRow;
   }
}
