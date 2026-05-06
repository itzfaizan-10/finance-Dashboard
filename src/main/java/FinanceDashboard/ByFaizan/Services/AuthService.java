package FinanceDashboard.ByFaizan.Services;

import FinanceDashboard.ByFaizan.DTO.LoginRequestDto;
import FinanceDashboard.ByFaizan.DTO.RequestDto;
import FinanceDashboard.ByFaizan.ResponseDTO.LoginResponseDto;
import FinanceDashboard.ByFaizan.ResponseDTO.signupResponse;
import FinanceDashboard.ByFaizan.Entity.User;
import FinanceDashboard.ByFaizan.Repositary.UserRepositary;
import FinanceDashboard.ByFaizan.Security.Token.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepositary userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;




    public LoginResponseDto login(LoginRequestDto requestDto) {
        if(requestDto == null ||
                requestDto.getEmail() == null ||
                requestDto.getPassword() == null ) {
            throw new IllegalArgumentException("Invalid login credentials");
        }
        Authentication authentication =   authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail().trim()
                        ,requestDto.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtUtil.generateToken(user);
        return new LoginResponseDto(token,user.getId(), user.getUsername(),  user.getEmail());
    }

    public signupResponse signUp(RequestDto signupDto) {
         User user = userRepository.findByName(signupDto.getUsername()).orElse(null);
         if(user != null)  throw  new IllegalArgumentException("User Already Exists");

         user = userRepository.save(User.builder()
                 .name(signupDto.getUsername())
                 .email(signupDto.getEmail())
                 .password(passwordEncoder.encode(signupDto.getPassword()))
                 .build());

        String token = jwtUtil.generateToken(user);

        return new signupResponse(user.getId(), user.getName(), user.getEmail(), token);
    }
}












