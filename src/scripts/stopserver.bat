@echo off

echo Stopping Kafka servers only...

for /f "tokens=2 delims=," %%i in ('tasklist /FI "IMAGENAME eq java.exe" /FO CSV /NH') do (
    wmic process where "ProcessId=%%~i and CommandLine like '%%kafka%%'" call terminate >nul
)

echo Kafka servers stopped.
pause