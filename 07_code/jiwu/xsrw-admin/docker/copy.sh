#!/bin/sh

# 复制项目的文件到对应docker路径，便于一键生成镜像。
usage() {
	echo "Usage: sh copy.sh"
	exit 1
}


# copy sql
echo "begin copy sql "
cp ../sql/ry_20230223.sql ./mysql/db
cp ../sql/ry_config_20220929.sql ./mysql/db

# copy html
echo "begin copy html "
cp -r ../xsrw-ui/dist/** ./nginx/html/dist


# copy jar
echo "begin copy xsrw-gateway "
cp ../xsrw-gateway/target/xsrw-gateway.jar ./ruoyi/gateway/jar

echo "begin copy xsrw-auth "
cp ../xsrw-auth/target/xsrw-auth.jar ./ruoyi/auth/jar

echo "begin copy xsrw-visual "
cp ../xsrw-visual/xsrw-monitor/target/xsrw-visual-monitor.jar  ./ruoyi/visual/monitor/jar

echo "begin copy xsrw-modules-system "
cp ../xsrw-modules/xsrw-system/target/xsrw-modules-system.jar ./ruoyi/modules/system/jar

echo "begin copy xsrw-modules-file "
cp ../xsrw-modules/xsrw-file/target/xsrw-modules-file.jar ./ruoyi/modules/file/jar

echo "begin copy xsrw-modules-job "
cp ../xsrw-modules/xsrw-job/target/xsrw-modules-job.jar ./ruoyi/modules/job/jar

echo "begin copy xsrw-modules-gen "
cp ../xsrw-modules/xsrw-gen/target/xsrw-modules-gen.jar ./ruoyi/modules/gen/jar

