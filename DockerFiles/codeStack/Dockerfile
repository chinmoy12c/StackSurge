FROM base-system:latest

# java
RUN apt-get update -y
RUN apt-get install -y --no-install-recommends default-jre default-jdk

# gdb, C, C++
RUN apt-get install -y gdb build-essential

# vscode
RUN wget -qO- https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor > packages.microsoft.gpg
RUN install -D -o root -g root -m 644 packages.microsoft.gpg /etc/apt/keyrings
RUN sh -c 'echo "deb [arch=amd64,arm64,armhf signed-by=/etc/apt/keyrings/packages.microsoft.gpg] https://packages.microsoft.com/repos/code stable main" > /etc/apt/sources.list.d/vscode.list'
RUN rm -f packages.microsoft.gpg
RUN apt install -y apt-transport-https
RUN apt update
RUN apt install -y code
RUN rm -rf /var/lib/apt/lists

COPY menu.xml /var/lib/openbox/debian-menu.xml

CMD [ "sh", "-c", "chown student:student /home/student/studentData /dev/stdout && exec gosu student supervisord" ]
