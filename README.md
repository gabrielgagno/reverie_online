# reverie_online
The official repository of Reverie Online, the automated task scheduler

===========================
# INSTALLATION INSTRUCTIONS
===========================

---------------------------
## INSTALL JAVA
---------------------------
1. From the CD root, go to Application/prerequisites.
2. Click on the Java installer. Follow the steps to install.
3. Once done, click on Computer (Windows 7) or This PC (Windows 8).
4. Right click on anywhere and click on properties.
5. On the left-hand side of the Properties window, click on "Advanced System Settings".
6. Click "environment variables."
7. On System Variables, find "PATH".
8. Open another window called and navigate to "C:\Program Files\Java\jdk<version>\bin" and copy the address on the address bar.
9. Back on the System Variables box, click on "PATH" and edit it by appending what you just copied to the list (don't forget to add ";" first).
10. To test if it's working, open a command prompt and type "javac -version".

--------------------------
## INSTALL XAMPP
--------------------------
1. From the CD root, go to Application/prerequisites.
2. Click on the xampp installer. Continue clicking next and leave defaults on (unless you have other credentials as instructed by your superior).
3. Open the XAMPP Control Panel.
4. Activate MySQL and Tomcat ONLY.

--------------------------
## DEPLOY REVERIE
--------------------------
1. Go to localhost:8080/ (or you can change the port number if you didn't leave the defaults on). The Tomcat home page shall appear.
2. Click on "Manager App". Enter the credentials.
3. Scroll down to "Deploy" section, and find the heading "WAR File to deploy".
4. Click "Choose File" and choose the file "reverie.war" in Application/WAR File.
5. Click on deploy.
6. To test the application, try going to localhost:8080/reverie.

------------------------
## INSTALL GRAILS (for developers only)
------------------------
