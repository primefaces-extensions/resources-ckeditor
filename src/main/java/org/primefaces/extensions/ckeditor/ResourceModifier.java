/*
 * Copyright 2014 PrimeFaces Extensions.
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

package org.primefaces.extensions.ckeditor;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class ResourceModifier
{    
    private static final String ROOT_PATH = "/home/tandraschko/NetBeansProjects/pfe/resources-ckeditor/";
    
    private final static String[] SKINS = { "moono", "kama" };

    public static void main(String[] args) throws IOException
    {
        System.err.println("######## Modify skin styles....");
        
        for (String skin : SKINS)
        {
            System.err.println("#### Modify skin '" + skin + "'");
            
            File skinPath = new File(ROOT_PATH + "src/main/resources/META-INF/resources/primefaces-extensions/ckeditor/skins/" + skin);
            
            for (File file : skinPath.listFiles())
            {
                if (!file.getName().endsWith(".css"))
                {
                    continue;
                }
            
                System.err.println("## Modify file '" + file.getName() + "'");
                
                String fileContent = FileUtils.readFileToString(file);
                
                fileContent = fileContent.replaceAll(
                        "url\\(icons.png\\)",
                        "url\\(\"#{resource['primefaces-extensions:ckeditor/skins/" + skin + "/icons.png']}\"\\)");
                
                fileContent = fileContent.replaceAll(
                        "url\\(icons_hidpi.png\\)",
                        "url\\(\"#{resource['primefaces-extensions:ckeditor/skins/" + skin + "/icons_hidpi.png']}\"\\)");
                
                fileContent = fileContent.replaceAll(
                        "url\\(images/close.png\\)",
                        "url\\(\"#{resource['primefaces-extensions:ckeditor/skins/" + skin + "/images/close.png']}\"\\)");
                
                fileContent = fileContent.replaceAll(
                        "url\\(images/hidpi/close.png\\)",
                        "url\\(\"#{resource['primefaces-extensions:ckeditor/skins/" + skin + "/images/hidpi/close.png']}\"\\)");
                
                fileContent = fileContent.replaceAll(
                        "url\\(images/refresh.png\\)",
                        "url\\(\"#{resource['primefaces-extensions:ckeditor/skins/" + skin + "/images/refresh.png']}\"\\)");
                
                fileContent = fileContent.replaceAll(
                        "url\\(images/hidpi/refresh.png\\)",
                        "url\\(\"#{resource['primefaces-extensions:ckeditor/skins/" + skin + "/images/hidpi/refresh.png']}\"\\)");
                
                fileContent = fileContent.replaceAll(
                        "url\\(images/arrow.png\\)",
                        "url\\(\"#{resource['primefaces-extensions:ckeditor/skins/" + skin + "/images/arrow.png']}\"\\)");
                
                fileContent = fileContent.replaceAll(
                        "url\\(images/lock.png\\)",
                        "url\\(\"#{resource['primefaces-extensions:ckeditor/skins/" + skin + "/images/lock.png']}\"\\)");
                
                fileContent = fileContent.replaceAll(
                        "url\\(images/hidpi/lock.png\\)",
                        "url\\(\"#{resource['primefaces-extensions:ckeditor/skins/" + skin + "/images/hidpi/lock.png']}\"\\)");

                FileUtils.writeStringToFile(file, fileContent);
            }
            
        }
    }   
}
