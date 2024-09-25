## About
- web crawler designed to tap into the power of systematic data extraction, large scale information retrieval, and real time web monitoring

- project contains a complete ip to geolocation resolver package `geolocation_utils`, using MaxMind's free geolocation database GeoLite2 

- `data`directory contains data processing scripts and outputs `geodata.db` 

- `src/main` contains web crawler application and `src/test` contains unit tests for utility classes

## Design
- Object Oriented design to support multi-site and multi-page crawling

- Webpage class encapsulates page access and attributes

- Robots txt cached in singleton class, disallowed links are filtered at creation of Webpage instance

- example project crawls wiki's Special:Random/Categories namespace and subsequent links, extracts all users in edit history of each topic, creates a User instance for each contributor, and resolves network address of anonymous contributors

## Instructions 
1. crawlers written in different languages, so clone branch of interest 

    `git clone --branch <branch_name> --single-branch <repository_url>`
2. open java project with maven configured IDE
3. clean build artifacts (if there are any) and package code and dependencies to jar file

    `mvn clean package` 
4. uber jar will contain all code and dependencies

    `crawler-1.0-SNAPSHOT.jar`
5. run jar: 

    `java -jar target/crawler-1.0-SNAPSHOT.jar`


