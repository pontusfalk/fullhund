package lk.pontusfa.fullhund.assembler;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.Collections.unmodifiableCollection;

class DeploymentDescriptor {
    private final Collection<String> errors = new ArrayList<>();
    private WebAppDescriptor webAppDescriptor = new WebAppDescriptor();
    private Status status = Status.ERROR;

    WebAppDescriptor getWebAppDescriptor() {
        return webAppDescriptor;
    }

    void setWebAppDescriptor(WebAppDescriptor webAppDescriptor) {
        this.webAppDescriptor = webAppDescriptor;
    }

    Collection<String> getErrors() {
        return unmodifiableCollection(errors);
    }

    Status getStatus() {
        return status;
    }

    void setStatus(Status status) {
        this.status = status;
    }

    void addError(String error) {
        errors.add(error);
    }

    enum Status {
        COMPLETE,
        ERROR
    }
}
