/*
 * MaulssLib
 * 
 * Created on 08 February 2015 at 3:44 PM.
 */

package com.maulss.enhancedlib.net.paster;

import com.maulss.enhancedlib.collect.EnhancedMap;
import com.sun.istack.internal.NotNull;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Hastebin extends Paster {

	private static final String URL = "http://www.hastebin.com/";

	Hastebin(@NotNull String content) {
		super(content);
	}

	@Override
	protected URL process() throws Exception {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(URL + "documents");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);

			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(getContent());
			wr.flush();
			wr.close();

			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			Map<String, Object> jsonMap = EnhancedMap.fromJson(rd.readLine());
			return new URL(URL + jsonMap.get("key"));
		} finally {
			if (connection != null) connection.disconnect();
		}
	}
}