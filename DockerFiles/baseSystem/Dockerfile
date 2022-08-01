FROM ubuntu

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install --fix-missing

# Aptitude
RUN apt-get install -y aptitude

# Basic software
RUN aptitude install -y tigervnc-standalone-server openbox tint2 supervisor novnc lxterminal gedit nitrogen

# Basic utility tools
RUN aptitude install -y net-tools git software-properties-common nano nautilus curl wget gzip unzip zip ca-certificates tar sudo gosu debian-keyring debian-archive-keyring apt-transport-https

# Firefox
RUN add-apt-repository ppa:mozillateam/ppa
RUN aptitude install -y firefox-esr

# Caddy
RUN curl -1sLf 'https://dl.cloudsmith.io/public/caddy/stable/gpg.key' | gpg --dearmor -o /usr/share/keyrings/caddy-stable-archive-keyring.gpg
RUN curl -1sLf 'https://dl.cloudsmith.io/public/caddy/stable/debian.deb.txt' | tee /etc/apt/sources.list.d/caddy-stable.list
RUN aptitude update && aptitude -y install caddy

# Remove apt cache
RUN rm -rf /var/lib/apt/lists

# Student account
RUN groupadd --gid 1000 student && \
    useradd --home-dir /home/student/ --shell /bin/bash --uid 1000 --gid 1000 student && \
    mkdir /home/student
VOLUME /home/student/studentData
RUN chown -R student:student /home/student/ /dev/stdout
RUN echo -n "student\nstudent" | passwd student
RUN usermod -aG sudo student

# Nitrogen wallpaper setup
RUN mkdir /home/student/wallpapers
COPY ./wallpapers/bg-saved.cfg /home/student/.config/nitrogen/bg-saved.cfg
COPY ./wallpapers/nitrogen.cfg /home/student/.config/nitrogen/nitrogen.cfg
COPY ./wallpapers/ /home/student/wallpapers/
RUN chown -R student:student /home/student/ /dev/stdout /dev/fd/1

# Configuration
COPY supervisord.conf /etc/
COPY menu.xml /var/lib/openbox/debian-menu.xml
COPY Caddyfile /etc/

CMD [ "sh", "-c", "chown student:student /home/student/studentData /dev/stdout && exec gosu student supervisord" ]
