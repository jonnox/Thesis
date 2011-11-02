$bx = 47
$by = 35
$rx = 47
$ry = 122
$gx = 48
$gy = 207

echo $1

java test.ImageCli .\startdata.jpg $rx $ry 20
java test.ImageCli .\startdata.jpg $gx $gy 20
java test.ImageCli .\startdata.jpg $bx $by 20
