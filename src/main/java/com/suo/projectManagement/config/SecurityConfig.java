package com.suo.projectManagement.config;

import com.suo.projectManagement.service.impl.MyUserDetailsService;
import com.suo.projectManagement.service.impl.UserService;
import com.suo.projectManagement.utils.RoleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by Suo Tian on 2018/5/3.
 */

@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
    /*
        当我们在一个类上添加@EnableWebSecurity注解后，Spring Security会自动帮助我们创建一个名字为的springSecurityFilterChain过滤器。
        这个过滤器实际上只是Spring Security框架验证请求的一个入口，到底如何验证请求实际上是要依赖于我们如何配置Spring Security。
        我们以WebSecurityConfigurerAdapter默认的configuer(HttpSecurity)方法进行配置。
        关于spring security参考https://www.cnblogs.com/softidea/p/5991897.html
        配置WebSecurityConfig就是要分别配置HttpSecurity, AuthenticationManagerBuilder, WebSecurity三个实例。每个实例需要有若干安全配置器SecurityConfigurer实例的配合.
    */

    private static Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    @Autowired MyUserDetailsService myUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
            HttpSecurity是SecurityBuilder接口的一个实现类，我们在构建的时候可能需要一些配置，当我们调用HttpSecurity对象的方法时，实际上就是在进行配置。
            例如在默认的安全配置中authorizeRequests()，formLogin()、httpBasic()这三个方法返回的分别是ExpressionUrlAuthorizationConfigurer、FormLoginConfigurer、HttpBasicConfigurer，
            他们都是SecurityConfigurer接口的实现类，分别代表的是不同类型的安全配置器。因此，从总的流程上来说，当我们在进行配置的时候，需要一个安全构建器SecurityBuilder(例如我们这里的HttpSecurity)，SecurityBuilder实例的创建需要有若干安全配置器SecurityConfigurer实例的配合，
            每个SecurityConfigurer子类都对应一个或多个过滤器（例如FormLoginConfigurer对应的UsernamePasswordAuthenticationFilter）。        *
        */
        //http.csrf().disable();
        http
            .authorizeRequests()
                //resources,static,templates,public下面是系统默认的静态资源搜索路径
                .antMatchers("/js/**","/css/**","/fonts/**","/images/**","/login", "/", "/logout").permitAll()
                //要求访问应用的所有用户都要被验证
                .anyRequest().authenticated()
                //Java配置中的and()方法类似于xml配置中的结束标签，and()分割后的每段配置实际上都是在HttpSecuirty中添加一个过滤器。当然并不是每个SecurityConfigurer都是通过这种方式来创建过滤器的，例如FormLoginConfigurer就直接在构造方法中来创建一个类型为UsernamePasswordAuthenticationFilter的过滤器
                .and()
            //允许所有用户可以通过表单进行验证
            .formLogin()
                //是默认的校验表单项的URL链接，检查项包括：login.html中的username, password。1. 这个文档中配置了j_spring_security_check，同时在login.html文档的form的action中配置了@{/j_spring_security_check}的话，就会检查login.html中的username和password标签的名称。2.如果这两个地方同时不加j_spring_security_check也没有问题，在login.html中要加上th:action="@{/login}"，系统会按顺序取页面输入的2个值，作为username和password，然后交由AuthenticationProvider比对。3. 如果这里不加.loginPage("/login")的话会进入系统自带的login页面。4.th:action="@{/login}"的检查也是系统自带的，因此如果自己在controller里加上了一个/login post的函数会不起作用，因为这与系统自带的重名了。
                .loginProcessingUrl("/login")
                //.loginProcessingUrl("/j_spring_security_check")
                //如果要在/login页面中使用非username和password的标签名，可以通过：.usernameParameter("uname")自定义用户名参数名称 .passwordParameter("pwd")自定义密码参数名称
                .loginPage("/login").successForwardUrl("/mainpage").permitAll()
                .and()
                //当使用WebSecurityConfigurerAdapter，注销功能将会被自动应用，也就是说，就算不写也有用。默认情况下访问/logout将会将用户注销,包含的内容有：1、使HttpSession失效 2、清空已配置的RememberMe验证 3、清空 SecurityContextHolder 4、重定向到 /login?logout. logoutUrl配置的是触发logout操作的url
                .logout().logoutSuccessUrl("/login");
}

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        /*
        * 多种方式的authenticate, 参考https://www.programcreek.com/java-api-examples/index.php?api=org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
        * UserDetails(查询数据库得到用户名相对应的密码和权限塞到这个对象中) - UserDetailsService - AuthenticationManager(AuthenticationProvider列表中的每一个AuthenticationProvider依次验证 - 实现类ProviderManager，例如代码中的AuthenticationManagerBuilder auth，auth中存储的是http请求中的用户名和密码信息) - 验证成功后填充到Authentication对象-存储在SecurityContext（HttpSession的一个属性）中。其中AuthenticationManager之后的步骤都是Spring boot框架封装好的。
        */
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
        auth.inMemoryAuthentication().withUser(UserService.USER_ADMIN).password(encodePassword(UserService.PASSWORD_ADMIN)).roles(RoleUtil.ROLE_ADMIN_WITHOUT_PREFIX);
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static String encodePassword(String password){
        return passwordEncoder().encode(password);
    }

    //BCryptPasswordEncoder is an algorithm of hashing, not encoding, therefore it provides only the hash/match method rather than the decoding method.
    public static boolean matchPassword(String password, String hashPassword){
        return passwordEncoder().matches(password, hashPassword);
    }
}