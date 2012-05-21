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

    public static final String DEFAULT_PAGE_LAYOUT_CODE = "columns_2";

    private UserService userService;
    private NewAccountService newAccountService;

    private String attributeEmail;
    private String attributeUsername;
    private String attributeDisplay;
    private String attributeGivenname;
    private String attributeSurname;

    @Required
    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    @Required
    public void setNewAccountService(final NewAccountService newAccountService) {
        this.newAccountService = newAccountService;
    }

    @Required
    public void setAttributeEmail(final String attributeEmail) {
        this.attributeEmail = attributeEmail;
    }

    @Required
    public void setAttributeUsername(final String attributeUsername) {
        this.attributeUsername = attributeUsername;
    }

    @Required
    public void setAttributeDisplay(final String attributeDisplay) {
        this.attributeDisplay = attributeDisplay;
    }

    @Required
    public void setAttributeGivenname(final String attributeGivenname) {
        this.attributeGivenname = attributeGivenname;
    }

    @Required
    public void setAttributeSurname(final String attributeSurname) {
        this.attributeSurname = attributeSurname;
    }

    public UserDetails provisionUser(final org.opensaml.saml2.core.Assertion assertion) {
        String email = getValueFromAttributeStatements(assertion, attributeEmail);
        String username = getValueFromAttributeStatements(assertion, attributeUsername);
        String display = getValueFromAttributeStatements(assertion, attributeDisplay);
        String givenName = getValueFromAttributeStatements(assertion, attributeGivenname);
        String surname = getValueFromAttributeStatements(assertion, attributeSurname);

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
        newUser.setDefaultPageLayoutCode(DEFAULT_PAGE_LAYOUT_CODE);
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
