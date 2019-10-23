# CSYE 6225 - Fall 2019

## Team Information

| Name | NEU ID | Email Address |
| --- | --- | --- |
| Sixin Wang | 001400940 | wang.sixi@husky.neu.edu |
| Wenkai Zheng | 001444202 | zheng.wenk@husky.neu.edu |
| Yumeng Chen | 001409547 | chen.yum@husky.neu.edu |
| | | |

## Technology Stack
#### Framework: Spring Boot 2.1.8
#### Database: MariaDB
#### Language: Java JDK 1.8
#### IDE: IntelliJ IDEA Utilmate
#### OS: Ubuntu 18.04

## Build Instructions
### 1. Install the Ubuntu
### 2. Install the Java SDK
#### sudo apt update
#### sudo apt install openjdk-8-jdk
### 3. Install the MariaDB
#### sudo apt-get install software-properties-common
#### sudo apt-key adv --recv-keys --keyserver hkp://keyserver.ubuntu.com:80 0xF1656F24C74CD1D8
#### sudo add-apt-repository 'deb [arch=amd64,arm64,ppc64el] http://mirror.host.ag/mariadb/repo/10.4/ubuntu bionic main'

#### sudo apt update
#### sudo apt install mariadb-server
### 4. Install the IntellJ IDEA Ultimate
#### https://www.jetbrains.com/idea/download/#section=linux

## Deploy Instructions
### 1. Run IntelliJ IDEA
### 2. Import this project
### 3. Choose maven project
### 4. Run project

## Running Tests
### 1. When the server is in the initial state, the server needs to be restarted to obtain the newly added data. Therefore, we first run the project to inject a demo data (email: test@email.com, password: 1111Test!!) into the database. Then we can update and get the stored data in the database.

### 2. Test the functions include insert/update/get user infomation. With multiple cases for insert data to a user from wrong password and illegal email scope; test update user name and password function works well; test user only be allowed to update First Name, Last Name and Password.
## recipe example data
using data below to run recipe related API
<pre><code>{
  "cook_time_in_min": 15,
  "prep_time_in_min": 15,
  "title": "Creamy Cajun Chicken Pasta",
  "cusine": "Italian",
  "servings": 2,
  "ingredients": [
    "4 ounces linguine pasta",
    "2 boneless, skinless chicken breast halves, sliced into thin strips",
    "2 teaspoons Cajun seasoning",
    "2 tablespoons butter"
  ],
  "steps": [
    {
      "position": 1,
      "items": "some text here"
    }
  ],
  "nutrition_information": {
    "calories": 100,
    "cholesterol_in_mg": 4,
    "sodium_in_mg": 100,
    "carbohydrates_in_grams": 53.7,
    "protein_in_grams": 53.7
  }
}</code></pre>

## Run Project
<pre><code>
mvn spring-boot:run -Dspring-boot.run.arguments=--region=[region],--bucketName=[bucketName],--accessKey=[accessKey],--secretKey=[secretKey],--dbURL=[database address],--dbName=[database name]</pre></code>
## CI/CD


