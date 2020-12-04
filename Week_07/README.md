# 作业
* [多数据源配置](./write-read-split-datasource/src/main/java/org/geektime/data/source/DataSourceConfiguration.java)
    * [读写分离JdbcTemplate](./write-read-split-datasource/src/main/java/org/geektime/jdbc/JdbcTemplate.java)
    
    
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