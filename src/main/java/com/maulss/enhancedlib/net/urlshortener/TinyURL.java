/*
 * EnhancedLib
 * 
 * Created on 06 June 2015 at 7:52 PM.
 */

package com.maulss.enhancedlib.net.urlshortener;

import com.google.common.base.Strings;
import com.maulss.enhancedlib.service.ServiceExecutor;
import com.maulss.enhancedlib.net.http.HttpRequest;

import java.net.URL;

public final class TinyURL extends URLShortener {

	private static final String URL = "http://tinyurl.com/create.php?url=";

	private String alias;

	TinyURL(String longUrl) {
		super(longUrl);
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Override
	public URL process() throws Exception {
		String result = longUrl;

		String alias = Strings.isNullOrEmpty(TinyURL.this.alias) ? "" : "&alias=" + TinyURL.this.alias;
		URL shortener = HttpRequest.url(URL + longUrl + alias);

		String content;

		try (HttpRequest http = HttpRequest.post(shortener)) {
			content = http
					.bodyForm(ServiceExecutor.mozillaForm())
					.execute()
					.expectResponseCode(200)
					.returnContent()
					.asString("UTF-8")
					.trim();
		}

		for (String line : content.split("\n")) {
			if (line.contains("[<a href=\"")) {
				result = line.substring(
						line.indexOf("[<a href=\"") + 10,
						line.indexOf("\" ")
				);
				break;
			}
		}

		return HttpRequest.url(result);
	}
}