package org.csiro.igsn.security;

import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.csiro.igsn.utilities.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.authentication.SpringSecurityAuthenticationSource;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.google.gson.Gson;

@EnableWebSecurity
public class MultiHttpSecurityConfig {
	
	@Configuration
	@Order(1)
	public static class APISecurityConfig extends
			WebSecurityConfigurerAdapter {
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {	
			http
			.antMatcher("/api/**")
	    	.httpBasic()
		    .and()
				.authorizeRequests()
					.antMatchers("/api/subnamespace/**").authenticated()
					.antMatchers("/api/metadata/**").authenticated()
					.antMatchers("/api/igsn/**").authenticated()				
			.and()
				.csrf().disable();
			
			
				
		}
		
		
		
		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			
			auth.ldapAuthentication()
			.userDetailsContextMapper(new UserDetailsContextMapperImpl())
			.userSearchFilter("(&(sAMAccountName={0}))") 
	        .groupRoleAttribute("cn").groupSearchBase("ou=Groups").groupSearchFilter("(&(member={0}))")
	        .contextSource(getLdapContextSource()); 
			
			
		}
		
		private LdapContextSource getLdapContextSource() throws Exception {
	        LdapContextSource cs = new LdapContextSource();
	        cs.setUrl(Config.getLdapUrl());
	        cs.setBase(Config.getLDAPBase());
	        cs.setUserDn(Config.getUserDN());
	        //cs.setAnonymousReadOnly(true);
	        Hashtable<String, Object> env = new Hashtable<String, Object>();
	        env.put(Context.REFERRAL, "follow");
	        cs.setBaseEnvironmentProperties(env);
	        cs.setPassword(Config.getLdapPassword());       
	        cs.afterPropertiesSet();
	        
	        return cs;
	    }
	
	@Configuration
	public static  class SecurityConfig extends
			WebSecurityConfigurerAdapter {
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {	
			http.authorizeRequests()
			 .antMatchers("/restricted/**").authenticated()	
			 .antMatchers("/web/**").authenticated()
			 .and()
			    .formLogin()
			    	.usernameParameter("j_username") // default is username
	               .passwordParameter("j_password") // default is password
			    	.loginPage("/views/login.html").successHandler(new CustomSuccessHandler()).failureUrl("/views/login.html?failure")		   		
			 .and()
			    .logout().logoutSuccessUrl("/")
			    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))		 
			 .and()
			    .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
				.csrf().csrfTokenRepository(csrfTokenRepository());
		}
		
		private CsrfTokenRepository csrfTokenRepository() {
			  HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
			  repository.setHeaderName("X-XSRF-TOKEN");
			  return repository;
			}
		
		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {		 
			auth.ldapAuthentication()
			.userDetailsContextMapper(new UserDetailsContextMapperImpl())
	        .userSearchBase("ou=People").userSearchFilter("(&(sAMAccountName={0}))") 
	        .groupRoleAttribute("cn").groupSearchBase("ou=Groups").groupSearchFilter("(&(member={0}))")
	        .contextSource(getLdapContextSource());

		}
		
		private LdapContextSource getLdapContextSource() throws Exception {
	        LdapContextSource cs = new LdapContextSource();
	        cs.setUrl(Config.getLdapUrl());
	        cs.setBase(Config.getLDAPBase());
	        cs.setUserDn(Config.getUserDN());
	        //cs.setAnonymousReadOnly(true);
	        Hashtable<String, Object> env = new Hashtable<String, Object>();
	        env.put(Context.REFERRAL, "follow");
	        cs.setBaseEnvironmentProperties(env);
	        cs.setPassword(Config.getLdapPassword());       
	        cs.afterPropertiesSet();
	        
	        return cs;
	    }
		
		protected class CustomSuccessHandler implements AuthenticationSuccessHandler{
			
			@Override  
		    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,  
		                                        HttpServletResponse httpServletResponse,  
		                                        Authentication authentication)  
		            throws IOException, ServletException {  
				
				HttpSession session = httpServletRequest.getSession();
				LdapUser authUser = (LdapUser) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();
				session.setAttribute("username", authUser.getUsername());
				session.setAttribute("authorities", authentication.getAuthorities());

				// set our response to OK status
				httpServletResponse.setStatus(HttpServletResponse.SC_OK);
				httpServletResponse.setContentType("text/html; charset=UTF-8");			
				Gson gson = new Gson();			
				httpServletResponse.getWriter().write(gson.toJson(authUser));
		    }  
			
			
		} 

	}
	
	
		
		

	}
	
	
}
