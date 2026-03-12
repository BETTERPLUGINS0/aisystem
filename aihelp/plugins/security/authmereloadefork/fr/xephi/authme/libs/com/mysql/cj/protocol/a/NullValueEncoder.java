package fr.xephi.authme.libs.com.mysql.cj.protocol.a;

import fr.xephi.authme.libs.com.mysql.cj.BindValue;
import fr.xephi.authme.libs.com.mysql.cj.conf.PropertySet;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionInterceptor;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Message;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ServerSession;
import fr.xephi.authme.libs.com.mysql.cj.util.StringUtils;

public class NullValueEncoder extends AbstractValueEncoder {
   public void init(PropertySet pset, ServerSession serverSess, ExceptionInterceptor excInterceptor) {
      super.init(pset, serverSess, excInterceptor);
   }

   public byte[] getBytes(BindValue binding) {
      return StringUtils.getBytes("null");
   }

   public String getString(BindValue binding) {
      return "NULL";
   }

   public void encodeAsBinary(Message msg, BindValue binding) {
   }
}
