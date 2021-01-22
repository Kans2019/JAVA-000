package io.kimmking.kmq.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class Kmq {
    private final static Logger logger = LoggerFactory.getLogger(Kmq.class);

    private final AtomicLong lsf;

    private final Map<String, Long> indexMap = new ConcurrentHashMap<>();

    private final Map<String, Long> nonCommitIndexMap = new ConcurrentHashMap<>();

    public Kmq(String topic, int capacity) {
        this.topic = topic;
        this.capacity = capacity;
        this.queue = new KmqMessage[this.capacity];
        this.lsf = new AtomicLong(0);
    }

    private final String topic;

    private final int capacity;

    private final KmqMessage[] queue;

    public boolean send(KmqMessage message) {
        final long index = lsf.getAndIncrement() % this.capacity;
        this.queue[Math.toIntExact(index)] = message;
        synchronized (this) {
            this.notify();
        }
        return true;
    }

    public KmqMessage poll() {
        return poll(0);
    }

    public KmqMessage poll(long timeout) {
        String consumerId = getConsumerId();
        long index = nonCommitIndexMap.getOrDefault(consumerId, indexMap.getOrDefault(consumerId, 0L));
        while (index >= lsf.intValue()) {
//            logger.debug("{} 超过了当前消息位移, 进入阻塞状态...", index);
            try {
                synchronized (this) {
                    this.wait(timeout);
                }
            } catch (InterruptedException e) {
                logger.error("", e);
                return null;
            }
        }
        index = index % this.capacity;
        nonCommitIndexMap.put(consumerId, index + 1);
        return this.queue[Math.toIntExact(index)];
    }

    private String getConsumerId() {
        return String.valueOf(Thread.currentThread().getId());
    }

    public boolean confirm() {
        String consumerId = getConsumerId();
        long pIndex = nonCommitIndexMap.getOrDefault(consumerId, 0L), index = indexMap.getOrDefault(consumerId, 0L);
        if (pIndex < index) {
            throw new IllegalArgumentException(String.format("不合法的位移 %d, 当前位移 %d", pIndex, index));
        }
        indexMap.put(consumerId, pIndex);
        nonCommitIndexMap.remove(consumerId);
        return true;
    }

    /**
     * 修改当前用户的位移
     * @param index
     * @return
     */
    public boolean submit(long index) {
        if (index < 0 || index >= lsf.get()) {
            throw new IndexOutOfBoundsException(String.format("不合法的位移 %d, 当前最大位移 %d", index, lsf.get()));
        }
        String consumerId = this.getConsumerId();
        indexMap.put(consumerId, index);
        nonCommitIndexMap.remove(consumerId);
        return true;
    }
}
