@echo off
setlocal EnableExtensions DisableDelayedExpansion
REM configura qui la cartella di xampp
set xamppFolder=C:/xampp

set DefaultFolder=%xamppFolder%/htdocs/Simpler_School/default
set UserFolder=%xamppFolder%/htdocs/Simpler_School/users
set defaultAvatr=defaultAvatar.jpg
set dbAvatar=%DefaultFolder%/defaultAvatar.jpg

if not exist "%defaultAvatr%" ( 
	echo Cannot find defaultAvatar.jpg! Put the file in the same folder
	goto :end
)

if not exist "%xamppFolder%\*" (
	echo %xamppFolder% doesn't exist
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
		echo %DefaultFolder% created
		goto :createUser
	)
	echo %DefaultFolder% alreay exists
	
:createUser
	if not exist "%UserFolder%\*" (
		md "%UserFolder%"
		if errorlevel 1 (
			pause
			goto :EOF
		)
		echo %UserFolder% created
		goto :end
	)
	echo %UserFolder% alreay exists
	
:end
pause