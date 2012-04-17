$fname = "T9_multi.jpg"

$startingpoints = @(@('29','511','T9_a.comp','T9_a.csv','20'),
@('29','495','T9_b.comp','T9_b.csv','20'),
@('29','429','T9_e.comp','T9_e.csv','20'),
@('31','264','T9_c.comp','T9_c.csv','35'),
@('29','259','T9_d.comp','T9_d.csv','20'))

for($i = 0; $i -lt $startingpoints.COUNT; $i++)
{java test.ImageCli $fname $startingpoints[$i][0] $startingpoints[$i][1] $startingpoints[$i][4] $startingpoints[$i][2] $startingpoints[$i][3]}
