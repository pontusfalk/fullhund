package lk.pontusfa.fullhund.servlet.registration;

import lk.pontusfa.fullhund.servlet.NotImplementedException;

import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletSecurityElement;
import java.util.Set;

public class FullHundDynamicServletRegistration extends FullHundServletRegistration
    implements ServletRegistration.Dynamic {

    public FullHundDynamicServletRegistration(String servletName, Servlet servlet) {
        super(servletName, servlet);
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
    public void setRunAsRole(String roleName) {
        throw new NotImplementedException();

    }

    @Override
    public void setAsyncSupported(boolean isAsyncSupported) {
        throw new NotImplementedException();
    }
}
