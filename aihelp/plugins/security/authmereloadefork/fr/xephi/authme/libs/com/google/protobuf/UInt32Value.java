package fr.xephi.authme.libs.com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class UInt32Value extends GeneratedMessageV3 implements UInt32ValueOrBuilder {
   private static final long serialVersionUID = 0L;
   public static final int VALUE_FIELD_NUMBER = 1;
   private int value_;
   private byte memoizedIsInitialized;
   private static final UInt32Value DEFAULT_INSTANCE = new UInt32Value();
   private static final Parser<UInt32Value> PARSER = new AbstractParser<UInt32Value>() {
      public UInt32Value parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         UInt32Value.Builder builder = UInt32Value.newBuilder();

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

   private UInt32Value(GeneratedMessageV3.Builder<?> builder) {
      super(builder);
      this.memoizedIsInitialized = -1;
   }

   private UInt32Value() {
      this.memoizedIsInitialized = -1;
   }

   protected Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
      return new UInt32Value();
   }

   public final UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
   }

   public static final Descriptors.Descriptor getDescriptor() {
      return WrappersProto.internal_static_google_protobuf_UInt32Value_descriptor;
   }

   protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
      return WrappersProto.internal_static_google_protobuf_UInt32Value_fieldAccessorTable.ensureFieldAccessorsInitialized(UInt32Value.class, UInt32Value.Builder.class);
   }

   public int getValue() {
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
      if (this.value_ != 0) {
         output.writeUInt32(1, this.value_);
      }

      this.getUnknownFields().writeTo(output);
   }

   public int getSerializedSize() {
      int size = this.memoizedSize;
      if (size != -1) {
         return size;
      } else {
         size = 0;
         if (this.value_ != 0) {
            size += CodedOutputStream.computeUInt32Size(1, this.value_);
         }

         size += this.getUnknownFields().getSerializedSize();
         this.memoizedSize = size;
         return size;
      }
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof UInt32Value)) {
         return super.equals(obj);
      } else {
         UInt32Value other = (UInt32Value)obj;
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
         hash = 53 * hash + this.getValue();
         hash = 29 * hash + this.getUnknownFields().hashCode();
         this.memoizedHashCode = hash;
         return hash;
      }
   }

   public static UInt32Value parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
      return (UInt32Value)PARSER.parseFrom(data);
   }

   public static UInt32Value parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (UInt32Value)PARSER.parseFrom(data, extensionRegistry);
   }

   public static UInt32Value parseFrom(ByteString data) throws InvalidProtocolBufferException {
      return (UInt32Value)PARSER.parseFrom(data);
   }

   public static UInt32Value parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (UInt32Value)PARSER.parseFrom(data, extensionRegistry);
   }

   public static UInt32Value parseFrom(byte[] data) throws InvalidProtocolBufferException {
      return (UInt32Value)PARSER.parseFrom(data);
   }

   public static UInt32Value parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (UInt32Value)PARSER.parseFrom(data, extensionRegistry);
   }

   public static UInt32Value parseFrom(InputStream input) throws IOException {
      return (UInt32Value)GeneratedMessageV3.parseWithIOException(PARSER, input);
   }

   public static UInt32Value parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (UInt32Value)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
   }

   public static UInt32Value parseDelimitedFrom(InputStream input) throws IOException {
      return (UInt32Value)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
   }

   public static UInt32Value parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (UInt32Value)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
   }

   public static UInt32Value parseFrom(CodedInputStream input) throws IOException {
      return (UInt32Value)GeneratedMessageV3.parseWithIOException(PARSER, input);
   }

   public static UInt32Value parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (UInt32Value)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
   }

   public UInt32Value.Builder newBuilderForType() {
      return newBuilder();
   }

   public static UInt32Value.Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
   }

   public static UInt32Value.Builder newBuilder(UInt32Value prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
   }

   public UInt32Value.Builder toBuilder() {
      return this == DEFAULT_INSTANCE ? new UInt32Value.Builder() : (new UInt32Value.Builder()).mergeFrom(this);
   }

   protected UInt32Value.Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
      UInt32Value.Builder builder = new UInt32Value.Builder(parent);
      return builder;
   }

   public static UInt32Value getDefaultInstance() {
      return DEFAULT_INSTANCE;
   }

   public static UInt32Value of(int value) {
      return newBuilder().setValue(value).build();
   }

   public static Parser<UInt32Value> parser() {
      return PARSER;
   }

   public Parser<UInt32Value> getParserForType() {
      return PARSER;
   }

   public UInt32Value getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
   }

   // $FF: synthetic method
   UInt32Value(GeneratedMessageV3.Builder x0, Object x1) {
      this(x0);
   }

   public static final class Builder extends GeneratedMessageV3.Builder<UInt32Value.Builder> implements UInt32ValueOrBuilder {
      private int value_;

      public static final Descriptors.Descriptor getDescriptor() {
         return WrappersProto.internal_static_google_protobuf_UInt32Value_descriptor;
      }

      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
         return WrappersProto.internal_static_google_protobuf_UInt32Value_fieldAccessorTable.ensureFieldAccessorsInitialized(UInt32Value.class, UInt32Value.Builder.class);
      }

      private Builder() {
      }

      private Builder(GeneratedMessageV3.BuilderParent parent) {
         super(parent);
      }

      public UInt32Value.Builder clear() {
         super.clear();
         this.value_ = 0;
         return this;
      }

      public Descriptors.Descriptor getDescriptorForType() {
         return WrappersProto.internal_static_google_protobuf_UInt32Value_descriptor;
      }

      public UInt32Value getDefaultInstanceForType() {
         return UInt32Value.getDefaultInstance();
      }

      public UInt32Value build() {
         UInt32Value result = this.buildPartial();
         if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
         } else {
            return result;
         }
      }

      public UInt32Value buildPartial() {
         UInt32Value result = new UInt32Value(this);
         result.value_ = this.value_;
         this.onBuilt();
         return result;
      }

      public UInt32Value.Builder clone() {
         return (UInt32Value.Builder)super.clone();
      }

      public UInt32Value.Builder setField(Descriptors.FieldDescriptor field, Object value) {
         return (UInt32Value.Builder)super.setField(field, value);
      }

      public UInt32Value.Builder clearField(Descriptors.FieldDescriptor field) {
         return (UInt32Value.Builder)super.clearField(field);
      }

      public UInt32Value.Builder clearOneof(Descriptors.OneofDescriptor oneof) {
         return (UInt32Value.Builder)super.clearOneof(oneof);
      }

      public UInt32Value.Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
         return (UInt32Value.Builder)super.setRepeatedField(field, index, value);
      }

      public UInt32Value.Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
         return (UInt32Value.Builder)super.addRepeatedField(field, value);
      }

      public UInt32Value.Builder mergeFrom(Message other) {
         if (other instanceof UInt32Value) {
            return this.mergeFrom((UInt32Value)other);
         } else {
            super.mergeFrom(other);
            return this;
         }
      }

      public UInt32Value.Builder mergeFrom(UInt32Value other) {
         if (other == UInt32Value.getDefaultInstance()) {
            return this;
         } else {
            if (other.getValue() != 0) {
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

      public UInt32Value.Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
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
                     this.value_ = input.readUInt32();
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

      public int getValue() {
         return this.value_;
      }

      public UInt32Value.Builder setValue(int value) {
         this.value_ = value;
         this.onChanged();
         return this;
      }

      public UInt32Value.Builder clearValue() {
         this.value_ = 0;
         this.onChanged();
         return this;
      }

      public final UInt32Value.Builder setUnknownFields(UnknownFieldSet unknownFields) {
         return (UInt32Value.Builder)super.setUnknownFields(unknownFields);
      }

      public final UInt32Value.Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
         return (UInt32Value.Builder)super.mergeUnknownFields(unknownFields);
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
