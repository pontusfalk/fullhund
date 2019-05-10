package lk.pontusfa.fullhund.assembler;

import javax.xml.bind.annotation.XmlElement;

class ServletMappingDescriptor {
    @XmlElement(name = "servlet-name")
    private String servletName;
    @XmlElement(name = "url-pattern")
    private String urlPattern;

    @SuppressWarnings("unused") //for unmarshalling
    private ServletMappingDescriptor() {
    }

    public ServletMappingDescriptor(String servletName, String urlPattern) {
        this.servletName = servletName;
        this.urlPattern = urlPattern;
    }

    String getServletName() {
        return servletName;
    }

    String getUrlPattern() {
        return urlPattern;
    }

    @Override
    public String toString() {
        return "ServletMappingDescriptor{" +
               "servletName='" + servletName + '\'' +
               ", urlPattern='" + urlPattern + '\'' +
               '}';
    }
}
