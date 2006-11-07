/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2005, 2006 The Sakai Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *      http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.portal.charon.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.portal.charon.PortalRenderContext;
import org.sakaiproject.portal.charon.PortalRenderEngine;
import org.w3c.tidy.Tidy;

/**
 * <p>
 * This class operates as Mock portal to enable testing of a set of templates. It can be used to drive
 * any render engine, but is setup for the default render engine. It is run as a unit test.
 * </p>
 */
public class MockCharonPortal extends HttpServlet
{

	/** Our log (commons). */
	private static Log log = LogFactory.getLog(MockCharonPortal.class);

	private PortalRenderEngine rengine;

	public MockCharonPortal() throws Exception 
	{
		String renderEngineClass = PortalRenderEngine.DEFAULT_RENDER_ENGINE;

		Class c = Class.forName(renderEngineClass);
		rengine = (PortalRenderEngine) c.newInstance();
		rengine.init();
			
	}

	public void doError() throws IOException
	{

		// start the response
		PortalRenderContext rcontext = startPageContext();

		showSession(rcontext);

		showSnoop(rcontext);

		sendResponse(rcontext, "error");
	}

	private void showSnoop(PortalRenderContext rcontext)
	{

		rcontext.put("snoopRequest", "snoopRequest");

		Map m = new HashMap();
		m.put("param1", "configparam1");
		m.put("param1", "configparam1");
		rcontext.put("snoopServletConfigParams", m);
		rcontext.put("snoopRequest", "snoopRequest");
		rcontext.put("snoopRequestHeaders", m);
		rcontext.put("snoopRequestParamsSingle", m);
		rcontext.put("snoopRequestParamsMulti", m);
		rcontext.put("snoopRequestAttr", m);
	}

	public void doGallery() throws IOException
	{
		PortalRenderContext rcontext = startPageContext();

		// the 'little' top area
		includeGalleryNav(rcontext);

		includeWorksite(rcontext);

		includeBottom(rcontext);

		sendResponse(rcontext, "gallery");
	}

	public void doGalleryTabs() throws IOException
	{

		PortalRenderContext rcontext = startPageContext();

		// Remove the logout button from gallery since it is designed to be
		// included within
		// some other application (like a portal) which will want to control
		// logout.

		// includeTabs(out, req, session, siteId, "gallery", true);
		includeTabs(rcontext);

		sendResponse(rcontext, "gallery-tabs");
	}

	public void doTitle() throws IOException
	{
		includeTitle();
	}

	/**
	 * Output the content of the title frame for a tool.
	 */
	protected void includeTitle() throws IOException
	{

		PortalRenderContext rcontext = startPageContext();

		rcontext.put("titleShowResetButton", Boolean.valueOf(true));
		rcontext.put("titleResetActionUrl", "resetActionUrl");
		rcontext.put("titleShowHelpButton", Boolean.valueOf(true));
		rcontext.put("titleHelpActionUrl", "helpActionUrl");

		rcontext.put("titleToolResetNow", Boolean.valueOf(true));

		rcontext.put("titleMainFrameId", "mainFrameId");
		rcontext.put("titleMainFrameUrl", "mainFrameUrl");
		sendResponse(rcontext, "tool-title");
	}

	public void doNavLogin() throws IOException
	{
		// start the response
		PortalRenderContext rcontext = startPageContext();

		includeLogo(rcontext);

		sendResponse(rcontext, "login");
	}

	public void doNavLoginGallery() throws IOException
	{
		// start the response

		PortalRenderContext rcontext = startPageContext();

		includeGalleryLogin(rcontext);
		// end the response
		sendResponse(rcontext, "gallery-login");
	}

	public void doPage() throws IOException
	{
		PortalRenderContext rcontext = startPageContext();

		includePage(rcontext);

		sendResponse(rcontext, "page");
	}

	private PortalRenderContext startPageContext()
	{
		PortalRenderContext rcontext = rengine.newRenderContext(null);
		rcontext.put("pageSkinRepo", "skinRepo");
		rcontext.put("pageSkin", "skin");
		rcontext.put("pageTitle", "Web.escapeHtml(title)");
		rcontext.put("pageScriptPath", "getScriptPath()");
		rcontext.put("pageTop", Boolean.valueOf(true));
		rcontext.put("pageSiteType", "class=\"siteType\" ");
		rcontext.put("toolParamResetState", "PARM_STATE_RESET");
		rcontext.put("sitHelp", "Help");
		rcontext.put("sitReset", "Reset");

		return rcontext;
	}

	public void doSite() throws IOException
	{
		PortalRenderContext rcontext = startPageContext();

		// the 'full' top area
		includeSiteNav(rcontext);

		includeWorksite(rcontext);

		includeBottom(rcontext);

		// end the response
		sendResponse(rcontext, "site");
	}

	public void doSiteTabs() throws IOException
	{
		// start the response
		PortalRenderContext rcontext = startPageContext();

		includeLogo(rcontext);
		includeTabs(rcontext);

		sendResponse(rcontext, "site-tabs");
	}

