#!/bin/bash
# Run Gateway
mvn -f gateway spring-boot:run
# Run All Services
mvn -f cart-service spring-boot:run
mvn -f catalog-service spring-boot:run
mvn -f customer-service spring-boot:run
mvn -f order-service spring-boot:run
mvn -f payment-service spring-boot:run
