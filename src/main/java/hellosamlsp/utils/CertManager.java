package hellosamlsp.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import hellosamlsp.common.CommonConstants;
import org.opensaml.security.x509.BasicX509Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CertManager
 *
 * @author charles
 * @date 2020-09-29
 */
public class CertManager {

    private static final Logger logger = LoggerFactory.getLogger(CertManager.class);

    private static BasicX509Credential publicCredential;

    public static void initSigningCredential() throws Exception {

        InputStream inStream = new FileInputStream(CommonConstants.PUBLIC_KEY_PATH);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate publicKey = (X509Certificate) cf.generateCertificate(inStream);
        inStream.close();
        publicCredential = new BasicX509Credential(publicKey);
        logger.info("CertManager init success!");

    }

    public static BasicX509Credential getPublicCredential() {
        return publicCredential;
    }

}
