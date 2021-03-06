package com.revature.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.revature.filter.JwtFilter;
import com.revature.service.CustomUserDetailsService;


@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends  WebSecurityConfigurerAdapter {
	
		@Autowired
		private CustomUserDetailsService userDetailsService;
	 	
	 	@Autowired
	    private JwtFilter jwtFilter;

	 	@Bean
	    public PasswordEncoder passwordEncoder(){
	        return NoOpPasswordEncoder.getInstance();
	    }
	    
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	    	//http.cors(); // The cors() method will add the Spring-provided CorsFilter to 
	        http.cors().and().csrf()  // the application context which in turn bypasses the authorization
	        .disable()   // checks for OPTIONS requests.
	        .authorizeRequests()
	        .antMatchers("/authenticate","/register","/score/global","/score/friendlist/*")
	        .permitAll().antMatchers(HttpMethod.OPTIONS, "/**")
	                .permitAll().anyRequest().authenticated()
	                .and().exceptionHandling().and().sessionManagement()
	                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	        
	        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	    }

	    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	    @Override
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	        return super.authenticationManagerBean();
	    }

	    @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(userDetailsService);
	    }
	    
    

}