package FinanceDashboard.ByFaizan.ResponseDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class signupResponse {

   private Long id;
   private String username;
   private String email;
   private String token;

//   public signupResponse(Long userId, String name, String email, String token) {
//      this.id = userId;
//      this.username = name;
//      this.email = email;
//      this.token = token;
//   }

}
