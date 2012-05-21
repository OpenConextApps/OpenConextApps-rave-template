Surfnet's Apache Rave Template
------------------------------

This is a minimal WAR overlay project to run Apache Rave at SURFnet.
New features should preferably be generalized and contributed to Apache Rave.

Building
--------

Building this template requires git and maven 3.0.

```bash
git clone git://github.com/OpenConextApps/OpenConextApps-rave-template.git
cd OpenConextApps-rave-template
mvn clean install
```

Running locally
---------------

Running locally is easy! Please do the following:

```bash
cd coin-rave-portal-design
mvn cargo:start
```

And visit http://localhost:8080/portal/

Deploy on a server
------------------