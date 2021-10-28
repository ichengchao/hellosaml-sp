package hellosamlsp.service;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import hellosamlsp.utils.CertManager;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.Unmarshaller;
import org.opensaml.core.xml.io.UnmarshallerFactory;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Response;
import org.opensaml.xmlsec.signature.Signature;
import org.opensaml.xmlsec.signature.support.SignatureValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component
public class Saml2Service {

    private Logger logger = LoggerFactory.getLogger(Saml2Service.class);

    public String validateSAMLResponse(String samlResponse) throws Exception {

        Assert.hasText(samlResponse, "samlResponse can not be blank!");

        byte[] base64DecodedResponse = Base64.getDecoder().decode(samlResponse);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new ByteArrayInputStream(base64DecodedResponse));

        Element element = doc.getDocumentElement();
        UnmarshallerFactory unmarshallerFactory = XMLObjectProviderRegistrySupport.getUnmarshallerFactory();
        Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(element);
        XMLObject responseXmlObj = unmarshaller.unmarshall(element);

        Response response = (Response) responseXmlObj;
        Assertion assertion = response.getAssertions().get(0);

        String subject = assertion.getSubject().getNameID().getValue();
        String issuer = assertion.getIssuer().getValue();
        String audience = assertion.getConditions().getAudienceRestrictions().get(0).getAudiences().get(0).getURI();
        String statusCode = response.getStatus().getStatusCode().getValue();
        Signature sig = response.getSignature();
        SignatureValidator.validate(sig, CertManager.getPublicCredential());

        logger.info("=============================================");
        logger.info(new String(base64DecodedResponse));
        logger.info("=============================================");
        logger.info("subject:" + subject);
        logger.info("issuer:" + issuer);
        logger.info("audience:" + audience);
        logger.info("statusCode:" + statusCode);

        return subject;
    }

}
