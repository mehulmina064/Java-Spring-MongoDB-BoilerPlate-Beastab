package com.beastab.dataservice.common.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    //@Value("${jwt.secret:prodoadminsecret}")
    private String secret = "prodoadminsecret";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final String authHeader = request.getHeader("Authorization");
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
        } else {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new ServletException("Authorization header missing");
            }

            final String token = authHeader.substring(7);

            try {
                // Boolean expireCheck = JWTUtils.validateExpiryToken(token);
                Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
                request.setAttribute("client_id", claims.get("id"));
                request.setAttribute("company_id", claims.get("currentCompanyId"));
                request.setAttribute("role", claims.get("role"));

                // Check token expiry
                long currentTimeMillis = System.currentTimeMillis();
                long expiryTimeMillis = claims.getExpiration().getTime();
                if (currentTimeMillis > expiryTimeMillis) {
                    throw new ServletException("Token has expired");
                }
                filterChain.doFilter(request, response);
            } catch (MalformedJwtException ex) {
                throw new RuntimeException("Malformed Authentication Token");
            } catch (ExpiredJwtException ex) {
                throw new ServletException("Token has expired");
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }
}
