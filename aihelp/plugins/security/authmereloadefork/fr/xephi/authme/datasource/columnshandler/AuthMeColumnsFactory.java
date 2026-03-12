package fr.xephi.authme.datasource.columnshandler;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.libs.ch.jalu.configme.properties.Property;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.ColumnType;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.StandardTypes;
import java.util.function.Function;

final class AuthMeColumnsFactory {
   private AuthMeColumnsFactory() {
   }

   static DataSourceColumn<Integer> createInteger(Property<String> nameProperty, AuthMeColumnsFactory.ColumnOptions... options) {
      return new DataSourceColumn(StandardTypes.INTEGER, nameProperty, isOptional(options), hasDefaultForNull(options));
   }

   static PlayerAuthColumn<Integer> createInteger(Property<String> nameProperty, Function<PlayerAuth, Integer> playerAuthGetter, AuthMeColumnsFactory.ColumnOptions... options) {
      return createInternal(StandardTypes.INTEGER, nameProperty, playerAuthGetter, options);
   }

   static PlayerAuthColumn<Long> createLong(Property<String> nameProperty, Function<PlayerAuth, Long> playerAuthGetter, AuthMeColumnsFactory.ColumnOptions... options) {
      return createInternal(StandardTypes.LONG, nameProperty, playerAuthGetter, options);
   }

   static PlayerAuthColumn<String> createString(Property<String> nameProperty, Function<PlayerAuth, String> playerAuthGetter, AuthMeColumnsFactory.ColumnOptions... options) {
      return createInternal(StandardTypes.STRING, nameProperty, playerAuthGetter, options);
   }

   static PlayerAuthColumn<Double> createDouble(Property<String> nameProperty, Function<PlayerAuth, Double> playerAuthGetter, AuthMeColumnsFactory.ColumnOptions... options) {
      return createInternal(StandardTypes.DOUBLE, nameProperty, playerAuthGetter, options);
   }

   static PlayerAuthColumn<Float> createFloat(Property<String> nameProperty, Function<PlayerAuth, Float> playerAuthGetter, AuthMeColumnsFactory.ColumnOptions... options) {
      return createInternal(StandardTypes.FLOAT, nameProperty, playerAuthGetter, options);
   }

   private static <T> PlayerAuthColumn<T> createInternal(ColumnType<T> type, Property<String> nameProperty, Function<PlayerAuth, T> authGetter, AuthMeColumnsFactory.ColumnOptions... options) {
      return new PlayerAuthColumn(type, nameProperty, isOptional(options), hasDefaultForNull(options), authGetter);
   }

   private static boolean isOptional(AuthMeColumnsFactory.ColumnOptions[] options) {
      return containsInArray(AuthMeColumnsFactory.ColumnOptions.OPTIONAL, options);
   }

   private static boolean hasDefaultForNull(AuthMeColumnsFactory.ColumnOptions[] options) {
      return containsInArray(AuthMeColumnsFactory.ColumnOptions.DEFAULT_FOR_NULL, options);
   }

   private static boolean containsInArray(AuthMeColumnsFactory.ColumnOptions needle, AuthMeColumnsFactory.ColumnOptions[] haystack) {
      AuthMeColumnsFactory.ColumnOptions[] var2 = haystack;
      int var3 = haystack.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         AuthMeColumnsFactory.ColumnOptions option = var2[var4];
         if (option == needle) {
            return true;
         }
      }

      return false;
   }

   static enum ColumnOptions {
      OPTIONAL,
      DEFAULT_FOR_NULL;

      // $FF: synthetic method
      private static AuthMeColumnsFactory.ColumnOptions[] $values() {
         return new AuthMeColumnsFactory.ColumnOptions[]{OPTIONAL, DEFAULT_FOR_NULL};
      }
   }
}
