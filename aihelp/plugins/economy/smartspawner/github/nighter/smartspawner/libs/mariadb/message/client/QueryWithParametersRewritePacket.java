package github.nighter.smartspawner.libs.mariadb.message.client;

import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.client.util.Parameter;
import github.nighter.smartspawner.libs.mariadb.client.util.Parameters;
import github.nighter.smartspawner.libs.mariadb.plugin.codec.ByteArrayCodec;
import github.nighter.smartspawner.libs.mariadb.util.ClientParser;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class QueryWithParametersRewritePacket implements RedoableClientMessage {
   private final String preSqlCmd;
   private final ClientParser parser;
   private List<Parameters> parametersList;

   public QueryWithParametersRewritePacket(String preSqlCmd, ClientParser parser, List<Parameters> batchParameterList) {
      this.preSqlCmd = preSqlCmd;
      this.parametersList = batchParameterList;
      this.parser = parser;
   }

   public void saveParameters() {
      List<Parameters> savedList = new ArrayList(this.parametersList.size());
      Iterator var2 = this.parametersList.iterator();

      while(var2.hasNext()) {
         Parameters parameterList = (Parameters)var2.next();
         savedList.add(parameterList.clone());
      }

      this.parametersList = savedList;
   }

   public void ensureReplayable(Context context) throws IOException, SQLException {
      for(int j = 0; j < this.parametersList.size(); ++j) {
         Parameters parameters = (Parameters)this.parametersList.get(j);
         int parameterCount = parameters.size();

         for(int i = 0; i < parameterCount; ++i) {
            Parameter p = parameters.get(i);
            if (!p.isNull() && p.canEncodeLongData()) {
               parameters.set(i, new github.nighter.smartspawner.libs.mariadb.codec.Parameter(ByteArrayCodec.INSTANCE, p.encodeData()));
            }
         }
      }

   }

   public int encode(Writer writer, Context context) throws IOException, SQLException {
      Iterator<Parameters> paramIterator = this.parametersList.iterator();
      Parameters parameters = (Parameters)paramIterator.next();
      int rewritePacketNo = 0;
      int endingPartLen = this.parser.getQuery().length - (Integer)this.parser.getValuesBracketPositions().get(1);

      label76:
      while(true) {
         ++rewritePacketNo;
         writer.initPacket();
         writer.writeByte(3);
         if (this.preSqlCmd != null) {
            writer.writeAscii(this.preSqlCmd);
         }

         int pos = 0;
         if (this.parser.getParamCount() > parameters.size()) {
            throw context.getExceptionFactory().create("wrong number of parameters", "Y0000");
         }

         int paramPos;
         int parameterLength;
         for(parameterLength = 0; parameterLength < this.parser.getParamCount(); ++parameterLength) {
            paramPos = (Integer)this.parser.getParamPositions().get(parameterLength);
            writer.writeBytes(this.parser.getQuery(), pos, paramPos - pos);
            pos = paramPos + 1;
            parameters.get(parameterLength).encodeText(writer, context);
         }

         if (!paramIterator.hasNext()) {
            break;
         }

         parameters = (Parameters)paramIterator.next();
         if (writer.throwMaxAllowedLengthOr16M(writer.pos() + endingPartLen)) {
            writer.writeBytes(this.parser.getQuery(), (Integer)this.parser.getValuesBracketPositions().get(1), endingPartLen);
            writer.flush();
         } else {
            while(true) {
               parameterLength = 0;
               boolean knownParameterSize = true;
               if (this.parser.getParamCount() > parameters.size()) {
                  throw context.getExceptionFactory().create("wrong number of parameters", "Y0000");
               }

               int i;
               for(i = 0; i < this.parser.getParamCount(); ++i) {
                  int paramSize = parameters.get(i).getApproximateTextProtocolLength();
                  if (paramSize == -1) {
                     knownParameterSize = false;
                     break;
                  }

                  if (i > 0) {
                     parameterLength += (Integer)this.parser.getParamPositions().get(i) - ((Integer)this.parser.getParamPositions().get(i - 1) + 1);
                  }

                  parameterLength += paramSize;
               }

               if (!knownParameterSize || writer.throwMaxAllowedLengthOr16M(writer.pos() + parameterLength)) {
                  writer.writeBytes(this.parser.getQuery(), (Integer)this.parser.getValuesBracketPositions().get(1), endingPartLen);
                  writer.flush();
                  break;
               }

               writer.writeBytes(this.parser.getQuery(), pos, (Integer)this.parser.getValuesBracketPositions().get(1) + 1 - pos);
               writer.writeByte(44);
               pos = (Integer)this.parser.getValuesBracketPositions().get(0);

               for(i = 0; i < this.parser.getParamPositions().size(); ++i) {
                  paramPos = (Integer)this.parser.getParamPositions().get(i);
                  writer.writeBytes(this.parser.getQuery(), pos, paramPos - pos);
                  pos = paramPos + 1;
                  parameters.get(i).encodeText(writer, context);
               }

               if (!paramIterator.hasNext()) {
                  break label76;
               }

               parameters = (Parameters)paramIterator.next();
            }
         }
      }

      writer.writeBytes(this.parser.getQuery(), (Integer)this.parser.getValuesBracketPositions().get(1), endingPartLen);
      writer.flush();
      return rewritePacketNo;
   }

   public boolean binaryProtocol() {
      return false;
   }

   public InputStream getLocalInfileInputStream() {
      return null;
   }

   public String description() {
      return "REWRITE: " + this.preSqlCmd + this.parser.getSql();
   }

   public int batchUpdateLength() {
      return this.parametersList.size();
   }

   public boolean validateLocalFileName(String fileName, Context context) {
      return false;
   }
}
