C:
cd "C:\Program Files (x86)\torcs"

copy .\config\raceman\scenarios\scenario1.xml .\config\raceman\quickrace.xml

for /l %%x in (1, 1, 10) do (
	echo %%x
	wtorcs.exe -T
)

copy .\config\raceman\scenarios\scenario2.xml .\config\raceman\quickrace.xml

for /l %%x in (1, 1, 10) do (
	echo %%x
	wtorcs.exe -T
)

copy .\config\raceman\scenarios\scenario3.xml .\config\raceman\quickrace.xml

for /l %%x in (1, 1, 10) do (
	echo %%x
	wtorcs.exe -T
)

copy .\config\raceman\scenarios\scenario4.xml .\config\raceman\quickrace.xml

for /l %%x in (1, 1, 10) do (
	echo %%x
	wtorcs.exe -T
)

copy .\config\raceman\scenarios\scenario5.xml .\config\raceman\quickrace.xml

for /l %%x in (1, 1, 10) do (
	echo %%x
	wtorcs.exe -T
)

copy .\config\raceman\scenarios\scenario6.xml .\config\raceman\quickrace.xml

for /l %%x in (1, 1, 10) do (
	echo %%x
	wtorcs.exe -T
)

pause