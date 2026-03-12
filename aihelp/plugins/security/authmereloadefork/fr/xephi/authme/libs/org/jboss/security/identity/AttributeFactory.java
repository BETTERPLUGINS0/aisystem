package fr.xephi.authme.libs.org.jboss.security.identity;

public class AttributeFactory {
   public static <T> Attribute<T> createAttribute(final String name, final T value) {
      return new Attribute<T>() {
         private static final long serialVersionUID = 1L;

         public String getName() {
            return name;
         }

         public T getValue() {
            return value;
         }
      };
   }

   public static Attribute<String> createEmailAddress(String emailAddress) {
      return createAttribute(Attribute.TYPE.EMAIL_ADDRESS.get(), emailAddress);
   }
}
