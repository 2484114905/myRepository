package code;

import java.util.Date;

public class Proxy extends AbstractBusiness {
    private Business business = new Business();

    @Override
    public void method() {
        try {
            Date date = new Date();
            System.out.println("方法method()被调用，调用时间为" + date.toString());
            business.method();
            System.out.println("方法method()调用成功");
        }
        catch (Exception e){
            System.out.println("方法method()调用失败");
        }
    }
}
