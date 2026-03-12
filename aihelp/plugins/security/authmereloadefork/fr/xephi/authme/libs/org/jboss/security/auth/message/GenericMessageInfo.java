package fr.xephi.authme.libs.org.jboss.security.auth.message;

import java.util.HashMap;
import java.util.Map;
import javax.security.auth.message.MessageInfo;

public class GenericMessageInfo implements MessageInfo {
   private static final long serialVersionUID = -8148794884261757664L;
   protected Object request = null;
   protected Object response = null;
   private Map<Object, Object> map = new HashMap();

   public GenericMessageInfo() {
   }

   public GenericMessageInfo(Object request, Object response) {
      this.request = request;
      this.response = response;
   }

   public Object getRequestMessage() {
      return this.request;
   }

   public Object getResponseMessage() {
      return this.response;
   }

   public void setRequestMessage(Object request) {
      this.request = request;
   }

   public void setResponseMessage(Object response) {
      this.response = response;
   }

   public Map<Object, Object> getMap() {
      return this.map;
   }
}
