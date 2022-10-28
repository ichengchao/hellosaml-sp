package name.chengchao.hellosaml.sp.service;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.opensaml.core.config.InitializationService;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.Unmarshaller;
import org.opensaml.core.xml.io.UnmarshallerFactory;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Response;
import org.opensaml.xmlsec.signature.Signature;
import org.opensaml.xmlsec.signature.support.SignatureValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import name.chengchao.hellosaml.sp.utils.CertManager;

public class SAMLPaserTest {

    public static void main(String[] args) throws Exception {
        String publicKeyString = "MIICljCCAX4CCQC7RjkGROeKPDANBgkqhkiG9w0BAQsFADANMQswCQYDVQQGEwJDTjAeFw0yMDA5MzAwMzIxNTBaFw0zMDA5MzAwMzIxNTBaMA0xCzAJBgNVBAYTAkNOMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuhz/GGrDLm0ooXDtTF4KP0KSF9c/9NpvrrWSOObNA/8zmoC8wIE0WAMCQARtWeztnyAPVT8l36hK23tldDaz9CmAUWBmUfqtIBnI6EFt20MA43bls3EOoMxOj65Z93mQulPsQn1NaWKQ48gL6LPFDRPKMt2UBH+s6FbTJmX22KYiM6WGqp5lJqJelswWRVm7CLfgbcU3+eBo5L7L+M/GN0xcgb9PajO3L3ad7FLLFs20Zu/dyteYvHDdR0n0P+yvhrkHcaWW5Mx83vcr2IhvtgJcXeby4dGfep4Ym8WJPrU4n4arUCfzYAohJklfGmtEop7nc8+ljktEZIEYbB9LVwIDAQABMA0GCSqGSIb3DQEBCwUAA4IBAQCBLtYUlvxIzTKExsjZj691/vwxvtlLo3w1Cf7+VPPHCS1zAliFI4S0U5U46mkiohgvPI+GUQTnPOgDj9mN6oYQ17A0czjqBwOUMiPCCNMKN+FMHFXKflUwzJ5SMWL1S4zPNaomIguRHDKWeMDagAQuAspUe2tM6GY8qDCYwf5w0TqT0t0l0ZsSvbwMs+6+8KWa1kUMcP4mqFEA6XWlUaEZgIj0sxgC9YSM9M4tlY5rVB85e4TwrskpHSszMZcyiHNcyuOo/CR6xT71y+4JQq+bMtGeSvP5Qw6n6oGZpS9paB8D6MJSmpgiVVFnXBhPpgWPLxLmuuFslanasyQjvREB";
        CertManager.initSigningCredential(publicKeyString);
        InitializationService.initialize();

        String samlresponse = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz48c2FtbDJwOlJlc3BvbnNlIHhtbG5zOnNhbWwycD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOnByb3RvY29sIiBJRD0iOWQzMTQyMDItNjQ5ZS00NmU2LWIzM2MtODMyZWI2MGJmZjMwIiBJc3N1ZUluc3RhbnQ9IjIwMjEtMTEtMDFUMDg6NDU6NTIuNjU0WiIgVmVyc2lvbj0iMi4wIj48c2FtbDI6SXNzdWVyIHhtbG5zOnNhbWwyPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXNzZXJ0aW9uIj5odHRwczovL2NoZW5nY2hhby5uYW1lL2I2NWQ3NmNlNDI2MC88L3NhbWwyOklzc3Vlcj48c2FtbDJwOlN0YXR1cz48c2FtbDJwOlN0YXR1c0NvZGUgVmFsdWU9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDpzdGF0dXM6U3VjY2VzcyIvPjwvc2FtbDJwOlN0YXR1cz48c2FtbDI6QXNzZXJ0aW9uIHhtbG5zOnNhbWwyPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXNzZXJ0aW9uIiBJRD0iNjFjOWY2OWEtZDkzNy00Njc4LThmOGYtZWZhNjcwZDhhNThlIiBJc3N1ZUluc3RhbnQ9IjIwMjEtMTEtMDFUMDg6NDU6NTIuNjU0WiIgVmVyc2lvbj0iMi4wIiB4bWxuczp4cz0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEiPjxzYW1sMjpJc3N1ZXI+aHR0cHM6Ly9jaGVuZ2NoYW8ubmFtZS9iNjVkNzZjZTQyNjAvPC9zYW1sMjpJc3N1ZXI+PGRzOlNpZ25hdHVyZSB4bWxuczpkcz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnIyI+PGRzOlNpZ25lZEluZm8+PGRzOkNhbm9uaWNhbGl6YXRpb25NZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzEwL3htbC1leGMtYzE0biMiLz48ZHM6U2lnbmF0dXJlTWV0aG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnI3JzYS1zaGExIi8+PGRzOlJlZmVyZW5jZSBVUkk9IiM2MWM5ZjY5YS1kOTM3LTQ2NzgtOGY4Zi1lZmE2NzBkOGE1OGUiPjxkczpUcmFuc2Zvcm1zPjxkczpUcmFuc2Zvcm0gQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjZW52ZWxvcGVkLXNpZ25hdHVyZSIvPjxkczpUcmFuc2Zvcm0gQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzEwL3htbC1leGMtYzE0biMiPjxlYzpJbmNsdXNpdmVOYW1lc3BhY2VzIHhtbG5zOmVjPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzEwL3htbC1leGMtYzE0biMiIFByZWZpeExpc3Q9InhzIi8+PC9kczpUcmFuc2Zvcm0+PC9kczpUcmFuc2Zvcm1zPjxkczpEaWdlc3RNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjc2hhMSIvPjxkczpEaWdlc3RWYWx1ZT5WK2VqU3NTRW1uYllyblRzZzFtWUM3RkFIWVE9PC9kczpEaWdlc3RWYWx1ZT48L2RzOlJlZmVyZW5jZT48L2RzOlNpZ25lZEluZm8+PGRzOlNpZ25hdHVyZVZhbHVlPm9BNFBXaEJpdGhPdnJMUnRRbUI3ZEgzc3lLb0VjNHVJTzhjVnVyNGppcmFpTkJCYUhUNnhYdEZ0UzlhWnczbUNYaTNZNlBrQXByY2g4ZlNRNmpGY3hjaEpFQTRBMFJQa3Y2M25CWDE2dmhaOTlZMFJ4VENkNmIvbDNCb0g5VXQ1ZGl1alAvR0tJcHRFcTU0ZWI1S2lsWVJOd2tZYmU1SUduWGJFTm43ZlpZSWgrdzhCdEIyQnE5RXk2RHJJK1pTSlJDVEJGTktyS0NITzdPRlBzTGIvd1hheElXNHYzbCszRmpSa21IdHNGRk95VjN5TFZkNXdDc1Jkb1hQaG52MDMzWENnMEc0ejVZUTRYN2hYRTFuV09YMnl1K3lLSTRxT0J3RXFxY0k4TDFFQjFLQVFYM1puSU94M0p1Tit5VkI2U1BHT1V1bUJLRlRSbFpVaTNPaWVKdz09PC9kczpTaWduYXR1cmVWYWx1ZT48L2RzOlNpZ25hdHVyZT48c2FtbDI6U3ViamVjdD48c2FtbDI6TmFtZUlEIEZvcm1hdD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOm5hbWVpZC1mb3JtYXQ6cGVyc2lzdGVudCI+c3ViamVjdDwvc2FtbDI6TmFtZUlEPjxzYW1sMjpTdWJqZWN0Q29uZmlybWF0aW9uIE1ldGhvZD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOmNtOmJlYXJlciI+PHNhbWwyOlN1YmplY3RDb25maXJtYXRpb25EYXRhIE5vdE9uT3JBZnRlcj0iMjAyMS0xMS0wNlQwODo0NTo1Mi42NTNaIiBSZWNpcGllbnQ9Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9zYW1sMi9zcC5kbyIvPjwvc2FtbDI6U3ViamVjdENvbmZpcm1hdGlvbj48L3NhbWwyOlN1YmplY3Q+PHNhbWwyOkNvbmRpdGlvbnMgTm90T25PckFmdGVyPSIyMDIxLTExLTA2VDA4OjQ1OjUyLjY1NFoiPjxzYW1sMjpBdWRpZW5jZVJlc3RyaWN0aW9uPjxzYW1sMjpBdWRpZW5jZT51cm46YWxpYmFiYTpjbG91ZGNvbXB1dGluZzwvc2FtbDI6QXVkaWVuY2U+PC9zYW1sMjpBdWRpZW5jZVJlc3RyaWN0aW9uPjwvc2FtbDI6Q29uZGl0aW9ucz48c2FtbDI6QXV0aG5TdGF0ZW1lbnQgQXV0aG5JbnN0YW50PSIyMDIxLTExLTAxVDA4OjQ1OjUyLjY1M1oiPjxzYW1sMjpBdXRobkNvbnRleHQ+PHNhbWwyOkF1dGhuQ29udGV4dENsYXNzUmVmPnVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDphYzpjbGFzc2VzOlBhc3N3b3JkUHJvdGVjdGVkVHJhbnNwb3J0PC9zYW1sMjpBdXRobkNvbnRleHRDbGFzc1JlZj48L3NhbWwyOkF1dGhuQ29udGV4dD48L3NhbWwyOkF1dGhuU3RhdGVtZW50PjxzYW1sMjpBdHRyaWJ1dGVTdGF0ZW1lbnQ+PHNhbWwyOkF0dHJpYnV0ZSBOYW1lPSJodHRwczovL3d3dy5hbGl5dW4uY29tL1NBTUwtUm9sZS9BdHRyaWJ1dGVzL1JvbGVTZXNzaW9uTmFtZSI+PHNhbWwyOkF0dHJpYnV0ZVZhbHVlIHhtbG5zOnhzaT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEtaW5zdGFuY2UiIHhzaTp0eXBlPSJ4czpzdHJpbmciPmFkbWluQGV4YW1wbGUubmFtZTwvc2FtbDI6QXR0cmlidXRlVmFsdWU+PC9zYW1sMjpBdHRyaWJ1dGU+PHNhbWwyOkF0dHJpYnV0ZSBOYW1lPSJodHRwczovL3d3dy5hbGl5dW4uY29tL1NBTUwtUm9sZS9BdHRyaWJ1dGVzL1JvbGUiPjxzYW1sMjpBdHRyaWJ1dGVWYWx1ZSB4bWxuczp4c2k9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvWE1MU2NoZW1hLWluc3RhbmNlIiB4c2k6dHlwZT0ieHM6c3RyaW5nIj5hY3M6cmFtOjoxNzY0MjYzMTQwNDc0NjQzOnJvbGUvc3VwZXIzLGFjczpyYW06OjE3NjQyNjMxNDA0NzQ2NDM6c2FtbC1wcm92aWRlci9zdXBlckFEPC9zYW1sMjpBdHRyaWJ1dGVWYWx1ZT48c2FtbDI6QXR0cmlidXRlVmFsdWUgeG1sbnM6eHNpPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxL1hNTFNjaGVtYS1pbnN0YW5jZSIgeHNpOnR5cGU9InhzOnN0cmluZyI+YWNzOnJhbTo6MTc2NDI2MzE0MDQ3NDY0Mzpyb2xlL3N1cGVyMixhY3M6cmFtOjoxNzY0MjYzMTQwNDc0NjQzOnNhbWwtcHJvdmlkZXIvc3VwZXJBRDwvc2FtbDI6QXR0cmlidXRlVmFsdWU+PC9zYW1sMjpBdHRyaWJ1dGU+PC9zYW1sMjpBdHRyaWJ1dGVTdGF0ZW1lbnQ+PC9zYW1sMjpBc3NlcnRpb24+PC9zYW1sMnA6UmVzcG9uc2U+";
        byte[] base64DecodedResponse = Base64.getDecoder().decode(samlresponse);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
//        Document doc = db.parse(new File("/Users/charles/Desktop/aa.xml"));
        Document doc = db.parse(new ByteArrayInputStream(base64DecodedResponse));

        Element element = doc.getDocumentElement();
        System.out.println(element);

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
        if (null == sig) {
            sig = assertion.getSignature();
        }

        System.out.println("=============================================");
        System.out.println(new String(base64DecodedResponse));
        System.out.println("=============================================");
        System.out.println("subject:" + subject);
        System.out.println("issuer:" + issuer);
        System.out.println("audience:" + audience);
        System.out.println("statusCode:" + statusCode);
        System.out.println("sig:" + sig);

        SignatureValidator.validate(sig, CertManager.getPublicCredential());
    }

