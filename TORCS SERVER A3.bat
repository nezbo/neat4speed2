C:
cd "C:\Program Files (x86)\torcs"


for /l %%x in (1, 1, 10) do wtorcs.exe -T -r SCENARIOS/scenario1.xml

for /l %%x in (1, 1, 2) do wtorcs.exe -T -r SCENARIOS/scenario6.xml

for /l %%x in (1, 1, 10) do wtorcs.exe -T -r SCENARIOS/scenario2.xml

for /l %%x in (1, 1, 2) do wtorcs.exe -T -r SCENARIOS/scenario6.xml

for /l %%x in (1, 1, 10) do wtorcs.exe -T -r SCENARIOS/scenario3.xml

for /l %%x in (1, 1, 2) do wtorcs.exe -T -r SCENARIOS/scenario6.xml

for /l %%x in (1, 1, 10) do wtorcs.exe -T -r SCENARIOS/scenario4.xml

for /l %%x in (1, 1, 2) do wtorcs.exe -T -r SCENARIOS/scenario6.xml

for /l %%x in (1, 1, 10) do wtorcs.exe -T -r SCENARIOS/scenario5.xml

for /l %%x in (1, 1, 2) do wtorcs.exe -T -r SCENARIOS/scenario6.xml

pause