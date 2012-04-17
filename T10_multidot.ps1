$fname = "T10_multidot.jpg"

$startingpoints = @(@('29','511','T10_a.comp','T10_a.csv','20'),
@('29','495','T10_b.comp','T10_b.csv','20'),
@('29','429','T10_e.comp','T10_e.csv','20'),
@('31','264','T10_c.comp','T10_c.csv','35'),
@('29','259','T10_d.comp','T10_d.csv','20'))

# $startingpoints = @(@('29','429','T10_e.comp','T10_e.csv','20'))

for($i = 0; $i -lt $startingpoints.COUNT; $i++)
{java test.ImageCli $fname $startingpoints[$i][0] $startingpoints[$i][1] $startingpoints[$i][4] $startingpoints[$i][2] $startingpoints[$i][3]}
