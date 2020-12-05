# 作业
## 读写分离
### 自行实现版本(profile:jdbc)
* [配置文件](./write-read-split-datasource/src/main/resources/application-jdbc.yml)
* [多数据源配置](./write-read-split-datasource/src/main/java/org/geektime/data/source/DataSourceConfiguration.java)
* [读写分离JdbcTemplate](./write-read-split-datasource/src/main/java/org/geektime/jdbc/JdbcTemplate.java)
* [使用AbstractRoutingDataSource实现读写分离](./write-read-split-datasource/src/main/java/org/geektime/support/DynamicDataSource.java)
* [读写分离Aop](./write-read-split-datasource/src/main/java/org/geektime/support/DynamicDataSourceAop.java)
* [负载均衡](./write-read-split-datasource/src/main/java/org/geektime/support/strategy)

### shardingSphere-jdbc版本(profile:shardingsphere-jdbc)
* [配置文件](./write-read-split-datasource/src/main/resources/application-shardingsphere-jdbc.yml)

### shardingSphere-proxy 版本(profile:shardingsphere-proxy)
* [配置文件](./write-read-split-datasource/src/main/resources/application-shardingsphere-proxy.yml)

## 批量插入100W数据
```markdown
* 一次插入100条 hundred-by-step-insert[main] 运行时间 18s.
* 一次性全部插入 once-insert[main] 运行时间 7s.
* 一次插入一条 step-by-step-insert[main] 运行时间 581s.
* 一次插入十条 ten-by-step-insert[main] 运行时间 107s.
* 一次插入一万条 ten-thousand-by-step-insert[main] 运行时间 7s.
* 一次插入一千条 thousand-by-step-insert[main] 运行时间 8s.
```
**综上所述, 尽可能做批量插入, 减少IO交互可以极大提高效率。**

## ShardingSphere-Proxy 配置
* server.yaml
```yaml
authentication:
  users:
    root:
      password: root
    learn:
      password: learn
      authorizedSchemas: learn
```

* config-learn.yaml
```yaml
schemaName: learn

dataSourceCommon:
  username: learn
  password: learn
  connectionTimeoutMilliseconds: 30000
  idleTimeoutMilliseconds: 60000
  maxLifetimeMilliseconds: 1800000
  maxPoolSize: 50
  minPoolSize: 1
  maintenanceIntervalMilliseconds: 30000

dataSources:
  master:
    url: jdbc:mysql://localhost:3306/learn?characterEncoding=utf-8
  slave01:
    url: jdbc:mysql://localhost:3308/learn?characterEncoding=utf-8
  slave02:
    url: jdbc:mysql://localhost:3316/learn?characterEncoding=utf-8

rules:
- !REPLICA_QUERY
  dataSources:
    learn:
      name: learn
      loadBalancerName: round-robin
      primaryDataSourceName: master
      replicaDataSourceNames:
        - slave01
        - slave02
  loadBalancers:
    random:
      type: RANDOM
    round-robin:
      type: ROUND_ROBIN
```