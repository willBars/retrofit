package cn.xy.unittext;

/**
 * Created by p on 18/5/26
 * 定义返回code类型
 */
public interface ErrorCode {

    int CODE_OK = 0;
    int CODE_error = -1;
    int CODE_FOUNT_error = 404;
    int CODE_TIME_OUT = -5000;
    int CODE_NET_ERROR = -5001;
    int CODE_RESPONSE_EMPTY = -5002;
}
