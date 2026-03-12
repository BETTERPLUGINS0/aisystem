package fr.xephi.authme.libs.com.mysql.cj.interceptors;

import fr.xephi.authme.libs.com.mysql.cj.MysqlConnection;
import fr.xephi.authme.libs.com.mysql.cj.Query;
import fr.xephi.authme.libs.com.mysql.cj.log.Log;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Message;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Resultset;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ServerSession;
import java.util.Properties;
import java.util.function.Supplier;

public interface QueryInterceptor {
   QueryInterceptor init(MysqlConnection var1, Properties var2, Log var3);

   <T extends Resultset> T preProcess(Supplier<String> var1, Query var2);

   default <M extends Message> M preProcess(M queryPacket) {
      return null;
   }

   boolean executeTopLevelOnly();

   void destroy();

   <T extends Resultset> T postProcess(Supplier<String> var1, Query var2, T var3, ServerSession var4);

   default <M extends Message> M postProcess(M queryPacket, M originalResponsePacket) {
      return null;
   }
}
