@echo off

pushd %~dp0\..\..\FileSearcher\FileSearcher
gradlew.bat --console=plain fatJar sourcesJar
popd
