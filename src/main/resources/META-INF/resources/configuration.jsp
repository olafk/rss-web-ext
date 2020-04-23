<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */
--%>

<%@ include file="/init.jsp" %>

<liferay-portlet:actionURL portletConfiguration="<%= true %>" var="configurationActionURL" />
<liferay-portlet:renderURL portletConfiguration="<%= true %>" var="configurationRenderURL" />

<liferay-frontend:edit-form
	action="<%= configurationActionURL %>"
	method="post"
	name="fm"
>
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
	<aui:input name="redirect" type="hidden" value="<%= configurationRenderURL %>" />

	<liferay-frontend:edit-form-body>
		<liferay-ui:error exception="<%= ValidatorException.class %>">
			<liferay-ui:message key="the-following-are-invalid-urls" />

			<%
			ValidatorException ve = (ValidatorException)errorException;

			Enumeration enu = ve.getFailedKeys();

			while (enu.hasMoreElements()) {
				String url = (String)enu.nextElement();
			%>

				<strong><%= HtmlUtil.escape(url) %></strong><%= enu.hasMoreElements() ? ", " : "." %>

			<%
			}
			%>

		</liferay-ui:error>

		<liferay-frontend:fieldset-group>
			<liferay-frontend:fieldset
				collapsed="<%= false %>"
				collapsible="<%= true %>"
				label="display-settings"
			>
				<div class="display-template">
					<liferay-ddm:template-selector
						className="<%= RSSFeed.class.getName() %>"
						displayStyle="<%= rssPortletInstanceConfiguration.displayStyle() %>"
						displayStyleGroupId="<%= rssDisplayContext.getDisplayStyleGroupId() %>"
						label="display-template"
						refreshURL="<%= configurationRenderURL.toString() %>"
						showEmptyOption="<%= true %>"
					/>
				</div>

				<aui:input name="preferences--showFeedTitle--" type="toggle-switch" value="<%= rssPortletInstanceConfiguration.showFeedTitle() %>" />

				<aui:input name="preferences--showFeedPublishedDate--" type="toggle-switch" value="<%= rssPortletInstanceConfiguration.showFeedPublishedDate() %>" />

				<aui:input name="preferences--showFeedDescription--" type="toggle-switch" value="<%= rssPortletInstanceConfiguration.showFeedDescription() %>" />

				<%
				String taglibShowFeedImageOnClick = "if (this.checked) {document." + renderResponse.getNamespace() + "fm." + renderResponse.getNamespace() + "feedImageAlignment.disabled = '';} else {document." + renderResponse.getNamespace() + "fm." + renderResponse.getNamespace() + "feedImageAlignment.disabled = 'disabled';}";
				%>

				<aui:input name="preferences--showFeedImage--" onClick="<%= taglibShowFeedImageOnClick %>" type="toggle-switch" value="<%= rssPortletInstanceConfiguration.showFeedImage() %>" />

				<aui:input name="preferences--showFeedItemAuthor--" type="toggle-switch" value="<%= rssPortletInstanceConfiguration.showFeedItemAuthor() %>" />

				<aui:input name="preferences--showEntryDescription--" type="toggle-switch" value="<%= rssPortletInstanceConfiguration.showEntryDescription() %>" helpMessage="show-entry-description-description"/>

				<aui:select label="num-of-entries-per-feed" name="preferences--entriesPerFeed--">

					<%
					for (int i = 1; i < 10; i++) {
					%>

						<aui:option label="<%= i %>" selected="<%= i == rssPortletInstanceConfiguration.entriesPerFeed() %>" />

					<%
					}
					%>

				</aui:select>

				<aui:select label="num-of-expanded-entries-per-feed" name="preferences--expandedEntriesPerFeed--">

					<%
					for (int i = 0; i < 10; i++) {
					%>

						<aui:option label="<%= i %>" selected="<%= i == rssPortletInstanceConfiguration.expandedEntriesPerFeed() %>" />

					<%
					}
					%>

				</aui:select>

				<aui:select disabled="<%= !rssPortletInstanceConfiguration.showFeedImage() %>" name="preferences--feedImageAlignment--">
					<aui:option label="left" selected='<%= rssPortletInstanceConfiguration.feedImageAlignment().equals("left") %>' />
					<aui:option label="right" selected='<%= rssPortletInstanceConfiguration.feedImageAlignment().equals("right") %>' />
				</aui:select>
			</liferay-frontend:fieldset>

			<liferay-frontend:fieldset
				collapsed="<%= true %>"
				collapsible="<%= true %>"
				label="feeds"
			>
				<div id="<portlet:namespace />subscriptions">

					<%
					String[] urls = rssPortletInstanceConfiguration.urls();

					if (urls.length == 0) {
						urls = new String[1];
						urls[0] = StringPool.BLANK;
					}

					String[] titles = rssPortletInstanceConfiguration.titles();

					for (int i = 0; i < urls.length; i++) {
						String title = StringPool.BLANK;

						if (i < titles.length) {
							title = titles[i];
						}
					%>

						<div class="field-row lfr-form-row lfr-form-row-inline">
							<div class="row-fields">
								<aui:input cssClass="lfr-input-text-container" label="title" name='<%= "title" + i %>' value="<%= title %>" />

								<aui:input cssClass="lfr-input-text-container" label="url" name='<%= "url" + i %>' value="<%= urls[i] %>" />
							</div>
						</div>

					<%
					}
					%>

				</div>
			</liferay-frontend:fieldset>
		</liferay-frontend:fieldset-group>
	</liferay-frontend:edit-form-body>

	<liferay-frontend:edit-form-footer>
		<aui:button type="submit" />

		<aui:button type="cancel" />
	</liferay-frontend:edit-form-footer>
</liferay-frontend:edit-form>

<aui:script use="liferay-auto-fields">
	new Liferay.AutoFields(
		{
			contentBox: '#<portlet:namespace />subscriptions',
			fieldIndexes: '<portlet:namespace />subscriptionIndexes',
			namespace: '<portlet:namespace />',
			sortable: true,
			sortableHandle: '.field-row'
		}
	).render();
</aui:script>