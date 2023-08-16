## Microservice
This Springboot project contains services:
<ol>
  <li> <b>Inventory Service:</b> to store and validate inventory in a MySQL database.
  <li> <b>Order Service:</b> receives orders and stores order data in a MySQL database.
  <li> <b>Product Serices:</b> stores the product in a MongoDB database.
</ol>
<b>discovery-server</b> uses <b>Spring Netflix Eureka</b> as Service Discovery server. All the above mentioned services are client to the discovery-server.

All these services can communicate with each other using <b>Spring Webflux.</b>
## Uses
<ul>
  <li> Spring Cloud
  <li> TestContainers
  <li> Hibernate
  <li> MockedMvc
  <li> Spring Webflux
  <li> Spring Netflix Eureka
<ul>

