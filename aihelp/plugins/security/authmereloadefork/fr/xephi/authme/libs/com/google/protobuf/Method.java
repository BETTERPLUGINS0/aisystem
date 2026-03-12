package fr.xephi.authme.libs.com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Method extends GeneratedMessageV3 implements MethodOrBuilder {
   private static final long serialVersionUID = 0L;
   public static final int NAME_FIELD_NUMBER = 1;
   private volatile Object name_;
   public static final int REQUEST_TYPE_URL_FIELD_NUMBER = 2;
   private volatile Object requestTypeUrl_;
   public static final int REQUEST_STREAMING_FIELD_NUMBER = 3;
   private boolean requestStreaming_;
   public static final int RESPONSE_TYPE_URL_FIELD_NUMBER = 4;
   private volatile Object responseTypeUrl_;
   public static final int RESPONSE_STREAMING_FIELD_NUMBER = 5;
   private boolean responseStreaming_;
   public static final int OPTIONS_FIELD_NUMBER = 6;
   private List<Option> options_;
   public static final int SYNTAX_FIELD_NUMBER = 7;
   private int syntax_;
   private byte memoizedIsInitialized;
   private static final Method DEFAULT_INSTANCE = new Method();
   private static final Parser<Method> PARSER = new AbstractParser<Method>() {
      public Method parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         Method.Builder builder = Method.newBuilder();

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

   private Method(GeneratedMessageV3.Builder<?> builder) {
      super(builder);
      this.memoizedIsInitialized = -1;
   }

   private Method() {
      this.memoizedIsInitialized = -1;
      this.name_ = "";
      this.requestTypeUrl_ = "";
      this.responseTypeUrl_ = "";
      this.options_ = Collections.emptyList();
      this.syntax_ = 0;
   }

   protected Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
      return new Method();
   }

   public final UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
   }

   public static final Descriptors.Descriptor getDescriptor() {
      return ApiProto.internal_static_google_protobuf_Method_descriptor;
   }

   protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
      return ApiProto.internal_static_google_protobuf_Method_fieldAccessorTable.ensureFieldAccessorsInitialized(Method.class, Method.Builder.class);
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

   public String getRequestTypeUrl() {
      Object ref = this.requestTypeUrl_;
      if (ref instanceof String) {
         return (String)ref;
      } else {
         ByteString bs = (ByteString)ref;
         String s = bs.toStringUtf8();
         this.requestTypeUrl_ = s;
         return s;
      }
   }

   public ByteString getRequestTypeUrlBytes() {
      Object ref = this.requestTypeUrl_;
      if (ref instanceof String) {
         ByteString b = ByteString.copyFromUtf8((String)ref);
         this.requestTypeUrl_ = b;
         return b;
      } else {
         return (ByteString)ref;
      }
   }

   public boolean getRequestStreaming() {
      return this.requestStreaming_;
   }

   public String getResponseTypeUrl() {
      Object ref = this.responseTypeUrl_;
      if (ref instanceof String) {
         return (String)ref;
      } else {
         ByteString bs = (ByteString)ref;
         String s = bs.toStringUtf8();
         this.responseTypeUrl_ = s;
         return s;
      }
   }

   public ByteString getResponseTypeUrlBytes() {
      Object ref = this.responseTypeUrl_;
      if (ref instanceof String) {
         ByteString b = ByteString.copyFromUtf8((String)ref);
         this.responseTypeUrl_ = b;
         return b;
      } else {
         return (ByteString)ref;
      }
   }

   public boolean getResponseStreaming() {
      return this.responseStreaming_;
   }

   public List<Option> getOptionsList() {
      return this.options_;
   }

   public List<? extends OptionOrBuilder> getOptionsOrBuilderList() {
      return this.options_;
   }

   public int getOptionsCount() {
      return this.options_.size();
   }

   public Option getOptions(int index) {
      return (Option)this.options_.get(index);
   }

   public OptionOrBuilder getOptionsOrBuilder(int index) {
      return (OptionOrBuilder)this.options_.get(index);
   }

   public int getSyntaxValue() {
      return this.syntax_;
   }

   public Syntax getSyntax() {
      Syntax result = Syntax.valueOf(this.syntax_);
      return result == null ? Syntax.UNRECOGNIZED : result;
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

      if (!GeneratedMessageV3.isStringEmpty(this.requestTypeUrl_)) {
         GeneratedMessageV3.writeString(output, 2, this.requestTypeUrl_);
      }

      if (this.requestStreaming_) {
         output.writeBool(3, this.requestStreaming_);
      }

      if (!GeneratedMessageV3.isStringEmpty(this.responseTypeUrl_)) {
         GeneratedMessageV3.writeString(output, 4, this.responseTypeUrl_);
      }

      if (this.responseStreaming_) {
         output.writeBool(5, this.responseStreaming_);
      }

      for(int i = 0; i < this.options_.size(); ++i) {
         output.writeMessage(6, (MessageLite)this.options_.get(i));
      }

      if (this.syntax_ != Syntax.SYNTAX_PROTO2.getNumber()) {
         output.writeEnum(7, this.syntax_);
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

         if (!GeneratedMessageV3.isStringEmpty(this.requestTypeUrl_)) {
            size += GeneratedMessageV3.computeStringSize(2, this.requestTypeUrl_);
         }

         if (this.requestStreaming_) {
            size += CodedOutputStream.computeBoolSize(3, this.requestStreaming_);
         }

         if (!GeneratedMessageV3.isStringEmpty(this.responseTypeUrl_)) {
            size += GeneratedMessageV3.computeStringSize(4, this.responseTypeUrl_);
         }

         if (this.responseStreaming_) {
            size += CodedOutputStream.computeBoolSize(5, this.responseStreaming_);
         }

         for(int i = 0; i < this.options_.size(); ++i) {
            size += CodedOutputStream.computeMessageSize(6, (MessageLite)this.options_.get(i));
         }

         if (this.syntax_ != Syntax.SYNTAX_PROTO2.getNumber()) {
            size += CodedOutputStream.computeEnumSize(7, this.syntax_);
         }

         size += this.getUnknownFields().getSerializedSize();
         this.memoizedSize = size;
         return size;
      }
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof Method)) {
         return super.equals(obj);
      } else {
         Method other = (Method)obj;
         if (!this.getName().equals(other.getName())) {
            return false;
         } else if (!this.getRequestTypeUrl().equals(other.getRequestTypeUrl())) {
            return false;
         } else if (this.getRequestStreaming() != other.getRequestStreaming()) {
            return false;
         } else if (!this.getResponseTypeUrl().equals(other.getResponseTypeUrl())) {
            return false;
         } else if (this.getResponseStreaming() != other.getResponseStreaming()) {
            return false;
         } else if (!this.getOptionsList().equals(other.getOptionsList())) {
            return false;
         } else if (this.syntax_ != other.syntax_) {
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
         hash = 37 * hash + 2;
         hash = 53 * hash + this.getRequestTypeUrl().hashCode();
         hash = 37 * hash + 3;
         hash = 53 * hash + Internal.hashBoolean(this.getRequestStreaming());
         hash = 37 * hash + 4;
         hash = 53 * hash + this.getResponseTypeUrl().hashCode();
         hash = 37 * hash + 5;
         hash = 53 * hash + Internal.hashBoolean(this.getResponseStreaming());
         if (this.getOptionsCount() > 0) {
            hash = 37 * hash + 6;
            hash = 53 * hash + this.getOptionsList().hashCode();
         }

         hash = 37 * hash + 7;
         hash = 53 * hash + this.syntax_;
         hash = 29 * hash + this.getUnknownFields().hashCode();
         this.memoizedHashCode = hash;
         return hash;
      }
   }

   public static Method parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
      return (Method)PARSER.parseFrom(data);
   }

   public static Method parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Method)PARSER.parseFrom(data, extensionRegistry);
   }

   public static Method parseFrom(ByteString data) throws InvalidProtocolBufferException {
      return (Method)PARSER.parseFrom(data);
   }

   public static Method parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Method)PARSER.parseFrom(data, extensionRegistry);
   }

   public static Method parseFrom(byte[] data) throws InvalidProtocolBufferException {
      return (Method)PARSER.parseFrom(data);
   }

   public static Method parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Method)PARSER.parseFrom(data, extensionRegistry);
   }

   public static Method parseFrom(InputStream input) throws IOException {
      return (Method)GeneratedMessageV3.parseWithIOException(PARSER, input);
   }

   public static Method parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (Method)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
   }

   public static Method parseDelimitedFrom(InputStream input) throws IOException {
      return (Method)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
   }

   public static Method parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (Method)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
   }

   public static Method parseFrom(CodedInputStream input) throws IOException {
      return (Method)GeneratedMessageV3.parseWithIOException(PARSER, input);
   }

   public static Method parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (Method)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
   }

   public Method.Builder newBuilderForType() {
      return newBuilder();
   }

   public static Method.Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
   }

   public static Method.Builder newBuilder(Method prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
   }

   public Method.Builder toBuilder() {
      return this == DEFAULT_INSTANCE ? new Method.Builder() : (new Method.Builder()).mergeFrom(this);
   }

   protected Method.Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
      Method.Builder builder = new Method.Builder(parent);
      return builder;
   }

   public static Method getDefaultInstance() {
      return DEFAULT_INSTANCE;
   }

   public static Parser<Method> parser() {
      return PARSER;
   }

   public Parser<Method> getParserForType() {
      return PARSER;
   }

   public Method getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
   }

   // $FF: synthetic method
   Method(GeneratedMessageV3.Builder x0, Object x1) {
      this(x0);
   }

   public static final class Builder extends GeneratedMessageV3.Builder<Method.Builder> implements MethodOrBuilder {
      private int bitField0_;
      private Object name_;
      private Object requestTypeUrl_;
      private boolean requestStreaming_;
      private Object responseTypeUrl_;
      private boolean responseStreaming_;
      private List<Option> options_;
      private RepeatedFieldBuilderV3<Option, Option.Builder, OptionOrBuilder> optionsBuilder_;
      private int syntax_;

      public static final Descriptors.Descriptor getDescriptor() {
         return ApiProto.internal_static_google_protobuf_Method_descriptor;
      }

      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
         return ApiProto.internal_static_google_protobuf_Method_fieldAccessorTable.ensureFieldAccessorsInitialized(Method.class, Method.Builder.class);
      }

      private Builder() {
         this.name_ = "";
         this.requestTypeUrl_ = "";
         this.responseTypeUrl_ = "";
         this.options_ = Collections.emptyList();
         this.syntax_ = 0;
      }

      private Builder(GeneratedMessageV3.BuilderParent parent) {
         super(parent);
         this.name_ = "";
         this.requestTypeUrl_ = "";
         this.responseTypeUrl_ = "";
         this.options_ = Collections.emptyList();
         this.syntax_ = 0;
      }

      public Method.Builder clear() {
         super.clear();
         this.name_ = "";
         this.requestTypeUrl_ = "";
         this.requestStreaming_ = false;
         this.responseTypeUrl_ = "";
         this.responseStreaming_ = false;
         if (this.optionsBuilder_ == null) {
            this.options_ = Collections.emptyList();
         } else {
            this.options_ = null;
            this.optionsBuilder_.clear();
         }

         this.bitField0_ &= -2;
         this.syntax_ = 0;
         return this;
      }

      public Descriptors.Descriptor getDescriptorForType() {
         return ApiProto.internal_static_google_protobuf_Method_descriptor;
      }

      public Method getDefaultInstanceForType() {
         return Method.getDefaultInstance();
      }

      public Method build() {
         Method result = this.buildPartial();
         if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
         } else {
            return result;
         }
      }

      public Method buildPartial() {
         Method result = new Method(this);
         int from_bitField0_ = this.bitField0_;
         result.name_ = this.name_;
         result.requestTypeUrl_ = this.requestTypeUrl_;
         result.requestStreaming_ = this.requestStreaming_;
         result.responseTypeUrl_ = this.responseTypeUrl_;
         result.responseStreaming_ = this.responseStreaming_;
         if (this.optionsBuilder_ == null) {
            if ((this.bitField0_ & 1) != 0) {
               this.options_ = Collections.unmodifiableList(this.options_);
               this.bitField0_ &= -2;
            }

            result.options_ = this.options_;
         } else {
            result.options_ = this.optionsBuilder_.build();
         }

         result.syntax_ = this.syntax_;
         this.onBuilt();
         return result;
      }

      public Method.Builder clone() {
         return (Method.Builder)super.clone();
      }

      public Method.Builder setField(Descriptors.FieldDescriptor field, Object value) {
         return (Method.Builder)super.setField(field, value);
      }

      public Method.Builder clearField(Descriptors.FieldDescriptor field) {
         return (Method.Builder)super.clearField(field);
      }

      public Method.Builder clearOneof(Descriptors.OneofDescriptor oneof) {
         return (Method.Builder)super.clearOneof(oneof);
      }

      public Method.Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
         return (Method.Builder)super.setRepeatedField(field, index, value);
      }

      public Method.Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
         return (Method.Builder)super.addRepeatedField(field, value);
      }

      public Method.Builder mergeFrom(Message other) {
         if (other instanceof Method) {
            return this.mergeFrom((Method)other);
         } else {
            super.mergeFrom(other);
            return this;
         }
      }

      public Method.Builder mergeFrom(Method other) {
         if (other == Method.getDefaultInstance()) {
            return this;
         } else {
            if (!other.getName().isEmpty()) {
               this.name_ = other.name_;
               this.onChanged();
            }

            if (!other.getRequestTypeUrl().isEmpty()) {
               this.requestTypeUrl_ = other.requestTypeUrl_;
               this.onChanged();
            }

            if (other.getRequestStreaming()) {
               this.setRequestStreaming(other.getRequestStreaming());
            }

            if (!other.getResponseTypeUrl().isEmpty()) {
               this.responseTypeUrl_ = other.responseTypeUrl_;
               this.onChanged();
            }

            if (other.getResponseStreaming()) {
               this.setResponseStreaming(other.getResponseStreaming());
            }

            if (this.optionsBuilder_ == null) {
               if (!other.options_.isEmpty()) {
                  if (this.options_.isEmpty()) {
                     this.options_ = other.options_;
                     this.bitField0_ &= -2;
                  } else {
                     this.ensureOptionsIsMutable();
                     this.options_.addAll(other.options_);
                  }

                  this.onChanged();
               }
            } else if (!other.options_.isEmpty()) {
               if (this.optionsBuilder_.isEmpty()) {
                  this.optionsBuilder_.dispose();
                  this.optionsBuilder_ = null;
                  this.options_ = other.options_;
                  this.bitField0_ &= -2;
                  this.optionsBuilder_ = GeneratedMessageV3.alwaysUseFieldBuilders ? this.getOptionsFieldBuilder() : null;
               } else {
                  this.optionsBuilder_.addAllMessages(other.options_);
               }
            }

            if (other.syntax_ != 0) {
               this.setSyntaxValue(other.getSyntaxValue());
            }

            this.mergeUnknownFields(other.getUnknownFields());
            this.onChanged();
            return this;
         }
      }

      public final boolean isInitialized() {
         return true;
      }

      public Method.Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
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
                     this.requestTypeUrl_ = input.readStringRequireUtf8();
                     break;
                  case 24:
                     this.requestStreaming_ = input.readBool();
                     break;
                  case 34:
                     this.responseTypeUrl_ = input.readStringRequireUtf8();
                     break;
                  case 40:
                     this.responseStreaming_ = input.readBool();
                     break;
                  case 50:
                     Option m = (Option)input.readMessage(Option.parser(), extensionRegistry);
                     if (this.optionsBuilder_ == null) {
                        this.ensureOptionsIsMutable();
                        this.options_.add(m);
                     } else {
                        this.optionsBuilder_.addMessage(m);
                     }
                     break;
                  case 56:
                     this.syntax_ = input.readEnum();
                     break;
                  default:
                     if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                        done = true;
                     }
                  }
               }
            } catch (InvalidProtocolBufferException var9) {
               throw var9.unwrapIOException();
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

      public Method.Builder setName(String value) {
         if (value == null) {
            throw new NullPointerException();
         } else {
            this.name_ = value;
            this.onChanged();
            return this;
         }
      }

      public Method.Builder clearName() {
         this.name_ = Method.getDefaultInstance().getName();
         this.onChanged();
         return this;
      }

      public Method.Builder setNameBytes(ByteString value) {
         if (value == null) {
            throw new NullPointerException();
         } else {
            AbstractMessageLite.checkByteStringIsUtf8(value);
            this.name_ = value;
            this.onChanged();
            return this;
         }
      }

      public String getRequestTypeUrl() {
         Object ref = this.requestTypeUrl_;
         if (!(ref instanceof String)) {
            ByteString bs = (ByteString)ref;
            String s = bs.toStringUtf8();
            this.requestTypeUrl_ = s;
            return s;
         } else {
            return (String)ref;
         }
      }

      public ByteString getRequestTypeUrlBytes() {
         Object ref = this.requestTypeUrl_;
         if (ref instanceof String) {
            ByteString b = ByteString.copyFromUtf8((String)ref);
            this.requestTypeUrl_ = b;
            return b;
         } else {
            return (ByteString)ref;
         }
      }

      public Method.Builder setRequestTypeUrl(String value) {
         if (value == null) {
            throw new NullPointerException();
         } else {
            this.requestTypeUrl_ = value;
            this.onChanged();
            return this;
         }
      }

      public Method.Builder clearRequestTypeUrl() {
         this.requestTypeUrl_ = Method.getDefaultInstance().getRequestTypeUrl();
         this.onChanged();
         return this;
      }

      public Method.Builder setRequestTypeUrlBytes(ByteString value) {
         if (value == null) {
            throw new NullPointerException();
         } else {
            AbstractMessageLite.checkByteStringIsUtf8(value);
            this.requestTypeUrl_ = value;
            this.onChanged();
            return this;
         }
      }

      public boolean getRequestStreaming() {
         return this.requestStreaming_;
      }

      public Method.Builder setRequestStreaming(boolean value) {
         this.requestStreaming_ = value;
         this.onChanged();
         return this;
      }

      public Method.Builder clearRequestStreaming() {
         this.requestStreaming_ = false;
         this.onChanged();
         return this;
      }

      public String getResponseTypeUrl() {
         Object ref = this.responseTypeUrl_;
         if (!(ref instanceof String)) {
            ByteString bs = (ByteString)ref;
            String s = bs.toStringUtf8();
            this.responseTypeUrl_ = s;
            return s;
         } else {
            return (String)ref;
         }
      }

      public ByteString getResponseTypeUrlBytes() {
         Object ref = this.responseTypeUrl_;
         if (ref instanceof String) {
            ByteString b = ByteString.copyFromUtf8((String)ref);
            this.responseTypeUrl_ = b;
            return b;
         } else {
            return (ByteString)ref;
         }
      }

      public Method.Builder setResponseTypeUrl(String value) {
         if (value == null) {
            throw new NullPointerException();
         } else {
            this.responseTypeUrl_ = value;
            this.onChanged();
            return this;
         }
      }

      public Method.Builder clearResponseTypeUrl() {
         this.responseTypeUrl_ = Method.getDefaultInstance().getResponseTypeUrl();
         this.onChanged();
         return this;
      }

      public Method.Builder setResponseTypeUrlBytes(ByteString value) {
         if (value == null) {
            throw new NullPointerException();
         } else {
            AbstractMessageLite.checkByteStringIsUtf8(value);
            this.responseTypeUrl_ = value;
            this.onChanged();
            return this;
         }
      }

      public boolean getResponseStreaming() {
         return this.responseStreaming_;
      }

      public Method.Builder setResponseStreaming(boolean value) {
         this.responseStreaming_ = value;
         this.onChanged();
         return this;
      }

      public Method.Builder clearResponseStreaming() {
         this.responseStreaming_ = false;
         this.onChanged();
         return this;
      }

      private void ensureOptionsIsMutable() {
         if ((this.bitField0_ & 1) == 0) {
            this.options_ = new ArrayList(this.options_);
            this.bitField0_ |= 1;
         }

      }

      public List<Option> getOptionsList() {
         return this.optionsBuilder_ == null ? Collections.unmodifiableList(this.options_) : this.optionsBuilder_.getMessageList();
      }

      public int getOptionsCount() {
         return this.optionsBuilder_ == null ? this.options_.size() : this.optionsBuilder_.getCount();
      }

      public Option getOptions(int index) {
         return this.optionsBuilder_ == null ? (Option)this.options_.get(index) : (Option)this.optionsBuilder_.getMessage(index);
      }

      public Method.Builder setOptions(int index, Option value) {
         if (this.optionsBuilder_ == null) {
            if (value == null) {
               throw new NullPointerException();
            }

            this.ensureOptionsIsMutable();
            this.options_.set(index, value);
            this.onChanged();
         } else {
            this.optionsBuilder_.setMessage(index, value);
         }

         return this;
      }

      public Method.Builder setOptions(int index, Option.Builder builderForValue) {
         if (this.optionsBuilder_ == null) {
            this.ensureOptionsIsMutable();
            this.options_.set(index, builderForValue.build());
            this.onChanged();
         } else {
            this.optionsBuilder_.setMessage(index, builderForValue.build());
         }

         return this;
      }

      public Method.Builder addOptions(Option value) {
         if (this.optionsBuilder_ == null) {
            if (value == null) {
               throw new NullPointerException();
            }

            this.ensureOptionsIsMutable();
            this.options_.add(value);
            this.onChanged();
         } else {
            this.optionsBuilder_.addMessage(value);
         }

         return this;
      }

      public Method.Builder addOptions(int index, Option value) {
         if (this.optionsBuilder_ == null) {
            if (value == null) {
               throw new NullPointerException();
            }

            this.ensureOptionsIsMutable();
            this.options_.add(index, value);
            this.onChanged();
         } else {
            this.optionsBuilder_.addMessage(index, value);
         }

         return this;
      }

      public Method.Builder addOptions(Option.Builder builderForValue) {
         if (this.optionsBuilder_ == null) {
            this.ensureOptionsIsMutable();
            this.options_.add(builderForValue.build());
            this.onChanged();
         } else {
            this.optionsBuilder_.addMessage(builderForValue.build());
         }

         return this;
      }

      public Method.Builder addOptions(int index, Option.Builder builderForValue) {
         if (this.optionsBuilder_ == null) {
            this.ensureOptionsIsMutable();
            this.options_.add(index, builderForValue.build());
            this.onChanged();
         } else {
            this.optionsBuilder_.addMessage(index, builderForValue.build());
         }

         return this;
      }

      public Method.Builder addAllOptions(Iterable<? extends Option> values) {
         if (this.optionsBuilder_ == null) {
            this.ensureOptionsIsMutable();
            AbstractMessageLite.Builder.addAll(values, this.options_);
            this.onChanged();
         } else {
            this.optionsBuilder_.addAllMessages(values);
         }

         return this;
      }

      public Method.Builder clearOptions() {
         if (this.optionsBuilder_ == null) {
            this.options_ = Collections.emptyList();
            this.bitField0_ &= -2;
            this.onChanged();
         } else {
            this.optionsBuilder_.clear();
         }

         return this;
      }

      public Method.Builder removeOptions(int index) {
         if (this.optionsBuilder_ == null) {
            this.ensureOptionsIsMutable();
            this.options_.remove(index);
            this.onChanged();
         } else {
            this.optionsBuilder_.remove(index);
         }

         return this;
      }

      public Option.Builder getOptionsBuilder(int index) {
         return (Option.Builder)this.getOptionsFieldBuilder().getBuilder(index);
      }

      public OptionOrBuilder getOptionsOrBuilder(int index) {
         return this.optionsBuilder_ == null ? (OptionOrBuilder)this.options_.get(index) : (OptionOrBuilder)this.optionsBuilder_.getMessageOrBuilder(index);
      }

      public List<? extends OptionOrBuilder> getOptionsOrBuilderList() {
         return this.optionsBuilder_ != null ? this.optionsBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.options_);
      }

      public Option.Builder addOptionsBuilder() {
         return (Option.Builder)this.getOptionsFieldBuilder().addBuilder(Option.getDefaultInstance());
      }

      public Option.Builder addOptionsBuilder(int index) {
         return (Option.Builder)this.getOptionsFieldBuilder().addBuilder(index, Option.getDefaultInstance());
      }

      public List<Option.Builder> getOptionsBuilderList() {
         return this.getOptionsFieldBuilder().getBuilderList();
      }

      private RepeatedFieldBuilderV3<Option, Option.Builder, OptionOrBuilder> getOptionsFieldBuilder() {
         if (this.optionsBuilder_ == null) {
            this.optionsBuilder_ = new RepeatedFieldBuilderV3(this.options_, (this.bitField0_ & 1) != 0, this.getParentForChildren(), this.isClean());
            this.options_ = null;
         }

         return this.optionsBuilder_;
      }

      public int getSyntaxValue() {
         return this.syntax_;
      }

      public Method.Builder setSyntaxValue(int value) {
         this.syntax_ = value;
         this.onChanged();
         return this;
      }

      public Syntax getSyntax() {
         Syntax result = Syntax.valueOf(this.syntax_);
         return result == null ? Syntax.UNRECOGNIZED : result;
      }

      public Method.Builder setSyntax(Syntax value) {
         if (value == null) {
            throw new NullPointerException();
         } else {
            this.syntax_ = value.getNumber();
            this.onChanged();
            return this;
         }
      }

      public Method.Builder clearSyntax() {
         this.syntax_ = 0;
         this.onChanged();
         return this;
      }

      public final Method.Builder setUnknownFields(UnknownFieldSet unknownFields) {
         return (Method.Builder)super.setUnknownFields(unknownFields);
      }

      public final Method.Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
         return (Method.Builder)super.mergeUnknownFields(unknownFields);
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
