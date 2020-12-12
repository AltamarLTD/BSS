FROM openjdk:8-jdk-alpine
COPY target/BSS.jar BSS_final.jar
CMD ["java","-jar","/BSS/target/BSS.jar"]
RUN mkdir -p /home/myapp/file_storage
ENTRYPOINT ["java","-jar","BSS_final.jar"]
