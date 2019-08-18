FROM openjdk
COPY target/SimbirSoftInternship-1.0.jar /app/ws-chat.jar
WORKDIR /app
ENV PORT "8080"
ENV database.url "jdbc:postgresql://ec2-54-246-98-119.eu-west-1.compute.amazonaws.com:5432/ddq5rnvt0q3fk4"
ENV database.username "gausqmvvqjfvqb"
ENV database.password "e1bda6141431694212702770fce45c7e7f2d494603c4aee20fa1b9f90077a7a6"
ENV ddl-auto "update"
ENV youtube.api.key "AIzaSyAMHHmgLe-DzKT5H6mELDvEZUwtjzEKk8k"
CMD [ "java", "-Xms256M", "-Xmx1536M", "-jar", "ws-chat.jar" ]