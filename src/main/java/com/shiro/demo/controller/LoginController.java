package com.shiro.demo.controller;

import com.shiro.demo.model.User;
import com.shiro.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Void> toLogin(String username, String password, HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
            //Shiro认证通过后会将user信息放到subject内，生成token并返回
            User user = (User) subject.getPrincipal();
            //String newToken = userService.generateJwtToken(user.getUsername());
            //response.setHeader("x-auth-token", newToken);
            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            // 如果校验失败，shiro会抛出异常，返回客户端失败
            log.error("User {} login fail, Reason:{}", username, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @GetMapping("logout")
    @ResponseBody
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "logoutSuccess";
    }

    @GetMapping("authenticated")
    @ResponseBody
    public String authenticated() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return subject.getPrincipal().toString();
        } else {
            return null;
        }
    }

    @GetMapping("unauthorized")
    @ResponseBody
    public String unauthorized() {
        return "无权限";
    }

    @GetMapping("role")
    @ResponseBody
    public String role() {
        Subject subject = SecurityUtils.getSubject();
        try {

            subject.checkRole("admin");
        } catch (AuthenticationException e) {
            log.error(e.getMessage(), e);
            return "无admin角色";
        }
        return "有admin角色";
    }

    @GetMapping("permission")
    @ResponseBody
    public String permission() {
        Subject subject = SecurityUtils.getSubject();
        try {

            subject.checkPermission("user:create");
        } catch (AuthenticationException e) {
            log.error(e.getMessage(), e);
            return "无user创建权限";
        }
        return "有权限";
    }

    @GetMapping("{/,/index}")
    public String error() {
        return "index";
    }

    @RequestMapping(name = "/formfilterlogin", method = RequestMethod.POST)
    @ResponseBody
    public String formfilterlogin(HttpServletRequest request) {
        String errorClassName = (String) request.getAttribute("shiroLoginFailure");
        String error = null;
        if (UnknownAccountException.class.getName().equals(errorClassName)) {
            error = "用户名/密码错误";
        } else if (IncorrectCredentialsException.class.getName().equals(errorClassName)) {
            error = "用户名/密码错误";
        } else if (errorClassName != null) {
            error = "未知错误：" + errorClassName;
        }
        return error;
    }

    @RequestMapping(name = "/formfilterlogin", method = RequestMethod.GET)
    public String login2() {
        return "formfilterlogin";
    }
}
