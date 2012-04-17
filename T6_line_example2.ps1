# Source: http://www.mathgoodies.com/lessons/graphs/line.html
# img: http://www.mathgoodies.com/lessons/graphs/images/line_example2.jpg

$img='T6_line_example2.jpg'
$x=82
$y=61

java test.ImageCli $img $x $y 20 .\T6_line_example2.comp T6_RESULTS.csv

java test.ImageCli $img $x $y 20 .\T6_line_example2.txt
