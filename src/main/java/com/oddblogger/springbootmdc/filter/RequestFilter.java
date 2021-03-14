package com.oddblogger.springbootmdc.filter;

import com.oddblogger.springbootmdc.constant.Constants;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
@Slf4j
public class RequestFilter implements Filter {

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    UUID uniqueId = UUID.randomUUID();

    MDC.put(Constants.REQUEST_ID, uniqueId.toString());

    log.info("Request IP address is {}", servletRequest.getRemoteAddr());
    log.info("Request content type is {}", servletRequest.getContentType());

    HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(
        httpServletResponse
    );

    filterChain.doFilter(servletRequest, responseWrapper);

    responseWrapper.setHeader(Constants.REQUEST_ID, uniqueId.toString());

    log.info("Response header is set with uuid {}", responseWrapper.getHeader(Constants.REQUEST_ID));
  }
}