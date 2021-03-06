package org.apache.ibatis.reflection;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证putIfAbsent与computeIfAbsent的区别
 * <p>
 * computeIfAbsent和putIfAbsent区别是三点：
 * 1、当Key存在的时候，如果Value获取比较昂贵的话，putIfAbsent就白白浪费时间在获取这个昂贵的Value上（这个点特别注意）
 * 2、Key不存在的时候，putIfAbsent返回null，小心空指针，而computeIfAbsent返回计算后的值
 * 3、当Key不存在的时候，putIfAbsent允许put null进去，而computeIfAbsent不能，之后进行containsKey查询是有区别的（当然了，此条针对HashMap，ConcurrentHashMap不允许put null value进去）
 * <a>https://time.geekbang.org/column/article/209494</a>
 *
 * @author heqin
 */
public class PutIfAbsentAndComputeIfAbsentTest {
    @Test
    void testPutIfAbsent() {
        //官方api注释：如果指定键尚未与值关联（或映射为null ），则将其与给定值关联并返回null ，否则返回当前值
        Map<String, String> map = new HashMap<>();
        map.put("one", "xiaoming");
        //指定键one在map中已有映射，则返回已有映射值
        String s = map.putIfAbsent("one", "mingming");
        System.out.println(s);//输出：xiaoming
        //指定键two在map中还没有与值关联，则put到map中并且返回null
        s = map.putIfAbsent("two", "mingming");
        System.out.println(s);//输出：null
        System.out.println(map.size());//输出：2
        System.out.println("注意putIfAbsent允许put null进去！！！");
        s = map.putIfAbsent("three", null);
        System.out.println(s);//输出：null
        System.out.println(map.size());//输出：3 大小变为3说明putIfAbsent允许put null进去
    }

    @Test
    void testComputeIfAbsent() {
        //官方api注释：如果指定的键尚未与值关联（或映射为null ），则尝试使用给定的映射函数计算其值，除非null否则将其输入此映射。
        //返回值：与指定键关联的当前（现有或计算得出的）值；如果计算出的值为null，则返回null
        Map<String, String> map = new HashMap<>();
        map.put("one", "xiaoming");
        String s = map.computeIfAbsent("one", k -> new String("new value..."));
        System.out.println(s);//输出：xiaoming
        s = map.computeIfAbsent("two", k -> new String("new value..."));
        System.out.println(s);//输出：new value...
        System.out.println(map.get("two"));//输出：new value...
        System.out.println(map.size());//输出：2
        System.out.println("注意computeIfAbsent不允许put null进去！！！");
        s = map.computeIfAbsent("three", k -> null);
        System.out.println(s);//输出：null
        System.out.println(map.get("two"));//输出：null
        System.out.println(map.size());//输出：2 大小仍为2，说明computeIfAbsent不允许put null进去
    }
}
