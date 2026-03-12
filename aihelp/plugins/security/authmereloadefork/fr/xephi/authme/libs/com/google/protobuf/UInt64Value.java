package fr.xephi.authme.libs.com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class UInt64Value extends GeneratedMessageV3 implements UInt64ValueOrBuilder {
   private static final long serialVersionUID = 0L;
   public static final int VALUE_FIELD_NUMBER = 1;
   private long value_;
   private byte memoizedIsInitialized;
   private static final UInt64Value DEFAULT_INSTANCE = new UInt64Value();
   private static final Parser<UInt64Value> PARSER = new AbstractParser<UInt64Value>() {
      public UInt64Value parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         UInt64Value.Builder builder = UInt64Value.newBuilder();

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

   private UInt64Value(GeneratedMessageV3.Builder<?> builder) {
      super(builder);
      this.memoizedIsInitialized = -1;
   }

   private UInt64Value() {
      this.memoizedIsInitialized = -1;
   }

   protected Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
      return new UInt64Value();
   }

   public final UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
   }

   public static final Descriptors.Descriptor getDescriptor() {
      return WrappersProto.internal_static_google_protobuf_UInt64Value_descriptor;
   }

   protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
      return WrappersProto.internal_static_google_protobuf_UInt64Value_fieldAccessorTable.ensureFieldAccessorsInitialized(UInt64Value.class, UInt64Value.Builder.class);
   }

   public long getValue() {
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
      if (this.value_ != 0L) {
         output.writeUInt64(1, this.value_);
      }

      this.getUnknownFields().writeTo(output);
   }

   public int getSerializedSize() {
      int size = this.memoizedSize;
      if (size != -1) {
         return size;
      } else {
         size = 0;
         if (this.value_ != 0L) {
            size += CodedOutputStream.computeUInt64Size(1, this.value_);
         }

         size += this.getUnknownFields().getSerializedSize();
         this.memoizedSize = size;
         return size;
      }
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof UInt64Value)) {
         return super.equals(obj);
      } else {
         UInt64Value other = (UInt64Value)obj;
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
         hash = 53 * hash + Internal.hashLong(this.getValue());
         hash = 29 * hash + this.getUnknownFields().hashCode();
         this.memoizedHashCode = hash;
         return hash;
      }
   }

   public static UInt64Value parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
      return (UInt64Value)PARSER.parseFrom(data);
   }

   public static UInt64Value parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (UInt64Value)PARSER.parseFrom(data, extensionRegistry);
   }

   public static UInt64Value parseFrom(ByteString data) throws InvalidProtocolBufferException {
      return (UInt64Value)PARSER.parseFrom(data);
   }

   public static UInt64Value parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (UInt64Value)PARSER.parseFrom(data, extensionRegistry);
   }

   public static UInt64Value parseFrom(byte[] data) throws InvalidProtocolBufferException {
      return (UInt64Value)PARSER.parseFrom(data);
   }

   public static UInt64Value parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (UInt64Value)PARSER.parseFrom(data, extensionRegistry);
   }

   public static UInt64Value parseFrom(InputStream input) throws IOException {
      return (UInt64Value)GeneratedMessageV3.parseWithIOException(PARSER, input);
   }

   public static UInt64Value parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (UInt64Value)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
   }

   public static UInt64Value parseDelimitedFrom(InputStream input) throws IOException {
      return (UInt64Value)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
   }

   public static UInt64Value parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (UInt64Value)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
   }

   public static UInt64Value parseFrom(CodedInputStream input) throws IOException {
      return (UInt64Value)GeneratedMessageV3.parseWithIOException(PARSER, input);
   }

   public static UInt64Value parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (UInt64Value)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
   }

   public UInt64Value.Builder newBuilderForType() {
      return newBuilder();
   }

   public static UInt64Value.Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
   }

   public static UInt64Value.Builder newBuilder(UInt64Value prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
   }

   public UInt64Value.Builder toBuilder() {
      return this == DEFAULT_INSTANCE ? new UInt64Value.Builder() : (new UInt64Value.Builder()).mergeFrom(this);
   }

   protected UInt64Value.Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
      UInt64Value.Builder builder = new UInt64Value.Builder(parent);
      return builder;
   }

   public static UInt64Value getDefaultInstance() {
      return DEFAULT_INSTANCE;
   }

   public static UInt64Value of(long value) {
      return newBuilder().setValue(value).build();
   }

   public static Parser<UInt64Value> parser() {
      return PARSER;
   }

   public Parser<UInt64Value> getParserForType() {
      return PARSER;
   }

   public UInt64Value getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
   }

   // $FF: synthetic method
   UInt64Value(GeneratedMessageV3.Builder x0, Object x1) {
      this(x0);
   }

   public static final class Builder extends GeneratedMessageV3.Builder<UInt64Value.Builder> implements UInt64ValueOrBuilder {
      private long value_;

      public static final Descriptors.Descriptor getDescriptor() {
         return WrappersProto.internal_static_google_protobuf_UInt64Value_descriptor;
      }

      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
         return WrappersProto.internal_static_google_protobuf_UInt64Value_fieldAccessorTable.ensureFieldAccessorsInitialized(UInt64Value.class, UInt64Value.Builder.class);
      }

      private Builder() {
      }

      private Builder(GeneratedMessageV3.BuilderParent parent) {
         super(parent);
      }

      public UInt64Value.Builder clear() {
         super.clear();
         this.value_ = 0L;
         return this;
      }

      public Descriptors.Descriptor getDescriptorForType() {
         return WrappersProto.internal_static_google_protobuf_UInt64Value_descriptor;
      }

      public UInt64Value getDefaultInstanceForType() {
         return UInt64Value.getDefaultInstance();
      }

      public UInt64Value build() {
         UInt64Value result = this.buildPartial();
         if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
         } else {
            return result;
         }
      }

      public UInt64Value buildPartial() {
         UInt64Value result = new UInt64Value(this);
         result.value_ = this.value_;
         this.onBuilt();
         return result;
      }

      public UInt64Value.Builder clone() {
         return (UInt64Value.Builder)super.clone();
      }

      public UInt64Value.Builder setField(Descriptors.FieldDescriptor field, Object value) {
         return (UInt64Value.Builder)super.setField(field, value);
      }

      public UInt64Value.Builder clearField(Descriptors.FieldDescriptor field) {
         return (UInt64Value.Builder)super.clearField(field);
      }

      public UInt64Value.Builder clearOneof(Descriptors.OneofDescriptor oneof) {
         return (UInt64Value.Builder)super.clearOneof(oneof);
      }

      public UInt64Value.Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
         return (UInt64Value.Builder)super.setRepeatedField(field, index, value);
      }

      public UInt64Value.Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
         return (UInt64Value.Builder)super.addRepeatedField(field, value);
      }

      public UInt64Value.Builder mergeFrom(Message other) {
         if (other instanceof UInt64Value) {
            return this.mergeFrom((UInt64Value)other);
         } else {
            super.mergeFrom(other);
            return this;
         }
      }

      public UInt64Value.Builder mergeFrom(UInt64Value other) {
         if (other == UInt64Value.getDefaultInstance()) {
            return this;
         } else {
            if (other.getValue() != 0L) {
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

      public UInt64Value.Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
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
                     this.value_ = input.readUInt64();
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

      public long getValue() {
         return this.value_;
      }

      public UInt64Value.Builder setValue(long value) {
         this.value_ = value;
         this.onChanged();
         return this;
      }

      public UInt64Value.Builder clearValue() {
         this.value_ = 0L;
         this.onChanged();
         return this;
      }

      public final UInt64Value.Builder setUnknownFields(UnknownFieldSet unknownFields) {
         return (UInt64Value.Builder)super.setUnknownFields(unknownFields);
      }

      public final UInt64Value.Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
         return (UInt64Value.Builder)super.mergeUnknownFields(unknownFields);
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
