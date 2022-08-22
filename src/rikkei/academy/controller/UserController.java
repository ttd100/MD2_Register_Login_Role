package rikkei.academy.controller;

import rikkei.academy.dto.request.SignUpDTO;
import rikkei.academy.dto.response.ResponseMessenger;
import rikkei.academy.model.Role;
import rikkei.academy.model.RoleName;
import rikkei.academy.model.User;
import rikkei.academy.service.role.IRoleService;
import rikkei.academy.service.role.RoleServiceIMPL;
import rikkei.academy.service.user.IUserService;
import rikkei.academy.service.user.UserServiceIMPL;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserController {
    private final IUserService userService = new UserServiceIMPL();
    private IRoleService roleService = new RoleServiceIMPL();
    public List<User> showListUser(){
        return userService.findAll();
    }
    public ResponseMessenger register(SignUpDTO signUpDTO){
        if(userService.existedByUsername(signUpDTO.getUsername())){
            return new ResponseMessenger("username_existed");
        }
        if(userService.existedByEmail(signUpDTO.getEmail())){
            return new ResponseMessenger("email_existed");
        }
        Set<String> strRoles = signUpDTO.getStrRoles();
        Set<Role> roles = new HashSet<>();
        strRoles.forEach(role->{
            switch (role){
                case "admin":
                    Role adminRole = roleService.findByName(RoleName.ADMIN);
                    roles.add(adminRole);
                    break;
                case "pm":
                    Role pmRole = roleService.findByName(RoleName.PM);
                    roles.add(pmRole);
                    break;
                default:
                    Role userRole = roleService.findByName(RoleName.USER);
                    roles.add(userRole);
            }
        });
        User user = new User(signUpDTO.getId(), signUpDTO.getName(), signUpDTO.getUsername(), signUpDTO.getEmail(), signUpDTO.getPassword(), roles);
        userService.save(user);
        showListUser();
        return new ResponseMessenger("success");
    }
}
