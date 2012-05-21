# Surfnet's Apache Rave Template

This is a minimal WAR overlay project to run Apache Rave at SURFnet.
New features should preferably be generalized and contributed to Apache Rave.

## Building

Building this template requires git and maven 3.0.

```bash
git clone git://github.com/OpenConextApps/OpenConextApps-rave-template.git
cd OpenConextApps-rave-template
mvn clean install
```

## Running locally

After building this project, you can run it locally.
Please perform the following steps in your OpenConextApps-rave-template directory:

```bash
cd coin-rave-portal-design
mvn cargo:start
```

And visit http://localhost:8080/portal/

## Automatic deploy on a server

```bash
cd coin-rave-dist
mvn -Pdist
```

Grab the apache-rave-1.0-SNAPSHOT.tar.gz tarball inside target.
This file contains a tomcat instance which you can install on your server.

## Custom deploy on a server

### Step 1 Setup Tomcat 6.0

At the time of writing 6.0.35 was the latest tomcat 6.0 version.
Please verify if there was not a newer release so you can use that one instead.

```bash
wget http://mirrors.supportex.net/apache/tomcat/tomcat-6/v6.0.35/bin/apache-tomcat-6.0.35.tar.gz
tar xvfz apache-tomcat-6.0.35.tar.gz
cd apache-tomcat-6.0.35
```

#### Copy the web application archives to tomcat.

You can find these in the coin-rave-dist/target/apache-rave-1.0-SNAPSHOT.tar.gz.
The files are called *coin-rave-portal-dist-1.0-SNAPSHOT.war* and *coin-rave-shindig-1.0-SNAPSHOT.war*.

#### Add more memory to Tomcat.

*Create a file* called bin/setenv.sh with the following contents:

```bash
export JAVA_OPTS="$JAVA_OPTS -Xmx512m -XX:MaxPermSize=256m"
```

And make this file executable:

```bash
chmod +x bin/setenv.sh
```

#### Place the properties files

Create a *directory* for the properties files.

```bash
mkdir classpath_properties
```

*Edit* conf/catalina.propeties so that the common.loader also includes the conf/classpath_properties directory.

```bash
common.loader=${catalina.home}/lib,${catalina.home}/lib/*.jar,${catalina.home}/conf/classpath_properties
```

Next, copy the files rave-shindig-container.js, rave.shindig.properties, rave-opensaml.properties and portal.properties to conf/classpath_properties.

Edit all files.

#### Configure the hosts

Add the following host configuration to conf/server.xml:

```xml
<Host name="rave.dev.surfconext.nl" appBase="webapps/rave.dev.surfconext.nl"/>
<Host name="rave-shindig.dev.surfconext.nl" appBase="webapps/rave-shindig.dev.surfconext.nl"/>
```

Create the directories *conf/Catalina*, *conf/Catalina/rave.dev.surfconext.nl* and *conf/Catalina/rave-shindig.dev.surfconext.nl*.

Create a file *conf/Catalina/rave.dev.surfconext.nl/portal.xml*.
Please change the docbase to the correct location.

```xml
<Context path="/portal" docBase="/opt/tomcat/wars/coin-rave-portal-dist-1.0-SNAPSHOT.war" debug="0">
</Context>
```

Create a file *conf/Catalina/rave-shindig.dev.surfconext.nl/ROOT.xml*.
Please change the docbase to the correct location.

```xml
<Context path="" docBase="/opt/tomcat/wars/coin-rave-shindig-1.0-SNAPSHOT.war" debug="0"></Context>
```

#### Start the server

```bash
bin/startup.sh
```





