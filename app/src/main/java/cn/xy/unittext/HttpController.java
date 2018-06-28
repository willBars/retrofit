package cn.xy.unittext;

/**
 * Created by p on 18/5/28.
 */
public class HttpController {

    private static HttpController INSTANCE;
    private static HttpSetProxy mHttpSetProxy;

    private HttpController() {
        INSTANCE = this;
        mHttpSetProxy = new HttpSetProxy();
    }

    public static HttpController getInstance(){
        if (INSTANCE == null){
            synchronized (HttpController.class){
                INSTANCE = new HttpController();
            }
        }
        return INSTANCE;
    }

    public HttpSetProxy getmNetProxy() {
        return mHttpSetProxy;
    }
}
