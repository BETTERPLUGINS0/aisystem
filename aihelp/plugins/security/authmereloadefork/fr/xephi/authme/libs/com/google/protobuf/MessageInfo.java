package fr.xephi.authme.libs.com.google.protobuf;

@CheckReturnValue
interface MessageInfo {
   ProtoSyntax getSyntax();

   boolean isMessageSetWireFormat();

   MessageLite getDefaultInstance();
}
