package org.summer.database;

import com.alibaba.druid.DbType;
import com.alibaba.druid.pool.DruidDataSource;
import com.mysql.cj.jdbc.Driver;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;

@Slf4j
public class DatabaseManager {
    //单独一个写线程（顺序处理避免先后写同步问题）
    private final EventExecutor writeExecutor = new DefaultEventExecutor();
    //并发读线程
    private final EventExecutorGroup readExecutorGroup = new DefaultEventExecutorGroup(4);
    //负责创建session，即获取到数据库的一条连接
    private SqlSessionFactory sessionFactory;

    public SqlSessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SqlSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void init(DatabaseConfig config) {
        DataSource ds = initDataSource(config);
        TransactionFactory tf = new JdbcTransactionFactory();
        Environment env = new Environment("default", tf, ds);
        Configuration conf = new Configuration(env);
        conf.addMappers("org.summer.database.mapper");
        sessionFactory = new SqlSessionFactoryBuilder().build(conf);
        log.info("mybatis sqlSessionFactory built!");
    }

    private DataSource initDataSource(DatabaseConfig config) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDbType(DbType.mysql);
        dataSource.setEnable(true);
        dataSource.setKeepAlive(true);
        dataSource.setMaxActive(config.getMaxConnCount());
        dataSource.setMinIdle(config.getIdleConnCount());
        dataSource.setDefaultAutoCommit(true);
        dataSource.setDriverClassName(Driver.class.getCanonicalName());
        dataSource.setUrl(config.getUrl());
        dataSource.setUsername(config.getUsername());
        dataSource.setPassword(config.getPassword());
        return dataSource;
    }

}
