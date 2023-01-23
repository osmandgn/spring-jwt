package com.tpe.config;

import com.tpe.security.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.method.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.authentication.*;


@AllArgsConstructor
@Configuration //configuration class'ımız
@EnableWebSecurity //web security aktif etmek için
@EnableGlobalMethodSecurity(prePostEnabled = true)//metod seviyesinde security aktifleştirmek için
public class WebSecurityConfig extends WebSecurityConfigurerAdapter { //Spring'in oluşturduğu class'a extens

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().
                sessionManagement().
                sessionCreationPolicy(SessionCreationPolicy.STATELESS).//restful API olduğu için stateless olacak.
                and().
                authorizeRequests().//requestleri authorize at.
                antMatchers("/register","/login").permitAll().//şunları validasyona tabi tutma.
                anyRequest().authenticated(); // dışındakileri validasyona tabi tut.
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //filter'ımızı ve usernamepassword authentication class'ını veriyoruz.
    }

    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //provider userdetailservice ve passEncoder'ımızı tanıtıyoruz.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
