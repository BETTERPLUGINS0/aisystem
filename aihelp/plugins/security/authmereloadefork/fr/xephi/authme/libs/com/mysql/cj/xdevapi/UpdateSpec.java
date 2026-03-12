package fr.xephi.authme.libs.com.mysql.cj.xdevapi;

import fr.xephi.authme.libs.com.mysql.cj.Messages;
import fr.xephi.authme.libs.com.mysql.cj.x.protobuf.MysqlxCrud;
import fr.xephi.authme.libs.com.mysql.cj.x.protobuf.MysqlxExpr;

public class UpdateSpec {
   private MysqlxCrud.UpdateOperation.UpdateType updateType;
   private MysqlxExpr.ColumnIdentifier source;
   private MysqlxExpr.Expr value;

   public UpdateSpec(UpdateType updateType) {
      this.updateType = MysqlxCrud.UpdateOperation.UpdateType.valueOf(updateType.name());
      this.source = MysqlxExpr.ColumnIdentifier.getDefaultInstance();
   }

   public UpdateSpec(UpdateType updateType, String source) {
      this.updateType = MysqlxCrud.UpdateOperation.UpdateType.valueOf(updateType.name());
      if (source != null && !source.trim().isEmpty()) {
         if (source.length() > 0 && source.charAt(0) == '$') {
            source = source.substring(1);
         }

         this.source = (new ExprParser(source, false)).documentField().getIdentifier();
      } else {
         throw new XDevAPIError(Messages.getString("ModifyStatement.0", new String[]{"docPath"}));
      }
   }

   public Object getUpdateType() {
      return this.updateType;
   }

   public Object getSource() {
      return this.source;
   }

   public UpdateSpec setValue(Object value) {
      this.value = ExprUtil.argObjectToExpr(value, false);
      return this;
   }

   public Object getValue() {
      return this.value;
   }
}
