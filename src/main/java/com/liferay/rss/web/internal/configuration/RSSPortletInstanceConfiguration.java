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

package com.liferay.rss.web.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Juergen Kappler
 */
@ExtendedObjectClassDefinition(
	category = "rss",
	scope = ExtendedObjectClassDefinition.Scope.PORTLET_INSTANCE
)
@Meta.OCD(
	id = "com.liferay.rss.web.internal.configuration.RSSPortletInstanceConfiguration",
	localization = "content/Language",
	name = "rss-portlet-instance-configuration-name"
)
public interface RSSPortletInstanceConfiguration {

	@Meta.AD(deflt = "0", name = "display-style-group-id", required = false)
	public long displayStyleGroupId();

	/**
	 * Set a DDM template ID that starts with the prefix "ddmTemplate_" (i.e.
	 * ddmTemplate_rss-navigation-ftl) to use as the display style.
	 */
	@Meta.AD(name = "display-style", required = false)
	public String displayStyle();

	@Meta.AD(deflt = "true", name = "show-feed-title", required = false)
	public boolean showFeedTitle();

	@Meta.AD(
		deflt = "true", name = "show-feed-published-date", required = false
	)
	public boolean showFeedPublishedDate();

	@Meta.AD(deflt = "true", name = "show-feed-description", required = false)
	public boolean showFeedDescription();

	@Meta.AD(deflt = "true", name = "show-feed-image", required = false)
	public boolean showFeedImage();

	@Meta.AD(deflt = "true", name = "show-feed-item-author", required = false)
	public boolean showFeedItemAuthor();

	// That's what this ext is for
	
	@Meta.AD(deflt = "true", name = "show-entry-description", description="show-entry-description-description", required = false)
	public boolean showEntryDescription();

	@Meta.AD(deflt = "4", name = "entries-per-feed", required = false)
	public int entriesPerFeed();

	@Meta.AD(deflt = "8", name = "expanded-entries-per-feed", required = false)
	public int expandedEntriesPerFeed();

	@Meta.AD(deflt = "right", name = "feed-image-alignment", required = false)
	public String feedImageAlignment();

	@Meta.AD(name = "titles", required = false)
	public String[] titles();

	@Meta.AD(name = "urls", required = false)
	public String[] urls();

}
