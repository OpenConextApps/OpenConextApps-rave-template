package nl.surfnet.spring.security.opensaml;

import org.opensaml.saml2.core.Assertion;
import org.springframework.security.core.userdetails.UserDetails;

public interface Provisioner {
    UserDetails provisionUser(final Assertion assertion);
}
