package com.sxc.demo.provider;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName:MyJob
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  17:45
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 17:45   kuchensheng    1.0
 */
public class MyJob implements Serializable {
    private Integer id;
    private String name;
    private Long currentTime;
    private Date now;
    private Float price;
    private Boolean check;
    private Double max;

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

    public Long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Long currentTime) {
        this.currentTime = currentTime;
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }
}
