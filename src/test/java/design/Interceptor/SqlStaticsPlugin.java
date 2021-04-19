package design.Interceptor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author heqin
 * 仅供参考
 * https://m.imooc.com/wiki/mybatis-plugins
 */
@Intercepts({@Signature(
        type = StatementHandler.class,
        method = "prepare",
        args = {Connection.class, Integer.class}
)})
public class SqlStaticsPlugin implements Interceptor {

    private Logger logger = LoggerFactory.getLogger(SqlStaticsPlugin.class);

    private List<String> methods = Arrays.asList("SELECT", "INSERT", "UPDATE", "DELETE");

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObj = SystemMetaObject.forObject(statementHandler);
        // 得到SQL类型
        String sqlCommandType = metaObj.getValue("delegate.mappedStatement.sqlCommandType").toString();
        // 如果方法配置中没有SQL类型，则无需计时，直接返回调用
        if (!methods.contains(sqlCommandType)) {
            return invocation.proceed();
        }
        String sql = (String) metaObj.getValue("delegate.boundSql.sql");
        long startTime = System.currentTimeMillis();
        Object res = invocation.proceed();
        long endTime = System.currentTimeMillis();
        long sqlCost = endTime - startTime;
        logger.info("sql: {} - cost: {}ms", sql.replace("\n", ""), sqlCost);
        return res;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        String methodsStr = properties.getProperty("methods");
        if (methodsStr == null || methodsStr.isEmpty()) {
            return;
        }
        String[] parts = methodsStr.split(",");
        methods = Arrays.stream(parts).map(String::toUpperCase).collect(Collectors.toList());
    }
}
