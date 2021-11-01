package hellosamlsp.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import hellosamlsp.common.CommonConstants;
import hellosamlsp.service.Saml2Service;
import hellosamlsp.utils.CertManager;
import org.opensaml.core.config.InitializationService;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.Unmarshaller;
import org.opensaml.core.xml.io.UnmarshallerFactory;
import org.opensaml.saml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml.saml2.metadata.IDPSSODescriptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Controller
@RequestMapping("/saml2")
public class Saml2Controller extends BaseController implements InitializingBean {

    @Autowired
    private Saml2Service saml2Service;

    private String idp_entityID;

    /**
     * 初始化
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        InitializationService.initialize();

        // 解析metadata.xml
        DocumentBuilderFactory dbf_meta = DocumentBuilderFactory.newInstance();
        dbf_meta.setNamespaceAware(true);
        DocumentBuilder db_meta = dbf_meta.newDocumentBuilder();
        Document doc_meta = db_meta.parse(new File(CommonConstants.IDP_METADATA_FILEPATH));
        Element element_meta = doc_meta.getDocumentElement();
        UnmarshallerFactory unmarshallerFactory_meta = XMLObjectProviderRegistrySupport.getUnmarshallerFactory();
        Unmarshaller unmarshaller_meta = unmarshallerFactory_meta.getUnmarshaller(element_meta);
        XMLObject responseXmlObj_meta = unmarshaller_meta.unmarshall(element_meta);
        EntityDescriptor entityDescriptor = (EntityDescriptor) responseXmlObj_meta;
        IDPSSODescriptor idpssoDescriptor = entityDescriptor
                .getIDPSSODescriptor("urn:oasis:names:tc:SAML:2.0:protocol");
        idp_entityID = entityDescriptor.getEntityID();
        String publicKey = idpssoDescriptor.getKeyDescriptors().get(0).getKeyInfo().getX509Datas().get(0)
                .getX509Certificates().get(0).getValue();
        String use = idpssoDescriptor.getKeyDescriptors().get(0).getUse().getValue();
        logger.info("=============================================");
        logger.info("Meta data");
        logger.info("EntityID:" + idp_entityID);
        logger.info("publicKey:" + publicKey);
        logger.info("use:" + use);
        logger.info("=============================================");

        CertManager.initSigningCredential(publicKey);

    }

    @RequestMapping("/sp.do")
    public void sp(HttpServletRequest request, HttpServletResponse response) {
        String result = new String();
        try {
            // admin @KreI2yZ6EcRt
            String samlResponse = request.getParameter("SAMLResponse");
            logger.info("=============================================");
            logger.info(samlResponse);
            logger.info("=============================================");
            String username = saml2Service.validateSAMLResponse(samlResponse);
            result = username + " login success!";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = e.getMessage();
        }
        outputToString(response, result);
    }

}
