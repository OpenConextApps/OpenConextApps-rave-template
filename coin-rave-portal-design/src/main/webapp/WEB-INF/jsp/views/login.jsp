<%--
  ~ Licensed to the Apache Software Foundation (ASF) under one
  ~ or more contributor license agreements.  See the NOTICE file
  ~ distributed with this work for additional information
  ~ regarding copyright ownership.  The ASF licenses this file
  ~ to you under the Apache License, Version 2.0 (the
  ~ "License"); you may not use this file except in compliance
  ~ with the License.  You may obtain a copy of the License at
  ~
  ~   http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing,
  ~ software distributed under the License is distributed on an
  ~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  ~ KIND, either express or implied.  See the License for the
  ~ specific language governing permissions and limitations
  ~ under the License.
  --%>
<%@ page language="java" trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp" %>
<fmt:setBundle basename="messages"/>
<%-- Note: This page has the body definition embedded so we can reference it directly from the security config file. --%>
<tiles:insertDefinition name="templates.base">
    <%-- Override the default pageTitleKey and then export it to the request scope for use later on this page --%>
    <tiles:putAttribute name="pageTitleKey" value="page.login.title"/>
    <tiles:importAttribute name="pageTitleKey" scope="request"/>

    <tiles:putAttribute name="body">
        <div class="container">
            <c:if test="${not empty message}">
                <div class="alert alert-info">${message}</div>
            </c:if>

            <h1><fmt:message key="${pageTitleKey}"/></h1>

            <div class="row well">
                <div class="span6">
                  <a href="OpenSAML.sso/Login?target=https://engine.dev.surfconext.nl/authentication/idp/single-sign-on">
                    <button class="btn btn-primary">Login at surfconext</button>
                  </a>

                  <a href="OpenSAML.sso/Login?target=https://openidp.feide.no/simplesaml/saml2/idp/SSOService.php">
                    <button class="btn btn-primary">Login at feido.no</button>
                  </a>

                  <a href="OpenSAML.sso/Login?target=https://mujina-idp.dev.surfconext.nl/SingleSignOnService">
                    <button class="btn btn-primary">Login at Mujina</button>
                  </a>
                </div>
            </div>
        </div>
    </tiles:putAttribute>
</tiles:insertDefinition>
