package com.ratemyschool.main.configuration;

import com.ratemyschool.main.repository.AppUserRepository;
import com.ratemyschool.main.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static com.ratemyschool.main.util.JwtTokenUtil.BEARER_PREFIX;
import static java.util.Optional.ofNullable;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
@Configuration
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final AppUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        final String JWT = header.split(" ")[1].trim();
        if (Boolean.FALSE.equals(jwtTokenUtil.validateJWT(JWT))) {
            filterChain.doFilter(request, response);
            return;
        }


        UserDetails springUserDetails = userRepository.findByEmail(jwtTokenUtil.getUserEmail(JWT))
                .orElse(null);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                springUserDetails, null,
                ofNullable(springUserDetails).map(UserDetails::getAuthorities).orElse(Collections.emptyList()));

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
