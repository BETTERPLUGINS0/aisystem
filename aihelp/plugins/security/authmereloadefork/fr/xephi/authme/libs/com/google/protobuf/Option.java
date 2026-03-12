package fr.xephi.authme.libs.com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class Option extends GeneratedMessageV3 implements OptionOrBuilder {
   private static final long serialVersionUID = 0L;
   public static final int NAME_FIELD_NUMBER = 1;
   private volatile Object name_;
   public static final int VALUE_FIELD_NUMBER = 2;
   private Any value_;
   private byte memoizedIsInitialized;
   private static final Option DEFAULT_INSTANCE = new Option();
   private static final Parser<Option> PARSER = new AbstractParser<Option>() {
      public Option parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         Option.Builder builder = Option.newBuilder();

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

   private Option(GeneratedMessageV3.Builder<?> builder) {
      super(builder);
      this.memoizedIsInitialized = -1;
   }

   private Option() {
      this.memoizedIsInitialized = -1;
      this.name_ = "";
   }

   protected Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
      return new Option();
   }

   public final UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
   }

   public static final Descriptors.Descriptor getDescriptor() {
      return TypeProto.internal_static_google_protobuf_Option_descriptor;
   }

   protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
      return TypeProto.internal_static_google_protobuf_Option_fieldAccessorTable.ensureFieldAccessorsInitialized(Option.class, Option.Builder.class);
   }

   public String getName() {
      Object ref = this.name_;
      if (ref instanceof String) {
         return (String)ref;
      } else {
         ByteString bs = (ByteString)ref;
         String s = bs.toStringUtf8();
         this.name_ = s;
         return s;
      }
   }

   public ByteString getNameBytes() {
      Object ref = this.name_;
      if (ref instanceof String) {
         ByteString b = ByteString.copyFromUtf8((String)ref);
         this.name_ = b;
         return b;
      } else {
         return (ByteString)ref;
      }
   }

   public boolean hasValue() {
      return this.value_ != null;
   }

   public Any getValue() {
      return this.value_ == null ? Any.getDefaultInstance() : this.value_;
   }

   public AnyOrBuilder getValueOrBuilder() {
      return this.getValue();
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
      if (!GeneratedMessageV3.isStringEmpty(this.name_)) {
         GeneratedMessageV3.writeString(output, 1, this.name_);
      }

      if (this.value_ != null) {
         output.writeMessage(2, this.getValue());
      }

      this.getUnknownFields().writeTo(output);
   }

   public int getSerializedSize() {
      int size = this.memoizedSize;
      if (size != -1) {
         return size;
      } else {
         size = 0;
         if (!GeneratedMessageV3.isStringEmpty(this.name_)) {
            size += GeneratedMessageV3.computeStringSize(1, this.name_);
         }

         if (this.value_ != null) {
            size += CodedOutputStream.computeMessageSize(2, this.getValue());
         }

         size += this.getUnknownFields().getSerializedSize();
         this.memoizedSize = size;
         return size;
      }
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof Option)) {
         return super.equals(obj);
      } else {
         Option other = (Option)obj;
         if (!this.getName().equals(other.getName())) {
            return false;
         } else if (this.hasValue() != other.hasValue()) {
            return false;
         } else if (this.hasValue() && !this.getValue().equals(other.getValue())) {
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
         hash = 53 * hash + this.getName().hashCode();
         if (this.hasValue()) {
            hash = 37 * hash + 2;
            hash = 53 * hash + this.getValue().hashCode();
         }

         hash = 29 * hash + this.getUnknownFields().hashCode();
         this.memoizedHashCode = hash;
         return hash;
      }
   }

   public static Option parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
      return (Option)PARSER.parseFrom(data);
   }

   public static Option parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Option)PARSER.parseFrom(data, extensionRegistry);
   }

   public static Option parseFrom(ByteString data) throws InvalidProtocolBufferException {
      return (Option)PARSER.parseFrom(data);
   }

   public static Option parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Option)PARSER.parseFrom(data, extensionRegistry);
   }

   public static Option parseFrom(byte[] data) throws InvalidProtocolBufferException {
      return (Option)PARSER.parseFrom(data);
   }

   public static Option parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Option)PARSER.parseFrom(data, extensionRegistry);
   }

   public static Option parseFrom(InputStream input) throws IOException {
      return (Option)GeneratedMessageV3.parseWithIOException(PARSER, input);
   }

   public static Option parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (Option)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
   }

   public static Option parseDelimitedFrom(InputStream input) throws IOException {
      return (Option)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
   }

   public static Option parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (Option)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
   }

   public static Option parseFrom(CodedInputStream input) throws IOException {
      return (Option)GeneratedMessageV3.parseWithIOException(PARSER, input);
   }

   public static Option parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (Option)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
   }

   public Option.Builder newBuilderForType() {
      return newBuilder();
   }

   public static Option.Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
   }

   public static Option.Builder newBuilder(Option prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
   }

   public Option.Builder toBuilder() {
      return this == DEFAULT_INSTANCE ? new Option.Builder() : (new Option.Builder()).mergeFrom(this);
   }

   protected Option.Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
      Option.Builder builder = new Option.Builder(parent);
      return builder;
   }

   public static Option getDefaultInstance() {
      return DEFAULT_INSTANCE;
   }

   public static Parser<Option> parser() {
      return PARSER;
   }

   public Parser<Option> getParserForType() {
      return PARSER;
   }

   public Option getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
   }

   // $FF: synthetic method
   Option(GeneratedMessageV3.Builder x0, Object x1) {
      this(x0);
   }

   public static final class Builder extends GeneratedMessageV3.Builder<Option.Builder> implements OptionOrBuilder {
      private Object name_;
      private Any value_;
      private SingleFieldBuilderV3<Any, Any.Builder, AnyOrBuilder> valueBuilder_;

      public static final Descriptors.Descriptor getDescriptor() {
         return TypeProto.internal_static_google_protobuf_Option_descriptor;
      }

      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
         return TypeProto.internal_static_google_protobuf_Option_fieldAccessorTable.ensureFieldAccessorsInitialized(Option.class, Option.Builder.class);
      }

      private Builder() {
         this.name_ = "";
      }

      private Builder(GeneratedMessageV3.BuilderParent parent) {
         super(parent);
         this.name_ = "";
      }

      public Option.Builder clear() {
         super.clear();
         this.name_ = "";
         if (this.valueBuilder_ == null) {
            this.value_ = null;
         } else {
            this.value_ = null;
            this.valueBuilder_ = null;
         }

         return this;
      }

      public Descriptors.Descriptor getDescriptorForType() {
         return TypeProto.internal_static_google_protobuf_Option_descriptor;
      }

      public Option getDefaultInstanceForType() {
         return Option.getDefaultInstance();
      }

      public Option build() {
         Option result = this.buildPartial();
         if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
         } else {
            return result;
         }
      }

      public Option buildPartial() {
         Option result = new Option(this);
         result.name_ = this.name_;
         if (this.valueBuilder_ == null) {
            result.value_ = this.value_;
         } else {
            result.value_ = (Any)this.valueBuilder_.build();
         }

         this.onBuilt();
         return result;
      }

      public Option.Builder clone() {
         return (Option.Builder)super.clone();
      }

      public Option.Builder setField(Descriptors.FieldDescriptor field, Object value) {
         return (Option.Builder)super.setField(field, value);
      }

      public Option.Builder clearField(Descriptors.FieldDescriptor field) {
         return (Option.Builder)super.clearField(field);
      }

      public Option.Builder clearOneof(Descriptors.OneofDescriptor oneof) {
         return (Option.Builder)super.clearOneof(oneof);
      }

      public Option.Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
         return (Option.Builder)super.setRepeatedField(field, index, value);
      }

      public Option.Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
         return (Option.Builder)super.addRepeatedField(field, value);
      }

      public Option.Builder mergeFrom(Message other) {
         if (other instanceof Option) {
            return this.mergeFrom((Option)other);
         } else {
            super.mergeFrom(other);
            return this;
         }
      }

      public Option.Builder mergeFrom(Option other) {
         if (other == Option.getDefaultInstance()) {
            return this;
         } else {
            if (!other.getName().isEmpty()) {
               this.name_ = other.name_;
               this.onChanged();
            }

            if (other.hasValue()) {
               this.mergeValue(other.getValue());
            }

            this.mergeUnknownFields(other.getUnknownFields());
            this.onChanged();
            return this;
         }
      }

      public final boolean isInitialized() {
         return true;
      }

      public Option.Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
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
                  case 10:
                     this.name_ = input.readStringRequireUtf8();
                     break;
                  case 18:
                     input.readMessage((MessageLite.Builder)this.getValueFieldBuilder().getBuilder(), extensionRegistry);
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

      public String getName() {
         Object ref = this.name_;
         if (!(ref instanceof String)) {
            ByteString bs = (ByteString)ref;
            String s = bs.toStringUtf8();
            this.name_ = s;
            return s;
         } else {
            return (String)ref;
         }
      }

      public ByteString getNameBytes() {
         Object ref = this.name_;
         if (ref instanceof String) {
            ByteString b = ByteString.copyFromUtf8((String)ref);
            this.name_ = b;
            return b;
         } else {
            return (ByteString)ref;
         }
      }

      public Option.Builder setName(String value) {
         if (value == null) {
            throw new NullPointerException();
         } else {
            this.name_ = value;
            this.onChanged();
            return this;
         }
      }

      public Option.Builder clearName() {
         this.name_ = Option.getDefaultInstance().getName();
         this.onChanged();
         return this;
      }

      public Option.Builder setNameBytes(ByteString value) {
         if (value == null) {
            throw new NullPointerException();
         } else {
            AbstractMessageLite.checkByteStringIsUtf8(value);
            this.name_ = value;
            this.onChanged();
            return this;
         }
      }

      public boolean hasValue() {
         return this.valueBuilder_ != null || this.value_ != null;
      }

      public Any getValue() {
         if (this.valueBuilder_ == null) {
            return this.value_ == null ? Any.getDefaultInstance() : this.value_;
         } else {
            return (Any)this.valueBuilder_.getMessage();
         }
      }

      public Option.Builder setValue(Any value) {
         if (this.valueBuilder_ == null) {
            if (value == null) {
               throw new NullPointerException();
            }

            this.value_ = value;
            this.onChanged();
         } else {
            this.valueBuilder_.setMessage(value);
         }

         return this;
      }

      public Option.Builder setValue(Any.Builder builderForValue) {
         if (this.valueBuilder_ == null) {
            this.value_ = builderForValue.build();
            this.onChanged();
         } else {
            this.valueBuilder_.setMessage(builderForValue.build());
         }

         return this;
      }

      public Option.Builder mergeValue(Any value) {
         if (this.valueBuilder_ == null) {
            if (this.value_ != null) {
               this.value_ = Any.newBuilder(this.value_).mergeFrom(value).buildPartial();
            } else {
               this.value_ = value;
            }

            this.onChanged();
         } else {
            this.valueBuilder_.mergeFrom(value);
         }

         return this;
      }

      public Option.Builder clearValue() {
         if (this.valueBuilder_ == null) {
            this.value_ = null;
            this.onChanged();
         } else {
            this.value_ = null;
            this.valueBuilder_ = null;
         }

         return this;
      }

      public Any.Builder getValueBuilder() {
         this.onChanged();
         return (Any.Builder)this.getValueFieldBuilder().getBuilder();
      }

      public AnyOrBuilder getValueOrBuilder() {
         if (this.valueBuilder_ != null) {
            return (AnyOrBuilder)this.valueBuilder_.getMessageOrBuilder();
         } else {
            return this.value_ == null ? Any.getDefaultInstance() : this.value_;
         }
      }

      private SingleFieldBuilderV3<Any, Any.Builder, AnyOrBuilder> getValueFieldBuilder() {
         if (this.valueBuilder_ == null) {
            this.valueBuilder_ = new SingleFieldBuilderV3(this.getValue(), this.getParentForChildren(), this.isClean());
            this.value_ = null;
         }

         return this.valueBuilder_;
      }

      public final Option.Builder setUnknownFields(UnknownFieldSet unknownFields) {
         return (Option.Builder)super.setUnknownFields(unknownFields);
      }

      public final Option.Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
         return (Option.Builder)super.mergeUnknownFields(unknownFields);
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