	public void doWorksite() throws IOException
	{

		PortalRenderContext rcontext = startPageContext();

		includeWorksite(rcontext);

		// end the response
		sendResponse(rcontext, "worksite");
	}

	protected void includeBottom(PortalRenderContext rcontext)
	{

		{
			List l = new ArrayList();
			l.add("bottomnav1");
			l.add("bottomnav2");
			rcontext.put("bottomNav", l);
		}

		rcontext.put("bottomNavSitNewWindow", "site.newwindow");
		{

			List l = new ArrayList();
			Map m = new HashMap();
			m.put("poweredByUrl", "poweredByUrl[i]");
			m.put("poweredByImage", "poweredByImage[i]");
			m.put("poweredByAltText", "poweredByAltText[i]");
			l.add(m);
			rcontext.put("bottomNavPoweredBy", l);

		}
		{
			List l = new ArrayList();
			Map m = new HashMap();
			m.put("poweredByUrl", "http://sakaiproject.org");
			m.put("poweredByImage", "/library/image/sakai_powered.gif");
			m.put("poweredByAltText", "Powered by Sakai");
			l.add(m);
			rcontext.put("bottomNavPoweredBy", l);
		}

		rcontext.put("bottomNavService", "service");
		rcontext.put("bottomNavCopyright", "copyright");
		rcontext.put("bottomNavServiceVersion", "serviceVersion");
		rcontext.put("bottomNavSakaiVersion", "sakaiVersion");
		rcontext.put("bottomNavServer", "server");
	}

	protected void includeGalleryLogin(PortalRenderContext rcontext)
			throws IOException
	{
		includeLogin(rcontext);
	}

	protected void includeGalleryNav(PortalRenderContext rcontext)
			throws IOException
	{
		rcontext.put("galleryHasAccessibilityURL", Boolean.valueOf(true));

		rcontext.put("galleryAccessibilityURL", "accessibilityURL");
		rcontext.put("gallarySitAccessibility", "sit.accessibility");
		rcontext.put("gallarySitJumpcontent", "sit.jumpcontent");
		rcontext.put("gallarySitJumptools", "sit.jumptools");
		rcontext.put("gallarySitJumpworksite", "sit.jumpworksite");
		rcontext.put("gallaryLoggedIn", Boolean.valueOf(true));
		includeTabs(rcontext);

	}

	protected void includeLogo(PortalRenderContext rcontext) throws IOException
	{
		rcontext.put("logoSkin", "skin");
		rcontext.put("logoSkinRepo", "skinRepo");
		rcontext.put("logoSiteType", "siteType");
		rcontext.put("logoSiteClass", "cssClass");
		includeLogin(rcontext);
	}

	protected void includeLogin(PortalRenderContext rcontext)
	{

		rcontext.put("loginTopLogin", Boolean.valueOf(true));
		rcontext.put("loginLogInOutUrl", "logInOutUrl");
		rcontext.put("loginMessage", "message");
		rcontext.put("loginImage1", "image1");
		rcontext.put("image1HasImage1",Boolean.valueOf(true));
		rcontext.put("loginLogInOutUrl2", "logInOutUrl2");
		rcontext.put("loginHasLogInOutUrl2", Boolean.valueOf(true));
		rcontext.put("loginMessage2", "message2");
		rcontext.put("loginImage2", "image2");
		rcontext.put("image1HasImage2",Boolean.valueOf(true));


		rcontext.put("loginPortalPath", "portalPath");
		rcontext.put("loginEidWording", "eidWording");
		rcontext.put("loginPwWording", "pwWording");
		rcontext.put("loginWording", "loginWording");
	}

	protected void includePage(PortalRenderContext rcontext) throws IOException
	{
		// divs to wrap the tools
		rcontext.put("pageWrapperClass", "wrapperClass");
		rcontext.put("pageColumnLayout", "col1of2" );
		{
			List toolList = new ArrayList();
			toolList.add(includeTool());
			toolList.add(includeTool());
			rcontext.put("pageColumn0Tools", toolList);
		}

		rcontext.put("pageTwoColumn", Boolean.valueOf(true));
		{
			List toolList = new ArrayList();
			toolList.add(includeTool());
			rcontext.put("pageColumn1Tools", toolList);
		}
	}

	protected void includePageNav(PortalRenderContext rcontext)
			throws IOException
	{
		rcontext.put("pageNavPublished", Boolean.valueOf(true));
		rcontext.put("pageNavType", "type");
		rcontext.put("pageNavIconUrl", "iconUrl");
		rcontext.put("pageNavSitToolsHead", "sit.toolshead");

		List l = new ArrayList();
		Map m = new HashMap();
		m.put("current", Boolean.valueOf(true));
		m.put("ispopup", Boolean.valueOf(false));
		m.put("pagePopupUrl", "pagePopupUrl");
		m.put("pageIdWeb", "pageId");
		m.put("jsPageTitle", "pageTitleJS");
		m.put("htmlPageTitle", "pageTitleHTML");
		m.put("pagerefUrl", "pagerefUrl");
		l.add(m);
		m.put("current", Boolean.valueOf(false));
		m.put("ispopup", Boolean.valueOf(false));
		m.put("pagePopupUrl", "pagePopupUrl");
		m.put("pageIdWeb", "pageId");
		m.put("jsPageTitle", "pageTitleJS");
		m.put("htmlPageTitle", "pageTitleHTML");
		m.put("pagerefUrl", "pagerefUrl");
		l.add(m);
		rcontext.put("pageNavTools", l);

		rcontext.put("pageNavShowHelp", Boolean.valueOf(true));
		rcontext.put("pageNavHelpUrl", "helpUrl");
		rcontext.put("pageNavSitHelp", "sit.help");

		rcontext.put("pageNavSitPresenceTitle", "sit.presencetitle");
		rcontext.put("pageNavSitPresenceFrameTitle", "sit.presenceiframetit");
		rcontext.put("pageNavShowPresenceLoggedIn", Boolean.valueOf(true));
		rcontext.put("pageNavPresenceUrl", "presenceUrl");
		rcontext.put("pageNavSitContentshead", "sit.contentshead");

	}

