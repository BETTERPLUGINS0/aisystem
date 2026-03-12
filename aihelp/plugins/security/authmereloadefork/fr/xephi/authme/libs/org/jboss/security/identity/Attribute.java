package fr.xephi.authme.libs.org.jboss.security.identity;

import java.io.Serializable;

public interface Attribute<T> extends Serializable {
   String getName();

   T getValue();

   public static enum TYPE {
      COUNTRY("country"),
      EMAIL_ADDRESS("email"),
      EMPLOYEE_TYPE("employeeType"),
      EMPLOYEE_NUMBER("employeeNumber"),
      GIVEN_NAME("givenName"),
      PREFERRED_LANGUAGE("preferredLanguage"),
      PO_BOX("postOfficeBox"),
      POSTAL_CODE("postalCode"),
      POSTAL_ADDRESS("postalAddress"),
      SURNAME("surname"),
      STREET("street"),
      TITLE("title"),
      TELEPHONE("telephoneNumber");

      private String type;

      private TYPE(String type) {
         this.type = type;
      }

      public String get() {
         return this.type;
      }
   }
}
