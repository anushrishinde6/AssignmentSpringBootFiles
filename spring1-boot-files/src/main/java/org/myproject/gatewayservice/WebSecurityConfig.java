package org.myproject.gatewayservice;
	


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	@Autowired
	private BasicAuthenticationEntryPoint entryPoint;

	@Autowired
	private AccessDeniedHandler handler;

	/*@Autowired
	private UserDetailsService userDetailsService;
*/
   
	
	
	
	/*@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(userDetailsService);
	}*/
    //Authentication :User --> Roles
	@Override
    protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.inMemoryAuthentication().passwordEncoder(org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance()).withUser("user1").password("password1")
				.roles("UPLOADFILE","DOWNLOADFILE").and().withUser("user2").password("password2")
				.roles("UPLOADFILE", "DOWNLOADFILE");
	}
    
 // Authorization : Role -> Access
    @Override
 	protected void configure(HttpSecurity http) throws Exception {
 		http.csrf().disable().authorizeRequests().antMatchers("/uploadFile")
 				.hasRole("UPLOADFILE").antMatchers("/uploadMultipleFiles").hasRole("UPLOADFILE").antMatchers("/downloadFile/**").hasRole("DOWNLOADFILE")
 				 .and()
 		         .httpBasic().authenticationEntryPoint(entryPoint)
 		         .and()
 		         .exceptionHandling().accessDeniedHandler(handler);
 				//.formLogin().permitAll().and().logout().permitAll();
 				
 				//headers().frameOptions().disable().
 	}

    /*@Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
             User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }*/
}
