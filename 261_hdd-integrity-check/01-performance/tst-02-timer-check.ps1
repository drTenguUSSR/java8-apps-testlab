#Set-ExecutionPolicy Bypass -Scope Process -Force
$stopwatch = [System.Diagnostics.Stopwatch]::StartNew()

Write-Host ("Частота таймера: {0} Гц" -f [System.Diagnostics.Stopwatch]::Frequency)
Write-Host ("Разрешение: {0:F9} секунд" -f (1/[System.Diagnostics.Stopwatch]::Frequency))
Write-Host ("IsHighResolution: {0} " -f [System.Diagnostics.Stopwatch]::IsHighResolution)

$stopwatch.Stop()

Write-Host ("Elapsed ticks: {0}" -f $stopwatch.ElapsedTicks)
Write-Host ("Elapsed ms: {0}" -f $stopwatch.ElapsedMilliseconds)
