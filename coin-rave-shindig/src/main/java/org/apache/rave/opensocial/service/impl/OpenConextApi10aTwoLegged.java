package org.apache.rave.opensocial.service.impl;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 *
 */
public class OpenConextApi10aTwoLegged extends DefaultApi10a {

    @Override
    public String getRequestTokenEndpoint() {
        return "";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getAuthorizationUrl(final Token token) {
        return "";  //To change body of implemented methods use File | Settings | File Templates.
    }
}
