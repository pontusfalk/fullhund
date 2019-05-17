package lk.pontusfa.fullhund;

public class BadWebAppConfigurationException extends RuntimeException {
    public BadWebAppConfigurationException(String message) {
        super(message);
    }

    public BadWebAppConfigurationException(Throwable e) {
        super(e);
    }
}
