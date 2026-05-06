package FinanceDashboard.ByFaizan.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
      String jwt;
      Long userId;
      String username;
      String email;

}
