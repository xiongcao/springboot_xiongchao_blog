package com.xiongchao.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;

/**
 * @author William Guo
 */
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyAuthenticationProvider myAuthenticationProvider;

    @Value("${spring.profiles}")
    private String profiles;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //  关闭csrf
        http.csrf().disable();

        // 开启cors跨域支持
        http.cors().configurationSource(request -> {
            CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
            // 允许所有方法
            corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
            // 允许携带cookie
            corsConfiguration.setAllowCredentials(true);
            if ("prod".equals(profiles)) {
                corsConfiguration.addAllowedOrigin("http://manage.wantianqing.com");
                corsConfiguration.addAllowedOrigin("https://manage.wantianqing.com");
            }

            return corsConfiguration;
        });

        http.authorizeRequests()
                .anyRequest().permitAll()
                .and()
//                .formLogin().loginPage("/admin/signIn").defaultSuccessUrl("/admin/signInSuccess")
//                .antMatchers(HttpMethod.GET, "/admin/signIn")
//                .permitAll()
//                .antMatchers(HttpMethod.POST, "/admin")
//                .permitAll()
//                // swagger
//                .antMatchers(HttpMethod.GET, "/v2/api-docs", "/swagger-resources/**", "/webjars/**", "/swagger-ui.html")
//                .permitAll()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .sessionManagement()
//                .and()
//                .httpBasic();
        ;
    }

//    @Override
//    protected AuthenticationManager authenticationManager() {
//        ProviderManager authenticationManager = new ProviderManager(Arrays.asList(myAuthenticationProvider));
//        //不擦除认证密码，擦除会导致TokenBasedRememberMeServices因为找不到Credentials再调用UserDetailsService而抛出UsernameNotFoundException
//        authenticationManager.setEraseCredentialsAfterAuthentication(false);
//        return authenticationManager;
//    }

}
