# 作业

**[主:127.0.0.1:6379](conf/redis-6379.conf)** <br/> 
**[从:127.0.0.1:6380](conf/redis-6380.conf)**<br/> 
**[主哨兵: 127.0.0.1:26379](conf/sential0.conf)**<br/> 
**[从哨兵: 127.0.0.1:26380](conf/sential1.conf)**<br/> 

#### 杀死主节点后
```bash
127.0.0.1:6380> info replication
# Replication
role:slave
master_host:127.0.0.1
master_port:6379
master_link_status:down
master_last_io_seconds_ago:-1
master_sync_in_progress:0
slave_repl_offset:27452
master_link_down_since_seconds:10
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:70ef003fed0c8a2a084663edf160b108a185aed7
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:27452
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:27452
127.0.0.1:6380> info replication
# Replication
role:master
connected_slaves:0
master_replid:b80abc93135b4fb3c74bed0e8f5da218654ec943
master_replid2:70ef003fed0c8a2a084663edf160b108a185aed7
master_repl_offset:28007
second_repl_offset:27453
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:28007
```