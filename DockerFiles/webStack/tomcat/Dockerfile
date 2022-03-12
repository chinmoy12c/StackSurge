FROM base-system:latest

# java
RUN apt-get update -y && \
    apt-get install -y --no-install-recommends default-jre default-jdk && \
    rm -rf /var/lib/apt/lists

# sql
RUN apt-get update -y && \
    apt-get install -y --no-install-recommends mysql-server && \
    rm -rf /var/lib/apt/lists

# tomat
RUN wget -O tomcatSetup.zip https://dlcdn.apache.org/tomcat/tomcat-10/v10.0.16/bin/apache-tomcat-10.0.16.zip
RUN unzip tomcatSetup.zip
RUN mv apache* /home/student/tomcat
RUN rm tomcatSetup.zip
RUN chmod +x /home/student/tomcat/bin/catalina.sh
RUN chown -R student:student /home/student/

COPY --from=base-system:latest /usr/local/bin/easy-novnc /usr/local/bin
COPY menu.xml /var/lib/openbox/debian-menu.xml
COPY supervisord.conf /etc/
EXPOSE 6000

CMD ["sh", "-c", "chown student:student /home/student/studentData /dev/stdout && exec gosu student supervisord"]
