package com.contact.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.contact.entity.UserRole;
import com.contact.services.UserService;
import com.security.CustomUserDetails;
import com.security.CustomUserDetailsService;

@Configuration
public class SecurityConfig {


	private final UserService userService;

	public SecurityConfig(UserService userService) {
		this.userService = userService;
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(this.userDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());

		return daoAuthenticationProvider;
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return (request, response, authentication) -> {

			boolean isAdmin = authentication.getAuthorities().stream().anyMatch(
					a-> a.getAuthority().equals("ROLE_ADMIN"));

			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

			userService.updateLastLogin(userDetails.getUser().getId());

			if(isAdmin) {
				response.sendRedirect("/admin/dashboard");
			} else {
				response.sendRedirect("/user/dashboard");
			}

		};
	}


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
		httpSecurity.authenticationProvider(this.daoAuthenticationProvider());

		httpSecurity

		.authorizeHttpRequests(auth -> auth
			.requestMatchers("/" , "/about","/css/**", "/login" , "/sign-up","/signup-success"
					,"/contact-us", "/help","/process-signup").permitAll()
			.requestMatchers("/user/**").hasRole(UserRole.USER.toString())
			.requestMatchers("/admin/**").hasRole(UserRole.ADMIN.toString())
			.anyRequest().authenticated())

		.formLogin(form -> form
				.loginPage("/login")
				.loginProcessingUrl("/process-login")
				.usernameParameter("username")
				.passwordParameter("password")
				.successHandler(authenticationSuccessHandler())
                .failureUrl("/login?error")
                .permitAll())
		.logout(logout->logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login?logout")
				.invalidateHttpSession(true)
				.permitAll())
		.rememberMe(remember->remember
				.key("contact-manager-remember-key")
				.tokenValiditySeconds(24*60*60) // 1 day
				.userDetailsService(this.userDetailsService())
				.rememberMeParameter("remember-me"))
		.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
				);

		return httpSecurity.build();
	}

}
