package org.apache.ibatis.javabase.clone;
import org.apache.ibatis.javabase.clone.DeepClone.People;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author heqin
 *
 */
public class ArraysCopyOfExample {

    /**
     * 测试 Arrays.copyOf() 是一个浅克隆
     */
    @Test
    public void testArraysCopyOfIsShallow(){
        People[] o1 = {new People(1, "Java",null)};
        People[] o2 = Arrays.copyOf(o1, o1.length);
        // 修改原型对象的第一个元素的值
        o1[0].setName("Jdk");
        //System.out.println("o1:" + o1[0].getName());//Jdk
        //System.out.println("o2:" + o2[0].getName());//Jdk

        Assert.assertEquals("Jdk",o1[0].getName());
        Assert.assertEquals("Jdk",o2[0].getName());
        //结果o1[0].getName()与o2[0].getName()的值都是Jdk，也就是修改原型对象的第一个元素后，克隆对象的第一个元素也被修改；

        //System.out.println(o1 == o2);//false
        Assert.assertFalse("o1不等于o2预测不对！",o1 == o2);
        //System.out.println(o1[0] == o2[0]);//true
        Assert.assertTrue("o1[0]等于o2[0]预测不对",o1[0] == o2[0]);
    }
}
