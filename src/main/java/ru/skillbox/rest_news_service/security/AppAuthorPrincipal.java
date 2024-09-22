package ru.skillbox.rest_news_service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;;
import ru.skillbox.rest_news_service.entity.Author;
import ru.skillbox.rest_news_service.entity.Role;

import java.util.Collection;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class AppAuthorPrincipal implements UserDetails {

    private final Author author;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return author.getRoles().stream().map(Role::toAuthority).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return author.getPassword();
    }

    @Override
    public String getUsername() {
        return author.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
