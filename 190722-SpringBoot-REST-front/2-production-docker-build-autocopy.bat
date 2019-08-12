copy build-build.bat build
copy Dockerfile-build build
cd build
docker build -t roksard/empdb_front -f Dockerfile-build .
pause
cmd