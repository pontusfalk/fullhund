package lk.pontusfa.fullhund.assembler;

import lk.pontusfa.fullhund.assembler.DeploymentDescriptor.Status;
import lk.pontusfa.fullhund.assembler.WebAppDescriptorVerifier.Error;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

public class DeploymentDescriptorAssembler {
    public DeploymentDescriptor assemble(InputStream xmlSource) {
        DeploymentDescriptor deploymentDescriptor = new DeploymentDescriptor();
        if (xmlSource == null) {
            return deploymentDescriptor;
        }

        try {
            WebAppDescriptor webAppDescriptor = parseWebAppDescriptor(xmlSource);
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

    private static WebAppDescriptor parseWebAppDescriptor(InputStream xmlSource) throws UnparseableWebAppException {
        try {
            JAXBContext context = JAXBContext.newInstance(WebAppDescriptor.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setEventHandler(event -> false);
            return (WebAppDescriptor) unmarshaller.unmarshal(xmlSource);
        } catch (JAXBException e) {
            Throwable linkedException = e.getLinkedException();
            throw new UnparseableWebAppException(linkedException != null ? linkedException : e);
        }
    }
}