    public static void main1(String[] args) throws Exception {

        CertManager.initSigningCredential(null);
        InitializationService.initialize();

        String samlresponse = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz48c2FtbDJwOlJlc3BvbnNlIHhtbG5zOnNhbWwycD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOnByb3RvY29sIiBEZXN0aW5hdGlvbj0iaHR0cDovL2xvY2FsaG9zdDo4MDgwL3NhbXBsZS1zcC9sb2dpbi9zYW1sMi9zc28vc2FtbGV4YW1wbGUxMSIgSUQ9Il8yYjhjZTc4NmY0YjA5MGYwNDczYjljZDU2ODM1NmJhMiIgSW5SZXNwb25zZVRvPSJpZGFhc2JlMGMwYTQyMjE1YzAxNDIwNTI5MGNmZDcyM2Q3OTE1VWVTZkVQU0ZHckciIElzc3VlSW5zdGFudD0iMjAyMS0xMC0yN1QwOTowMzoyNS45NDlaIiBWZXJzaW9uPSIyLjAiPjxzYW1sMjpJc3N1ZXIgeG1sbnM6c2FtbDI9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDphc3NlcnRpb24iPmh0dHBzOi8vY2hlbmdjaGFvLm5hbWUvYjY1ZDc2Y2U0MjYwLzwvc2FtbDI6SXNzdWVyPjxkczpTaWduYXR1cmUgeG1sbnM6ZHM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyMiPjxkczpTaWduZWRJbmZvPjxkczpDYW5vbmljYWxpemF0aW9uTWV0aG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8xMC94bWwtZXhjLWMxNG4jIi8+PGRzOlNpZ25hdHVyZU1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyNyc2Etc2hhMSIvPjxkczpSZWZlcmVuY2UgVVJJPSIjXzJiOGNlNzg2ZjRiMDkwZjA0NzNiOWNkNTY4MzU2YmEyIj48ZHM6VHJhbnNmb3Jtcz48ZHM6VHJhbnNmb3JtIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnI2VudmVsb3BlZC1zaWduYXR1cmUiLz48ZHM6VHJhbnNmb3JtIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8xMC94bWwtZXhjLWMxNG4jIi8+PC9kczpUcmFuc2Zvcm1zPjxkczpEaWdlc3RNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjc2hhMSIvPjxkczpEaWdlc3RWYWx1ZT50U01Gcm83bFowZElGZUlHNE1RODlRSmVRclU9PC9kczpEaWdlc3RWYWx1ZT48L2RzOlJlZmVyZW5jZT48L2RzOlNpZ25lZEluZm8+PGRzOlNpZ25hdHVyZVZhbHVlPnBuN21yV0xHSm1KcFRhOXNSenhubExCQkVwSGNQRWVUZ3BjUkgyVmk1TVhQT3piWHFQVkQ5RHlqa2R5RHk1ZkN5U2p2bTdrVlovaENtcnpmakZPK21yZmlHbGdSVHZMNzBRMVpKVVoyMHM2VTBJR1lMTkUyZURDMWxNaTF1clZUSytuaWxZQlh0R1BRMzY0Vmt5NXBzZndybDN6VUtwYlA0bW1OaUpRYWhaaVRna3Z2RHJEQ3JMUmp4N1ZsSkJQY0h6NTdubDVuUXFvVDErTUxCRndvSmoxMjlwamtPOUxWcG9lTnZWekE3VitiNFNuWjRVTXRYVW1OaVk2dUtCb050V3A1RkRJdTdvbW9pSEg0NkhnOVlmTGZnMFlPNnYxeUFiemRTdCsyRXUyTXQrcU5qQzhpYUNVYktiM1ZNRjdvZENVazhWZE1mSXNpaHFsZjRRUlUydz09PC9kczpTaWduYXR1cmVWYWx1ZT48ZHM6S2V5SW5mbz48ZHM6WDUwOURhdGE+PGRzOlg1MDlDZXJ0aWZpY2F0ZT5NSUlDbGpDQ0FYNENDUUM3UmprR1JPZUtQREFOQmdrcWhraUc5dzBCQVFzRkFEQU5NUXN3Q1FZRFZRUUdFd0pEVGpBZUZ3MHlNREE1Ck16QXdNekl4TlRCYUZ3MHpNREE1TXpBd016SXhOVEJhTUEweEN6QUpCZ05WQkFZVEFrTk9NSUlCSWpBTkJna3Foa2lHOXcwQkFRRUYKQUFPQ0FROEFNSUlCQ2dLQ0FRRUF1aHovR0dyRExtMG9vWER0VEY0S1AwS1NGOWMvOU5wdnJyV1NPT2JOQS84em1vQzh3SUUwV0FNQwpRQVJ0V2V6dG55QVBWVDhsMzZoSzIzdGxkRGF6OUNtQVVXQm1VZnF0SUJuSTZFRnQyME1BNDNibHMzRU9vTXhPajY1WjkzbVF1bFBzClFuMU5hV0tRNDhnTDZMUEZEUlBLTXQyVUJIK3M2RmJUSm1YMjJLWWlNNldHcXA1bEpxSmVsc3dXUlZtN0NMZmdiY1UzK2VCbzVMN0wKK00vR04weGNnYjlQYWpPM0wzYWQ3RkxMRnMyMFp1L2R5dGVZdkhEZFIwbjBQK3l2aHJrSGNhV1c1TXg4M3ZjcjJJaHZ0Z0pjWGVieQo0ZEdmZXA0WW04V0pQclU0bjRhclVDZnpZQW9oSmtsZkdtdEVvcDduYzgrbGprdEVaSUVZYkI5TFZ3SURBUUFCTUEwR0NTcUdTSWIzCkRRRUJDd1VBQTRJQkFRQ0JMdFlVbHZ4SXpUS0V4c2paajY5MS92d3h2dGxMbzN3MUNmNytWUFBIQ1MxekFsaUZJNFMwVTVVNDZta2kKb2hndlBJK0dVUVRuUE9nRGo5bU42b1lRMTdBMGN6anFCd09VTWlQQ0NOTUtOK0ZNSEZYS2ZsVXd6SjVTTVdMMVM0elBOYW9tSWd1UgpIREtXZU1EYWdBUXVBc3BVZTJ0TTZHWThxRENZd2Y1dzBUcVQwdDBsMFpzU3Zid01zKzYrOEtXYTFrVU1jUDRtcUZFQTZYV2xVYUVaCmdJajBzeGdDOVlTTTlNNHRsWTVyVkI4NWU0VHdyc2twSFNzek1aY3lpSE5jeXVPby9DUjZ4VDcxeSs0SlFxK2JNdEdlU3ZQNVF3Nm4KNm9HWnBTOXBhQjhENk1KU21wZ2lWVkZuWEJoUHBnV1BMeExtdXVGc2xhbmFzeVFqdlJFQjwvZHM6WDUwOUNlcnRpZmljYXRlPjwvZHM6WDUwOURhdGE+PC9kczpLZXlJbmZvPjwvZHM6U2lnbmF0dXJlPjxzYW1sMnA6U3RhdHVzIHhtbG5zOnNhbWwycD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOnByb3RvY29sIj48c2FtbDJwOlN0YXR1c0NvZGUgVmFsdWU9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDpzdGF0dXM6U3VjY2VzcyIvPjwvc2FtbDJwOlN0YXR1cz48c2FtbDI6QXNzZXJ0aW9uIHhtbG5zOnNhbWwyPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXNzZXJ0aW9uIiBJRD0iX2EzZDFkZDNkZjkwMTViOGI4MGRhMDFmMTdjNTIwYTRiIiBJc3N1ZUluc3RhbnQ9IjIwMjEtMTAtMjdUMDk6MDM6MjUuOTQ3WiIgVmVyc2lvbj0iMi4wIj48c2FtbDI6SXNzdWVyPmh0dHBzOi8vY2hlbmdjaGFvLm5hbWUvYjY1ZDc2Y2U0MjYwLzwvc2FtbDI6SXNzdWVyPjxzYW1sMjpTdWJqZWN0PjxzYW1sMjpOYW1lSUQgRm9ybWF0PSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6bmFtZWlkLWZvcm1hdDp0cmFuc2llbnQiPmFkbWluPC9zYW1sMjpOYW1lSUQ+PHNhbWwyOlN1YmplY3RDb25maXJtYXRpb24gTWV0aG9kPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6Y206YmVhcmVyIj48c2FtbDI6U3ViamVjdENvbmZpcm1hdGlvbkRhdGEgSW5SZXNwb25zZVRvPSJpZGFhc2JlMGMwYTQyMjE1YzAxNDIwNTI5MGNmZDcyM2Q3OTE1VWVTZkVQU0ZHckciIE5vdE9uT3JBZnRlcj0iMjAyMS0xMC0yN1QwOTowODoyNS45NDdaIiBSZWNpcGllbnQ9Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9zYW1wbGUtc3AvbG9naW4vc2FtbDIvc3NvL3NhbWxleGFtcGxlMTEiLz48L3NhbWwyOlN1YmplY3RDb25maXJtYXRpb24+PC9zYW1sMjpTdWJqZWN0PjxzYW1sMjpDb25kaXRpb25zIE5vdEJlZm9yZT0iMjAyMS0xMC0yN1QwODo1ODoyNS45NDdaIiBOb3RPbk9yQWZ0ZXI9IjIwMjEtMTAtMjdUMDk6MDg6MjUuOTQ3WiI+PHNhbWwyOkF1ZGllbmNlUmVzdHJpY3Rpb24+PHNhbWwyOkF1ZGllbmNlPmh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9zYW1wbGUtc3Avc2FtbDIvc2VydmljZS1wcm92aWRlci1tZXRhZGF0YS9zYW1sZXhhbXBsZTExPC9zYW1sMjpBdWRpZW5jZT48L3NhbWwyOkF1ZGllbmNlUmVzdHJpY3Rpb24+PC9zYW1sMjpDb25kaXRpb25zPjxzYW1sMjpBdXRoblN0YXRlbWVudCBBdXRobkluc3RhbnQ9IjIwMjEtMTAtMjdUMDk6MDM6MjUuOTQ3WiIgU2Vzc2lvbkluZGV4PSJpZGFhc2JlMGMwYTQyMjE1YzAxNDIwNTI5MGNmZDcyM2Q3OTE1VWVTZkVQU0ZHckciIFNlc3Npb25Ob3RPbk9yQWZ0ZXI9IjIwMjEtMTAtMjdUMDk6MDg6MjUuOTQ3WiI+PHNhbWwyOkF1dGhuQ29udGV4dD48c2FtbDI6QXV0aG5Db250ZXh0Q2xhc3NSZWY+dXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOmFjOmNsYXNzZXM6UGFzc3dvcmQ8L3NhbWwyOkF1dGhuQ29udGV4dENsYXNzUmVmPjwvc2FtbDI6QXV0aG5Db250ZXh0Pjwvc2FtbDI6QXV0aG5TdGF0ZW1lbnQ+PC9zYW1sMjpBc3NlcnRpb24+PC9zYW1sMnA6UmVzcG9uc2U+";
        byte[] base64DecodedResponse = Base64.getDecoder().decode(samlresponse);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
//        Document doc = db.parse(new File("/Users/charles/Desktop/aa.xml"));
        Document doc = db.parse(new ByteArrayInputStream(base64DecodedResponse));

        Element element = doc.getDocumentElement();
        System.out.println(element);

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

        System.out.println("=============================================");
        System.out.println(new String(base64DecodedResponse));
        System.out.println("=============================================");
        System.out.println("subject:" + subject);
        System.out.println("issuer:" + issuer);
        System.out.println("audience:" + audience);
        System.out.println("statusCode:" + statusCode);

        SignatureValidator.validate(sig, CertManager.getPublicCredential());
    }

}
