# Работы по курсу "Разработчик Java" в Otus.ru

Группа 2020-06

### Автор 
Mher Arshakyan (mherarsh@gmail.com)

Приложение из себя представляет мини аналог брокера сообщений на веб сокетах.
Можно подключить любое количество клиентов. Клиенты настраиваются в файле конфигурации.

Для запуска можно использовать скрипт "build-and-run.bat" или приложение "AppStarter", проекты скомпилируются и запустятся.


### Пример запуска:  
запуск сервера: java -jar build/message-server/message-server.jar  
при запуске сервер так же может запустить клиентов, для этого нужно установить значение свойства ms.runClients=true (поведение по умолчанию)

запуск сервиса базы данных: java -jar build/db-server/db-server.jar

запуске двух фронтенд сервисов:  
java -jar build/frontend/frontend.jar  --spring.config.location=application-front1.yaml  
java -jar build/frontend/frontend.jar  --spring.config.location=application-front11.yaml  
java -jar build/frontend/frontend.jar  --spring.config.location=application-front2.yaml
