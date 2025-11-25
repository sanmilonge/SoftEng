---

# World Population Analysis System

Software Engineering Methods Coursework – Edinburgh Napier University

![Build](https://img.shields.io/github/actions/workflow/status/sanmilonge/SoftEng/main.yml?label=Build\&logo=github)
![Coverage](https://img.shields.io/codecov/c/github/sanmilonge/SoftEng?logo=codecov)
![Release](https://img.shields.io/github/v/release/sanmilonge/SoftEng?logo=github)
![Contributors](https://img.shields.io/github/contributors/sanmilonge/SoftEng?logo=github)


## Overview

The **World Population Analysis System** is a Java application used to analyse global population data based on the official MySQL **world** database.
It generates **32 detailed Markdown reports** covering population statistics for countries, cities, capital cities, continents, regions, districts, and world languages.

This project is part of the **Software Engineering Methods** coursework at **Edinburgh Napier University**.

---

## Technologies Used

| Component        | Technology               |
| ---------------- | ------------------------ |
| Language         | Java 17                  |
| Build Tool       | Maven                    |
| Database         | MySQL (world-db)         |
| Mocking          | Mockito + Mockito-inline |
| Testing          | JUnit 5                  |
| Coverage         | JaCoCo + Codecov         |
| CI/CD            | GitHub Actions           |
| Containerisation | Docker & Docker Compose  |
| Output Format    | Markdown Reports         |

---

## Features

The system includes:

* Full implementation of **all 32 required population analysis reports**
* Automatic Markdown report generation
* Support for interactive mode and CI mode
* Ability to run using:

  * The release JAR file
  * Docker Compose
  * Maven
* Fully tested components with unit and integration tests

---

# How to Run the Application

You can run the system in **two ways**:

1. By cloning the repository
2. By using the release JAR file directly

Both methods require the MySQL world database. The repository includes the `/db` folder which builds this database automatically via Docker.

---

# 1. Clone the Repository

```
git clone https://github.com/sanmilonge/SoftEng.git
cd SoftEng
```

This gives you:

* The Java application
* Docker Compose setup
* Database build files (`/db`)
* All source code
* Test suite

---

# 2. Running the Application Using the Release JAR

The release contains:

```
devops.jar
```

You do **not** need to clone the entire repo to run the JAR, but you **do need the database**.
To obtain the database setup only:

### Clone only the `/db` folder

```bash
git clone --depth=1 --filter=blob:none --sparse https://github.com/sanmilonge/SoftEng.git
cd SoftEng
git sparse-checkout set db
```

Now build and start the database:

```bash
docker build -t worlddb ./db
docker run -d --name world-db -p 33060:3306 worlddb
```

Then run the application:

```bash
java -jar devops.jar
```

The app will connect to:

```
localhost:33060
```

Reports are generated at:

```
src/main/resources/reports/
```

---

# 3. Running with Docker Compose

If you cloned the whole repository:

Start the full stack:

```bash
docker compose up --build
```

Run the application only:

```bash
docker compose run --rm app
```

Shut down services:

```bash
docker compose down
```

---

# Testing and Coverage

* Unit and integration tests included
* Static mocking via Mockito-inline
* Coverage produced by JaCoCo
* GitHub Actions CI uploads coverage to Codecov

Run tests locally:

```bash
mvn test
```

Run full pipeline with coverage:

```bash
mvn clean verify
```

---

# Project Structure

```
├── db/                         # World database build (Docker)
├── src/main/java/Coursework    # Application code
├── src/test/java/Coursework    # Test suite
├── src/main/resources/reports  # Generated report files
├── docker-compose.yml
├── pom.xml
└── devops.jar (release)
```

---

# Contributors

[![Sanmi Longe](https://img.shields.io/badge/GitHub-Longe-black?logo=github)](https://github.com/sanmilonge)
[![Imran Khan](https://img.shields.io/badge/GitHub-Imran-black?logo=github)](https://github.com/03006689)
---

# License

This project was created for academic use as part of coursework for Edinburgh Napier University.

---
