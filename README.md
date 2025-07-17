# Project Bishop
это микросервис для управления задачами андройда(человека подобного робота) с гибкой системой приоритизации и встроенным аудитом.

## Архитектура
- bishop-prototype - основное приложение
- synthetic-human-core-starter - спринг-стартер

## Возможности
- Добавление задачи в очередь / Выполнение срочной задачи
- Очистка очереди задачь (на случай востания машин )
- Очередь с ограничением по размеру (max 10)
- Prometheus-метрики:
    - queue_size — размер очереди задач
    - done_commands — количество выполненных задач
- Аудит методов с аннотацией @WeylandWatchingYou
    - в Kafka или в логах (в зависимости от настройки)
- Отдельный обработчик ошибок с кастомными ответами

## API
| Метод  | URL                             |  Описание                                  |
| :-------|:--------------------------------|:-------------------------------------------|
| POST    | /api/v1/android/addCommand      | Добавить задачу                            |
| GET     | /api/v1/android/commandCount    | Получить размер очереди                    |
| GET     | /api/v1/android/done_commands   | Получить статистику по выполненным задачам |
| DELETE  | /api/v1/android/deleteCommands  | Очистить очередь                           |

## Доступы к сервисам
| Сервис    | URL                                           |
| --------- | :---------------------------------------------|
| Prometheus| http://localhost:9090/query                   |
| Kafka UI  | http://localhost:8090/ui                      |
| Swagger UI| http://localhost:8080/swagger-ui/index.html#/ |

## Стек
- Spring Boot 3 
- ThreadPoolExecutor
- Kafka, AOP
- Actuator, Micrometer, Prometheus
- @ExceptionHandler
- Swagger

## Выполнил: 
Селявский Кирилл
