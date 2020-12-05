package org.geektime.support;

import org.apache.shardingsphere.replicaquery.spi.ReplicaLoadBalanceAlgorithm;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * shardingsphere 权重负载均衡实现
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/5
 * @since 1.8
 **/
@SuppressWarnings("unchecked")
public final class WeightReplicaLoadBalanceAlgorithm implements ReplicaLoadBalanceAlgorithm {
    private Properties props = new Properties();

    private final Random random = new Random();

    private Map<String, Integer> weightSum = new HashMap<>();

    private synchronized void init(String name, List<String> replicaDataSourceNames) {
        int sum = 0;
        try {
            Map<String, Object> replicas = (Map<String, Object>) props.get(name);
            if (!weightSum.containsKey(name)) {
                for (String dataSource : replicaDataSourceNames) {
                    sum += this.getWeight(name, dataSource);
                }
            }
            weightSum.put(name, sum);
        } catch (Exception e) {
            throw new RuntimeException("无法获取数据源[" + name + "]的权重配置", e);
        }
    }

    public int getWeight(String name, String replicaDataSourceName) {
        Map<String, Object> replicas = (Map<String, Object>) props.get(name);
        return Integer.valueOf(String.valueOf(replicas.getOrDefault(replicaDataSourceName, "0")));
    }

    @Override
    public String getDataSource(String name, String primaryDataSourceName, List<String> replicaDataSourceNames) {
        if (!weightSum.containsKey(name)) {
            this.init(name, replicaDataSourceNames);
        }
        final int sum = weightSum.get(name);
        int randomInt = random.nextInt(sum);
        for (String dataSource : replicaDataSourceNames) {
            if (this.getWeight(name, dataSource) > randomInt) {
                return dataSource;
            } else {
                randomInt -= this.getWeight(name, dataSource);
            }
        }

        return replicaDataSourceNames.get(replicaDataSourceNames.size() - 1);
    }

    @Override
    public String getType() {
        return "WEIGHT";
    }

    @Override
    public Properties getProps() {
        return props;
    }

    @Override
    public void setProps(Properties props) {
        this.props = props;
    }
}
