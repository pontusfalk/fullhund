package lk.pontusfa.fullhund.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;

public class FullHundServletConfig implements ServletConfig {
    private final String servletName;
    private final ServletContext servletContext;

    FullHundServletConfig(String servletName, ServletContext servletContext) {
        this.servletName = servletName;
        this.servletContext = servletContext;
    }

    @Override
    public String getServletName() {
        return servletName;
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public String getInitParameter(String name) {
        throw new NotImplementedException();
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        throw new NotImplementedException();
    }
}
