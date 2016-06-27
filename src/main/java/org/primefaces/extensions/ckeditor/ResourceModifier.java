package org.primefaces.extensions.ckeditor;

/*
 * Copyright 2011-2015 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id$
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class ResourceModifier {
	private static final String ROOT_PATH = System.getProperty("user.dir");

	private final static String[] SKINS = { "Moono_blue", "moonocolor", "kama", "office2013", "icy_orange", };

	public static void main(String[] args) throws IOException {
		System.err.println("######## Modify skin styles....");

		for (String skin : SKINS) {
			System.err.println("#### Modify skin '" + skin + "'");
			String ckeditorPath = ROOT_PATH + "\\src\\main\\resources\\META-INF\\resources\\primefaces-extensions\\";
			File skinPath = new File(
					ROOT_PATH + "/src/main/resources/META-INF/resources/primefaces-extensions/ckeditor/skins/" + skin);

			for (File file : skinPath.listFiles()) {

				if (!file.getName().endsWith(".css")) {
					continue;
				}
				System.err.println("## Modify file '" + file.getName() + "'");

				String fileContent = FileUtils.readFileToString(file);
				for (File resource : getResourcesList(skinPath)) {
					String resourceName = resource.getName();
					String resourceRelativePath = StringUtils
							.replace(StringUtils.replace(resource.getPath(), ckeditorPath, ""), "\\", "/");
					System.out.println(resourceName + "   -    " + resourceRelativePath);

					fileContent = fileContent.replaceAll("url\\(" + resourceName + "\\)",
							"url\\(\"#{resource['primefaces-extensions:" + resourceRelativePath + "']}\"\\)");

					// Pattern regex =
					// Pattern.compile("url\\(icons\\.png\\?t=\\w{7}\\)");
					// Matcher regexMatcher = regex.matcher(fileContent);
					//
					// List<String> allMatches = new ArrayList<String>();
					// while (regexMatcher.find()) {
					// allMatches.add(regexMatcher.group());
					// }
					// System.out.println(allMatches);
					//
					// if (!allMatches.isEmpty()) {
					// System.out.println(allMatches);
					// for (String icon : allMatches) {
					// String value = icon.replace("url(", "").replace(")",
					// "").replace("?", "\\?");
					// String replaceWith =
					// "\"#{resource['primefaces-extensions:" + value + "']}\"";
					//
					// //System.out.println(value + " ###### with :" +
					// replaceWith);
					//
					// fileContent = fileContent.replaceAll(value, replaceWith);
					// }
					// }
					String value = "\\(icons.png\\?t=a35abfe";
					String path = StringUtils.replace(StringUtils.replace(skinPath.getPath(), ckeditorPath, ""), "\\",
							"/");

					String replaceWith = "(\"#{resource['primefaces-extensions:" + path + "/" + "icons.png"
							+ "']}&t=a35abfe\"";

					System.out.println(value + " ###### with : " + replaceWith);

					fileContent = fileContent.replaceAll(value, replaceWith);

				}
				FileUtils.writeStringToFile(file, fileContent);
			}

			String fileContent = "";
			File file = null;

			// modify smileys plugin to load the smileys via CKEditor.getUrl
			file = new File(ROOT_PATH
					+ "/src/main/resources/META-INF/resources/primefaces-extensions/ckeditor/plugins/smiley/dialogs/smiley.js");
			fileContent = FileUtils.readFileToString(file).replaceAll(
					"CKEDITOR.tools.htmlEncode\\(e\\.smiley_path\\+h\\[a\\]\\)",
					"CKEDITOR.tools.htmlEncode\\(CKEDITOR.getUrl\\(e\\.smiley_path\\+h\\[a\\]\\)\\)");
			FileUtils.writeStringToFile(file, fileContent);

		}
	}

	private static List<File> getResourcesList(File file) {
		List<File> result = new ArrayList<File>();
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				result.addAll(getResourcesList(f));
			}
		} else {
			if (file.getName().endsWith(".png") || file.getName().endsWith(".css"))
				result.add(file);
		}
		return result;
	}
}
