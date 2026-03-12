package fr.xephi.authme.libs.com.mysql.cj.protocol.a;

import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.WrongArgumentException;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ColumnDefinition;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ProtocolEntityFactory;
import fr.xephi.authme.libs.com.mysql.cj.result.DefaultColumnDefinition;
import fr.xephi.authme.libs.com.mysql.cj.result.Field;

public class MergingColumnDefinitionFactory extends ColumnDefinitionFactory implements ProtocolEntityFactory<ColumnDefinition, NativePacketPayload> {
   public MergingColumnDefinitionFactory(long columnCount, ColumnDefinition columnDefinitionFromCache) {
      super(columnCount, columnDefinitionFromCache);
   }

   public boolean mergeColumnDefinitions() {
      return true;
   }

   public ColumnDefinition createFromFields(Field[] fields) {
      if (this.columnDefinitionFromCache != null) {
         if ((long)fields.length != this.columnCount) {
            throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, "Wrong number of ColumnDefinition fields.");
         }

         Field[] f = this.columnDefinitionFromCache.getFields();

         for(int i = 0; i < fields.length; ++i) {
            fields[i].setFlags(f[i].getFlags());
         }
      }

      return new DefaultColumnDefinition(fields);
   }
}
