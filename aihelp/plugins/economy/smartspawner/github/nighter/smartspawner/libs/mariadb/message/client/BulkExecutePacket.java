package github.nighter.smartspawner.libs.mariadb.message.client;

import github.nighter.smartspawner.libs.mariadb.BasePreparedStatement;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.client.util.Parameter;
import github.nighter.smartspawner.libs.mariadb.client.util.Parameters;
import github.nighter.smartspawner.libs.mariadb.export.MaxAllowedPacketException;
import github.nighter.smartspawner.libs.mariadb.export.Prepare;
import github.nighter.smartspawner.libs.mariadb.message.server.PrepareResultPacket;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class BulkExecutePacket implements RedoableWithPrepareClientMessage {
   private final String command;
   private final BasePreparedStatement prep;
   private List<Parameters> batchParameterList;
   private Prepare prepareResult;
   private boolean mightBeBulkResult;

   public BulkExecutePacket(Prepare prepareResult, List<Parameters> batchParameterList, String command, BasePreparedStatement prep) {
      this.batchParameterList = batchParameterList;
      this.prepareResult = prepareResult;
      this.command = command;
      this.prep = prep;
   }

   public void saveParameters() {
      List<Parameters> savedList = new ArrayList(this.batchParameterList.size());
      Iterator var2 = this.batchParameterList.iterator();

      while(var2.hasNext()) {
         Parameters parameterList = (Parameters)var2.next();
         savedList.add(parameterList.clone());
      }

      this.batchParameterList = savedList;
   }

   public int encode(Writer writer, Context context, Prepare newPrepareResult) throws IOException, SQLException {
      int statementId = newPrepareResult != null && newPrepareResult.getStatementId() != -1 ? newPrepareResult.getStatementId() : (this.prepareResult != null ? this.prepareResult.getStatementId() : -1);
      Iterator<Parameters> paramIterator = this.batchParameterList.iterator();
      Parameters parameters = (Parameters)paramIterator.next();
      int parameterCount = parameters.size();
      Parameter[] parameterHeaderType = new Parameter[parameterCount];

      for(int i = 0; i < parameterCount; ++i) {
         parameterHeaderType[i] = parameters.get(i);
      }

      byte[] lastCmdData = null;
      int bulkPacketNo = 0;
      this.mightBeBulkResult = context.hasClientCapability(137438953472L);

      label118:
      while(true) {
         ++bulkPacketNo;
         writer.initPacket();
         writer.writeByte(250);
         writer.writeInt(statementId);
         writer.writeShort((short)(this.mightBeBulkResult ? 192 : 128));

         int i;
         for(i = 0; i < parameterCount; ++i) {
            writer.writeShort((short)parameterHeaderType[i].getBinaryEncodeType());
         }

         if (lastCmdData != null) {
            if (writer.throwMaxAllowedLength(lastCmdData.length)) {
               throw new MaxAllowedPacketException("query size is >= to max_allowed_packet", writer.getCmdLength() != 0L);
            }

            writer.writeBytes(lastCmdData);
            writer.mark();
            lastCmdData = null;
            if (!paramIterator.hasNext()) {
               break;
            }

            parameters = (Parameters)paramIterator.next();
         }

         while(true) {
            for(i = 0; i < parameterCount; ++i) {
               Parameter param = parameters.get(i);
               if (param.isNull()) {
                  writer.writeByte(1);
               } else {
                  writer.writeByte(0);
                  param.encodeBinary(writer, context);
               }
            }

            if (!writer.bufIsDataAfterMark() && !writer.isMarked() && writer.hasFlushed()) {
               writer.flush();
               if (!paramIterator.hasNext()) {
                  break label118;
               }

               parameters = (Parameters)paramIterator.next();
               i = 0;

               while(true) {
                  if (i >= parameterCount) {
                     continue label118;
                  }

                  parameterHeaderType[i] = parameters.get(i);
                  ++i;
               }
            }

            if (writer.isMarked() && writer.throwMaxAllowedLength(writer.pos())) {
               writer.flushBufferStopAtMark();
               writer.mark();
               lastCmdData = writer.resetMark();
               break;
            }

            writer.mark();
            if (writer.bufIsDataAfterMark()) {
               lastCmdData = writer.resetMark();
               break;
            }

            if (!paramIterator.hasNext()) {
               break label118;
            }

            parameters = (Parameters)paramIterator.next();

            for(i = 0; i < parameterCount; ++i) {
               if (parameterHeaderType[i].getBinaryEncodeType() != parameters.get(i).getBinaryEncodeType() && !parameters.get(i).isNull()) {
                  writer.flush();
                  int j = 0;

                  while(true) {
                     if (j >= parameterCount) {
                        continue label118;
                     }

                     parameterHeaderType[j] = parameters.get(j);
                     ++j;
                  }
               }
            }
         }
      }

      writer.flush();
      return bulkPacketNo;
   }

   public boolean mightBeBulkResult() {
      return this.mightBeBulkResult;
   }

   public int batchUpdateLength() {
      return this.batchParameterList.size();
   }

   public String getCommand() {
      return this.command;
   }

   public BasePreparedStatement prep() {
      return this.prep;
   }

   public boolean binaryProtocol() {
      return true;
   }

   public String description() {
      return "BULK: " + this.command;
   }

   public void setPrepareResult(PrepareResultPacket prepareResult) {
      this.prepareResult = prepareResult;
   }
}
