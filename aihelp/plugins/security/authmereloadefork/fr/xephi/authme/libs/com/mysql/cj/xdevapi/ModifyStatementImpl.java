package fr.xephi.authme.libs.com.mysql.cj.xdevapi;

import fr.xephi.authme.libs.com.mysql.cj.Messages;
import fr.xephi.authme.libs.com.mysql.cj.MysqlxSession;
import fr.xephi.authme.libs.com.mysql.cj.protocol.x.XMessage;
import fr.xephi.authme.libs.com.mysql.cj.protocol.x.XMessageBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ModifyStatementImpl extends FilterableStatement<ModifyStatement, Result> implements ModifyStatement {
   private List<UpdateSpec> updates = new ArrayList();

   ModifyStatementImpl(MysqlxSession mysqlxSession, String schema, String collection, String criteria) {
      super(new DocFilterParams(schema, collection, false));
      this.mysqlxSession = mysqlxSession;
      if (criteria != null && !criteria.trim().isEmpty()) {
         this.filterParams.setCriteria(criteria);
         if (!this.mysqlxSession.supportsPreparedStatements()) {
            this.preparedState = PreparableStatement.PreparedState.UNSUPPORTED;
         }

      } else {
         throw new XDevAPIError(Messages.getString("ModifyStatement.0", new String[]{"criteria"}));
      }
   }

   protected Result executeStatement() {
      return (Result)this.mysqlxSession.query(this.getMessageBuilder().buildDocUpdate(this.filterParams, this.updates), new UpdateResultBuilder());
   }

   protected XMessage getPrepareStatementXMessage() {
      return this.getMessageBuilder().buildPrepareDocUpdate(this.preparedStatementId, this.filterParams, this.updates);
   }

   protected Result executePreparedStatement() {
      return (Result)this.mysqlxSession.query(this.getMessageBuilder().buildPrepareExecute(this.preparedStatementId, this.filterParams), new UpdateResultBuilder());
   }

   public CompletableFuture<Result> executeAsync() {
      return this.mysqlxSession.queryAsync(((XMessageBuilder)this.mysqlxSession.getMessageBuilder()).buildDocUpdate(this.filterParams, this.updates), new UpdateResultBuilder());
   }

   public ModifyStatement set(String docPath, Object value) {
      this.resetPrepareState();
      this.updates.add((new UpdateSpec(UpdateType.ITEM_SET, docPath)).setValue(value));
      return this;
   }

   public ModifyStatement change(String docPath, Object value) {
      this.resetPrepareState();
      this.updates.add((new UpdateSpec(UpdateType.ITEM_REPLACE, docPath)).setValue(value));
      return this;
   }

   public ModifyStatement unset(String... docPath) {
      this.resetPrepareState();
      if (docPath == null) {
         throw new XDevAPIError(Messages.getString("ModifyStatement.0", new String[]{"docPath"}));
      } else {
         this.updates.addAll((java.util.Collection)Arrays.stream(docPath).map((dp) -> {
            return new UpdateSpec(UpdateType.ITEM_REMOVE, dp);
         }).collect(Collectors.toList()));
         return this;
      }
   }

   public ModifyStatement patch(DbDoc document) {
      this.resetPrepareState();
      return this.patch(document.toString());
   }

   public ModifyStatement patch(String document) {
      this.resetPrepareState();
      this.updates.add((new UpdateSpec(UpdateType.MERGE_PATCH)).setValue(Expression.expr(document)));
      return this;
   }

   public ModifyStatement arrayInsert(String docPath, Object value) {
      this.resetPrepareState();
      this.updates.add((new UpdateSpec(UpdateType.ARRAY_INSERT, docPath)).setValue(value));
      return this;
   }

   public ModifyStatement arrayAppend(String docPath, Object value) {
      this.resetPrepareState();
      this.updates.add((new UpdateSpec(UpdateType.ARRAY_APPEND, docPath)).setValue(value));
      return this;
   }

   /** @deprecated */
   @Deprecated
   public ModifyStatement where(String searchCondition) {
      return (ModifyStatement)super.where(searchCondition);
   }
}
