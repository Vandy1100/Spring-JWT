package com.istad.jwt.rsa.repository;

import com.istad.jwt.rsa.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserRepository {
   @Select("SELECT username ,password,role FROM users_tb\n" +
           "INNER JOIN user_role_tb urt on users_tb.id = urt.user_id\n" +
           "INNER JOIN role_tb rt on rt.id = urt.role_id\n" +
           "WHERE username LIKE #{username}")
   List<User>findUserByName(String username);

}
