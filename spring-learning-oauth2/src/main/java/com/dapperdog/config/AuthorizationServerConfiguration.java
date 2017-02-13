package com.dapperdog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

//if auth server is in seperate app, then token converters would need to be implemented identically in both spots
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
	
	@Value("signing-key:8j12893j1289d8jf89j1892jf89j98a-sd0f")
	private String signingKey;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public AuthorizationServerConfiguration(){
		super();
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter(){
		final JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey(signingKey);
		return accessTokenConverter;
	}
	
	@Bean
	public TokenStore tokenStore(){
		return new JwtTokenStore(accessTokenConverter());
	}
	
	public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
		endpoints.tokenStore(tokenStore())
		.authenticationManager(authenticationManager)
		.accessTokenConverter(accessTokenConverter());
	}
	
	@Override
	public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
		.withClient("client-with-secret")
		.secret("usersecret")
		.authorizedGrantTypes("client_credentials","password")
		.authorities("ROLE_CLIENT_WITH_SECRET")
		.scopes("read")
		.accessTokenValiditySeconds(3600).and()
		.withClient("admin-trusted")
		.authorizedGrantTypes("password","authorization_code","refresh_token","implicit")
		.accessTokenValiditySeconds(3600);
	}
}
