package fr.xephi.authme.libs.com.mysql.cj.exceptions;

import fr.xephi.authme.libs.com.mysql.cj.log.Log;
import fr.xephi.authme.libs.com.mysql.cj.util.Util;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class ExceptionInterceptorChain implements ExceptionInterceptor {
   private List<ExceptionInterceptor> interceptors;

   public ExceptionInterceptorChain(String interceptorClasses, Properties props, Log log) {
      this.interceptors = (List)Util.loadClasses(ExceptionInterceptor.class, interceptorClasses, "Connection.BadExceptionInterceptor", (ExceptionInterceptor)null).stream().map((i) -> {
         return i.init(props, log);
      }).collect(Collectors.toCollection(LinkedList::new));
   }

   public void addRingZero(ExceptionInterceptor interceptor) {
      this.interceptors.add(0, interceptor);
   }

   public Exception interceptException(Exception sqlEx) {
      ExceptionInterceptor ie;
      for(Iterator var2 = this.interceptors.iterator(); var2.hasNext(); sqlEx = ie.interceptException(sqlEx)) {
         ie = (ExceptionInterceptor)var2.next();
      }

      return sqlEx;
   }

   public void destroy() {
      this.interceptors.forEach(ExceptionInterceptor::destroy);
   }

   public ExceptionInterceptor init(Properties properties, Log log) {
      this.interceptors = (List)this.interceptors.stream().map((i) -> {
         return i.init(properties, log);
      }).collect(Collectors.toCollection(LinkedList::new));
      return this;
   }

   public List<ExceptionInterceptor> getInterceptors() {
      return this.interceptors;
   }
}
