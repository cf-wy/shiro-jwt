package com.shiro.demo.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    UNAUTHORIZED(0,"未认证"),
    NORMAL(1,"正常"),
    LOCK(2,"锁定");
    private Integer key;
    private String value;
    UserStatus(Integer key,String value){
        this.key=key;
        this.value=value;
    }
    public static UserStatus convertByKey(Integer key){
        for(UserStatus userStatus:values()){
            if(userStatus.getKey()==key){
                return userStatus;
            }
        }
        return null;
    }

}
