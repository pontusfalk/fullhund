package lk.pontusfa.fullhund.assembler;

import javax.xml.bind.annotation.XmlElement;
import java.util.Optional;

class ServletMappingDescriptor {
    private String servletName;
    private String urlPattern;

    @SuppressWarnings("unused") //for unmarshalling
    private ServletMappingDescriptor() {
    }

    public ServletMappingDescriptor(String servletName, String urlPattern) {
        this.servletName = servletName;
        this.urlPattern = urlPattern;
    }

    @XmlElement(name = "servlet-name")
    String getServletName() {
        return servletName;
    }

    @SuppressWarnings("unused") //for xml unmarshalling
    private void setServletName(String servletName) {
        this.servletName = Optional.ofNullable(servletName)
                                   .map(String::trim)
                                   .filter(s -> !s.isEmpty())
                                   .orElse(null);
    }

    @XmlElement(name = "url-pattern")
    String getUrlPattern() {
        return urlPattern;
    }

    @SuppressWarnings("unused") //for xml unmarshalling
    private void setUrlPattern(String urlPattern) {
        this.urlPattern = Optional.ofNullable(urlPattern)
                                  .map(String::trim)
                                  .filter(s -> !s.isEmpty())
                                  .orElse(null);
    }

    @Override
    public String toString() {
        return "ServletMappingDescriptor{" +
               "servletName='" + servletName + '\'' +
               ", urlPattern='" + urlPattern + '\'' +
               '}';
    }
}
