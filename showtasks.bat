call runcrud.bat
IF "%ERRORLEVEL%" == 0   goto startweb
echo.
echo error occured
pause

:startweb
start chrome "http://localhost:8080/crud/v1/task/tasks"git status
