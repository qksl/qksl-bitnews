package io.bitnews.passport.filter;

import com.alibaba.fastjson.JSONObject;
import io.bienews.common.helper.exception.UnauthorizedException;
import io.bitnews.common.constants.RedisConstant;
import io.bitnews.common.constants.UserSdkErrorCode;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.framework.redis.RedisManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 用于验证用户是否被限制登录
 */
@Component
@Slf4j
public class CustomerAuthFilter extends OncePerRequestFilter {

    @Autowired
    private RedisManager redisManager;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        boolean isAdmin = false;
        while (iterator.hasNext()) {
            GrantedAuthority grantedAuthority = iterator.next();
            String authority = grantedAuthority.getAuthority();
            if ("ADMIN".equals(authority)) {
                isAdmin = true;
            }
        }
        if (isAdmin) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        String username = (String) authentication.getPrincipal();
        String header = httpServletRequest.getHeader("Authorization");
        String token = StringUtils.substringAfter(header, "Bearer ");
        try {
            checkToken(username, token);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }catch (UnauthorizedException e) {
            log.warn(e.getMessage());
            returnJson(httpServletResponse, e.getMessage());
        }
    }

    private void returnJson(HttpServletResponse response, String message) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            BaseResponse baseResponse = new BaseResponse(UserSdkErrorCode.USER_SERVER_LOGIN_TOKEN_INVALID);
            String result = JSONObject.toJSONString(baseResponse);
            writer.print(result);
        } catch (IOException e){
            log.error("拦截器输出流异常"+e);
        } finally {
            if(writer != null){
                writer.close();
            }
        }
    }

    private void checkToken(String username, String token) {
        String key = RedisConstant.LOGIN_KEY + username + RedisConstant.COLON_SEPARATE + token;
        boolean hasKey = redisManager.hasKey(key);
        if (!hasKey) {
            throw new UnauthorizedException("用户认证失效，请重新登录");
        }
    }


}