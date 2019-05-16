package lk.pontusfa.fullhund.servlet.registration;

import lk.pontusfa.fullhund.servlet.NotImplementedException;

import javax.servlet.Registration;
import javax.servlet.Servlet;
import java.util.Map;
import java.util.Set;

class FullHundRegistration implements Registration {
    private final String servletName;
    private final Servlet servlet;

    FullHundRegistration(String servletName, Servlet servlet) {
        this.servletName = servletName;
        this.servlet = servlet;
    }

    @Override
    public String getName() {
        return servletName;
    }

    @Override
    public String getClassName() {
        return servlet != null ? servlet.getClass().getName() : null;
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
}
