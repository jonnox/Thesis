$fname = "multicross.png"

$startingpoints = @(@('39','435','comparedataset1.txt'),@('39','155','comparedataset3.txt'))

for($i = 0; $i -lt $startingpoints.COUNT; $i++)
{java test.ImageCli $fname $startingpoints[$i][0] $startingpoints[$i][1] 20 $startingpoints[$i][2]}

