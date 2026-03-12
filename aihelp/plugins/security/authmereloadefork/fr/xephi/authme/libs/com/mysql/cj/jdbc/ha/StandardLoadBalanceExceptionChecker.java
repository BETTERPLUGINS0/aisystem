package fr.xephi.authme.libs.com.mysql.cj.jdbc.ha;

import fr.xephi.authme.libs.com.mysql.cj.conf.PropertyKey;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.CJCommunicationsException;
import fr.xephi.authme.libs.com.mysql.cj.jdbc.exceptions.CommunicationsException;
import fr.xephi.authme.libs.com.mysql.cj.util.StringUtils;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

public class StandardLoadBalanceExceptionChecker implements LoadBalanceExceptionChecker {
   private List<String> sqlStateList;
   private List<Class<?>> sqlExClassList;

   public boolean shouldExceptionTriggerFailover(Throwable ex) {
      String sqlState = ex instanceof SQLException ? ((SQLException)ex).getSQLState() : null;
      Iterator i;
      if (sqlState != null) {
         if (sqlState.startsWith("08")) {
            return true;
         }

         if (this.sqlStateList != null) {
            i = this.sqlStateList.iterator();

            while(i.hasNext()) {
               if (sqlState.startsWith(((String)i.next()).toString())) {
                  return true;
               }
            }
         }
      }

      if (!(ex instanceof CommunicationsException) && !(ex instanceof CJCommunicationsException)) {
         if (this.sqlExClassList != null) {
            i = this.sqlExClassList.iterator();

            while(i.hasNext()) {
               if (((Class)i.next()).isInstance(ex)) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return true;
      }
   }

   public void destroy() {
   }

   public void init(Properties props) {
      this.configureSQLStateList(props.getProperty(PropertyKey.loadBalanceSQLStateFailover.getKeyName(), (String)null));
      this.configureSQLExceptionSubclassList(props.getProperty(PropertyKey.loadBalanceSQLExceptionSubclassFailover.getKeyName(), (String)null));
   }

   private void configureSQLStateList(String sqlStates) {
      if (sqlStates != null && !"".equals(sqlStates)) {
         this.sqlStateList = (List)StringUtils.split(sqlStates, ",", true).stream().filter((s) -> {
            return !s.isEmpty();
         }).collect(Collectors.toList());
      }
   }

   private void configureSQLExceptionSubclassList(String sqlExClasses) {
      if (sqlExClasses != null && !"".equals(sqlExClasses)) {
         this.sqlExClassList = (List)StringUtils.split(sqlExClasses, ",", true).stream().filter((s) -> {
            return !s.isEmpty();
         }).map((s) -> {
            try {
               return Class.forName(s, false, this.getClass().getClassLoader());
            } catch (ClassNotFoundException var3) {
               return null;
            }
         }).filter(Objects::nonNull).collect(Collectors.toList());
      }
   }
}
