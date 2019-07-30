package com.sxc.demo.provider;


public interface TestService {

    String sayHello(String name);

    String sayHello(TypeEnum typeEnum);

    String sayHello(MyJob myJob);

    String sayHello(Integer index, String name, TypeEnum typeEnum, MyJob myJob);
}
