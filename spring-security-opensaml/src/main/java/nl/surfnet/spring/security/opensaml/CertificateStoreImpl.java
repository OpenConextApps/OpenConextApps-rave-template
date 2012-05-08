package nl.surfnet.spring.security.opensaml;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class CertificateStoreImpl implements CertificateStore, InitializingBean {
    private String keystorePassword;
    private KeyStore keyStore;
    private Map<String, String> certificates;

    @Required
    public void setCertificates(Map<String, String> certificates) {
        this.certificates = certificates;
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }

    public void afterPropertiesSet() throws Exception {
        keystorePassword = "secret";
        try {
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(null, keystorePassword.toCharArray());
            for (Map.Entry<String, String> entry : certificates.entrySet()) {
                appendToKeyStore(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void appendToKeyStore(String keyAlias, String pemCert) throws Exception {
        String wrappedCert = "-----BEGIN CERTIFICATE-----\n" + pemCert + "\n-----END CERTIFICATE-----";
        ByteArrayInputStream certificateInputStream = new ByteArrayInputStream(wrappedCert.getBytes());
        final CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        final Certificate cert = certificateFactory.generateCertificate(certificateInputStream);
        IOUtils.closeQuietly(certificateInputStream);
        keyStore.setCertificateEntry(keyAlias, cert);
    }
}
