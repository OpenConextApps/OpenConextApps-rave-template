/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.opensocial.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.protocol.RestfulCollection;
import org.apache.shindig.social.core.model.GroupImpl;
import org.apache.shindig.social.opensocial.model.Group;
import org.apache.shindig.social.opensocial.spi.CollectionOptions;
import org.apache.shindig.social.opensocial.spi.GroupId;
import org.apache.shindig.social.opensocial.spi.GroupService;
import org.apache.shindig.social.opensocial.spi.UserId;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import nl.surfnet.coin.api.client.domain.Group20;
import nl.surfnet.coin.api.client.domain.Group20Entry;

public class CoinGroupService implements GroupService {

    private Logger logger = LoggerFactory.getLogger(CoinGroupService.class);

    private final static String OAUTH_OPENCONEXT_API_READ_SCOPE = "read";

    private String oauthKey;
    private String oauthSecret;
    private String apiLocation;

    /**
     * Get all groups from an opensocial endpoint.
     *
     * http://localhost:8080/social/rest/groups/urn:collab:person:test.surfguest.nl:alle.veenstra
     *
     * @param userId
     * @param options
     * @param fields
     * @param token
     * @return
     */
    public Future<RestfulCollection<Group>> getGroups(UserId userId, CollectionOptions options, Set<String> fields, SecurityToken token) {

        OAuthService service = new ServiceBuilder()
                .provider(new OpenConextApi10aTwoLegged())
                .apiKey(oauthKey)
                .apiSecret(oauthSecret)
                .scope(OAUTH_OPENCONEXT_API_READ_SCOPE)
                .callback("oob")
                .signatureType(SignatureType.QueryString)
                .debug()
                .build();



        OAuthRequest req = new OAuthRequest(Verb.GET, apiLocation + "social/rest/groups/" + userId.getUserId(token));
        service.signRequest(new Token("", ""), req);
        Response response = req.send();

        final String bodyText = response.getBody();
        logger.debug("Response body: {}", bodyText);

        ObjectMapper om = new ObjectMapper();
        Group20Entry group20Entry1 = null;
        try {
            group20Entry1 = om.readValue(bodyText, Group20Entry.class);
        } catch (IOException e) {
            logger.error("Unable to parse JSON", e);
            group20Entry1 = new Group20Entry();
        }

        final List<Group20> entry = group20Entry1.getEntry();

        final List<Group> groups = new ArrayList<Group>();

        for (Group20 group20 : entry) {
            GroupImpl group = new GroupImpl();
            group.setDescription(group20.getDescription());
            group.setTitle(group20.getTitle());
            GroupId groupId = new GroupId(GroupId.Type.groupId, group20.getId());
            group.setId(groupId);
            groups.add(group);
        }

        final RestfulCollection<Group> groupRestfulCollection = new RestfulCollection<Group>(groups);
        return new Future<RestfulCollection<Group>>() {
            public boolean cancel(final boolean b) {
                return false;
            }

            public boolean isCancelled() {
                return false;
            }

            public boolean isDone() {
                return true;
            }

            public RestfulCollection<Group> get() throws InterruptedException, ExecutionException {
                return groupRestfulCollection;
            }

            public RestfulCollection<Group> get(final long l, final TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
                return groupRestfulCollection;
            }
        };
    }

    @Required
    public void setOauthKey(final String oauthKey) {
        this.oauthKey = oauthKey;
    }

    @Required
    public void setOauthSecret(final String oauthSecret) {
        this.oauthSecret = oauthSecret;
    }

    @Required
    public void setApiLocation(final String apiLocation) {
        this.apiLocation = apiLocation;
    }
}
