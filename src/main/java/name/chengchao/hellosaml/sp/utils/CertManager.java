package name.chengchao.hellosaml.sp.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.opensaml.core.config.InitializationService;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.Unmarshaller;
import org.opensaml.core.xml.io.UnmarshallerFactory;
import org.opensaml.saml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml.saml2.metadata.IDPSSODescriptor;
import org.opensaml.security.x509.BasicX509Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import name.chengchao.hellosaml.sp.common.CommonConstants;

/**
 * CertManager
 *
 * @author charles
 * @date 2020-09-29
 */
public class CertManager {

    private static final Logger logger = LoggerFactory.getLogger(CertManager.class);

    private static BasicX509Credential publicCredential;

    public static void initSigningCredential(String base64PublicKey) throws Exception {
        Assert.hasText(base64PublicKey, "publickey can not be blank!");
        byte publiccert[] = Base64.getDecoder().decode(base64PublicKey);
//        InputStream inStream = new FileInputStream(CommonConstants.IDP_METADATA_FILEPATH);
        InputStream inStream = new ByteArrayInputStream(publiccert);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate publicKey = (X509Certificate) cf.generateCertificate(inStream);
        inStream.close();
        publicCredential = new BasicX509Credential(publicKey);
        logger.info("CertManager init success!");

    }

    public static BasicX509Credential getPublicCredential() {
        return publicCredential;
    }

    public static void main(String[] args) throws Exception {
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
        String idp_entityID = entityDescriptor.getEntityID();
        String publicKey = idpssoDescriptor.getKeyDescriptors().get(0).getKeyInfo().getX509Datas().get(0)
                .getX509Certificates().get(0).getValue();
        String use = idpssoDescriptor.getKeyDescriptors().get(0).getUse().getValue();
        logger.info("=============================================");
        logger.info("Meta data");
        logger.info("EntityID:" + idp_entityID);
        logger.info("publicKey:" + publicKey);
        logger.info("use:" + use);
        logger.info("=============================================");

        initSigningCredential(publicKey);
    }

}
