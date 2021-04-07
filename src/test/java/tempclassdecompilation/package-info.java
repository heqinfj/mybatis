/**
 * @author heqin
 *
 * java如何查看jvm中动态代理class类内容
 * https://my.oschina.net/luyaolove/blog/3079498
 *
 * JDK动态代理之查看代理类代码
 * https://zhuanlan.zhihu.com/p/140804795
 *
 *
 * 此目录作用是使用idea来反编译class文件，可以用来解决jd-gui.exe反编译工具编译不成功的情况。
 *
 * 在生成的.class文件直接贴到目录 target\test-classes\tempclassdecompilation\ 下打开即可被反编译
 *
 * 大致步骤：
 * 1、断点
 * dubug模式下，代理对象上下断点，这一步主要是方便获取代理类的类名，大家直接打印出来也可以。断点操作是防止代理类会变。
 *
 * 2、查看进程ID
 * jps命令查看当前程序的进程ID值，注意程序如果有重启，进程id是会变的。
 * 那如果确定进程id呢？很简单，在项目未启动时，jps看下，项目启动后，jps看下，多了那个进程id就是我们需要的id值了。
 *
 * 3、启动jvm中HSDB工具
 *
 * hsdb是jvm自带的工具，所以前提要把jdk安装好，环境配置好。
 * java -classpath "%JAVA_HOME%/lib/sa-jdi.jar" sun.jvm.hotspot.HSDB
 *
 * 4、配置HSDB
 * 点击【File】-->【Attach to hotspot process】-->输入进程id-->【ok】
 *
 * 5、获取代理对象类名
 * 我断点userMapper对应代理类的类名是$Proxy18@4954。图片中显示的很明显。
 *
 * 6、根据代理类类名在JVM中查看class
 * 点击【Tools】-->【Class Browser】-->输入代理类类名
 * 这里userMapper对象显示代理类类名是$Proxy18，下面查找出来一个。
 *
 * 7、生成class
 * 点击查找出来的class，再点击【Create .class File】，此操作会在本地生成一个class文件。
 *
 * 8、class反编译
 *
 * 默认生成的class文件在c盘，我的目录是在C:\Users\heqin\com\sun\proxy\$Proxy18.class，仔细看下，生成路径它是根据包名生成相应的目录。
 *
 * jd-gui.exe反编译工具编译不成功。
 *
 * 我是直接将class文件copy到idea工具，某个项目的target/class目录下，让idea工具反编译。看到selectByNumb方法了，说明这个类正是我要找userMapper的代理类。
 *
 * 备注：
 * （1）本机生成的class文件目录是在：C:\Users\heqin\com\sun\proxy\
 * （2）在生成的.class文件直接贴到目录 target\test-classes\tempclassdecompilation\ 下打开即可被反编译
 *
 * 通过这次反编译结果，可以明白:
 * 1、为什么Proxy.newProxyInstance生成代理类时，要传入InvocationHandler类，是如何调到invoke()方法的。因为代理对象对方法的实现是直接调用handler.invoke方法的。
 * 2、为什么mybatis写的mapper.java接口类，可以直接注入并调用，因为mybatis对每个mapper.java接口类，生成了代理对象是实现了这个接口的。
 */
package tempclassdecompilation;