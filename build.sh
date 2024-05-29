rm -r src/main/scala/smartvector/*
cp -r riscv-vector/src/main/scala/* src/main/scala/smartvector/.
find src/main/scala/smartvector/. -name "*.scala" |xargs -n1 sed -i 's/chipsalliance\.rocketchip\.config/org\.chipsalliance\.cde\.config/g'
find src/main/scala/smartvector/. -name "*.scala" |xargs -n1 sed -i 's/freechips\.rocketchip\.config/org\.chipsalliance\.cde\.config/g'
sudo chmod -R 777 ./src/main/scala/smartvector
sed -i '/object FCMA extends App {/,/^}/d' ./src/main/scala/smartvector/darecreek/exu/fu/fp/fudian/FCMA.scala
sed -i '/object FCMA extends App {/,/^}/d' ./src/main/scala/smartvector/darecreek/exu/vfu/fp/fudian/FCMA.scala
sed -i '/class HellaCacheExceptions extends Bundle {/,/}/d' ./src/main/scala/smartvector/smartVector/RocketIntf.scala
# 在下面两个文件中
# ./src/main/scala/smartvector/darecreek/exu/fu/fp/fudian/FCMA.scala
# ./src/main/scala/smartvector/darecreek/exu/vfu/fp/fudian/FCMA.scala
# 注释对象： object FCMA extends App
 

