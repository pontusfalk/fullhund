package lk.pontusfa.fullhund.servlet;

import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletSecurityElement;
import java.util.*;

class FullHundDynamicServletRegistration implements ServletRegistration.Dynamic {
    private final String servletName;
    private final Servlet servlet;
    private final Collection<String> mappings = new HashSet<>();

    FullHundDynamicServletRegistration(String servletName, Servlet servlet) {
        this.servletName = servletName;
        this.servlet = servlet;
    }

    @Override
    public String getName() {
        throw new NotImplementedException();
    }

    @Override
    public String getClassName() {
        return servlet.getClass().getName();
    }

    @Override
    public boolean setInitParameter(String name, String value) {
        throw new NotImplementedException();
    }

    @Override
    public String getInitParameter(String name) {
        throw new NotImplementedException();
    }

    @Override
    public Set<String> setInitParameters(Map<String, String> initParameters) {
        throw new NotImplementedException();
    }

    @Override
    public Map<String, String> getInitParameters() {
        throw new NotImplementedException();
    }

    @Override
    public void setLoadOnStartup(int loadOnStartup) {
        throw new NotImplementedException();
    }

    @Override
    public Set<String> setServletSecurity(ServletSecurityElement constraint) {
        throw new NotImplementedException();
    }

    @Override
    public void setMultipartConfig(MultipartConfigElement multipartConfig) {
        throw new NotImplementedException();
    }

    @Override
    public void setAsyncSupported(boolean isAsyncSupported) {
        throw new NotImplementedException();
    }

    @Override
    public Set<String> addMapping(String... urlPatterns) {
        mappings.addAll(Arrays.asList(urlPatterns));
        return Collections.emptySet();
    }

    @Override
    public Collection<String> getMappings() {
        return mappings;
    }

    @Override
    public String getRunAsRole() {
        throw new NotImplementedException();
    }

    @Override
    public void setRunAsRole(String roleName) {
        throw new NotImplementedException();
    }
}
