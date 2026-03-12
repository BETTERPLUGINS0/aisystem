package com.nisovin.shopkeepers.util.data.property;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.property.validation.ChainedPropertyValidator;
import com.nisovin.shopkeepers.util.data.property.validation.PropertyValidator;
import com.nisovin.shopkeepers.util.data.serialization.DataLoader;
import com.nisovin.shopkeepers.util.data.serialization.DataSaver;
import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.MissingDataException;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.function.Supplier;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BasicProperty<T> implements Property<T> {
   @Nullable
   private String name = null;
   private boolean nullable = false;
   @Nullable
   private T defaultValue = null;
   @Nullable
   private Supplier<T> defaultValueSupplier = null;
   private boolean omitIfDefault = false;
   private boolean useDefaultIfMissing = false;
   @Nullable
   private PropertyValidator<? super T> validator = null;
   private StringConverter<? super T> stringConverter;
   @Nullable
   private DataLoader<? extends T> dataLoader;
   @Nullable
   private DataSaver<? super T> dataSaver;
   private final DataLoader<T> unvalidatedDataLoader;
   private final DataSaver<T> unvalidatedDataSaver;
   private boolean built;

   public BasicProperty() {
      this.stringConverter = StringConverter.DEFAULT;
      this.unvalidatedDataLoader = new DataLoader<T>() {
         public T load(DataContainer dataContainer) throws InvalidDataException {
            return ((BasicProperty)Unsafe.initialized(BasicProperty.this)).loadUnvalidated(dataContainer);
         }
      };
      this.unvalidatedDataSaver = new DataSaver<T>() {
         public void save(DataContainer dataContainer, @Nullable T value) {
            ((BasicProperty)Unsafe.initialized(BasicProperty.this)).saveUnvalidated(dataContainer, value);
         }
      };
      this.built = false;
   }

   protected final boolean isBuilt() {
      return this.built;
   }

   protected final void validateNotBuilt() {
      Validate.State.isTrue(!this.isBuilt(), "Property has already been built!");
   }

   protected final void validateBuilt() {
      Validate.State.isTrue(this.isBuilt(), "Property has not yet been built!");
   }

   public final <P extends BasicProperty<T>> P build() {
      this.validateNotBuilt();
      Validate.State.notEmpty(this.name, "Property name is null or empty!");
      Validate.State.notNull(this.dataLoader, (String)"No data accessor has been set!");

      assert this.dataSaver != null;

      Validate.State.isTrue(!this.omitIfDefault || this.hasDefaultValue(), "omitIfDefault is set, but property has no default value!");
      Validate.State.isTrue(!this.useDefaultIfMissing || this.hasDefaultValue(), "useDefaultIfMissing is set, but property has no default value!");
      this.postConstruct();
      String var10000;
      if (this.hasDefaultValue()) {
         try {
            this.validateValue(this.getDefaultValue());
         } catch (Exception var3) {
            var10000 = this.getName();
            throw Validate.State.error("The default value for property '" + var10000 + "' is invalid: " + var3.getMessage());
         }
      }

      if (this.isNullable()) {
         try {
            this.validateValue((Object)null);
         } catch (Exception var2) {
            var10000 = this.getName();
            throw Validate.State.error("Null is considered an invalid value, even though property '" + var10000 + "' is nullable: " + var2.getMessage());
         }
      }

      this.built = true;
      return this;
   }

   protected void postConstruct() {
   }

   public final String getName() {
      Validate.State.notNull(this.name, (String)"name has not yet been set");
      return (String)Unsafe.assertNonNull(this.name);
   }

   public final <P extends BasicProperty<T>> P name(String name) {
      this.validateNotBuilt();
      Validate.State.isTrue(this.name == null, "Another name has already been set!");
      Validate.notEmpty(name, "name is null or empty");
      this.name = name;
      return this;
   }

   public final boolean isNullable() {
      return this.nullable;
   }

   public final <P extends BasicProperty<T>> P nullable() {
      this.validateNotBuilt();
      this.nullable = true;
      return this;
   }

   public final boolean hasDefaultValue() {
      return this.defaultValue != null || this.defaultValueSupplier != null || this.isNullable();
   }

   public final T getDefaultValue() {
      Validate.State.isTrue(this.hasDefaultValue(), "This property does not have a valid default value!");
      return this.defaultValueSupplier != null ? Unsafe.cast(this.defaultValueSupplier.get()) : Unsafe.cast(this.defaultValue);
   }

   public final <P extends BasicProperty<T>> P defaultValue(T defaultValue) {
      this.validateNotBuilt();
      this.defaultValue = defaultValue;
      this.defaultValueSupplier = null;
      return this;
   }

   public final <P extends BasicProperty<T>> P defaultValueSupplier(@Nullable Supplier<T> defaultValueSupplier) {
      this.validateNotBuilt();
      this.defaultValueSupplier = defaultValueSupplier;
      this.defaultValue = null;
      return this;
   }

   public final <P extends BasicProperty<T>> P omitIfDefault() {
      this.validateNotBuilt();
      this.omitIfDefault = true;
      this.useDefaultIfMissing();
      return this;
   }

   public final <P extends BasicProperty<T>> P useDefaultIfMissing() {
      this.validateNotBuilt();
      this.useDefaultIfMissing = true;
      return this;
   }

   public final <P extends BasicProperty<T>> P validator(PropertyValidator<? super T> validator) {
      this.validateNotBuilt();
      Validate.notNull(validator, (String)"validator is null");
      if (this.validator != null) {
         this.validator = new ChainedPropertyValidator(this.validator, validator);
      } else {
         this.validator = validator;
      }

      return this;
   }

   public final void validateValue(@Nullable T value) {
      if (value == null) {
         Validate.isTrue(this.isNullable(), "value is null for non-nullable property");
      } else {
         this.internalValidateValue(value);
         if (this.validator != null) {
            this.validator.validate(value);
         }
      }

   }

   protected void internalValidateValue(@NonNull T value) {
   }

   public final <P extends BasicProperty<T>> P stringConverter(StringConverter<? super T> stringConverter) {
      this.validateNotBuilt();
      Validate.notNull(stringConverter, (String)"stringConverter is null");
      this.stringConverter = stringConverter;
      return this;
   }

   public final String toString(@Nullable T value) {
      return this.stringConverter.toString(value);
   }

   public final DataLoader<? extends T> getLoader() {
      this.validateBuilt();
      return (DataLoader)Unsafe.assertNonNull(this.dataLoader);
   }

   public final DataSaver<? super T> getSaver() {
      this.validateBuilt();
      return (DataSaver)Unsafe.assertNonNull(this.dataSaver);
   }

   public final DataLoader<? extends T> getUnvalidatedLoader() {
      this.validateBuilt();
      return this.unvalidatedDataLoader;
   }

   public final DataSaver<? super T> getUnvalidatedSaver() {
      this.validateBuilt();
      return this.unvalidatedDataSaver;
   }

   public final <P extends BasicProperty<T>, A extends DataLoader<? extends T> & DataSaver<? super T>> P dataAccessor(A dataAccessor) {
      this.validateNotBuilt();
      Validate.notNull(dataAccessor, (String)"dataAccessor is null");
      Validate.State.isTrue(this.dataLoader == null, "Another DataAccessor has already been set!");

      assert this.dataSaver == null;

      this.dataLoader = dataAccessor;
      this.dataSaver = (DataSaver)dataAccessor;
      if (this.name == null && dataAccessor instanceof DataKeyAccessor) {
         this.name(((DataKeyAccessor)dataAccessor).getDataKey());
      }

      return this;
   }

   public final <P extends BasicProperty<T>> P dataKeyAccessor(String dataKey, DataSerializer<T> serializer) {
      return this.dataAccessor(new DataKeyAccessor(dataKey, serializer));
   }

   public final void save(DataContainer dataContainer, @Nullable T value) {
      this.validateValue(value);
      this.saveUnvalidated(dataContainer, value);
   }

   private void saveUnvalidated(DataContainer dataContainer, @Nullable T value) {
      this.validateBuilt();
      Validate.notNull(dataContainer, (String)"dataContainer is null");

      try {
         if (value == null) {
            this.saveValue(dataContainer, (Object)null);
         } else if (this.omitIfDefault && value.equals(this.getDefaultValue())) {
            this.saveValue(dataContainer, (Object)null);
         } else {
            this.saveValue(dataContainer, value);
         }
      } catch (Exception var4) {
         throw new RuntimeException("Failed to save property '" + this.getName() + "': " + var4.getMessage(), var4);
      }
   }

   private void saveValue(DataContainer dataContainer, @Nullable T value) {
      this.getSaver().save(dataContainer, value);
   }

   public final T load(DataContainer dataContainer) throws MissingDataException, InvalidDataException {
      Object value = this.loadUnvalidated(dataContainer);

      try {
         this.validateValue(value);
         return value;
      } catch (Exception var4) {
         throw new InvalidDataException(this.loadingFailedErrorMessage(var4.getMessage()), var4);
      }
   }

   private T loadUnvalidated(DataContainer dataContainer) throws MissingDataException, InvalidDataException {
      this.validateBuilt();

      try {
         try {
            return this.loadValue(dataContainer);
         } catch (MissingDataException var3) {
            if (this.useDefaultIfMissing) {
               return this.getDefaultValue();
            } else if (this.isNullable()) {
               return Unsafe.uncheckedNull();
            } else {
               throw var3;
            }
         }
      } catch (MissingDataException var4) {
         throw new MissingDataException(this.loadingFailedErrorMessage(var4.getMessage()), var4);
      } catch (InvalidDataException var5) {
         throw new InvalidDataException(this.loadingFailedErrorMessage(var5.getMessage()), var5);
      }
   }

   private String loadingFailedErrorMessage(@Nullable String cause) {
      String var10000 = this.getName();
      return "Failed to load property '" + var10000 + "': " + cause;
   }

   private T loadValue(DataContainer dataContainer) throws InvalidDataException {
      Validate.notNull(dataContainer, (String)"dataContainer is null");
      return this.getLoader().load(dataContainer);
   }

   public final T loadOrDefault(DataContainer dataContainer) throws InvalidDataException {
      try {
         T value = this.load(dataContainer);
         if (value != null) {
            return value;
         } else {
            assert this.isNullable() && this.hasDefaultValue();

            return this.getDefaultValue();
         }
      } catch (MissingDataException var3) {
         if (this.hasDefaultValue()) {
            return this.getDefaultValue();
         } else {
            throw var3;
         }
      }
   }
}
