package lk.pontusfa.fullhund.assembler;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.Collections.unmodifiableCollection;

public class DeploymentDescriptor {
    private final Collection<String> errors = new ArrayList<>();
    private WebAppDescriptor webAppDescriptor = new WebAppDescriptor();
    private Status status = Status.COMPLETE;

    public WebAppDescriptor getWebAppDescriptor() {
        return webAppDescriptor;
    }

    void setWebAppDescriptor(WebAppDescriptor webAppDescriptor) {
        this.webAppDescriptor = webAppDescriptor;
    }

    public Collection<String> getErrors() {
        return unmodifiableCollection(errors);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void addError(String error) {
        if (error != null && !error.isBlank()) {
            errors.add(error);
        }
    }

    public enum Status {
        COMPLETE,
        ERROR
    }
}
