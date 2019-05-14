package lk.pontusfa.fullhund.assembler;

import lk.pontusfa.fullhund.assembler.DeploymentDescriptor.Status;
import lk.pontusfa.fullhund.assembler.WebAppDescriptorVerifier.Error;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.Reader;

public class DeploymentDescriptorAssembler {
    private final Reader xmlSource;

    public DeploymentDescriptorAssembler(Reader xmlSource) {
        this.xmlSource = xmlSource;
    }

    public DeploymentDescriptor assemble() {
        DeploymentDescriptor deploymentDescriptor = new DeploymentDescriptor();
        if (xmlSource == null) {
            return deploymentDescriptor;
        }

        try {
            WebAppDescriptor webAppDescriptor = parseWebAppDescriptor();
            deploymentDescriptor.setWebAppDescriptor(webAppDescriptor);
            WebAppDescriptorVerifier verifier = new WebAppDescriptorVerifier(webAppDescriptor);
            for (Error error : verifier.verify()) {
                deploymentDescriptor.addError(error.toString());
            }
            deploymentDescriptor.setStatus(Status.COMPLETE);
        } catch (UnparseableWebAppException e) {
            deploymentDescriptor.addError(e.getLocalizedMessage());
            deploymentDescriptor.setStatus(Status.ERROR);
        }

        return deploymentDescriptor;
    }

    private WebAppDescriptor parseWebAppDescriptor() throws UnparseableWebAppException {
        try {
            JAXBContext context = JAXBContext.newInstance(WebAppDescriptor.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setEventHandler(event -> false);
            return (WebAppDescriptor) unmarshaller.unmarshal(xmlSource);
        } catch (JAXBException e) {
            Throwable linkedException = e.getLinkedException();
            throw new UnparseableWebAppException(linkedException != null ? linkedException : e);
        } catch (IllegalArgumentException e) {
            throw new UnparseableWebAppException(e);
        }
    }
}
