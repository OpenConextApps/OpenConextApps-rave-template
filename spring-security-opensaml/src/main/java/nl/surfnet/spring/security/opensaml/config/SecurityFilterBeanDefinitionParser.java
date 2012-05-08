package nl.surfnet.spring.security.opensaml.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.w3c.dom.Element;

import nl.surfnet.spring.security.opensaml.AuthenticationFailureHandlerImpl;
import nl.surfnet.spring.security.opensaml.SAMLResponseAuthenticationProcessingFilter;

public class SecurityFilterBeanDefinitionParser extends AbstractBeanDefinitionParser {

    @Override
    protected AbstractBeanDefinition parseInternal(final Element element, final ParserContext parserContext) {

        final String authenticationManagerRef = element.getAttribute("authentication-manager-ref");
        if (StringUtils.isBlank(authenticationManagerRef)) {
            parserContext.getReaderContext().error("Missing or empty authentication-manager-ref.", element);
        }

        final String assertionConsumerURI = element.getAttribute("assertion-consumer-uri");
        if (StringUtils.isBlank(assertionConsumerURI)) {
            parserContext.getReaderContext().error("Missing or empty assertion-consumer-uri.", element);
        }

        final String samlMessageHandlerRef = element.getAttribute("saml-message-handler-ref");
        if (StringUtils.isBlank(samlMessageHandlerRef)) {
            parserContext.getReaderContext().error("Missing or empty saml-message-handler-ref.", element);
        }

        final HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        final AuthenticationFailureHandlerImpl authenticationFailureHandler = new AuthenticationFailureHandlerImpl(requestCache);

        BeanDefinitionBuilder authenticationFilter = BeanDefinitionBuilder
                .rootBeanDefinition(SAMLResponseAuthenticationProcessingFilter.class);
        authenticationFilter.addConstructorArgValue(assertionConsumerURI);
        authenticationFilter.addPropertyReference("SAMLMessageHandler", samlMessageHandlerRef);
        authenticationFilter.addPropertyReference("authenticationManager", authenticationManagerRef);
        authenticationFilter.addPropertyValue("authenticationFailureHandler", authenticationFailureHandler);

        return authenticationFilter.getBeanDefinition();
    }
}
