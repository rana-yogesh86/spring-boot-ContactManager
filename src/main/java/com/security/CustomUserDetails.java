package com.security;


import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.contact.entity.User;

public class CustomUserDetails implements  UserDetails {

	private User user;

	public CustomUserDetails(User user) {
		super();
		this.user = user;
	}



	public User getUser() {
		return user;
	}



	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_"+user.getRole().toString());
		return List.of(simpleGrantedAuthority);
	}

	@Override
	public @Nullable String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getEmail();
	}

}
