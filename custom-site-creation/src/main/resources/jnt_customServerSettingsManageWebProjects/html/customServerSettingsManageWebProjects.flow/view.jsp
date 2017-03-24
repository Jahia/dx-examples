<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<%--@elvariable id="webprojectHandler" type="org.jahia.modules.serversettings.flow.WebprojectHandler"--%>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>
<jcr:node var="sites" path="/sites"/>
<jcr:nodeProperty name="j:defaultSite" node="${sites}" var="defaultSite"/>
<c:set var="defaultPrepackagedSite" value="acmespaceelektra.zip"/>
<template:addResources type="javascript" resources="jquery.min.js,jquery-ui.min.js,admin-bootstrap.js,bootstrap-filestyle.min.js,jquery.metadata.js,jquery.tablesorter.js,jquery.tablecloth.js,workInProgress.js"/>
<template:addResources type="css" resources="jquery-ui.smoothness.css,jquery-ui.smoothness-jahia.css,tablecloth.css"/>
<template:addResources type="javascript" resources="datatables/jquery.dataTables.js,i18n/jquery.dataTables-${currentResource.locale}.js,datatables/dataTables.bootstrap-ext.js"/>
<jsp:useBean id="nowDate" class="java.util.Date" />
<fmt:formatDate value="${nowDate}" pattern="yyyy-MM-dd-HH-mm" var="now"/>
<fmt:message key="label.workInProgressTitle" var="i18nWaiting"/><c:set var="i18nWaiting" value="${functions:escapeJavaScript(i18nWaiting)}"/>
<fmt:message key="serverSettings.manageWebProjects.noWebProjectSelected" var="i18nNoSiteSelected"/>
<c:set var="i18nNoSiteSelected" value="${functions:escapeJavaScript(i18nNoSiteSelected)}"/>
<c:set var="exportAllowed" value="${renderContext.user.root}"/>
<script type="text/javascript">
    function submitSiteForm(act, site) {
    	if (typeof site != 'undefined') {
    		$("<input type='hidden' name='sitesKey' />").attr("value", site).appendTo('#sitesForm');
    	} else {
    		$("#sitesForm input:checkbox[name='selectedSites']:checked").each(function() {
    			$("<input type='hidden' name='sitesKey' />").attr("value", $(this).val()).appendTo('#sitesForm');
    		});
    	}
        if (act == 'exportToFile' || act == 'exportToFileStaging') {
            workInProgress('${i18nWaiting}');
        }
        $('#sitesFormAction').val(act);
    	$('#sitesForm').submit();
    }

    $(document).ready(function () {
    	$("a.sitesAction").click(function () {
    		var act=$(this).attr('id');
    		if (act != 'createSite' && $("#sitesForm input:checkbox[name='selectedSites']:checked").length == 0) {
        		alert("${i18nNoSiteSelected}");
    			return false;
    		}
    		submitSiteForm(act);
    		return false;
    	});
    	<c:if test="${exportAllowed}">
        $("#exportSites").click(function (){
            var selectedSites = [];
            var checkedSites = $("input[name='selectedSites']:checked");
            checkedSites.each(function(){
                selectedSites.push($(this).val());
            });
            if(selectedSites.length==0) {
                alert("${i18nNoSiteSelected}");
                return false;
            }
            var name = selectedSites.length>1?"sites":selectedSites;
            var sitebox = "";
            for (i = 0; i < selectedSites.length; i++) {
                sitebox = sitebox + "&sitebox=" + selectedSites[i];
            }
            $(this).target = "_blank";
            window.open("${url.context}/cms/export/default/"+name+ '_export_${now}.zip?exportformat=site&live=true'+sitebox);
        });

        $("#exportStagingSites").click(function (){
            var selectedSites = [];
            var checkedSites = $("input[name='selectedSites']:checked");
            checkedSites.each(function(){
                selectedSites.push($(this).val());
            });
            if(selectedSites.length==0) {
                alert("${i18nNoSiteSelected}");
                return false;
            }
            var name = selectedSites.length>1?"sites":selectedSites;
            var sitebox = "";
            for (i = 0; i < selectedSites.length; i++) {
                sitebox = sitebox + "&sitebox=" + selectedSites[i];
            }
            $(this).target = "_blank";
            window.open("${url.context}/cms/export/default/"+name+ '_staging_export_${now}.zip?exportformat=site&live=false'+sitebox);
        });
        </c:if>
        $(":file").filestyle({classButton: "btn",classIcon: "icon-folder-open"/*,buttonText:"Translation"*/});
    })
</script>
<script type="text/javascript" charset="utf-8">
    $(document).ready(function() {
        var sitesTable = $('#sitesTable');

        sitesTable.dataTable({
            "sDom": "<'row-fluid'<'span6'l><'span6 text-right'f>r>t<'row-fluid'<'span6'i><'span6 text-right'p>>",
            "iDisplayLength": 10,
            "sPaginationType": "bootstrap",
            "aaSorting": [] //this option disable sort by default, the user steal can use column names to sort the table
        });
    });
</script>
<form id="sitesForm" action="${flowExecutionUrl}" method="post">
    <fieldset>
        <h2><fmt:message key="label.virtualSitesManagement"/></h2>
        <input type="hidden" id="sitesFormAction" name="_eventId" value="" />
        <div class="btn-group">
            <a href="#create" id="createSite" class="btn sitesAction">
                <i class="icon-plus"></i>
                <fmt:message key="serverSettings.manageWebProjects.add"/>
            </a>
        </div>
    </fieldset>

</form>
