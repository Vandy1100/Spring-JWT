package com.istad.jwt.rsa.configuration;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    UserDetailsService userDetailsService;

    //user credentail
//    @Bean
//    public InMemoryUserDetailsManager userDetailsManager(){
//        UserDetails user=User.builder()
//                .username("dara")
//                .password("{noop}123")
//                .authorities("read","write").build();
//        return  new InMemoryUserDetailsManager(user);
//    }
    //UserFilterChain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws  Exception{
        //1.chaining
//      return   http.csrf().disable()
////                .sessionManagement()
////                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////                .and()
//                .authorizeHttpRequests()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .httpBasic().and().build();

        //2.builder
        //3.lambda
       return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request->request.anyRequest().authenticated())
//                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }
    //1.keypair
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public KeyPair keyPair(){
        try {
            var keyGenerator= KeyPairGenerator.getInstance("RSA");
            keyGenerator.initialize(2048);
            return keyGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    //2.rsakey
    //3.encoder
    @Bean
   public JwtEncoder jwtEncoder(){
        //create instance of jwt
        JWK jwk=new RSAKey.Builder((RSAPublicKey) keyPair().getPublic()).privateKey(keyPair().getPrivate()).build();
        //provide that instance to the jwtresource
        JWKSource<SecurityContext> jwkSource=new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }
    //4.decoder
    @Bean
    public JwtDecoder jwtDecoder(){
        return  NimbusJwtDecoder.withPublicKey((RSAPublicKey) keyPair().getPublic()).build();
    }
}
