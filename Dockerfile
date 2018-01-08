FROM debian:buster

# Maintainer
MAINTAINER Richard Casar<caricsvk@gmail.com>

# Set environment variables and default password for user 'admin'
ENV GLASSFISH_PKG=latest-glassfish.zip \
    GLASSFISH_URL=http://download.oracle.com/glassfish/5.0/nightly/latest-glassfish.zip \
    GLASSFISH_HOME=/glassfish5 \
    PATH=$PATH:/glassfish5/bin \
    JAVA_HOME=/usr/lib/jvm/default-java \
    DEBIAN_FRONTEND=noninteractive


# Install packages, download and extract GlassFish
# Setup password file
# Enable DAS
RUN bash -c 'debconf-set-selections <<< "mariadb-server mysql-server/root_password password root"'
RUN bash -c 'debconf-set-selections <<< "mariadb-server mysql-server/root_password_again password root"'
RUN apt-get update && apt-get install -y unzip curl openjdk-8-jre-headless mariadb-server && \
    curl -O $GLASSFISH_URL && \
    unzip -o $GLASSFISH_PKG && \
    rm -f $GLASSFISH_PKG && \
    /etc/init.d/mysql start

COPY shiftplanner/target/shiftplanner.war /
COPY docker-files/h2-1.4.196.jar /glassfish5/glassfish/domains/domain1/lib/ext/
COPY docker-files/docker-entrypoint.sh /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]

# Ports being exposed
EXPOSE 8080

# Start asadmin console and the domain
CMD ["asadmin", "start-domain", "-v"]
