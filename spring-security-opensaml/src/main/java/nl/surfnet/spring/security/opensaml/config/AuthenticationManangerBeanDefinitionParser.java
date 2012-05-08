package nl.surfnet.spring.security.opensaml.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import nl.surfnet.spring.security.opensaml.AssertionConsumerImpl;
import nl.surfnet.spring.security.opensaml.SAMLResponseAuthenticationProvider;

public class AuthenticationManangerBeanDefinitionParser extends AbstractBeanDefinitionParser {

    public static final String BEAN_ASSERTIONCONSUMER = "samlAssertionConsumer";

    @Override
    protected AbstractBeanDefinition parseInternal(final Element element, final ParserContext parserContext) {

        final String provisionerRef = element.getAttribute("provisioner-ref");
        if (StringUtils.isBlank(provisionerRef)) {
            parserContext.getReaderContext().error("The provisioner-ref is mandatory", element);
        }

        BeanDefinitionBuilder assertionComsumerBean = BeanDefinitionBuilder.genericBeanDefinition(AssertionConsumerImpl.class);
        assertionComsumerBean.addPropertyReference("provisioner", provisionerRef);
        parserContext.getRegistry().registerBeanDefinition(BEAN_ASSERTIONCONSUMER, assertionComsumerBean.getBeanDefinition());

        BeanDefinitionBuilder authenticationProvider = BeanDefinitionBuilder
                .rootBeanDefinition(SAMLResponseAuthenticationProvider.class);
        authenticationProvider.addConstructorArgReference(BEAN_ASSERTIONCONSUMER);

        return authenticationProvider.getBeanDefinition();

    }
}
