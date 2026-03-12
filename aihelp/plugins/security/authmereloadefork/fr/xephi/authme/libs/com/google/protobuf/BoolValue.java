package fr.xephi.authme.libs.com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class BoolValue extends GeneratedMessageV3 implements BoolValueOrBuilder {
   private static final long serialVersionUID = 0L;
   public static final int VALUE_FIELD_NUMBER = 1;
   private boolean value_;
   private byte memoizedIsInitialized;
   private static final BoolValue DEFAULT_INSTANCE = new BoolValue();
   private static final Parser<BoolValue> PARSER = new AbstractParser<BoolValue>() {
      public BoolValue parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         BoolValue.Builder builder = BoolValue.newBuilder();

         try {
            builder.mergeFrom(input, extensionRegistry);
         } catch (InvalidProtocolBufferException var5) {
            throw var5.setUnfinishedMessage(builder.buildPartial());
         } catch (UninitializedMessageException var6) {
            throw var6.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
         } catch (IOException var7) {
            throw (new InvalidProtocolBufferException(var7)).setUnfinishedMessage(builder.buildPartial());
         }

         return builder.buildPartial();
      }
   };

   private BoolValue(GeneratedMessageV3.Builder<?> builder) {
      super(builder);
      this.memoizedIsInitialized = -1;
   }

   private BoolValue() {
      this.memoizedIsInitialized = -1;
   }

   protected Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
      return new BoolValue();
   }

   public final UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
   }

   public static final Descriptors.Descriptor getDescriptor() {
      return WrappersProto.internal_static_google_protobuf_BoolValue_descriptor;
   }

   protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
      return WrappersProto.internal_static_google_protobuf_BoolValue_fieldAccessorTable.ensureFieldAccessorsInitialized(BoolValue.class, BoolValue.Builder.class);
   }

   public boolean getValue() {
      return this.value_;
   }

   public final boolean isInitialized() {
      byte isInitialized = this.memoizedIsInitialized;
      if (isInitialized == 1) {
         return true;
      } else if (isInitialized == 0) {
         return false;
      } else {
         this.memoizedIsInitialized = 1;
         return true;
      }
   }

   public void writeTo(CodedOutputStream output) throws IOException {
      if (this.value_) {
         output.writeBool(1, this.value_);
      }

      this.getUnknownFields().writeTo(output);
   }

   public int getSerializedSize() {
      int size = this.memoizedSize;
      if (size != -1) {
         return size;
      } else {
         size = 0;
         if (this.value_) {
            size += CodedOutputStream.computeBoolSize(1, this.value_);
         }

         size += this.getUnknownFields().getSerializedSize();
         this.memoizedSize = size;
         return size;
      }
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof BoolValue)) {
         return super.equals(obj);
      } else {
         BoolValue other = (BoolValue)obj;
         if (this.getValue() != other.getValue()) {
            return false;
         } else {
            return this.getUnknownFields().equals(other.getUnknownFields());
         }
      }
   }

   public int hashCode() {
      if (this.memoizedHashCode != 0) {
         return this.memoizedHashCode;
      } else {
         int hash = 41;
         int hash = 19 * hash + getDescriptor().hashCode();
         hash = 37 * hash + 1;
         hash = 53 * hash + Internal.hashBoolean(this.getValue());
         hash = 29 * hash + this.getUnknownFields().hashCode();
         this.memoizedHashCode = hash;
         return hash;
      }
   }

   public static BoolValue parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
      return (BoolValue)PARSER.parseFrom(data);
   }

   public static BoolValue parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (BoolValue)PARSER.parseFrom(data, extensionRegistry);
   }

   public static BoolValue parseFrom(ByteString data) throws InvalidProtocolBufferException {
      return (BoolValue)PARSER.parseFrom(data);
   }

   public static BoolValue parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (BoolValue)PARSER.parseFrom(data, extensionRegistry);
   }

   public static BoolValue parseFrom(byte[] data) throws InvalidProtocolBufferException {
      return (BoolValue)PARSER.parseFrom(data);
   }

   public static BoolValue parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (BoolValue)PARSER.parseFrom(data, extensionRegistry);
   }

   public static BoolValue parseFrom(InputStream input) throws IOException {
      return (BoolValue)GeneratedMessageV3.parseWithIOException(PARSER, input);
   }

   public static BoolValue parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (BoolValue)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
   }

   public static BoolValue parseDelimitedFrom(InputStream input) throws IOException {
      return (BoolValue)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
   }

   public static BoolValue parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (BoolValue)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
   }

   public static BoolValue parseFrom(CodedInputStream input) throws IOException {
      return (BoolValue)GeneratedMessageV3.parseWithIOException(PARSER, input);
   }

   public static BoolValue parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (BoolValue)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
   }

   public BoolValue.Builder newBuilderForType() {
      return newBuilder();
   }

   public static BoolValue.Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
   }

   public static BoolValue.Builder newBuilder(BoolValue prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
   }

   public BoolValue.Builder toBuilder() {
      return this == DEFAULT_INSTANCE ? new BoolValue.Builder() : (new BoolValue.Builder()).mergeFrom(this);
   }

   protected BoolValue.Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
      BoolValue.Builder builder = new BoolValue.Builder(parent);
      return builder;
   }

   public static BoolValue getDefaultInstance() {
      return DEFAULT_INSTANCE;
   }

   public static BoolValue of(boolean value) {
      return newBuilder().setValue(value).build();
   }

   public static Parser<BoolValue> parser() {
      return PARSER;
   }

   public Parser<BoolValue> getParserForType() {
      return PARSER;
   }

   public BoolValue getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
   }

   // $FF: synthetic method
   BoolValue(GeneratedMessageV3.Builder x0, Object x1) {
      this(x0);
   }

   public static final class Builder extends GeneratedMessageV3.Builder<BoolValue.Builder> implements BoolValueOrBuilder {
      private boolean value_;

      public static final Descriptors.Descriptor getDescriptor() {
         return WrappersProto.internal_static_google_protobuf_BoolValue_descriptor;
      }

      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
         return WrappersProto.internal_static_google_protobuf_BoolValue_fieldAccessorTable.ensureFieldAccessorsInitialized(BoolValue.class, BoolValue.Builder.class);
      }

      private Builder() {
      }

      private Builder(GeneratedMessageV3.BuilderParent parent) {
         super(parent);
      }

      public BoolValue.Builder clear() {
         super.clear();
         this.value_ = false;
         return this;
      }

      public Descriptors.Descriptor getDescriptorForType() {
         return WrappersProto.internal_static_google_protobuf_BoolValue_descriptor;
      }

      public BoolValue getDefaultInstanceForType() {
         return BoolValue.getDefaultInstance();
      }

      public BoolValue build() {
         BoolValue result = this.buildPartial();
         if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
         } else {
            return result;
         }
      }

      public BoolValue buildPartial() {
         BoolValue result = new BoolValue(this);
         result.value_ = this.value_;
         this.onBuilt();
         return result;
      }

      public BoolValue.Builder clone() {
         return (BoolValue.Builder)super.clone();
      }

      public BoolValue.Builder setField(Descriptors.FieldDescriptor field, Object value) {
         return (BoolValue.Builder)super.setField(field, value);
      }

      public BoolValue.Builder clearField(Descriptors.FieldDescriptor field) {
         return (BoolValue.Builder)super.clearField(field);
      }

      public BoolValue.Builder clearOneof(Descriptors.OneofDescriptor oneof) {
         return (BoolValue.Builder)super.clearOneof(oneof);
      }

      public BoolValue.Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
         return (BoolValue.Builder)super.setRepeatedField(field, index, value);
      }

      public BoolValue.Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
         return (BoolValue.Builder)super.addRepeatedField(field, value);
      }

      public BoolValue.Builder mergeFrom(Message other) {
         if (other instanceof BoolValue) {
            return this.mergeFrom((BoolValue)other);
         } else {
            super.mergeFrom(other);
            return this;
         }
      }

      public BoolValue.Builder mergeFrom(BoolValue other) {
         if (other == BoolValue.getDefaultInstance()) {
            return this;
         } else {
            if (other.getValue()) {
               this.setValue(other.getValue());
            }

            this.mergeUnknownFields(other.getUnknownFields());
            this.onChanged();
            return this;
         }
      }

      public final boolean isInitialized() {
         return true;
      }

      public BoolValue.Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         if (extensionRegistry == null) {
            throw new NullPointerException();
         } else {
            try {
               boolean done = false;

               while(!done) {
                  int tag = input.readTag();
                  switch(tag) {
                  case 0:
                     done = true;
                     break;
                  case 8:
                     this.value_ = input.readBool();
                     break;
                  default:
                     if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                        done = true;
                     }
                  }
               }
            } catch (InvalidProtocolBufferException var8) {
               throw var8.unwrapIOException();
            } finally {
               this.onChanged();
            }

            return this;
         }
      }

      public boolean getValue() {
         return this.value_;
      }

      public BoolValue.Builder setValue(boolean value) {
         this.value_ = value;
         this.onChanged();
         return this;
      }

      public BoolValue.Builder clearValue() {
         this.value_ = false;
         this.onChanged();
         return this;
      }

      public final BoolValue.Builder setUnknownFields(UnknownFieldSet unknownFields) {
         return (BoolValue.Builder)super.setUnknownFields(unknownFields);
      }

      public final BoolValue.Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
         return (BoolValue.Builder)super.mergeUnknownFields(unknownFields);
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }

      // $FF: synthetic method
      Builder(GeneratedMessageV3.BuilderParent x0, Object x1) {
         this(x0);
      }
   }
}
