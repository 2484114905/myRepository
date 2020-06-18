package csu.demo.service;

import csu.demo.model.User;
import csu.demo.utils.ConcurrentUtil;
import org.springframework.stereotype.Service;

@Service
public class HostHolder {
    public User getUser(){
        return ConcurrentUtil.getHost();
    }

    public void setUser(User user){
        ConcurrentUtil.setHost(user);
    }
}
