<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a name="readme-top"></a>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->



<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/othneildrew/Best-README-Template">
    <img src="LikeIcon.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">Filmorate</h3>

  <p align="center">
    Агрегатор оценок фильмов
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Содержание</summary>
  <ol>
    <li>
      <a href="#о-проекте">О проекте</a>
      <ul>
        <li><a href="#создан-при-помощи">Создан при помощи</a></li>
      </ul>
      <ul>
        <li><a href="#команда-разработки">Команда разрабокти</a></li>
      </ul>
    </li>
    <li>
      <a href="#запуск-проекта">Запуск проекта</a>
      <ul>
        <li><a href="#приготовления">Приготовления</a></li>
        <li><a href="#установка">Установка</a></li>
      </ul>
    </li>
    <li><a href="#использование">Использование</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## О проекте

Групповой проект реализованный в виде монолитного приложения. Представляет собой сервис для оценки и рекомендации фильмов между пользователями.

Сервис предоставляет следующий функционал:
* Регистрация пользователей, получение информации о уже зарегестрированных пользователях, lобавление других пользователей в список друзей
* Добавление, редактирование и удаление фильмов из базы данных
* Поиск фильмов по названию и режиссёру
* Добавление отзывов на фильмы от пользователей
* Просмотр ленты событий пользователей по их действиям на платформе
* Получение рекомендаций по фильмам на основе списка друзей

Приложение представляет собой RESTful веб-сервис и базу данных в одном лице. В качестве БД используется H2. Всё приложение поднимается в одном контейнере.

<p align="right">(<a href="#readme-top">к заглавию</a>)</p>

### Команда разработки

* Александр Леонов (тимлид) - https://github.com/LeonovAlexProg
* Александр Александров - https://github.com/IceCubeNext
* Ольга Шаталова - https://github.com/ol5ga
* Роман Антипенко - https://github.com/RomanAntipenko
* Артур Шакиров - https://github.com/AraShock

<p align="right">(<a href="#readme-top">к заглавию</a>)</p>

### Создан при помощи

* ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
* ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
* ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

<p align="right">(<a href="#readme-top">к заглавию</a>)</p>



<!-- GETTING STARTED -->
## Запуск проекта

Далее описаны требования для запуска проекта на локальной машине:

### Приготовления

Для работы приложение требуется установленный и запущенный Docker daemon. Для проверки его наличия введите следующую команду в консоли (Windows)
* cmd
  ```sh
  docker version
  ```
Если выводится информация о установленной системе, переходим к следующему шагу.

### Установка

_Далее описаны пункты для запуска проекта_

1. Клонируйте репозиторий
   ```cmd
   git clone https://github.com/LeonovAlexProg/java-filmorate.git
   ```
2. Перейдите в корневую папку проекта
   ```cmd
   cd {путь да корневой директории}/java-filmorate
   ```
4. Создайте образ контейнера
   ```cmd
   docker build -t filmorate_image .   
   ```
4. Запустите контейнер
   ```cmd
   docker run --name filmorate_container -p 8080:8080 filmorate_image
   ```

<p align="right">(<a href="#readme-top">к заглавию</a>)</p>



<!-- USAGE EXAMPLES -->
## Использование

_Postman-коллекция с примерами запросов - [Ссылка](https://github.com/yandex-praktikum/java-filmorate/blob/develop/postman/sprint.json)_

<p align="right">(<a href="#readme-top">к заглавию</a>)</p>

<!-- CONTACT -->
## Контакты

Леонов Александр - https://t.me/b4klazhan - leonovalexprog@gmail.com

Ссылка на проект: [https://github.com/LeonovAlexProg/java-shareit](https://github.com/LeonovAlexProg/java-shareit)

<p align="right">(<a href="#readme-top">к заглавию</a>)</p>
