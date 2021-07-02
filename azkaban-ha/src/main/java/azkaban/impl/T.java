package azkaban.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class T{
    public static void main(String[] args) throws Exception{
        //获得Person的Class对象
        Class<?> cls = Class.forName("testJavaSE.Person");
        Constructor con = cls.getDeclaredConstructor();
        System.out.println("得到了Person的构造函数");
        //创建Person实例
        Person p = (Person) con.newInstance();
        System.out.println("创建了一个person对象");
        //获得Person的Method对象,参数为方法名,参数列表的类型Class对象
        Method method = cls.getDeclaredMethod("eat", String.class);
        System.out.println("得到了Person的eat方法");
        //invoke方法，参数为Person实例对象，和想要调用的方法参数
        String value = (String) method.invoke(p, "肉");
        //输出invoke方法的返回值
        System.out.println("eat方法的返回值：" + value);
    }
    static class Person{
        public  String  eat(String food) {
            System.out.println("吃"+food);
            return "返回值";
        }
    }
}