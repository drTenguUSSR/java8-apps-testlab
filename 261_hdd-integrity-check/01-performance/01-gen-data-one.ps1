#Set-ExecutionPolicy Bypass -Scope Process -Force
Import-Module .\sub-lib-New-RandomDataFile.psd1 -Force -ErrorAction Stop
. .\sub-set-local.ps1

Write-Host ("work dir = {0}" -f $workDir)
if ([string]::IsNullOrWhiteSpace($workDir)) {
    Write-Error "Переменная пустая, null или содержит только пробелы"
    exit 2
}

if (-not (Test-Path -Path $workDir -PathType Container)) {
    Write-Error ("каталог `${0}` не существует" -f $workDir)
    exit 2
}

$workDir = $workDir.Trim()
if (-not $workDir.EndsWith("\")) {
    $workDir += "\"
}

$workFile = Join-Path -Path $workDir -ChildPath "dat-one.bin"

if (Test-Path $workFile) {
    Write-Warning "Файл $workFile существует, продолжаем работу"
    exit 2
}

#. dd if=/dev/urandom of=$workFile bs=64M count=16 iflag=fullblock

# Запуск функции
#$result = New-RandomDataFile -Path $workFile -BlockSize 64MB -BlockCount 16
$result = New-RandomDataFile -Path $workFile -BlockSize 64MB -BlockCount 160

# Безопасный вывод результатов
if ($null -ne $result  ) {
    Write-Host "`n=== Результаты выполнения ===" -ForegroundColor Cyan
    Write-Host "Файл: $($result.FilePath)" -ForegroundColor Green
    Write-Host "Размер: $([math]::Round($result.FileSizeMB, 2)) MB" -ForegroundColor Green
    Write-Host "Время выполнения: $([math]::Round($result.DurationSeconds, 2)) сек" -ForegroundColor Yellow
    Write-Host "Средняя скорость: $([math]::Round($result.SpeedMBps, 2)) MB/сек" -ForegroundColor Yellow
    Write-Host "SHA256: $($result.SHA256)" -ForegroundColor Magenta
    
    if ($null -ne $result.StartTime) {
        Write-Host "Начало:    $($result.StartTime.ToString('yyyy-MM-dd HH:mm:ss'))" -ForegroundColor Gray
    }
    if ($null -ne $result.EndTime) {
        Write-Host "Окончание: $($result.EndTime.ToString('yyyy-MM-dd HH:mm:ss'))" -ForegroundColor Gray
    }
} else {
    Write-Error "Функция не вернула результат. Проверьте логи ошибок."
}


Write-Host "done 01-gen"