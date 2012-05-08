package nl.surfnet.spring.security.opensaml;

import java.security.KeyStore;

public interface CertificateStore {
    KeyStore getKeyStore();
}
