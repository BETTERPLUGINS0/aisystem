package fr.xephi.authme.libs.org.postgresql.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Locale.Category;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;

public class GT {
   private static final GT _gt = new GT();
   private static final Object[] noargs = new Object[0];
   @Nullable
   private ResourceBundle bundle;

   @Pure
   public static String tr(String message, Object... args) {
      return _gt.translate(message, args);
   }

   private GT() {
      try {
         this.bundle = ResourceBundle.getBundle("fr.xephi.authme.libs.org.postgresql.translation.messages", Locale.getDefault(Category.DISPLAY));
      } catch (MissingResourceException var2) {
         this.bundle = null;
      }

   }

   private String translate(String message, Object[] args) {
      if (this.bundle != null && message != null) {
         try {
            message = this.bundle.getString(message);
         } catch (MissingResourceException var4) {
         }
      }

      if (args == null) {
         args = noargs;
      }

      if (message != null) {
         message = MessageFormat.format(message, args);
      }

      return message;
   }
}
