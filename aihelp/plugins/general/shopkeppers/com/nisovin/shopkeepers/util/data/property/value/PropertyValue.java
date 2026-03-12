package com.nisovin.shopkeepers.util.data.property.value;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.MissingDataException;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PropertyValue<T> {
   public static final Set<? extends PropertyValue.UpdateFlag> DEFAULT_UPDATE_FLAGS;
   private static final Set<? extends PropertyValue.UpdateFlag> FORCE_DIRTY_FLAGS;
   private final Property<T> property;
   @Nullable
   private AbstractPropertyValuesHolder holder;
   @Nullable
   private ValueChangeListener<T> valueChangeListener = null;
   @Nullable
   private T value;
   private boolean requireInitialValue = false;

   public PropertyValue(Property<T> property) {
      Validate.notNull(property, (String)"property is null");
      this.property = property;
   }

   private final boolean isBuilt() {
      return this.holder != null;
   }

   protected final void validateBuilt() {
      Validate.State.isTrue(this.isBuilt(), "PropertyValue has not yet been built!");
   }

   protected final void validateNotBuilt() {
      Validate.State.isTrue(!this.isBuilt(), "PropertyValue has already been built!");
   }

   public final <P extends PropertyValue<T>> P build(PropertyValuesHolder holder) {
      this.validateNotBuilt();
      Validate.notNull(holder, (String)"holder is null");
      Validate.isTrue(holder instanceof AbstractPropertyValuesHolder, "holder is not of type AbstractPropertyValuesHolder");
      if (this.property.hasDefaultValue()) {
         this.value = this.property.getDefaultValue();
      } else {
         this.requireInitialValue = true;
      }

      this.postConstruct();
      this.holder = (AbstractPropertyValuesHolder)holder;
      this.holder.add(this);
      return this;
   }

   protected void postConstruct() {
   }

   public final Property<T> getProperty() {
      return this.property;
   }

   public final PropertyValuesHolder getHolder() {
      this.validateBuilt();
      return (PropertyValuesHolder)Unsafe.assertNonNull(this.holder);
   }

   public final T getValue() {
      this.validateBuilt();
      if (this.requireInitialValue) {
         assert this.holder != null;

         String var10002 = this.holder.getLogPrefix();
         throw new IllegalStateException(var10002 + "Value for property '" + this.property.getName() + "' has not yet been initialized!");
      } else {
         return Unsafe.cast(this.value);
      }
   }

   public final void setValue(T value) {
      this.setValue(value, DEFAULT_UPDATE_FLAGS);
   }

   public final void setValue(T value, Set<? extends PropertyValue.UpdateFlag> updateFlags) {
      this.validateBuilt();
      Validate.notNull(updateFlags, (String)"updateFlags is null");
      this.property.validateValue(value);
      T oldValue = Unsafe.cast(this.value);
      if (Objects.equals(oldValue, value)) {
         if (updateFlags.contains(PropertyValue.DefaultUpdateFlag.FORCE_MARK_DIRTY)) {
            assert this.holder != null;

            this.holder.markDirty();
         }

      } else {
         this.value = value;
         this.requireInitialValue = false;
         if (updateFlags.contains(PropertyValue.DefaultUpdateFlag.MARK_DIRTY) || updateFlags.contains(PropertyValue.DefaultUpdateFlag.FORCE_MARK_DIRTY)) {
            assert this.holder != null;

            this.holder.markDirty();
         }

         this.onValueChanged(oldValue, value, updateFlags);
         if (this.valueChangeListener != null) {
            this.valueChangeListener.onValueChanged(this, oldValue, value, updateFlags);
         }

      }
   }

   protected void onValueChanged(T oldValue, T newValue, Set<? extends PropertyValue.UpdateFlag> updateFlags) {
   }

   public final <P extends PropertyValue<T>> P onValueChanged(ValueChangeListener<T> listener) {
      this.validateNotBuilt();
      Validate.State.isTrue(this.valueChangeListener == null, "Another listener has already been set!");
      Validate.notNull(listener, (String)"listener is null");
      this.valueChangeListener = listener;
      return this;
   }

   public final <P extends PropertyValue<T>> P onValueChanged(Runnable runnable) {
      Validate.notNull(runnable, (String)"runnable is null");
      return this.onValueChanged((property, oldValue, newValue, updateFlags) -> {
         runnable.run();
      });
   }

   public final void load(DataContainer dataContainer) throws InvalidDataException {
      this.validateBuilt();
      Set updateFlags = Collections.emptySet();

      Object value;
      try {
         value = this.property.load(dataContainer);
      } catch (MissingDataException var5) {
         if (!this.property.hasDefaultValue()) {
            throw var5;
         }

         value = this.property.getDefaultValue();
         updateFlags = FORCE_DIRTY_FLAGS;

         assert this.holder != null;

         String var10000 = this.holder.getLogPrefix();
         Log.warning(var10000 + "Missing data for property '" + this.property.getName() + "'. Using the default value now: '" + this.property.toString(value) + "'");
      }

      this.setValue(value, updateFlags);
   }

   public final void save(DataContainer dataContainer) {
      this.validateBuilt();
      this.property.save(dataContainer, this.value);
   }

   static {
      DEFAULT_UPDATE_FLAGS = Collections.singleton(PropertyValue.DefaultUpdateFlag.MARK_DIRTY);
      FORCE_DIRTY_FLAGS = Collections.singleton(PropertyValue.DefaultUpdateFlag.FORCE_MARK_DIRTY);
   }

   public static enum DefaultUpdateFlag implements PropertyValue.UpdateFlag {
      MARK_DIRTY,
      FORCE_MARK_DIRTY;

      // $FF: synthetic method
      private static PropertyValue.DefaultUpdateFlag[] $values() {
         return new PropertyValue.DefaultUpdateFlag[]{MARK_DIRTY, FORCE_MARK_DIRTY};
      }
   }

   public interface UpdateFlag {
   }
}
