package code;

public class Client {
    public static void main(String[] args){
        AbstractBusiness proxy = new Proxy();
        proxy.method();
    }
}
