package nl.surfnet.spring.security.opensaml.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class OpenSAMLSecurityNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
        registerBeanDefinitionParser("saml-message-handler", new SamlBindingBeanDefinitionParser());
        registerBeanDefinitionParser("authentication-provider", new AuthenticationManangerBeanDefinitionParser());
		registerBeanDefinitionParser("security-filter", new SecurityFilterBeanDefinitionParser());
	}
}