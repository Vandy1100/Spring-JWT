package com.istad.jwt.rsa;

import com.istad.jwt.rsa.model.User;
import com.istad.jwt.rsa.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtRsaFriendlyApplicationTests {
    @Autowired
    UserRepository userRepository;

    @Test
   void findUser(){
        System.out.println(userRepository.findUserByName("heng"));
    }

}
