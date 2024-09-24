## About
- use web crawler to tap into the power of systematic data extraction, large scale information retrieval, and real time web monitoring

## Design
- Webpage class encapsulates page access and attributes

- RobotsCache is singleton class of robots txt information, so robots txt parsing isn't repeated for the same website

- crawler abides to website crawling instructions, forbidden links are filtered at Webpage creation

- example project crawls wiki's Special:Random/Categories namespace and subsequent links, extracts all users in edit history of each topic and creates a User instance for each contributor

- working on geolocation resolver for anonymous contributors, whose username is stored as an IP address

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


