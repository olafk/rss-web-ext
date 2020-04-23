package com.liferay.rss.web.internal.util;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.sanitizer.Sanitizer;
import com.liferay.portal.kernel.sanitizer.SanitizerException;
import com.liferay.portal.kernel.sanitizer.SanitizerUtil;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.rss.util.RSSUtil;
import com.liferay.rss.web.internal.configuration.RSSPortletInstanceConfiguration;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

import java.util.List;
import java.util.Objects;

/**
 * @author Eudaldo Alonso
 */
public class RSSFeedEntry {

	public RSSFeedEntry(
		RSSFeed rssFeed, SyndEntry syndEntry, ThemeDisplay themeDisplay) {

		_rssFeed = rssFeed;
		_syndEntry = syndEntry;
		_themeDisplay = themeDisplay;

		@SuppressWarnings("unchecked")
		List<SyndEnclosure> syndEnclosures = syndEntry.getEnclosures();

		String syndEnclosureLink = StringPool.BLANK;
		String syndEnclosureLinkTitle = syndEntry.getTitle();

		for (SyndEnclosure syndEnclosure : syndEnclosures) {
			if (Validator.isNotNull(syndEnclosure.getUrl())) {
				syndEnclosureLink = syndEnclosure.getUrl();

				int pos = syndEnclosureLink.lastIndexOf(
					StringPool.FORWARD_SLASH);

				if (pos > -1) {
					syndEnclosureLinkTitle = syndEnclosureLink.substring(
						pos + 1);
				}
				else {
					syndEnclosureLinkTitle = syndEnclosureLink;
				}

				break;
			}
		}

		_syndEnclosureLink = syndEnclosureLink;
		_syndEnclosureLinkTitle = syndEnclosureLinkTitle;

		String syndEntryLink = syndEntry.getLink();

		if (Validator.isNotNull(syndEntryLink) &&
			!HttpUtil.hasDomain(syndEntryLink)) {

			syndEntryLink = rssFeed.getBaseURL() + syndEntryLink;
		}

		_syndEntryLink = syndEntryLink;
	}

	public String getSanitizedContent() {
		PortletDisplay portletDisplay = _themeDisplay.getPortletDisplay();

		boolean displayEntryDescription = true;

		try {
			RSSPortletInstanceConfiguration rssPortletInstanceConfiguration =
				portletDisplay.getPortletInstanceConfiguration(
					RSSPortletInstanceConfiguration.class);
			displayEntryDescription = rssPortletInstanceConfiguration.showEntryDescription();
		} catch (ConfigurationException e) {
			_log.error(e);
		}
		
		String baseURL = _rssFeed.getBaseURL();
		SyndFeed syndFeed = _rssFeed.getSyndFeed();

		List<SyndContent> syndContents = getSyndContents();
		if(!displayEntryDescription) {
			syndContents = getFullSyndContents(); // that's what this ext is for
		}
		
		StringBundler sb = new StringBundler(syndContents.size());

		for (SyndContent syndContent : syndContents) {
			if ((syndContent == null) ||
				Validator.isNull(syndContent.getValue())) {

				continue;
			}

			String sanitizedValue = StringPool.BLANK;

			String feedType = syndFeed.getFeedType();

			String type = syndContent.getType();

			if (type == null) {
				type = "text/plain";
			}

			if (Objects.equals(RSSUtil.getFormatType(feedType), RSSUtil.ATOM) &&
				(type.equals("html") || type.equals("xhtml"))) {

				sanitizedValue = _sanitize(syndContent.getValue(), baseURL);
			}
			else if (Objects.equals(
						RSSUtil.getFormatType(feedType), RSSUtil.RSS) &&
					 (type.equals("text/html") || type.equals("text/xhtml") ||
					  type.equals("html"))) {

				sanitizedValue = _sanitize(syndContent.getValue(), baseURL);
			}
			else {
				sanitizedValue = HtmlUtil.escape(syndContent.getValue());
			}

			sb.append(sanitizedValue);
		}

		return sb.toString();
	}

	public String getSyndEnclosureLink() {
		return _syndEnclosureLink;
	}

	public String getSyndEnclosureLinkTitle() {
		return _syndEnclosureLinkTitle;
	}

	public SyndEntry getSyndEntry() {
		return _syndEntry;
	}

	public String getSyndEntryLink() {
		return _syndEntryLink;
	}

	@SuppressWarnings("unchecked")
	protected List<SyndContent> getSyndContents() {
		SyndContent syndContent = _syndEntry.getDescription();

		if (syndContent == null) {
			return _syndEntry.getContents();
		}

		return ListUtil.toList(syndContent);
	}
	
	@SuppressWarnings("unchecked")
	protected List<SyndContent> getFullSyndContents() {
		return _syndEntry.getContents();
	}
	
	private String _sanitize(String value, String baseURL) {
		value = StringUtil.replace(
			value, new String[] {"src=\"/", "href=\"/"},
			new String[] {"src=\"" + baseURL + "/", "href=\"" + baseURL + "/"});

		try {
			value = SanitizerUtil.sanitize(
				_themeDisplay.getCompanyGroupId(),
				_themeDisplay.getScopeGroupId(), _themeDisplay.getUserId(),
				null, 0, ContentTypes.TEXT_HTML, Sanitizer.MODE_XSS, value,
				null);
		}
		catch (SanitizerException se) {

			// LPS-52675

			if (_log.isDebugEnabled()) {
				_log.debug(se, se);
			}
		}

		return value;
	}

	private static final Log _log = LogFactoryUtil.getLog(RSSFeedEntry.class);

	private final RSSFeed _rssFeed;
	private final String _syndEnclosureLink;
	private final String _syndEnclosureLinkTitle;
	private final SyndEntry _syndEntry;
	private final String _syndEntryLink;
	private final ThemeDisplay _themeDisplay;

}