package ru.skillbox.rest_news_service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skillbox.rest_news_service.service.AuthorService;

@Service
@RequiredArgsConstructor
public class AuthorDetailsServiceImpl implements UserDetailsService {

    private final AuthorService authorService;


    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return new AppAuthorPrincipal(authorService.findByUsername(name));
    }
}