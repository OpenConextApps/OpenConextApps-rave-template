<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

Let's rave!

<%--@elvariable id="request" type="javax.servlet.ServletRequest"--%>

<c:url context="/Shibboleth.sso" value="/Login" var="loginUrl">
  <c:param name="target" value="${request.scheme}://${request.serverName}:${request.serverPort}${request.contextPath}"/>
</c:url>

<p><a href="${loginUrl}">Click to login!</a></p>