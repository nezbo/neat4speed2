C:
cd "C:\Program Files (x86)\torcs"

copy .\config\raceman\scenarios\scenario6.xml .\config\raceman\quickrace.xml

for /l %%x in (1, 1, 60) do wtorcs.exe -T

pause