package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

abstract class BaseMpscLinkedArrayQueueConsumerFields<E> extends BaseMpscLinkedArrayQueuePad2<E> {
   protected long consumerMask;
   protected E[] consumerBuffer;
   protected long consumerIndex;
}
