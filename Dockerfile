FROM node:18-alpine AS node_stage

WORKDIR /app/boilerplate-spring-boot-serverless

COPY package.json package-lock.json ./

RUN npm i

FROM eclipse-temurin:21-jdk-alpine AS java_stage

RUN apk update && apk add --no-cache \
    curl \
    maven \
    git \
    nodejs \
    npm

WORKDIR /app/boilerplate-spring-boot-serverless

COPY . .

COPY --from=node_stage /app/boilerplate-spring-boot-serverless/node_modules /app/boilerplate-spring-boot-serverless/node_modules

RUN mvn dependency:resolve

CMD ["sh"]
