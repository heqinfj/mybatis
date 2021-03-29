package org.apache.ibatis.javabase.string;

/**
 * @author heqin
 * 为什么在Java中将String类声明为final？
 *
 * https://www.zhihu.com/question/31345592
 * https://stackoverflow.com/questions/2068804/why-is-the-string-class-declared-final-in-java
 *
 * final修饰的好处
 * https://kaiwu.lagou.com/course/courseInfo.htm?courseId=59#/detail/pc?id=1761
 * 第一个好处是安全；
 * （Java 语言之父 James Gosling 的回答是，他会更倾向于使用 final，因为它能够缓存结果，当你在传参时不需要考虑谁会修改它的值；
 * 如果是可变类的话，则有可能需要重新拷贝出来一个新值进行传参，这样在性能上就会有一定的损失。）
 *
 * 第二个好处是高效，只有字符串是不可变时，我们才能实现字符串常量池，字符串常量池可以为我们缓存字符串，提高程序的运行效率；
 *
 *
 */
public class TestStringIsFinal {

    //不可变的String
    public static String appendString(String str){
        str += "bbb";
        return str;
    }

    //可变的StringBuilder
    public static StringBuilder appendStringBuilder(StringBuilder stringBuilder){
       return stringBuilder.append("bbb");
    }

    public static void main(String[] args) {
        String str = new String("aaa");
        appendString(str);
        System.out.println("String aaa => " + str);//String aaa => aaa

        StringBuilder stringBuilder = new StringBuilder("aaa");
        appendStringBuilder(stringBuilder);
        System.out.println("StringBuilder aaa => " + stringBuilder);//StringBuilder aaa => aaabbb
    }
}
