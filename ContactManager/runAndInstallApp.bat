@echo "Assembling Contact Organizer Build"
@REM call gradlew clean
call gradlew assembleRelease


md apk
copy app\build\outputs\apk\release\*.apk apk
cd apk
call adb install app-release.apk
