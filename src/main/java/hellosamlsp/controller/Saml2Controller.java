package hellosamlsp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hellosamlsp.service.Saml2Service;
import hellosamlsp.utils.CertManager;
import org.opensaml.core.config.InitializationService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/saml2")
public class Saml2Controller extends BaseController implements InitializingBean {

    @Autowired
    private Saml2Service saml2Service;

    /**
     * 初始化
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        CertManager.initSigningCredential();
        InitializationService.initialize();
    }

    @RequestMapping("/sp.do")
    public void sp(HttpServletRequest request, HttpServletResponse response) {
        String result = new String();
        try {
            // admin @KreI2yZ6EcRt
            String samlResponse = request.getParameter("SAMLResponse");
            String username = saml2Service.validateSAMLResponse(samlResponse);
            result = username + " login success!";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = e.getMessage();
        }
        outputToString(response, result);
    }

}
