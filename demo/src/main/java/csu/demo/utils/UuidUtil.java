package csu.demo.utils;

import java.util.UUID;

public class UuidUtil {
    public static String next(){
        return UUID.randomUUID().toString().replace('-', 'a');
    }

}
