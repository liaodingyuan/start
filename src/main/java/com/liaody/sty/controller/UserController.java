package com.liaody.sty.controller;

import com.github.pagehelper.PageHelper;
import com.liaody.sty.constants.ResultCode;
import com.liaody.sty.constants.ResultMessage;
import com.liaody.sty.dao.UserMapper;
import com.liaody.sty.entity.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yuanhahajackjson
 */
@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/select-user")
    public ResultMessage selectUser(@RequestParam Integer userId){
        if(userId==null || userId<1){
            return ResultCode.BAD_REQUEST_PARAMS.withMessage("用户id不能为空且大于0");
        }
        User user = userMapper.selectUser(userId);
        return ResultCode.SUCCESS.withData(user);
    }
    @GetMapping("/select-all-users")
    public ResultMessage selectAllUsers(@RequestParam Integer pageSize,
                                        @RequestParam Integer currentPage){

        PageHelper.startPage(currentPage,pageSize);
        List<User> users = userMapper.selectAllUsers();
//        List<User> list = sqlSession.selectList("com.liaody.sty.dao.selectAllUsers", null, new RowBounds(1, 10));
        return ResultCode.SUCCESS.withData(users);
    }
}
