package cifpcm.es.GilPlasenciaEduardoMyIkea.security;

import cifpcm.es.GilPlasenciaEduardoMyIkea.services.UserServiceDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  @Autowired
  UserServiceDB userService;
  @Bean
  public static PasswordEncoder getEncoder(){return new BCryptPasswordEncoder();}
  @Bean
  public SecurityFilterChain mainConfig(HttpSecurity http) throws RuntimeException{
    try {
      http.authorizeHttpRequests((authz) -> authz
            .requestMatchers("/", "/login", "/register").permitAll()
            .anyRequest().authenticated()
      );
      http.formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/")
            .permitAll();
      http.logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/");
      http.userDetailsService(userService);
      return http.build();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
