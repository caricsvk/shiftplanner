#!/bin/bash
# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
# 
# Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
if [[ -z $ADMIN_PASSWORD ]]; then
	ADMIN_PASSWORD=$(date| md5sum | fold -w 8 | head -n 1)
	echo "##########GENERATED ADMIN PASSWORD: $ADMIN_PASSWORD  ##########"
fi
echo "AS_ADMIN_PASSWORD=" > /tmp/glassfishpwd
echo "AS_ADMIN_NEWPASSWORD=${ADMIN_PASSWORD}" >> /tmp/glassfishpwd
asadmin --user=admin --passwordfile=/tmp/glassfishpwd change-admin-password --domain_name domain1
asadmin start-domain

echo "domain started"

echo "AS_ADMIN_PASSWORD=${ADMIN_PASSWORD}" > /tmp/glassfishpwd

asadmin --user=admin --passwordfile=/tmp/glassfishpwd create-jdbc-connection-pool \
	--driverclassname org.h2.Driver \
	--datasourceclassname org.h2.jdbcx.JdbcDataSource \
	--restype javax.sql.DataSource \
	--property url="jdbc\:h2\:file\:~/shiftplanner.h2.db":serverName=localhost:databaseName=shiftplanner.h2.db:connectionAttributes=\;DB_CLOSE\\=FALSE H2Pool

asadmin --user=admin --passwordfile=/tmp/glassfishpwd create-jdbc-resource --connectionpoolid H2Pool jdbc/h2

echo "jdbc pool & resource created"

asadmin --user=admin --passwordfile=/tmp/glassfishpwd deploy --contextroot "/" /shiftplanner.war

echo "app deployed"

asadmin --user=admin --passwordfile=/tmp/glassfishpwd enable-secure-admin

asadmin --user=admin stop-domain
rm /tmp/glassfishpwd
exec "$@"
