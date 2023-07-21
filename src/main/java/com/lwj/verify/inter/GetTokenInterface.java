package com.lwj.verify.inter;


import javax.servlet.http.HttpServletRequest;

public interface GetTokenInterface {
    String getToken(HttpServletRequest request);
}
