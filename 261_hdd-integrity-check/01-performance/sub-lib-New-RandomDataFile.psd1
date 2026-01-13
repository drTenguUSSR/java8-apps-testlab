@{
    ModuleVersion = '1.0.0'
    GUID = 'e0613fe4-06c0-4e53-93e7-1de2d69da4d3'
    Author = 'drTenguUSSR'
    CompanyName = 'SeT-34'
    Copyright = '(c) 2026 drTenguUSSR. All rights reserved.'
    
    # Описание модуля
    Description = 'Модуль для генерации файла с случайным содержимым'
    
    # Корневой модуль
    RootModule = 'sub-lib-New-RandomDataFile.psm1'
    
    # Функции для экспорта
    FunctionsToExport = @('New-RandomDataFile')
    
    # Псевдонимы для экспорта
    AliasesToExport = @()
    
    # Переменные для экспорта
    VariablesToExport = @()
    
    # Cmdlets для экспорта
    CmdletsToExport = @()
    
    # Требуемые модули
    RequiredModules = @()
    
    # Требуемая версия PowerShell
    PowerShellVersion = '5.1'
    
    # Типы для обработки
    TypesToProcess = @()
    
    # Форматы для обработки
    FormatsToProcess = @()
    
    # Скрипты для выполнения при импорте модуля
    ScriptsToProcess = @()
    
    # Папки модуля
    ModuleList = @()
    
    # Файлы модуля
    FileList = @('sub-lib-New-RandomDataFile.psm1', 'sub-lib-New-RandomDataFile.psd1')
    
    # Приватные данные
    PrivateData = @{
        PSData = @{
            # Теги для галереи PowerShell
            # Tags = @('Math', 'Factorial', 'Calculation')
            
            # Лицензия URI
            LicenseUri = ''
            
            # Проект URI
            ProjectUri = ''
            
            # URI справки
            HelpInfoUri = ''
            
            # Указано, должен ли этот модуль требовать административных правив
            RequiresAdmin = $false
        }
    }
}
