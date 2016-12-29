<%@ page contentType="text/html;charset=UTF-8" language="java"
		%><?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.util.*" %>
<%@ page import="org.jahia.services.content.JCRNodeWrapper" %>
<%@ page import="org.jahia.services.content.JCRSessionFactory" %>
<%@ page import="org.jahia.services.usermanager.JahiaUserManagerService" %>
<%@ page import="org.jahia.services.content.JCRSessionWrapper" %>
<%@ page import="javax.jcr.NodeIterator" %>
<%@ page import="javax.jcr.query.Query" %>
<%@ page import="org.jahia.services.content.JCRContentUtils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>Find deactivated cache</title>
	<%@ include file="css.jspf" %>
</head>

<body>
<%@ include file="gotoIndex.jspf" %>

<h1>Nodes with cache deactivated by users</h1>
<table border="1" cellspacing="0" cellpadding="5">
	<thead>
	<tr>
		<th>Node name</th>
		<th>Path</th>
	</tr>
	</thead>
	<tbody>

	<%
		final PrintWriter s = new PrintWriter(pageContext.getOut());
		JCRSessionFactory.getInstance().setCurrentUser(JahiaUserManagerService.getInstance().lookupRootUser().getJahiaUser());
		JCRSessionWrapper sessionWrapper = JCRSessionFactory.getInstance().getCurrentUserSession((String) pageContext.getAttribute("workspace"));

		String pageQueryStr =
				"SELECT * FROM [jnt:content] AS item WHERE item.[j:expiration]=0";

		Query query = sessionWrapper.getWorkspace().getQueryManager().createQuery(pageQueryStr, Query.JCR_SQL2);
		NodeIterator iterator = query.execute().getNodes();
		while (iterator.hasNext()) {
			JCRNodeWrapper nodeWithDeactivatedCache = (JCRNodeWrapper) iterator.next();

	%>
	<tr>
		<td>
			<%=nodeWithDeactivatedCache.getName()%>
		</td>
		<td>
			<a href="/modules/tools/jcrBrowser.jsp?path=<%=nodeWithDeactivatedCache.getPath()%>"><%=nodeWithDeactivatedCache.getPath()%></a>
		</td>
	</tr>
	<%
			pageContext.getOut().flush();
		}
	%>
	</tbody>
</table>

</body>
</html>