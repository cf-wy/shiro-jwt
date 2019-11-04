package com.shiro.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_permissions")
@NoArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主键.
    private String permission; //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    private String description;
    private Boolean available;
    public Permission(String permission, String description, Boolean available) {
        this.permission = permission;
        this.description = description;
        this.available = available;
    }
}
