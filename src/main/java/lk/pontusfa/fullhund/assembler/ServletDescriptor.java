package lk.pontusfa.fullhund.assembler;

import javax.xml.bind.annotation.XmlElement;

public class ServletDescriptor {
    @XmlElement(name = "servlet-name")
    private String servletName;
    @XmlElement(name = "servlet-class")
    private String servletClass;

    @SuppressWarnings("unused") //for unmarshalling
    private ServletDescriptor() {
    }

    public ServletDescriptor(String servletName, String servletClass) {
        this.servletName = servletName;
        this.servletClass = servletClass;
    }

    public String getServletName() {
        return servletName;
    }

    public String getServletClass() {
        return servletClass;
    }

    @Override
    public String toString() {
        return "ServletDescriptor{" +
               "servletName='" + servletName + '\'' +
               ", servletClass='" + servletClass + '\'' +
               '}';
    }
}
