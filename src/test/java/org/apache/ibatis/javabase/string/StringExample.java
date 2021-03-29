package org.apache.ibatis.javabase.string;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author heqin
 */
public class StringExample {

    @Test
    public void test_instanceof() {
        Object oString = "123";
        Object oInt = 123;
        //System.out.println(oString instanceof String); // 返回 true
        //System.out.println(oInt instanceof String); // 返回 false

        assertTrue(oString instanceof String);
        assertFalse(oInt instanceof String);
    }

    /**
     * == 和 equals 的区别
     * == 对于基本数据类型来说，是用于比较 “值”是否相等的；而对于引用类型来说，是用于比较引用地址是否相同的。
     * String重写了Object的equal方法
     */
    @Test
    public void test_equal() {
        //"123"在常量池中
        String str1 = "123";
        String str2 = "123";
        String str3 = new String("123");
        assertTrue(str1 == str2);//测试通过
        assertFalse(str1 == str3);//测试通过
        assertTrue(str1.equals(str3));//测试通过
    }

    /**
     * 总结来说，使用 finaltest 修饰的第一个好处是安全；第二个好处是高效，以 JVM 中的字符串常量池来举例，如下两个变量：
     * String s1 = "java";
     * String s2 = "java";
     * 只有字符串是不可变时，我们才能实现字符串常量池，字符串常量池可以为我们缓存字符串，提高程序的运行效率；
     */
    @Test
    public void test_string_final(){
        String s1 = "java";
        String s2 = "java";
        String s3 = "ja" + "va";//代码 "Ja"+"va" 被直接编译成了 "Java"，编译器对字符串优化的结果；
        assertTrue(s1 == s2);
        assertTrue(s1 == s3);
    }

    @Test
    public void test_intern() {
        //"123"在常量池中
        String str1 = "123";
        String str2 = "123";
        String str3 = new String("123");
        String str4 = str1.intern();//直接从常量池中取出"123"
        String str5 = new String("123").intern();
        assertTrue(str1 == str2);
        assertFalse(str1 == str3);
        assertTrue(str1 == str4);
        assertTrue(str1 == str5);
    }
}
