package com.blogapp.securityconfig;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class MySecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) {
		http.authorizeHttpRequests((requests) -> requests.requestMatchers("/auth/**").permitAll()
				.requestMatchers("/dashboard/admin").hasAuthority("ADMIN").requestMatchers("/admin/category/**")
				.hasAuthority("ADMIN")
				.requestMatchers("/post/admin/approvePost/**", "/post/admin/rejectPost/**",
						"/post/admin/viewAllcomments/**", "/post/admin/delete-comment/**")
				.hasAuthority("ADMIN").requestMatchers("/dashboard/guest").hasAuthority("GUEST")
				.requestMatchers("/post/allpost", "/post/newpost", "/post/savepost", "/post/editpostdata",
						"/post/updatepost", "/post/deletepost", "/post/viewpostdata")
				.hasAnyAuthority("ADMIN", "GUEST")
				.requestMatchers("/", "/category/**", "/blogpost/**", "/searchblogpost", "/postfiles/**", "/addcomment",
						"/error", "/style.css", "/css/**", "/js/**", "/images/**", "/favicon.ico")
				.permitAll());

		http.formLogin(form -> form.loginPage("/auth/customlogin").loginProcessingUrl("/login")
				.defaultSuccessUrl("/", true).permitAll());

		http.httpBasic(withDefaults());
		return http.build();
	}

}
