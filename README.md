## Spring Boot Microservices
This Springboot project contains services:
<ol>
  <li> <b>Inventory Service:</b> to store and validate inventory in a MySQL database.
  <li> <b>Order Service:</b> receives orders and stores order data in a MySQL database.
  <li> <b>Product Serices:</b> stores the product in a MongoDB database.
</ol>
<code>discovery-server</code> uses <b>Spring Netflix Eureka</b> as Service Discovery server. All the above mentioned services are client to the discovery-server.

All these services can communicate with each other using <b>Spring Webflux.</b>
## Uses
<ul>
  <li> Spring Cloud
  <li> TestContainers
  <li> Hibernate
  <li> MockedMvc
  <li> Spring Webflux
  <li> Spring Netflix Eureka
</ul>

## Spring Netflix Eureka Setup
### Step 1 : Modify pom.xml
Add the following in `pom.xml` of parent project:
```xml
<properties>
    ...
    <spring-cloud.version>2021.0.2</spring-cloud.version>
</properties>
<dependencyManagement>
        <dependencies>
            ...
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```
In `pom.xml` of `discovery-server`:
```xml
<dependencies>
        ...
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
</dependencies>
```
For rest of the services add the following in `pom.xml`:
```xml
<dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```
### Step 2 : Update applications.properties file
In `application.properties` of `discovery-server` add the following lines:
```
eureka.instance.hostname=localhost
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka
server.port=8761
```
The property `eureka.client.register-with-eureka=false` avoids Eureka server to register with itself as a client and make it act as a server. Another property `eureka.client.fetch-registry=false` tells Eureka server to don't search for other registry nodes. Both these properties are **mandatory** to add in the application.properties.

For the remaining services, the application.properties should look something like this:
```
server.port=8081
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/
spring.application.name=order-service # For order-service
```
### Step 3 : Use Annotations
For the server module, use `@EnableEurekaServer` on the main class.

For the other services, use `@EnableEurekaClient` or `@EnableDiscoveryClient` on their main classes depending on the version of Spring Cloud.

Hit the URL http://localhost:8761/ to see the Eureka Dashboard. Please make sure the Spring Cloud version and the Spring Boot version are compatible with each other to avoid Whitelable error page.
