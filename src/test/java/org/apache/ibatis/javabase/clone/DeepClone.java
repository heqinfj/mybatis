package org.apache.ibatis.javabase.clone;

import com.google.gson.Gson;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author heqin
 * 深克隆的实现方式有很多种，大体可以分为以下几类:
 * . 所有对象都实现克隆方法；
 * . 通过构造方法实现深克隆；
 * . 使用 JDK 自带的字节流实现深克隆；
 * . 使用第三方工具实现深克隆，比如 Apache Commons Lang；
 * . 使用 JSON 工具类实现深克隆，比如 Gson、FastJSON 等。
 *
 * <a href="https://kaiwu.lagou.com/course/courseInfo.htm?courseId=59#/detail/pc?id=1767">深克隆和浅克隆有什么区别？它的实现方式有哪些？</a>
 *
 * 浅克隆（Shadow Clone）是把原型对象中成员变量为值类型的属性都复制给克隆对象，把原型对象中成员变量为引用类型的引用地址也复制给克隆对象，
 * 也就是原型对象中如果有成员变量为引用对象，则此引用对象的地址是共享给原型对象和克隆对象的。
 * 简单来说就是浅克隆只会复制原型对象，但不会复制它所引用的对象；
 *
 * 深克隆（Deep Clone）是将原型对象中的所有类型，无论是值类型还是引用类型，都复制一份给克隆对象，也就是说深克隆会把原型对象和原型对象所引用的对象，都复制一份给克隆对象；
 *
 */
public class DeepClone {

    /**
     * 用户类
     */
    static class People implements Cloneable,Serializable {
        private Integer id;
        private String name;
        private Address address;

        public People(Integer id, String name, Address address) {
            this.id = id;
            this.name = name;
            this.address = address;
        }


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        /**
         * 重写 clone 方法
         *
         * @throws CloneNotSupportedException
         */
        @Override
        protected People clone() throws CloneNotSupportedException {
            People people = (People) super.clone();
            people.setAddress(this.address.clone()); // 引用类型克隆赋值
            return people;
        }

    }

    /**
     * 地址类
     */
    static class Address implements Cloneable,Serializable {
        private Integer id;
        private String city;

        public Address(Integer id, String city) {
            this.id = id;
            this.city = city;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        /**
         * 重写 clone 方法
         *
         * @throws CloneNotSupportedException
         */
        @Override
        protected Address clone() throws CloneNotSupportedException {
            return (Address) super.clone();
        }

    }


    /**
     * 1. 所有对象都实现克隆 <b>前提是所有对象需要实现Cloneable接口，并且重写clone方法</b>
     * 既要实现空接口 Cloneable，还要重写 Object 的 clone() 方法；
     * 这种方式我们需要修改 People 和 Address 类，让它们都实现 Cloneable 的接口，让所有的引用对象都实现克隆，从而实现 People 类的深克隆；
     * @throws CloneNotSupportedException
     */
    @Test
    public void all_object_impl_clone() throws CloneNotSupportedException {
        // 创建被赋值对象
        Address address = new Address(110, "北京");
        People p1 = new People(1, "Java", address);
        // 克隆 p1 对象
        People p2 = p1.clone();
        // 修改原型对象
        p1.getAddress().setCity("西安");
        // 输出 p1 和 p2 地址信息
        //System.out.println("p1:" + p1.getAddress().getCity() + " p2:" + p2.getAddress().getCity());
        //System.out.println( p1.getAddress() == p1.clone().getAddress());
        assertEquals("西安",p1.getAddress().getCity());
        assertEquals("北京",p2.getAddress().getCity());
    }

    /**
     * 2. 通过构造方法实现深克隆 <b><所有对象是普通的java bean就行；/b>
     * 《Effective Java》中推荐使用构造器（Copy Constructor）来实现深克隆，如果构造器的参数为基本数据类型或字符串类型则直接赋值，如果是对象类型，则需要重新 new 一个对象
     */
    @Test
    public void by_constructor_clone(){
        // 创建对象
        Address address = new Address(110, "北京");
        People p1 = new People(1, "Java", address);

        // 调用构造函数克隆对象
        People p2 = new People(p1.getId(), p1.getName(), new Address(p1.getAddress().getId(), p1.getAddress().getCity()));

        // 修改原型对象
        p1.getAddress().setCity("西安");

        // 输出 p1 和 p2 地址信息
        //System.out.println("p1:" + p1.getAddress().getCity() + " p2:" + p2.getAddress().getCity());

        assertEquals("西安",p1.getAddress().getCity());
        assertEquals("北京",p2.getAddress().getCity());
    }

    /**
     * 通过字节流实现克隆
     */
    static class StreamClone {
        public static <T extends Serializable> T clone(Object obj) {
            T cloneObj = null;
            try {
                // 写入字节流
                ByteArrayOutputStream bo = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bo);
                oos.writeObject(obj);
                oos.close();
                // 分配内存,写入原始对象,生成新对象
                ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());//获取上面的输出字节流
                ObjectInputStream oi = new ObjectInputStream(bi);
                // 返回生成的新对象
                cloneObj = (T) oi.readObject();
                oi.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return cloneObj;
        }
    }

    /**
     * 通过字节流实现深克隆  <b>前提是所有对象需要实现Serializable接口</b>
     * 此方式需要注意的是，由于是通过字节流序列化实现的深克隆，因此每个对象必须能被序列化，必须实现 Serializable 接口，标识自己可以被序列化；
     */
    @Test
    public void by_byte_stream_clone(){
        // 创建对象
        Address address = new Address(110, "北京");
        People p1 = new People(1, "Java", address);
        // 通过字节流实现克隆
        People p2 = StreamClone.clone(p1);
        // 修改原型对象
        p1.getAddress().setCity("西安");

        assertEquals("西安",p1.getAddress().getCity());
        assertEquals("北京",p2.getAddress().getCity());
    }

    /**
     * 通过第三方工具实现深克隆 <b>前提是所有对象需要实现Serializable接口</b>
     * 下面是通过 apache.commons.lang 实现
     */
    @Test
    public void by_third_tool_clone(){
        // 创建对象
        Address address = new Address(110, "北京");
        People p1 = new People(1, "Java", address);

        // 调用 apache.commons.lang 克隆对象
        People p2 = (People) SerializationUtils.clone(p1);

        // 修改原型对象
        p1.getAddress().setCity("西安");

        assertEquals("西安",p1.getAddress().getCity());
        assertEquals("北京",p2.getAddress().getCity());
    }

    /**
     * 通过 JSON 工具类实现深克隆 <b><所有对象是普通的java bean就行；/b>
     * 下面使用 Google 提供的 JSON 转化工具 Gson 来实现，其他 JSON 转化工具类也是类似的；
     */
    @Test
    public void by_json_tool_clone(){

        // 创建对象
        Address address = new Address(110, "北京");
        People p1 = new People(1, "Java", address);

        // 调用 Gson 克隆对象
        Gson gson = new Gson();
        People p2 = gson.fromJson(gson.toJson(p1), People.class);

        // 修改原型对象
        p1.getAddress().setCity("西安");

        assertEquals("西安",p1.getAddress().getCity());
        assertEquals("北京",p2.getAddress().getCity());
    }
}
