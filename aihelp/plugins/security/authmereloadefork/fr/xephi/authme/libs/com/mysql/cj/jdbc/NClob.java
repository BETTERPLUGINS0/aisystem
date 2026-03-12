package fr.xephi.authme.libs.com.mysql.cj.jdbc;

import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionInterceptor;

public class NClob extends Clob implements java.sql.NClob {
   NClob(ExceptionInterceptor exceptionInterceptor) {
      super(exceptionInterceptor);
   }

   public NClob(String charDataInit, ExceptionInterceptor exceptionInterceptor) {
      super(charDataInit, exceptionInterceptor);
   }
}
