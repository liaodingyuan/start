package com.liaody.sty.dao;

import com.liaody.sty.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yuanhaha
 */
@Repository
public interface UserMapper {

    /**
     * @param id
     * @return
     */
    User selectUser(@Param("id") int id);

    List<User> selectAllUsers();

}
