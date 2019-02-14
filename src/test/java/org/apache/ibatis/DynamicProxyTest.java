package org.apache.ibatis;

import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Properties;

/**
 * @Author liwenguang
 * @Date 2019-02-14 02:07
 * @Description
 */
public class DynamicProxyTest {// 由于篇幅限制， main ()方法不再单独写在另一个 类中

    public static void main(String[] args) throws Exception {
        Field field = System.class.getDeclaredField("props");
        field.setAccessible(true);
        Properties props = (Properties) field.get(null);
        props.put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        Subject subject = new RealSubject();
        TestinvokerHandler invokerHandler = new TestinvokerHandler(subject); //获取代理对象
        invokerHandler.getProxy();
    }
}

class TestinvokerHandler implements InvocationHandler {
    private Object target; // 真正的业务对象，也就是 RealSubject 对象

    public TestinvokerHandler(Object target) {// 构造方法
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //. ..在执行业务方法之前的预处理...
        Object result = method.invoke(target, args); // ...在执行业务方法之后的后 直处理 ...
        return result;
    }

    public Object getProxy() { //创建代理对象
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), target.getClass().getInterfaces(), this);
    }
}

interface Subject {
    void operation();
}

class RealSubject implements Subject {

    @Override
    public void operation() {
        System.out.println("operation");
    }
}

