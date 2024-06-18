FROM tomcat:10.1.18

COPY ./server.xml /usr/local/tomcat/conf/server.xml

COPY ./target/project-acdc-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/