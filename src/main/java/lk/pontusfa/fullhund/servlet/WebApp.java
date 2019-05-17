package lk.pontusfa.fullhund.servlet;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Collections.unmodifiableCollection;

public class WebApp {
    private final ServletContext servletContext;
    private final Collection<String> messages = new ArrayList<>();
    private Status status = Status.UNKNOWN;

    public WebApp(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Collection<String> getMessages() {
        return unmodifiableCollection(messages);
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public enum Status {
        UNKNOWN,
        LOADED,
        FAILED_ON_LOAD
    }
}
