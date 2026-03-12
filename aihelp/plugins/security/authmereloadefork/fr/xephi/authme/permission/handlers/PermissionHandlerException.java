package fr.xephi.authme.permission.handlers;

public class PermissionHandlerException extends Exception {
   public PermissionHandlerException(String message) {
      super(message);
   }

   public PermissionHandlerException(String message, Throwable cause) {
      super(message, cause);
   }
}
