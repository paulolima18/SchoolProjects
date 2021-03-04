REQUIREMENTS: JAVA JDK 8 - APPLET VIEWER NOT AVAILABLE ON FURTHER VERSIONS!

IF MULTIPLE JAVA VERSIONS ARE AVAILABLE, ADAPT/ADD THE FOLLOWING SYSTEM'S VARIABLES FOR JDK FILE PATH (TESTED ON WINDOWS):
PATH: C:\Program Files\Java\jdk1.8.0_152\bin
JAVA_HOME: C:\Program Files\Java\jdk1.8.0_152

(LINUX)
1. find /usr/lib/jvm/java-1.x.x-openjdk
2. sudo nano /etc/profile
3. add lines:
export JAVA_HOME="path that you found"
export PATH=$JAVA_HOME/bin:$PATH
4. source /etc/profile

(MAC)
1. vim .bash_profile 
2. export JAVA_HOME=$(/usr/libexec/java_home)
3. source .bash_profile
4. echo $JAVA_HOME

IF YOU ARE USING APPLETVIEWER TO RUN THE APPLET, YOU SHOULD USE THE COMMAND "APPLETVIEWER SWINGAPPLET.JAVA" IN THE DIRECTORY WHERE YOU HAVE EXTRACTED THE SOURCE CODE TO.