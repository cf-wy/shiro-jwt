package com.shiro.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_roles")
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 编号
    private String role; // 角色标识程序中判断使用,如"admin",这个是唯一的:
    private String description; // 角色描述,UI界面显示使用
    private Boolean available; // 是否可用,如果不可用将不会添加给用户
    public Role(String role, String description, Boolean available) {
        this.role = role;
        this.description = description;
        this.available = available;
    }
}
