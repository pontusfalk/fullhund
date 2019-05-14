package lk.pontusfa.fullhund.servlet;

import javax.servlet.ServletContext;

public class WebApp {
    private final ServletContext servletContext;
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

    public ServletContext getServletContext() {
        return servletContext;
    }

    public enum Status {
        UNKNOWN,
        LOADED,
        FAILED_ON_LOAD
    }
}
