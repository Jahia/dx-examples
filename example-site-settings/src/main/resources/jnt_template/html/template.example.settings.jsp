<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="settingsNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>
<%--@elvariable id="allowedByLicense" type="java.lang.Boolean"--%>
<html lang="${fn:substring(renderContext.request.locale,0,2)}">
<head>
  <meta charset="UTF-8">
  <jcr:nodeProperty node="${renderContext.mainResource.node}" name="jcr:description" inherited="true"
                    var="description"/>
  <jcr:nodeProperty node="${renderContext.mainResource.node}" name="jcr:createdBy" inherited="true" var="author"/>
  <c:set var="keywords" value="${jcr:getKeywords(renderContext.mainResource.node, true)}"/>
  <c:if test="${!empty description}">
    <meta name="description" content="${description.string}"/>
  </c:if>
  <c:if test="${!empty author}">
    <meta name="author" content="${author.string}"/>
  </c:if>
  <c:if test="${!empty keywords}">
    <meta name="keywords" content="${keywords}"/>
  </c:if>
  <title>${fn:escapeXml(renderContext.mainResource.node.displayableName)}</title>
  <c:if test="${not empty i18nJavaScriptFile}">
    <template:addResources type="javascript" resources="${i18nJavaScriptFile}"/>
  </c:if>
  <template:addResources type="css" resources="
        example/settings/roboto-fonts.css,
        example/settings/material-icons.css,
        example/settings/libs/angular-material.min.css,
        example/settings/libs/angular-material.layouts.min.css,
        example/settings/app.css"/>

  <template:addResources type="css" resources="example/fixIEissue.css"/>
  <template:addResources type="javascript"  resources="
        example/settings/libs/jquery.min.js,
        example/settings/libs/angular.min.js,
        example/settings/libs/angular-sanitize.min.js,
        example/settings/libs/angular-route.min.js,
        example/settings/libs/angular-animate.min.js,
        example/settings/libs/angular-aria.min.js,
        example/settings/libs/angular-messages.min.js,
        example/settings/libs/angular-material.min.js,
        example/settings/libs/underscore-min.js,
        example/settings/libs/underscore.string.min.js,
        example/settings/app.js,
        example/settings/directives/i18n.js
        "/>

  <template:addResources>
    <script type="text/javascript">
      (function(){
        angular.module('jahia.example')
            .constant('maContextInfos', {
              i18nLabels: examplei18n,
              moduleBase: "${url.context}${url.currentModule}",
              uiLocale: "${renderContext.UILocale}",
              siteKey:"${renderContext.site.siteKey}",
              sitePath:"${renderContext.site.path}",
              siteIdentifier:"${renderContext.site.identifier}",
              settingsActionUrl: "${url.context}${url.basePreview}${renderContext.site.path}.exampleSettings.do",
              serverContext: "${url.context}",
              hasSettings: ${exampleHasSettings},
              generateId: function() {
                return '_' + Math.random().toString(36).substr(2, 9);
              }
            });
      })();

      $(document).ready(function() {
        if (navigator.userAgent.indexOf('MSIE ') > 0) {
          $('body').addClass('msieCSS');
        }
      });
    </script>
  </template:addResources>
</head>
<body ng-app="jahia.example" class="ma-example-app">
  <template:area path="pagecontent"/>
</body>
</html>
