# 01. Проблема производительности

## Оценка текущих HDD

CrystalDiskMark, 1 Gb, SEQ1M Q1T1, 5 Iter

|disk  | read (MB/s) | write (MB/s)|
|------|------------:| -----------:|
|disk-d| 205.32      | 189.95      |
|disk-k| 223.77      | 223.76      |
|disk-h| 314.17      | 300.93      |
|disk-f| 170.74      | 169.66      |
|disk-g| 155.83      | 155.83      |

примечания:

- disk-d - заполненность 60%
- disk-k - заполненность 85%
- disk-h - заполненность 65%
- disk-f - заполненность 96%
- disk-h - raid-0 на дисках F,D,K. заполненность 62%
- disk-g - самый новый диск. заполненность 91%

## Дополнения

### разрешение запуска ps1 из powershell

```powershell
Set-ExecutionPolicy Bypass -Scope Process -Force
```

### Русский язык в скриптах - UTF-8 witch BOM в файлах

настройки VS Code (/.vscode/settings.json)

```json
{
    "files.encoding": "utf8bom",
    "terminal.integrated.defaultProfile.windows": "PowerShell",
    "terminal.integrated.profiles.windows": {
        "PowerShell": {
            "source": "PowerShell",
            "icon": "terminal-powershell",
            "args": ["-NoExit", "-Command", "chcp 65001 > $null"]
        }
    }
}
```

### Перекодировка файла в UTF-8 witch BOM

VS Code. Нажать ctrl+shift+P. ввести "Change File Encoding", выбрать
"Save With Encoding", выбрать "UTF8 with BOM".

### Русский язык в скриптах - принудительный UTF-8 (непроверенно)

```powershell
$OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
Write-Host "проверка русского языка"
```

### Проверка кода выхода внешних программ

```powershell
Start-Process notepad.exe -Wait
if ($LASTEXITCODE -ne 0) {
    Write-Error "Notepad завершился с кодом ошибки: $LASTEXITCODE"
    exit $LASTEXITCODE
}
```

### Переформатирование документа

VS Code. нажать shift+alt+f.
прим. переформатируется весь документ. выделять фрагмент не требуется.

### Генерация файла со случайным содержимым, средствами dd.exe из Git

```powershell
#итоговый файл 64Mb * 16 = 1 024 Mb = 1 Gb
dd if=/dev/urandom of=$workFile bs=64M count=16 iflag=fullblock
```

```cmd
C:\PROGRA~1\Git\usr\bin\dd.exe if=/dev/urandom of=dat-1.bin count=1 bs=4M iflag=fullblock
```

### Генерация контрольной суммы для одиночного файла

```cmd
certutil -hashfile "C:\path\to\your\file.txt" SHA512 > file.sha512
```
