##### Calender Application

Backend app to schedule an event, find available slots and 
check the meeting conflicts of users.

## Getting Started
Clone the github Repo and import in intellij (or your choice of IDE) \
*Notes:* 
- If you have the zipped project, just unzip it and import in any IDE
- SQL Queries to generate Schema and populate data are writen in `schema.sql` and `data.sql` files resp. \
  You can find them inside `src/main/resources` 

### Prerequisites
- Java 11
- gradle-6.8.3 (Gradle wrapper is already present)
- Intellij (or your choice of IDE)

### Installing
- Install java 11 \
  You can use [sdman](https://sdkman.io/install) and choose 11.0.9.j9-adpt as java<br>
  `sdk install java 11.0.9.j9-adpt`
  
### CURLS
- Book event for the user.
    ```
        curl -L -X POST 'localhost:8080/events/book?id=1' \
        -H 'Content-Type: application/json' \
        --data-raw '{
            "title": "something",
            "description": "testdesc",
            "date": "2021-06-20",
            "startTime": "2021-06-20 16:00:00",
            "endTime": "2021-06-20 16:30:00"
        }'    
    ```
- Find available slots for given two users.
    ```
    curl -L -X GET 'localhost:8080/events/available-slots?firstEmpId=1&secondEmpId=2&date=2021-06-20'
    ```
- Find meeting conflicts.
    ```
    curl -L -X GET 'localhost:8080/events/meeting' \
    -H 'Content-Type: application/json' \
    --data-raw '{
        "title": "something",
        "description": "testdesc",
        "date": "2021-06-20",
        "startTime": "2021-06-20 19:40:00",
        "endTime": "2021-06-20 19:20:00",
        "emails" : [
            "shubham@gmail.com",
            "sharma@gmail.com"
        ]
    }'
    ```
    
## Built With
* [Spring boot ](https://spring.io/projects/spring-boot) - The framework used
* [Gradle](https://gradle.org/) - Dependency Management

## Improvements
- Indexing in tables.
- DB normalization can be done to some more extent.
- DB queries executed can be modified to work better.

## Authors
* **Shubham Sharma** - *Owner* - [Github](https://github.com/shubham-shar)

## Acknowledgments
- [Baeldung](https://www.baeldung.com)
- [StackoverFlow](https://stackoverflow.com/)
