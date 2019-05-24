package lk.pontusfa.fullhund.servlet;

import javax.servlet.Registration;
import java.util.Map;
import java.util.Set;

class FullHundRegistration implements Registration {
    private final String className;
    private final String name;
    private Status status = Status.CREATED;

    FullHundRegistration(String name, String className) {
        this.name = name;
        this.className = className;
    }

    Status getStatus() {
        return status;
    }

    void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getClassName() {
        return className;
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

    enum Status {
        CREATED,
        REGISTERED,
        IN_SERVICE,
        FAILED_ON_INIT
    }
}
