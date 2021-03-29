package org.apache.ibatis.javabase.finaltest;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @author heqin
 *
 * final 可以修饰类，方法和变量；
 * 被final修饰的类不能被继承，即它不能拥有自的子类；
 * 被final修饰的方法不能被重写；
 * final修饰的变量，无论是类属性、对象属性、形参还是局部变量，都需要进行初始化操作，初始化后不能再重写赋值；
 */
public class FinalTest {

    @Test
    public void testFinal() {
        //final修饰的字段创建以后就不可改变
        final int[] value = {1, 2, 3};
        int[] anotherValue = {4, 5, 6};
        //value = anotherValue;//编译器报错 Cannot assign a value to final variable 'value'

        //虽然value是不可变，也只是value这个引用地址不可变。挡不住Array数组是可变的事实。也就是说Array变量只是stack上的一个引用，数组的本体结构在heap堆。
//        value[2] = 100;
//        System.out.println(Arrays.toString(value));//这时候数组里已经是[1, 2, 100]

        //反射直接改
        Array.set(value, 2, 100);
        System.out.println(Arrays.toString(value));//这时候数组里已经是[1, 2, 100]

    }
}

