package FinanceDashboard.ByFaizan.Controller;

import FinanceDashboard.ByFaizan.DTO.LoginRequestDto;
import FinanceDashboard.ByFaizan.DTO.RequestDto;
import FinanceDashboard.ByFaizan.ResponseDTO.LoginResponseDto;
import FinanceDashboard.ByFaizan.ResponseDTO.signupResponse;
import FinanceDashboard.ByFaizan.Services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "https://finance-dashboard-frontend-x7l4.vercel.app")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

        private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<signupResponse> signup(@RequestBody RequestDto signupDto){
        System.out.print(signupDto);
        return ResponseEntity.ok(authService.signUp(signupDto));
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> Login(@RequestBody LoginRequestDto requestDto) {
            return ResponseEntity.ok(authService.login(requestDto));
    }

    }

