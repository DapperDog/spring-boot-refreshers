package com.dapperdog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
	
	public ResourceServerConfiguration() {
		super();
	}
	
	//localhost:8080/oauth/token?grant_type=password&client_id=user&username=user&password=password
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser("user").password("password").roles("USER").and()
		.withUser("admin").password("password").roles("ADMIN");
	}

	@Override
	public void configure(final HttpSecurity http) throws Exception {
		http.
		authorizeRequests().
		anyRequest().authenticated().and().
		sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().
		csrf().disable();
	}
}
