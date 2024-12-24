package io.redispro.redisexec.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 요청 URI 및 경로 출력
        logger.info("Request URI: {}", httpRequest.getRequestURI());
        logger.info("Client IP: {}", request.getRemoteAddr());
        logger.info("HTTP Method: {}", httpRequest.getMethod());
        logger.info("Query String: {}", httpRequest.getQueryString());
        logger.info("Request URL: {}", httpRequest.getRequestURL());

        // 요청을 다음 필터나 서블릿으로 전달
        chain.doFilter(request, response);
    }
}