package design.Interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * @Intercepts的值此处使用的是java静态初始化
 * 简化的静态初始化方式 type[] arrayName = {element1,element2,element3...};
 * 示例：
 * String[] strArr = {"张三","李四","王二麻"};
 *
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class}),
        @Signature(type = Executor.class, method = "close", args = {boolean.class})
})
public class DemoPlugin1 implements Interceptor {

    private int logLevel;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println(String.format("%s before intercept",getClass().getName()));
        Object proceed = invocation.proceed();
        System.out.println(String.format("%s after intercept",this.getClass().getName()));
        return proceed;
    }

//    @Override
//    public Object plugin(Object target) {
//        return Plugin.wrap(target, this);
//    }

    @Override
    public void setProperties(Properties properties) {
        logLevel = Integer.valueOf(properties.getProperty("logLevel","9999"));
        //System.out.println(String.format("logLevel=%s",logLevel));
    }
}
