package org.apache.ibatis.javabase.statictype;

import org.junit.jupiter.api.Test;

/**
 * @author heqin
 */
public class TestDemo {

    @Test
    public void test(){
        MappedStatement.Builder builder1 = new MappedStatement.Builder("testSelect2021");
        builder1 = builder1.resource("/test.xml");

        builder1.logOption();

        MappedStatement.Builder builder2 = new MappedStatement.Builder("testSelect2021");
        builder2 = builder2.resource("/test.xml");

        builder2.changeOption();

        builder1.logOption();

        builder2.logOption();
    }
}
