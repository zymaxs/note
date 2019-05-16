::@echo off&setlocal EnableDelayedExpansion
set localSitePath=d:

:: ======FTP传输======
echo open %1 >ftp.txt
echo user %3>>ftp.txt
echo %4>>ftp.txt
echo bin >>ftp.txt
echo prompt >>ftp.txt
echo mput %2 >>ftp.txt
echo bye >>ftp.txt
echo exit >>ftp.txt
ftp -n -s:"ftp.txt"