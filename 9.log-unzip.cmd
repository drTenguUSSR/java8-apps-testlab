for %%i in (*.zip) do (
mkdir fol-%%i
7z x %%i -ofol-%%i
)
