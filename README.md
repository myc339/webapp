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

## CI/CD


