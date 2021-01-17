# 作业
* [生产者](./mq-demo/src/main/java/org/geektime/mq/controller/MessageController.java)
* [消费者](./mq-demo/src/main/java/org/geektime/mq/service/MessageHandler.java)

### 执行性能测试
> bin/kafka-consumer-perf-test.sh --topic test32 --fetch-size 1048576 --messages 100000 --threads 8 --broker-list localhost:9090,localhost:9091,localhost:9092
  WARNING: option [threads] and [num-fetch-threads] have been deprecated and will be ignored by the test
  start.time, end.time, data.consumed.in.MB, MB.sec, data.consumed.in.nMsg, nMsg.sec, rebalance.time.ms, fetch.time.ms, fetch.MB.sec, fetch.nMsg.sec
  WARNING: Exiting before consuming the expected number of messages: timeout (10000 ms) exceeded. You can use the --timeout option to increase the timeout.
  2021-01-17 11:10:23:640, 2021-01-17 11:10:34:473, 78.5036, 7.2467, 82317, 7598.7261, 1610853024029, -1610853013196, -0.0000, -0.0001
> bin/kafka-producer-perf-test.sh --topic test32 --num-records 100000 --record-size 1000 --throughput 2000 --producer-props bootstrap.servers=localhost:9090,localhost:9091,localhost:9092
 [2021-01-17 11:06:48,245] WARN [Producer clientId=producer-1] Error while fetching metadata with correlation id 1 : {test32=LEADER_NOT_AVAILABLE} (org.apache.kafka.clients.NetworkClient)
 10000 records sent, 2000.0 records/sec (1.91 MB/sec), 2.6 ms avg latency, 399.0 ms max latency.
 10006 records sent, 2000.8 records/sec (1.91 MB/sec), 0.7 ms avg latency, 5.0 ms max latency.
 10002 records sent, 2000.0 records/sec (1.91 MB/sec), 0.7 ms avg latency, 6.0 ms max latency.
 10004 records sent, 2000.4 records/sec (1.91 MB/sec), 0.7 ms avg latency, 19.0 ms max latency.
 9998 records sent, 1999.6 records/sec (1.91 MB/sec), 0.7 ms avg latency, 24.0 ms max latency.
 10004 records sent, 2000.4 records/sec (1.91 MB/sec), 0.8 ms avg latency, 1.0 ms max latency.
 10002 records sent, 2000.4 records/sec (1.91 MB/sec), 0.8 ms avg latency, 5.0 ms max latency.
 10004 records sent, 2000.4 records/sec (1.91 MB/sec), 0.8 ms avg latency, 3.0 ms max latency.