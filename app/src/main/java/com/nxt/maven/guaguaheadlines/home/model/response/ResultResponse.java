package com.nxt.maven.guaguaheadlines.home.model.response;

/**
 * Created by Jan Maven on 2017/8/14.
 * Email:cyjiang_11@163.com
 * Description: 访问返回的response
 */

public class ResultResponse<T> {

    public String has_more;
    public String message;
    public String success; 
    public T data;

    public ResultResponse(String more, String _message, T result) {
        has_more = more;
        message = _message;
        data = result;
    }
}
