FROM base-system:latest

ENV DEBIAN_FRONTEND=noninteractive

# java
RUN apt-get update -y
RUN apt-get install -y --no-install-recommends default-jre default-jdk
RUN cat /etc/passwd

# sql
RUN apt-get install -y --no-install-recommends mysql-server

# tomat
RUN wget -O tomcatSetup.zip https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.68/bin/apache-tomcat-9.0.68.zip
RUN unzip tomcatSetup.zip
RUN mv apache* /home/student/tomcat
RUN rm tomcatSetup.zip
RUN chmod +x /home/student/tomcat/bin/catalina.sh
RUN chown -R student:student /home/student/

COPY menu.xml /var/lib/openbox/debian-menu.xml

CMD ["sh", "-c", "chown student:student /home/student/studentData /dev/stdout && exec gosu student supervisord"]
