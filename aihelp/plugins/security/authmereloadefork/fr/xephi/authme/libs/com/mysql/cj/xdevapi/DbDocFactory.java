package fr.xephi.authme.libs.com.mysql.cj.xdevapi;

import fr.xephi.authme.libs.com.mysql.cj.conf.PropertySet;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ProtocolEntity;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ProtocolEntityFactory;
import fr.xephi.authme.libs.com.mysql.cj.protocol.x.XMessage;

public class DbDocFactory implements ProtocolEntityFactory<DbDoc, XMessage> {
   private PropertySet pset;

   public DbDocFactory(PropertySet pset) {
      this.pset = pset;
   }

   public DbDoc createFromProtocolEntity(ProtocolEntity internalRow) {
      return (DbDoc)((fr.xephi.authme.libs.com.mysql.cj.result.Row)internalRow).getValue(0, new DbDocValueFactory(this.pset));
   }
}
