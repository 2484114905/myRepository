package csu.demo.utils;

import csu.demo.model.User;

public class ConcurrentUtil {
    private static ThreadLocal<User> host = new ThreadLocal<>();

    public static User getHost(){
        return host.get();
    }

    public static void setHost(User user){
        host.set(user);
    }
}
