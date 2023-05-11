package com.csu.mypetstore.api.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


// Spring Security 要求容器中必须有 PasswordEncoder 实例。所以当自定义登录逻辑时要求必须给容器注入 PaswordEncoder 的 bean 对象
@Configuration
public class BCryptPasswordConfiguration {

    @Bean  // Spring初始化时执行，将返回的对象交给spring IOC容器管理，自动注入
    public PasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
