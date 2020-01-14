# stakater-nordmart-search
Spring Boot microservice for Products Search

## Introduction
This project implements search functionality for the products. It consumes data about products from Kafka topic 
and stores it into Elasticsearch. Afterwards it can be searched using REST API.

## Dependencies

It requires following things to be installed:

* Java: ^8.0
* Maven

## Deployment strategy

### Local deployment

To run the application locally use the command given below:

```bash
mvn spring-boot:run
```

### Docker

To deploy app inside a docker container

* Create a network if it doesn't already exist by executing

  ```bash
  docker network create --driver bridge nordmart-apps
  ```

* Build jar file of the app by executing command given below:

  ```bash
  mvn clean package
  ```

* Next build the image using

  ```bash
  docker build -t products-search .
  ```

* Finally run the docker compose

  ```bash
  docker-compose up
  ```

## Helm Charts

### Pre-requisites

Helm operator needs to to be running inside the cluster. Helm operator is deployed by Stakater Global Stack, deployment guidelines are provided in this [link](https://playbook.stakater.com/content/processes/bootstrapping/deploying-stack-on-azure.html)

### Helm chart deployment

To create helm release of this application using the command given below:

kubectl apply -f [helm-release](https://github.com/stakater-lab/nordmart-dev-apps/blob/master/releases/products-search-helm-release.yaml).yaml -n <namespace-name>

## Integration with Kafka

Products search service consumes data from topic with name 'productsTopic', it can be configured using 
skakater-nordmart-search.kafka.product.topic.name property. Application consumes 3 types of messages in JSON
format called commands:

- productCreate - used to create product
- productUpdate - used to update particular product
- productDelete - used to delete particular product

## APIs

- `GET /api/v1/product-search`
    - Perform products search with provided criteria.
    
## Pre-requisites:

* Create a Kubernetes Cluster by following the steps [here](https://github.com/stakater-lab/nordmart-dev-tools).

## Configuration

Can be found in application.yml:

 Kafka connection configuration
 
- skakater-nordmart-search.kafka.product.topic.name - name of the topic from which all the product commands are consumed
- skakater-nordmart-search.kafka.product.topic.create.on-startup - boolean property which defines if topic should be 
created on startup
- skakater-nordmart-search.kafka.product.topic.create.number-of-partitions - number of partitions on which this topic is
divided
- skakater-nordmart-search.kafka.product.topic.create.replication-factor - topic replication factor
- skakater-nordmart-search.kafka.bootstrap-servers - Kafka bootstrap servers

CORS configuration

- skakater-nordmart-search.cors.allowed.hosts - CORS allowed hosts

 Elasticsearch connection configuration

- skakater-nordmart-search.elasticsearch.host - Elasticsearch host
- skakater-nordmart-search.elasticsearch.port - port of Elasticsearch
- skakater-nordmart-search.elasticsearch.username - username for Elasticsearch
- skakater-nordmart-search.elasticsearch.password - password for Elasticsearch

 Server configuration

- spring.application.name - application name
- server.servlet.contextPath - servlet context path
- server.error.include-stacktrace - stacktraces should no be included in case if not handled error happens
- server.host - server hostname
- server.port - server hostname

 Application management configuration

- management.endpoint.metrics.enabled - if micrometer metrics is enabled
- management.endpoint.prometheus.enabled - if prometheus is enabled
- management.endpoints.web.exposure.include - which management endpoints are exposed
- management.metrics.export.prometheus.enabled - whether exporting of metrics to Prometheus is enabled. 
