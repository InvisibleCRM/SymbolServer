package io.invisible.symbols.manager.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author ValeriiL
 */
public class SecurityFilter implements Filter {

    private String managerRoleName;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        managerRoleName = filterConfig.getInitParameter("managerRoleName");
        if (managerRoleName == null || managerRoleName.isEmpty()) {
            throw new IllegalArgumentException("Parameter managerRoleName should be set");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (httpRequest.isUserInRole(managerRoleName)) {
            httpRequest.setAttribute("isManager", true);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // empty method
    }

}
