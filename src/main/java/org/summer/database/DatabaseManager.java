package org.summer.database;

import cn.hutool.core.thread.NamedThreadFactory;
import com.alibaba.druid.DbType;
import com.alibaba.druid.pool.DruidDataSource;
import com.mysql.cj.jdbc.Driver;
import io.netty.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class DatabaseManager {
    //单独一个写线程（顺序处理避免先后写同步问题）
    private final EventExecutor writeExecutor = new DefaultEventExecutor(new NamedThreadFactory("db-write-", false));
    //并发读线程
    private final EventExecutorGroup readExecutorGroup = new DefaultEventExecutorGroup(4, new NamedThreadFactory("db-read-", false));
    //负责创建session，即获取到数据库的一条连接
    private SqlSessionFactory sessionFactory;

    //执行查询，返回future
    public <V> Future<V> executeQuery(Function<SqlSession, V> queryFunc) {
        return readExecutorGroup.submit(() -> {
            try (var session = sessionFactory.openSession(true)) {
                return queryFunc.apply(session);
            }
        });
    }

    //执行查询，可通过promise获取结果
    @SuppressWarnings("unchecked")
    public <V> void executeQuery(Function<SqlSession, V> queryFunc, Promise<V> promise) {
        readExecutorGroup.submit(() -> {
            try (var session = sessionFactory.openSession(true)) {
                return queryFunc.apply(session);
            }
        }).addListener(f -> {
            if (f.isSuccess()) {
                promise.setSuccess((V)f.getNow());
            } else {
                promise.setFailure(f.cause());
            }
        });
    }

    public Future<?> executeWrite(Consumer<SqlSession> writeFunc) {
        return writeExecutor.submit(() -> {
            try (var session = sessionFactory.openSession(true)) {
                writeFunc.accept(session);
            }
        });
    }

    public void executeWrite(Consumer<SqlSession> writeFunc, Promise<Void> promise) {
         writeExecutor.submit(() -> {
            try (var session = sessionFactory.openSession(true)) {
                writeFunc.accept(session);
            }
        }).addListener(f -> {
            if (f.isSuccess()) {
                promise.setSuccess(null);
            } else {
                promise.setFailure(f.cause());
            }
         });
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

    public void shutdown() {
        readExecutorGroup.shutdownGracefully();
        writeExecutor.shutdownGracefully();
        log.info("db thread shutdown!");
    }

    private static final DatabaseManager INSTANCE = new DatabaseManager();
    public static DatabaseManager getInstance() {
        return INSTANCE;
    }

    public static void main(String[] args) {
        DatabaseConfig config = new DatabaseConfig();
        config.setUrl("jdbc:mysql://localhost:3306/rpg_game?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8");
        config.setUsername("root");
        config.setPassword("root");
        config.setMaxConnCount(10);
        config.setIdleConnCount(5);
        DatabaseManager.getInstance().init(config);
        DatabaseManager.getInstance().shutdown();

    }

}
