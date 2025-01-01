# java8-apps-testlab

Содержит коллекцию небольших суб-проектов для отработки той или иной технологии.
Ключевое ограничение - используется Java 8, SpringBoot 2.7.

Сортировка суб-проектов на странице - от новых (вверху) к более старым (внизу).

Именование для под-проектов на примере "247_Sensible-Soldier"

Схема именования папок:
- "247" - дата начала проекта: "24" - год; "7" - месяц [1...C]. 10 - A, 11 - B, 12 - C;
- "_" - разделитель;
- "Sensible-Soldier" - имя проекта.

Имя базавого пакета для проектов: mil.teng247.sensible.soldier

## [24C-AspectJ-via-gradle](24C-AspectJ-via-gradle)

консольное приложение для отладки перехвата вызовов:

- 24С-m1 - внутри проекта

- 24С-m2 - во внешнем коде (24B)

проблема: нужно совместить aspectj-компиляцию и присутствие lombok-кода
сборщик gradle 6.8.2

## [24B-AspectJ-baseAnimalLib](24B-AspectJ-baseAnimalLib)

базовый (библиотечный) проект для отработки AspectJ
цель: проект компилируется в jar без знания об AspectJ
внешний проект использует 24B и как-то накладывает на него перехват вызовов
внутри библиотеки, без вмешательства в её исходный код и перекомпиляции

сборщик - maven

## [24B-AspectJ-console](24B-AspectJ-console)

отработка aspectj-компиляции на консольком приложении
библиотеки - lombok,aspectj-maven-plugin
сборщик - maven

## [249_SupplyWarehouse-SB2](249_SupplyWarehouse-SB2)

Java8 SpringBoot2+JPA(Hibernate)

system downgraded: Java 8, SpringBoot 2.7.18
added: log4j2 yaml support

сборка проекта в maven