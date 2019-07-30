package com.sxc;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ClassName:GateWayEntry
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  18:22
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 18:22   kuchensheng    1.0
 */
@SpringBootApplication
@EnableDubbo
public class GateWayEntry {
    public static void main(String[] args) {
        SpringApplication.run(GateWayEntry.class,args);
    }
}
