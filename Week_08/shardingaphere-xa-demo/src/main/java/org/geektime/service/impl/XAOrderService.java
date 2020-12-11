package org.geektime.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.session.SqlSession;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.geektime.mapper.OrderMapper;
import org.geektime.pojo.Order;
import org.geektime.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Random;

/**
 * XA demo
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/11
 * @since 1.8
 **/
@Primary
@Service
public class XAOrderService extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    Random random = new Random();

    private final Logger logger = LoggerFactory.getLogger(XAOrderService.class);

    @Override
    @Transactional
    @ShardingTransactionType(TransactionType.XA)
    public boolean saveBatch(Collection<Order> entityList, int batchSize) {
        String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int i = 0;
            for (Order anEntityList : entityList) {
                batchSqlSession.insert(sqlStatement, anEntityList);
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
            if (random.nextInt(10) > 6) {
                logger.error("随机错误发生");
                throw new RuntimeException();
            }
        }
        return true;
    }
}
