package com.smart.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
//step 3
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

@Configuration
@EnableWebSecurity
public class MyConfig extends WebSecurityConfigurerAdapter
{
    @Bean
    public UserDetailsService getUserDetailService()
    {
    	return new UserDetailsServiceImpl();
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
    	return new  BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider()
    {
         DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();

         daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());
         daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
         
         return  daoAuthenticationProvider;
    }

  //configure method...

	@Override 
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
	
	//sare rpoute mat protect karo,jo mai bta raha hu unko protect karo

	@Override
	protected void configure(HttpSecurity http) throws Exception 
	{
		http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
	    .antMatchers("/user/**").hasRole("USER")
	    .antMatchers("/**").permitAll().and().formLogin().
	    loginPage("/signin")
	    .loginProcessingUrl("/dologin")
	    .defaultSuccessUrl("/user/index")
	    .and().csrf().disable();
	}
    
        
    
}
