package fr.xephi.authme.libs.com.mysql.cj.protocol;

import fr.xephi.authme.libs.com.mysql.cj.exceptions.CJOperationNotSupportedException;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;

public interface MessageListener<M extends Message> {
   default boolean processMessage(M message) {
      throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not allowed");
   }

   void error(Throwable var1);
}
