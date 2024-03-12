package com.nian.shortlink.admin.utils;

import com.nian.shortlink.admin.domain.dto.user.UserDTO;

public class UserThreadLocal {
    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    public static void saveUser(UserDTO user){
        tl.set(user);
    }

    public static UserDTO getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
