package lk.pontusfa.fullhund.servlet;

import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletSecurityElement;
import java.util.Set;

public class FullHundDynamicServletRegistration extends FullHundServletRegistration
    implements ServletRegistration.Dynamic {

    FullHundDynamicServletRegistration(String servletName, Servlet servlet,
                                       ServletMappingResolver servletMappingResolver) {
        super(servletName, servlet, servletMappingResolver);
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
