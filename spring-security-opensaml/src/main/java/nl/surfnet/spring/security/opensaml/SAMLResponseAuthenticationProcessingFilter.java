/*
 * Copyright 2012 SURFnet bv, The Netherlands
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.surfnet.spring.security.opensaml;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.saml2.core.Response;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class SAMLResponseAuthenticationProcessingFilter extends
        AbstractAuthenticationProcessingFilter {

    private final static Logger logger = LoggerFactory
            .getLogger(SAMLResponseAuthenticationProcessingFilter.class);

    private SAMLMessageHandler samlMessageHandler;

    @Required
    public void setSAMLMessageHandler(SAMLMessageHandler samlMessageHandler) {
        this.samlMessageHandler = samlMessageHandler;
    }


    public SAMLResponseAuthenticationProcessingFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        logger.debug("Attempting authentication.");

        SAMLMessageContext messageContext = null;

        try {
            messageContext = samlMessageHandler.extractSAMLMessageContext(request);
        } catch (MessageDecodingException me) {
            throw new ServiceProviderAuthenticationException("Could not decode SAML Response", me);
        } catch (org.opensaml.xml.security.SecurityException se) {
            throw new ServiceProviderAuthenticationException("Could not decode SAML Response", se);
        }

        logger.debug("Message received from issuer: " + messageContext.getInboundMessageIssuer());

        if (!(messageContext.getInboundSAMLMessage() instanceof Response)) {
            throw new ServiceProviderAuthenticationException("SAML Message was not a Response.");
        }

        String credentials = samlMessageHandler.extractSAMLMessage(request);

        SAMLAuthenticationToken authRequest = new SAMLAuthenticationToken((Response) messageContext.getInboundSAMLMessage(), credentials, null);

        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
        logger.debug("authRequest.getDetails(): {}", authRequest.getDetails());

        final Authentication authenticate = this.getAuthenticationManager().authenticate(authRequest);

        return authenticate;

    }

}
