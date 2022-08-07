## ⚒ Tech Stack
&nbsp;

<div align="center">
  <img src="https://img.shields.io/badge/SpringBoot-2.6.6-6DB33F?logo=SpringBoot"> 
  <img src="https://img.shields.io/badge/SpringSecurity-sky">
  <img src="https://img.shields.io/badge/Gradle-02303A?style=flat&logo=Gradle&logoColor=white"></a>&nbsp;
  <img src="https://img.shields.io/badge/Java-11-9cf">
  <img src="https://img.shields.io/badge/JPA-Green">
  <img src="https://img.shields.io/badge/QueryDsl-5.0.0-blue">
  <img src="https://img.shields.io/badge/MySQL-8.0.23-E0234E?logo=MySQL"> 
  <img src="https://img.shields.io/badge/JWT-black?logo=JSON%20web%20tokens">
  <img src="https://img.shields.io/badge/Swagger-brightgreen">
</div>

<div align="center">

  <img src="https://img.shields.io/badge/AWS EC2-orange"> 
  <img src="https://img.shields.io/badge/AWS RDS-blue"> 
  <img src="https://img.shields.io/badge/Nginx-1.18.0-009639?logo=Nginx">
  <img src="https://img.shields.io/badge/Ubuntu-E95420?style=flat&logo=Ubuntu&logoColor=white"</a>&nbsp;

</div>
&nbsp;
&nbsp;
&nbsp;

## 📖 API Docs
### <a href = "https://dev.umcsimple.shop/swagger-ui/index.html"> Gridge-Challenge API Docs </a>

&nbsp;
&nbsp;
&nbsp;


## 📂 Directory Structure
```
📦server
 ┗ 📂insta
 ┃ ┣ 📂config       - 서버 설정 및 security, validation, oAuth의 로직을 전반적으로 관리
 ┃ ┃ ┣ 📂Entity     - domain에 보조적으로 도움을 줄 수 있는 Entity 모음
 ┃ ┃ ┣ 📂exception  - Validation
 ┃ ┃ ┣ 📂oAuth      - oAuth 토큰 인증 로직
 ┃ ┃ ┣ 📂response   - Api 공통 response
 ┃ ┃ ┃ ┣ 📂result
 ┃ ┃ ┃ ┗ 📜ResponseService.java
 ┃ ┃ ┣ 📂security   - Spring security 및 Jwt설정
 ┃ ┃ ┃ ┣ 📂jwt
 ┃ ┃ ┃ ┣ 📜CorsConfig.java
 ┃ ┃ ┃ ┗ 📜SecurityConfig.java
 ┃ ┃ ┣ 📜QuerydslConfiguration.java
 ┃ ┃ ┣ 📜SwaggerConfig.java
 ┃ ┃ ┗ 📜Workaround.java
 ┃ ┣ 📂controller   
 ┃ ┣ 📂domain
 ┃ ┣ 📂dto
 ┃ ┃ ┣ 📂request
 ┃ ┃ ┗ 📂response
 ┃ ┣ 📂log          - Log 관련모음          
 ┃ ┣ 📂repository
 ┃ ┣ 📂service
```

&nbsp;
&nbsp;
&nbsp;



## 📝 Architecture
<img width="800" alt="스크린샷 2022-08-05 오후 6 54 41" src="https://user-images.githubusercontent.com/88089316/183052923-faccaade-da02-4ddc-8f66-53e62145ebb4.png">

&nbsp;
&nbsp;
&nbsp;


## 📔 ERD

<img width="675" alt="2200807_Server_심플:조석환_ERD" src="https://user-images.githubusercontent.com/88089316/183279746-aa589d86-8633-478e-8120-b42871d76d7d.png">


















