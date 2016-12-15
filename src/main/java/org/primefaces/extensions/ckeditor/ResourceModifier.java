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

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Steps to update:
 * - Delete all files in the src/main/resources/META-INF/resources/primefaces-extensions/ckeditor/ directory
 * - extract new ckeditor files
 * - remove samples dir
 * - Add an empty skin.js file to the skin directory which does not contain a default skin.js
 */
public class ResourceModifier {

	private static final String PROJECT_DIRECTORY = System.getProperty("user.dir");

	public static void main(String[] args) throws IOException {

        String resourcesDirectory = PROJECT_DIRECTORY + "/src/main/resources/META-INF/resources/primefaces-extensions/";

        System.err.println("######## Modify skin styles....");

        File skinsPath = new File(PROJECT_DIRECTORY + "/src/main/resources/META-INF/resources/primefaces-extensions/ckeditor/skins/");
        
		for (String skin : skinsPath.list()) {
			System.err.println("#### Modify skin '" + skin + "'");

			File skinDirectory = new File(resourcesDirectory + "ckeditor/skins/" + skin);
            String relativeSkinPath =  StringUtils.replace(
                        StringUtils.replace(skinDirectory.getPath(), resourcesDirectory, ""),
                        "\\", 
                        "/");

			for (File resourceToModify : skinDirectory.listFiles()) {

                // modify css only
				if (!resourceToModify.getName().endsWith(".css")) {
					continue;
				}

				System.err.println("## Modify file '" + resourceToModify.getName() + "'");

				String fileContent = FileUtils.readFileToString(resourceToModify);
                List<File> allSkinResources = getResourcesList(skinDirectory);

                // loop all possible image references
				for (File resource : allSkinResources) {
                    
                    // we just need to check included images
                    if (resource.getName().endsWith(".css")) {
                        continue;
                    }

					String relativeResourcePath = StringUtils.replace(
                            StringUtils.replace(resource.getPath(), resourcesDirectory, ""),
                            "\\", 
                            "/");
                    String resourceName = StringUtils.replace(
                            resource.getAbsolutePath().replace(skinDirectory.getAbsolutePath(), ""),
                            "\\",
                            "/");

                    if (resourceName.startsWith("/")) {
                        resourceName = resourceName.substring(1);
                    }

					fileContent = fileContent.replaceAll("url\\(" + resourceName + "\\)",
							"url\\(\"#{resource['primefaces-extensions:" + relativeResourcePath + "']}\"\\)");
				}

                // icons.png
                fileContent = fileContent.replaceAll("url\\(icons.png\\?t=GAGE",
                        "url\\(\"#{resource['primefaces-extensions:" + relativeSkinPath + "/icons.png']}&t=GAGE\"");
                
                // icons_hidpi.png
                fileContent = fileContent.replaceAll("url\\(icons_hidpi.png\\?t=GAGE",
                        "url\\(\"#{resource['primefaces-extensions:" + relativeSkinPath + "/icons_hidpi.png']}&t=GAGE\"");
                
                
				FileUtils.writeStringToFile(resourceToModify, fileContent);
			}

			String fileContent = "";
			File file = null;

			// modify smileys plugin to load the smileys via CKEditor.getUrl
			file = new File(PROJECT_DIRECTORY
					+ "/src/main/resources/META-INF/resources/primefaces-extensions/ckeditor/plugins/smiley/dialogs/smiley.js");
			fileContent = FileUtils.readFileToString(file).replaceAll(
					"CKEDITOR.tools.htmlEncode\\(e\\.smiley_path\\+h\\[a\\]\\)",
					"CKEDITOR.tools.htmlEncode\\(CKEDITOR.getUrl\\(e\\.smiley_path\\+h\\[a\\]\\)\\)");
			FileUtils.writeStringToFile(file, fileContent);
			
			// modify copyFormatter plugin to load copyFormatter.css via CKEditor.getUrl
			file = new File(resourcesDirectory
					+ "ckeditor/ckeditor.js");
			fileContent = FileUtils.readFileToString(file).replaceAll("this.path\\+\"styles/copyformatting.css\"", "CKEDITOR.getUrl(this.path\\+\"styles/copyformatting.css\")");

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
