package fr.xephi.authme.libs.com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class Empty extends GeneratedMessageV3 implements EmptyOrBuilder {
   private static final long serialVersionUID = 0L;
   private byte memoizedIsInitialized;
   private static final Empty DEFAULT_INSTANCE = new Empty();
   private static final Parser<Empty> PARSER = new AbstractParser<Empty>() {
      public Empty parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         Empty.Builder builder = Empty.newBuilder();

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

   private Empty(GeneratedMessageV3.Builder<?> builder) {
      super(builder);
      this.memoizedIsInitialized = -1;
   }

   private Empty() {
      this.memoizedIsInitialized = -1;
   }

   protected Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
      return new Empty();
   }

   public final UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
   }

   public static final Descriptors.Descriptor getDescriptor() {
      return EmptyProto.internal_static_google_protobuf_Empty_descriptor;
   }

   protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
      return EmptyProto.internal_static_google_protobuf_Empty_fieldAccessorTable.ensureFieldAccessorsInitialized(Empty.class, Empty.Builder.class);
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
      this.getUnknownFields().writeTo(output);
   }

   public int getSerializedSize() {
      int size = this.memoizedSize;
      if (size != -1) {
         return size;
      } else {
         int size = 0;
         size = size + this.getUnknownFields().getSerializedSize();
         this.memoizedSize = size;
         return size;
      }
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof Empty)) {
         return super.equals(obj);
      } else {
         Empty other = (Empty)obj;
         return this.getUnknownFields().equals(other.getUnknownFields());
      }
   }

   public int hashCode() {
      if (this.memoizedHashCode != 0) {
         return this.memoizedHashCode;
      } else {
         int hash = 41;
         int hash = 19 * hash + getDescriptor().hashCode();
         hash = 29 * hash + this.getUnknownFields().hashCode();
         this.memoizedHashCode = hash;
         return hash;
      }
   }

   public static Empty parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
      return (Empty)PARSER.parseFrom(data);
   }

   public static Empty parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Empty)PARSER.parseFrom(data, extensionRegistry);
   }

   public static Empty parseFrom(ByteString data) throws InvalidProtocolBufferException {
      return (Empty)PARSER.parseFrom(data);
   }

   public static Empty parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Empty)PARSER.parseFrom(data, extensionRegistry);
   }

   public static Empty parseFrom(byte[] data) throws InvalidProtocolBufferException {
      return (Empty)PARSER.parseFrom(data);
   }

   public static Empty parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Empty)PARSER.parseFrom(data, extensionRegistry);
   }

   public static Empty parseFrom(InputStream input) throws IOException {
      return (Empty)GeneratedMessageV3.parseWithIOException(PARSER, input);
   }

   public static Empty parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (Empty)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
   }

   public static Empty parseDelimitedFrom(InputStream input) throws IOException {
      return (Empty)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
   }

   public static Empty parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (Empty)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
   }

   public static Empty parseFrom(CodedInputStream input) throws IOException {
      return (Empty)GeneratedMessageV3.parseWithIOException(PARSER, input);
   }

   public static Empty parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (Empty)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
   }

   public Empty.Builder newBuilderForType() {
      return newBuilder();
   }

   public static Empty.Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
   }

   public static Empty.Builder newBuilder(Empty prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
   }

   public Empty.Builder toBuilder() {
      return this == DEFAULT_INSTANCE ? new Empty.Builder() : (new Empty.Builder()).mergeFrom(this);
   }

   protected Empty.Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
      Empty.Builder builder = new Empty.Builder(parent);
      return builder;
   }

   public static Empty getDefaultInstance() {
      return DEFAULT_INSTANCE;
   }

   public static Parser<Empty> parser() {
      return PARSER;
   }

   public Parser<Empty> getParserForType() {
      return PARSER;
   }

   public Empty getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
   }

   // $FF: synthetic method
   Empty(GeneratedMessageV3.Builder x0, Object x1) {
      this(x0);
   }

   public static final class Builder extends GeneratedMessageV3.Builder<Empty.Builder> implements EmptyOrBuilder {
      public static final Descriptors.Descriptor getDescriptor() {
         return EmptyProto.internal_static_google_protobuf_Empty_descriptor;
      }

      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
         return EmptyProto.internal_static_google_protobuf_Empty_fieldAccessorTable.ensureFieldAccessorsInitialized(Empty.class, Empty.Builder.class);
      }

      private Builder() {
      }

      private Builder(GeneratedMessageV3.BuilderParent parent) {
         super(parent);
      }

      public Empty.Builder clear() {
         super.clear();
         return this;
      }

      public Descriptors.Descriptor getDescriptorForType() {
         return EmptyProto.internal_static_google_protobuf_Empty_descriptor;
      }

      public Empty getDefaultInstanceForType() {
         return Empty.getDefaultInstance();
      }

      public Empty build() {
         Empty result = this.buildPartial();
         if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
         } else {
            return result;
         }
      }

      public Empty buildPartial() {
         Empty result = new Empty(this);
         this.onBuilt();
         return result;
      }

      public Empty.Builder clone() {
         return (Empty.Builder)super.clone();
      }

      public Empty.Builder setField(Descriptors.FieldDescriptor field, Object value) {
         return (Empty.Builder)super.setField(field, value);
      }

      public Empty.Builder clearField(Descriptors.FieldDescriptor field) {
         return (Empty.Builder)super.clearField(field);
      }

      public Empty.Builder clearOneof(Descriptors.OneofDescriptor oneof) {
         return (Empty.Builder)super.clearOneof(oneof);
      }

      public Empty.Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
         return (Empty.Builder)super.setRepeatedField(field, index, value);
      }

      public Empty.Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
         return (Empty.Builder)super.addRepeatedField(field, value);
      }

      public Empty.Builder mergeFrom(Message other) {
         if (other instanceof Empty) {
            return this.mergeFrom((Empty)other);
         } else {
            super.mergeFrom(other);
            return this;
         }
      }

      public Empty.Builder mergeFrom(Empty other) {
         if (other == Empty.getDefaultInstance()) {
            return this;
         } else {
            this.mergeUnknownFields(other.getUnknownFields());
            this.onChanged();
            return this;
         }
      }

      public final boolean isInitialized() {
         return true;
      }

      public Empty.Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
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

      public final Empty.Builder setUnknownFields(UnknownFieldSet unknownFields) {
         return (Empty.Builder)super.setUnknownFields(unknownFields);
      }

      public final Empty.Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
         return (Empty.Builder)super.mergeUnknownFields(unknownFields);
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
