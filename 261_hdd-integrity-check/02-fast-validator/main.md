# 02. Быстрая валидация по текущим хеш-суммам

- [02. Быстрая валидация по текущим хеш-суммам](#02-быстрая-валидация-по-текущим-хеш-суммам)
  - [Исходные данные](#исходные-данные)
  - [модификация данных для быстрых проверок HV-MachinesK-0](#модификация-данных-для-быстрых-проверок-hv-machinesk-0)
    - [результаты](#результаты)
  - [Проверка полного пакета HV-MachinesK-0 ... HV-MachinesK-19](#проверка-полного-пакета-hv-machinesk-0--hv-machinesk-19)
    - [*время выполнения*](#время-выполнения)
  - [Замеры времени. cmd-скрипт замера времени выполнения](#замеры-времени-cmd-скрипт-замера-времени-выполнения)
  - [Генерация хеш суммы через git/sha512sum](#генерация-хеш-суммы-через-gitsha512sum)
  - [Проверка хеш суммы через git/sha512sum](#проверка-хеш-суммы-через-gitsha512sum)

## Исходные данные

- в корневой папке есть массив подпапакок HV-MachinesK-0 ... HV-MachinesK-19.
На самом деле подпапки являются symlink (dir /a отображает как ```<JUNCTION>```) на одну подпапку на томже диске K
- подпапка содержит 36.8 Гб. Суммарно 736 Гб.
- в корне лежит checksum.sha содержащий в формате fsum, содержащий полный перечень
  файлов из всех подпапок и набор контрольных сумм для каждого (md5,sha2-256, sha2-512)

## модификация данных для быстрых проверок HV-MachinesK-0

1.Из checksum.sha отбираются данные, относящиеся только к одной подпапке "HV-MachinesK-0". Результат в hv0-full.txt

```bash
grep 'MachinesK-0\\HV02K' checksum.sha >hv0-full.txt
```

2.Из hv0-full.txt отбираются только те строки, где проверяется sha2-512 сумма. Результат в hv0-sha.txt

```bash
grep '\s\?SHA512\*HV' hv0-full.txt > hv0-sha.txt
```

3.Из hv0-sha.txt приводится к формату sha512sum.exe (из git). результат в hv0-git512.txt

```bash
awk '!/^DENIED / {gsub(/\s\?SHA512\*HV/, " *HV"); print}' hv0-sha.txt > hv0-git512.txt
```

4.Замеряется скорость выполнения:

   1. cmd-1(K): fsum -c hv0-sha.txt
   2. cmd-2(K): sha512sum.exe -c hv0-git512.txt

### результаты

выполнены два прогона каждого замера

- cmd-1(K) - 40:48 минут
- cmd-2(K) - 1:37 минут
- cmd-1(K) - 40:45 минут
- cmd-2(K) - 3:21 минут
- cmd-2(K) - 1:38 минут

## Проверка полного пакета HV-MachinesK-0 ... HV-MachinesK-19

```bash
grep '\s\?SHA512\*HV' checksum.sha | awk '!/^DENIED / {gsub(/\s\?SHA512\*HV/, " *HV"); print}' > hvAll-git512.txt
```

```cmd
sha512sum.exe -c hvAll-git512.txt
```

### *время выполнения*

для пакетов (0...19) на K: - 32:08 / 32:15

для пакетов (0...19) на G: - 1:22:26 / 1:22:24 / 1:22:20

## Замеры времени. cmd-скрипт замера времени выполнения

```cmd
@echo off
echo ================================== >> do-1.txt
FOR /F "tokens=*" %%i IN ('powershell -Command "Get-Date -Format \"yyyy-MM-dd HH:mm:ss\""') DO SET DateTimeStamp=%%i
echo === begin %DateTimeStamp% >> do-1.txt
@rem fsum -c hv0-sha.txt
@rem C:\PROGRA~1\Git\usr\bin\sha512sum.exe -c hv0-git512.txt
C:\PROGRA~1\Git\usr\bin\sha512sum.exe -c hvAll-git512.txt 2>&1 >>do-1.txt 
@rem C:\PROGRA~1\Git\usr\bin\sha512sum.exe -c dat-one.sha >>do-1.txt 
FOR /F "tokens=*" %%i IN ('powershell -Command "Get-Date -Format \"yyyy-MM-dd HH:mm:ss\""') DO SET DateTimeStamp=%%i
echo === end   %DateTimeStamp% >> do-1.txt
```

## Генерация хеш суммы через git/sha512sum

```cmd
C:\PROGRA~1\Git\usr\bin\sha512sum.exe -b dat-one.bin > dat-one.sha
```

## Проверка хеш суммы через git/sha512sum

```cmd
C:\PROGRA~1\Git\usr\bin\sha512sum.exe -c dat-one.sha >rep-1.log 2>rep-2.log
```

rep-1.log - успешные/неуспешные проверки с указанием имени файла

rep-2.log - итоговый результат числа сбойных файлов типа `"WARNING: 4 computed checksums did NOT match"`

в формате .sha - комментарий начинается с \#; пустые строки не поддерживаются
