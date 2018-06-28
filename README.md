pilger
======
App with Java EE7 + Wildfly 12 + Vaadin 8


#### Requirements:
- jdk 1.8
- eclipse Oxygen   
- MySQL 5.7   
- WildFly 12   
<br/>

#### 1. eclipse: Install IDE
Download:   Eclipse IDE for Java EE Developers  
InstallDir:     C:\dev\ide\e4.6-JEE  
<br/>

#### 2. eclispe: Install JBOSS Tools   
Help > Eclipse Marketplace...   
Search:	JBoss   
Install:		JBoss Tools 4.5.3.Final   
<br/>

#### 3. MySQL: Install
Standardinstallation
<br/>

#### 4. MySQL: Create Database
```
mysql -u root –p;
CREATE DATABASE pilgerdb;
CREATE USER 'pilgeruser'@'localhost' IDENTIFIED BY '123atgfd';
GRANT ALL ON pilgerdb.* TO 'pilgeruser'@'localhost' IDENTIFIED BY '123atgfd' WITH GRANT OPTION;
quit;
```
<br/>

#### 5. WildFly: Install
Download:		WildFly 12   
InstallDir:		C:\dev\wildfly\wildfly-12
<br/>

#### 6. WildFly: Create user admin
C:\dev\wildfly\wildfly-12\bin\add-user.bat
<br/>

#### 7. WildFly: Deploy MySQL-Driver
Source:		C:\Program Files (x86)\MySQL\Connector J 5.1\mysql-connector-java-5.1.45-bin.jar   
Target:		C:\dev\wildfly\wildfly-12\standalone\deployments\mysql-connector-java-5.1.45-bin.jar
<br/>

#### 8. WildFly: Create Datasource
```
http://localhost:9990

Configuration > Subsystems > Datasources > Non-XA

|Value                       | Settings                                                                                                        |
|------------------------|----------------------------------------------------------------------------------------|
|Name:                     | PilgerDS      																									|
|JNDI:			      		   |java:jboss/datasources/PilgerDS																|
|Driver:			    		   |mysql-connector-java-5.1.45-bin.jar_com.mysql.jdbc.Driver_5_1	|
|Connection URL:   |jdbc:mysql://localhost:3306/pilgerdb?ssl=true										|
|User name:		       |pilgeruser																										|
|Password:		       |123atgfd																										|
```

#### 9. Maven (execute on App, not on AppAggregat, AppFrontend and AppBackend)
Build:		clean verify  
Deploy:	clean package wildfly:deploy 

#### 10. Test
http://localhost:8080/AppFrontend/

<<<<<<< HEAD

=======
>>>>>>> branch 'master' of https://github.com/haglo/pilger.git
