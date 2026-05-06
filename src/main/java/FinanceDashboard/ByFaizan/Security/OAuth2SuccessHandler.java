package FinanceDashboard.ByFaizan.Security;

import FinanceDashboard.ByFaizan.Entity.User;
import FinanceDashboard.ByFaizan.Repositary.UserRepositary;
import FinanceDashboard.ByFaizan.Security.Token.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.management.RuntimeMBeanException;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepositary  userRepositary;

    private final JwtUtil jwtUtil;

    @Value("${app.frontend.redirect-url}")
    private String frontendRedirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        System.out.println("=== OAUTH SUCCESS ===");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        if(email == null){
            throw new RuntimeException("email not found");
        }
        String name = oAuth2User.getAttribute("name");

        System.out.println("Email: " + email);
        System.out.println("Name: " + name);
//        System.out.println("Frontend URL from properties: " + frontendRedirectUrl);

        // 👉 Find or create user in DB
        User user = userRepositary.findByEmail(email).orElseGet(() ->
                userRepositary.save(User.builder()
                        .email(email)
                        .name(name)
                        .password(null)// OAuth users may not need password
                        .build())
        );
        System.out.println("User saved with name: " + user.getName());
        String token = jwtUtil.generateToken(user);
        response.sendRedirect(frontendRedirectUrl + "?token=" + token);

    }
}
