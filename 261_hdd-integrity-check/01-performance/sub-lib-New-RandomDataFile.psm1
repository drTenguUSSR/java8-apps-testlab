function New-RandomDataFile {
    <#
    .SYNOPSIS
        Создает файл заданного размера со случайными данными, вычисляет SHA256 хеш и показывает детальную статистику производительности.
    
    .DESCRIPTION
        Функция генерирует файл с криптографически безопасными случайными данными, вычисляет SHA256 хеш в реальном времени,
        отображает текущую скорость записи, время начала и предполагаемое время окончания. Использует Stopwatch для точного замера времени.
        Если файл существует, функция завершается с кодом ошибки 2.
        Гарантированно освобождает память для буфера через $buffer = $null и принудительную сборку мусора.
    
    .PARAMETER Path
        Путь к выходному файлу.
    
    .PARAMETER BlockSize
        Размер одного блока данных в байтах. Можно использовать суффиксы: KB, MB, GB.
        По умолчанию: 64MB
    
    .PARAMETER BlockCount
        Количество блоков для записи.
        По умолчанию: 16
    
    .PARAMETER ShowProgress
        Показывать прогресс выполнения.
        По умолчанию: $true
    
    .EXAMPLE
        New-RandomDataFile -Path "C:\temp\sample.txt" -BlockSize 64MB -BlockCount 16
        
        Создает файл размером 1GB с детальной статистикой производительности.
    
    .NOTES
        Author: AI Assistant
        Version: 4.4
        License: MIT
    #>
    
    [CmdletBinding()]
    param (
        [Parameter(Mandatory = $true, Position = 0)]
        [string]$Path,
        
        [Parameter(Position = 1)]
        [long]$BlockSize = 64MB,
        
        [Parameter(Position = 2)]
        [int]$BlockCount = 16,
        
        [bool]$ShowProgress = $true
    )
    
    begin {
        # Проверка размера блока
        if ($BlockSize -le 0) {
            throw "BlockSize должен быть больше 0"
        }
        
        # Проверка количества блоков
        if ($BlockCount -le 0) {
            throw "BlockCount должен быть больше 0"
        }
        
        # Проверка существования файла - если существует, выход с кодом 2
        if (Test-Path -Path $Path) {
            Write-Error "Файл '$Path' уже существует"
            exit 2
        }
        
        $totalSize = $BlockSize * $BlockCount
        $totalSizeMB = $totalSize / 1MB
        
        Write-Verbose "Создание файла: $Path"
        Write-Verbose "Размер блока: $BlockSize байт ($([math]::Round($BlockSize / 1MB, 2)) MB)"
        Write-Verbose "Количество блоков: $BlockCount"
        Write-Verbose "Общий размер: $totalSize байт ($([math]::Round($totalSizeMB, 2)) MB)"
    }
    
    process {
        $stopwatch = [System.Diagnostics.Stopwatch]::StartNew()
        $startTime = Get-Date
        $buffer = $null  # Объявление для области видимости
        $result = $null  # Инициализация результата
        
        try {
            # Создание потоков для файла и SHA256
            $stream = [System.IO.File]::Create($Path)
            $sha256 = [System.Security.Cryptography.SHA256]::Create()
            $hashStream = New-Object System.Security.Cryptography.CryptoStream($stream, $sha256, "Write")
            
            $random = New-Object System.Security.Cryptography.RNGCryptoServiceProvider
            
            $bytesWritten = 0
            
            # Создание буфера
            $buffer = New-Object byte[] $BlockSize
            
            for ($i = 0; $i -lt $BlockCount; $i++) {
                # Генерация случайных данных для блока
                $random.GetBytes($buffer)
                
                # Запись блока в поток
                $hashStream.Write($buffer, 0, $buffer.Length)
                $bytesWritten += $buffer.Length
                
                # Отображение прогресса с детальной статистикой
                if ($ShowProgress) {
                    $elapsedSeconds = $stopwatch.Elapsed.TotalSeconds
                    $percentComplete = [math]::Min([math]::Round(($bytesWritten / $totalSize) * 100, 2), 100)
                    $writtenMB = $bytesWritten / 1MB
                    $totalMB = $totalSize / 1MB
                    
                    # Расчет текущей скорости
                    $currentSpeed = 0
                    if ($elapsedSeconds -gt 0.1) { # Ждем немного для точности
                        $currentSpeed = ($writtenMB) / $elapsedSeconds
                    }
                    
                    # Расчет ETA (предполагаемое время окончания)
                    $eta = "N/A"
                    $etaDateTime = $null
                    if ($currentSpeed -gt 0) {
                        $remainingMB = $totalMB - $writtenMB
                        $etaSeconds = $remainingMB / $currentSpeed
                        $eta = [math]::Round($etaSeconds, 1)
                        $etaDateTime = (Get-Date).AddSeconds($etaSeconds)
                    }
                    
                    # Формирование строки статуса
                    $status = "Блок $i/$BlockCount | Записано: $([math]::Round($writtenMB, 2)) MB из $([math]::Round($totalMB, 2)) MB"
                    $status += " | Скорость: $([math]::Round($currentSpeed, 2)) MB/сек"
                    
                    if ($etaDateTime) {
                        $etaTimeString = $etaDateTime.ToString("HH:mm:ss")
                        $status += " | ETA: $eta c ($etaTimeString)"
                    } else {
                        $status += " | ETA: вычисление..."
                    }
                    
                    $startTimeString = $startTime.ToString("HH:mm:ss")
                    $status += " | Начало: $startTimeString"
                    
                    Write-Progress -Activity "Генерация случайных данных" -Status $status -PercentComplete $percentComplete
                }
            }
            
            # Завершение операций - критически важный порядок
            $hashStream.Flush()
            $hashStream.Close()  # Это завершает хеширование
            
            # Теперь можно получить хеш
            $hashBytes = $sha256.Hash
            if ($null -eq $hashBytes -or $hashBytes.Length -eq 0) {
                throw "Не удалось вычислить SHA256 хеш"
            }
            
            $sha256Hash = [BitConverter]::ToString($hashBytes).Replace("-", "").ToLower()
            
            # Остановка таймера
            $stopwatch.Stop()
            $durationSeconds = $stopwatch.Elapsed.TotalSeconds
            $endTime = Get-Date
            
            # Расчет финальной производительности
            $speedMBps = $totalSizeMB / $durationSeconds
            
            # Закрытие файлового потока
            $stream.Close()
            
            # Формирование результата
            $result = @{
                FilePath = $Path
                FileSizeBytes = $bytesWritten
                FileSizeMB = $totalSizeMB
                SHA256 = $sha256Hash
                BlockSize = $BlockSize
                BlockCount = $BlockCount
                DurationSeconds = $durationSeconds
                SpeedMBps = $speedMBps
                StartTime = $startTime
                EndTime = $endTime
                Success = $true
            }
            
            return $result
        }
        finally {
            # Остановка таймера если еще работает
            if ($null -ne $stopwatch -and $stopwatch.IsRunning) {
                $stopwatch.Stop()
            }
            
            # === КРИТИЧЕСКИ ВАЖНО: освобождение памяти для буфера ===
            if ($null -ne $buffer) {
                $buffer = $null  # Гарантированное освобождение ссылки
            }
            
            # Принудительная сборка мусора для освобождения памяти
            [GC]::Collect()
            [GC]::WaitForPendingFinalizers()
            
            # Очистка остальных ресурсов в правильном порядке
            if ($null -ne $hashStream) { 
                try { $hashStream.Dispose() } catch { }
            }
            if ($null -ne $stream) { 
                try { $stream.Dispose() } catch { }
            }
            if ($null -ne $sha256) { 
                try { $sha256.Dispose() } catch { }
            }
            if ($null -ne $random) { 
                try { $random.Dispose() } catch { }
            }
            
            # Удаление ссылки на stopwatch (не требует Dispose)
            $stopwatch = $null
            
            # Завершение прогресса
            if ($ShowProgress) {
                Write-Progress -Activity "Генерация случайных данных" -Completed
            }
        }
    }
    
    end {
        # Эта секция не будет выполнена, так как мы возвращаем результат в process
        # Все очистки уже выполнены в finally
    }
}

# Экспорт функции из модуля
Export-ModuleMember -Function New-RandomDataFile
