@echo off
setlocal EnableExtensions DisableDelayedExpansion
REM configura qui la cartella di xampp
set xamppFolder=C:/xampp

set DefaultFolder=%xamppFolder%/htdocs/Simpler_School/default
set UserFolder=%xamppFolder%/htdocs/Simpler_School/users
set defaultAvatr=defaultAvatar.jpg
set dbAvatar=%DefaultFolder%/defaultAvatar.jpg

if not exist "%defaultAvatr%" ( 
	echo defaultAvatar.jpg non esiste! 
	goto :end
)

if not exist "%xamppFolder%\*" (
	echo %xamppFolder% non esiste. Apri il batch e cambia manualmente la cartella di xampp
	goto :end
)
echo %xamppFolder% is ok
goto :createDefault

:createDefault
	if not exist "%DefaultFolder%\*" (
		md "%DefaultFolder%"
		
		echo copy %defaultAvatr%
		echo to %dbAvatar%
		
		copy "%defaultAvatr%" "%dbAvatar%"
		if errorlevel 1 (
			pause
			goto :EOF
		)
		echo %DefaultFolder% creata
		goto :createUser
	)
	echo %DefaultFolder% gia esistente
	
:createUser
	if not exist "%UserFolder%\*" (
		md "%UserFolder%"
		if errorlevel 1 (
			pause
			goto :EOF
		)
		echo %UserFolder% creata
		goto :end
	)
	echo %UserFolder% gia esistente
	
:end
pause