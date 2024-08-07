package goorm_runner.backend.member.security.config.oauth2;

import goorm_runner.backend.member.domain.Role;
import goorm_runner.backend.member.security.config.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider tokenProvider;

    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomOauth2User oauthUser = (CustomOauth2User) authentication.getPrincipal();
        String token = tokenProvider.createToken(oauthUser.getName(), Role.USER);

        response.addHeader("Authorization", "Bearer " + token);
        response.setContentType("application/json");
        response.getWriter().write("{\"token\":\"" + token + "\"}");
    }


}
