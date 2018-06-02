# pilger
App with Java EE7 + Wildfly12 + Vaadin 8


#### Here with:
- eclipse Oxygen   
- MySQL 5.7   
- WildFly 12   



### 1. eclipse: Install IDE
Download: 	Eclipse IDE for Java EE Developers  
InstallDir:	C:\dev\ide\e4.6-JEE  



### 2. eclispe: Install JBOSS Tools   
Help > Eclipse Marketplace...   
Search: 	JBoss   
Install:	JBoss Tools 4.5.3.Final   



### 3. MySQL: Install
Standardinstallation



### 4. MySQL: Create Database
```
mysql -u root –p;
CREATE DATABASE pilgerdb;
CREATE USER 'pilgeruser'@'localhost' IDENTIFIED BY '123atgfd';
GRANT ALL ON pilgerdb.* TO 'pilgeruser'@'localhost' IDENTIFIED BY '123atgfd' WITH GRANT OPTION;
quit;
```


### 5. WildFly: Install
Download:	WildFly 12   
InstallDir:	C:\dev\wildfly\wildfly-12



### 6. Wildfly: Create user admin
C:\dev\wildfly\wildfly-12\bin\add-user.bat



### 7. WildFly: Deploy MySQL-Driver
Source:	C:\Program Files (x86)\MySQL\Connector J 5.1\mysql-connector-java-5.1.45-bin.jar   
Target:	C:\dev\wildfly\wildfly-12\standalone\deployments\mysql-connector-java-5.1.45-bin.jar



### 8. WildFly: Create Datasource

http://http://localhost:9990

Configuration > Subsystems > Datasources > Non-XA

- Name:			      PilgerDS   
- JNDI:			      java:jboss/datasources/PilgerDS
- Driver:			    mysql-connector-java-5.1.45-bin.jar_com.mysql.jdbc.Driver_5_1
- Connection URL: jdbc:mysql://localhost:3306/pilgerdb?ssl=true
- User name:		  pilgeruser
- Password:		    123atgfd

