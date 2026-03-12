package fr.xephi.authme.libs.com.mysql.cj;

import fr.xephi.authme.libs.com.mysql.cj.conf.PropertyKey;
import fr.xephi.authme.libs.com.mysql.cj.conf.RuntimeProperty;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.WrongArgumentException;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Message;
import fr.xephi.authme.libs.com.mysql.cj.util.StringUtils;

public class ClientPreparedQuery extends AbstractQuery implements PreparedQuery {
   protected QueryInfo queryInfo;
   protected QueryBindings queryBindings = null;
   protected String originalSql = null;
   protected int parameterCount;
   protected int batchCommandIndex = -1;
   protected RuntimeProperty<Boolean> autoClosePStmtStreams;
   protected RuntimeProperty<Boolean> useStreamLengthsInPrepStmts;

   public ClientPreparedQuery(NativeSession sess) {
      super(sess);
      this.autoClosePStmtStreams = this.session.getPropertySet().getBooleanProperty(PropertyKey.autoClosePStmtStreams);
      this.useStreamLengthsInPrepStmts = this.session.getPropertySet().getBooleanProperty(PropertyKey.useStreamLengthsInPrepStmts);
   }

   public void closeQuery() {
      super.closeQuery();
   }

   public QueryInfo getQueryInfo() {
      return this.queryInfo;
   }

   public void setQueryInfo(QueryInfo queryInfo) {
      this.queryInfo = queryInfo;
   }

   public String getOriginalSql() {
      return this.originalSql;
   }

   public void setOriginalSql(String originalSql) {
      this.originalSql = originalSql;
   }

   public int getParameterCount() {
      return this.parameterCount;
   }

   public void setParameterCount(int parameterCount) {
      this.parameterCount = parameterCount;
   }

   public QueryBindings getQueryBindings() {
      return this.queryBindings;
   }

   public void setQueryBindings(QueryBindings queryBindings) {
      this.queryBindings = queryBindings;
   }

   public int getBatchCommandIndex() {
      return this.batchCommandIndex;
   }

   public void setBatchCommandIndex(int batchCommandIndex) {
      this.batchCommandIndex = batchCommandIndex;
   }

   public int computeBatchSize(int numBatchedArgs) {
      long[] combinedValues = this.computeMaxParameterSetSizeAndBatchSize(numBatchedArgs);
      long maxSizeOfParameterSet = combinedValues[0];
      long sizeOfEntireBatch = combinedValues[1];
      return sizeOfEntireBatch < (long)((Integer)this.maxAllowedPacket.getValue() - this.originalSql.length()) ? numBatchedArgs : (int)Math.max(1L, (long)((Integer)this.maxAllowedPacket.getValue() - this.originalSql.length()) / maxSizeOfParameterSet);
   }

   public void checkNullOrEmptyQuery(String sql) {
      if (sql == null) {
         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedQuery.0"), this.session.getExceptionInterceptor());
      } else if (sql.length() == 0) {
         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedQuery.1"), this.session.getExceptionInterceptor());
      }
   }

   public String asSql() {
      StringBuilder buf = new StringBuilder();
      Object batchArg = null;
      if (this.batchCommandIndex != -1) {
         batchArg = this.batchedArgs.get(this.batchCommandIndex);
      }

      byte[][] staticSqlStrings = this.queryInfo.getStaticSqlParts();

      for(int i = 0; i < this.parameterCount; ++i) {
         buf.append(this.charEncoding != null ? StringUtils.toString(staticSqlStrings[i], this.charEncoding) : StringUtils.toString(staticSqlStrings[i]));
         String val = null;
         if (batchArg != null && batchArg instanceof String) {
            buf.append((String)batchArg);
         } else {
            val = this.batchCommandIndex == -1 ? (this.queryBindings == null ? null : this.queryBindings.getBindValues()[i].getString()) : ((QueryBindings)batchArg).getBindValues()[i].getString();
            buf.append(val == null ? "** NOT SPECIFIED **" : val);
         }
      }

      buf.append(this.charEncoding != null ? StringUtils.toString(staticSqlStrings[this.parameterCount], this.charEncoding) : StringUtils.toAsciiString(staticSqlStrings[this.parameterCount]));
      return buf.toString();
   }

   protected long[] computeMaxParameterSetSizeAndBatchSize(int numBatchedArgs) {
      long sizeOfEntireBatch = 1L;
      long maxSizeOfParameterSet = 0L;
      int i;
      if (this.session.getServerSession().supportsQueryAttributes()) {
         sizeOfEntireBatch += 10L;
         sizeOfEntireBatch += (long)((this.queryAttributesBindings.getCount() + 7) / 8 + 1);

         for(i = 0; i < this.queryAttributesBindings.getCount(); ++i) {
            BindValue queryAttribute = this.queryAttributesBindings.getAttributeValue(i);
            sizeOfEntireBatch += (long)(2 + queryAttribute.getName().length()) + queryAttribute.getBinaryLength();
         }
      }

      for(i = 0; i < numBatchedArgs; ++i) {
         long sizeOfParameterSet = 0L;
         BindValue[] bindValues = ((QueryBindings)this.batchedArgs.get(i)).getBindValues();

         for(int j = 0; j < bindValues.length; ++j) {
            sizeOfParameterSet += bindValues[j].getTextLength();
         }

         sizeOfParameterSet += this.queryInfo.getValuesClauseLength() != -1 ? (long)(this.queryInfo.getValuesClauseLength() + 1) : (long)(this.originalSql.length() + 1);
         sizeOfEntireBatch += sizeOfParameterSet;
         if (sizeOfParameterSet > maxSizeOfParameterSet) {
            maxSizeOfParameterSet = sizeOfParameterSet;
         }
      }

      return new long[]{maxSizeOfParameterSet, sizeOfEntireBatch};
   }

   public <M extends Message> M fillSendPacket(QueryBindings bindings) {
      return this.session.getProtocol().getMessageBuilder().buildComQuery(this.session.getSharedSendPacket(), this.session, this, bindings, this.charEncoding);
   }
}