	protected void includeSiteNav(PortalRenderContext rcontext)
			throws IOException
	{
		rcontext.put("siteNavHasAccessibilityURL", Boolean.valueOf((true)));
		rcontext.put("siteNavAccessibilityURL", "accessibilityURL");
		rcontext.put("siteNavSitAccessability", "sit.accessibility");
		rcontext.put("siteNavSitJumpContent", "sit.jumpcontent");
		rcontext.put("siteNavSitJumpTools", "sit.jumptools");
		rcontext.put("siteNavSitJumpWorksite", "sit.jumpworksite");

		rcontext.put("siteNavLoggedIn", Boolean.valueOf(true));

		includeLogo(rcontext);
		includeTabs(rcontext);
	}

	protected void includeTabs(PortalRenderContext rcontext) throws IOException
	{

		rcontext.put("tabsCssClass", "cssClass");
		rcontext.put("tabsSitWorksiteHead", "sit.worksiteshead");
		rcontext.put("tabsCurMyWorkspace", Boolean.valueOf(true));
		rcontext.put("tabsSitMyWorkspace", "sit.mywor");
		rcontext.put("tabsSiteUrl", "mySiteUrl");
		rcontext.put("tabsSitWorksite", "sit.worksite");

		List l = new ArrayList();
		{
			Map m = new HashMap();
			m.put("isCurrentSite", Boolean.valueOf(false));
			m.put("siteTitle", "siteTitle");
			m.put("siteUrl", "Web.escapeHtml(s.getTitle())");
			l.add(m);
			l.add(m);

		}
		rcontext.put("tabsSites", l);

		rcontext.put("tabsHasExtraTitle", Boolean.valueOf(true));

		rcontext.put("tabsExtraTitle", "Web.escapeHtml(extraTitle)");
		rcontext.put("tabsMoreSitesShow", Boolean.valueOf(true));
		rcontext.put("tabsSitSelectMessage", "sit.selectmessage");
		rcontext.put("tabsSitMode", "sit.more");
		{
			Map m = new HashMap();

			m.put("siteTitle", "Web.escapeHtml(s.getTitle())");
			m.put("siteUrl", "siteUrl");
			l.add(m);
			l.add(m);
		}
		rcontext.put("tabsMoreSites", l);

		rcontext.put("tabsAddLogout", Boolean.valueOf(true));
		rcontext.put("tabsLogoutUrl", "logoutUrl");
		rcontext.put("tabsSitLog", "sit.log");
	}

	protected Map includeTool() throws IOException
	{
		Map toolMap = new HashMap();
		toolMap.put("toolUrl", "toolUrl");
		toolMap.put("toolPlacementIDJS",
				"Main_"+System.currentTimeMillis());
		toolMap.put("toolTitle", "titleString");
		toolMap.put("toolShowResetButton", Boolean.valueOf(true));
		toolMap.put("toolShowHelpButton", Boolean.valueOf(true));
		toolMap.put("toolHelpActionUrl", "helpActionUrl");
		return toolMap;
	}

	protected void includeWorksite(PortalRenderContext rcontext)
			throws IOException
	{
		// add the page navigation with presence
		includePageNav(rcontext);

		// add the page
		includePage(rcontext);
	}

	/**
	 * Output some session information
	 * 
	 * @param rcontext
	 *        The print writer
	 * @param html
	 *        If true, output in HTML, else in text.
	 */
	protected void showSession(PortalRenderContext rcontext)
	{
		rcontext.put("sessionSession", "s");
		rcontext.put("sessionToolSession", "ts");
	}

	protected void sendResponse(PortalRenderContext rcontext, String template)
			throws IOException
	{
		// get the writer
		FileWriter f = new FileWriter(template + ".html");
		
		log.info("Context Dump is "+rcontext.dump())
		;

		try
		{
			log.info("Rendering "+rcontext+" to "+template);
			rengine.render(template, rcontext, f);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Failed to render template ", e);
		}
		f.close();
		
		Tidy t = new Tidy();
		FileOutputStream fo = new FileOutputStream(template+".html.tidy.txt");
		t.setIndentContent(true);
		t.setXHTML(true);
		t.parse(new FileInputStream(template+".html"),fo);

	}

}
