
set NDK=D:\android\android-ndk-r10


@echo off
echo *********************************************************
echo *     请选择编译方式
echo *
echo *     1. 清理后完全编译Release版
echo *     2. 清理后完全编译Debug版
echo *     3. 直接编译Release版
echo *     4. 输出全部编译过程
echo *********************************************************
choice /c:1234

if errorlevel 4 goto BuilDebug
if errorlevel 3 goto Release
if errorlevel 2 goto CleanNDebug
if errorlevel 1 goto CleanNRelease

:CleanNRelease
call %NDK%\ndk-build clean
call %NDK%\ndk-build
goto end

:CleanNDebug
call %NDK%\ndk-build clean
call %NDK%\ndk-build NDK_DEBUG=1
goto end

:Release
call %NDK%\ndk-build
goto end


:BuilDebug
call %NDK%\ndk-build -d
goto end

:end
pause