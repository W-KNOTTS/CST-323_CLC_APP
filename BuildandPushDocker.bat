@echo off

:MENU
cls
echo Choose an action:
echo 1. Build Docker image
echo 2. Login to Azure Container Registry
echo 3. Push Docker image to Azure Container Registry
echo 4. Delete Azure Container Instance
echo 5. Create Azure Container Instance
echo 6. Exit

set /p choice=Enter your choice: 

if "%choice%"=="1" (
    call :BUILD_DOCKER_IMAGE
) else if "%choice%"=="2" (
    call :LOGIN_TO_ACR
) else if "%choice%"=="3" (
    call :PUSH_DOCKER_IMAGE
) else if "%choice%"=="4" (
    call :DELETE_ACI
) else if "%choice%"=="5" (
    call :CREATE_ACI
) else if "%choice%"=="6" (
    exit
) else (
    echo Invalid choice. Please try again.
    pause
    goto MENU
)

goto :EOF

:BUILD_DOCKER_IMAGE
cls
echo Building Docker image...
docker build -t myapp:latest -f .\create.dockerfile .
pause
goto MENU

:LOGIN_TO_ACR
cls
echo Logging in to Azure Container Registry...
call az acr login --name activity2cst323willk
pause
goto MENU


:PUSH_DOCKER_IMAGE
cls
echo Tagging Docker image...
docker tag myapp:latest activity2cst323willk.azurecr.io/myapp:latest
echo Pushing Docker image to Azure Container Registry...
docker push activity2cst323willk.azurecr.io/myapp:latest
pause
goto MENU


:DELETE_ACI
cls
echo Deleting Azure Container Instance...
call az container delete --resource-group MyResourceGroup --name activity2container --yes
pause
goto MENU


:CREATE_ACI
cls
echo Creating Azure Container Instance...
call az container create ^
    --resource-group MyResourceGroup ^
    --name activity2container ^
    --image activity2cst323willk.azurecr.io/myapp:latest ^
    --cpu 1 ^
    --memory 1 ^
    --registry-login-server activity2cst323willk.azurecr.io ^
    --registry-username activity2cst323willk ^
    --registry-password "qwjtIlWFw56FrjONbysGDQ4gWdAgncx134CxCmp4lT+ACRAjWmpm" ^
    --dns-name-label willkmyapp0324 ^
    --ports 8888 ^
    --environment-variables "DATABASE_URL=jdbc:sqlserver://cst323.database.windows.net:1433;database=cst323;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30" ^
    --environment-variables "DATABASE_USERNAME=willk@cst323" ^
    --environment-variables "DATABASE_PASSWORD=#8675309!abc" ^
    --environment-variables "SERVER_PORT=8888"
pause
goto MENU

