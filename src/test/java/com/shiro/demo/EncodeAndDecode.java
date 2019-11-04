package com.shiro.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.*;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.SimpleByteSource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.Key;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class EncodeAndDecode {

    @Test
    public void testBase64(){
        String str = "hello";
        String base64Encoded = Base64.encodeToString(str.getBytes());
        String str2 = Base64.decodeToString(base64Encoded);
        log.info(base64Encoded);
        log.info(str2);
        Assert.assertEquals(str, str2);
    }

    @Test
    public void testHex(){
        String str = "hello";
        String hexEncoded = Hex.encodeToString(str.getBytes());
        String str2 = new String(Hex.decode(hexEncoded.getBytes()));
        log.info(hexEncoded);
        Assert.assertEquals(str, str2);
    }

    @Test
    public void testMD5(){
        String str = "hello";
        String salt = "123";
        String md5 = new Md5Hash(str, salt).toString();
        String md5toHex = new Md5Hash(str, salt).toHex();
        String md5toBase64 = new Md5Hash(str, salt).toBase64();
        log.info(md5);
        log.info(md5toBase64);
        log.info(md5toHex);
    }
    @Test
    public void testSha256(){
        String str = "hello";
        String salt = "123";
        String sha= new Sha256Hash(str,salt).toString();
        String shatoBase64=new Sha256Hash(str,salt).toBase64();
        log.info(sha);
        log.info(shatoBase64);
    }
    @Test
    public void testSha1(){
        String str = "hello";
        String salt = "123";
        String sha= new Sha1Hash(str,salt).toString();
        String shatoBase64=new Sha1Hash(str,salt).toBase64();
        log.info(sha);
        log.info(shatoBase64);
    }
    @Test
    public void testHashService(){
        DefaultHashService hashService = new DefaultHashService(); //默认算法SHA-512
        hashService.setHashAlgorithmName("SHA-512");
        hashService.setPrivateSalt(new SimpleByteSource("123")); //私盐，默认无
        hashService.setGeneratePublicSalt(true);//是否生成公盐，默认false
        hashService.setRandomNumberGenerator(new SecureRandomNumberGenerator());//用于生成公盐。默认就这个
        hashService.setHashIterations(1); //生成Hash值的迭代次数
        HashRequest request = new HashRequest.Builder()
                .setAlgorithmName("MD5").setSource(ByteSource.Util.bytes("hello"))
                .setSalt(ByteSource.Util.bytes("12")).setIterations(2).build();
        Hash hash=hashService.computeHash(request);
        String hex = hash.toHex();
        String base64=hash.toBase64();
        log.info(hex);
        log.info(base64);
    }

    @Test
    public void testAesService(){
        AesCipherService aesCipherService = new AesCipherService();
        aesCipherService.setKeySize(128); //设置key长度
//生成key
        Key key = aesCipherService.generateNewKey();
        String text = "hello";
//加密
        String encrptText =
                aesCipherService.encrypt(text.getBytes(), key.getEncoded()).toHex();
//解密
        String text2 =
                new String(aesCipherService.decrypt(Hex.decode(encrptText), key.getEncoded()).getBytes());
        log.info(encrptText);
        log.info(text2);
        Assert.assertEquals(text, text2);
    }
    @Test
    public void testPasswordService(){
        DefaultPasswordService passwordService=new DefaultPasswordService();
        String ps=passwordService.encryptPassword("123");
        log.info(ps);
    }
    @Test
    public void testJdbcPasswordService(){
        /*DefaultPasswordService passwordService=new DefaultPasswordService();
        String ps=passwordService.encryptPassword("123");
        log.info(ps);*/
        //1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-jdbc-passwordservice.ini");
        //2、得到SecurityManager实例 并绑定给SecurityUtils
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("wu", "123");
        try {
            //4、登录，即身份验证
            subject.login(token);
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
        }
        Assert.assertEquals(true, subject.isAuthenticated()); //断言用户已经登录
        //6、退出
        subject.logout();
    }

    @Test
    public void testMd5Hash(){
        //String algorithmName = "md5";
        String username = "wang";
        String password = "123";
        String salt1 = username;
        String salt2 = new SecureRandomNumberGenerator().nextBytes().toHex();
        int hashIterations = 2;
        //SimpleHash hash = new SimpleHash(algorithmName, password, salt1 + salt2, hashIterations);
        String encodedPassword=new Md5Hash(password,salt1+salt2,hashIterations).toHex();
         //= hash.toHex();
        log.info(salt2);
        log.info(encodedPassword);
    }

}
