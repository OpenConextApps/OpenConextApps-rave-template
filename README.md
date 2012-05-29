# Surfnet's Apache Rave Template

This is a minimal WAR overlay project to run Apache Rave at SURFnet.
New features should preferably be generalized and contributed to Apache Rave.

## Building

Building this template requires git and maven 3.0.

```bash
git clone git://github.com/OpenConextApps/OpenConextApps-rave-template.git
cd OpenConextApps-rave-template
mvn -P dist clean install
```

## Running locally

After building this project, you can run it locally.
Please perform the following steps in your OpenConextApps-rave-template directory:

```bash
cd coin-rave-portal-design
mvn cargo:start
```

And visit http://localhost:8080/portal/

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

You can find these in the *coin-rave-dist/target/coin-rave-dist-1.0-SNAPSHOT-bin.tar.gz*.
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
mkdir conf/classpath_properties
```

*Edit* conf/catalina.propeties so that the common.loader also includes the conf/classpath_properties directory.

```bash
common.loader=${catalina.home}/lib,${catalina.home}/lib/*.jar,${catalina.home}/conf/classpath_properties
```

Next, copy the files rave-shindig-container.js, rave.shindig.properties, rave-opensaml.properties and portal.properties to conf/classpath_properties.
You can find these files in coin-rave-dist/src/main/dist/conf/classpath_properties.
Edit all files so they match your server configuration, find / replace every rave.example.com and rave-shindig.example.com and replace them with your own hostname.

#### Add the shared libraries

*Edit* conf/catalina.properties so that the *shared.loader* loads the *shared/lib* directory.

```bash
shared.loader=${catalina.home}/shared/lib,${catalina.home}/shared/lib/*.jar
```

Next, copy the following three libraries to shared/lib:

* activation-1.1.jar
* mail-1.4.4.jar
* mysql-connector-java-5.1.20-bin.jar

You can find these files on the internet. Because of GPL we can not package them for you.

#### Configure a MySQL Database

Create a database (for example "rave-portal") in MySQL and give a user access.
Next, set the database name, user name and password in *portal.properties* and *rave.shindig.properties*.

For example:

**rave.shindig.properties**:
```ini
rave-shindig.dataSource.url=jdbc:mysql://localhost:3306/rave-portal
rave-shindig.dataSource.driver=com.mysql.jdbc.Driver
rave-shindig.dataSource.username=rave
rave-shindig.dataSource.password=SecretPassword123
```

**portal.properties**:
```ini
portal.dataSource.url=jdbc:mysql://localhost:3306/rave-portal?allowMultiQueries=true
portal.dataSource.driver=com.mysql.jdbc.Driver
portal.dataSource.username=rave
portal.dataSource.password=SecretPassword123
```

#### Configure the hosts

Add the following host configuration to conf/server.xml:

```xml
<Host name="rave.example.com" appBase="webapps/rave.example.com"/>
<Host name="rave-shindig.example.com" appBase="webapps/example.com"/>
```

Create the directories *conf/Catalina*, *conf/Catalina/rave.example.com* and *conf/Catalina/rave-shindig.example.com*.

Create a file *conf/Catalina/rave.example.com/portal.xml*.
Please change the docBase parameter to the correct location.

```xml
<Context path="/portal" docBase="/path/to/coin-rave-portal-dist-1.0-SNAPSHOT.war" debug="0">
</Context>
```

Create a file *conf/Catalina/rave-shindig.example.com/ROOT.xml*.
Please change the docBase parameter to the correct location.

```xml
<Context path="" docBase="/path/to/coin-rave-shindig-1.0-SNAPSHOT.war" debug="0"></Context>
```

#### Start the server

```bash
bin/startup.sh
```





