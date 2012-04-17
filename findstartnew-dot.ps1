$fname = "multi-dot.png"

$startingpoints = @(@('38','416','comparedataset1.txt'),@('38','399','comparedataset2.txt'),@('38','149','comparedataset3.txt'),@('38','234','comparedataset4.txt'),@('38','327','comparedataset5.txt'))

for($i = 0; $i -lt $startingpoints.COUNT; $i++)
{java test.ImageCli $fname $startingpoints[$i][0] $startingpoints[$i][1] 20 $startingpoints[$i][2]}

