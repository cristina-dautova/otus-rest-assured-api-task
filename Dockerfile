FROM gradle:8.5-jdk17 AS builder

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and dependencies first (to leverage Docker caching)
COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY gradle gradle

# Download dependencies
RUN ./gradlew dependencies --no-daemon
RUN curl -o allure-2.24.0.tgz -L https://github.com/allure-framework/allure2/releases/download/2.24.0/allure-2.24.0.tgz \
&& tar -zxvf allure-2.24.0.tgz \ && mv allure-2.24.0 /opt/allure \
&& ln -s /opt/allure/bin/allure /usr/local/bin/allure

# Copy the rest of the project files
COPY . .

# Run tests
CMD ["./gradlew", "test", "--no-daemon"]