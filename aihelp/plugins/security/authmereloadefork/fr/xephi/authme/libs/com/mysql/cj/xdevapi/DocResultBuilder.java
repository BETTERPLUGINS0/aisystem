package fr.xephi.authme.libs.com.mysql.cj.xdevapi;

import fr.xephi.authme.libs.com.mysql.cj.MysqlxSession;
import fr.xephi.authme.libs.com.mysql.cj.conf.PropertySet;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.WrongArgumentException;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ColumnDefinition;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ProtocolEntity;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ResultBuilder;
import fr.xephi.authme.libs.com.mysql.cj.protocol.x.FetchDoneEntity;
import fr.xephi.authme.libs.com.mysql.cj.protocol.x.Notice;
import fr.xephi.authme.libs.com.mysql.cj.protocol.x.StatementExecuteOk;
import fr.xephi.authme.libs.com.mysql.cj.protocol.x.StatementExecuteOkBuilder;
import fr.xephi.authme.libs.com.mysql.cj.result.BufferedRowList;
import fr.xephi.authme.libs.com.mysql.cj.result.DefaultColumnDefinition;
import fr.xephi.authme.libs.com.mysql.cj.result.Field;
import java.util.ArrayList;
import java.util.List;

public class DocResultBuilder implements ResultBuilder<DocResult> {
   private ArrayList<Field> fields = new ArrayList();
   private ColumnDefinition metadata;
   private List<fr.xephi.authme.libs.com.mysql.cj.result.Row> rows = new ArrayList();
   private DocResult result;
   PropertySet pset;
   private StatementExecuteOkBuilder statementExecuteOkBuilder = new StatementExecuteOkBuilder();

   public DocResultBuilder(MysqlxSession sess) {
      this.pset = sess.getPropertySet();
   }

   public boolean addProtocolEntity(ProtocolEntity entity) {
      if (entity instanceof Field) {
         this.fields.add((Field)entity);
         return false;
      } else if (entity instanceof fr.xephi.authme.libs.com.mysql.cj.result.Row) {
         if (this.metadata == null) {
            this.metadata = new DefaultColumnDefinition((Field[])this.fields.toArray(new Field[0]));
         }

         this.rows.add(((fr.xephi.authme.libs.com.mysql.cj.result.Row)entity).setMetadata(this.metadata));
         return false;
      } else if (entity instanceof Notice) {
         this.statementExecuteOkBuilder.addProtocolEntity(entity);
         return false;
      } else if (entity instanceof FetchDoneEntity) {
         return false;
      } else if (entity instanceof StatementExecuteOk) {
         return true;
      } else {
         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, "Unexpected protocol entity " + entity);
      }
   }

   public DocResult build() {
      this.result = new DocResultImpl(new BufferedRowList(this.rows), () -> {
         return this.statementExecuteOkBuilder.build();
      }, this.pset);
      return this.result;
   }
}
