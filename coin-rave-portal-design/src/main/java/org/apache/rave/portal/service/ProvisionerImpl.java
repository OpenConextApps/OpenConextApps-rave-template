package org.apache.rave.portal.service;

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.userdetails.UserDetails;

import nl.surfnet.spring.security.opensaml.Provisioner;

public class ProvisionerImpl implements Provisioner {

    private UserService userService;
    private NewAccountService newAccountService;

    @Required
    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    @Required
    public void setNewAccountService(final NewAccountService newAccountService) {
        this.newAccountService = newAccountService;
    }

    public UserDetails provisionUser(final org.opensaml.saml2.core.Assertion assertion) {
        final String ATTRIBUTE_EMAIL = "urn:mace:dir:attribute-def:mail";
        final String ATTRIBUTE_USERNAME = "urn:oid:1.3.6.1.4.1.1076.20.40.40.1";
        final String ATTRIBUTE_DISPLAY = "urn:mace:dir:attribute-def:displayName";
        final String ATTRIBUTE_GIVENNAME = "urn:mace:dir:attribute-def:givenName";
        final String ATTRIBUTE_SURNAME = "urn:mace:dir:attribute-def:sn";

        String email = getValueFromAttributeStatements(assertion, ATTRIBUTE_EMAIL);
        String username = getValueFromAttributeStatements(assertion, ATTRIBUTE_USERNAME);
        String display = getValueFromAttributeStatements(assertion, ATTRIBUTE_DISPLAY);
        String givenName = getValueFromAttributeStatements(assertion, ATTRIBUTE_GIVENNAME);
        String surname = getValueFromAttributeStatements(assertion, ATTRIBUTE_SURNAME);

        if (isNewUser(username)) {
            createNewUser(display, username, email, givenName, surname, "Just new!", "I didn't fill this in yet.");
        }

        return userService.getUserByUsername(username);
    }

    private boolean isNewUser(String userName) {
        return userService.getUserByUsername(userName) == null;
    }

    private void createNewUser(String displayName,
                               String username,
                               String email,
                               String givenName,
                               String familyName,
                               String status,
                               String aboutMe) {
        org.apache.rave.portal.model.User newUser = new org.apache.rave.portal.model.User();
        newUser.setDisplayName(displayName);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setDefaultPageLayoutCode("columns_2");
        newUser.setPassword(RandomStringUtils.random(16));
        newUser.setGivenName(givenName);
        newUser.setFamilyName(familyName);
        newUser.setStatus(status);
        newUser.setAboutMe(aboutMe);

        try {
            newAccountService.createNewAccount(newUser);
        } catch (Exception e) {
            throw new RuntimeException("Cannot create new account", e);
        }
    }

    private String getValueFromAttributeStatements(final Assertion assertion, final String name) {
        final List<AttributeStatement> attributeStatements = assertion.getAttributeStatements();
        for (AttributeStatement attributeStatement : attributeStatements) {
            final List<Attribute> attributes = attributeStatement.getAttributes();
            for (Attribute attribute : attributes) {
                if (attribute.getName().equals(name)) {
                    return attribute.getAttributeValues().get(0).getDOM().getTextContent();
                }
            }
        }
        return "";
    }
}
