FROM openjdk:15
COPY ./target/BankSecurity-0.0.1-SNAPSHOT.jar banksecurity.jar
ENTRYPOINT ["java", "-jar", "banksecurity.jar"]